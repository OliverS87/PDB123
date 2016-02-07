package GUI.SettingsUI;

import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by oliver on 06.02.16.
 * Add functionality to the settings view elements
 * defined in PDB123SettingsView
 * Settings provides these functionalities:
 * 1. Set H-Bond detection sensitivity (low to high)
 * 2. Activate pseudoknot detection
 * 3. Set nucleotide color mode
 * 4. Choose colors for nucleotides
 */
public class PDB123SettingsPresenter {
    private PDB123SettingsView settingsView;
    private Stage settingsStage;
    private StringProperty colorMode;
    private BooleanProperty detectPseudoknots = new SimpleBooleanProperty();
    private DoubleProperty detectionSensitivity = new SimpleDoubleProperty(1.);
    private Boolean needToRedrawStructure = false;
    private BooleanProperty redraw = new SimpleBooleanProperty(false);

    public PDB123SettingsPresenter() {
        this.settingsView = new PDB123SettingsView();
        this.colorMode = new SimpleStringProperty();
        addListener();
        setButtonAction();
        setStage();
    }

    private void addListener()
    {
       BooleanProperty colorByNt = settingsView.getColorByNt().selectedProperty();
        // Set default colormode
        colorMode.setValue("resType");
        colorByNt.addListener((observable, oldValue, newValue) -> {
            if(newValue) colorMode.setValue("resType");
            else colorMode.setValue("baseType");
        });
        detectPseudoknots.bind(settingsView.getPseudoKnotDetection().selectedProperty());
        Slider hBondSensitivitySlider = settingsView.gethBondSensitivity();
        // Need to reverse slider value: We need a high value for low sensitivity, and a low value
        // for high sensitivity. As slider values cannnot be shown in reverse order, we are reversing
        // the values here according to this complex formula:
        // Inverse-Value = MaxValue - Value + MinValue
        detectionSensitivity.bind(hBondSensitivitySlider.maxProperty().subtract(hBondSensitivitySlider.valueProperty()).add(hBondSensitivitySlider.minProperty()));
        // If the sensitivity slider or the pseudoknot detection checkbox have changed values, we need to redraw the 1D, 2D, 3D structures
        hBondSensitivitySlider.valueProperty().addListener(observable -> {
            needToRedrawStructure = true;
        });
        settingsView.getPseudoKnotDetection().selectedProperty().addListener(observable -> {
            needToRedrawStructure = true;
        });
    }
    // Button closes the settings stage
    // Value of local boolean needToRedrawStructure is
    // set to the public boolean property redraw.
    // Setting this value only upon closing the settings window
    // prevents redrawing before all new settings are made.
    private void setButtonAction()
    {
        settingsView.getOk().setOnAction(event -> {
            redraw.setValue(needToRedrawStructure);
            settingsStage.close();
        });
    }

    private void setStage()
    {
        // Keep settings window on top
        // Prevent actions in other window while settings window
        // is open
        this.settingsStage = new Stage();
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setAlwaysOnTop(true);
        Scene settingsScene = new Scene(settingsView,520,350);
        settingsScene.getStylesheets().add("GUI/settingsStyle.css");
        settingsStage.setScene(settingsScene);
    }


    // Getter for stage
    // When a new settings stage is created,
    // reset the boolean values for redraw structure to false
    public Stage getSettingsStage() {
        redraw.setValue(false);
        needToRedrawStructure=false;
        return settingsStage;
    }

    // Other getters
    public ObjectProperty<Color> adeUnselectedColorProperty()
    {
        return settingsView.getColorPickAdeUnSel().valueProperty();
    }
    public ObjectProperty<Color> adeSelectedColorProperty()
    {
        return settingsView.getColorPickAdeSel().valueProperty();
    }
    public ObjectProperty<Color> cytUnselectedColorProperty()
    {
        return settingsView.getColorPickCytUnSel().valueProperty();
    }
    public ObjectProperty<Color> cytSelectedColorProperty()
    {
        return settingsView.getColorPickCytSel().valueProperty();
    }
    public ObjectProperty<Color> guaUnselectedColorProperty()
    {
        return settingsView.getColorPickGuaUnSel().valueProperty();
    }
    public ObjectProperty<Color> guaSelectedColorProperty()
    {
        return settingsView.getColorPickGuaSel().valueProperty();
    }
    public ObjectProperty<Color> uraUnselectedColorProperty()
    {
        return settingsView.getColorPickUraUnSel().valueProperty();
    }
    public ObjectProperty<Color> uraSelectedColorProperty()
    {
        return settingsView.getColorPickUraSel().valueProperty();
    }
    public ObjectProperty<Color> pyrUnselectedColorProperty()
    {
        return settingsView.getColorPickPyrUnSel().valueProperty();
    }
    public ObjectProperty<Color> pyrSelectedColorProperty()
    {
        return settingsView.getColorPickPyrSel().valueProperty();
    }
    public ObjectProperty<Color> purUnselectedColorProperty()
    {
        return settingsView.getColorPickPurUnSel().valueProperty();
    }
    public ObjectProperty<Color> purSelectedColorProperty()
    {
        return settingsView.getColorPickPurSel().valueProperty();
    }


    public StringProperty colorModeProperty() {
        return colorMode;
    }

    public boolean getDetectPseudoknots() {
        return detectPseudoknots.get();
    }



    public DoubleProperty detectionSensitivityProperty() {
        return detectionSensitivity;
    }


    public BooleanProperty redrawProperty() {
        return redraw;
    }
}
