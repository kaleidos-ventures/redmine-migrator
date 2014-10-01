package gui.console

import javax.swing.JTextArea
import org.apache.log4j.spi.LoggingEvent
import org.apache.log4j.AppenderSkeleton

import org.viewaframework.swing.table.DynamicTableModel

class ConsoleAppender extends AppenderSkeleton {

    DynamicTableModel<ConsoleViewEntry> loggingModel

    ConsoleAppender(DynamicTableModel<ConsoleViewEntry> model) {
        super()
        loggingModel = model
    }

    @Override
    void append(LoggingEvent event) {
        if(!event.logger.name.contains('net.kaleidos')) {
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
