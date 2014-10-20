package gui.console

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import gui.controller.*
import org.viewaframework.controller.*

class ConsoleSaveLogWarningViewController extends AbstractOpenerController<ActionListener,ActionEvent> {

    ConsoleSaveLogWarningViewController() {
       super(new ConsoleSaveLogWarningView())
    }

    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

}
