package gui.console

import java.awt.Color
import java.awt.Component
import javax.swing.JLabel
import javax.swing.JTable

import org.viewaframework.view.ViewContainer
import org.viewaframework.widget.view.ui.MasterViewCellRenderer
import org.jdesktop.swingx.renderer.DefaultTableRenderer

class ConsoleViewEntryRenderer extends DefaultTableRenderer implements MasterViewCellRenderer {

    Component getTableCellRendererComponent(JTable table,
                                      Object value,
                                      boolean isSelected,
                                      boolean hasFocus,
                                      int row,
                                      int column) {

        def formerLabel =
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)

        switch(value) {
            case 'ERROR':
                formerLabel.foreground = Color.RED.darker()
                formerLabel.font = formerLabel.font.deriveFont(java.awt.Font.BOLD)
            break
            case 'WARN':
                formerLabel.foreground = Color.ORANGE.darker()
                formerLabel.font = formerLabel.font.deriveFont(java.awt.Font.BOLD)
            break
        }

        return formerLabel


   }

    public void setViewContainer(ViewContainer viewContainer) {

    }

    public ViewContainer getViewContainer() {
        return null
    }

}
