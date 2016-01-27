package SecStructure.RNA2D;

import GUI.PDB123PrintLog;
import SecStructure.GraphCalculator.Graph;
import SecStructure.GraphCalculator.SpringEmbedder;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.SubScene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by oliver on 26.01.16.
 * Main class for the calculation of
 * the RNA 2D representation.
 * Takes dot-bracket notation as input.
 */
public class Rna2DGraph {
    private Group root2D;
    private String dotBracketSeq;
    private char[] rnaSeq;
    private ArrayList<Rna2DNode> nodes;
    private SubScene secSubScene;
    private Graph grp;
    private PDB123PrintLog log;
    private Timer timer;
    long lastResizeTime = System.currentTimeMillis();

    public Rna2DGraph(Group secDrawings, SubScene secSubScene, String dotBracketSeq, String rnaSeq, PDB123PrintLog log) {
        this.root2D = secDrawings;
        this.dotBracketSeq = dotBracketSeq;
        this.secSubScene = secSubScene;
        this.rnaSeq = rnaSeq.toCharArray();
        this.log = log;
        this.nodes = new ArrayList<>();
        this.grp = new Graph();
        timer = new Timer();

        resizeBindings();
    }
    public void getRna2D()
    {
        root2D.getChildren().clear();
        nodes.clear();
        root2D.setRotate(0);

        calculateGraph();
        double[][] nodeCoordinates = calculateNodeCoordinates(false);
        drawNodes(nodeCoordinates);
        drawEdges();

    }


    private void calculateGraph() {

        try {
            grp.parseNotation(dotBracketSeq);
        } catch (IOException e) {
            log.printLogMessage("Error during RNA 2D graph calculations.");

        }
    }

    // Use graph to calculate X/Y coordinates of nodes
    private double[][] calculateNodeCoordinates(Boolean rotate) {
        double[][] coordinates = SpringEmbedder.computeSpringEmbedding(1000, grp.getNumberOfNodes(), grp.getEdges(), null);
        // Get current width/height of drawArea. Leave a small border.
        int drawAreaWidth = (int) (secSubScene.getWidth() * 0.95);
        int drawAreaHeight = (int) (secSubScene.getHeight() * 0.95);
        // Center coordinates on drawArea
        SpringEmbedder.centerCoordinates(coordinates, 10, drawAreaWidth, 10, drawAreaHeight);
        return coordinates;
    }

    // Create Circle node and add to drawArea
    // Collect nodes in ArrayList
    // Color and Tooltip is set based on sequence letter
    private void drawNodes(double[][] nodeCoordinates) {


        for (int i = 0; i < nodeCoordinates.length; i++) {

            Rna2DNode  next = new Rna2DNode(nodeCoordinates[i][0], nodeCoordinates[i][1], 3, i);

            try {
                next.identify(rnaSeq[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                next.identify('?');
            }

            next.setRadius(2);
            nodes.add(next);
            root2D.getChildren().add(next);

        }

    }

    private void drawEdges() {
        // List of edges
        // Each edge is defined by its start and end node
        int[][] edges = grp.getEdges();
        // Assign every Rna2DEdge with two RNa2DNodes
        for (int i = 0; i < grp.getNumberOfEdges(); i++) {
            Rna2DEdge edge = new Rna2DEdge(nodes.get(edges[i][0]), nodes.get(edges[i][1]));
            // Check if edge connects neighboring nodes -> covalent
            boolean isCovalent = (Math.abs(edges[i][0] - edges[i][1]) == 1);
            edge.setEdgeColor(isCovalent);
            root2D.getChildren().add(edge);
        }
        // Bring nodes back to front
        nodes.forEach(circle -> circle.toFront());
    }

    // Recalculate 2D structure upon window resize
    private void resizeBindings(){
       secSubScene.heightProperty().addListener(observable -> getRna2D());
       secSubScene.widthProperty().addListener(observable -> getRna2D());


    }



}
