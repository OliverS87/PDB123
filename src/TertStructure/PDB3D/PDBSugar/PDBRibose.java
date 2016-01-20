package TertStructure.PDB3D.PDBSugar;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import TertStructure.RNAMesh3D.DrawLine;
import TertStructure.RNAMesh3D.DrawRibose;

/**
 * Created by oliver on 15.12.15.
 * PDB123StartUp class for representation of ribose PDB coordinates.
 * Returns 3D structure generated with TertStructure.RNAMesh3D package.
 */
public class PDBRibose
{
    private Point3D C1,C2,C3,C4,C5,O2,O3,O4,O5;

    public Point3D getC1() {
        return C1;
    }

    public void setC1(Point3D c1) {
        C1 = c1;
    }

    public Point3D getC2() {
        return C2;
    }

    public void setC2(Point3D c2) {
        C2 = c2;
    }

    public Point3D getC3() {
        return C3;
    }

    public void setC3(Point3D c3) {
        C3 = c3;
    }

    public Point3D getC4() {
        return C4;
    }

    public void setC4(Point3D c4) {
        C4 = c4;
    }

    public Point3D getC5() {
        return C5;
    }

    public void setC5(Point3D c5) {
        C5 = c5;
    }

    public Point3D getO2() {
        return O2;
    }

    public void setO2(Point3D o2) {
        O2 = o2;
    }

    public Point3D getO3() {
        return O3;
    }

    public void setO3(Point3D o3) {
        O3 = o3;
    }

    public Point3D getO4() {
        return O4;
    }

    public void setO4(Point3D o4) {
        O4 = o4;
    }

    public Point3D getO5() {
        return O5;
    }

    public void setO5(Point3D o5) {
        O5 = o5;
    }

    // Group all 3D elements and return as Group
    // Group contains 3D mesh structure of ribose ring and additional atoms and
    // connecting lines
    public Group getStructure()
    {
        // Draw ribose
        Group riboseGrp = new Group();
        DrawRibose drawRibose = new DrawRibose(this.getC1(), this.getC2(), this.getC3(), this.getC4(), this.getO4());
        MeshView riboseMesh = drawRibose.getRibose();
        riboseMesh.setMaterial(new PhongMaterial(Color.BURLYWOOD));
        riboseGrp.getChildren().add(riboseMesh);
        // Draw connected C5 and O5
        Sphere carbon5 = new Sphere(0.35/1.5);
        carbon5.setTranslateX(C5.getX());
        carbon5.setTranslateY(C5.getY());
        carbon5.setTranslateZ(C5.getZ());
        carbon5.setMaterial(new PhongMaterial(Color.CHOCOLATE));
        DrawLine c4ToC5 = new DrawLine(C4,C5);
        Sphere oxygen5 = new Sphere(0.35/2);
        oxygen5.setTranslateX(O5.getX());
        oxygen5.setTranslateY(O5.getY());
        oxygen5.setTranslateZ(O5.getZ());
        oxygen5.setMaterial(new PhongMaterial(Color.RED));
        DrawLine c5ToO5 = new DrawLine(C5,O5);
        // Draw O3 connected to C3
        Sphere oxygen3 = new Sphere(0.35/2);
        oxygen3.setTranslateX(O3.getX());
        oxygen3.setTranslateY(O3.getY());
        oxygen3.setTranslateZ(O3.getZ());
        oxygen3.setMaterial(new PhongMaterial(Color.RED));
        DrawLine o3ToC3 = new DrawLine(O3,C3);
        riboseGrp.getChildren().addAll(carbon5, c4ToC5.getStructure(), oxygen5, c5ToO5.getStructure(), o3ToC3.getStructure(), oxygen3);
        return riboseGrp;
    }
}
