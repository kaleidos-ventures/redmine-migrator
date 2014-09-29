package gui.settings

import static org.viewaframework.util.ComponentFinder.find

import javax.swing.JTextField

import gui.controller.*
import org.viewaframework.view.event.ViewContainerEvent
import org.viewaframework.view.event.DefaultViewContainerEventController

class CheckStoredSettingsListener extends GlassPaneAwareListener {

    @Override
    public void onViewInitBackActions(ViewContainerEvent event) {

        def addressField = find(JTextField).in(event.source).named('redmineUrl')
        def apiKeyField = find(JTextField).in(event.source).named('redmineApiKey')
        def context = event.source.application.applicationContext

        addressField.text = context.getAttribute('redmineUrl')
        apiKeyField.text = context.getAttribute('redmineApiKey')

    }

}
