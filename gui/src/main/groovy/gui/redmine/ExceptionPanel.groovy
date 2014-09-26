package gui.redmine

import java.awt.*
import javax.swing.*
import org.jdesktop.swingx.*
import org.jdesktop.swingx.error.*

/**
*
* @author mario
*/
public class ExceptionPanel extends JXTitledPanel {

    private JLabel icon
    private Throwable throwable
    private JXErrorPane errorPane
	private static final long serialVersionUID = -1763180926774257415L;

   public ExceptionPanel(Throwable throwable) {
        super()
        this.icon = new JLabel()
        this.throwable = throwable
        this.errorPane = new JXErrorPane()
   }

    ExceptionPanel initComponents() {
        this.icon.name = 'iconLabel'
        this.leftDecoration = icon
        this.errorPane.getUI().closeButton.name = 'closeButton'
        this.errorPane.setErrorInfo(
            new ErrorInfo("Fatal Error", throwable.message, null, null, throwable, null, null)
        )
        this.add(errorPane)
        this.name = 'exceptionPanel'
        return this
    }

}

