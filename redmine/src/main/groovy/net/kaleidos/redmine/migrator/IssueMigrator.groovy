package net.kaleidos.redmine.migrator

import com.taskadapter.redmineapi.bean.Attachment as RedmineAttachment
import com.taskadapter.redmineapi.bean.Issue as RedmineIssue
import com.taskadapter.redmineapi.bean.Journal as RedmineHistory
import com.taskadapter.redmineapi.bean.User as RedmineUser
import com.taskadapter.redmineapi.internal.Transport.Pagination

import groovy.util.logging.Log4j
import groovy.transform.InheritConstructors

import net.kaleidos.domain.Attachment as TaigaAttachment
import net.kaleidos.domain.History as TaigaHistory
import net.kaleidos.domain.Issue as TaigaIssue
import net.kaleidos.domain.Project as TaigaProject
import net.kaleidos.domain.User as TaigaUser
import net.kaleidos.redmine.RedmineTaigaRef

@Log4j
@InheritConstructors
class IssueMigrator extends AbstractMigrator<TaigaIssue> {

    final String SEVERITY_NORMAL = 'Normal'

    List<TaigaIssue> migrateIssuesByProject(final RedmineTaigaRef ref, Closure<Void> feedback = {}) {
        Iterator<Pagination<TaigaIssue>> issueIterator =
            redmineClient.findAllIssueByProjectIdentifier(ref.redmineIdentifier)

        Closure<Boolean> thereIsNext = { Pagination pagination -> pagination.list }
        Closure<Void> keepPosted = { Pagination p->
            feedback("Processing issues ${p.offset}-${p.offset + p.list.size()} / ${p.total}")
        }

        log.debug("Migrating issues from Redmine project ${ref.redmineIdentifier}")

        def workflow =
            this.&populateIssue >>
            this.&addRedmineIssueToTaigaProject.rcurry(ref.project) >>
            this.&save

        return issueIterator
            .takeWhile(thereIsNext) // issueIterator is an infinite stream
            .collect { Pagination pagination ->
                pagination.with(keepPosted) // keeping the UI informed
                pagination
                    .list
                    .findResults(executeSafelyAndWarn(workflow))
            }
            .flatten() // return all processed issues
    }

    RedmineIssue populateIssue(final RedmineIssue basicIssue) {
        return redmineClient.findIssueById(basicIssue.id)
    }

    TaigaIssue addRedmineIssueToTaigaProject(
        final RedmineIssue source,
        final TaigaProject taigaProject) {

        return new TaigaIssue(
            ref: source.id,
            project: taigaProject,
            type: source.tracker.name,
            status: source.statusName,
            priority: source.priorityText,
            severity: SEVERITY_NORMAL,
            subject: source.subject,
            description: source.with { description ?: subject },
            createdDate: source.createdOn,
            finishedDate: source.dueDate,
            owner: getUserMail(source.author),
            assignedTo: getUserMail(source.assignee),
            attachments: extractIssueAttachments(source),
            history: extractIssueHistory(source)
        )

    }

    private String getUserMail(RedmineUser user) {
        return user ? redmineClient.findUserFullById(user.id).mail : null
    }

    List<TaigaAttachment> extractIssueAttachments(final RedmineIssue issue) {
        log.debug("Adding ${issue.attachments.size()} attachments to issue ${issue.subject}")

        def attachments =
            issue
                .attachments
                .collect(executeSafelyAndWarn(this.&convertToTaigaAttachment))
                .findAll { it } // TODO replace with findResults

        return attachments
    }


    List<TaigaHistory> extractIssueHistory(final RedmineIssue issue) {
        //We are only interested in migrating those historic entries
        //containing notes, not data changes entries
        def comments =
            issue
                .journals
                .findAll { it.notes }
                .collect(this.&convertToTaigaHistory)

        return comments
    }

    @Override
    TaigaIssue save(TaigaIssue issue) {
        log.debug("Saving issue from -${issue.project.name}- : ${issue.subject} ")
        return taigaClient.createIssue(issue)
    }

}
