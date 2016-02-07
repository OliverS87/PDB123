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
import TertStructure.PDB3D.PDBNucleobases.PDBCytosine;
import TertStructure.PDB3D.PDBSugar.PDBRibose;
import TertStructure.RNA3DComponents.DrawLine;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by oliver on 15.12.15.
 * PDBCytidine represents one Cytidine nucleotide. Inherits
 * common interface of all nucleotides from PDBNucleotide.
 * Extends Group. Elements of groups are only created after
 * getStructure() is called.
 */
public class PDBCytidine extends PDBNucleotide {
    // Cytidine consists of a
    // - sugar: ribo
    // - nucleobase: cyt
    // - a phosphat backbone: pbb
    private PDBRibose ribo;
    private PDBCytosine cyt;
    private PDBBackbone pbb;
    // PDBCytidine has two Color properties, one for unselected and selected state.
    private ObjectProperty<Color> unselected, selected;

    public PDBCytidine(PDB123PrintLog log, PDB123SettingsPresenter settings) {
        super(log, settings);
        this.ribo = new PDBRibose();
        this.cyt = new PDBCytosine();
        this.pbb = new PDBBackbone();
        // Keep track of atoms with undefined coordinates
        defAtoms = new ArrayList<>(Collections.nCopies(24, false));
        // Add color mode listener
        addColorModeListener();
        // Set initial color mode
        setColorMode(settings.colorModeProperty().getValue());
    }

    // Get substructures
    public PDBRibose getRibose() {
        return ribo;
    }
    public PDBCytosine getCytosine() {
        return cyt;
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
        if (!this.allAtomsDefined())
            printLog.printLogMessage("WARNING: Cytosine " + this.getResIndex() + " has undefined atoms.");
        Group cytidineGrp = new Group();
        PhongMaterial cytMaterial = new PhongMaterial();
        // Build structure for ribose, nucleobase and phosphat backbone
        // Bind visibility of components to showBackbone/showSugar/showBase
        Group cytidineRibose = this.ribo.getStructure(cytMaterial);
        cytidineRibose.visibleProperty().bind(showSugar);
        cytMaterial.diffuseColorProperty().bind(this.ntColorProperty());
        Group cytidineNucleobase = this.cyt.getStructure();
        cyt.setNucleobaseColor(cytMaterial);
        cytidineNucleobase.visibleProperty().bind(showBase);
        Group cytidinePBB = this.pbb.getStructure();
        cytidinePBB.visibleProperty().bind(showBackbone);
        cytidineGrp.getChildren().addAll(
                cytidineRibose,
                cytidineNucleobase,
                cytidinePBB);
        // Connect ribose and cytosine
        DrawLine c1toN1 = new DrawLine(ribo.getC1(), cyt.getN1());
        c1toN1.visibleProperty().bind(showSugar.and(showBase));
        // Connect ribose and phosphat
        // Check if phosphat is present (might not be available in first residue of chain)
        if (pbb.getP() == null) {
            cytidineGrp.getChildren().addAll(c1toN1);
        } else {
            // Connect ribose and phosphat
            DrawLine o5ToP = new DrawLine(ribo.getO5(), pbb.getP());
            o5ToP.visibleProperty().bind(showSugar.and(showBackbone));
            cytidineGrp.getChildren().addAll(c1toN1, o5ToP);
        }
        Tooltip.install(this, new Tooltip("Cytosine " + this.getResIndex()));
        this.getChildren().add(cytidineGrp);
    }

    // Color purin ring according to colormode
    // Colormode could be:
    // - Type of residue
    // - Purin or Pyrimidine
    private void setColorMode(String colorMode) {
        switch (colorMode) {
            case ("resType"): {
                this.unselected = this.getSettings().cytUnselectedColorProperty();
                this.selected = this.getSettings().cytSelectedColorProperty();

            }
            break;
            case ("baseType"): {
                this.unselected = this.getSettings().pyrUnselectedColorProperty();
                this.selected = this.getSettings().pyrSelectedColorProperty();
            }
            break;

        }
        // Bind color to selection state
        this.ntColorProperty().bind(Bindings.when(isSelectedProperty()).then(selected).otherwise(unselected));
    }

