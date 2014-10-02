package gui.controller

import javax.swing.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

abstract class DefaultActionViewControllerWorker<A> extends
    DefaultViewControllerWorker<ActionListener, ActionEvent, String, A> {

    @Override
    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

}
