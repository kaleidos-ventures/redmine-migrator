package gui.settings

import static org.viewaframework.util.ComponentFinder.find

import java.awt.event.ActionEvent

import javax.swing.JButton
import javax.swing.JMenuItem
import javax.swing.JTextField

import org.viewaframework.view.ViewContainer
import org.viewaframework.controller.AbstractActionController

class SettingsController extends AbstractActionController {

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        if (event.source.name == 'settingsCancelButton') {
            return
        }

        def addressField = find(JTextField).in(view).named('redmineUrl')
        def apiKeyField = find(JTextField).in(view).named('redmineApiKey')
        def context = viewManager.application.applicationContext

        context.setAttribute('redmineUrl', addressField.text)
        context.setAttribute('redmineApiKey', apiKeyField.text)
    }

    @Override
    void postHandlingView(ViewContainer view, ActionEvent event) {
        // enable to list redmine projects
        find(JButton).in(locateRootView()).named('redmine').enabled = true
        find(JMenuItem).in(locateRootView()).named('redmine').enabled = true

        // removing settings view
        viewManager.removeView(view)
    }

}
