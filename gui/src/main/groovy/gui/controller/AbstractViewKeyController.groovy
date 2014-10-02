package gui.controller

import java.awt.event.*
import org.viewaframework.controller.*

abstract class AbstractViewKeyController extends AbstractViewController<KeyListener, KeyEvent> {

    Class<KeyListener> getSupportedClass() {
        return KeyListener
    }


}
