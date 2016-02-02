package TertStructure.PDB3D.PDBNucleotide;

import GUI.PDB123PrintLog;
import SecStructure.RNA2D.Rna2DNode;
import TertStructure.PDB3D.PDBSugar.PDBRibose;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;

import java.util.ArrayList;

/**
 * Created by oliver on 15.12.15.
 * Common functions for all PDBNucleotides.
 * Allows access to structure and 5', 3' end
 * without knowledge about the actual type of residue
 */
public abstract class PDBNucleotide {
    private int resIndex;
    private Rna2DNode rna2DNode;
    private BooleanProperty isSelected = new SimpleBooleanProperty(false);
    PDB123PrintLog printLog;
    // Count number of defined atoms
    ArrayList<Boolean> defAtoms;
    private PDBNucleotide basePairedTo;
    // Future: set base pairing status
    protected boolean isBasePaired = false;
    public abstract void setAtom(String[] atom);

    public PDBNucleotide(PDB123PrintLog log)
    {
        this.printLog = log;
    }
    // Set the position index of the nucleotide
    public void setResIndex(int resIndex) {
        this.resIndex=resIndex;
    }
    public int getResIndex(){
        return this.resIndex;
    }

    public abstract String getType();
    public abstract Group getStructure(BooleanProperty showBackbone, BooleanProperty showSugar, BooleanProperty showNucleoBase);
    public abstract Point3D getFivePrimeEnd();
    public abstract Point3D getThreePrimeEnd();
    public abstract void setColorMode(String colorMode);
    public abstract Point3D getCentralCoordinate();
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
    // Are the coordinates of all atoms set?
    public  boolean allAtomsDefined(){
        return !defAtoms.contains(false);
    };
    public BooleanProperty isSelectedProperty() {return isSelected;}
    public Rna2DNode getRna2DNode(){return  rna2DNode;}

    public void setRna2DNode(Rna2DNode rna2DNode) {
        this.rna2DNode = rna2DNode;
    }
}
