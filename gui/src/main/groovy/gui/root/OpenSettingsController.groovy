package gui.root

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import gui.settings.SettingsView
import org.viewaframework.controller.AbstractOpenerController

class OpenSettingsController extends AbstractOpenerController<ActionListener,ActionEvent> {

    OpenSettingsController() {
       super(new SettingsView())
    }

    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

}
