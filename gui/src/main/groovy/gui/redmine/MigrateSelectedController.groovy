package gui.redmine

import static org.viewaframework.util.ComponentFinder.find

import javax.swing.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import groovy.util.logging.Log4j

import org.viewaframework.view.*
import org.viewaframework.util.*
import org.viewaframework.view.perspective.*
import org.viewaframework.widget.view.*

import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory
import com.taskadapter.redmineapi.bean.Project

import gui.swingx.JXBusyFeedbackLabel
import gui.settings.SettingsService
import gui.migration.MigrationProgress
import gui.migration.MigrationProgressView
import gui.controller.DefaultActionViewControllerWorker

import net.kaleidos.taiga.TaigaClient
import net.kaleidos.redmine.RedmineClientFactory
import net.kaleidos.redmine.migrator.RedmineMigrator

@Log4j
class MigrateSelectedController extends DefaultActionViewControllerWorker<MigrationProgress> {

    @Override
    void preHandlingView(ViewContainer view, ActionEvent event) {
        viewManager.addView(new MigrationProgressView())
    }

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        List<Project> selectedProjectList =
            locate(ProjectListView.ID)
                .model
                .selectedObjects
        def total = selectedProjectList.size()
        def settings = new SettingsService().loadSettings()

        def redmineClient =
            RedmineClientFactory.newInstance(
                settings.redmineUrl,
                settings.redmineApiKey)
        def taigaClient =
            new TaigaClient(settings.taigaUrl)
                .authenticate(
                    settings.taigaUsername,
                    settings.taigaPassword)
        def migrator = new RedmineMigrator(redmineClient, taigaClient)

        try { // TODO viewa postHandlingOnError doesnt work
            selectedProjectList.eachWithIndex { Project p, index ->
                publish(
                    new MigrationProgress(
                        projectName: p.name,
                        progress: index.div(total)
                    )
                )
                migrator.migrateProject(p)
            }

            publish(new MigrationProgress(progress:1.0))
        } catch(Throwable th) {
            publish(
                new MigrationProgress(
                    exception: th,
                    progress:0.0
                )
            )
        }


    }

    @Override
    void handleViewPublising(ViewContainer view, ActionEvent event, List<MigrationProgress> chunks) {
        def migrationProgress = chunks.first()
        def progressView = locate(MigrationProgressView).named(MigrationProgressView.ID)

        def progressBar = find(JProgressBar).in(progressView).named('migrationProgressBar')
        def loggingProgress = find(JLabel).in(progressView).named('loggingProgress')
        def closeButton = find(JButton).in(progressView).named('closeButton')
        def busyLabel = find(JXBusyFeedbackLabel).in(progressView).named('outputIconLabel')

        if (migrationProgress.exception) {
            log.error("Exception while migrating: ${migrationProgress.exception.message}")
            closeButton.enabled = true
            loggingProgress.text = "Migration Failed!!! Please check log"
            progressBar.setValue(100)
            busyLabel.setFailure()
            return
        }

        if (migrationProgress.progress.intValue() == 1) {
            closeButton.enabled = true
            loggingProgress.text = "Migration Finished!!!"
            progressBar.setValue(100)
            busyLabel.setSuccess()
            return
        }

        loggingProgress.text = "Migrating project: ${migrationProgress.projectName}"
        progressBar.setValue((migrationProgress.progress * 100).intValue())

    }

}
