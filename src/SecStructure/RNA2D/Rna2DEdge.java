package SecStructure.RNA2D;

import javafx.beans.property.BooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Created by oliver on 30.11.15.
 * Shape Rn2DEdge based on Shape Line
 */
public class Rna2DEdge extends Line
{
    private BooleanProperty isSelected;
    private Color edgeColor;
    // Bind start + end X/Y to two Rna2DNodes
    public Rna2DEdge(Rna2DNode n1, Rna2DNode n2)
    {
        this.startXProperty().bind(n1.posXProperty());
        this.startYProperty().bind(n1.posYProperty());
        this.endXProperty().bind(n2.posXProperty());
        this.endYProperty().bind(n2.posYProperty());

    }
    // Covalent bonds are black, non-covalent blue
    public void setEdgeStyle(String style)
    {
        switch (style)
        {
            case("hydroBond"): {this.setStroke(Color.BLUE);break;}
            case("covBond"): {this.setStroke(Color.BLACK);break;}
        }

    }
    private void onSelection()
    {
        this.setOnMouseClicked(event -> {
            isSelected.setValue(!isSelected.getValue());
        });
    }
    private void selectionListener()
    {
        isSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue){
                this.setStrokeDashOffset(0.2);
            }
        });
    }
}
