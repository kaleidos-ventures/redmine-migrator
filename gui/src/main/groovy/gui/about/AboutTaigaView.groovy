package gui.about

import gui.controller.CloseViewController
import org.viewaframework.annotation.*
import org.viewaframework.widget.view.AboutView
import groovy.transform.InheritConstructors

@InheritConstructors
@Controllers([
    @Controller(type=CloseViewController,pattern="aboutClose")
])
class AboutTaigaView extends AboutView {


}
