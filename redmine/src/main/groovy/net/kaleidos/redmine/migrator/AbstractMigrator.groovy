package net.kaleidos.redmine.migrator

import net.kaleidos.redmine.RedmineClient
import net.kaleidos.taiga.TaigaClient

import com.taskadapter.redmineapi.bean.User as RedmineUser
import com.taskadapter.redmineapi.bean.Journal as RedmineHistory
import com.taskadapter.redmineapi.bean.Attachment as RedmineAttachment

import net.kaleidos.domain.User as TaigaUser
import net.kaleidos.domain.History as TaigaHistory
import net.kaleidos.domain.Attachment as TaigaAttachment

import groovy.util.logging.Log4j

@Log4j
abstract class AbstractMigrator<A> implements Migrator<A> {

    final TaigaClient taigaClient
    final RedmineClient redmineClient

    AbstractMigrator(final RedmineClient redmineClient, final TaigaClient taigaClient) {
        this.redmineClient = redmineClient
        this.taigaClient = taigaClient
    }

    def executeSafelyAndWarn = { action ->
        return { source ->
            try {
                action(source)
            } catch(Throwable e) {
                log.warn(e.message)
            }
        }
    }

    TaigaAttachment convertToTaigaAttachment(RedmineAttachment att) {
        RedmineUser user = redmineClient.findUserFullById(att.author.id)
        byte[] attachmentData = redmineClient.downloadAttachment(att)

        return new TaigaAttachment(
            data: attachmentData.encodeBase64(),
            name: att.fileName,
            description: att.description,
            createdDate: att.createdOn,
            owner: user.mail
        )

    }

    TaigaHistory convertToTaigaHistory(RedmineHistory journal) {
        RedmineUser redmineUser = redmineClient.findUserFullById(journal.user.id)
        TaigaUser taigaUser =
            new TaigaUser(
                name: redmineUser.fullName,
                email: redmineUser.mail)

        return new TaigaHistory(
            user: taigaUser,
            createdAt: journal.createdOn,
            comment: journal.notes
        )
    }


}
