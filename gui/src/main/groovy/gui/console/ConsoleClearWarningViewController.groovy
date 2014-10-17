package gui.console

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import gui.controller.*
import org.viewaframework.controller.*

class ConsoleClearWarningViewController extends AbstractOpenerController<ActionListener,ActionEvent> {

    ConsoleClearWarningViewController() {
       super(new ConsoleClearWarningView())
    }

    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

}
