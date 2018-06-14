package TertStructure.PDB3D.PDBNucleotide;

import GUI.LogMessagesUI.PDB123PrintLog;
import GUI.SettingsUI.PDB123SettingsPresenter;
import SecStructure.RNA2D.Rna2DNode;
import SelectionModel.PDB123SelectionModel;
import SelectionModel.Selectable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by oliver on 15.12.15.
 * Abstract class provides functions common to all
 * four derived nucleotide classes (PDBAdenosine, PDBCytidine,
 * PDBGuanosine and PDBUridine). Provides a common interface
 * to access attributes of all four classes without knowledge
 * of the actual type of residue.
 * Common interfaces are:
 * - Set atom coordinates
 * - Set and get residue index
 * - Get nucleotide type (A,C,G or U)
 * - Get 3D structure
 * - Get Nucleotides 5' and 3' end
 * - Get central coordinate
 * - Get or set Nucleotide is base-paired?
 * - Get or set base-pairing partner
 * - Are all atom coordinates defined?
 * - Get isSelected boolean property
 * - Get or set 2D representation of this nucleotide
 * - Get Color property
 * - Set or get selection state
 *



 */
public abstract class PDBNucleotide extends Group implements Selectable{

    private ObjectProperty<Color> ntColor = new SimpleObjectProperty<>();
    private BooleanProperty isSelected = new SimpleBooleanProperty(false);
    private Rna2DNode rna2DNode;
    private int resIndex;
    private PDBNucleotide basePairedTo;
    protected boolean isBasePaired = false;
    // Count number of defined atoms
    ArrayList<Boolean> defAtoms;
    private PDB123SettingsPresenter settings;
    PDB123PrintLog printLog;


    // Constructor
    // Connect instance PDBNucleotide to log output on main UI
    // and to settings made in window settings
    // Register instance at PDB123SelectionModel and add mouse handler
    public PDBNucleotide(PDB123PrintLog log, PDB123SettingsPresenter settings)
    {
        this.printLog = log;
        this.settings=settings;
        PDB123SelectionModel.registerSelectable(this);
        PDB123SelectionModel.addMouseHandler(this);
    }

    // Set atom coordinates
    public abstract void setAtom(String[] atom);
    // Set/get the position index of the nucleotide
    public void setResIndex(int resIndex) {
        this.resIndex=resIndex;
    }
    public int getResIndex(){
        return this.resIndex;
    }
    // Get nucleotide type
    public abstract String getType();
    // Init. 3D structure
    public abstract void getStructure(BooleanProperty showBackbone, BooleanProperty showSugar, BooleanProperty showNucleoBase);
    // Return 5' and 3' end or central coordinate
    public abstract Point3D getFivePrimeEnd();
    public abstract Point3D getThreePrimeEnd();
    public abstract Point3D getCentralCoordinate();
    // Set or get selection or base-pairing status
    public boolean isBasePaired(){
        return isBasePaired;
    }
    public void setBasePaired(boolean basePaired){
        this.isBasePaired = basePaired;
    }
    public void setBasePairedTo(PDBNucleotide nt){
    this.basePairedTo=nt;
    }
    public PDBNucleotide getBasePairedTo(){
        return basePairedTo;
    }
    public BooleanProperty isSelectedProperty() {return isSelected;}
    // Are the coordinates of all atoms set?
    public  boolean allAtomsDefined(){
        return !defAtoms.contains(false);
    };
    // Get or set 2D representation of this 3D nucleotide
    public Rna2DNode getRna2DNode(){return  rna2DNode;}
    public void setRna2DNode(Rna2DNode rna2DNode) {
        this.rna2DNode = rna2DNode;
    }
    // Get the color property of this nucleotide
    public ObjectProperty<Color> ntColorProperty() {
        return ntColor;
    }
    // Provide access to settings from inherited classes
    protected PDB123SettingsPresenter getSettings() {
        return settings;
    }
    // Implement functions for MouseSelectionModel
    @Override
    public boolean isSelected() {
        return isSelected.getValue();
    }

    @Override
    public void setSelected(boolean sel) {
    isSelected.setValue(sel);
    }




}
