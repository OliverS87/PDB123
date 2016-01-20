package TertStructure.PDB3D.PDBNucleobases;

import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

/**
 * Created by oliver on 14.12.15.
 * Common 3D points for all purin nucleotides
 */
public abstract class PDBPurin
{
    private Point3D N1, C2, N3, C4, N9, C8, N7, C5, C6;
    protected MeshView meshV;
    public void setNucleobaseColor(PhongMaterial phongM)
    {
        meshV.setMaterial(phongM);
    }
    public Point3D getN1() {
        return N1;
    }

    public void setN1(Point3D n1) {
        N1 = n1;
    }

    public Point3D getC2() {
        return C2;
    }

    public void setC2(Point3D c2) {
        C2 = c2;
    }

    public Point3D getN3() {
        return N3;
    }

    public void setN3(Point3D n3) {
        N3 = n3;
    }

    public Point3D getC4() {
        return C4;
    }

    public void setC4(Point3D c4) {
        C4 = c4;
    }

    public Point3D getN9() {
        return N9;
    }

    public void setN9(Point3D n9) {
        N9 = n9;
    }

    public Point3D getC8() {
        return C8;
    }

    public void setC8(Point3D c8) {
        C8 = c8;
    }

    public Point3D getN7() {
        return N7;
    }

    public void setN7(Point3D n7) {
        N7 = n7;
    }

    public Point3D getC5() {
        return C5;
    }

    public void setC5(Point3D c5) {
        C5 = c5;
    }

    public Point3D getC6() {
        return C6;
    }

    public void setC6(Point3D c6) {
        C6 = c6;
    }
}
