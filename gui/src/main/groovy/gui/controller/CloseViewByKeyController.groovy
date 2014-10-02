package gui.controller

import java.awt.event.*
import org.viewaframework.view.*

class CloseViewByKeyController extends AbstractViewKeyController {

    public void postHandlingView(ViewContainer view, KeyEvent eventObject) throws ViewException{
        int keyCode = eventObject.getKeyCode();

        // keyCode == Escape
        if (keyCode == 27) {
            //def keyString = "key code = " + keyCode + " (" + KeyEvent.getKeyText(keyCode) + ")";
            view.application.viewManager.removeView(view)
        }
    }

}
