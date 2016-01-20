package TertStructure.PDB3D.PDBNucleobases;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.RNAMesh3D.DrawPyrimidine;

/**
 * Created by oliver on 14.12.15.
 * Stores PDB coordinates of one Uracil.
 * Returns 3D Structure.
 */
public class PDBUracil extends PDBPyrimidine {
    private Point3D O4;

    public Point3D getO4() {
        return O4;
    }

    public void setO4(Point3D o4) {
        O4 = o4;
    }

    public Group getStructure()
    {
        Group uracilGrp = new Group();
        // Draw a pyrimidine ring
        DrawPyrimidine drawUracile = new DrawPyrimidine(this.getN1(), this.getC2(), this.getN3(), this.getC4(), this.getC5(), this.getC6());
        meshV = drawUracile.getPyrimidine();
        uracilGrp.getChildren().add(meshV);
        return uracilGrp;
    }
}
