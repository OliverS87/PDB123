package TertStructure.PDB3D.PDBNucleotide;

import GUI.LogMessagesUI.PDB123PrintLog;
import GUI.SettingsUI.PDB123SettingsPresenter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.PDB3D.PDBBackbone.PDBBackbone;
import TertStructure.PDB3D.PDBNucleobases.PDBAdenine;
import TertStructure.PDB3D.PDBSugar.PDBRibose;
import TertStructure.RNA3DComponents.DrawLine;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by oliver on 15.12.15.
 * PDBAdenosine represents one Adenosine nucleotide. Inherits
 * common interface of all nucleotides from PDBNucleotide.
 * Extends Group. Elements of groups are only created after
 * getStructure() is called.
 */
public class PDBAdenosine extends PDBNucleotide
{
    // Adensosine consists of a
    // - sugar: ribo
    // - nucleobase: ade
    // - a phosphat backbone: pbb
    private PDBRibose ribo;
    private PDBAdenine ade;
    private PDBBackbone pbb;
    // PDBAdenosine has two Color properties, one for unselected and selected state.
    private ObjectProperty<Color> unselected, selected;

    public PDBAdenosine(PDB123PrintLog log,PDB123SettingsPresenter settings) {
        super(log, settings);
        this.ribo = new PDBRibose();
        this.ade = new PDBAdenine();
        this.pbb = new PDBBackbone();
        // Keep track of atoms with undefined coordinates
        defAtoms = new ArrayList<>(Collections.nCopies(26, false));
        // Add color mode listener
        addColorModeListener();
        // Set initial color mode
        setColorMode(settings.colorModeProperty().getValue());

    }

    // Get substructures
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

    // Init. 3D structure. Add connecting lines between individual parts.
    // Visibility of components can be activated/deactivated by user
    @Override
    public void getStructure(BooleanProperty showBackbone, BooleanProperty showSugar, BooleanProperty showBase) {
        if (!this.allAtomsDefined()) printLog.printLogMessage("WARNING: Adenosine "+this.getResIndex()+" has undefined atoms.");
        Group adenosineGrp = new Group();
        PhongMaterial adeMaterial = new PhongMaterial();
        adeMaterial.diffuseColorProperty().bind(this.ntColorProperty());
        // Build structure for ribose, nucleobase and phosphat backbone
        // Bind visibility of components to showBackbone/showSugar/showBase
        Group adenosineRibose = this.ribo.getStructure(adeMaterial);
        adenosineRibose.visibleProperty().bind(showSugar);
        Group adenosineNucleoBase = this.ade.getStructure();
        ade.setNucleobaseColor(adeMaterial);
        adenosineNucleoBase.visibleProperty().bind(showBase);
        Group adenosinePPB = this.pbb.getStructure();
        adenosinePPB.visibleProperty().bind(showBackbone);
        adenosineGrp.getChildren().addAll(adenosineRibose, adenosineNucleoBase, adenosinePPB);
        // Connect ribose and adenine
        DrawLine c1ToN9 = new DrawLine(ribo.getC1(), ade.getN9());
        c1ToN9.visibleProperty().bind(showSugar.and(showBase));
        // Connect ribose and phosphat
        // Check if phosphat is present (might not be available in first residue of chain)
        if (pbb.getP() == null)
        {
            adenosineGrp.getChildren().addAll(c1ToN9);
        }
        else {
            // Connect phosphatgroup and ribose
            DrawLine o5ToP = new DrawLine(ribo.getO5(), pbb.getP());
            o5ToP.visibleProperty().bind(showBackbone.and(showSugar));
            adenosineGrp.getChildren().addAll(c1ToN9, o5ToP);
        }
        // Add tooltip
        Tooltip.install(this, new Tooltip("Adenosine "+this.getResIndex()));
        this.getChildren().add(adenosineGrp);
    }


