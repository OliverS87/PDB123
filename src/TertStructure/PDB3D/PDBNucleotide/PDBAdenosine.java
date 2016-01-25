package TertStructure.PDB3D.PDBNucleotide;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.PDB3D.PDBBackbone.PDBBackbone;
import TertStructure.PDB3D.PDBNucleobases.PDBAdenine;
import TertStructure.PDB3D.PDBSugar.PDBRibose;
import TertStructure.RNAMesh3D.DrawLine;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by oliver on 15.12.15.
 * PDB123StartUp class for representation of PDB nucleotides of Adenosine.
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
public class PDBAdenosine extends PDBNucleotide
{
    private PDBRibose ribo;
    private PDBAdenine ade;
    private PDBBackbone pbb;



    public PDBAdenosine() {
        this.ribo = new PDBRibose();
        this.ade = new PDBAdenine();
        this.pbb = new PDBBackbone();
    }

    public PDBRibose getRibose() {
        return ribo;
    }
    public PDBAdenine getAdenine() {
        return ade;
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

    // Return 3D structure. Add connecting lines between individual parts.
    @Override
    public Group getStructure() {
        Group adenosineGrp = new Group();
        adenosineGrp.getChildren().addAll(this.ribo.getStructure(), this.ade.getStructure(), this.pbb.getStructure());
        // Connect ribose and adenine
        DrawLine c1ToN9 = new DrawLine(ribo.getC1(), ade.getN9());
        // Connect phosphatgroup and ribose
        DrawLine o5ToP = new DrawLine(ribo.getO5(), pbb.getP());
        adenosineGrp.getChildren().addAll(c1ToN9, o5ToP);
        // Add tooltip
        Tooltip.install(adenosineGrp, new Tooltip("Ade_"+this.getResIndex()));
        return adenosineGrp;
    }
    // Color purin ring according to colormode
    // Colormode could be:
    // - Type of residue
    // - Purin or Pyrimidine
    // - Basepaired?
    public void setColorMode(String colorMode)
    {
        switch (colorMode){
            case("resType"): ade.setNucleobaseColor(new PhongMaterial(Color.DARKGREEN)); break;
            case("baseType"): ade.setNucleobaseColor(new PhongMaterial(Color.DARKBLUE)); break;
            case("basePaired"): {
                if (isBasePaired) ade.setNucleobaseColor(new PhongMaterial(Color.BLACK));
                else ade.setNucleobaseColor(new PhongMaterial(Color.WHITE));
                break;
            }
        }
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
            case("N1"): ade.setN1(xyz); break;
            case("N3"): ade.setN3(xyz); break;
            case("N6"): ade.setN6(xyz); break;
            case("N7"): ade.setN7(xyz); break;
            case("N9"): ade.setN9(xyz); break;
            case("C2"): ade.setC2(xyz); break;
            case("C4"): ade.setC4(xyz); break;
            case("C5"): ade.setC5(xyz); break;
            case("C6"): ade.setC6(xyz); break;
            case("C8"): ade.setC8(xyz); break;
            case("O2"): ade.setO2(xyz); break;
            case("H8"): ade.setH8(xyz); break;
            case("H2"): ade.setH2(xyz); break;
            case("H61"): ade.setH61(xyz); break;
            case("H62"): ade.setH62(xyz); break;

        }

    }

    @Override
    public String getType() {
        return "Adenosine";
    }
}
