package GUI;

import Charts.PDBBarChart;
import PDBParser.ReadPDB;
import SecStructure.RNA2D.Rna2DEdge;
import SecStructure.RNA2D.Rna2DNode;
import SelectionModel.PDB123SelectionModel;
import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Rotate;
import javafx.stage.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oliver on 19.01.16.
 * Presenter class for PDB123 MVP design pattern
 * Adds functionality to view elements
 */
public class PDB123Presenter {
    private GUI.PDB123View PDB123View;
    private Stage stage;
    private Rotate rotateStructureX, rotateStructureY;
    private double mouseXold, mouseXnew, mouseYold, mouseYNew, mouseXDelta, mouseYDelta;
    private PDB123PrintLog printLog;
    private BooleanProperty showBackbone, showSugar, showNucleoBase, showHBonds;
    private PDB123SettingsPresenter settingsWindow;
    private String path;
    private PDBBarChart barChart;

    public PDB123Presenter(Stage primaryStage) {
        // Initialize class variables
        initClassVariables(primaryStage);
        // Add functionality to menubar elements
        exitFunction();
        loadFileFunction();
        aboutFunction();
        // Set functions for menu items of edit menu
        editFunctions();
        // Set functions for menu items of view menu
        viewFunctions();
        // Add window size listener
        PDB123View.get3DSubScene().widthProperty().addListener(observable -> centerCamera3D());
        PDB123View.get3DSubScene().heightProperty().addListener(observable -> centerCamera3D());
        // Set 3D perspective
        setCameraClip3D();
        setRotation3D();
        centerCamera3D();
        // Set 3D subscene Mouse actions
        setMouseActions3D();
        // Set 2D subscene Mouse actions
        setMouseActions2D();
        // Set checkbox actions
        setCheckboxes();
        // Set center-button actions
        setCenterButtons();
        // Test function for innovative new moduls
        // Set redraw bindings
        redrawBindings();


    }

    private void editFunctions()
    {

        PDB123View.getMenuItemClearSelection().setOnAction(event -> PDB123SelectionModel.unselectAll());
        PDB123View.getMenuItemSettings().setOnAction(event -> {
            settingsWindow.getSettingsStage().show();
        });
    }
    private void viewFunctions()
    {
        PDB123View.getMenuItemStats().setOnAction(event -> {

          //  stackedBarChart.updateData(ntMap, firstNtIndex, lastNtIndex);
            barChart.getBarChartStage().show();
        });
    }

    private void aboutFunction()
    {
        PDB123View.getMenuItemClear().setOnAction(event -> {

        });

    }

