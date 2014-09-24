package gui.settings

import javax.swing.JLabel
import org.jdesktop.swingx.JXTitledPanel

class SettingsPanel extends JXTitledPanel {

    JLabel icon

    SettingsPanel() {
        super("title")
    }

    SettingsPanel initComponents() {
        this.name = "settingsPanel"
        this.icon = new JLabel()
        this.icon.name = 'iconLabel'
        this.leftDecoration = icon
        this.add(new SettingsForm())
        return this
    }

}
