package gui.redmine

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import gui.controller.*
import org.viewaframework.controller.*

class RedmineMigrationWarningController extends AbstractOpenerController<ActionListener,ActionEvent> {

    RedmineMigrationWarningController() {
       super(new RedmineMigrationWarningView())
    }

    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

}
