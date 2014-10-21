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
    @Controller(type=CloseViewController,pattern='cancelButton'),
    @Controller(type=ConsoleClearLogController, pattern='acceptButton')
])
@Listeners([
    @Listener(id="consoleClearWarningViewListenerID", type=GlassPaneAwareListener)
])
@InheritConstructors
class ConsoleClearWarningView extends WarningView {

}
