package GUI.Main;

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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Created by oliver on 19.01.16.
 * View class of the MVP pattern
 * Defines Layout, Nodes and Controls
 */
public class PDB123View extends VBox {
    // Two text representations: Primary structure (Textflow)
    // and log-messages (TextArea)
    private TextFlow primStructure;
    private TextArea log;
    // Menubar and menu items
    private MenuBar menuBar;
    private Menu menuFile, menuView, menuEdit;
    private MenuItem menuItemOpen, menuItemExit, menuItemClearSelection, menuItemSettings, menuItemStats;
    // Subscenes for 1D, 2D and 3D structure + log/msg window
    private SubScene subScene1D, subScene2D, subScene3D, subSceneLog;
    // 2D and 3D representation will have another BorderPane "on top"
    // Panes are stacked in a StackPane
    private StackPane stack3D, stack2D;
    private BorderPane topPane2D, topPane3D;
    // Camera for 3D representation
    private PerspectiveCamera camera;
    // Container for 3D and 2D elements
    private Group secStructureElements;
    private Group tertStructureElements;
    // HBoxes for horizontal placement of subscenes
    private HBox subScene1D2D;
    private HBox subScenelog3D;
    // Buttons for re-centering of 2D and 3D structure
    private Button center3D, center2D;
    // Checkboxes to add/remove drawings of 3D or 2D components
    private CheckBox cBsugar3D, cBnucleoBase3D, cBpBB3D, cBhDB;
    // Controls for 2D and 3D strucure are collected in HBoxes
    private HBox controls3D, controls2D;

    // Constructor
    public PDB123View() {
        // Initialize class variables
        initClassVariables();
        // Set Menubar
        setMenuBar();
        // Set Layout elements
        setLayout();

    }

    // Initialize class variables
    private void initClassVariables() {
        primStructure = new TextFlow(new Text("Primary Structure"));
        log = new TextArea();
        subScene1D2D = new HBox();
        subScenelog3D = new HBox();
        secStructureElements = new Group();
        tertStructureElements = new Group();
        subScene1D = new SubScene(primStructure, 0, 0);
        subScene2D = new SubScene(secStructureElements, 0, 0);
        subScene3D = new SubScene(tertStructureElements, 0, 0, true, SceneAntialiasing.DISABLED);
        stack3D = new StackPane();
        stack2D = new StackPane();
        topPane3D = new BorderPane();
        topPane2D = new BorderPane();
        camera = new PerspectiveCamera(false);
        subScene3D.setCamera(camera);
        subSceneLog = new SubScene(log, 0, 0);
        // Buttons for 2D/3D re-centering
        center3D = new Button("Center");
        center2D = new Button("Center");
        // Checkboxes to select 3D components to display
        cBsugar3D = new CheckBox("Ribose");
        cBnucleoBase3D = new CheckBox("Nucleobase");
        cBpBB3D = new CheckBox("Backbone");
        cBhDB = new CheckBox("Hydrogen bonds");
        // Hboxes for 3D and 2D controls
        controls2D = new HBox();
        controls3D = new HBox();


    }

    // Set layout elements and positioning
    private void setLayout() {
        // Stack subScene3D and topPane3D
        stack3D.setAlignment(Pos.CENTER);
        topPane3D.setPickOnBounds(false);
        subScene3D.setFill(Color.TRANSPARENT);
        controls3D.getChildren().addAll(center3D, cBnucleoBase3D, cBsugar3D, cBpBB3D, cBhDB);
        controls3D.setSpacing(5);
        controls3D.setPadding(new Insets(5));
        controls3D.setAlignment(Pos.CENTER_RIGHT);
        topPane3D.setBottom(controls3D);
        center3D.getStyleClass().add("buttonCenter");
        topPane3D.setPadding(new Insets(10));
        stack3D.getChildren().addAll(subScene3D, topPane3D);
        // Stack subScene2D and topPane2D
        stack2D.setAlignment(Pos.CENTER);
        topPane2D.setPickOnBounds(false);
        stack2D.setPickOnBounds(false);
        subScene2D.setPickOnBounds(true);
        controls2D.getChildren().addAll(center2D);
        controls2D.setSpacing(5);
        controls2D.setPadding(new Insets(5));
        controls2D.setAlignment(Pos.CENTER_RIGHT);
        topPane2D.setBottom(controls2D);
        center2D.getStyleClass().add("buttonCenter");
        topPane2D.setPadding(new Insets(10));
        stack2D.getChildren().addAll(subScene2D, topPane2D);

        // Add HBoxes to View and add subscenes to HBoxes
        this.getChildren().addAll(subScene1D2D, subScenelog3D);
        subScene1D2D.getChildren().addAll(subScene1D, stack2D);
        subScenelog3D.getChildren().addAll(subSceneLog, stack3D);

        // Resize subscenes when size of this view changes
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

        // Spacing between subscenes
        this.setSpacing(5);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_CENTER);
        subScene1D2D.setSpacing(5);
        subScene1D2D.setPadding(new Insets(5));
        subScenelog3D.setSpacing(5);
        subScenelog3D.setPadding(new Insets(5));

        // Set border around 2D and 3D representation
        topPane2D.getStyleClass().add("subsceneBorder");
        topPane3D.getStyleClass().add("subsceneBorder");
        primStructure.getStyleClass().add("textFlowBorder");
    }

    // Initialize Menubar
    private void setMenuBar() {
        menuBar = new MenuBar();
        menuFile = new Menu("File");
        menuEdit = new Menu("Edit");
        menuView = new Menu("View");
        menuItemOpen = new MenuItem("Open");
        menuItemExit = new MenuItem("Exit");
        menuItemStats = new MenuItem("Statistics");
        menuItemClearSelection = new MenuItem("Clear selection");
        menuItemSettings = new MenuItem("Settings");
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        menuFile.getItems().addAll(menuItemOpen, menuItemExit);
        menuEdit.getItems().addAll(menuItemClearSelection, menuItemSettings);
        menuView.getItems().addAll(menuItemStats);
        this.getChildren().add(menuBar);
    }

    // Getters
    MenuItem getMenuItemOpen() {
        return menuItemOpen;
    }

    MenuItem getMenuItemExit() {
        return menuItemExit;
    }


    public MenuItem getMenuItemClearSelection() {
        return menuItemClearSelection;
    }

    TextArea getLog() {
        return log;
    }

    public Group getSecStructureElements() {
        return secStructureElements;
    }

    public Group getTertStructureElements() {
        return tertStructureElements;
    }

    PerspectiveCamera get3DCamera() {
        return camera;
    }

    SubScene get3DSubScene() {
        return subScene3D;
    }

    SubScene getSubScene2D() {
        return subScene2D;
    }


    TextFlow getPrimStructure() {
        return primStructure;
    }

    CheckBox getcBsugar3D() {
        return cBsugar3D;
    }

    CheckBox getcBnucleoBase3D() {
        return cBnucleoBase3D;
    }

    CheckBox getcBpBB3D() {
        return cBpBB3D;
    }

    public CheckBox getcBhDB() {
        return cBhDB;
    }


    Button getCenter2D() {
        return center2D;
    }

    Button getCenter3D() {
        return center3D;
    }


    public MenuItem getMenuItemSettings() {
        return menuItemSettings;
    }

    public MenuItem getMenuItemStats() {
        return menuItemStats;
    }
}