    private void redrawBindings()
    {
    settingsWindow.redrawProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue && path != null) loadStructureTask();
    });




    }

    private void setCenterButtons()
    {
        PDB123View.getCenter3D().setOnAction(event -> {
      centerCamera3D();
        setRotation3D();
    });
        PDB123View.getCenter2D().setOnAction(event -> {
            center2D();
        });
    }

    private void setCheckboxes()
    {
        PDB123View.getcBnucleoBase3D().setSelected(true);
        showNucleoBase.bind(PDB123View.getcBnucleoBase3D().selectedProperty());
        PDB123View.getcBsugar3D().setSelected(true);
        showSugar.bind(PDB123View.getcBsugar3D().selectedProperty());
        PDB123View.getcBpBB3D().setSelected(false);
        showBackbone.bind(PDB123View.getcBpBB3D().selectedProperty());
        showHBonds.bind(PDB123View.getcBhDB().selectedProperty());
    }

    private void centerCamera3D()
    {
        PerspectiveCamera cam = PDB123View.get3DCamera();
        cam.setTranslateX(PDB123View.get3DSubScene().getWidth()/(-2));
        cam.setTranslateY(PDB123View.get3DSubScene().getHeight()/(-2));
        cam.setTranslateZ((PDB123View.get3DSubScene().getWidth()+PDB123View.get3DSubScene().getHeight())/1.7);
    }

    private void center2D()
    {

        Group root2D = PDB123View.getSecDrawings();
        root2D.setScaleX(1.);
        root2D.setScaleY(1.);
        root2D.setTranslateX(0);
        root2D.setTranslateY(0);
        root2D.setRotate(0.);
    }

    // Initialize class variables
    private void initClassVariables(Stage primaryStage) {
        this.stage = primaryStage;
        PDB123View = new PDB123View(primaryStage);
        this.printLog = new PDB123PrintLog(PDB123View.getLog());

        this.showNucleoBase = new SimpleBooleanProperty(true);
        this.showSugar = new SimpleBooleanProperty(true);
        this.showBackbone = new SimpleBooleanProperty(true);
        this.showHBonds = new SimpleBooleanProperty(true);
        this.barChart = new PDBBarChart();
        PDB123SelectionModel.initSelectionModel();
        this.settingsWindow = new PDB123SettingsPresenter();
    }

    // Menu -> exit
    private void exitFunction() {
        PDB123View.getMenuItemExit().setOnAction(event -> System.exit(0));
    }


    // Menu -> Load file
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
            printLog.printLogMessage("File loaded " + fileName);
            // (re)center camera
            centerCamera3D();
            center2D();
            // Employ a background task to load 1,2 and 3D structure from PDB file
            loadStructureTask();



        });
    }

    private void loadStructureTask()
    {


        // Derive 1D, 2D and 3D RNA representation from ntMap
        // ntMap contains PDBNucleotides (Java representation of PDB 3D coordinates)
        // and residue index positions



        PDB123PresenterStructureTask task1 = new PDB123PresenterStructureTask(printLog, path, "resType", PDB123View.getSubScene2D(), barChart);
        Stage progressStage = new Stage(StageStyle.UNDECORATED);
        progressStage.initModality(Modality.WINDOW_MODAL);
        progressStage.setAlwaysOnTop(true);

        PDB123ProgressPresenter progressPresenter = new PDB123ProgressPresenter(task1, progressStage);
        Scene progressScene = new Scene(progressPresenter.getProgressView(), 200,150);
        progressScene.getStylesheets().add("GUI/myStyle.css");
        progressScene.setFill(Color.BLUE);

        progressStage.setScene(progressScene);
        progressStage.show();
        task1.setBooleanProperties(showBackbone, showSugar, showNucleoBase, showHBonds);
        task1.setSettings(settingsWindow);
        task1.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            if(task1.isCancelled()) System.out.println("Loading canceled.");
            else System.out.println("Sadly, an error occured.");
        });
        Thread structureThread = new Thread(task1);
        structureThread.setDaemon(true);
        structureThread.start();
        task1.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Add 1D representation
            // Textflow requires that all Text nodes are added sequentielly to the parent node
            // Adding Text nodes as group causes them to stack upon each other
            ArrayList<Text> primStructure = (ArrayList<Text>) newValue.get(0);
            TextFlow primStructureNode = PDB123View.getPrimStructure();
            primStructureNode.getChildren().clear();
            for (Text t: primStructure
                    ) {
                primStructureNode.getChildren().add(t);

            }
            // Add 2D representation
            PDB123View.getSecDrawings().getChildren().clear();
            PDB123View.getSecDrawings().getChildren().add((Group)newValue.get(1));
            // Add 3D representation
            PDB123View.getThreeDrawings().getChildren().clear();
            PDB123View.getThreeDrawings().getChildren().add((Group)newValue.get(2));
            // Setup bar chart
            int[][] ntCounts = (int[][]) newValue.get(3);
            barChart.updateData(ntCounts[0], ntCounts[1]);
        });
    }








    private void setCameraClip3D()
    {
        PerspectiveCamera cam = PDB123View.get3DCamera();
        cam.setNearClip(0.0000001);
        cam.setFarClip(1000.0);
    }

    private void setRotation3D()
    {
        Group structure = PDB123View.getThreeDrawings();
        // Rotation of Group structure
        rotateStructureY = new Rotate(0, Rotate.Y_AXIS);
        //rotateStructureY.setAxis(Rotate.Y_AXIS);
        rotateStructureX = new Rotate(0, Rotate.X_AXIS);
        //rotateStructureX.setAxis(Rotate.X_AXIS);
        structure.getTransforms().clear();
        structure.getTransforms().addAll(rotateStructureX, rotateStructureY);
    }



    private void setMouseActions3D() {
        PerspectiveCamera cam = PDB123View.get3DCamera();
        // Save initial mouseX/Y
        PDB123View.get3DSubScene().setOnMousePressed(event -> {
            mouseXold = mouseXnew = event.getX();
            mouseYold = mouseYNew = event.getY();
            mouseXDelta = 0;
            mouseYDelta = 0;
        });
        // Transformations depend directly on mouseXYZ delta
        PDB123View.get3DSubScene().setOnMouseDragged(event -> {
            mouseXold = mouseXnew;
            mouseYold = mouseYNew;
            mouseXnew = event.getX();
            mouseYNew = event.getY();
            mouseXDelta = mouseXnew - mouseXold;
            mouseYDelta = mouseYNew - mouseYold;
            if (event.isShiftDown()) {
                cam.setTranslateZ(cam.getTranslateZ() + mouseYDelta);
            }
            if (event.isControlDown()) {
                double newX = cam.getTranslateX() - mouseXDelta * 0.2;
                double newY = cam.getTranslateY() - mouseYDelta * 0.2;
                cam.setTranslateX(newX);
                cam.setTranslateY(newY);
            }
            if (!event.isShiftDown() && !event.isControlDown()) {
                rotateStructureY.setAngle(rotateStructureY.getAngle() + mouseXDelta * 0.2);
                rotateStructureX.setAngle(rotateStructureX.getAngle() - mouseYDelta * 0.2);
            }

        });

    }

    private void setMouseActions2D() {

        Group root2D = PDB123View.getSecDrawings();
        // Save initial mouseX/Y
        PDB123View.getSubScene2D().setOnMousePressed(event -> {
            mouseXold = mouseXnew = event.getX();
            mouseYold = mouseYNew = event.getY();
            mouseXDelta = 0;
            mouseYDelta = 0;

        });
        // Transformations depend directly on mouseXYZ delta
        PDB123View.getSubScene2D().setOnMouseDragged(event -> {
            mouseXold = mouseXnew;
            mouseYold = mouseYNew;
            mouseXnew = event.getX();
            mouseYNew = event.getY();
            mouseXDelta = mouseXnew - mouseXold;
            mouseYDelta = mouseYNew - mouseYold;
            if (event.isShiftDown()) {
                root2D.setScaleX(root2D.getScaleX() + mouseYDelta*0.4);
                root2D.setScaleY(root2D.getScaleY()+mouseYDelta*0.4);

            }
            if (event.isControlDown()) {
                root2D.setTranslateX(root2D.getTranslateX()+mouseXDelta*0.6);
                root2D.setTranslateY(root2D.getTranslateY()+mouseYDelta*0.6);

            }
            if (!event.isShiftDown() && ! event.isControlDown()){
                root2D.setRotate(root2D.getRotate()+mouseXDelta*-0.2);
            }

        });

    }


    // FileChooser dialog
    private String showFileChooser() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDB Files", "*.pdb"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile.getAbsolutePath();
    }

    // Getter for view
    public GUI.PDB123View getPDB123View() {
        return PDB123View;
    }
}
