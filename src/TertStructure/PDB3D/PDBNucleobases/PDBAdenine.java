package TertStructure.PDB3D.PDBNucleobases;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.RNAMesh3D.DrawPurin;

/**
 * Created by oliver on 14.12.15.
 * Stores PDB coordinates of one Adenine.
 * Returns 3D structure.
 */
public class PDBAdenine extends PDBPurin
{

    private Point3D N6, O2;

    public Point3D getN6() {
        return N6;
    }

    public void setN6(Point3D n6) {
        N6 = n6;
    }

    public Point3D getO2() {
        return O2;
    }

    public void setO2(Point3D o2) {
        O2 = o2;
    }

    public Group getStructure()
    {
        Group adenineGGrp = new Group();
        // Draw a purin ring
        DrawPurin drawAdenine = new DrawPurin(this.getN1(), this.getC2(), this.getN1(), this.getC4(), this.getN9(), this.getC8(), this.getN7(), this.getC5(), this.getC6());
        meshV = drawAdenine.getPurin();
        //this.setNucleobaseColor(new PhongMaterial(Color.DARKGREEN));
        adenineGGrp.getChildren().add(meshV);
        return adenineGGrp;
    }
}
