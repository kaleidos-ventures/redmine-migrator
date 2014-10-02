package gui.swingx

import static org.viewaframework.util.ResourceLocator.getImageIcon
import groovy.transform.InheritConstructors
import org.jdesktop.swingx.JXBusyLabel

@InheritConstructors
class JXBusyFeedbackLabel extends JXBusyLabel {

    void setFailure() {
        this.setIcon(getImageIcon(JXBusyFeedbackLabel,'org/viewaframework/widget/icon/fan/img/misc/exclamation.png'))
    }

    void setSuccess() {
        this.setIcon(getImageIcon(JXBusyFeedbackLabel,'org/viewaframework/widget/icon/fan/img/misc/accept.png'))
    }

}
