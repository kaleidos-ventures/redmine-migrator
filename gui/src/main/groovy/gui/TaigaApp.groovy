package gui

import gui.root.RootView

import org.viewaframework.annotation.View
import org.viewaframework.annotation.Views
import org.viewaframework.annotation.ViewsPerspective

import org.viewaframework.core.DefaultApplication
import org.viewaframework.view.perspective.PerspectiveConstraint
import org.viewaframework.docking.mydoggy.MyDoggyPerspective


//import org.viewaframework.integration.spring.SpringApplicationLauncher

@Views([
    @View(type=RootView,isRoot=true)
])
@ViewsPerspective(MyDoggyPerspective)
class TaigaApp extends DefaultApplication {
    static void main(String[] args) {
        //new SpringApplicationLauncher().execute(TaigaApp)
        new TaigaAppLauncher().execute(TaigaApp)
    }



}
