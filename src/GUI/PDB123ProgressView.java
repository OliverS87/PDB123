package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

/**
 * Created by oliver on 06.02.16.
 * View class for progressbar window.
 * Defines controls for progressbar window.
 */
public class PDB123ProgressView extends VBox {
    private ProgressBar progressBar;
    private Label label;
    private Button button;

    public PDB123ProgressView() {
        initControls();
        setLayout();
    }
    private void  initControls()
    {
        progressBar = new ProgressBar();
        label = new Label();
        button = new Button("Cancel");
    }
    private void setLayout()
    {
        this.getChildren().addAll(progressBar, label, button);
        this.setSpacing(2);
        this.setPadding(new Insets(2));
        this.setAlignment(Pos.TOP_CENTER);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Label getLabel() {
        return label;
    }

    public Button getButton() {
        return button;
    }
}
