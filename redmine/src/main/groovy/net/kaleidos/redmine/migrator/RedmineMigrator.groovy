package net.kaleidos.redmine.migrator

import net.kaleidos.taiga.TaigaClient
import net.kaleidos.domain.*
import net.kaleidos.redmine.*
import net.kaleidos.redmine.migrator.*
import com.taskadapter.redmineapi.bean.Project as RedmineProject

import groovy.util.logging.Log4j

@Log4j
class RedmineMigrator {

    RedmineClient redmineClient
    TaigaClient taigaClient
    ProjectMigrator projectMigrator
    IssueMigrator issueMigrator
    WikiMigrator wikiMigrator

    RedmineMigrator(RedmineClient redmineClient, TaigaClient taigaClient) {
        this.taigaClient = taigaClient
        this.redmineClient = redmineClient
        this.projectMigrator = new ProjectMigrator(redmineClient, taigaClient)
        this.issueMigrator = new IssueMigrator(redmineClient, taigaClient)
        this.wikiMigrator = new WikiMigrator(redmineClient, taigaClient)
    }

    public void migrateProject(
        final RedmineProject redmineProject,
        final Closure progressCallBack = { String message, BigDecimal progress = 0.0 -> } ) {

        progressCallBack("Creating project")
        log.debug("MIGRATING ${redmineProject.name}")

        projectMigrator
            .migrateProject(redmineProject)
            .each { RedmineTaigaRef ref ->
                log.debug "Migrating issues from ${ref.redmineIdentifier}"

                progressCallBack("Migrating issues")
                issueMigrator.migrateIssuesByProject(ref)

                log.debug "Migrating wikipages from ${ref.redmineIdentifier}"

                progressCallBack("Migrating wiki pages")
                def possibleWikiPages = wikiMigrator.migrateWikiPagesByProject(ref)

                log.debug("Wiki pages found: ${possibleWikiPages.size()}")

                if (possibleWikiPages) {
                    wikiMigrator.setWikiHomePage(possibleWikiPages)
                }
            }

        log.debug("PROJECT ${redmineProject.name} SUCCESSFULLY MIGRATED")

    }

}
