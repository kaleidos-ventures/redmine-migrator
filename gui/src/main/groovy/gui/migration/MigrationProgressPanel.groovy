package gui.migration

import javax.swing.*
import org.jdesktop.swingx.JXTitledPanel

class MigrationProgressPanel extends JXTitledPanel {

    JLabel icon

    MigrationProgressPanel() {
        super("title")
    }

    MigrationProgressPanel initComponents() {
        this.name = "migrationProgressPanel"
        this.icon = new JLabel()
        this.icon.name = 'iconLabel'
        this.leftDecoration = icon
        this.add(new MigrationProgressForm().initComponents())
        return this
    }

}
