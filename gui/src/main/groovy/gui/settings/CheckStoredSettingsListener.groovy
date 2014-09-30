package gui.settings

import static org.viewaframework.util.ComponentFinder.find

import javax.swing.JTextField
import javax.swing.JPasswordField

import gui.controller.*
import org.viewaframework.view.event.ViewContainerEvent
import org.viewaframework.view.event.DefaultViewContainerEventController

class CheckStoredSettingsListener extends GlassPaneAwareListener {

    @Override
    public void onViewInitBackActions(ViewContainerEvent event) {

        def textFieldFinder = find(JTextField).in(event.source)
        def passwordFinder = find(JPasswordField).in(event.source)

        def addressField = textFieldFinder.named('redmineUrl')
        def apiKeyField = textFieldFinder.named('redmineApiKey')
        def taigaUrl = textFieldFinder.named('taigaUrl')
        def taigaUsername = textFieldFinder.named('taigaUsername')
        def taigaPassword = passwordFinder.named('taigaPassword')

        def settings = new SettingsService().loadSettings()

        addressField.text = settings.redmineUrl
        apiKeyField.text = settings.redmineApiKey
        taigaUrl.text = settings.taigaUrl
        taigaUsername.text = settings.taigaUsername
        taigaPassword.text = settings.taigaPassword

    }

}
