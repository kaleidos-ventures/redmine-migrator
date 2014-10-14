package net.kaleidos.redmine.migrator

import com.taskadapter.redmineapi.bean.Attachment as RedmineAttachment
import com.taskadapter.redmineapi.bean.Issue as RedmineIssue
import com.taskadapter.redmineapi.bean.Journal as RedmineHistory
import com.taskadapter.redmineapi.bean.User as RedmineUser
import com.taskadapter.redmineapi.internal.Transport.Pagination

import groovy.util.logging.Log4j
import groovy.transform.InheritConstructors

import net.kaleidos.domain.Attachment as TaigaAttachment
import net.kaleidos.domain.History as TaigaHistory
import net.kaleidos.domain.Issue as TaigaIssue
import net.kaleidos.domain.Project as TaigaProject
import net.kaleidos.domain.User as TaigaUser
import net.kaleidos.redmine.RedmineTaigaRef

@Log4j
@InheritConstructors
class IssueMigrator extends AbstractMigrator<TaigaIssue> {

    final String SEVERITY_NORMAL = 'Normal'

    List<TaigaIssue> migrateIssuesByProject(final RedmineTaigaRef ref, Closure<Void> feedback = {}) {
        Iterator<Pagination<TaigaIssue>> issueIterator =
            redmineClient.findAllIssueByProjectIdentifier(ref.redmineIdentifier)

        Closure<Boolean> thereIsNext = { Pagination pagination -> pagination.list }
        Closure<Void> keepPosted = { Pagination p->
            feedback("Processing issues ${p.offset}-${p.offset + p.list.size()} / ${p.total}")
        }

        log.debug("Migrating issues from Redmine project ${ref.redmineIdentifier}")

        return issueIterator
            .takeWhile(thereIsNext) // issueIterator is an infinite stream
            .collect { Pagination pagination ->
                pagination.with(keepPosted) // keeping the UI informed
                pagination
                    .list
                    .collect(
                        this.&populateIssue >>
                        this.&addRedmineIssueToTaigaProject.rcurry(ref.project) >>
                        this.&save)
            }
            .flatten() // return all processed issues
    }

    RedmineIssue populateIssue(final RedmineIssue basicIssue) {
        return redmineClient.findIssueById(basicIssue.id)
    }

    TaigaIssue addRedmineIssueToTaigaProject(
        final RedmineIssue source,
        final TaigaProject taigaProject) {

        RedmineUser user =
            redmineClient.findUserFullById(source.author.id)

        return new TaigaIssue(
            ref: source.id,
            project: taigaProject,
            type: source.tracker.name,
            status: source.statusName,
            priority: source.priorityText,
            severity: SEVERITY_NORMAL,
            subject: source.subject,
            description: source.with { description ?: subject },
            createdDate: source.createdOn,
            owner: user.mail,
            attachments: extractIssueAttachments(source),
            history: extractIssueHistory(source)
        )

    }

    List<TaigaAttachment> extractIssueAttachments(final RedmineIssue issue) {
        log.debug("Adding ${issue.attachments.size()} attacments to issue ${issue.subject}")

        def attachments =
            issue
                .attachments
                .collect(executeSafelyAndWarn(this.&convertToTaigaAttachment))
                .findAll { it } // TODO replace with findResults

        return attachments
    }

    def executeSafelyAndWarn = { action ->
        return { source ->
            try {
                action(source)
            } catch(Throwable e) {
                log.warn(e.message)
            }
        }
    }

    TaigaAttachment convertToTaigaAttachment(RedmineAttachment att) {
        RedmineUser user = redmineClient.findUserFullById(att.author.id)

        return new TaigaAttachment(
            data: new URL(att.contentURL).bytes.encodeBase64(),
            name: att.fileName,
            description: att.description,
            createdDate: att.createdOn,
            owner: user.mail
        )

    }

    List<TaigaHistory> extractIssueHistory(final RedmineIssue issue) {
        //We are only interested in migrating those historic entries
        //containing notes, not data changes entries
        def comments =
            issue
                .journals
                .findAll { it.notes }
                .collect(this.&convertToTaigaHistory)

        return comments
    }

    TaigaHistory convertToTaigaHistory(RedmineHistory journal) {
        RedmineUser redmineUser = redmineClient.findUserFullById(journal.user.id)
        TaigaUser taigaUser =
            new TaigaUser(
                name: redmineUser.fullName,
                email: redmineUser.mail)

        return new TaigaHistory(
            user: taigaUser,
            createdAt: journal.createdOn,
            comment: journal.notes
        )
    }

    @Override
    TaigaIssue save(TaigaIssue issue) {
        log.debug("Saving issue from -${issue.project.name}- : ${issue.subject} ")
        return taigaClient.createIssue(issue)
    }

}
