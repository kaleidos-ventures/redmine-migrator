package gui.taiga

import gui.controller.*
import net.kaleidos.domain.Project

import org.viewaframework.annotation.*
import org.viewaframework.widget.view.*
import org.viewaframework.widget.view.ui.*
import org.viewaframework.view.DefaultViewContainer

@Controllers([
    @Controller(type=TaigaProjectDeletionWarningController, pattern='deleteSelected'),
    @Controller(type=TaigaProjectListController, pattern='refresh'),
])
class TaigaProjectListView extends MasterView<Project> {

    static final ID = 'taigaProjectListViewID'

    TaigaProjectListView() {
        super(ID, [
            new MasterViewColumn("id", 10),
            new MasterViewColumn("slug", 200),
            new MasterViewColumn("name", 200),
            new MasterViewColumn("description", 600),
        ])
    }

    Class<Project> getMasterType() {
        return Project
    }

}
