package gui.taiga

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
    @Controller(type=DeleteTaigaProjectController, pattern='acceptButton')
])
@Listeners([
    @Listener(id="deleteWarningListenerID", type=GlassPaneAwareListener)
])
@InheritConstructors
class DeleteTaigaProjectWarningView extends WarningView { }
