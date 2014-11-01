package gui.ui

import java.awt.Component

import javax.swing.JTable
import javax.swing.SwingConstants

import org.jdesktop.swingx.renderer.DefaultTableRenderer

import org.viewaframework.view.ViewContainer
import org.viewaframework.widget.view.ui.MasterViewCellRenderer

class DateCellRenderer extends DefaultTableRenderer implements MasterViewCellRenderer {

    public Component getTableCellRendererComponent(
        JTable table,
        Object value,
        boolean isSelected,
        boolean hasFocus,
        int row,
        int column) {

        def formerLabel =
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)

        if (value) {
            formerLabel.setText(value.format("dd/MM/yyyy"))
            formerLabel.setHorizontalAlignment(SwingConstants.CENTER)
        }


        return formerLabel
    }

    public void setViewContainer(ViewContainer viewContainer) {

    }

    public ViewContainer getViewContainer() {
        return null
    }

}
