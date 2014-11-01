package gui.console

import static org.viewaframework.util.ComponentFinder.find

import javax.swing.JTable

import org.apache.log4j.Logger

import java.awt.event.ComponentEvent
import java.awt.event.ComponentAdapter

import org.viewaframework.view.event.ViewContainerEvent
import org.viewaframework.view.event.DefaultViewContainerEventController

class ConsoleViewListener extends DefaultViewContainerEventController {

    @Override
    public void onViewInitBackActions(ViewContainerEvent event) {
        Logger
            .rootLogger
            .addAppender(
                new ConsoleAppender(event.source.model)
            )
    }

    public void onViewFinalUIState(ViewContainerEvent event) {
        def loggingTable = find(JTable).in(event.source).named('table')

        loggingTable.addComponentListener(new ComponentAdapter() {
            void componentResized(ComponentEvent e) {
                loggingTable.scrollRectToVisible(
                    loggingTable.getCellRect(
                        loggingTable.getRowCount()-1, 0, true
                    )
                );
            }
        });

    }

}
