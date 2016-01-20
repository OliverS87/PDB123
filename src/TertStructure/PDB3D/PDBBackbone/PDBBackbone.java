package TertStructure.PDB3D.PDBBackbone;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import TertStructure.RNAMesh3D.DrawLine;

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
        Sphere phosphat = new Sphere(0.35);
        phosphat.setTranslateX(P.getX());
        phosphat.setTranslateY(P.getY());
        phosphat.setTranslateZ(P.getZ());
        phosphat.setMaterial(new PhongMaterial(Color.PURPLE));
        // Draw Oxygens
        Sphere oxygen1 = new Sphere(0.35/2);
        oxygen1.setTranslateX(OP1.getX());
        oxygen1.setTranslateY(OP1.getY());
        oxygen1.setTranslateZ(OP1.getZ());
        oxygen1.setMaterial(new PhongMaterial(Color.RED));
        Sphere oxygen2 = new Sphere(0.35/2);
        oxygen2.setTranslateX(OP2.getX());
        oxygen2.setTranslateY(OP2.getY());
        oxygen2.setTranslateZ(OP2.getZ());
        oxygen2.setMaterial(new PhongMaterial(Color.RED));
        // Connect Phosphat and oxygens
        DrawLine pToOp1 = new DrawLine(P, OP1);
        DrawLine pToOp2 = new DrawLine(P, OP2);
        backboneGrp.getChildren().addAll(phosphat, oxygen1, oxygen2, pToOp1.getStructure(), pToOp2.getStructure());
        return backboneGrp;
    }
}
