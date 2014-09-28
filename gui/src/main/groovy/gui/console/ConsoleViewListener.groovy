package gui.console

import static org.viewaframework.util.ComponentFinder.find

import javax.swing.JTextField
import javax.swing.JTextArea

import org.apache.log4j.Logger

import org.viewaframework.swing.DynamicTable
import org.viewaframework.view.event.ViewContainerEvent
import org.viewaframework.view.event.DefaultViewContainerEventController

class ConsoleViewListener extends DefaultViewContainerEventController {

    @Override
    public void onViewInitBackActions(ViewContainerEvent event) {

        def table = find(DynamicTable).in(event.source).named('loggingTable')

        Logger
            .rootLogger
            .addAppender(new ConsoleAppender(table.model))


    }

}
