package gui.migration

import gui.controller.CloseViewController
import org.viewaframework.annotation.*
import org.viewaframework.view.AbstractViewContainerDialog

@Controllers([
    @Controller(type=CloseViewController,pattern='closeButton')
])
class MigrationProgressView extends AbstractViewContainerDialog {

    static final ID = "migrationProgressViewID"

    MigrationProgressView() {
        super(ID, new MigrationProgressPanel().initComponents(), true)
    }

}
