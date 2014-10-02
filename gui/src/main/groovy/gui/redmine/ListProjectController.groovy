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

import gui.exception.ExceptionView
import gui.settings.SettingsService
import gui.controller.DefaultViewControllerWorker

class ListProjectController extends
    DefaultViewControllerWorker<ActionListener, ActionEvent, String, Project> {

    @Override
    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

    @Override
    void preHandlingView(ViewContainer view, ActionEvent event) {
        updateStatus("Loading list...", 50)
        def projectsView = getProjectListView()

        if (!projectsView) {
            projectsView = new ProjectListView()
            viewManager.addView(projectsView , PerspectiveConstraint.RIGHT)
        }

        projectsView.model.clear()
        view.rootPane.glassPane.visible = true
    }

    ViewContainer getProjectListView() {
        return locate(ProjectListView).named(ProjectListView.ID)
    }

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        def settings = new SettingsService().loadSettings()
        def redmineManager =
            new RedmineManager(
                settings.redmineUrl,
                settings.redmineApiKey)

        publish(redmineManager.projects)
    }

    @Override
    void handleViewPublising(ViewContainer view, ActionEvent event, List<Project> chunks) {
        projectListView.model.addAll(chunks.flatten())

        view.rootPane.glassPane.visible = false
    }

    @Override
    void postHandlingView(ViewContainer viewContainer, ActionEvent event) {
        def rows = projectListView.model.rowCount

        if (!rows) {
            setMigrationButtonEnabled(false)
            updateStatus("No results found", 0)
            return
        }

        setMigrationButtonEnabled(true)
        updateStatus("Showing $rows Redmine projects ", 0)
    }

    ProjectListView getProjectListView() {
        locate(ProjectListView).named(ProjectListView.ID)
    }

    void setMigrationButtonEnabled(boolean enabled) {
        find(JButton)
            .in(projectListView)
            .named('migrateSelected')
            .setEnabled(enabled)
    }

    void updateStatus(String message, Integer progress) {
        def progressBar = find(JProgressBar).in(viewManager.rootView).named(StatusBar.STATUS_BAR_NAME)
        def label = find(JLabel).in(viewManager.rootView).named(StatusBar.LEFT_PANEL_LABEL)

        progressBar.value = progress
        label.text = message
    }

}
