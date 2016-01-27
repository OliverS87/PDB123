package PrimStructure;

import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;

import java.util.Map;

/**
 * Created by oliver on 27.01.16.
 */
public class ParseSequence
{
    public String parseRnaSeq(Map<Integer, PDBNucleotide> ntMap, int firstNtIndex, int lastNtIndex)
    {
        StringBuffer sb = new StringBuffer(lastNtIndex);
        for (int i = firstNtIndex; i <= lastNtIndex; i++)
        {
            if (ntMap.containsKey(i)) sb.append(ntMap.get(i).getType());
        }
        return sb.toString();
    }
}
