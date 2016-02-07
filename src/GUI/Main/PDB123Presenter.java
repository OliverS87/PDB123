package GUI.Main;

import Charts.PDBBarChart;
import PDBParser.PDB123PresenterStructureTask;
import GUI.LogMessagesUI.PDB123PrintLog;
import GUI.ProgressUI.PDB123ProgressPresenter;
import GUI.SettingsUI.PDB123SettingsPresenter;
import SelectionModel.PDB123SelectionModel;
;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by oliver on 19.01.16.
 * Presenter class for PDB123 MVP design pattern
 * Adds functionality to view elements
 */
public class PDB123Presenter {
    // View elements
    private GUI.Main.PDB123View PDB123View;
    // 2D and 3D transformations
    private Rotate rotateTertStructureX, rotateTertStructureY, rotateSecStructure;
    private Translate translateTertCam, translateSecStructure;
    private Scale scaleSecStructure;
    // Mouse position coordinates
    private double mouseXold, mouseXnew, mouseYold, mouseYNew, mouseXDelta, mouseYDelta;
    // Message output
    private PDB123PrintLog printLog;
    // Booleanproperties to select 3D components to show
    private BooleanProperty showBackbone, showSugar, showNucleoBase, showHBonds;
    // Settings window
    private PDB123SettingsPresenter settingsWindow;
    // Barchart plot window
    private PDBBarChart barChart;
    // Path to currently loaded PDB file
    private String path;

    // Constructor
    public PDB123Presenter(Stage primaryStage) {
        // Initialize class variables
        initClassVariables();
        // Add functionality to menubar elements
        // Set functions to menu items of file menu
        loadFileFunction();
        exitFunction();
        // Set functions for menu items of edit menu
        editFunctions();
        // Set functions for menu items of view menu
        viewFunctions();
        // Set 3D camera
        set3DCamera();
        // Initialize 2D and 3D transformations
        set2DTransformations();
        set3DTransformations();
        // Set 2D and 3D subscene Mouse actions
        setMouseActions2D();
        setMouseActions3D();
        // Set checkbox actions
        setCheckboxes();
        // Set center-button actions
        setCenterButtons();
        // Set redraw bindings
        redrawStructureBindings();


    }

    // Initialize class variables
    private void initClassVariables() {
        // Init. UI components
        this.PDB123View = new PDB123View();
        this.printLog = new PDB123PrintLog(PDB123View.getLog());
        printLog.showInitalMessage();
        this.barChart = new PDBBarChart();
        this.settingsWindow = new PDB123SettingsPresenter();
        // Init. boolean bindings
        this.showNucleoBase = new SimpleBooleanProperty(true);
        this.showSugar = new SimpleBooleanProperty(true);
        this.showBackbone = new SimpleBooleanProperty(true);
        this.showHBonds = new SimpleBooleanProperty(true);
        // Init. selection model
        PDB123SelectionModel.initSelectionModel();

    }


