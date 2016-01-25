package TertStructure.PDB3D.PDBNucleotide;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.PDB3D.PDBBackbone.PDBBackbone;
import TertStructure.PDB3D.PDBNucleobases.PDBGuanine;
import TertStructure.PDB3D.PDBSugar.PDBRibose;
import TertStructure.RNAMesh3D.DrawLine;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by oliver on 15.12.15.
 * PDB123StartUp class for representation of PDB nucleotides of Guanosine.
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
 *  3D structure is generated with TertStructure.RNAMesh3D package.
 */
public class PDBGuanosine extends PDBNucleotide
{
    private PDBRibose ribo;
    private PDBGuanine gua;
    private PDBBackbone pbb;

    public PDBGuanosine() {
        this.ribo = new PDBRibose();
        this.gua = new PDBGuanine();
        this.pbb = new PDBBackbone();
    }

    public PDBRibose getRibose() {
        return ribo;
    }

    public PDBGuanine getGuanine() {
        return gua;
    }

    public PDBBackbone getPbb() {
        return pbb;
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
            case("N1"): gua.setN1(xyz); break;
            case("N3"): gua.setN3(xyz); break;
            case("N7"): gua.setN7(xyz); break;
            case("N9"): gua.setN9(xyz); break;
            case("C2"): gua.setC2(xyz); break;
            case("C4"): gua.setC4(xyz); break;
            case("C5"): gua.setC5(xyz); break;
            case("C6"): gua.setC6(xyz); break;
            case("C8"): gua.setC8(xyz); break;
            case("O2"): gua.setO2(xyz); break;
            case("H8"): gua.setH8(xyz); break;
            case("H1"): gua.setH1(xyz); break;
            case("H21"): gua.setH22(xyz); break;
            case("H22"): gua.setH21(xyz); break;
            case("O6"): gua.setO6(xyz); break;
            case("N2"): gua.setN2(xyz); break;

        }

    }

    // Return 3D structure. Add connecting lines between individual parts.
    @Override
    public Group getStructure() {
        Group guanosineGrp = new Group();
        guanosineGrp.getChildren().addAll(this.ribo.getStructure(), this.gua.getStructure(), this.pbb.getStructure());
        // Connect ribose and guanine
        DrawLine c1ToN9 = new DrawLine(ribo.getC1(), gua.getN9());
        // Connect ribose and phosphat
        // Check if phosphat is present (not available in first residue of chain)
        if (pbb.getP() == null)
        {
            guanosineGrp.getChildren().addAll(c1ToN9);
            return guanosineGrp;
        }
        DrawLine o5ToP = new DrawLine(ribo.getO5(), pbb.getP());
        guanosineGrp.getChildren().addAll(c1ToN9, o5ToP);
        // Add tooltip
        Tooltip.install(guanosineGrp, new Tooltip("Gua_"+this.getResIndex()));
        return guanosineGrp;
    }

    // Color purin ring according to colormode
    // Colormode could be:
    // - Type of residue
    // - Purin or Pyrimidine
    // - Future: Basepaired?
    public void setColorMode(String colorMode)
    {
        switch (colorMode){
            case("resType"): gua.setNucleobaseColor(new PhongMaterial(Color.CYAN)); break;
            case("baseType"): gua.setNucleobaseColor(new PhongMaterial(Color.DARKBLUE)); break;
            case("basePaired"): {
                if (isBasePaired) gua.setNucleobaseColor(new PhongMaterial(Color.BLACK));
                else gua.setNucleobaseColor(new PhongMaterial(Color.WHITE));
                break;
            }
        }
    }

    @Override
    public String getType() {
        return "Guanosine";
    }
}
