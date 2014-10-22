package net.kaleidos.redmine.migrator

import spock.lang.Unroll
import spock.lang.Specification

import net.kaleidos.redmine.RedmineTaigaRef
import net.kaleidos.redmine.MigratorToTaigaSpecBase
import net.kaleidos.redmine.testdata.WikiDataProvider
import net.kaleidos.redmine.testdata.ProjectDataProvider

import net.kaleidos.taiga.TaigaClient
import net.kaleidos.redmine.RedmineClient

import net.kaleidos.domain.Wikipage as TaigaWikiPage
import com.taskadapter.redmineapi.bean.WikiPage as RedmineWikiPage

class WikiMigratorSpec extends MigratorToTaigaSpecBase {

    @Delegate ProjectDataProvider projectDataProvider = new ProjectDataProvider()
    @Delegate WikiDataProvider wikiDataProvider = new WikiDataProvider()

    void setup() {
        deleteTaigaProjects()
    }

    void 'migrating project wikies'() {
        setup: 'building a simple migrator instance'
            RedmineClient redmineClient = buildWikiRedmineClient()
            TaigaClient taigaClient = createTaigaClient()
            ProjectMigrator projectMigrator = new ProjectMigrator(redmineClient, taigaClient)
            WikiMigrator wikiMigrator = new WikiMigrator(redmineClient, taigaClient)
        when: 'trying to migrate basic estructure of a redmine project'
            RedmineTaigaRef migratedProjectInfo = projectMigrator.migrateProject(buildRedmineProject())
            List<TaigaWikiPage> wikiPages = wikiMigrator.migrateWikiPagesByProject(migratedProjectInfo)
            TaigaWikiPage homeWikiPage = wikiMigrator.setWikiHomePage(wikiPages)
        and: 'checking migration went ok'
            assert homeWikiPage
            assert homeWikiPage.slug == 'home'
            assert wikiPages.size() == 5
            assert wikiPages.count { it.slug.toLowerCase() == 'home' } == 0
        and: 'try to save again the home page'
            wikiMigrator.setWikiHomePage(wikiPages)
        then: 'home cant be repeated'
            thrown(Exception)
    }

    RedmineClient buildWikiRedmineClient() {
        return Stub(RedmineClient) {
            // To create a project
            findAllMembershipByProjectIdentifier(_) >> buildRedmineMembershipList()
            findUserFullById(_) >> { Integer id -> buildRedmineUser("${randomTime}") }
            findAllTracker() >> buildRedmineTrackerList()
            findAllIssueStatus() >> buildRedmineStatusList()
            findAllIssuePriority() >> buildRedmineIssuePriorityList()
            // To create wiki pages
            findAllWikiPageByProjectIdentifier(_) >> buildWikiPageSummaryList()
            findCompleteWikiPageByProjectIdentifierAndTitle(_,_) >> { String projectRef, String title ->
                buildFullWikiPageByTitle(title)
            }
        }
    }

    def 'check how works header transformer'() {
        given: 'a wiki text with headers in form h1.'
            def wikiContent = """
                h1.header1
                h2.header2
                h3.header3
                h4.header4
                h5.header5
            """
        when: 'applying fix'
            def result =
                new WikiMigrator(null, null)
                    .fixContentHeaders(wikiContent)
        then: 'headers should have changed'
            result.contains("#header1")
            result.contains("##header2")
            result.contains("###header3")
            result.contains("####header4")
            result.contains("#####header5")

    }

    @Unroll
    def 'check how works the content transformer'() {
        when: 'applying patch'
            def result =
                new WikiMigrator(null, null)
                    .fixContent(sample)
        then: 'checking result'
            result == expected
        where: 'possible inputs are'
                    sample                             |  expected
            '[[Configuraci\u00F3n del Entorno]]'       |  '[[configuracion_del_entorno]]'
            '[[ Enlaces a prototipos ]]'               |  '[[_enlaces_a_prototipos_]]'
            '[[Enlaces a prototipos ]]'                |  '[[enlaces_a_prototipos_]]'
            '[[Enlaces a prototipos]]'                 |  '[[enlaces_a_prototipos]]'
            '[[NombrePagia |Enlaces a prototipos]]'    |  '[[nombrepagia_|Enlaces a prototipos]]'
            '[[ NombrePagia|Enlaces a prototipos]]'    |  '[[_nombrepagia|Enlaces a prototipos]]'
            '[[NombrePagia|Enlaces a prototipos]]'     |  '[[nombrepagia|Enlaces a prototipos]]'
            '[[ NombrePagia |Enlaces a prototipos]]'   |  '[[_nombrepagia_|Enlaces a prototipos]]'
            '[[ Nombre Pagia |Enlaces a prototipos]]'  |  '[[_nombre_pagia_|Enlaces a prototipos]]'
            '[[ Nombre Pagia |Enlaces a prototipos]]'  |  '[[_nombre_pagia_|Enlaces a prototipos]]'
    }

}
