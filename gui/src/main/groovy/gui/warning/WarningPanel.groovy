package gui.warning

import javax.swing.JLabel
import org.jdesktop.swingx.JXTitledPanel

class WarningPanel extends JXTitledPanel {

    JLabel icon

    WarningPanel() {
        super("title")
    }

    WarningPanel initComponents() {
        this.name = "warningPanel"
        this.icon = new JLabel()
        this.icon.name = 'iconLabel'
        this.leftDecoration = icon
        this.add(new WarningPanelForm().initComponents())
        return this
    }

}
