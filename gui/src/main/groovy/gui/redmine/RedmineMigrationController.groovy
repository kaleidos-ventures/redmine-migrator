package gui.redmine

import static org.viewaframework.util.ComponentFinder.find

import javax.swing.*
import java.awt.event.ActionEvent
import groovy.util.logging.Log4j

import org.viewaframework.view.*
import org.viewaframework.util.*
import org.viewaframework.view.perspective.*
import org.viewaframework.widget.view.*

import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory
import com.taskadapter.redmineapi.bean.Project

import gui.settings.Settings
import gui.settings.SettingsService
import gui.swingx.JXBusyFeedbackLabel
import gui.migration.MigrationProgress
import gui.migration.MigrationProgressView
import gui.controller.MigrationProgressAwareController

import net.kaleidos.taiga.TaigaClient
import net.kaleidos.redmine.RedmineClientFactory
import net.kaleidos.redmine.migrator.RedmineMigrator

@Log4j
class RedmineMigrationController extends MigrationProgressAwareController {

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        log.debug("Getting selected objects")
        def selectedProjectList =
            locate(RedmineProjectListView.ID)
                .model
                .selectedObjects
        def total = selectedProjectList.size()
        def service = new SettingsService()
        def settings = service.loadSettings()

        log.debug("Migrating $total projects")

        if (!service.areServicesUp(settings.redmineUrl, settings.taigaUrl)) {
            publishFailure(new Exception("Please check your connections!!"))
            return
        }


        try {
            def migrator = buildMigratorWithSettings(settings)

            selectedProjectList.eachWithIndex { Project project, index ->
                migrator.migrateProject(
                    project,
                    progressClosure(project.name, (index + 1).div(total + 1))
                )
            }

            publishSuccess()

        } catch(Throwable th) {
           publishFailure(th)
        }

    }

    RedmineMigrator buildMigratorWithSettings(Settings settings) {
        log.debug("Configuring redmine client")
        def redmineClient =
            RedmineClientFactory.newInstance(settings.properties)

        log.debug("Configuring Taiga client")
        def taigaClient =
            new TaigaClient(settings.taigaUrl)
                .authenticate(
                    settings.taigaUsername,
                    settings.taigaPassword)

        log.debug("Building Redmine Migrator")
        return new RedmineMigrator(redmineClient, taigaClient)
    }

    Closure<Void> progressClosure = { final String projectName, final BigDecimal overallProgress ->
        return { String message, BigDecimal progress = overallProgress ->
             publish(
                new MigrationProgress(
                    projectName: projectName,
                    message: message,
                    progress: progress
                )
            )
        }
    }

    String buildMessage(MigrationProgress migrationProgress) {
        if (!migrationProgress.projectName) {
            return migrationProgress.message
        }

        return "${migrationProgress.projectName} : ${migrationProgress.message}"
    }

}
