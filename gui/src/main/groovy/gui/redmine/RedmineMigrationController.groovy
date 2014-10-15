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
import gui.controller.DefaultActionViewControllerWorker

import net.kaleidos.taiga.TaigaClient
import net.kaleidos.redmine.RedmineClientFactory
import net.kaleidos.redmine.migrator.RedmineMigrator

@Log4j
class RedmineMigrationController extends DefaultActionViewControllerWorker<MigrationProgress> {

    @Override
    void preHandlingView(ViewContainer view, ActionEvent event) {
        viewManager.removeView(view)
        viewManager.addView(new MigrationProgressView())
    }

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

        def migrator = buildMigratorWithSettings(settings)

        try {

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

    @Override
    void handleViewPublising(ViewContainer view, ActionEvent event, List<MigrationProgress> chunks) {
        MigrationProgress migrationProgress = chunks.first()
        setProgressFeedback(migrationProgress)

        if (migrationProgress.exception) {
            log.error("Exception while migrating: ${migrationProgress.exception.message}")
            busyLabel.setFailure()
            return
        }

        if (migrationProgress.progress >= 1) {
            busyLabel.setSuccess()
            return
        }

    }

    @Override
    void postHandlingView(ViewContainer view, ActionEvent event) {
        closeButton.enabled = true
    }

    void publishSuccess() {
        publish(
            new MigrationProgress(
                message:"Migration Finished Successfully",
                progress:1.0
            )
        )
    }

    void publishFailure(Throwable th) {
        publish(
            new MigrationProgress(
                exception: th,
                message: "Migration Failed!!! Please check log",
                progress: 1.0
            )
        )
    }

    RedmineMigrator buildMigratorWithSettings(Settings settings) {
        def redmineClient =
            RedmineClientFactory.newInstance(
                settings.redmineUrl,
                settings.redmineApiKey)
        def taigaClient =
            new TaigaClient(settings.taigaUrl)
                .authenticate(
                    settings.taigaUsername,
                    settings.taigaPassword)

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

    void setProgressFeedback(MigrationProgress migrationProgress) {
        progressMessageLabel.text = buildMessage(migrationProgress)
        migrationProgressBar.setValue((migrationProgress.progress * 100).intValue())
    }

    String buildMessage(MigrationProgress migrationProgress) {
        if (!migrationProgress.projectName) {
            return migrationProgress.message
        }

        return "${migrationProgress.projectName} : ${migrationProgress.message}"
    }

    JXBusyFeedbackLabel getBusyLabel() {
        return find(JXBusyFeedbackLabel).in(progressView).named('outputIconLabel')
    }

    JButton getCloseButton() {
        return find(JButton).in(progressView).named('closeButton')
    }

    JProgressBar getMigrationProgressBar() {
        return find(JProgressBar).in(progressView).named('migrationProgressBar')
    }

    JLabel getProgressMessageLabel() {
        return find(JLabel).in(progressView).named('loggingProgress')
    }

    MigrationProgressView getProgressView() {
        return locate(MigrationProgressView).named(MigrationProgressView.ID)
    }

}
