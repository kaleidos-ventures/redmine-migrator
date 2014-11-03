package gui.redmine

import groovy.transform.InheritConstructors

import org.viewaframework.annotation.Controller
import org.viewaframework.annotation.Controllers
import org.viewaframework.annotation.Listener
import org.viewaframework.annotation.Listeners
import org.viewaframework.widget.controller.ExitActionController

import gui.warning.WarningView
import gui.controller.GlassPaneAwareListener

@Controllers([
    @Controller(type=ExitActionController,pattern='cancelButton'),
    @Controller(type=RedmineMigrationController, pattern='acceptButton')
])
@Listeners([
    @Listener(id="migrationWarningListenerID", type=GlassPaneAwareListener)
])
@InheritConstructors
class RedmineMigrationWarningView extends WarningView { }
