package SecStructure.DotBracketNotation;

import GUI.LogMessagesUI.PDB123PrintLog;
import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by oliver on 26.01.16.
 * Calculate dot-bracket notation
 * for a set of PDBNucleotides
 */
public class DotBracket {
    private Map<Integer, PDBNucleotide> ntMap;
    private int firstNtIndex, lastNtIndex;
    private PDB123PrintLog printLog;

    public DotBracket(PDB123PrintLog printLog) {
        this.printLog = printLog;
    }

    public void setNtMap(Map<Integer, PDBNucleotide> ntMap) {
        this.ntMap = ntMap;
    }

    public void setFirstNtIndex(int firstNtIndex) {
        this.firstNtIndex = firstNtIndex;
    }

    public void setLastNtIndex(int lastNtIndex) {
        this.lastNtIndex = lastNtIndex;
    }

    // Compute dot-bracket string
    public String getDotBracket(Boolean detectPseudoKnots)
    {
        //New ArrayList, filled with empty string from pos. 0 to pos. last nt index
        ArrayList<String> dotBracketList = new ArrayList<>(Collections.nCopies(lastNtIndex+1, ""));
        // Pseudoknot detector
        PseudoKnots pseudoKnotDetector = new PseudoKnots();
        // For every possible nucleotide index
        // check if index belongs to a nucleotide
        // if so, check if nucleotide is base-paired
        // if not, add a dot to dotBracketList and proceed
        // Else,  add a opening bracket to dotBracketList
        // and identify base-pairing partner. Add a closing
        // bracket to dotBracketList for partner.
        for(int i = firstNtIndex; i <= lastNtIndex; i++)
        {
           if(ntMap.containsKey(i) && dotBracketList.get(i).equals("") )
           {
               PDBNucleotide nuc1 = ntMap.get(i);
               if (!nuc1.isBasePaired()) dotBracketList.set(i, ".");
               else{
                   PDBNucleotide nuc2 = nuc1.getBasePairedTo();
                   int posNuc2 = nuc2.getResIndex();
                   pseudoKnotDetector.addBasePair(i, posNuc2);
                   dotBracketList.set(i, "(");
                   dotBracketList.set(posNuc2, ")");
               }
           }
        }
        if (detectPseudoKnots) {
            ArrayList<Integer[]> pseudoknots = pseudoKnotDetector.detectCross();
            for (Integer[] pseudoKnot : pseudoknots
                    ) {
                printLog.printLogMessage("Pseudoknot detected between positions: " + pseudoKnot[0] + " and " + pseudoKnot[1]);
                dotBracketList.set(pseudoKnot[0], "{");
                dotBracketList.set(pseudoKnot[1], "}");

            }
        }
        // Convert ArrayList to simple string and return
        String dotBracket = String.join("", dotBracketList);
        printLog.printLogMessage("DotBracketNotation: "+dotBracket);
        return dotBracket;
    }
}
