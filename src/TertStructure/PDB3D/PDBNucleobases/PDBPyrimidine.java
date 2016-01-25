package TertStructure.PDB3D.PDBNucleobases;

import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

/**
 * Created by oliver on 14.12.15.
 * Common 3D points for all pyrimidine nucleotides
 */
public class PDBPyrimidine
{
    private Point3D N1;
    private Point3D C2;
    private Point3D O2;
    private Point3D N3;
    private Point3D C4;
    private Point3D C5;
    private Point3D C6;
    private Point3D H5, H6;

    protected MeshView meshV;
    public void setNucleobaseColor(PhongMaterial phongM)
    {
        meshV.setMaterial(phongM);
    }
    public Point3D getO2() {
        return O2;
    }

    public void setO2(Point3D o2) {
        O2 = o2;
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

    public Point3D getH5() {
        return H5;
    }

    public void setH5(Point3D h5) {
        H5 = h5;
    }

    public Point3D getH6() {
        return H6;
    }

    public void setH6(Point3D h6) {
        H6 = h6;
    }
}
