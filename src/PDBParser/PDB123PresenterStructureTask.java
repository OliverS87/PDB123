package PDBParser;

import GUI.LogMessagesUI.PDB123PrintLog;
import GUI.SettingsUI.PDB123SettingsPresenter;
import PrimStructure.ParsePrimaryStructure;
import SecStructure.DotBracketNotation.DotBracket;
import SecStructure.RNA2D.Rna2DEdge;
import SecStructure.RNA2D.Rna2DGraph;
import SecStructure.RNA2D.Rna2DNode;
import SelectionModel.PDB123SelectionModel;
import TertStructure.Basepairing.HydrogenBondDetector;
import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;
import TertStructure.RNA3DComponents.DrawLine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oliver on 05.02.16.
 * Task creates RNA 1D, 2D and 3D representation.
 * PDBFile is read by ReadPDB and converted to a hashmap-styled
 * database ntMap. ntMap contains PDBNucleotides, one for each index position,
 * these hold the components for the 3D representation. 1D and 2D components
 * are build from the information contained in PDBNucleotide.PDBNucleotides are
 * connected to elements of 1D and 2D representation by references and boolean bindings.
 * Task returns four objects:
 * 1. ArrayList of Text for 1D representation
 * 2. Group of 2D shapes for 2D representation
 * 3. Group of 3D shapes for 3D representation
 * 4. Int[][] with nucleotide counts for bar chart plot statistics
 * All four objects are returned on one ArrayList<Object>
 */

// PDB123PresenterStructureTask extends Task, it can therefore be run in
// a background thread and still access some elements of the UI
public class PDB123PresenterStructureTask extends Task<ArrayList<Object>> {

    private PDB123PrintLog log;
    private ReadPDB pdbReader;
    private String pdbFilePath;
    private Map<Integer, PDBNucleotide> ntMap;
    private int firstNtIndex, lastNtIndex;
    private BooleanProperty showBackbone, showSugar, showNucleoBase, showHBonds;
    private SubScene subScene2D;
    private PDB123SettingsPresenter settings;


    // Task needs access to the following objects:
    // 1. Message area on main UI
    // 2. Path to PDB file
    // 3. Subscene of 2D representation
    // 4. Booleanproperties: Show elements of 3D structure?
    // 5. Settings
    // 1-3. are set by constructor
    // 4. are set by setBooleanProperties()
    // 5. are set by setSettings()

    public PDB123PresenterStructureTask(PDB123PrintLog log, String pdbFilePath, SubScene subScene2D) {
        this.log = log;
        pdbReader = new ReadPDB(log);
        this.pdbFilePath = pdbFilePath;
        this.subScene2D = subScene2D;
    }

    public void setBooleanProperties(BooleanProperty showBackbone, BooleanProperty showSugar, BooleanProperty showNucleoBase, BooleanProperty showHBonds) {
        this.showBackbone = showBackbone;
        this.showSugar = showSugar;
        this.showNucleoBase = showNucleoBase;
        this.showHBonds = showHBonds;
    }

    public void setSettings(PDB123SettingsPresenter settings) {
        this.settings = settings;
    }


