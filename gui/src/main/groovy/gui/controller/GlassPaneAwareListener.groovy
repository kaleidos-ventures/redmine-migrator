package gui.controller

import static org.viewaframework.util.ComponentFinder.find

import java.awt.event.*
import javax.swing.*
import javax.swing.event.*
import javax.swing.JDialog
import javax.swing.JTextField

import org.viewaframework.view.ViewContainer
import org.viewaframework.view.event.ViewContainerEvent
import org.viewaframework.view.event.DefaultViewContainerEventController

class GlassPaneAwareListener extends DefaultViewContainerEventController {

    private final Boolean exitable

    GlassPaneAwareListener() {
        super()
        this.exitable = true
    }

    GlassPaneAwareListener(Boolean exitable) {
        super()
        this.exitable = exitable
    }

    @Override
    public void onViewInitUIState(final ViewContainerEvent event) {
        setGlassPaneVisible(event, true)

        if (exitable) {
            final ViewContainer view = event.source

            ActionListener actionListener = { view.application.viewManager.removeView(view) }
            KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)

            find(JDialog)
                .in(view)
                .named('viewDialog')
                .rootPane
                .registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW)
            }
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
