package GUI;

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
    public void printLogMessage (String msg)
    {
        log.appendText(msg + "\n");
    }
}