    // Menu File -> Load file
    private void loadFileFunction() {
        PDB123View.getMenuItemOpen().setOnAction(event -> {
            // Try to load a file
            // If file loading canceled -> print message
            // else: print file name
            path = null;
            try {
                path = showFileChooser();
            } catch (Exception e) {
                printLog.printLogMessage("No file loaded");
            }
            if (path == null) return;
            String fileName = path.substring(Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\')) + 1);
            printLog.printLogMessage("File loaded: " + fileName);
            // Reset any applied transformation in 2D and 3D
            reset2DTransformations();
            reset3DTransformations();
            // Employ a background task to load 1,2 and 3D structure from PDB file
            loadStructureTask();
        });
    }

    // Menu File -> exit
    private void exitFunction() {
        PDB123View.getMenuItemExit().setOnAction(event -> System.exit(0));
    }

    // Menu Edit -> Unselect all
    // Menu Edit -> Settings
    private void editFunctions()
    {
        // Unselect all nucleotides
        PDB123View.getMenuItemClearSelection().setOnAction(event -> PDB123SelectionModel.unselectAll());
        // Show settings window
        PDB123View.getMenuItemSettings().setOnAction(event -> {
            settingsWindow.getSettingsStage().show();
        });
    }
    // Menu View -> Show statistics
    private void viewFunctions()
    {
        // Show bar chart window
        PDB123View.getMenuItemStats().setOnAction(event -> {
            barChart.getBarChartStage().show();
        });
    }

    // Set 3D perspective camera
    // Set the near and farclip
    // Fix the camera XYZ position at the center of the subscene
    private void set3DCamera()
    {
        PerspectiveCamera cam = PDB123View.get3DCamera();
        SubScene subScene3D = PDB123View.get3DSubScene();
        // Set the near and farclip
        cam.setNearClip(0.0000001);
        cam.setFarClip(1000.0);
        // Keep camera in the center of the subscene
        // by binding camera XYZ to subscene width and height.
        // Does not affect user-defined translations
        // as these translations are added "on-top" of
        // the defined camera position
        cam.translateXProperty().bind(subScene3D.widthProperty().divide(-2));
        cam.translateYProperty().bind(subScene3D.heightProperty().divide(-2));
        cam.translateZProperty().bind(subScene3D.widthProperty().add(subScene3D.heightProperty()).divide(1.7));
    }


    // Set 2D transformations: translate, scale and rotate
    private void set2DTransformations()
    {
        // Apply transformations to Group containing all 2nd structure shapes
        Group secStructure = PDB123View.getSecStructureElements();
        // Translate in XY direction
        translateSecStructure = new Translate();
        // Scale Group. Scaling needs a pivot point. Pivot point is bound to the center of the
        // 2D subscene.
        scaleSecStructure = new Scale();
        scaleSecStructure.pivotXProperty().bind(PDB123View.getSubScene2D().widthProperty().divide(2.));
        scaleSecStructure.pivotYProperty().bind(PDB123View.getSubScene2D().heightProperty().divide(2.));
        // Rotate Group. Rotating needs a pivot point. Pivot point is bound to the center of the
        // 2D subscene.
        rotateSecStructure = new Rotate();
        rotateSecStructure.pivotXProperty().bind(PDB123View.getSubScene2D().widthProperty().divide(2.));
        rotateSecStructure.pivotYProperty().bind(PDB123View.getSubScene2D().heightProperty().divide(2.));
        // Add all transformations to 2nd structure Group
        secStructure.getTransforms().addAll(translateSecStructure, scaleSecStructure, rotateSecStructure);

    }
    // Set 3D transformations: translate and rotate.
    // Unlike for 2D transformations, a scaling transformation is not needed.
    // Zooming is instead facilitated by translating on the Z-Axis.
    // Rotation is applied to the 3D shapes.
    // Translation is applied to the camera.
    private void set3DTransformations()
    {
        Group structure = PDB123View.getTertStructureElements();
        // Rotation of Group structure
        rotateTertStructureY = new Rotate(0, Rotate.Y_AXIS);
        rotateTertStructureX = new Rotate(0, Rotate.X_AXIS);
        structure.getTransforms().addAll(rotateTertStructureX, rotateTertStructureY);
        // Translation of camera
        PerspectiveCamera cam = PDB123View.get3DCamera();
        translateTertCam = new Translate();
        cam.getTransforms().add(translateTertCam);
    }


    // Mouse actions applied to 2D subscene
    // Detect mouse clicks on the 2D subscene
    // Action performed depends on the state of the
    // shift and ctrl key
    private void setMouseActions2D() {
        // Save initial mouseX/Y
        PDB123View.getSubScene2D().setOnMousePressed(event -> {
            mouseXold = mouseXnew = event.getX();
            mouseYold = mouseYNew = event.getY();
            mouseXDelta = 0;
            mouseYDelta = 0;
        });
        // Dimension of the transformation depends on the
        // difference between the initial and new mouse position
        // ->   mouseXDelta
        // ->   mouseYDelta
        // Empiric correction factors are applied to mouseXDelta
        // and mouseYDelta to reduce mouse movement sensitivity
        PDB123View.getSubScene2D().setOnMouseDragged(event -> {
            mouseXold = mouseXnew;
            mouseYold = mouseYNew;
            mouseXnew = event.getX();
            mouseYNew = event.getY();
            mouseXDelta = mouseXnew - mouseXold;
            mouseYDelta = mouseYNew - mouseYold;
            // If shift key pressed: Apply scale transformation
            if (event.isShiftDown()) {
                scaleSecStructure.setX(scaleSecStructure.getX()+mouseYDelta*0.4);
                scaleSecStructure.setY(scaleSecStructure.getY()+mouseYDelta*0.4);
            }
            // If ctrl key pressed: Apply translate transformation
            if (event.isControlDown()) {
                translateSecStructure.setX(translateSecStructure.getX()+mouseXDelta*0.6);
                translateSecStructure.setY(translateSecStructure.getY()+mouseYDelta*0.6);

            }
            // If no key pressed: Rotate structure
            if (!event.isShiftDown() && ! event.isControlDown()){
                rotateSecStructure.setAngle(rotateSecStructure.getAngle()+mouseXDelta*-0.2);
            }
        });
    }


    // Mouse actions applied to 3D subscene
    // Detect mouse clicks on the 3D subscene
    // Action performed depends on the state of the
    // shift and ctrl key
    private void setMouseActions3D() {
        PerspectiveCamera cam = PDB123View.get3DCamera();
        // Save initial mouseX/Y
        PDB123View.get3DSubScene().setOnMousePressed(event -> {
            mouseXold = mouseXnew = event.getX();
            mouseYold = mouseYNew = event.getY();
            mouseXDelta = 0;
            mouseYDelta = 0;
        });
        // Dimension of the transformation depends on the
        // difference between the initial and new mouse position
        // ->   mouseXDelta
        // ->   mouseYDelta
        // Empiric correction factors are applied to mouseXDelta
        // and mouseYDelta to reduce mouse movement sensitivity
        PDB123View.get3DSubScene().setOnMouseDragged(event -> {
            mouseXold = mouseXnew;
            mouseYold = mouseYNew;
            mouseXnew = event.getX();
            mouseYNew = event.getY();
            mouseXDelta = mouseXnew - mouseXold;
            mouseYDelta = mouseYNew - mouseYold;
            // If shift key pressed: Apply Z-translation to camera
            if (event.isShiftDown()) {
                translateTertCam.setZ(translateTertCam.getZ()+mouseYDelta);
            }
            // If ctrl key pressed: Apply X,Y-translate to camera
            if (event.isControlDown()) {

                translateTertCam.setX(translateTertCam.getX()-mouseXDelta*0.2);
                translateTertCam.setY(translateTertCam.getY()-mouseYDelta*0.2);
            }
            // If no key pressed: Rotate 3D structure group
            if (!event.isShiftDown() && !event.isControlDown()) {
                rotateTertStructureY.setAngle(rotateTertStructureY.getAngle() + mouseXDelta * 0.2);
                rotateTertStructureX.setAngle(rotateTertStructureX.getAngle() - mouseYDelta * 0.2);
            }

        });

    }

    // Buttons stacked on top of the 2D and 3D subscene
    // Click resets all applied 2D or 3D transformations
    private void setCenterButtons()
    {
        PDB123View.getCenter3D().setOnAction(event -> {
            reset3DTransformations();
    });
        PDB123View.getCenter2D().setOnAction(event -> {
            reset2DTransformations();
        });
    }

    // Set default values to checkboxes stacked on top of
    // 3D subscene. Value of checkbox decides if 3D structure element
    // (nucleobase, sugar, h-bonds, backbone) are shown
    private void setCheckboxes()
    {
        PDB123View.getcBnucleoBase3D().setSelected(true);
        showNucleoBase.bind(PDB123View.getcBnucleoBase3D().selectedProperty());
        PDB123View.getcBsugar3D().setSelected(true);
        showSugar.bind(PDB123View.getcBsugar3D().selectedProperty());
        PDB123View.getcBpBB3D().setSelected(false);
        showBackbone.bind(PDB123View.getcBpBB3D().selectedProperty());
        PDB123View.getcBhDB().setSelected(true);
        showHBonds.bind(PDB123View.getcBhDB().selectedProperty());
    }

    // Some changed values in settings window require a redrawing of the 2D and 3D structure.
    // Add a listener to the settings Boolean property redrawProperty. If value is changed to true
    // and a valid file is currently loaded, reload that file to apply the new settings.
    private void redrawStructureBindings()
    {
        settingsWindow.redrawProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && path != null) loadStructureTask();
        });
    }


    // Reset user-defined 2D translations,
    //  scaling and rotations back to zero.
    private void reset2DTransformations()
    {
        translateSecStructure.setX(0);
        translateSecStructure.setY(0);
        rotateSecStructure.setAngle(0);
        scaleSecStructure.setX(1);
        scaleSecStructure.setY(1);
    }

    // Reset user-defined 3D translations
    //  and rotations back to zero.
    private void reset3DTransformations()
    {
        translateTertCam.setX(0);
        translateTertCam.setY(0);
        translateTertCam.setZ(0);
        rotateTertStructureX.setAngle(0);
        rotateTertStructureY.setAngle(0);
    }



    // Load a PDB file and derive a 1D, 2D and 3D RNA structure representation from it
    // This task is done in a background thread to prevent the freezing of the UI
    // while this complex task is performed.
    // To show the progress of the background task, a progress window is shown.
    private void loadStructureTask()
    {

        // Define the background task
        // Parameters are:
        // 1. log-window to output messages
        // 2. Path to PDB file
        // 3. 2D subscene (required to set up bindings for proper resize of that subscene)
        PDB123PresenterStructureTask task1 = new PDB123PresenterStructureTask(printLog, path, PDB123View.getSubScene2D());
        // Set references to boolean properties
        task1.setBooleanProperties(showBackbone, showSugar, showNucleoBase, showHBonds);
        // Set reference to settings window to derive current settings
        task1.setSettings(settingsWindow);
        // Set actions to perform once task returns values
        // Task returns an ArrayList of objects of length 4
        // 1. Text nodes for primary structure as ArrayList<Text>
        // 2. Group of 2D shapes for secondary structure representation
        // 3. Group of 3D shapes for tertiary structure representation
        // 4. Statistics as int[][]: Nr. of nucleotides ACGU and Nr. of base-paired nucleotides
        task1.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Add 1D representation to UI
            // Textflow requires that all Text nodes are added sequentielly to the parent node
            // Adding Text nodes as group causes them to stack upon each other
            ArrayList<Text> primStructure = (ArrayList<Text>) newValue.get(0);
            TextFlow primStructureNode = PDB123View.getPrimStructure();
            primStructureNode.getChildren().clear();
            for (Text t: primStructure
                    ) {
                primStructureNode.getChildren().add(t);

            }
            // Add 2D representation to UI
            PDB123View.getSecStructureElements().getChildren().clear();
            PDB123View.getSecStructureElements().getChildren().add((Group)newValue.get(1));
            // Add 3D representation to UI
            PDB123View.getTertStructureElements().getChildren().clear();
            PDB123View.getTertStructureElements().getChildren().add((Group)newValue.get(2));
            // Show nucleotide counts on bar chart plot
            int[][] ntCounts = (int[][]) newValue.get(3);
            barChart.updateData(ntCounts[0], ntCounts[1]);
        });

        // Setup stage to show progress window
        // Stage does not need any decorations
        Stage progressStage = new Stage(StageStyle.UNDECORATED);
        // Block access to any other window while progress stage is shown
        progressStage.initModality(Modality.APPLICATION_MODAL);
        // Keep progress stage on top
        progressStage.setAlwaysOnTop(true);
        // Setup scene for progress bar stage
        PDB123ProgressPresenter progressPresenter = new PDB123ProgressPresenter(task1, progressStage);
        Scene progressScene = new Scene(progressPresenter.getProgressView(), 200,100);
        progressScene.getStylesheets().add("GUI/progressBarStyle.css");
        progressStage.setScene(progressScene);
        progressStage.show();
        // Create a new Thread from Task and start thread
        Thread structureThread = new Thread(task1);
        structureThread.start();
    }


    // FileChooser dialog
    private String showFileChooser() throws Exception {
        String prevPath = this.path;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDB Files", "*.pdb"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        return selectedFile.getAbsolutePath();
    }

    // Getter for view
    public GUI.Main.PDB123View getPDB123View() {
        return PDB123View;
    }
}
