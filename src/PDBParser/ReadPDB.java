package PDBParser;

import GUI.LogMessagesUI.PDB123PrintLog;
import GUI.SettingsUI.PDB123SettingsPresenter;
import TertStructure.PDB3D.PDBNucleotide.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliver on 15.12.15.
 * Read PDB file. Generates PDBNucleotide objects from atomic coordinates.
 */
public class ReadPDB
{
    private BufferedReader br;
    // Store PDNBucleotides in Map, using index position as key
    private Map<Integer, PDBNucleotide> nucleotideIndex;
    // Store the first and last nucleotide index
    private int firstNtIndex, lastNtIndex;
    // Store the offset of the molecule from the origin
    private double xOffset, yOffset, zOffset;
    // Reference to the log window on the main UI
    private PDB123PrintLog log;
    // Reference to the settings window
    private PDB123SettingsPresenter settings;
    // Constructor
    public ReadPDB(PDB123PrintLog log)  {
        this.log=log;
    }
    // Get reference to current settings window
    public void setSettings(PDB123SettingsPresenter settings)
    {
        this.settings = settings;
    }

    // Set file path to PDB file, then read PDB file.
    // PDB File is read twice. At first, the coordinates of all atoms
    // are extracted. The variance in each dimension is then calculated.
    // This variance is used to center the coordinates to the origin.
    // When the file is read for the second time, the XYZ variance
    // is directly substracted from the coordinates. The collection of
    // PDBNucleotides returned by this class is therefore already origin-centered.
    public void setFilePath(String filePath) throws Exception {
        this.br = new BufferedReader(new FileReader(filePath));
        calculateCenterOffset();
        this.br = new BufferedReader(new FileReader(filePath));
        readPDB();
    }




    // Calculate X,Y,Z variance of atoms defined in current PDBFile
    private void calculateCenterOffset() throws IOException
    {
        String line;
        double xSum, ySum, zSum;
        xSum=ySum=zSum=0;
        int atomCounter = 0;
        while ((line = br.readLine()) != null)
        {
            // Ignore lines NOT starting with ATOM
            if (!line.startsWith("ATOM")) continue;
            atomCounter++;
            // Split line at blankspaces
            String[] lineSplit = line.split("\\s+");
            // Get XYZ coordinates
            String posX = lineSplit[6];
            String posY = lineSplit[7];
            String posZ = lineSplit[8];
            xSum+=Double.parseDouble(posX);
            ySum+=Double.parseDouble(posY);
            zSum+=Double.parseDouble(posZ);
        }
        // Calculate X,Y,Z offset (i.e. variance)
         xOffset = xSum/atomCounter;
         yOffset = ySum/atomCounter;
         zOffset = zSum/atomCounter;
    }


    // Main function: Convert atomic coordinates in PDB file to a collection of
    // PDBNucleotides. Creates one PDBNucleotide per sequence position. The atomic
    // coordinates and a unique atom ID are handed over to the PDBNucleotide object
    // in String format. PDBNucleotide then handel's the matching of
    // the atomic coordinates to the correct 3D object.
    private void readPDB() throws Exception {
        // currPosIndex: detect if next sequence position is reached
        int currPosIndex = -1;
        // Stared with arbitrarily low or high values for first and last
        // index position
        firstNtIndex = Integer.MAX_VALUE;
        lastNtIndex = -1;
        // Init. a new PDBNucleotide
        PDBNucleotide currNt = null;
        // Hashmap to store PDBNucleotides with index as key
        nucleotideIndex = new HashMap<Integer, PDBNucleotide>();
        // Scan line-wise through PDB file
        String line;
        while ((line = br.readLine()) != null)
        {
            if (line.startsWith("END")) {
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
            // Get first letter of nucleotide type
            char resType = lineSplit[3].toCharArray()[0];
            // Get index position in sequence
            int nextPosIndex = Integer.parseInt(lineSplit[5]);
            // Actual coordinates
            String posX = lineSplit[6];
            String posY = lineSplit[7];
            String posZ = lineSplit[8];
            // Substract offset/variance to center coordinate at origin
            // Therefore, coordinate is parsed from String to Double.
            // Afterwards coordinate is parsed back to String format.
            // This is obviously not very efficient. But it is required
            // at the moment because downstream functions require an
            // String[] as input.
            posX = (Double.parseDouble(posX)-xOffset)+"";
            posY = (Double.parseDouble(posY)-yOffset)+"";
            posZ = (Double.parseDouble(posZ)-zOffset)+"";
            // Collect atomID and posX,Y and Z in one String[]
            String[] posXYZ = new String[] {atomID, posX,posY, posZ};
            // Does a new nucleotide start at the current position?
            // If true, add current PDBNucleotide to Map<Integer,PDBNucleotide>
            // and create a new, empty PDBNucleotide
            if (currPosIndex != nextPosIndex){
                // Generate a new PDBNucleotide depending on the current residue type
                switch (resType)
                {
                    case('A'): currNt = new PDBAdenosine(log, settings);break;
                    case('C'): currNt = new PDBCytidine(log, settings);break;
                    case('G'): currNt = new PDBGuanosine(log, settings);break;
                    case('U'): currNt = new PDBUridine(log, settings);break;
                }
                // Set new seq. index position as new reference
                currPosIndex=nextPosIndex;
                currNt.setResIndex(resIndex);
                // Update the first and last nucleotide index position
                firstNtIndex = Math.min(resIndex, firstNtIndex);
                lastNtIndex = Math.max(resIndex, lastNtIndex);
                nucleotideIndex.put(resIndex, currNt);
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
            // Repeat until end of file is reached
        }
       // If no nucleotide was found in file, throw exception
        if (currPosIndex == -1) throw new Exception("No PDB File");
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
