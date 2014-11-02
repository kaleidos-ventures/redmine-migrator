package gui.controller

import java.awt.event.ActionEvent

import org.viewaframework.controller.AbstractActionController
import org.viewaframework.view.ViewContainer
import org.viewaframework.view.ViewException
import org.viewaframework.view.ViewManager

/**
 * @author Mario Garcia
 *
 */
public class ExitAppController extends AbstractActionController {

    /* (non-Javadoc)
     * @see org.viewaframework.controller.AbstractViewController#postHandlingView(org.viewaframework.view.ViewContainer, java.util.EventObject)
     */
    @Override
    public void postHandlingView(ViewContainer view, ActionEvent eventObject) throws ViewException {
        view.getApplication().close()
    }
}
