package SecStructure.RNA2D;

import SelectionModel.PDB123SelectionModel;
import SelectionModel.Selectable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Circle;

/**
 * Created by oliver on 30.11.15.
 * Representation for RNA 2D Node. Based on circle.
 */
public class Rna2DNode extends Circle implements Selectable {
    // X/Y coordinates property bindings
    private SimpleDoubleProperty posX = new SimpleDoubleProperty();
    private SimpleDoubleProperty posY = new SimpleDoubleProperty();
    private BooleanProperty isSelected;

    // Index of node in structure
    private int nodeNr;


    public Rna2DNode(Double posX, Double posY, double radius, int nodeNr, BooleanProperty isSelected) {
        this.posX.setValue(posX);
        this.posY.setValue(posY);
        this.setRadius(radius);
        this.centerXProperty().bind(this.posX);
        this.centerYProperty().bind(this.posY);
        this.nodeNr = nodeNr;
        this.isSelected = isSelected;
        // Radius depends on selection status
        // -> node is enlarged when selected
        this.radiusProperty().bind(Bindings.when(isSelected).then(radius * 1.2).otherwise(radius));
    }

    public SimpleDoubleProperty posXProperty() {
        return posX;
    }

    public SimpleDoubleProperty posYProperty() {
        return posY;
    }

    public void setPosX(double posX) {
        this.posX.set(posX);
    }

    public void setPosY(double posY) {
        this.posY.set(posY);
    }

    public int getNodeNr() {
        return nodeNr;
    }

    // Set tooltip according to associated character
    public void identify(char nucleotide) {
        String ntId;

        switch (nucleotide) {
            case ('A'):
                ntId = "Adenine";

                break;
            case ('C'):
                ntId = "Cytosine";

                break;
            case ('G'):
                ntId = "Guanine";

                break;
            case ('T'):
                ntId = "Thymine";

                break;
            case ('U'):
                ntId = "Uracile";

                break;
            default:
                ntId = "Nucleotide";

        }
        // Install tooltip for this 2D node
        Tooltip.install(this, new Tooltip(ntId + " " + nodeNr));
        // Register Rna2DNode at mouse selection model
        PDB123SelectionModel.registerSelectable(this);
        PDB123SelectionModel.addMouseHandler(this);


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
