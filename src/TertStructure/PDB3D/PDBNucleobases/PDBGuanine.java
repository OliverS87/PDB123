package TertStructure.PDB3D.PDBNucleobases;

import TertStructure.RNA3DComponents.*;
import javafx.geometry.Point3D;
import javafx.scene.Group;

/**
 * Created by oliver on 14.12.15.
 * Stores PDB coordinates of one Guanine.
 * Returns 3D structure.
 */
public class PDBGuanine extends PDBPurin {
     private Point3D  O6, H1, H21, H22, N2;

    public Point3D getO6() {
        return O6;
    }

    public Point3D getH1() {
        return H1;
    }

    public Point3D getN2() {
        return N2;
    }

    public Point3D getH21() {
        return H21;
    }

    public Point3D getH22() {
        return H22;
    }

    public void setO6(Point3D o6) {
        O6 = o6;
    }

    public void setH1(Point3D h1) {
        H1 = h1;
    }

    public void setH21(Point3D h21) {
        H21 = h21;
    }

    public void setH22(Point3D h22) {
        H22 = h22;
    }

    public void setN2(Point3D n2) {
        N2 = n2;
    }


    public Group getStructure(){
        Group guanineGrp = new Group();
        // Draw Purin ring
        DrawPurin drawGuanine = new DrawPurin(this.getN1(), this.getC2(), this.getN1(), this.getC4(), this.getN9(), this.getC8(), this.getN7(), this.getC5(), this.getC6());
        meshV = drawGuanine.getPurin();
        // Draw hydrogens and functional groups
        DrawHydrogen h1 = new DrawHydrogen(H1);
        DrawHydrogen h21 = new DrawHydrogen(H21);
        DrawHydrogen h22 = new DrawHydrogen(H22);
        DrawHydrogen h8 = new DrawHydrogen(this.getH8());
        DrawNitrogen n2 = new DrawNitrogen(N2);
        DrawOxygen o6 = new DrawOxygen(O6);
        // Draw bondings between hydrogens, functional groups and the purin ring
        DrawLine h1n1 = new DrawLine(H1, this.getN1());
        DrawLine h21n2 = new DrawLine(H21, N2);
        DrawLine h22n2 = new DrawLine(H22, N2);
        DrawLine n2c2 = new DrawLine(N2, this.getC2());
        DrawLine h8c8 = new DrawLine(this.getH8(), this.getC8());
        DrawLine c6ToO6 = new DrawLine(this.getC6(), O6);
        guanineGrp.getChildren().addAll(meshV, h1, h21, h22, h8, n2,o6,
        h1n1, h21n2, h22n2, n2c2, h8c8, c6ToO6);
        return guanineGrp;
    }
}
