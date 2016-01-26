package SecStructure.RNA2D;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Created by oliver on 30.11.15.
 * Shape Rn2DEdge based on Shape Line
 */
public class Rna2DEdge extends Line {
    // Bind start + end X/Y to two Rna2DNodes
    public Rna2DEdge(Rna2DNode n1, Rna2DNode n2)
    {
        this.startXProperty().bind(n1.posXProperty());
        this.startYProperty().bind(n1.posYProperty());
        this.endXProperty().bind(n2.posXProperty());
        this.endYProperty().bind(n2.posYProperty());
    }
    // Covalent bonds are white, non-covalent red
    public void setEdgeColor(boolean isCovalent)
    {
        if (isCovalent) this.setStroke(Color.WHITE);
        else this.setStroke(Color.RED);
    }
}
