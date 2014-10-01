package gui.taiga

import static org.viewaframework.util.ComponentFinder.find

import org.jdesktop.swingx.JXList

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.JLabel
import javax.swing.JButton
import javax.swing.JTextField
import javax.swing.JProgressBar
import javax.swing.DefaultListModel

import org.viewaframework.view.*
import org.viewaframework.util.*
import org.viewaframework.controller.*
import org.viewaframework.view.perspective.*
import org.viewaframework.widget.view.*

import gui.taiga.TaigaProjectListView
import gui.settings.SettingsService
import gui.exception.ExceptionView
import gui.migration.MigrationProgress
import gui.migration.MigrationProgressView
import gui.controller.DefaultViewControllerWorker

import groovy.util.logging.Log4j

import net.kaleidos.domain.Project
import net.kaleidos.taiga.TaigaClient

@Log4j
class DeleteTaigaProjectController extends
    DefaultViewControllerWorker<ActionListener, ActionEvent, String, Project> {

    @Override
    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

    @Override
    void preHandlingView(ViewContainer view, ActionEvent event) {
        viewManager.removeView(view)
        viewManager.addView(new MigrationProgressView())
    }

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        def model = locate(TaigaProjectListView.ID).model
        def selectedProjectList = model.selectedObjects

        log.debug('Deleting selected projects')
        def total = selectedProjectList?.size()
        def settings = new SettingsService().loadSettings()
        def taigaClient =
            new TaigaClient(settings.taigaUrl)
                .authenticate(
                    settings.taigaUsername,
                    settings.taigaPassword)

        selectedProjectList.eachWithIndex { Map p, index ->
            log.debug("deleting ${p.name}")
            publish(
                new MigrationProgress(
                    projectName: p.name,
                    progress: index.div(total)
                )
            )
            taigaClient.deleteProject(new Project(id:p.id, name:p.name))
            log.debug("project ${p.name} deleted")
        }
        log.debug("Updating list")
        model.addAll(taigaClient.projects)
        log.debug("All selected projects deleted")

        publish(new MigrationProgress(progress:1.0))
    }

    @Override
    void handleViewPublising(ViewContainer view, ActionEvent event, List<MigrationProgress> chunks) {
        def migrationProgress = chunks.first()
        def progressView = locate(MigrationProgressView).named(MigrationProgressView.ID)

        def progressBar = find(JProgressBar).in(progressView).named('migrationProgressBar')
        def loggingProgress = find(JLabel).in(progressView).named('loggingProgress')
        def closeButton = find(JButton).in(progressView).named('closeButton')

        if (migrationProgress.progress.intValue() == 1) {
            closeButton.enabled = true
            loggingProgress.text = "Task Finished!!!"
            progressBar.setValue(100)
            return
        }

        loggingProgress.text = "Deleting project: ${migrationProgress.projectName}"
        progressBar.setValue((migrationProgress.progress * 100).intValue())

    }

}
