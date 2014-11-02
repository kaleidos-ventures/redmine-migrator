package gui.about

import gui.controller.GlassPaneAwareListener

import org.viewaframework.annotation.*
import org.viewaframework.widget.view.AboutView
import org.viewaframework.widget.controller.ExitActionController

import groovy.transform.InheritConstructors

@Controllers([
    @Controller(type=ExitActionController,pattern="aboutClose")
])
@Listeners([
    @Listener(id="migrationWarningListenerID", type=GlassPaneAwareListener)
])
@InheritConstructors
class AboutTaigaView extends AboutView {


}
