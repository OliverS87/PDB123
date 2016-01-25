package TertStructure.PDB3D.PDBNucleobases;

import TertStructure.RNAMesh3D.*;
import javafx.geometry.Point3D;
import javafx.scene.Group;

/**
 * Created by oliver on 14.12.15.
 * Stores the PDB coordinates of one Cytosine.
 * Returns 3D structure.
 */
public class PDBCytosine extends PDBPyrimidine {
    //private Point3D N1, C2, N3, C4, C5, C6, O2;
    private Point3D N4, H41, H42;

    public Point3D getH41() {
        return H41;
    }

    public void setH41(Point3D h41) {
        H41 = h41;
    }

    public Point3D getH42() {
        return H42;
    }

    public void setH42(Point3D h42) {
        H42 = h42;
    }

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
        // Draw hydrogens and functional groups
        DrawOxygen oxygen2 = new DrawOxygen(this.getO2());
        DrawNitrogen nitrogen4 = new DrawNitrogen(N4);
        DrawHydrogen hydrogen41 = new DrawHydrogen(H41);
        DrawHydrogen hydrogen42 = new DrawHydrogen(H42);
        DrawHydrogen hydrogen5 = new DrawHydrogen(this.getH5());
        DrawHydrogen hydrogen6 = new DrawHydrogen(this.getH6());
        // Draw bondings between hydrogens, functional groups and the pyrimidine ring
        DrawLine c2ToO2 = new DrawLine(this.getC2(), this.getO2());
        DrawLine c4ToN4 = new DrawLine(this.getC4(), N4);
        DrawLine n4ToH41 = new DrawLine(N4, H41);
        DrawLine n4ToH42 = new DrawLine(N4, H42);
        DrawLine c5ToH5 = new DrawLine(this.getC5(), this.getH5());
        DrawLine c6ToH6 = new DrawLine(this.getC6(), this.getH6());
        cytosineGrp.getChildren().addAll(meshV, oxygen2, nitrogen4, hydrogen41, hydrogen42, hydrogen5, hydrogen6,
                c2ToO2, c4ToN4, n4ToH41, n4ToH42, c5ToH5, c6ToH6);
        return cytosineGrp;
    }
}
