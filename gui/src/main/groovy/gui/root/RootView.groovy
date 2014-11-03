package gui.root

import gui.about.OpenAboutTaigaController
import gui.redmine.RedmineProjectListController
import gui.taiga.TaigaProjectListController

import org.viewaframework.annotation.Controller
import org.viewaframework.annotation.Controllers
import org.viewaframework.annotation.Listener
import org.viewaframework.annotation.Listeners
import org.viewaframework.view.DefaultViewContainerFrame
import org.viewaframework.widget.controller.ExitActionController

@Controllers([
    @Controller(type=OpenExitWarningController,pattern=ExitActionController.EXIT_PATTERN),
    @Controller(type=OpenSettingsController, pattern = 'settings'),
    @Controller(type=RedmineProjectListController, pattern = 'redmine'),
    @Controller(type=TaigaProjectListController, pattern = 'taiga'),
    @Controller(type=OpenAboutTaigaController, pattern = 'aboutTaiga'),
])
@Listeners([
    @Listener(id='translucent',type=RootViewListener)
])
class RootView extends DefaultViewContainerFrame { }
