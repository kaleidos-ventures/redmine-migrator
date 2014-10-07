package gui.console

import org.viewaframework.annotation.Controller
import org.viewaframework.annotation.Controllers
import org.viewaframework.annotation.Listener
import org.viewaframework.annotation.Listeners
import org.viewaframework.view.DefaultViewContainer

@Listeners([
    @Listener(id='consoleID',type=ConsoleViewListener)
])
@Controllers([
    @Controller(type=ConsoleClearLogController, pattern='clearLog')
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
