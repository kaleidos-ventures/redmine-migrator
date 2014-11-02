package gui.about

import org.viewaframework.annotation.*
import org.viewaframework.widget.view.AboutView
import groovy.transform.InheritConstructors
import org.viewaframework.widget.controller.ExitActionController

@Controllers([
    @Controller(type=ExitActionController,pattern="aboutClose")
])
@InheritConstructors
class AboutTaigaView extends AboutView {


}
