package GUI;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by oliver on 19.01.16.
 * View class of the MVP pattern
 * Defines Layout, Nodes and Controls
 */
public class PDB123View extends VBox {
    // Two textareas: Prim. Structure and log/msg output
    private TextArea primStructure;
    private TextArea log;
    // Menubar
    private MenuBar menuBar;
    private Menu menuFile;
    private Menu menuView;
    private Menu menuAbout;
    private MenuItem menuItemOpen;
    private MenuItem menuItemExit;
    private MenuItem menuItemClear;
    // Subscenes for 1.-3. structure + log/msg window
    private SubScene subScene1D, subScene2D, subScene3D, subSceneLog;
    // Camera
    private PerspectiveCamera camera;
    // Container for 3D and 2D elements
    private Group secDrawings;
    private Group threeDrawings;
    // HBoxes for horizontal placement of subscenes
    private HBox subScene1D2D;
    private HBox subScenelog3D;


    public PDB123View(Stage primaryStage) {
        // Initialize class variables
        initClassVariables();
        // Set Menubar
        setMenuBar();
        // Set Layout elements
        setLayout(primaryStage);

    }

    // Initialize class variables
    private void initClassVariables() {
        primStructure = new TextArea("Primary Structure");
        log = new TextArea("Messages\n");
        subScene1D2D = new HBox();
        subScenelog3D = new HBox();
        secDrawings = new Group();
        threeDrawings = new Group();
        subScene1D = new SubScene(primStructure, 0, 0);
        subScene2D = new SubScene(secDrawings, 0, 0);
        subScene3D = new SubScene(threeDrawings, 0, 0, true, SceneAntialiasing.BALANCED);
        camera = new PerspectiveCamera(false);
        subScene3D.setCamera(camera);
        subSceneLog = new SubScene(log, 0, 0);
    }

    // Set layout elements and positioning
    private void setLayout(Stage primaryStage) {
        // Add HBoxes to View and add subscenes to HBoxes
        this.getChildren().addAll(subScene1D2D, subScenelog3D);
        subScene1D2D.getChildren().addAll(subScene1D, subScene2D);
        subScenelog3D.getChildren().addAll(subSceneLog, subScene3D);
        // Resize subscenes upon stage size change
        DoubleBinding primaryStageHeight = this.heightProperty().multiply(0.85);
        DoubleBinding primaryStageWidth = this.widthProperty().multiply(0.95);
        subScene1D.heightProperty().bind(primaryStageHeight.multiply(1. / 3));
        subScene1D.widthProperty().bind(primaryStageWidth.multiply(1. / 3));
        subScene2D.heightProperty().bind(primaryStageHeight.multiply(1. / 3));
        subScene2D.widthProperty().bind(primaryStageWidth.multiply(2. / 3));
        subSceneLog.heightProperty().bind(primaryStageHeight.multiply(2. / 3));
        subSceneLog.widthProperty().bind(primaryStageWidth.multiply(1. / 3));
        subScene3D.heightProperty().bind(primaryStageHeight.multiply(2. / 3));
        subScene3D.widthProperty().bind(primaryStageWidth.multiply(2. / 3));
        // Set subscene background colors
        subScene1D.setFill(Color.BLACK);
        subScene2D.setFill(Color.LIGHTGRAY);
        subScene3D.setFill(Color.LIGHTGREY);
        subSceneLog.setFill(Color.CHOCOLATE);
        // Spacing between subscenes
        this.setSpacing(5);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_CENTER);
        subScene1D2D.setSpacing(5);
        subScene1D2D.setPadding(new Insets(5));
        subScenelog3D.setSpacing(5);
        subScenelog3D.setPadding(new Insets(5));
    }

    // Initialize Menubar
    private void setMenuBar() {
        menuBar = new MenuBar();
        menuFile = new Menu("File");
        menuView = new Menu("View");
        menuAbout = new Menu("About");
        menuItemOpen = new MenuItem("Open");
        menuItemExit = new MenuItem("Exit");
        menuItemClear = new MenuItem("Clear");
        menuBar.getMenus().addAll(menuFile, menuView, menuAbout);
        menuFile.getItems().addAll(menuItemOpen, menuItemExit);
        menuView.getItems().addAll(menuItemClear);
        this.getChildren().add(menuBar);
    }

    // Getters
    MenuItem getMenuItemOpen() {
        return menuItemOpen;
    }

    MenuItem getMenuItemExit() {
        return menuItemExit;
    }

    MenuItem getMenuItemClear() {
        return menuItemClear;
    }

    TextArea getLog() {
        return log;
    }

    Group getThreeDrawings() {
        return threeDrawings;
    }

    PerspectiveCamera get3DCamera() {
        return camera;
    }

    SubScene get3DSubScene() {
        return subScene3D;
    }
}
