package gui.migration

import gui.controller.*
import org.viewaframework.annotation.*
import org.viewaframework.view.AbstractViewContainerDialog

@Controllers([
    @Controller(type=CloseViewController,pattern='closeButton')
])
@Listeners([
    @Listener(id="migrationListenerID", type=GlassPaneAwareListener)
])
class MigrationProgressView extends AbstractViewContainerDialog {

    static final ID = "migrationProgressViewID"

    MigrationProgressView() {
        super(ID, new MigrationProgressPanel().initComponents(), true)
    }

}
