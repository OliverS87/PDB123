package PDBParser;

import GUI.PDB123PrintLog;
import TertStructure.PDB3D.PDBNucleotide.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliver on 15.12.15.
 * Read PDB file. Generate PDBNucleotide objects from coordinates.
 * Stores all PDBNucleotide objects sequentielly in ArrayList.
 * ArrayList is returned.
 */
public class ReadPDB
{
    private BufferedReader br;
    // Collect all generated PDBNucleotides in ArrayList
    private ArrayList<PDBNucleotide> ntList;
    private Map<Integer, PDBNucleotide> nucleotideIndex;
    // Store the first and last nucleotide index
    private int firstNtIndex, lastNtIndex;
    private PDB123PrintLog log;
    public ReadPDB(PDB123PrintLog log)  {
        this.log=log; this.ntList = new ArrayList<>();
    }
    // Set file path to PDB file, then read PDB file
    public void setFilePath(String filePath) throws IOException {
        this.br = new BufferedReader(new FileReader(filePath));
        // Remove previous Nucleotides
        ntList.clear();
        readPDB();
    }


    // PDB123StartUp function of PDBreader. Converts information from PDB file
    // into Java objects. PDBNucleotides store all coordinates,
    // one PDBNucleotide per sequence position. At the moment hydrogen atoms
    // are ignored. SetAtom function of PDBNucleotides matches the coordinates
    // to the correct field.
    private void readPDB() throws IOException {
        // currPosIndex: detect if next sequence position is reached
        int currPosIndex = -1;
        firstNtIndex = Integer.MAX_VALUE;
        lastNtIndex = -1;
        PDBNucleotide currNt = null;
        // Hashmap to link residue index to PDBNucleotide
        nucleotideIndex = new HashMap<Integer, PDBNucleotide>();
        String line = "";
        while ((line = br.readLine()) != null)
        {
            if (line.startsWith("END")) {
                if (currNt != null){
                    // Add the last PDBnt to the Arraylist
                    ntList.add(currNt);
                }
                // Exit when END is reached
                break;
            }
            // Ignore lines NOT starting with ATOM
            if (!line.startsWith("ATOM")) continue;
            // Split line at blankspaces
            String[] lineSplit = line.split("\\s+");
            // Store residue index
            int resIndex = Integer.parseInt(lineSplit[5]);
            // atomID needed to match coordinates to correct position
            String atomID = lineSplit[2];
            char resType = lineSplit[3].toCharArray()[0];
            int nextPosIndex = Integer.parseInt(lineSplit[5]);
            // Actual coordinates
            String posX = lineSplit[6];
            String posY = lineSplit[7];
            String posZ = lineSplit[8];
            // Collect atomID and posX,Y and Z in one String[]
            String[] posXYZ = new String[] {atomID, posX,posY, posZ};
            // Does a new nucleotide start at the current position?
            // If true, add current PDBNucleotide to ArrayList
            // and create a new, empty PDBNucleotide
            if (currPosIndex != nextPosIndex){
                if (currPosIndex != -1){
                    // Add current PDBnucleotide to ArrayList
                    // (not at the very beginning of the read-in)
                   // ntList.add(currNt);
                }
                // Generate a new PDBNucleotide depending on the current residue type
                switch (resType)
                {
                    case('A'): currNt = new PDBAdenosine(log);break;
                    case('C'): currNt = new PDBCytidine(log);break;
                    case('G'): currNt = new PDBGuanosine(log);break;
                    case('U'): currNt = new PDBUridine(log);break;
                }
                // Set new seq. index position as new reference
                currPosIndex=nextPosIndex;
                currNt.setResIndex(resIndex);
                // Update the first and last nucleotide index position
                firstNtIndex = Math.min(resIndex, firstNtIndex);
                lastNtIndex = Math.max(resIndex, lastNtIndex);
                nucleotideIndex.put(resIndex, currNt);
                ntList.add(currNt);
            }
            //Cast PDBNucleotide to their actual type to allow setting of
            // type-specific positions
            switch (resType)
            {
                case('A'): ((PDBAdenosine)currNt).setAtom(posXYZ);break;
                case('C'): ((PDBCytidine)currNt).setAtom(posXYZ);break;
                case('G'): ((PDBGuanosine)currNt).setAtom(posXYZ);break;
                case('U'): ((PDBUridine)currNt).setAtom(posXYZ);break;
            }

        }

    }
    // Return ArrayList with PDBnucleotides
    public ArrayList<PDBNucleotide> getNtList()
    {
        return ntList;
    }
    public Map<Integer, PDBNucleotide> getNtMap(){return nucleotideIndex;}
    // Return index of first nucleotide in chain
    public int getFirstNtIndex() {
        return firstNtIndex;
    }
    // Return index of last nucleotide in chain
    public int getLastNtIndex() {
        return lastNtIndex;
    }
}
