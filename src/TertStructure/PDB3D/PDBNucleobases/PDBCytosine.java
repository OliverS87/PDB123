package TertStructure.PDB3D.PDBNucleobases;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.RNAMesh3D.DrawPyrimidine;

/**
 * Created by oliver on 14.12.15.
 * Stores the PDB coordinates of one Cytosine.
 * Returns 3D structure.
 */
public class PDBCytosine extends PDBPyrimidine {
    //private Point3D N1, C2, N3, C4, C5, C6, O2;
    private Point3D N4;

    public Point3D getN4() {
        return N4;
    }

    public void setN4(Point3D n4) {
        N4 = n4;
    }
    // Get 3D structure
    public Group getStructure()
    {
        Group cytosineGrp = new Group();
        // Draw a pyrimidine ring
        DrawPyrimidine drawCytosine = new DrawPyrimidine(this.getN1(), this.getC2(), this.getN3(), this.getC4(), this.getC5(), this.getC6());
        meshV = drawCytosine.getPyrimidine();
        cytosineGrp.getChildren().add(meshV);
        return cytosineGrp;
    }
}
