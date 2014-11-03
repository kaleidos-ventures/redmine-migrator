package gui.migration

import gui.controller.*
import org.viewaframework.annotation.*
import org.viewaframework.view.AbstractViewContainerDialog
import org.viewaframework.widget.controller.ExitActionController

@Controllers([
    @Controller(type=ExitActionController,pattern='closeButton')
])
@Listeners([
    @Listener(id="migrationListenerID", type=GlassPaneNotExitableListener)
])
class MigrationProgressView extends AbstractViewContainerDialog {

    static final ID = "migrationProgressViewID"

    MigrationProgressView() {
        super(ID, new MigrationProgressPanel().initComponents(), true)
    }

}
