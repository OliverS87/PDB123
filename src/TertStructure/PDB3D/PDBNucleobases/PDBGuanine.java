package TertStructure.PDB3D.PDBNucleobases;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.RNAMesh3D.DrawPurin;

/**
 * Created by oliver on 14.12.15.
 * Stores PDB coordinates of one Guanine.
 * Returns 3D structure.
 */
public class PDBGuanine extends PDBPurin {
     private Point3D O2;

    public Point3D getO2() {
        return O2;
    }

    public void setO2(Point3D o2) {
        O2 = o2;
    }
    public Group getStructure(){
        Group guanineGrp = new Group();
        // Draw Purin ring
        DrawPurin drawGuanine = new DrawPurin(this.getN1(), this.getC2(), this.getN1(), this.getC4(), this.getN9(), this.getC8(), this.getN7(), this.getC5(), this.getC6());
        meshV = drawGuanine.getPurin();
        guanineGrp.getChildren().add(meshV);
        return guanineGrp;
    }
}
