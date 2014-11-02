package gui.root

import groovy.transform.InheritConstructors

import org.viewaframework.annotation.Controller
import org.viewaframework.annotation.Controllers
import org.viewaframework.annotation.Listener
import org.viewaframework.annotation.Listeners
import org.viewaframework.widget.controller.ExitActionController

import gui.warning.WarningView
import gui.controller.ExitAppController
import gui.controller.GlassPaneAwareListener

@Controllers([
    @Controller(type=ExitActionController,pattern='cancelButton'),
    @Controller(type=ExitAppController, pattern='acceptButton')
])
@Listeners([
    @Listener(id="exitAppWarningListenerID", type=GlassPaneAwareListener)
])
@InheritConstructors
class ExitWarningView extends WarningView { }
