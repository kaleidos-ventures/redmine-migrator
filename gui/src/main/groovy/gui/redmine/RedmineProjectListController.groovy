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

import fnz.data.Try
import fnz.data.Maybe

import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory
import com.taskadapter.redmineapi.bean.Project

import gui.settings.Settings
import gui.settings.SettingsService
import gui.controller.DefaultActionViewControllerWorker

import net.kaleidos.redmine.RedmineClientFactory
import groovy.util.logging.Log4j

@Log4j
class RedmineProjectListController extends DefaultActionViewControllerWorker<Project> {

    @Override
    void preHandlingView(final ViewContainer view, final ActionEvent event) {
        updateStatus(Success("Loading list..."), 50)

        Try cleaned = $do {
            v = Maybe(redmineProjectListView).or { createAndAddView() }
            _ = Try { log.debug("Clearing project list...") }
            _ = Try { v.model.clear() }
            _ = Try { locateRootView().rootPane.glassPane.visible = true }

            $return "Project list cleaned"
        }

        updateStatus(cleaned, 0)
    }

    Maybe<ViewContainer> createAndAddView() {
        Maybe<ViewContainer> maybeViewContainer =
            $do {
                view  = Just(new RedmineProjectListView())
                _     = Try(view) { v -> viewManager.addView(v, PerspectiveConstraint.RIGHT) }
                added = Just(view)

                $return added
            }

        return maybeViewContainer
    }

    ViewContainer getRedmineProjectListView() {
        return locate(RedmineProjectListView).named(RedmineProjectListView.ID)
    }

    @Override
    void handleView(final ViewContainer view, final ActionEvent event) {
        log.debug("Listing Redmine projects")

        Try result = $do {
            service  = Try { new SettingsService() }
            settings = Try { service.loadSettings() }
            redmine  = Try { RedmineClientFactory.newInstance(settings.properties) }
            projects = Try { redmine.findAllProject() }

            $return projects
        }

        publishResult(result)
    }

    void publishResult(final Try.Success success) {
        publish(val(success))
    }

    void publishResult(final Try.Failure failure) {
        log.error(failure.exception.message)
    }

    @Override
    void handleViewPublising(final ViewContainer view, final ActionEvent event, final List<Project> chunks) {
        redmineProjectListView.model.addAll(chunks.flatten())
    }

    @Override
    void postHandlingView(final ViewContainer viewContainer, final ActionEvent event) {
        Try hasResult = $do {
            _      = Try { locateRootView().rootPane.glassPane.visible = false }
            rows   = Try { redmineProjectListView.model.rowCount }
            result = rows ? Success("Showing $rows Redmine projects") : Try.failure(new IllegalStateException("No results found"))

            $return result
        }

        updateMigrationButton(hasResult)
        updateStatus(hasResult, 0)
    }

    void updateMigrationButton(final Try.Success sucess) {
        setMigrationButtonEnabled(true)
    }

    void updateMigrationButton(final Try.Failure failure) {
        setMigrationButtonEnabled(false)
    }

    void updateStatus(final Try.Success<String> message, final Integer progress) {
        updateStatus(val(message), progress)
    }

    void updateStatus(final Try.Failure<String> message, final Integer progress) {
        updateStatus(message)
    }

    void updateStatus(final Try.Failure failure) {
        updateStatus(failure.exception.message, 0)
    }

    void setMigrationButtonEnabled(final boolean enabled) {
        migratedSelectedButton.setEnabled(enabled)
    }

    void updateStatus(final String message, final Integer progress) {
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
