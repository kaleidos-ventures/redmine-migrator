package gui.settings

import static org.viewaframework.util.ComponentFinder.find

import java.awt.event.ActionEvent

import javax.swing.JButton
import javax.swing.JMenuItem
import javax.swing.JTextField
import javax.swing.JPasswordField

import org.viewaframework.view.ViewContainer
import org.viewaframework.controller.AbstractActionController

class SettingsController extends AbstractActionController {

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        if (event.source.name == 'settingsCancelButton') {
            return
        }

        def textFieldFinder = find(JTextField).in(view)
        def passwordFinder  = find(JPasswordField).in(view)

        def redmineUrl = textFieldFinder.named('redmineUrl')
        def redmineApiKey= textFieldFinder.named('redmineApiKey')
        def taigaUrl = textFieldFinder.named('taigaUrl')
        def taigaUsername = textFieldFinder.named('taigaUsername')
        def taigaPassword =  passwordFinder.named('taigaPassword')

        def settings = new Settings(
            redmineUrl: redmineUrl.text,
            redmineApiKey: redmineApiKey.text,
            taigaUrl: taigaUrl.text,
            taigaUsername: taigaUsername.text,
            taigaPassword: new String(taigaPassword?.password)
        )

        new SettingsService().saveSettings(settings)

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
