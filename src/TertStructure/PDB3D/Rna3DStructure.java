package TertStructure.PDB3D;

import SecStructure.RNA2D.Rna2DEdge;
import SecStructure.RNA2D.Rna2DNode;
import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;
import TertStructure.RNAMesh3D.DrawLine;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oliver on 29.01.16.
 * Main class for the assembly of an RNA 3D sequence
 * structure from its individual nucleotides
 */
public class Rna3DStructure
{
    private Group structure;

    public Rna3DStructure(Group structure) {
        this.structure = structure;
    }
    public void generate3DStructure(int firstNtIndex, int lastNtIndex, String colorMode, Map<Integer, PDBNucleotide> ntMap, BooleanProperty showBackbone, BooleanProperty showSugar, BooleanProperty showNucleoBase, ArrayList<Rna2DNode> rna2DNodes, ArrayList<Rna2DEdge> rna2DEdges){
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
            Group currStructure = currentNt.getStructure(showBackbone, showSugar, showNucleoBase);
            structure.getChildren().add(currStructure);
            // Set the colormode for the purin/pyrimidine ring
            currentNt.setColorMode(colorMode);
            // Initialize a 2D Node for 2D representation
            Rna2DNode curr2DNode = new Rna2DNode(0.,0.,2,i, currentNt.isSelectedProperty());
            rna2DNodes.add(curr2DNode);
            currentNt.setRna2DNode(curr2DNode);
            // If the current index and the previous one differ by one, connect the both nucleotides
            if (prevIndex == i - 1) {
                DrawLine backboneConnection = new DrawLine(prev.getThreePrimeEnd(), currentNt.getFivePrimeEnd());
                // Bind backbone visibility state to BooleanProperty showBackbone
                backboneConnection.visibleProperty().bind(showBackbone);
                structure.getChildren().add(backboneConnection);
                // Add 2D edge to 2D representation
                Rna2DEdge currEdge = new Rna2DEdge(prev.getRna2DNode(), curr2DNode);
                currEdge.setEdgeStyle("covBond");
                rna2DEdges.add(currEdge);
            }
            prevIndex = i;
            prev = currentNt;
        }
    }
}
