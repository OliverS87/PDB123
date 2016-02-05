package SecStructure.RNA2D;

import SelectionModel.PDB123SelectionModel;
import SelectionModel.Selectable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * Created by oliver on 30.11.15.
 * Shape Rn2DEdge based on Shape Line
 */
public class Rna2DEdge extends Line implements Selectable
{
    private BooleanProperty isSelected;
    private Color selectedColor, unselectedColor;
    private BooleanProperty greyScaleOnHL = new SimpleBooleanProperty(false);
    private int posNt1, posNt2;
    // Bind start + end X/Y to two Rna2DNodes
    public Rna2DEdge(Rna2DNode n1, Rna2DNode n2, BooleanProperty isSelected)
    {
        this.startXProperty().bind(n1.posXProperty());
        this.startYProperty().bind(n1.posYProperty());
        this.endXProperty().bind(n2.posXProperty());
        this.endYProperty().bind(n2.posYProperty());
        this.isSelected=isSelected;
        // Register Rna2DEdge at PDB123SelectionModel
        PDB123SelectionModel.registerSelectable(this);
       PDB123SelectionModel.addMouseHandler(this);
        posNt1 = n1.getNodeNr();
        posNt2 = n2.getNodeNr();

    }

    public void checkForPseudoKnot(ArrayList<Integer> listOfPseudoKnottedNts)
    {
        if ((listOfPseudoKnottedNts.lastIndexOf(posNt1) != -1) || (listOfPseudoKnottedNts.lastIndexOf(posNt2) != -1)){
        System.out.println("dotted line");
    }
    }



    // Covalent bonds are black, non-covalent blue
    public void setEdgeStyle(String style)
    {
        switch (style)
        {
            case("hydroBond"): {
                unselectedColor = Color.web("#0099e6");
                selectedColor = Color.web("#006699");
                break;
            }
            case("covBond"): {
                unselectedColor=selectedColor= Color.BLACK;break;
            }
        }
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

    @Override
    public void setHLGreyscale(boolean hl) {
        greyScaleOnHL.setValue(hl);
    }
}
