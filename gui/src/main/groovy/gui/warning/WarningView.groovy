package gui.warning

import org.viewaframework.view.AbstractViewContainerDialog

// TODO JXErrorPane closeButton doesnt have name
class WarningView extends AbstractViewContainerDialog {

    static final ID = "warningViewID"

    WarningView() {
        super(ID, new WarningPanel().initComponents(), true)
    }

}
