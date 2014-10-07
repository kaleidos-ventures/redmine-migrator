package gui.console

import java.awt.event.ComponentEvent
import java.awt.event.ComponentAdapter

import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JScrollPane

import org.apache.log4j.spi.LoggingEvent

import org.viewaframework.swing.DynamicTable
import org.viewaframework.swing.table.DynamicTableModel
import org.viewaframework.swing.table.DynamicTableColumn

class ConsoleViewPanel extends JScrollPane {

    DynamicTable<ConsoleViewEntry> loggingTable
    DynamicTableModel<ConsoleViewEntry> loggingTableModel

    ConsoleViewPanel() {
        super()
    }

    public ConsoleViewPanel initComponents() {
        loggingTableModel = new DynamicTableModel<ConsoleViewEntry>([
            new DynamicTableColumn('timeStamp',1,50),
            new DynamicTableColumn('level',1,50, new ConsoleViewEntryRenderer()),
            new DynamicTableColumn('logger',1,150),
            new DynamicTableColumn('message',1,500),
        ])

        loggingTable = new DynamicTable<ConsoleViewEntry>(loggingTableModel)
        loggingTable.name = 'loggingTable'
        loggingTable.addComponentListener(new ComponentAdapter() {
            void componentResized(ComponentEvent e) {
                loggingTable.scrollRectToVisible(
                    loggingTable.getCellRect(
                        loggingTable.getRowCount()-1, 0, true
                    )
                );
            }
        });

        this.viewport.add(loggingTable, java.awt.BorderLayout.CENTER)
        return this
    }

}
