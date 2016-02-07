package GUI.ProgressUI;

import PDBParser.PDB123PresenterStructureTask;
import javafx.beans.binding.Bindings;
import javafx.stage.Stage;

/**
 * Created by oliver on 06.02.16.
 * Presenter class for progressbar window.
 * Assigns functionality to elements defined
 * in PDB123ProgressView.
 * Binds properties of a task to UI controls.
 */
public class PDB123ProgressPresenter {
    private PDB123ProgressView progressView;
    private PDB123PresenterStructureTask task;
    private Stage progressStage;

    public PDB123ProgressPresenter(PDB123PresenterStructureTask task, Stage progressStage) {
        progressView = new PDB123ProgressView();
        this.task = task;
        this.progressStage = progressStage;
        setBindings();
    }


    private void setBindings() {
        // Bind progressbar progress to progress of task
        progressView.getProgressBar().progressProperty().bind(task.progressProperty());
        // Show messages from the task at label
        progressView.getLabel().textProperty().bind(task.messageProperty());
        // React to exceptions thrown by the task
        // Show error message at label
        task.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            // If task is canceled, don't show any exceptions thrown
            // by the cancel-event.
            if (task.isCancelled()) return;
            // Unbind label textproperties to allow the
            // setting of an individual message.
            progressView.getLabel().textProperty().unbind();
            progressView.getLabel().setText("Sadly, an error occurred.");
        });
        // Change labeling of button depending on state of task
        // -> Cancel when task is running
        // -> OK if some error or canceling by user occurred
        progressView.getButton().textProperty().bind(Bindings.when(task.runningProperty()).then("cancel").otherwise("ok"));
        // Automatically close window when task has succeeded
        task.setOnSucceeded(event1 -> progressStage.close());
        // If task was canceled by user, show a message. Don't close window automatically.
        task.setOnCancelled(event1 -> {
            progressView.getLabel().textProperty().unbind();
            progressView.getLabel().setText("Canceled by user.");
        });
        // Set button actions:
        // -> While task is running: Cancel task
        // -> If task failed/canceled: Close window
        // No functionality added for when task is succeeded because
        // in that case window closes automatically
        progressView.getButton().setOnAction(event -> {
            if (task.isRunning()) {
                task.cancel();
            } else {
                progressStage.close();
            }
        });
    }

    public PDB123ProgressView getProgressView() {
        return progressView;
    }
}
