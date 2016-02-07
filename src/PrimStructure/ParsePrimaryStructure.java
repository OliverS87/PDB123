package PrimStructure;

import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oliver on 27.01.16.
 * Converts Map<Integer, PDBNucleotide> to
 * a set of Text objects, each representing one
 * nucleotide. Returns Text objects as ArrayList
 */
public class ParsePrimaryStructure
{
    public ArrayList<Text> parseRnaSeq(Map<Integer, PDBNucleotide> ntMap, int firstNtIndex, int lastNtIndex)
    {
      ArrayList<Text> text1D = new ArrayList<>();
        for (int i = firstNtIndex; i <= lastNtIndex; i++)
        {
            if (ntMap.containsKey(i))
            {
                PDBNucleotide currNt = ntMap.get(i);
                NucleotideLetter currLetter = new NucleotideLetter(currNt.getType(), currNt.isSelectedProperty(), i);
                currLetter.fillProperty().bind(currNt.ntColorProperty());
                text1D.add(currLetter);
            }
        }
    return text1D;
    }
}
