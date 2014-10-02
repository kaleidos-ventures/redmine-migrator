package gui.taiga

import java.awt.event.*

import gui.warning.*
import gui.controller.*

import org.viewaframework.controller.*

class TaigaProjectDeletionWarningController extends AbstractOpenerController<ActionListener,ActionEvent> {

    TaigaProjectDeletionWarningController() {
       super(new WarningView())
    }

    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

}
