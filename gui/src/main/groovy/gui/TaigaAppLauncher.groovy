package gui

import javax.swing.LookAndFeel

import com.jgoodies.looks.plastic.*
import com.jgoodies.looks.plastic.theme.*

import org.viewaframework.core.DefaultApplicationLauncher

class TaigaAppLauncher extends DefaultApplicationLauncher {

    LookAndFeel getLookAndFeel() {
        def lookAndFeel = new PlasticLookAndFeel()
        lookAndFeel.setCurrentTheme(new ExperienceBlue())

        return lookAndFeel
    }

}
