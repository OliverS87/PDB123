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
import TertStructure.PDB3D.PDBNucleobases.PDBGuanine;
import TertStructure.PDB3D.PDBSugar.PDBRibose;
import TertStructure.RNA3DComponents.DrawLine;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by oliver on 15.12.15.
 * PDBGuanosine represents one Guanosine nucleotide. Inherits
 * common interface of all nucleotides from PDBNucleotide.
 * Extends Group. Elements of groups are only created after
 * getStructure() is called.
 */
public class PDBGuanosine extends PDBNucleotide {
    // Guanosine consists of a
    // - sugar: ribo
    // - nucleobase: gua
    // - a phosphat backbone: pbb
    private PDBRibose ribo;
    private PDBGuanine gua;
    private PDBBackbone pbb;
    // PDBGuanosine has two Color properties, one for unselected and selected state.
    private ObjectProperty<Color> unselected, selected;


    public PDBGuanosine(PDB123PrintLog log, PDB123SettingsPresenter settings) {
        super(log, settings);
        this.ribo = new PDBRibose();
        this.gua = new PDBGuanine();
        this.pbb = new PDBBackbone();
        // Keep track of atoms with undefined coordinates
        defAtoms = new ArrayList<>(Collections.nCopies(27, false));
        // Add color mode listener
        addColorModeListener();
        // Set initial color mode
        setColorMode(settings.colorModeProperty().getValue());
    }

    // Get substructures
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


    // Init. 3D structure. Add connecting lines between individual parts.
    // Visibility of components can be activated/deactivated by user
    @Override
    public void getStructure(BooleanProperty showBackbone, BooleanProperty showSugar, BooleanProperty showBase) {
        if (!this.allAtomsDefined())
            printLog.printLogMessage("WARNING: Guanosine " + this.getResIndex() + " has undefined atoms.");
        Group guanosineGrp = new Group();
        // Build structure for ribose, nucleobase and phosphat backbone
        // Bind visibility of components to showBackbone/showSugar/showBase
        PhongMaterial guaMaterial = new PhongMaterial();
        Group guanosineSugar = this.ribo.getStructure(guaMaterial);
        guanosineSugar.visibleProperty().bind(showSugar);
        guaMaterial.diffuseColorProperty().bind(this.ntColorProperty());
        Group guanosineBase = this.gua.getStructure();
        gua.setNucleobaseColor(guaMaterial);
        guanosineBase.visibleProperty().bind(showBase);
        Group guanosinePBB = this.pbb.getStructure();
        guanosinePBB.visibleProperty().bind(showBackbone);
        guanosineGrp.getChildren().addAll(guanosineSugar, guanosineBase, guanosinePBB);
        // Connect ribose and guanine
        DrawLine c1ToN9 = new DrawLine(ribo.getC1(), gua.getN9());
        c1ToN9.visibleProperty().bind(showSugar.and(showBase));
        // Connect ribose and phosphat
        // Check if phosphat is present (might not be available in first residue of chain)
        if (pbb.getP() == null) {
            guanosineGrp.getChildren().addAll(c1ToN9);
        } else {
            DrawLine o5ToP = new DrawLine(ribo.getO5(), pbb.getP());
            o5ToP.visibleProperty().bind(showSugar.and(showBackbone));
            guanosineGrp.getChildren().addAll(c1ToN9, o5ToP);
        }
        // Add tooltip
        Tooltip.install(this, new Tooltip("Guanosine " + this.getResIndex()));
        this.getChildren().add(guanosineGrp);
    }

    // Color purin ring according to colormode
    // Colormode could be:
    // - Type of residue
    // - Purin or Pyrimidine
    public void setColorMode(String colorMode) {
        switch (colorMode) {
            case ("resType"): {
                this.unselected = this.getSettings().guaUnselectedColorProperty();
                this.selected = this.getSettings().guaSelectedColorProperty();

            }
            break;
            case ("baseType"): {
                this.unselected = this.getSettings().purUnselectedColorProperty();
                this.selected = this.getSettings().purSelectedColorProperty();
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
                defAtoms.set(26, true);
                break;
            }
            case ("OP1"): {
                pbb.setOP1(xyz);
                defAtoms.set(0, true);
                break;
            }
            case ("OP2"): {
                pbb.setOP2(xyz);
                defAtoms.set(1, true);
                break;
            }
            case ("C1'"): {
                ribo.setC1(xyz);
                defAtoms.set(2, true);
                break;
            }
            case ("C2'"): {
                ribo.setC2(xyz);
                defAtoms.set(3, true);
                break;
            }
            case ("C3'"): {
                ribo.setC3(xyz);
                defAtoms.set(4, true);
                break;
            }
            case ("C4'"): {
                ribo.setC4(xyz);
                defAtoms.set(5, true);
                break;
            }
            case ("C5'"): {
                ribo.setC5(xyz);
                defAtoms.set(6, true);
                break;
            }
            case ("O2'"): {
                ribo.setO2(xyz);
                defAtoms.set(7, true);
                break;
            }
            case ("O3'"): {
                ribo.setO3(xyz);
                defAtoms.set(8, true);
                break;
            }
            case ("O4'"): {
                ribo.setO4(xyz);
                defAtoms.set(9, true);
                break;
            }
            case ("O5'"): {
                ribo.setO5(xyz);
                defAtoms.set(10, true);
                break;
            }
            case ("N1"): {
                gua.setN1(xyz);
                defAtoms.set(11, true);
                break;
            }
            case ("N3"): {
                gua.setN3(xyz);
                defAtoms.set(12, true);
                break;
            }
            case ("N7"): {
                gua.setN7(xyz);
                defAtoms.set(13, true);
                break;
            }
            case ("N9"): {
                gua.setN9(xyz);
                defAtoms.set(14, true);
                break;
            }
            case ("C2"): {
                gua.setC2(xyz);
                defAtoms.set(15, true);
                break;
            }
            case ("C4"): {
                gua.setC4(xyz);
                defAtoms.set(16, true);
                break;
            }
            case ("C5"): {
                gua.setC5(xyz);
                defAtoms.set(17, true);
                break;
            }
            case ("C6"): {
                gua.setC6(xyz);
                defAtoms.set(18, true);
                break;
            }
            case ("C8"): {
                gua.setC8(xyz);
                defAtoms.set(19, true);
                break;
            }
            case ("H8"): {
                gua.setH8(xyz);
                defAtoms.set(20, true);
                break;
            }
            case ("H1"): {
                gua.setH1(xyz);
                defAtoms.set(21, true);
                break;
            }
            case ("H21"): {
                gua.setH22(xyz);
                defAtoms.set(22, true);
                break;
            }
            case ("H22"): {
                gua.setH21(xyz);
                defAtoms.set(23, true);
                break;
            }
            case ("O6"): {
                gua.setO6(xyz);
                defAtoms.set(24, true);
                break;
            }
            case ("N2"): {
                gua.setN2(xyz);
                defAtoms.set(25, true);
                break;
            }

        }

    }


    @Override
    public String getType() {
        return "G";
    }

    @Override
    // Return a central coordinate
    // Needed to estimate the distance to other nucleotides
    public Point3D getCentralCoordinate() {
        return gua.getC6().midpoint(gua.getN3());
    }
}
