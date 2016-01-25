package TertStructure.PDB3D.PDBNucleobases;

import TertStructure.RNAMesh3D.DrawHydrogen;
import TertStructure.RNAMesh3D.DrawLine;
import TertStructure.RNAMesh3D.DrawNitrogen;
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

    private Point3D N6, O2, H2, H61, H62;

    public Point3D getN6() {
        return N6;
    }

    public void setN6(Point3D n6) {
        N6 = n6;
    }

    public void setH2(Point3D h2) {
        H2 = h2;
    }

    public void setH61(Point3D h61) {
        H61 = h61;
    }

    public void setH62(Point3D h62) {
        H62 = h62;
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
        // Draw hydrogens and functional groups
        DrawHydrogen hydrogen2 = new DrawHydrogen(H2);
        DrawHydrogen hydrogen61 = new DrawHydrogen(H61);
        DrawHydrogen hydrogen62 = new DrawHydrogen(H62);
        DrawHydrogen hydrogen8 = new DrawHydrogen(this.getH8());
        DrawNitrogen nitrogen6 = new DrawNitrogen(N6);
        // Draw bondings between hydrogens, functional groups and the purin ring
        DrawLine h2purin = new DrawLine(H2, this.getC2());
        DrawLine h8purin = new DrawLine(this.getH8(), this.getC8());
        DrawLine n6purin = new DrawLine(N6, this.getC6());
        DrawLine h61n6 = new DrawLine(H61, N6);
        DrawLine h62n6 = new DrawLine(H62, N6);


        adenineGGrp.getChildren().addAll(meshV, hydrogen2, hydrogen61, hydrogen62, hydrogen8, nitrogen6,
        h2purin, h8purin,n6purin,h61n6,h62n6);
        return adenineGGrp;
    }
}
