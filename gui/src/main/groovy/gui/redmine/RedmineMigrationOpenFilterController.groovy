package gui.redmine

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import gui.filter.*
import gui.controller.*

import org.viewaframework.controller.*

class RedmineMigrationOpenFilterController extends AbstractOpenerController<ActionListener,ActionEvent> {

    RedmineMigrationOpenFilterController() {
       super(new FilterResultListView())
    }

    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

}
