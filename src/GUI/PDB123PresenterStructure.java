package GUI;

import PrimStructure.ParsePrimaryStructure;
import SecStructure.DotBracketNotation.DotBracket;
import SecStructure.RNA2D.Rna2DEdge;
import SecStructure.RNA2D.Rna2DGraph;
import SecStructure.RNA2D.Rna2DNode;
import TertStructure.Basepairing.HydrogenBondDetector;
import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;
import TertStructure.RNAMesh3D.DrawLine;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oliver on 29.01.16.
 * Main class for the assembly of  RNA 1D 2D and 3D representation
 * Coordinates are taken from ntMap, containing a PDBNucleotide for
 * every index position.
 * 3D structure elements are added to the root group of 3D subscene.
 * 2D structure elements (Nodes and Edges) are initially stored in array lists.
 * Final 2D structure is then build by RNA2D.RNA2DGraph.
 *
 */
public class PDB123PresenterStructure
{
    private Group structure3D;
    private HydrogenBondDetector hbDetector;
    private DotBracket dotBracket;
    private ParsePrimaryStructure parseSeq;
    private Rna2DGraph Graph2D;
    private PDB123PrintLog printLog;

    public PDB123PresenterStructure(Group structure3D, PDB123PrintLog printLog) {

        this.structure3D = structure3D;
        this.hbDetector = new HydrogenBondDetector(printLog);
        this.dotBracket = new DotBracket(printLog);
        this.parseSeq = new ParsePrimaryStructure();
        this.printLog = printLog;


    }
    public void generate123DRepresentation(int firstNtIndex, int lastNtIndex, String colorMode, Map<Integer, PDBNucleotide> ntMap, BooleanProperty showBackbone, BooleanProperty showSugar, BooleanProperty showNucleoBase, ArrayList<Rna2DNode> rna2DNodes, ArrayList<Rna2DEdge> rna2DEdges, TextFlow primaryStructure, Group secStructure, SubScene subScene2D){
        // Clear previous 3D structure
        structure3D.getChildren().clear();
        // Clear previous 2D structure
        rna2DNodes.clear();
        rna2DEdges.clear();
        // Clear previous 1D structure
        primaryStructure.getChildren().clear();
        // Go through the hashmap containing all PDB nucleotides, starting at the lowest index position
        // Store links to previous PDBNucleotide and its index position
        PDBNucleotide prev = null;
        // Start with an arbitrary low prevIndex
        // prevents that first nucleotide will be
        // connected to an non-existant previous nucleotide
        int prevIndex = -10;
        for (int i = firstNtIndex; i <= lastNtIndex; i++) {
            // If position is not contained in PDB file, continue with next position
            // -> Allow discontineous residue indices
            // Also allows PDB files were residue index does not start with 1
            if (!ntMap.containsKey(i)) continue;
            // Get PDBNucleotide at current index position
            PDBNucleotide currentNt = ntMap.get(i);
            // Retrieve 3D structure from current PDBNucleotide
            Group currStructure = currentNt.getStructure(showBackbone, showSugar, showNucleoBase);
            // Add a Mouse-click event handler to 3D Structure
            currStructure.setOnMouseClicked(event -> System.out.println("3D Structure clicked"));
            // Add 3D structure to 3D subscene
            structure3D.getChildren().add(currStructure);
            // Set the colormode for the 3D structure
            currentNt.setColorMode(colorMode);

            // Initialize a 2D Node ("Circle") for 2D representation
            // Store a reference to the 2D Node in PDBNucleotide
            // Use arbitrary x/y coordinates. These will be updated at a later stage
            Rna2DNode curr2DNode = new Rna2DNode(0.,0.,2,i, currentNt.isSelectedProperty());
            curr2DNode.identify(currentNt.getType().charAt(0));
            rna2DNodes.add(curr2DNode);
            currentNt.setRna2DNode(curr2DNode);

            // Set covalent bonds between nucleotides in 3D and 2D structure
            // If the current index and the previous one differ by one, connect the both nucleotides
            if (prevIndex == i - 1) {
                DrawLine backboneConnection = new DrawLine(prev.getThreePrimeEnd(), currentNt.getFivePrimeEnd());
                // Bind backbone visibility state to BooleanProperty showBackbone
                backboneConnection.visibleProperty().bind(showBackbone);
                // Add backbone to 3D subscene
                structure3D.getChildren().add(backboneConnection);
                // Add 2D edge to 2D representation
                Rna2DEdge currEdge = new Rna2DEdge(prev.getRna2DNode(), curr2DNode);
                currEdge.setEdgeStyle("covBond");
                rna2DEdges.add(currEdge);
            }
            prevIndex = i;
            prev = currentNt;
        }
        // Identify and visualize hydrogen bonds
        detectHydrogenBonds(firstNtIndex, lastNtIndex, ntMap, rna2DEdges);
        // Derive a dot-bracket notation from the detected hydrogen bonding pattern
        String dotBracketSeq  = calculateDotBracketNotation(ntMap, firstNtIndex, lastNtIndex);
        Graph2D = new Rna2DGraph(secStructure, subScene2D, dotBracketSeq, printLog, rna2DNodes, rna2DEdges);
        Graph2D.getRna2D();

        // Set 1D RNA representation
        // 1-letter Text objects are added to the FlowPane
        parseSeq.parseRnaSeq(ntMap, firstNtIndex, lastNtIndex, primaryStructure);


    }


    private void detectHydrogenBonds(int firstNtIndex, int lastNtIndex, Map<Integer, PDBNucleotide> ntMap, ArrayList<Rna2DEdge> rna2DEdges)
    {
        // HbDetector detects hydrogenbonds in 3D structure
        // 3D Coordinates are taken from ntMap
        // If hydrogenbond was found, 2D and 3D hydrogenbonds
        // are added to 3D subscene (via Group structure3D)
        // and to 2D subscene via ArrayList rna2DEdges
        hbDetector.setFirstNtIndex(firstNtIndex);
        hbDetector.setLastNtIndex(lastNtIndex);
        hbDetector.setNtMap(ntMap);
        hbDetector.setRna2DEdge(rna2DEdges);
        hbDetector.setStructure3D(this.structure3D);
        hbDetector.detectHDB();

    }

    private String calculateDotBracketNotation(Map<Integer, PDBNucleotide> ntMap, int firstNtIndex, int lastNtIndex)
    {
        // Provide reference to ntMap
        dotBracket.setNtMap(ntMap);
        // Provide first + last nt indices
        dotBracket.setFirstNtIndex(firstNtIndex);
        dotBracket.setLastNtIndex(lastNtIndex);
        // Calculate dotBracket notation and return as String
        return dotBracket.getDotBracket();
    }
}