    private void addColorModeListener() {
        StringProperty colorModeProperty = this.getSettings().colorModeProperty();
        colorModeProperty.addListener((observable, oldValue, newValue) -> setColorMode(newValue));

    }


    // SetAtom converts the PDB raw input into accesible Java Point3D coordinates
    // String[] atom has this format: [[atomID][X][Y][Z]]
    // If atomID is unknown, input will be ignored
    @Override
    public void setAtom(String[] atom) {
        double x = Double.parseDouble(atom[1]);
        double y = Double.parseDouble(atom[2]);
        double z = Double.parseDouble(atom[3]);
        Point3D xyz = new Point3D(x, y, z);
        switch (atom[0]) {
            case ("P"): {
                pbb.setP(xyz);
                defAtoms.set(0, true);
                break;
            }
            case ("OP1"):
                pbb.setOP1(xyz);
            {
                defAtoms.set(1, true);
                break;
            }
            case ("OP2"):
                pbb.setOP2(xyz);
            {
                defAtoms.set(2, true);
                break;
            }
            case ("C1'"):
                ribo.setC1(xyz);
            {
                defAtoms.set(3, true);
                break;
            }
            case ("C2'"):
                ribo.setC2(xyz);
            {
                defAtoms.set(4, true);
                break;
            }
            case ("C3'"):
                ribo.setC3(xyz);
            {
                defAtoms.set(5, true);
                break;
            }
            case ("C4'"):
                ribo.setC4(xyz);
            {
                defAtoms.set(6, true);
                break;
            }
            case ("C5'"):
                ribo.setC5(xyz);
            {
                defAtoms.set(7, true);
                break;
            }
            case ("O2'"):
                ribo.setO2(xyz);
            {
                defAtoms.set(8, true);
                break;
            }
            case ("O3'"):
                ribo.setO3(xyz);
            {
                defAtoms.set(9, true);
                break;
            }
            case ("O4'"):
                ribo.setO4(xyz);
            {
                defAtoms.set(10, true);
                break;
            }
            case ("O5'"):
                ribo.setO5(xyz);
            {
                defAtoms.set(11, true);
                break;
            }
            case ("C2"):
                cyt.setC2(xyz);
            {
                defAtoms.set(12, true);
                break;
            }
            case ("C4"):
                cyt.setC4(xyz);
            {
                defAtoms.set(13, true);
                break;
            }
            case ("C5"):
                cyt.setC5(xyz);
            {
                defAtoms.set(14, true);
                break;
            }
            case ("C6"):
                cyt.setC6(xyz);
            {
                defAtoms.set(15, true);
                break;
            }
            case ("N1"):
                cyt.setN1(xyz);
            {
                defAtoms.set(16, true);
                break;
            }
            case ("N3"):
                cyt.setN3(xyz);
            {
                defAtoms.set(17, true);
                break;
            }
            case ("N4"):
                cyt.setN4(xyz);
            {
                defAtoms.set(18, true);
                break;
            }
            case ("O2"):
                cyt.setO2(xyz);
            {
                defAtoms.set(19, true);
                break;
            }
            case ("H41"):
                cyt.setH41(xyz);
            {
                defAtoms.set(20, true);
                break;
            }
            case ("H42"):
                cyt.setH42(xyz);
            {
                defAtoms.set(21, true);
                break;
            }
            case ("H5"):
                cyt.setH5(xyz);
            {
                defAtoms.set(22, true);
                break;
            }
            case ("H6"):
                cyt.setH6(xyz);
            {
                defAtoms.set(23, true);
                break;
            }

        }

    }


    @Override
    public String getType() {
        return "C";
    }

    @Override
    // Return a central coordinate
    // Needed to estimate the distance to other nucleotides
    public Point3D getCentralCoordinate() {
        return cyt.getN1().midpoint(cyt.getC4());
    }
}
