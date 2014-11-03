package gui.console

import org.viewaframework.annotation.Controller
import org.viewaframework.annotation.Controllers
import org.viewaframework.annotation.Listener
import org.viewaframework.annotation.Listeners
import org.viewaframework.view.DefaultViewContainer

import org.viewaframework.widget.view.MasterView
import org.viewaframework.widget.view.ui.MasterViewColumn

@Listeners([
    @Listener(id='consoleID',type=ConsoleViewListener)
])
@Controllers([
    @Controller(type=ConsoleClearWarningViewController, pattern='clearLog'),
    @Controller(type=ConsoleSaveLogWarningViewController, pattern='saveLog')
])
class ConsoleView extends MasterView<ConsoleViewEntry> {

    static final String ID = "consoleViewID"

    ConsoleView() {
        super(
            ID, [
               new MasterViewColumn("timeStamp", 40),
               new MasterViewColumn("level", 50, new ConsoleViewEntryRenderer()),
               new MasterViewColumn("logger", 150),
               new MasterViewColumn("message", 500),
            ]
        )
    }

    Class<ConsoleViewEntry> getMasterType() {
        return ConsoleViewEntry
    }

}
