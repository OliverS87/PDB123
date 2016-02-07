package SecStructure.RNA2D;

import GUI.LogMessagesUI.PDB123PrintLog;
import SecStructure.GraphCalculator.Graph;
import SecStructure.GraphCalculator.SpringEmbedder;
import javafx.scene.Group;
import javafx.scene.SubScene;

import java.io.IOException;
import java.util.*;

/**
 * Created by oliver on 26.01.16.
 * Main class for the composition of
 * the RNA 2D representation.
 * Takes dot-bracket notation, a List of Rna2DNodes and
 * a List of Rna2DEdges as input. Also requires references
 * to the 2D subscene ans the print log area on the main UI.
 * Updated Rna2DEdges and Rna2DNodes are added to Group
 * secDrawings.
 */
public class Rna2DGraph {
    private Group secStructure;
    private String dotBracketSeq;
    private ArrayList<Rna2DNode> nodes;
    private SubScene secSubScene;
    private Graph grp;
    private PDB123PrintLog log;
    private ArrayList<Rna2DNode> node2DList;
    private ArrayList<Rna2DEdge> edge2DList;


    public Rna2DGraph(Group secStructure, SubScene secSubScene, String dotBracketSeq, PDB123PrintLog log, ArrayList<Rna2DNode> node2DList, ArrayList<Rna2DEdge> edge2DList) {
        this.secStructure = secStructure;
        this.dotBracketSeq = dotBracketSeq;
        this.secSubScene = secSubScene;
        this.log = log;
        this.nodes = new ArrayList<>();
        this.grp = new Graph();
        this.node2DList = node2DList;
        this.edge2DList = edge2DList;
        // Allow proper resizing of the subscene
        resizeBindings();
    }

    public void getRna2D() {
        // Calculate coordinates of 2D Nodes
        calculateGraph();
        // Retrieve coordinates of 2D Nodes
        double[][] nodeCoordinates = calculateNodeCoordinates();
        // Use calculated coordinates to adjust the coordinates
        // of the Rna2DNodes in node2DList
        drawNodes(nodeCoordinates);
        // Coordinates of Rna2DEdges do not need to be adjusted.
        // They are bound to two Rna2D Nodes. Add Rna2DEdges
        // to group secStructure
        addEdges();
    }


    // Redraw 2D representation.
    // Function is called when subscene is resized.
    // Basically the same functionality as the initial
    // drawing of the 2D structure. Differences:
    // Graph is not recalculated. Container for all 2D elements
    // (Group secStructure) and container for nodes with adjusted
    // coordinates (ArrayList nodes) are cleared before.
    private void redrawRna2D() {
        secStructure.getChildren().clear();
        nodes.clear();
        double[][] nodeCoordinates = calculateNodeCoordinates();
        drawNodes(nodeCoordinates);
        addEdges();
    }


    // Set up a graph for the 2D representation of an RNA molecule
    // Use dot-bracket sequence to provide information about
    // base-paired nucleotides
    private void calculateGraph() {
        try {
            grp.parseNotation(dotBracketSeq);
        } catch (IOException e) {
            log.printLogMessage("Error during RNA 2D graph calculations.");
        }
    }

    // Use graph to calculate X/Y coordinates of nodes
    private double[][] calculateNodeCoordinates() {
        double[][] coordinates = SpringEmbedder.computeSpringEmbedding(1000, grp.getNumberOfNodes(), grp.getEdges(), null);
        // Get current width/height of drawArea. Leave a small border.
        int drawAreaWidth = (int) (secSubScene.getWidth() * 0.95);
        int drawAreaHeight = (int) (secSubScene.getHeight() * 0.95);
        // Center coordinates on drawArea
        SpringEmbedder.centerCoordinates(coordinates, 10, drawAreaWidth, 10, drawAreaHeight);
        return coordinates;
    }

    // Adjust XY-coordinates of Rna2DNodes in node2DList
    // with coordinates retrieved from SpringEmbedder graph.
    // Adjusted nodes are added to secStructure group.
    private void drawNodes(double[][] nodeCoordinates) {
        for (int i = 0; i < nodeCoordinates.length; i++) {
            Rna2DNode next = node2DList.get(i);
            next.setPosX(nodeCoordinates[i][0]);
            next.setPosY(nodeCoordinates[i][1]);
            nodes.add(next);
            secStructure.getChildren().add(next);
        }
    }

    // Add Rna2DEdges from edge2DList to Group secStructure
    private void addEdges() {
        for (Rna2DEdge edge : edge2DList) {
            secStructure.getChildren().add(edge);
        }
        // Bring Rna 2DNodes back to front
        nodes.forEach(circle -> circle.toFront());
    }

    // Recalculate 2D structure upon window resize
    private void resizeBindings() {
        secSubScene.heightProperty().addListener(observable -> redrawRna2D());
        secSubScene.widthProperty().addListener(observable -> redrawRna2D());


    }


}
