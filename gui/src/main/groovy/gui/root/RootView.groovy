package gui.root

import gui.redmine.ListProjectController
import gui.taiga.TaigaProjectListController

import org.viewaframework.annotation.Controller
import org.viewaframework.annotation.Controllers
import org.viewaframework.annotation.Listener
import org.viewaframework.annotation.Listeners
import org.viewaframework.view.DefaultViewContainerFrame
import org.viewaframework.widget.controller.ExitActionController

@Controllers([
    @Controller(type=ExitActionController,pattern=ExitActionController.EXIT_PATTERN),
    @Controller(type=OpenSettingsController, pattern = 'settings'),
    @Controller(type=ListProjectController, pattern = 'redmine'),
    @Controller(type=TaigaProjectListController, pattern = 'taiga')
])
@Listeners([
    @Listener(id='translucent',type=RootViewListener)
])
class RootView extends DefaultViewContainerFrame { }
