package GUI.LogMessagesUI;

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
    public void printLogMessage(String msg) {

        Platform.runLater(() -> {
            log.appendText(msg + "\n");
        });
    }
    public void showInitalMessage()
    {
        log.setText("Welcome to PDB123!\n" +
                "Load a PDB file to explore this amazing tool.\n" +
                "Use mouse to rotate 2D and 3D representations.\n" +
                "Use ctrl+mouse to move the 2D and 3D structures.\n" +
                "Use shift+mouse to zoom in and out.\n" +
                "Click a 1D, 2D or 3D nucleotide to highlight this position\n" +
                "in all 3 representations.\n" +
                "Use shift+mouse click to select multiple nucleotides.\n" +
                "Have a look at settings for further options.\n");
    }
}
