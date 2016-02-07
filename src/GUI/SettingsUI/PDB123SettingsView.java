package GUI.SettingsUI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

/**
 * Created by oliver on 06.02.16.
 * Controls and other elements for settings view
 */
public class PDB123SettingsView extends BorderPane {
    private Button ok;
    private ColorPicker colorPickAdeSel, colorPickAdeUnSel, colorPickCytSel, colorPickCytUnSel, colorPickGuaSel, colorPickGuaUnSel, colorPickUraSel, colorPickUraUnSel, colorPickPyrSel, colorPickPyrUnSel, colorPickPurSel, colorPickPurUnSel;
    private Slider hBondSensitivity;
    private Label labelSlider;
    private CheckBox pseudoKnotDetection;
    private GridPane settingsPane;
    private RadioButton colorByNt, colorByBase;


    public PDB123SettingsView() {
        initControls();
        formatControls();
        initLayout();
    }

    private void initControls() {
        this.ok = new Button("OK");
        this.labelSlider = new Label("H-Bond detection sensitivity");
        this.hBondSensitivity = new Slider(0.8, 1.2, 0.1);
        // Init color pickers with nucleotides default colors
        this.colorByNt = new RadioButton("Color by nucleotide type");
        this.colorByBase = new RadioButton("Color by purine/pyrimidine");
        this.colorPickAdeSel = new ColorPicker(Color.web("#6afc38"));
        this.colorPickAdeUnSel = new ColorPicker(Color.web("#6a9738"));
        this.colorPickCytSel = new ColorPicker(Color.web("#caff38"));
        this.colorPickCytUnSel = new ColorPicker(Color.web("#cab938"));
        this.colorPickGuaSel = new ColorPicker(Color.CYAN);
        this.colorPickGuaUnSel = new ColorPicker(Color.web("#589aff"));
        this.colorPickUraSel = new ColorPicker(Color.web("#ff0064"));
        this.colorPickUraUnSel = new ColorPicker(Color.web("#c34a41"));
        this.colorPickPurSel = new ColorPicker(Color.DARKBLUE.invert());
        this.colorPickPurUnSel = new ColorPicker(Color.DARKBLUE);
        this.colorPickPyrSel = new ColorPicker(Color.DARKRED.invert());
        this.colorPickPyrUnSel = new ColorPicker(Color.DARKRED);
        this.settingsPane = new GridPane();
        this.pseudoKnotDetection = new CheckBox("Pseudoknot detection (experimental)");

    }

    private void formatControls() {
        hBondSensitivity.setShowTickMarks(true);
        hBondSensitivity.setShowTickLabels(true);
        hBondSensitivity.setValue(1.);
        hBondSensitivity.setMajorTickUnit(0.2f);
        hBondSensitivity.setBlockIncrement(0.1f);
        settingsPane.setHgap(10);
        settingsPane.setVgap(10);
        settingsPane.setPadding(new Insets(0, 10, 0, 10));
        // Slider should provide three tick-labels:
        // 1. Low sensitivty at the far left
        // 2. Medium sensitivity at the middle
        // 3. High sensitivity at the far right
        hBondSensitivity.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                String label = "";
                if (object >= 0.75) label = "Low sensitivity";
                if (object >= 0.95) label = "Medium sensitivity";
                if (object >= 1.1) label = "High sensitivity";
                return label;
            }

            @Override
            // Not needed here but requiered to implement
            public Double fromString(String string) {
                return null;
            }
        });
    }


    private void initLayout() {

        // Force the setting of exactly one colormode
        // by using a ToggleGroup
        ToggleGroup colorBy = new ToggleGroup();
        colorBy.getToggles().addAll(colorByNt, colorByBase);
        colorByNt.setSelected(true);

        this.setTop(new Text("Settings"));
        settingsPane.add(labelSlider, 0, 0, 7, 1);
        settingsPane.add(hBondSensitivity, 1, 1, 5, 1);
        settingsPane.add(pseudoKnotDetection, 1, 2, 5, 1);
        settingsPane.add(colorByNt, 0, 3, 3, 1);
        settingsPane.add(colorByBase, 3, 3, 3, 1);
        settingsPane.add(new Text("Selected"), 0, 5);
        settingsPane.add(new Text("Unselected"), 0, 6);
        settingsPane.add(new Text("Adenosine"), 1, 4);
        settingsPane.add(new Text("Cytidine"), 2, 4);
        settingsPane.add(new Text("Guanosine"), 3, 4);
        settingsPane.add(new Text("Uridine"), 4, 4);
        settingsPane.add(new Text("Purine"), 5, 4);
        settingsPane.add(new Text("Pyrimidine"), 6, 4);
        settingsPane.add(colorPickAdeSel, 1, 5);
        settingsPane.add(colorPickAdeUnSel, 1, 6);
        settingsPane.add(colorPickCytSel, 2, 5);
        settingsPane.add(colorPickCytUnSel, 2, 6);
        settingsPane.add(colorPickGuaSel, 3, 5);
        settingsPane.add(colorPickGuaUnSel, 3, 6);
        settingsPane.add(colorPickUraSel, 4, 5);
        settingsPane.add(colorPickUraUnSel, 4, 6);
        settingsPane.add(colorPickPurSel, 5, 5);
        settingsPane.add(colorPickPurUnSel, 5, 6);
        settingsPane.add(colorPickPyrSel, 6, 5);
        settingsPane.add(colorPickPyrUnSel, 6, 6);

        this.setCenter(settingsPane);
        HBox buttonBox = new HBox(20);
        buttonBox.getChildren().addAll(ok);
        buttonBox.setPadding(new Insets(5));
        this.setBottom(buttonBox);

    }

    public ColorPicker getColorPickAdeSel() {
        return colorPickAdeSel;
    }

    public ColorPicker getColorPickAdeUnSel() {
        return colorPickAdeUnSel;
    }

    public ColorPicker getColorPickCytSel() {
        return colorPickCytSel;
    }

    public ColorPicker getColorPickCytUnSel() {
        return colorPickCytUnSel;
    }

    public ColorPicker getColorPickGuaSel() {
        return colorPickGuaSel;
    }

    public ColorPicker getColorPickGuaUnSel() {
        return colorPickGuaUnSel;
    }

    public ColorPicker getColorPickUraSel() {
        return colorPickUraSel;
    }

    public ColorPicker getColorPickUraUnSel() {
        return colorPickUraUnSel;
    }

    public ColorPicker getColorPickPyrSel() {
        return colorPickPyrSel;
    }

    public ColorPicker getColorPickPyrUnSel() {
        return colorPickPyrUnSel;
    }

    public ColorPicker getColorPickPurSel() {
        return colorPickPurSel;
    }

    public ColorPicker getColorPickPurUnSel() {
        return colorPickPurUnSel;
    }

    public Button getOk() {
        return ok;
    }

    public Slider gethBondSensitivity() {
        return hBondSensitivity;
    }

    public CheckBox getPseudoKnotDetection() {
        return pseudoKnotDetection;
    }

    public RadioButton getColorByNt() {
        return colorByNt;
    }

}
