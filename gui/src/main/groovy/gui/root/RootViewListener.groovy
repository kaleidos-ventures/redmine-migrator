package gui.root

import static org.viewaframework.util.ComponentFinder.find
import static org.viewaframework.util.ResourceLocator.getImageIcon

import javax.swing.JTextField

import gui.settings.SettingsView
import gui.settings.SettingsService

import org.viewaframework.view.event.ViewContainerEvent
import org.viewaframework.view.event.DefaultViewContainerEventController

class RootViewListener extends DefaultViewContainerEventController {

    @Override
    public void onViewInitUIState(ViewContainerEvent event) {
        changeGlassPaneToTranslucent(event)
    }

    @Override
    public void onViewFinalUIState(ViewContainerEvent event) {
        showSettingsIfNeccesary(event)
    }

    void changeGlassPaneToTranslucent(ViewContainerEvent event) {
        def translucentPane = new TranslucentGlassPane(event.source)
        event.source.rootPane.glassPane = translucentPane
    }

    void showSettingsIfNeccesary(ViewContainerEvent event) {
        if (!SettingsService.configFile.exists()) {
            event.source.application.viewManager.addView(new SettingsView())
        }
    }

}
