package gui.exception

import com.taskadapter.redmineapi.bean.Project

import org.viewaframework.annotation.*
import org.viewaframework.widget.view.*
import org.viewaframework.widget.view.ui.*
import org.viewaframework.view.AbstractViewContainerDialog

import gui.controller.*

// TODO JXErrorPane closeButton doesnt have name
@Controllers([
    @Controller(type=CloseViewController,pattern='closeButton')
])
class ExceptionView extends AbstractViewContainerDialog {

    static final ID = "exceptionViewID"

    ExceptionView(final Throwable ex) {
        super(ID, new ExceptionPanel(ex).initComponents(), true)
    }

}
