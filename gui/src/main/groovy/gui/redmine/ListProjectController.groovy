package gui.redmine

import static org.viewaframework.util.ComponentFinder.find

import org.jdesktop.swingx.JXList

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.DefaultListModel
import javax.swing.JLabel
import javax.swing.JProgressBar
import javax.swing.JTextField

import org.viewaframework.view.*
import org.viewaframework.util.*
import org.viewaframework.controller.*
import org.viewaframework.view.perspective.*
import org.viewaframework.widget.view.*

import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory
import com.taskadapter.redmineapi.bean.Project

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
        def redmineAddress = getContextAttribute("redmineUrl")
        def redmineApiKey = getContextAttribute("redmineApiKey")
        def redmineManager = new RedmineManager(redmineAddress, redmineApiKey)

        publish(redmineManager.projects)
    }

    @Override
    void handleViewPublising(ViewContainer view, ActionEvent event, List<Project> chunks) {
        locate(ProjectListView)
            .named(ProjectListView.ID)
            .model
            .addAll(chunks.flatten())

        view.rootPane.glassPane.visible = false
    }

    @Override
    void postHandlingView(ViewContainer viewContainer, ActionEvent event) {
        def rows = locate(ProjectListView)
            .named(ProjectListView.ID)
            .model
            .rowCount

        if (!rows) {
            updateStatus("No results found", 0)
            viewManager.addView(
                new ExceptionView(new Exception("No results found please check your connection settings.Check log for more details"))
            )
            return
        }

        updateStatus("Showing $rows Redmine projects ", 0)
    }

    void updateStatus(String message, Integer progress) {
        def progressBar = find(JProgressBar).in(viewManager.rootView).named(StatusBar.STATUS_BAR_NAME)
        def label = find(JLabel).in(viewManager.rootView).named(StatusBar.LEFT_PANEL_LABEL)

        progressBar.value = progress
        label.text = message
    }

}
