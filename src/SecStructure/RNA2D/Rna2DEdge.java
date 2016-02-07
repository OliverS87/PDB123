package SecStructure.RNA2D;

import SelectionModel.PDB123SelectionModel;
import SelectionModel.Selectable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * Created by oliver on 30.11.15.
 * 2D representation of a bond (covalent or h-bond) between
 * two 2D nucleotides (Rna2DNode).
 */
public class Rna2DEdge extends Line implements Selectable {
    private BooleanProperty isSelected;
    private Color selectedColor, unselectedColor;
    private int posNt1, posNt2;

    // Bind start + end X/Y to two Rna2DNodes
    public Rna2DEdge(Rna2DNode n1, Rna2DNode n2, BooleanProperty isSelected) {
        this.startXProperty().bind(n1.posXProperty());
        this.startYProperty().bind(n1.posYProperty());
        this.endXProperty().bind(n2.posXProperty());
        this.endYProperty().bind(n2.posYProperty());
        this.isSelected = isSelected;
        posNt1 = n1.getNodeNr();
        posNt2 = n2.getNodeNr();
        // Register Rna2DEdge at PDB123SelectionModel
        PDB123SelectionModel.registerSelectable(this);
        PDB123SelectionModel.addMouseHandler(this);
    }

    public void checkForPseudoKnot(ArrayList<Integer> listOfPseudoKnottedNts) {
        if ((listOfPseudoKnottedNts.lastIndexOf(posNt1) != -1) || (listOfPseudoKnottedNts.lastIndexOf(posNt2) != -1)) {
            System.out.println("dotted line");
        }
    }


    // Set edge color. Unlike for Rna2DNode, edge color is not bound to
    // edge color of 3D representation. Covalent bonds are black, non-covalent blue/red
    public void setEdgeStyle(String style) {
        switch (style) {
            case ("hydroBond"): {
                unselectedColor = Color.web("#0099e6");
                selectedColor = Color.web("#cc0000");
                break;
            }
            case ("covBond"): {
                unselectedColor = selectedColor = Color.BLACK;
                break;
            }
        }
        // Change color depending on selection-status
        this.strokeProperty().bind(Bindings.when(isSelected).then(selectedColor).otherwise(unselectedColor));

    }


    @Override
    public boolean isSelected() {
        return isSelected.getValue();
    }

    @Override
    public void setSelected(boolean sel) {
        isSelected.setValue(sel);
    }


}
