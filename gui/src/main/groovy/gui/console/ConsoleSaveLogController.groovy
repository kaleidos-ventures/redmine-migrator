package gui.console

import static org.viewaframework.util.ComponentFinder.find

import java.awt.event.ActionEvent
import groovy.util.logging.Log4j

import org.viewaframework.view.*
import org.viewaframework.util.*
import org.viewaframework.widget.view.*
import org.viewaframework.swing.DynamicTable

import gui.settings.SettingsService
import gui.migration.MigrationProgress
import gui.controller.MigrationProgressAwareController

@Log4j
class ConsoleSaveLogController extends MigrationProgressAwareController {

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        needsToBeRead(2000)

        def model =  consoleView.model
        def logEntriesSize = model.size()

        publishIndeterminate("Preparing to save $logEntriesSize entries...")
        needsToBeRead(2000)

        File file =
            new File(
                System.getProperty('user.home'),
                "taiga_migrator_${new Date().getTime()}.log")

        try {

            if (!file.createNewFile()) {
                throw new IOException("Cant create file")
            }

            model.eachWithIndex { entry, Integer index ->
                String message = "Saving $index of $logEntriesSize"
                Float progress = index.div(logEntriesSize)

                file.append(entry.join(','))
                file.append("\n")

                publishProgress(message, progress)
            }

            needsToBeRead(1000)
        } catch(Throwable th) {
            publishFailure(th)
        }

        publishSuccessWithMessage("Log saved at ${file}")

    }

    ConsoleView getConsoleView() {
        return locate(ConsoleView).named(ConsoleView.ID)
    }

}
