package gui.taiga

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

import net.kaleidos.domain.Project
import net.kaleidos.taiga.TaigaClient

import gui.exception.ExceptionView
import gui.settings.SettingsService
import gui.controller.DefaultViewControllerWorker

class TaigaProjectListController extends
    DefaultViewControllerWorker<ActionListener, ActionEvent, String, Project> {

    @Override
    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

    @Override
    void preHandlingView(ViewContainer view, ActionEvent event) {
        updateStatus("Loading taiga list...", 50)
        def projectsView = getProjectListView()

        if (!projectsView) {
            projectsView = new TaigaProjectListView()
            viewManager.addView(projectsView , PerspectiveConstraint.RIGHT)
        }

        projectsView.model.clear()
        view.rootPane.glassPane.visible = true
    }

    ViewContainer getProjectListView() {
        return locate(TaigaProjectListView).named(TaigaProjectListView.ID)
    }

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        def settings = new SettingsService().loadSettings()
        def taigaClient =
            new TaigaClient(settings.taigaUrl)
                .authenticate(
                    settings.taigaUsername,
                    settings.taigaPassword
                )


        publish(taigaClient.projects)
    }

    @Override
    void handleViewPublising(ViewContainer view, ActionEvent event, List<Project> chunks) {
        locate(TaigaProjectListView)
            .named(TaigaProjectListView.ID)
            .model
            .addAll(chunks.flatten())

        view.rootPane.glassPane.visible = false
    }

    @Override
    void postHandlingView(ViewContainer viewContainer, ActionEvent event) {
        def rows = locate(TaigaProjectListView)
            .named(TaigaProjectListView.ID)
            .model
            .rowCount

        if (!rows) {
            updateStatus("No results found", 0)
            viewManager.addView(
                new ExceptionView(new Exception("No results found please check your connection settings.Check log for more details"))
            )
            return
        }

        updateStatus("Showing $rows Taiga projects ", 0)
    }

    void updateStatus(String message, Integer progress) {
        def progressBar = find(JProgressBar).in(viewManager.rootView).named(StatusBar.STATUS_BAR_NAME)
        def label = find(JLabel).in(viewManager.rootView).named(StatusBar.LEFT_PANEL_LABEL)

        progressBar.value = progress
        label.text = message
    }

}
