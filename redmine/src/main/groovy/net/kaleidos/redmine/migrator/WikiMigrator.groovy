package net.kaleidos.redmine.migrator

import com.taskadapter.redmineapi.bean.WikiPage as RedmineWikiPage
import com.taskadapter.redmineapi.bean.WikiPageDetail as RedmineWikiPageDetail

import groovy.util.logging.Log4j
import groovy.transform.InheritConstructors

import java.text.Normalizer
import java.util.regex.Pattern

import net.kaleidos.domain.Wikipage as TaigaWikiPage
import net.kaleidos.domain.Attachment as TaigaAttachment
import net.kaleidos.redmine.RedmineTaigaRef

@Log4j
@InheritConstructors
class WikiMigrator extends AbstractMigrator<TaigaWikiPage> {

    static final String REGEX1 = /\[\[(\s*\p{L}+(?:\s+\p{L}+)*\s*)\]\]/
    static final String REGEX2 = /\[\[(\s*\p{L}+(?:\s+\p{L}+)*\s*)\|[^\]]*\]\]/
    static final String EMPTY = ''
    static final String BLANK = ' '
    static final Pattern NORMALIZE_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")

    List<TaigaWikiPage> migrateWikiPagesByProject(final RedmineTaigaRef ref) {
        log.debug("Migrating wiki pages from: '${ref.project.name}'")

        def safeWorkflow =
            executeSafelyAndWarn(
                this.&getCompleteRedmineWikiPageFrom.rcurry(ref) >>
                this.&createTaigaWikiPage.rcurry(ref) >>
                this.&save)

        return findAllWikiPagesFromProject(ref.redmineIdentifier).findResults(safeWorkflow)
    }

    List<RedmineWikiPage> findAllWikiPagesFromProject(String identifier) {
        return redmineClient.findAllWikiPageByProjectIdentifier(identifier)
    }

    TaigaWikiPage createTaigaWikiPage(
        final RedmineWikiPage redmineWikiPage,
        final RedmineTaigaRef redmineTaigaRef) {
        log.debug("Building taiga wiki page: '${redmineWikiPage.title}'")

        return new TaigaWikiPage(
            slug: redmineWikiPage.title,
            content: fixContent(redmineWikiPage.text),
            project: redmineTaigaRef.project,
            attachments: extractWikiAttachments(redmineWikiPage)
        )
    }

    Closure<String> fixContent = this.&fixContentHeaders >> this.&fixContentLinks

    String fixContentHeaders(String content) {
        return (1..5).inject(content) { acc , n -> acc.replaceAll("h${n}.", ("#" * n)) }
    }

    String fixContentLinks(String content) {
        return content
            .replaceAll(REGEX1, regexSustitution)
            .replaceAll(REGEX2, regexSustitution)
    }

    Closure<String> regexSustitution = { Object[] it ->
        def all = it[0]
        def group1 = it[1]
        // replacing spaces in $1
        def sustitution =
            // Taiga changes accents
            normalize(
                group1
                    // forcing underscore
                    .replaceAll(BLANK,"_")
                    // Taiga converts slugs to lowercase
                    .toLowerCase())
        // replacing grupo 1 with group 0 replacement
        return all.replace(group1, sustitution)
    }

    String normalize(String source) {
        String normalizedString =
            Normalizer.normalize(source, Normalizer.Form.NFD)

        return NORMALIZE_PATTERN
            .matcher(normalizedString)
            .replaceAll(EMPTY)
    }

    List<TaigaAttachment> extractWikiAttachments(final RedmineWikiPage wiki) {
        log.debug("Adding ${wiki.attachments?.size()} attachments to wiki ${wiki.title}")

        def attachments =
            wiki
                .attachments
                .collect(executeSafelyAndWarn(this.&convertToTaigaAttachment))
                .findAll { it } // TODO replace with findResults

        return attachments
    }

    RedmineWikiPageDetail getCompleteRedmineWikiPageFrom(
        final RedmineWikiPage redmineWikiPage,
        final RedmineTaigaRef redmineTaigaRef) {
        log.debug("Getting complete redmine wiki page: '${redmineWikiPage.title}'")

        return redmineClient
            .findCompleteWikiPageByProjectIdentifierAndTitle(
            redmineTaigaRef.redmineIdentifier,
            redmineWikiPage.title
        )
    }

    @Override
    TaigaWikiPage save(TaigaWikiPage wikipage) {
        log.debug("Trying to save wiki page: ${wikipage.slug}")

        return taigaClient.createWiki(wikipage)
    }

    TaigaWikiPage setWikiHomePage(final List<TaigaWikiPage> alreadySavedWikiPages) {
        log.debug("Looking for wiki home")

        TaigaWikiPage home =
            alreadySavedWikiPages.find(wikiPageWithHomeName) ?:
                saveAlternativeWikiHome(alreadySavedWikiPages)

        return home
    }

    Closure<Boolean> wikiPageWithHomeName = { it.slug.toLowerCase() == 'home' }

    TaigaWikiPage saveAlternativeWikiHome(final List<TaigaWikiPage> alreadySavedWikiPages) {
        log.debug("No wiki home found in [${alreadySavedWikiPages?.size()}] pages...looking for an alternative")

        TaigaWikiPage alternative =
            alreadySavedWikiPages.find(byWikiTitle) ?:
                alreadySavedWikiPages.sort(byOldest).first()

        log.debug("Wiki alternative home found: ['${alternative.slug}']")

        return save(
            new TaigaWikiPage(
                slug: 'home',
                content: fixContent(alternative.content),
                project: alternative.project,
                owner: alternative.owner,
                createdDate: alternative.createdDate,
                attachments: alternative.attachments
            )
        )
    }

    Closure<Boolean> byWikiTitle = filteringBySlugToLowerCase('wiki')
    Closure<Boolean> byOldest = inAscendingOrderBy('createdDate')

    Closure<Boolean> filteringBySlugToLowerCase(String title) {
        return { it.slug.toLowerCase() == title }
    }

    Closure<Boolean> inAscendingOrderBy(String field) {
        return { it."$field" }
    }

}
