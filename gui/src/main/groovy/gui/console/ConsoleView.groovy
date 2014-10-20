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
    @Controller(type=ConsoleClearWarningViewController, pattern='clearLog'),
    @Controller(type=ConsoleSaveLogWarningViewController, pattern='saveLog')
])
class ConsoleView extends DefaultViewContainer {

    static final String ID = "consoleViewID"

    ConsoleView() {
        super(
            ID,
            "Console View",
            new ConsoleViewPanel().initComponents()
        )
    }

}
