package gui.controller

import static org.viewaframework.util.ComponentFinder.find

import javax.swing.JTextField

import org.viewaframework.view.event.ViewContainerEvent
import org.viewaframework.view.event.DefaultViewContainerEventController

class GlassPaneAwareListener extends DefaultViewContainerEventController {

    @Override
    public void onViewInitUIState(ViewContainerEvent event) {
        setGlassPaneVisible(event, true)
    }

    @Override
    public void onViewClose(ViewContainerEvent event) {
        setGlassPaneVisible(event, false)
    }

    private void setGlassPaneVisible(ViewContainerEvent event, boolean visible) {
        event
            .source
            .application
            .viewManager
            .rootView
            .rootPane
            .glassPane
            .visible = visible
    }

}
