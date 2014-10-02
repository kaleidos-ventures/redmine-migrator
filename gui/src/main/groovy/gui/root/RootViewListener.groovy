package gui.root

import static org.viewaframework.util.ComponentFinder.find
import static org.viewaframework.util.ResourceLocator.getImageIcon

import javax.swing.JTextField
import org.viewaframework.view.event.ViewContainerEvent
import org.viewaframework.view.event.DefaultViewContainerEventController

class RootViewListener extends DefaultViewContainerEventController {

    @Override
    public void onViewInitUIState(ViewContainerEvent event) {
        def translucentPane = new TranslucentGlassPane(event.source)
        event.source.rootPane.glassPane = translucentPane
    }

}
