package gui.root

import gui.redmine.ListProjectController

import org.viewaframework.annotation.Controller
import org.viewaframework.annotation.Controllers
import org.viewaframework.view.DefaultViewContainerFrame
import org.viewaframework.widget.controller.ExitActionController

@Controllers([
    @Controller(type=ExitActionController,pattern=ExitActionController.EXIT_PATTERN),
    @Controller(type=OpenSettingsController, pattern = 'settings'),
    @Controller(type=ListProjectController, pattern = 'redmine')
])
class RootView extends DefaultViewContainerFrame { }
