package TertStructure.PDB3D.PDBNucleobases;

import TertStructure.RNA3DComponents.*;
import javafx.geometry.Point3D;
import javafx.scene.Group;

/**
 * Created by oliver on 14.12.15.
 * Stores PDB coordinates of one Uracil.
 * Returns 3D Structure.
 */
public class PDBUracil extends PDBPyrimidine {
    private Point3D O4, H3;

    public Point3D getO4() {
        return O4;
    }

    public void setO4(Point3D o4) {
        O4 = o4;
    }

    public Point3D getH3() {
        return H3;
    }

    public void setH3(Point3D h3) {
        H3 = h3;
    }

    public Group getStructure() {
        Group uracilGrp = new Group();
        // Draw a pyrimidine ring
        DrawPyrimidine drawUracile = new DrawPyrimidine(this.getN1(), this.getC2(), this.getN3(), this.getC4(), this.getC5(), this.getC6());
        meshV = drawUracile.getPyrimidine();
        // Draw hydrogens and functional groups
        DrawOxygen oxygen1 = new DrawOxygen(this.getO2());
        DrawOxygen oxygen4 = new DrawOxygen(this.getO4());
        DrawHydrogen hydrogen3 = new DrawHydrogen(H3);
        DrawHydrogen hydrogen5 = new DrawHydrogen(this.getH5());
        DrawHydrogen hydrogen6 = new DrawHydrogen(this.getH6());
        // Draw bondings between hydrogens, functional groups and the pyrimidine ring
        DrawLine c2ToO2 = new DrawLine(this.getC2(), this.getO2());
        DrawLine n3ToH3 = new DrawLine(this.getN3(), H3);
        DrawLine c4ToO4 = new DrawLine(this.getC4(), O4);
        DrawLine c5ToH5 = new DrawLine(this.getC5(), this.getH5());
        DrawLine c6ToH6 = new DrawLine(this.getC6(), this.getH6());
        uracilGrp.getChildren().addAll(meshV, oxygen1, oxygen4, hydrogen3, hydrogen5, hydrogen6,
                c2ToO2, n3ToH3, c4ToO4, c5ToH5, c6ToH6);
        return uracilGrp;
    }
}
