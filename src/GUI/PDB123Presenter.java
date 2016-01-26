package GUI;

import PDBParser.ReadPDB;
import SecStructure.DotBracketNotation.DotBracket;
import TertStructure.Basepairing.HydrogenBondDetector;
import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;;
import TertStructure.RNAMesh3D.DrawLine;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
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
    private Map<Integer, PDBNucleotide> ntMap;
    private int firstNtIndex, lastNtIndex;
    private Rotate rotateStructureX, rotateStructureY;
    private double mouseXold, mouseXnew, mouseYold, mouseYNew, mouseXDelta, mouseYDelta;
    private HydrogenBondDetector hbDetector;
    private PDB123PrintLog printLog;
    private DotBracket dotBracket;
    private String dotBracketSeq;

    public PDB123Presenter(Stage primaryStage) {
        // Initialize class variables
        initClassVariables(primaryStage);
        // Add functionality to menubar elements
        exitFunction();
        loadFileFunction();
        // Set camera perspective
        setPerspective();
        // Set Mouse actions
        setMouseActions();


    }

    // Initialize class variables
    private void initClassVariables(Stage primaryStage) {
        this.stage = primaryStage;
        PDB123View = new PDB123View(primaryStage);
        this.printLog = new PDB123PrintLog(PDB123View.getLog());
        this.pdbReader = new ReadPDB(this.printLog);
        this.hbDetector = new HydrogenBondDetector(printLog);
        this.dotBracket = new DotBracket(printLog);
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
                // Recenter camera
                centerCam();
                // Colormode is fixed to residue type
                // Future: Can be changed by the user
                update3DStructure("resType");
            } catch (IOException e) {
                printLog.printLogMessage(e.getMessage());
            }
        });
    }


    // Add structures returned from PDBReader to Group structures
    // so that they are shown in the subscene
    // The order of the nucleotides in the PDB file is irrelevant
    private void update3DStructure(String colorMode) {

        Group structure = PDB123View.getThreeDrawings();
        // Clear previous structures
        structure.getChildren().clear();
        // Store links to previous PDBNucleotide and its index position
        PDBNucleotide prev = null;
        int prevIndex = -10;
        // Go through the hashmap containing all PDB nucleotides, starting at the lowest index position
        for (int i = firstNtIndex; i <= lastNtIndex; i++) {
            // If position is not contained in PDB file, continue with next position
            if (!ntMap.containsKey(i)) continue;
            // Get PDBNucleotide at current index position
            PDBNucleotide currentNt = ntMap.get(i);
            // Add structure from PDBNucleotide to subscene
            structure.getChildren().add(currentNt.getStructure());
            // Set the colormode for the purin/pyrimidine ring
            currentNt.setColorMode(colorMode);
            // If the current index and the previous one differ by one, connect the both nucleotides
            if (prevIndex == i - 1) {
                DrawLine backboneConnection = new DrawLine(prev.getThreePrimeEnd(), currentNt.getFivePrimeEnd());
                structure.getChildren().add(backboneConnection);
            }
            prevIndex = i;
            prev = currentNt;
        }
        // Identify and visualize hydrogen bonds
        detectHydrogenBonds();
        // Calculate dot-bracket notation for current PDB file
        calculateDotBracketNotation();



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
        Group hydrogenBonds = hbDetector.getHDB();
        PDB123View.getThreeDrawings().getChildren().add(hydrogenBonds);

    }

    // Reset camera + rotation to originals tate
    private void centerCam() {
        PerspectiveCamera cam = PDB123View.get3DCamera();
        cam.setTranslateX(0);
        cam.setTranslateY(0);
        cam.setTranslateZ(0);
        rotateStructureX.setAxis(new Point3D(PDB123View.get3DSubScene().getWidth() / 2, 0, 0));
        rotateStructureY.setAxis(new Point3D(0, PDB123View.get3DSubScene().getWidth() / 2, 0));

    }

    // Set camera settings and rotation of Group structure
    private void setPerspective() {
        PerspectiveCamera cam = PDB123View.get3DCamera();
        cam.setNearClip(0.0000001);
        cam.setFarClip(-100000.0);
        Group structure = PDB123View.getThreeDrawings();
        // Keep 3D Structure at center of subscene
        structure.translateXProperty().bind(PDB123View.get3DSubScene().widthProperty().divide(2.));
        structure.translateYProperty().bind(PDB123View.get3DSubScene().heightProperty().divide(2.));
        // Adjust Z-Positioning upon stage resize
        // Adjust Z-Position of Group proportional to stage width/height changes
        DoubleBinding sizeRatio = PDB123View.get3DSubScene().widthProperty().add(PDB123View.get3DSubScene().heightProperty());
        structure.translateZProperty().bind(sizeRatio.divide(-1.8));
        // Rotation of Group structure
        rotateStructureY = new Rotate(0, new Point3D(0, 1, 0));
        rotateStructureX = new Rotate(0, new Point3D(1, 0, 0));
        structure.getTransforms().addAll(rotateStructureX, rotateStructureY);

    }

    private void setMouseActions() {
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
                cam.setTranslateX(cam.getTranslateX() - mouseXDelta * 0.2);
                cam.setTranslateY(cam.getTranslateY() - mouseYDelta * 0.2);
            }
            if (!event.isShiftDown() && !event.isControlDown()) {
                rotateStructureY.setAngle(rotateStructureY.getAngle() + mouseXDelta * 0.2);
                rotateStructureX.setAngle(rotateStructureX.getAngle() - mouseYDelta * 0.2);
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
