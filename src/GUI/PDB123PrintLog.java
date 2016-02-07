package GUI;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Created by oliver on 26.01.16.
 * Simple text output to a textarea
 * Common object for all classes to connect
 * with a textarea on the GUI
 */
public class PDB123PrintLog {
    private TextArea log;

    public PDB123PrintLog(TextArea log) {
        this.log = log;
    }
    // Print a String message to the log textarea
    // Function contains a Platform.runlater call
    // Necessary when accessing this function from outside
    // the JavaFX application thread. Otherwise the UI
    // might get out of sync
    public void printLogMessage (String msg)
    {

        Platform.runLater(() -> {
            log.appendText(msg + "\n");
        });
    }
}
