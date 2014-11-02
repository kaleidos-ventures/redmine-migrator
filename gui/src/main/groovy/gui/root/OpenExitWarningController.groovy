package gui.root

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import gui.settings.SettingsView
import org.viewaframework.controller.AbstractOpenerController

class OpenExitWarningController extends AbstractOpenerController<ActionListener,ActionEvent> {

    OpenExitWarningController() {
       super(new ExitWarningView())
    }

    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

}