    // A task is executed with call()
    // Status of task is regulary updated with updateMessage
    // and updateProgress
    @Override
    public ArrayList<Object> call() throws Exception {
        updateMessage("Parsing PDB file");
        updateProgress(0, 10);
        // Add some short breaks
        // so that user can enjoy the progress bar
        Thread.sleep(300);
        // Provide settings and filepath to PDBReader
        pdbReader.setSettings(settings);
        pdbReader.setFilePath(pdbFilePath);
        // Retrieve hashmap with PDBNucleotides and first and last
        // index position from pdbReader
        ntMap = pdbReader.getNtMap();
        firstNtIndex = pdbReader.getFirstNtIndex();
        lastNtIndex = pdbReader.getLastNtIndex();
        // Container for 1D, 2D and 3D Structures
        Group rna3DStructure = new Group();
        Group rna2DStructure = new Group();
        ArrayList<Text> rna1DStructure;
        ArrayList<Rna2DNode> rna2DNodes = new ArrayList<>();
        ArrayList<Rna2DEdge> rna2DEdges = new ArrayList<>();
        // Container structure for all return values
        ArrayList<Object> structure123D = new ArrayList<>(4);
        // Reset selection model
        PDB123SelectionModel.initSelectionModel();
        updateMessage("RNA 3D structure modelling");
        Thread.sleep(300);
        updateProgress(1, 10);
        // Go through the hashmap containing all PDB nucleotides, starting at the lowest index position
        // Store links to previous PDBNucleotide and its index position
        PDBNucleotide prev = null;
        // Start with an arbitrary low prevIndex
        // prevents that first nucleotide will be
        // connected to an non-existing previous nucleotide
        int prevIndex = -10;
        for (int i = firstNtIndex; i <= lastNtIndex; i++) {
            // If position is not contained in PDB file, continue with next position
            // -> Allow discontinuous residue indices
            // Also allows PDB files were residue index does not start with 1
            if (!ntMap.containsKey(i)) continue;
            // Get PDBNucleotide at current index position
            PDBNucleotide currentNt = ntMap.get(i);
            // Initialize 3D structure for current PDBNucleotide
            currentNt.getStructure(showBackbone, showSugar, showNucleoBase);
            // Add 3D structure to 3D subscene
            rna3DStructure.getChildren().add(currentNt);

            // Initialize a 2D Node ("Circle") for 2D representation
            // Store a reference to the 2D Node in PDBNucleotide
            // Use arbitrary x/y coordinates. These will be updated at a later stage
            // when the 2D Graph is calculated
            Rna2DNode curr2DNode = new Rna2DNode(0., 0., 2, i, currentNt.isSelectedProperty());
            // Set type of 2DNode to actual nucleotide type
            curr2DNode.identify(currentNt.getType().charAt(0));
            // Bind coloring to 3D nucleotide
            curr2DNode.fillProperty().bind(currentNt.ntColorProperty());
            // Collect all 2DNodes in one array List
            rna2DNodes.add(curr2DNode);
            // Store a reference to the 2DNode in the 3D nucleotide
            currentNt.setRna2DNode(curr2DNode);

            // Set covalent bonds between nucleotides in 3D and 2D structure
            // If the current index and the previous one differ by one, connect the both nucleotides
            if (prevIndex == i - 1) {
                DrawLine backboneConnection = new DrawLine(prev.getThreePrimeEnd(), currentNt.getFivePrimeEnd());
                // Bind backbone visibility state to BooleanProperty showBackbone
                backboneConnection.visibleProperty().bind(showBackbone);
                // Add backbone to 3D subscene
                rna3DStructure.getChildren().add(backboneConnection);
                // Add 2D edge to 2D representation
                // Covalent edges have no boolean property connection with their 3D edges
                // To satisfy constructor, a new BooleanProperty is handed over
                Rna2DEdge currEdge = new Rna2DEdge(prev.getRna2DNode(), curr2DNode, new SimpleBooleanProperty(false));
                currEdge.setEdgeStyle("covBond");
                // Store all 2Dedges in one ArrayList
                rna2DEdges.add(currEdge);
            }
            prevIndex = i;
            prev = currentNt;
        }

        updateProgress(5, 10);
        // Identify and visualize hydrogen bonds
        updateMessage("Detecting hydrogenbonds");
        Thread.sleep(300);
        detectHydrogenBonds(rna3DStructure, rna2DEdges);
        updateProgress(6, 10);

        updateProgress(7, 10);
        updateMessage("Calculating statistics");
        Thread.sleep(300);
        // Calculate statistics
        // Number of nucleotides
        // and number of base-paired nucleotides
        int[][] ntCounts = calculateStatisitcs();

        // Derive a dot-bracket notation from the detected hydrogen bonding pattern
        updateMessage("Deriving dotBracket notation");
        Thread.sleep(300);
        String dotBracketSeq = calculateDotBracketNotation();

        // Set RNA 2D structure
        updateMessage("RNA 2D structure modelling");
        Thread.sleep(300);
        updateProgress(8, 10);
        set2DGraph(rna2DStructure, dotBracketSeq, rna2DNodes, rna2DEdges);

        // Set 1D RNA representation
        // 1-letter Text objects are added to the FlowPane
        ParsePrimaryStructure parseSeq = new ParsePrimaryStructure();
        updateMessage("RNA 1D structure modelling");
        Thread.sleep(300);
        updateProgress(9, 10);
        rna1DStructure = parseSeq.parseRnaSeq(ntMap, firstNtIndex, lastNtIndex);
        // Collect all return values in ArrayList<Object> structure123D
        structure123D.add(rna1DStructure);
        structure123D.add(rna2DStructure);
        structure123D.add(rna3DStructure);
        structure123D.add(ntCounts);
        updateProgress(10, 10);
        updateMessage("Finished");
        Thread.sleep(300);
        // return
        return structure123D;
    }


