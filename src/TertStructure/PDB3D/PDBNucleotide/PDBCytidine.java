package TertStructure.PDB3D.PDBNucleotide;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.PDB3D.PDBBackbone.PDBBackbone;
import TertStructure.PDB3D.PDBNucleobases.PDBCytosine;
import TertStructure.PDB3D.PDBSugar.PDBRibose;
import TertStructure.RNAMesh3D.DrawLine;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by oliver on 15.12.15.
 * PDB123StartUp class for representation of PDB nucleotides of Cytidine.
 * Encapsulates three PDB representations:
 *  - Ribose
 *  - Nucleoside
 *  - Backbone
 * Class is called by PDBreader. Ribose, Nucleoside and Backbone classes
 * are generated automatically.
 * Returns a collective 3D Structure group containing Ribose, Nucleoside and Backbone.
 * In addition, lines connecting residues and additional atoms can be added.
 * Allows direct access to 5' and 3' end to allow connection of residues in final
 * 3D structure.
 * 3D structure is generated with TertStructure.RNAMesh3D package.
 */
public class PDBCytidine extends PDBNucleotide
{
    private PDBRibose ribo;
    private PDBCytosine cyt;
    private PDBBackbone pbb;

    public PDBCytidine() {
        this.ribo = new PDBRibose();
        this.cyt = new PDBCytosine();
        this.pbb = new PDBBackbone();
    }

    public PDBRibose getRibose() {
        return ribo;
    }

    public PDBCytosine getCytosine() {
        return cyt;
    }

    public PDBBackbone getPbb() {
        return pbb;
    }

    // Return 3D structure. Add connecting lines between individual parts.
    @Override
    public Group getStructure() {
        Group cytidineGrp = new Group();
        cytidineGrp.getChildren().addAll(
                this.ribo.getStructure(),
                this.cyt.getStructure(),
                this.pbb.getStructure());
        // Connect ribose and cytosine
        DrawLine c1toN1 = new DrawLine(ribo.getC1(), cyt.getN1());
        // Connect ribose and phosphat
        DrawLine o5ToP = new DrawLine(ribo.getO5(), pbb.getP());
        cytidineGrp.getChildren().addAll(c1toN1.getStructure(), o5ToP.getStructure());
        // Add tooltip
        Tooltip.install(cytidineGrp, new Tooltip("Cyt_"+this.getResIndex()));
        return cytidineGrp;
    }

    // Color pyrimidine ring according to colormode
    // Colormode could be:
    // - Type of residue
    // - Purin or Pyrimidine
    // - Basepaired?
    public void setColorMode(String colorMode)
    {
        switch (colorMode){
            case("resType"): cyt.setNucleobaseColor(new PhongMaterial(Color.YELLOW)); break;
            case("baseType"): cyt.setNucleobaseColor(new PhongMaterial(Color.DARKRED)); break;
            case("basePaired"): {
                if (isBasePaired) cyt.setNucleobaseColor(new PhongMaterial(Color.BLACK));
                else cyt.setNucleobaseColor(new PhongMaterial(Color.WHITE));
                break;
            }
        }
    }


    // Get coordinates of 5' and 3' end
    @Override
    public Point3D getFivePrimeEnd() {
        return this.pbb.getP();
    }

    @Override
    public Point3D getThreePrimeEnd() {
        return this.ribo.getO3();
    }

    // SetAtom converts the PDB raw input into accesible Java Points3D coordinates
    @Override
    public void setAtom(String[] atom)
    {
        double x = Double.parseDouble(atom[1]);
        double y = Double.parseDouble(atom[2]);
        double z = Double.parseDouble(atom[3]);
        Point3D xyz = new Point3D(x,y,z);
        switch (atom[0])
        {
            case("P"): pbb.setP(xyz); break;
            case("OP1"): pbb.setOP1(xyz); break;
            case("OP2"): pbb.setOP2(xyz); break;
            case("C1'"): ribo.setC1(xyz);break;
            case("C2'"): ribo.setC2(xyz);break;
            case("C3'"): ribo.setC3(xyz);break;
            case("C4'"): ribo.setC4(xyz);break;
            case("C5'"): ribo.setC5(xyz);break;
            case("O2'"): ribo.setO2(xyz);break;
            case("O3'"): ribo.setO3(xyz);break;
            case("O4'"): ribo.setO4(xyz);break;
            case("O5'"): ribo.setO5(xyz);break;
            case("C2"): cyt.setC2(xyz); break;
            case("C4"): cyt.setC4(xyz); break;
            case("C5"): cyt.setC5(xyz); break;
            case("C6"): cyt.setC6(xyz); break;
            case("N1"): cyt.setN1(xyz); break;
            case("N3"): cyt.setN3(xyz); break;
            case("N4"): cyt.setN4(xyz); break;
            case("O2"): cyt.setO2(xyz); break;

        }

    }

    @Override
    public String getType() {
        return "Cytidine";
    }
}
