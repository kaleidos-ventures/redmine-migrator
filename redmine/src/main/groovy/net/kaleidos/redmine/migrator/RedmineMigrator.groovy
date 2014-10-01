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

    public void migrateProject(RedmineProject redmineProject) {

        log.debug("*" * 30)
        log.debug("*" * 30)
        log.debug("DELETING ALL PROYECTS | "* 2)
        log.debug("*" * 30)
        log.debug("*" * 30)
        taigaClient.with {
            projects.each { p ->
                log.debug "Deleting project '${p.name}' with id ${p.id}"
                deleteProject(new Project(id:p.id))
            }
        }

        log.debug("*" * 30)
        log.debug("*" * 30)
        log.debug("MIGRATING ${redmineProject.name}")
        log.debug("*" * 30)
        log.debug("*" * 30)

        projectMigrator
            .migrateProject(redmineProject)
            .collect { RedmineTaigaRef ref ->
                try {
                    log.debug "Migrating issues from ${ref.redmineIdentifier}"
                    issueMigrator.migrateIssuesByProject(ref)

                    log.debug "Migrating wikipages from ${ref.redmineIdentifier}"
                    wikiMigrator.setWikiHomePage(wikiMigrator.migrateWikiPagesByProject(ref))
                } catch (e) {
                    log.error "Error while migrating: ${ref.redmineIdentifier}"
                }
                // Los proyectos que se devuelvan son los que estan migrados
                // completamente
                return ref
            }

        log.debug("*" * 30)
        log.debug("*" * 30)
        log.debug("PROJECT ${redmineProject.name} SUCCESSFULLY MIGRATED")
        log.debug("*" * 30)
        log.debug("*" * 30)

    }

    static TaigaClient createTaigaClient(String specialUser) {
        def config =
            new ConfigSlurper()
                .parse(new File("src/test/resources/taiga${specialUser ? '_' + specialUser : ''}.groovy").text)
        def client = new TaigaClient(config.host)

        return client.authenticate(config.user, config.passwd)
    }

    static RedmineClient createRedmineClient() {
        def config =
            new ConfigSlurper()
                .parse(new File('src/test/resources/redmine.groovy').text)
        def client =
            RedmineClientFactory.newInstance(config.host, config.apiKey)

        return client
    }




}
