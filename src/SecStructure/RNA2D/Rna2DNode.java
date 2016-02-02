package SecStructure.RNA2D;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by oliver on 30.11.15.
 * Shape for RNa2DNode based on Circle
 */
public class Rna2DNode extends Circle
{
    // X/Y coordinates property bindings
    private SimpleDoubleProperty posX = new SimpleDoubleProperty();
    private SimpleDoubleProperty posY = new SimpleDoubleProperty();
    private BooleanProperty isSelected;
   // private SimpleDoubleProperty radius = new SimpleDoubleProperty();
    // Index of node in structure
    private int nodeNr;
    private Color ntColor;

    public Rna2DNode(Double posX, Double posY, double radius, int nodeNr, BooleanProperty isSelected) {
        this.posX.setValue(posX);
        this.posY.setValue(posY);
        this.setRadius(radius);
        this.radiusProperty().setValue(radius);
        this.centerXProperty().bind(this.posX);
        this.centerYProperty().bind(this.posY);
        this.nodeNr=nodeNr;
        this.isSelected=isSelected;
        isSelectedListener();
    }


    private void isSelectedListener()
    {
        isSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue)
            {
                this.setFill(ntColor.invert());
                this.setRadius(this.getRadius()*1.2);
            }
            if (!newValue)
            {
                this.setFill(ntColor);
                this.setRadius(this.getRadius()/1.2);
            }
        });
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

    // Set color + tooltip according to associated character
    public void identify(char nucleotide)
    {
        String ntId;

        switch (nucleotide) {
            case ('A'):
                ntId = "Adenine";
                ntColor = Color.DARKGREEN;
                break;
            case ('C'):
                ntId = "Cytosine";
                ntColor = Color.YELLOW;
                break;
            case ('G'):
                ntId = "Guanine";
                ntColor = Color.CYAN;
                break;
            case ('T'):
                ntId = "Thymine";
                ntColor = Color.HOTPINK;
                break;
            case ('U'):
                ntId = "Uracile";
                ntColor = Color.HOTPINK;
                break;
            default:
                ntId = "Nucleotide";
                ntColor = Color.FLORALWHITE;
        }
        Tooltip.install(this, new Tooltip(ntId + " " + nodeNr));
        this.setOnMouseClicked(event -> {
            System.out.println("node click");
            isSelected.setValue(!isSelected.getValue());
        });
        this.setFill(ntColor);
    }



}
