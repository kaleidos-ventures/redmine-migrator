package gui.redmine

import static org.viewaframework.util.ComponentFinder.find

import groovy.util.logging.Log4j

import java.awt.event.ActionEvent
import org.viewaframework.view.ViewContainer

import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory
import com.taskadapter.redmineapi.bean.Project

import fnz.data.Try

import gui.settings.Settings
import gui.settings.SettingsService
import gui.migration.MigrationProgress
import gui.migration.MigrationProgressView
import gui.controller.MigrationProgressAwareController

import net.kaleidos.taiga.TaigaClient
import net.kaleidos.redmine.RedmineClientFactory
import net.kaleidos.redmine.migrator.RedmineMigrator

@Log4j
class RedmineMigrationController extends MigrationProgressAwareController {

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        log.debug("Getting selected objects")

        Try result = $do {
            service  = Try { new SettingsService() }
            settings = Try { service.loadSettings() }
            _        = Try { checkAvailability(service, settings) }
            migrator = buildMigratorWithSettings(settings)
            total    = Try { migrateProjectsWith(selectedProjectList, migrator) }

            $return total
        }

        publishResult(result)
    }

    void checkAvailability(final SettingsService service, final Settings settings) {
        if (!service.areServicesUp(settings.redmineUrl, settings.taigaUrl)) {
            throw new IllegalStateException("Please check your connections!!")
        }
    }

    void publishResult(final Try.Success success) {
        publishSuccess()
    }

    void publishResult(final Try.Failure failure) {
        publishFailure(failure.exception)
    }

    Integer migrateProjectsWith(final List<Project> projects, final RedmineMigrator migrator) {
        Integer total = projects.size()

        log.debug("Migrating $total projects")

        projects.eachWithIndex { Project project, Integer index ->
            migrator.migrateProject(
                project,
                getProgressClosure(project.name, (index + 1).div(total + 1))
            )
        }

        return total
    }

    List<Project> getSelectedProjectList() {
       return locate(RedmineProjectListView.ID).model.selectedObjects
    }

    Try<RedmineMigrator> buildMigratorWithSettings(final Settings settings) {
        return $do {
            redmine  = Try { RedmineClientFactory.newInstance(settings.properties) }
            taiga    = Try { initTaigaClientWith(settings) }

            $return new RedmineMigrator(redmine, taiga)
        }
    }

    TaigaClient initTaigaClientWith(final Settings settings) {
        return new TaigaClient(settings.taigaUrl).authenticate(
            settings.taigaUsername,
            settings.taigaPassword
        )
    }

    Closure<Void> getProgressClosure(final String projectName, final BigDecimal overallProgress) {
        return { String msg, BigDecimal progress = overallProgress ->
             publish(new MigrationProgress(projectName: projectName, message: msg, progress: progress))
        }
    }

}
