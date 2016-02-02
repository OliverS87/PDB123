package SecStructure.RNA2D;

import GUI.PDB123PrintLog;
import SecStructure.GraphCalculator.Graph;
import SecStructure.GraphCalculator.SpringEmbedder;
import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.SubScene;

import java.io.IOException;
import java.util.*;

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
    private ArrayList<Rna2DNode> node2DList;
    private ArrayList<Rna2DEdge> edge2DList;


    public Rna2DGraph(Group secDrawings, SubScene secSubScene, String dotBracketSeq,  PDB123PrintLog log, ArrayList<Rna2DNode> node2DList, ArrayList<Rna2DEdge> edge2DList) {
        this.root2D = secDrawings;
        this.dotBracketSeq = dotBracketSeq;
        this.secSubScene = secSubScene;
        this.rnaSeq = null;
        this.log = log;
        this.nodes = new ArrayList<>();
        this.grp = new Graph();
        this.node2DList=node2DList;
        this.edge2DList=edge2DList;
       resizeBindings();
    }
    public void getRna2D()
    {
       root2D.getChildren().clear();
        nodes.clear();
        root2D.setRotate(0);

        calculateGraph();
        double[][] nodeCoordinates = calculateNodeCoordinates();
        drawNodes(nodeCoordinates);
        drawEdges();

    }

    private void redrawRna2D()
    {
        root2D.getChildren().clear();
        nodes.clear();
        root2D.setRotate(0);

        //calculateGraph();
        double[][] nodeCoordinates = calculateNodeCoordinates();
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
    private double[][] calculateNodeCoordinates() {
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

            Rna2DNode  next = node2DList.get(i);
            next.setPosX(nodeCoordinates[i][0]);
            next.setPosY(nodeCoordinates[i][1]);



           // try {
             //   next.identify(rnaSeq[i]);
            //} catch (ArrayIndexOutOfBoundsException e) {
              //  next.identify('?');
            //}

            nodes.add(next);
            root2D.getChildren().add(next);

        }

    }

    private void drawEdges() {
        // Add elements in list of edges to root2d
        for (Rna2DEdge edge:edge2DList
             ) {
            System.out.println(edge.toString());
            root2D.getChildren().add(edge);
        }


        // Bring nodes back to front
        nodes.forEach(circle -> circle.toFront());
    }

    // Recalculate 2D structure upon window resize
    private void resizeBindings(){
       secSubScene.heightProperty().addListener(observable -> redrawRna2D());
       secSubScene.widthProperty().addListener(observable -> redrawRna2D());


    }



}
