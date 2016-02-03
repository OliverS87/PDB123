package PrimStructure;

import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;
import javafx.scene.text.TextFlow;

import java.util.Map;

/**
 * Created by oliver on 27.01.16.
 * Returns RNA sequence as string
 * As side-effect the 1D structure is added to
 * the Textflow area
 */
public class ParsePrimaryStructure
{
    public void parseRnaSeq(Map<Integer, PDBNucleotide> ntMap, int firstNtIndex, int lastNtIndex, TextFlow textFlow)
    {
      //  StringBuffer sb = new StringBuffer(lastNtIndex);
        for (int i = firstNtIndex; i <= lastNtIndex; i++)
        {
            if (ntMap.containsKey(i))
            {
                PDBNucleotide currNt = ntMap.get(i);
              //  sb.append(currNt.getType());
                NucleotideLetter currLetter = new NucleotideLetter(currNt.getType(), currNt.isSelectedProperty(), i);
                currLetter.fillProperty().bind(currNt.ntColorProperty());
                textFlow.getChildren().add(currLetter);
            }
        }

    }
}
