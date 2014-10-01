package gui.warning

import com.taskadapter.redmineapi.bean.Project

import org.viewaframework.annotation.*
import org.viewaframework.widget.view.*
import org.viewaframework.widget.view.ui.*
import org.viewaframework.view.AbstractViewContainerDialog

import gui.controller.*
import gui.taiga.*

// TODO JXErrorPane closeButton doesnt have name
@Controllers([
    @Controller(type=CloseViewController,pattern='cancelButton'),
    @Controller(type=DeleteTaigaProjectController, pattern='acceptButton')
])
class WarningView extends AbstractViewContainerDialog {

    static final ID = "warningViewID"

    WarningView() {
        super(ID, new WarningPanel().initComponents(), true)
    }

}
