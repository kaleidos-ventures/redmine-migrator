package gui.redmine

import gui.redmine.*
import gui.controller.*
import com.taskadapter.redmineapi.bean.Project

import org.viewaframework.annotation.*
import org.viewaframework.widget.view.*
import org.viewaframework.widget.view.ui.*
import org.viewaframework.view.DefaultViewContainer

@Controllers([
    @Controller(type=RedmineMigrationWarningController,pattern='migrateSelected'),
    @Controller(type=RedmineProjectListController,pattern='refresh')
])
class RedmineProjectListView extends MasterViewEditor<Project> {

    static final ID = 'projectListViewID'

    RedmineProjectListView() {
        super(ID, [
            new MasterViewColumn("id", 10),
            new MasterViewColumn("name", 100),
            new MasterViewColumn("identifier", 100),
            new MasterViewColumn("description", 500),
            new MasterViewColumn("createdOn", 75, new gui.ui.DateCellRenderer()),
            new MasterViewColumn("updatedOn", 75, new gui.ui.DateCellRenderer()),
        ])
    }

    Class<Project> getMasterType() {
        return Project
    }

}
