package gui.redmine

import gui.controller.*
import com.taskadapter.redmineapi.bean.Project

import org.viewaframework.annotation.*
import org.viewaframework.widget.view.*
import org.viewaframework.widget.view.ui.*
import org.viewaframework.view.DefaultViewContainer

@Controllers([
    @Controller(type=MigrateSelectedController,pattern='migrateSelected'),
    @Controller(type=CloseViewController,pattern='closeTab')
])
class ProjectListView extends MasterView<Project> {

    static final ID = 'projectListViewID'

    ProjectListView() {
        super(ID, [
            new MasterViewColumn("id", 100),
            new MasterViewColumn("name", 100),
            new MasterViewColumn("identifier", 100),
            new MasterViewColumn("description", 200),
            new MasterViewColumn("createdOn", 50),
            new MasterViewColumn("updatedOn", 50),
        ])
    }

    Class<Project> getMasterType() {
        return Project
    }

}
