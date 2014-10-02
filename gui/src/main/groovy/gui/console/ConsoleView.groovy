package gui.console

import org.viewaframework.annotation.Listener
import org.viewaframework.annotation.Listeners
import org.viewaframework.view.DefaultViewContainer

@Listeners([
    @Listener(id='consoleID',type=ConsoleViewListener)
])
class ConsoleView extends DefaultViewContainer {

    ConsoleView() {
        super(
            "console",
            "Console View",
            new ConsoleViewPanel().initComponents()
        )
    }

}
