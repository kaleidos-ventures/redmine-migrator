package gui.controller

import static org.viewaframework.util.ComponentFinder.find

import javax.swing.*
import java.awt.event.ActionEvent
import groovy.util.logging.Log4j

import org.viewaframework.view.*
import org.viewaframework.util.*
import org.viewaframework.view.perspective.*
import org.viewaframework.widget.view.*

import gui.swingx.JXBusyFeedbackLabel
import gui.migration.MigrationProgress
import gui.migration.MigrationProgressView
import gui.controller.DefaultActionViewControllerWorker

@Log4j
abstract class MigrationProgressAwareController extends DefaultActionViewControllerWorker<MigrationProgress> {

    @Override
    void preHandlingView(ViewContainer view, ActionEvent event) {
        viewManager.removeView(view)
        viewManager.addView(new MigrationProgressView())
    }

    /**
     * This method shouldnt exist, but because Viewa framework is an old framework, it is not
     * prepare for certain behaviors. So when something happens really quick the postHandleView
     * can be executed before the handleView method. It is a bug, but it is something that is
     * in the core of it, so it is more likely to change the framework than fixing that particular
     * bug.
     */
    public void needsToBeRead(Long ms) {
        Thread.sleep(ms)
    }

    @Override
    void handleViewPublising(ViewContainer view, ActionEvent event, List<MigrationProgress> chunks) {
        MigrationProgress migrationProgress = chunks.first()
        setProgressFeedback(migrationProgress)

        if (migrationProgress.exception) {
            log.error("Exception while migrating: ${migrationProgress.exception.message}")
            busyLabel.setFailure()
            return
        }

        if (migrationProgress.progress >= 1) {
            busyLabel.setSuccess()
            return
        }
    }

    @Override
    void postHandlingView(ViewContainer view, ActionEvent event) {
        closeButton.enabled = true
    }

    void publishSuccess() {
        publish(
            new MigrationProgress(
                message:"Finished Successfully. ",
                progress:1.0
            )
        )
    }

    void publishSuccessWithMessage(String message) {
        publish(
            new MigrationProgress(
                message: message,
                progress: 1.0
            )
        )
    }

    void publishFailure(Throwable th) {
        publish(
            new MigrationProgress(
                exception: th,
                message: "Failed!!! Please check log",
                progress: 1.0
            )
        )
    }

    void publishProgress(String message, Float progress) {
        publish(
            new MigrationProgress(
                message: message,
                progress: progress
            )
        )
    }

    void publishIndeterminate(String message) {
        publish(
            new MigrationProgress(
                message: message,
                progress: -1.0
            )
        )
    }

    void setProgressFeedback(MigrationProgress migrationProgress) {
        progressMessageLabel.text = migrationProgress.message
        if (migrationProgress.progress < 0) {
            migrationProgressBar.indeterminate = true
            return
        }
        migrationProgressBar.indeterminate = false
        migrationProgressBar.setValue((migrationProgress.progress * 100).intValue())
    }

    JXBusyFeedbackLabel getBusyLabel() {
        return find(JXBusyFeedbackLabel).in(progressView).named('outputIconLabel')
    }

    JButton getCloseButton() {
        return find(JButton).in(progressView).named('closeButton')
    }

    JProgressBar getMigrationProgressBar() {
        return find(JProgressBar).in(progressView).named('migrationProgressBar')
    }

    JLabel getProgressMessageLabel() {
        return find(JLabel).in(progressView).named('loggingProgress')
    }

    MigrationProgressView getProgressView() {
        return locate(MigrationProgressView).named(MigrationProgressView.ID)
    }

}