    private void detectHydrogenBonds(Group rna3DStructure, ArrayList<Rna2DEdge> rna2DEdges) {
        // HbDetector detects hydrogenbonds in 3D structure
        // 3D Coordinates are taken from ntMap
        // If hydrogenbond was found, 2D and 3D hydrogenbonds
        // are added to 3D subscene (via Group structure3D)
        // and to 2D subscene via ArrayList rna2DEdges
        // hbDetector prints status messages on log textarea at main UI
        HydrogenBondDetector hbDetector = new HydrogenBondDetector(log);
        // First and last nt Index required to cover the complete nucleotide collection
        hbDetector.setFirstNtIndex(firstNtIndex);
        hbDetector.setLastNtIndex(lastNtIndex);
        hbDetector.setNtMap(ntMap);
        hbDetector.setRna2DEdge(rna2DEdges);
        hbDetector.setStructure3D(rna3DStructure);
        hbDetector.setSettings(settings);
        hbDetector.setShowHBonds(showHBonds);
        // call h-bond detection
        hbDetector.detectHDB();
    }

    private String calculateDotBracketNotation() {
        DotBracket dotBracket = new DotBracket(log);
        // Provide reference to hashmap with PDBnucleotides
        dotBracket.setNtMap(ntMap);
        // Provide first + last nt indices
        dotBracket.setFirstNtIndex(firstNtIndex);
        dotBracket.setLastNtIndex(lastNtIndex);
        // Calculate dotBracket notation and return as String
        // Boolean parameter tells if pseudoknots should be marked in dotBracket sequence
        return dotBracket.getDotBracket(settings.getDetectPseudoknots());
    }

    // Produce secondary structure from dot-bracket-sequence and with 2D nodes and edges generated before
    // No new 2D shapes are returned. Instead, the coordinates of the 2D nodes are adjusted and the 2D edges
    // are bound to existing 2D nodes
    private void set2DGraph(Group rna2DStructure, String dotBracketSeq, ArrayList<Rna2DNode> rna2DNodes, ArrayList<Rna2DEdge> rna2DEdges) {
        Rna2DGraph Graph2D = new Rna2DGraph(rna2DStructure, subScene2D, dotBracketSeq, log, rna2DNodes, rna2DEdges);
        Graph2D.getRna2D();
    }

    // Scan through PDBNucleotide map and calculate
    // the occurences of the nucleotides A,C,G,U in general
    // and the occurrences of A,C,G,U in base-pairings.
    private int[][] calculateStatisitcs() {
        int[] ntCountsOverall = new int[4];
        int[] ntCountsBasepaired = new int[4];
        int[][] ntCounts = {ntCountsOverall, ntCountsBasepaired};
        for (int i = firstNtIndex; i <= lastNtIndex; i++) {
            if (!ntMap.containsKey(i)) continue;
            PDBNucleotide currNt = ntMap.get(i);
            Boolean currNtBasepaired = currNt.isBasePaired();
            String ntType = currNt.getType();
            switch (ntType) {
                case ("A"):
                    ntCountsOverall[0]++;
                    if (currNtBasepaired) ntCountsBasepaired[0]++;
                    break;
                case ("C"):
                    ntCountsOverall[1]++;
                    if (currNtBasepaired) ntCountsBasepaired[1]++;
                    break;
                case ("G"):
                    ntCountsOverall[2]++;
                    if (currNtBasepaired) ntCountsBasepaired[2]++;
                    break;
                case ("U"):
                    ntCountsOverall[3]++;
                    if (currNtBasepaired) ntCountsBasepaired[3]++;
                    break;
            }

        }

        return ntCounts;
    }


}
