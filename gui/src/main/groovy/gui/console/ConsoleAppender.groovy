package gui.console

import javax.swing.JTextArea
import org.apache.log4j.spi.LoggingEvent
import org.apache.log4j.AppenderSkeleton

import org.viewaframework.widget.view.ui.MasterViewModel

class ConsoleAppender extends AppenderSkeleton {

    MasterViewModel<ConsoleViewEntry> loggingModel

    ConsoleAppender(MasterViewModel<ConsoleViewEntry> model) {
        super()
        loggingModel = model
    }

    @Override
    void append(LoggingEvent event) {
        boolean valid =
            event.logger.name.startsWith('net.kaleidos') ||
            event.logger.name.startsWith('gui')


        if(!valid) {
            return
        }
        def currentTime = Calendar.getInstance()
        currentTime.timeInMillis = event.timeStamp
        loggingModel.addRow(
            new ConsoleViewEntry(
                message:event.message,
                logger: event.loggerName.toString(),
                level: event.level.toString(),
                timeStamp: currentTime.time // Date
            )
        )
    }

    @Override
    boolean requiresLayout() {
        return true
    }

    void close() {}


}
