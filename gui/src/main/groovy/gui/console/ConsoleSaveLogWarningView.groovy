package gui.console

import gui.warning.WarningView

import groovy.transform.InheritConstructors

import org.viewaframework.annotation.Controller
import org.viewaframework.annotation.Controllers
import org.viewaframework.annotation.Listener
import org.viewaframework.annotation.Listeners

import gui.warning.WarningView
import gui.controller.CloseViewController
import gui.controller.GlassPaneAwareListener

@Controllers([
    @Controller(type=CloseViewController,pattern='cancelButton')
])
@Listeners([
    @Listener(id="consoleSaveLogWarningListenerID", type=GlassPaneAwareListener)
])
@InheritConstructors
class ConsoleSaveLogWarningView extends WarningView {

}
