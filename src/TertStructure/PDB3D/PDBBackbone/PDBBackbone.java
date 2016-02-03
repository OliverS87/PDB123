package TertStructure.PDB3D.PDBBackbone;

import TertStructure.RNA3DComponents.DrawOxygen;
import TertStructure.RNA3DComponents.DrawPhosphat;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.RNA3DComponents.DrawLine;

/**
 * Created by oliver on 14.12.15.
 * Stores coordinates of an RNA backbone
 * Returns 3D structure of backbone
 */
public class PDBBackbone {
    private Point3D P, OP1, OP2;

    public Point3D getP() {
        return P;
    }

    public void setP(Point3D p) {
        P = p;
    }

    public Point3D getOP1() {
        return OP1;
    }

    public void setOP1(Point3D OP1) {
        this.OP1 = OP1;
    }

    public Point3D getOP2() {
        return OP2;
    }

    public void setOP2(Point3D OP2) {
        this.OP2 = OP2;
    }
    public Group getStructure(){
        Group backboneGrp = new Group();
        // Check if phosphat is present (not found in first residue of chain)
        if (P==null) return backboneGrp;
        // Draw Phosphat
        DrawPhosphat phosphat = new DrawPhosphat(P);
        // Draw Oxygens
        DrawOxygen oxygen1=new DrawOxygen(OP1);
        DrawOxygen oxygen2=new DrawOxygen(OP2);

        // Connect Phosphat and oxygens
        DrawLine pToOp1 = new DrawLine(P, OP1);
        DrawLine pToOp2 = new DrawLine(P, OP2);
        backboneGrp.getChildren().addAll(phosphat, oxygen1, oxygen2, pToOp1, pToOp2);
        return backboneGrp;
    }
}
