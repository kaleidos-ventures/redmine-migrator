package gui.about

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import org.viewaframework.controller.AbstractOpenerController

class OpenAboutTaigaController extends AbstractOpenerController<ActionListener,ActionEvent> {

    OpenAboutTaigaController() {
       super(new AboutTaigaView())
    }

    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

}
