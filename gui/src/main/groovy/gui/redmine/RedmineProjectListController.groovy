package gui.redmine

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

import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory
import com.taskadapter.redmineapi.bean.Project

import gui.settings.SettingsService
import gui.controller.DefaultActionViewControllerWorker

import net.kaleidos.redmine.RedmineClientFactory
import groovy.util.logging.Log4j

@Log4j
class RedmineProjectListController extends DefaultActionViewControllerWorker<Project> {

    @Override
    void preHandlingView(ViewContainer view, ActionEvent event) {
        updateStatus("Loading list...", 50)
        def projectsView = getRedmineProjectListView()

        if (!projectsView) {
            projectsView = new RedmineProjectListView()
            viewManager.addView(projectsView , PerspectiveConstraint.RIGHT)
        }

        projectsView.model.clear()
        view.rootPane.glassPane.visible = true
    }

    ViewContainer getRedmineProjectListView() {
        return locate(RedmineProjectListView).named(RedmineProjectListView.ID)
    }

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        def service = new SettingsService()
        def settings = service.loadSettings()
        def areUp = service.areServicesUp(settings.redmineUrl)

        if (areUp) {
        try {
            def redmineClient  =
                RedmineClientFactory.newInstance(settings.properties)

        def projects = null
            projects = redmineClient.findAllProject()

            publish(projects)

        } catch(e) {
            log.error(e.message)
        }
        }

    }

    @Override
    void handleViewPublising(ViewContainer view, ActionEvent event, List<Project> chunks) {
        redmineProjectListView.model.addAll(chunks.flatten())
    }

    @Override
    void postHandlingView(ViewContainer viewContainer, ActionEvent event) {
        viewContainer.rootPane.glassPane.visible = false
        def rows = redmineProjectListView.model.rowCount

        if (!rows) {
            setMigrationButtonEnabled(false)
            updateStatus("No results found", 0)
            return
        }

        setMigrationButtonEnabled(true)
        updateStatus("Showing $rows Redmine projects ", 0)
    }

    void setMigrationButtonEnabled(boolean enabled) {
        migratedSelectedButton.setEnabled(enabled)
    }

    void updateStatus(String message, Integer progress) {
        statusBarProgressBar.value = progress
        statusBarMessageLabel.text = message
    }

    JButton getMigratedSelectedButton() {
        return find(JButton).in(redmineProjectListView).named('migrateSelected')
    }

    JProgressBar getStatusBarProgressBar() {
       return find(JProgressBar).in(viewManager.rootView).named(StatusBar.STATUS_BAR_NAME)
    }

    JLabel getStatusBarMessageLabel() {
        return find(JLabel).in(viewManager.rootView).named(StatusBar.LEFT_PANEL_LABEL)
    }

}
