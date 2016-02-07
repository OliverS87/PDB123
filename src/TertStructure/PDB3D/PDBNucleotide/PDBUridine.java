package TertStructure.PDB3D.PDBNucleotide;

import GUI.PDB123PrintLog;
import GUI.PDB123SettingsPresenter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.PDB3D.PDBBackbone.PDBBackbone;
import TertStructure.PDB3D.PDBNucleobases.PDBUracil;
import TertStructure.PDB3D.PDBSugar.PDBRibose;
import TertStructure.RNA3DComponents.DrawLine;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by oliver on 15.12.15.
 * PDB123StartUp class for representation of PDB nucleotides of Uridine.
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
 * 3D structure is generated with TertStructure.RNA3DComponents package.
 */
public class PDBUridine extends PDBNucleotide
{
    private PDBRibose ribo;
    private PDBUracil uri;
    private PDBBackbone pbb;
    private ObjectProperty<Color> unselected, selected;


    public PDBUridine(PDB123PrintLog log, PDB123SettingsPresenter settings) {
        super(log, settings);
        this.ribo = new PDBRibose();
        this.uri = new PDBUracil();
        this.pbb = new PDBBackbone();

        defAtoms = new ArrayList<>(Collections.nCopies(23, false));
        // Add listener
        addListener();
        // Set initial color mode
        setColorMode(settings.colorModeProperty().getValue());
    }

    public PDBRibose getRibose() {
        return ribo;
    }

    public PDBUracil getUracil() {
        return uri;
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
            case("P"): pbb.setP(xyz); {defAtoms.set(0,true);break;}
            case("OP1"): pbb.setOP1(xyz); {defAtoms.set(1,true);break;}
            case("OP2"): pbb.setOP2(xyz); {defAtoms.set(2,true);break;}
            case("C1'"): ribo.setC1(xyz); {defAtoms.set(3,true);break;}
            case("C2'"): ribo.setC2(xyz); {defAtoms.set(4,true);break;}
            case("C3'"): ribo.setC3(xyz); {defAtoms.set(5,true);break;}
            case("C4'"): ribo.setC4(xyz); {defAtoms.set(6,true);break;}
            case("C5'"): ribo.setC5(xyz); {defAtoms.set(7,true);break;}
            case("O2'"): ribo.setO2(xyz); {defAtoms.set(8,true);break;}
            case("O3'"): ribo.setO3(xyz); {defAtoms.set(9,true);break;}
            case("O4'"): ribo.setO4(xyz); {defAtoms.set(10,true);break;}
            case("O5'"): ribo.setO5(xyz); {defAtoms.set(11,true);break;}
            case("C2"): uri.setC2(xyz);  {defAtoms.set(12,true);break;}
            case("C4"): uri.setC4(xyz);  {defAtoms.set(13,true);break;}
            case("C5"): uri.setC5(xyz);  {defAtoms.set(14,true);break;}
            case("C6"): uri.setC6(xyz);  {defAtoms.set(15,true);break;}
            case("N1"): uri.setN1(xyz);  {defAtoms.set(16,true);break;}
            case("N3"): uri.setN3(xyz);  {defAtoms.set(17,true);break;}
            case("O4"): uri.setO4(xyz);  {defAtoms.set(18,true);break;}
            case("O2"): uri.setO2(xyz);  {defAtoms.set(19,true);break;}
            case("H3"): uri.setH3(xyz);  {defAtoms.set(20,true);break;}
            case("H5"): uri.setH5(xyz);  {defAtoms.set(21,true);break;}
            case("H6"): uri.setH6(xyz);  {defAtoms.set(22,true);break;}

        }

    }




    // Return 3D structure. Add connecting lines between individual parts.
    @Override
    public void getStructure(BooleanProperty showBackbone, BooleanProperty showSugar, BooleanProperty showBase) {
        if (!this.allAtomsDefined()) printLog.printLogMessage("WARNING: Uri_"+this.getResIndex()+" not completely defined.");
        Group uridineGrp = new Group();
        PhongMaterial uriMaterial = new PhongMaterial();
        Group uridineSugar = this.ribo.getStructure(uriMaterial);
        uridineSugar.visibleProperty().bind(showSugar);
        uriMaterial.diffuseColorProperty().bind(this.ntColorProperty());
        Group uridineNucleobase =this.uri.getStructure();
        uri.setNucleobaseColor(uriMaterial);
        uridineNucleobase.visibleProperty().bind(showBase);
        Group uridinePBB = this.pbb.getStructure();
        uridinePBB.visibleProperty().bind(showBackbone);
        uridineGrp.getChildren().addAll(
                uridineSugar,
                uridineNucleobase,
                uridinePBB
                );
        // Connect ribose and uracil
        DrawLine c1toN1 = new DrawLine(ribo.getC1(), uri.getN1());
        c1toN1.visibleProperty().bind(showSugar.and(showBase));
        // Connect ribose and phosphat
        // Check if phosphat is present (might not be available in first residue of chain)
        if (pbb.getP() == null)
        {
            uridineGrp.getChildren().addAll(c1toN1);
        }
        else
        {
            // Connect ribose and phosphate
            DrawLine o5ToP = new DrawLine(ribo.getO5(), pbb.getP());
            o5ToP.visibleProperty().bind(showSugar.and(showBackbone));
            uridineGrp.getChildren().addAll(c1toN1, o5ToP);
        }
        // Add tooltip
        Tooltip.install(this, new Tooltip("Uri_"+this.getResIndex()));
        this.getChildren().add(uridineGrp);
    }

    // Color purin ring according to colormode
    // Colormode could be:
    // - Type of residue
    // - Purin or Pyrimidine
    public void setColorMode(String colorMode)
    {
        switch (colorMode){
            case("resType"):{
                this.unselected = this.getSettings().uraUnselectedColorProperty();
                this.selected = this.getSettings().uraSelectedColorProperty();

            } break;
            case("baseType"):{
                this.unselected = this.getSettings().pyrUnselectedColorProperty();
                this.selected = this.getSettings().pyrSelectedColorProperty();
            } break;

        }
        // Bind color to selection state

        this.ntColorProperty().bind(Bindings.when(isSelectedProperty()).then(selected).otherwise(unselected));
    }

    private void addListener()
    {
        StringProperty colorModeProperty = this.getSettings().colorModeProperty();
        colorModeProperty.addListener((observable, oldValue, newValue) -> setColorMode(newValue));

    }
    @Override
    public String getType() {
        return "U";
    }
    @Override
    // Return a central coordinate
    // Needed to estimate the distance to other nucleotides
    public Point3D getCentralCoordinate() {
        return uri.getN1().midpoint(uri.getC4());
    }
}
