package GUI;

import PDBParser.ReadPDB;
import PrimStructure.ParseSequence;
import SecStructure.DotBracketNotation.DotBracket;
import SecStructure.RNA2D.Rna2DEdge;
import SecStructure.RNA2D.Rna2DGraph;
import SecStructure.RNA2D.Rna2DNode;
import TertStructure.Basepairing.HydrogenBondDetector;
import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
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
    private ReadPDB pdbReader;
    private Rna2DGraph Graph2D;
    private PDB123PresenterStructure Rna3D;
    private Map<Integer, PDBNucleotide> ntMap;
    private int firstNtIndex, lastNtIndex;
    private Rotate rotateStructureX, rotateStructureY;
    private double mouseXold, mouseXnew, mouseYold, mouseYNew, mouseXDelta, mouseYDelta;
    private HydrogenBondDetector hbDetector;
    private PDB123PrintLog printLog;
    private DotBracket dotBracket;
    private String dotBracketSeq, rnaSeq;
    private ParseSequence parseSeq;
    private BooleanProperty showBackbone, showSugar, showNucleoBase;
    private ArrayList<Rna2DNode> Node2DList;
    private ArrayList<Rna2DEdge> Edge2DList;

    public PDB123Presenter(Stage primaryStage) {
        // Initialize class variables
        initClassVariables(primaryStage);
        // Add functionality to menubar elements
        exitFunction();
        loadFileFunction();
        aboutFunction();
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
       //test();

    }

    private void aboutFunction()
    {
        PDB123View.getMenuItemClear().setOnAction(event -> {
            test();
        });

    }

    private void test()
    {
    Text t1 = new Text("A");
        Text t2 = new Text("N");
        t1.setOnMouseClicked(event -> {
            printLog.printLogMessage("Klappt :-)");
            t1.setFill(Color.RED);

        });
        Rectangle r = new Rectangle(t1.getX(), t1.getY(), t1.getStrokeWidth(), t1.getLineSpacing());
        PDB123View.getPrimStructure().getChildren().addAll(t1,t2, r);

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
        root2D.setScaleX(0.);
        root2D.setScaleY(0.);
        root2D.setTranslateX(0.);
        root2D.setTranslateY(0.);
        root2D.setRotate(0.);
    }

    // Initialize class variables
    private void initClassVariables(Stage primaryStage) {
        this.stage = primaryStage;
        PDB123View = new PDB123View(primaryStage);
        this.printLog = new PDB123PrintLog(PDB123View.getLog());
        this.pdbReader = new ReadPDB(this.printLog);
        this.hbDetector = new HydrogenBondDetector(printLog);
        this.dotBracket = new DotBracket(printLog);
        this.parseSeq = new ParseSequence();
        this.Rna3D = new PDB123PresenterStructure(PDB123View.getThreeDrawings());
        this.showNucleoBase = new SimpleBooleanProperty(true);
        this.showSugar = new SimpleBooleanProperty(true);
        this.showBackbone = new SimpleBooleanProperty(true);
        this.Node2DList = new ArrayList<>();
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
            String path = null;
            try {
                path = showFileChooser();
            } catch (Exception e) {
                printLog.printLogMessage("No file loaded");
            }
            if (path == null) return;
            String fileName = path.substring(Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\')) + 1);
            printLog.printLogMessage("File loaded " + fileName);
            // Try to parse PDB file
            try {
                pdbReader.setFilePath(path);
                ntMap = pdbReader.getNtMap();
                firstNtIndex = pdbReader.getFirstNtIndex();
                lastNtIndex = pdbReader.getLastNtIndex();
                Node2DList = new ArrayList<>();
                Edge2DList = new ArrayList<>();
                Rna3D.generate3DStructure(firstNtIndex, lastNtIndex, "resType", ntMap, showBackbone, showSugar, showNucleoBase, Node2DList, Edge2DList);
                // (re)center camera
                centerCamera3D();
                // Identify and visualize hydrogen bonds
                detectHydrogenBonds();
                // Calculate dot-bracket notation for current PDB file
                calculateDotBracketNotation();
                // Once 3D structure preparation is finished,
                // Produce 1D structure presentation
                rnaSeq = parseSeq.parseRnaSeq(ntMap, firstNtIndex, lastNtIndex, PDB123View.getPrimStructure());


                // produce 2D structure presentation
                Graph2D = new Rna2DGraph(this.getPDB123View().getSecDrawings(), this.getPDB123View().getSubScene2D(), dotBracketSeq, rnaSeq, printLog, Node2DList, Edge2DList);
                Graph2D.getRna2D();



            } catch (IOException e) {
                printLog.printLogMessage(e.getMessage());
            }
        });
    }



    private void calculateDotBracketNotation()
    {
        // Provide reference to ntMap
        dotBracket.setNtMap(this.ntMap);
        // Provide first + last nt indices
        dotBracket.setFirstNtIndex(this.firstNtIndex);
        dotBracket.setLastNtIndex(this.lastNtIndex);
        // Calculate dotBracket notation
        this.dotBracketSeq = dotBracket.getDotBracket();
    }

    private void detectHydrogenBonds()
    {
        hbDetector.setFirstNtIndex(this.firstNtIndex);
        hbDetector.setLastNtIndex(this.lastNtIndex);
        hbDetector.setNtMap(this.ntMap);
        hbDetector.setRna2DEdge(this.Edge2DList);
        Group hydrogenBonds = hbDetector.getHDB();
        PDB123View.getThreeDrawings().getChildren().add(hydrogenBonds);

    }


    private void setCameraClip3D()
    {
        PerspectiveCamera cam = PDB123View.get3DCamera();
        cam.setNearClip(0.0000001);
        cam.setFarClip(-100000.0);
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
