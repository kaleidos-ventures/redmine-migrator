package gui.settings

import gui.controller.*
import org.viewaframework.annotation.*
import org.viewaframework.view.AbstractViewContainerDialog

@Controllers([
    @Controller(type=SettingsController, pattern="settingsAcceptButton|settingsCancelButton")
])
@Listeners([
    @Listener(id='xxx',type=CheckStoredSettingsListener)
])
class SettingsView extends AbstractViewContainerDialog {

    static final ID = "settingsViewID"

    SettingsView() {
        super(ID, new SettingsPanel().initComponents(), true)
    }

}
