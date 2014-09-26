package gui.controller

import java.util.EventListener;
import java.util.EventObject;

import java.awt.event.*
import org.viewaframework.view.*;
import org.viewaframework.controller.*;

/**
 */
public class CloseViewController extends AbstractViewController<ActionListener,ActionEvent>{

    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }
    /* (non-Javadoc)
     * @see org.viewaframework.controller.AbstractViewController#postHandlingView(org.viewaframework.view.ViewContainer, java.util.EventObject)
     */
    @Override
    public void postHandlingView(ViewContainer view, ActionEvent eventObject) throws ViewException{
        getViewManager().removeView(view);
    }
}

