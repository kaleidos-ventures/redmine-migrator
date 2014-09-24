package gui.redmine

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import org.viewaframework.view.*
import org.viewaframework.util.*
import org.viewaframework.controller.*
import org.viewaframework.view.perspective.*
import org.viewaframework.widget.view.*

import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory
import com.taskadapter.redmineapi.bean.Project

import gui.controller.DefaultViewControllerWorker

class MigrateSelectedController extends
    DefaultViewControllerWorker<ActionListener, ActionEvent, String, Project> {

    @Override
    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        println "======================================================"
        println "======================================================"
        println locate(ProjectListView.ID).model.selectedObjects
        println "======================================================"
        println "======================================================"
        //publish(redmineManager.projects)
    }

    @Override
    void handleViewPublising(ViewContainer view, ActionEvent event, List<Project> chunks) {

    }
}