    private void addColorModeListener()
    {
        StringProperty colorModeProperty = this.getSettings().colorModeProperty();
        colorModeProperty.addListener((observable, oldValue, newValue) -> setColorMode(newValue));

    }
    // Color purin ring according to colormode
    // Colormode could be:
    // - Type of residue
    // - Purin or Pyrimidine
    private void setColorMode(String colorMode)
    {
            switch (colorMode){
                case("resType"):{
                    this.unselected = this.getSettings().adeUnselectedColorProperty();
                    this.selected = this.getSettings().adeSelectedColorProperty();
                } break;
                case("baseType"):{
                    this.unselected = this.getSettings().purUnselectedColorProperty();
                    this.selected = this.getSettings().purSelectedColorProperty();
                } break;
            }
            // Bind color to selection state
            this.ntColorProperty().bind(Bindings.when(isSelectedProperty()).then(selected).otherwise(unselected));
    }

    // SetAtom converts the PDB raw input into accesible Java Point3D coordinates
    // String[] atom has this format: [[atomID][X][Y][Z]]
    // If atomID is unknown, input will be ignored
    @Override
    public void setAtom(String[] atom)
    {
        double x = Double.parseDouble(atom[1]);
        double y = Double.parseDouble(atom[2]);
        double z = Double.parseDouble(atom[3]);
        Point3D xyz = new Point3D(x,y,z);
        switch (atom[0])
        {
            case("P"): {pbb.setP(xyz);defAtoms.set(0, true); break;}
            case("OP1"): {pbb.setOP1(xyz);defAtoms.set(1, true); break;}
            case("OP2"): {pbb.setOP2(xyz);defAtoms.set(2, true); break;}
            case("C1'"): {ribo.setC1(xyz);defAtoms.set(3, true); break;}
            case("C2'"): {ribo.setC2(xyz);defAtoms.set(4, true); break;}
            case("C3'"): {ribo.setC3(xyz);defAtoms.set(5, true); break;}
            case("C4'"): {ribo.setC4(xyz);defAtoms.set(6, true); break;}
            case("C5'"): {ribo.setC5(xyz);defAtoms.set(7, true); break;}
            case("O2'"): {ribo.setO2(xyz);defAtoms.set(8, true); break;}
            case("O3'"): {ribo.setO3(xyz);defAtoms.set(9, true); break;}
            case("O4'"): {ribo.setO4(xyz);defAtoms.set(10, true); break;}
            case("O5'"): {ribo.setO5(xyz);defAtoms.set(11, true); break;}
            case("N1"): {ade.setN1(xyz); defAtoms.set(12, true); break;}
            case("N3"): {ade.setN3(xyz); defAtoms.set(13, true); break;}
            case("N6"): {ade.setN6(xyz); defAtoms.set(14, true); break;}
            case("N7"): {ade.setN7(xyz); defAtoms.set(15, true); break;}
            case("N9"): {ade.setN9(xyz); defAtoms.set(16, true); break;}
            case("C2"): {ade.setC2(xyz); defAtoms.set(17, true); break;}
            case("C4"): {ade.setC4(xyz); defAtoms.set(18, true); break;}
            case("C5"): {ade.setC5(xyz); defAtoms.set(19, true); break;}
            case("C6"): {ade.setC6(xyz); defAtoms.set(20, true); break;}
            case("C8"): {ade.setC8(xyz); defAtoms.set(21, true); break;}
            case("H8"): {ade.setH8(xyz); defAtoms.set(22, true); break;}
            case("H2"): {ade.setH2(xyz); defAtoms.set(23, true); break;}
            case("H61"): {ade.setH61(xyz); defAtoms.set(24, true); break;}
            case("H62"): {ade.setH62(xyz); defAtoms.set(25, true); break;}
        }
    }

    @Override
    public String getType() {
        return "A";
    }

    @Override
    // Return a central coordinate
    // Needed to estimate the distance to other nucleotides
    public Point3D getCentralCoordinate() {
        return ade.getC6().midpoint(ade.getN3());
    }
}
