package TertStructure.PDB3D.PDBNucleotide;

import javafx.geometry.Point3D;
import javafx.scene.Group;

/**
 * Created by oliver on 15.12.15.
 * Common functions for all PDBNucleotides.
 * Allows access to structure and 5', 3' end
 * without knowledge about the actual type of residue
 */
public abstract class PDBNucleotide {
    private int resIndex;
    // Future: set base pairing status
    protected boolean isBasePaired = false;
    public abstract void setAtom(String[] atom);

    // Set the position index of the nucleotide
    public void setResIndex(int resIndex) {
        this.resIndex=resIndex;
    }
    public int getResIndex(){
        return this.resIndex;
    }

    public abstract String getType();
    public abstract Group getStructure();
    public abstract Point3D getFivePrimeEnd();
    public abstract Point3D getThreePrimeEnd();
    public abstract void setColorMode(String colorMode);
}
