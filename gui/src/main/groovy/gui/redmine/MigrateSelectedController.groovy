package gui.redmine

import static org.viewaframework.util.ComponentFinder.find

import javax.swing.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import groovy.util.logging.Log4j

import org.viewaframework.view.*
import org.viewaframework.util.*
import org.viewaframework.controller.*
import org.viewaframework.view.perspective.*
import org.viewaframework.widget.view.*

import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory
import com.taskadapter.redmineapi.bean.Project

import gui.migration.MigrationProgress
import gui.migration.MigrationProgressView
import gui.controller.DefaultViewControllerWorker

@Log4j
class MigrateSelectedController extends
    DefaultViewControllerWorker<ActionListener, ActionEvent, String, MigrationProgress> {

    @Override
    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

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

        selectedProjectList.eachWithIndex { Project p, index ->
            Thread.sleep(1000)
            log.debug("Migrating project ${p.name}")
            publish(
                new MigrationProgress(
                    project: p,
                    progress: index.div(total)
                )
            )
        }

        Thread.sleep(1000)
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
            loggingProgress.text = "Migration Finished!!!"
            progressBar.setValue(100)
            return
        }

        loggingProgress.text = "Migrating project: ${migrationProgress.project.name}"
        progressBar.setValue((migrationProgress.progress * 100).intValue())

    }

}
