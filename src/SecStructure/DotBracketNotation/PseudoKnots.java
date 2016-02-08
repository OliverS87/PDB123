package SecStructure.DotBracketNotation;

import java.util.ArrayList;

/**
 * Created by oliver on 05.02.16.
 * Detect pseudoknots in a set of basepaired positions.
 * Looks for the most conflicting basepair, i.e. that base-pair
 * that intercalates most with all other basepairs. That basepair is
 * removed and the process is repeated until no intercalating base-pairs
 * are present anymore.
 * Returns a list of all detected pseudoknots in Integer[] format.
 */
public class PseudoKnots
{
    // Two lists:
    // 1. List of basepairs
    // 2. List of detected pseudoknots
    private ArrayList<BasePairs> basePairsLst;
    private ArrayList<Integer[]> pseudoKnots;
    // Constructor
    public PseudoKnots()
    {
        basePairsLst = new ArrayList<>();
        pseudoKnots = new ArrayList<>();
    }
    // Add base-pairs. Base-pairs are stored as BasePairs object.
    // BasePairs object contains an additional field for a "conflict"-score.
    // This score is used to find the most conflicting base-pair.
    public void addBasePair(int startPos, int endPos)
    {
        basePairsLst.add(new BasePairs(startPos, endPos, 0));
    }

    // Iterate through list of Base-pairs. Compare each base-pair to
    // all other base-pairs and calculate for each base-pair the conflict-score
    // (i.e. the number of other base-pairs it intercalates with).
    // Subsequently remove the most conflicting base-pair.
    // Returns a list of base-pair indices that are pseudoknots.
    public ArrayList<Integer[]> detectCross()
    {
        // return null if no pseudoknots were detected
        if (basePairsLst.size()==0) return null;
        do {
            resetConflictScore();
            for (BasePairs pseudoKnotCandidateBP: basePairsLst
                    ) {
                for (BasePairs otherBP: basePairsLst
                        ) {
                    if (crossingBasePair(pseudoKnotCandidateBP, otherBP)) pseudoKnotCandidateBP.increaseConflictScore();

                }
            }
            removeMostConflictingBasepair();
        }
        while(hasCrossingBasePairs());
        return this.pseudoKnots;
    }

    // Detect if two base-pairs are intercalated
    // Algorithm appears to produce false-positives when base-pairs are formed
    // between nucleotides with short distances in the chain. Therefore consider
    // only base-pairs between more distant nucleotides as potential pseudoknots.
    private boolean crossingBasePair(BasePairs bp1, BasePairs bp2)
    {
        if ((Math.abs(bp1.start-bp1.end) <= 10) || (Math.abs(bp2.start-bp2.end) <= 10)) return false;
        Boolean cross = ((bp1.start < bp2.start) && (bp1.end < bp2.end)) || ((bp1.start > bp2.start) && (bp1.end > bp2.end));
        return cross;
    }
    // Check if set of base-pairs in basePairsLst contains
    // BasePairs with a conflict score above 0
    private boolean hasCrossingBasePairs()
    {
        for (BasePairs bp:basePairsLst
             ) {
            if (bp.conflictScore > 0) return true;
        }
        return false;
    }
    // Set the conflict score of all base-pairs in basePairsLst
    // back to zero
    private void resetConflictScore()
    {
        for (BasePairs bp: basePairsLst
             ) {
        bp.resetConflictScore();

        }
    }
    // Find the most conflicting base-pair in basePairsLst
    // Remove that base-pair from the list
    // Add its start and end positions to pseudoKnots list
    private void removeMostConflictingBasepair()
    {
        int maxConflictScore = 0;
        BasePairs maxConflictBasepair = null;
        for (BasePairs bp: basePairsLst
             ) {
            if (bp.conflictScore > maxConflictScore)
            {
                maxConflictScore = bp.conflictScore;
                maxConflictBasepair = bp;
            }
        }
        if (maxConflictBasepair != null)
        {
            Integer[] pseudoknot = {maxConflictBasepair.start,maxConflictBasepair.end};
            pseudoKnots.add(pseudoknot);
            basePairsLst.remove(maxConflictBasepair);
        }
    }


    // Internal representation of a base-pair
    // Adds a field for the conflict score to the start and end position of the base-pair
    private class BasePairs
    {
        private int start, end, conflictScore;

        private BasePairs(int start, int end, int conflictScore) {
            this.start = start;
            this.end = end;
            this.conflictScore = conflictScore;
        }
        private void increaseConflictScore()
        {
            this.conflictScore+=1;
        }
        private void resetConflictScore()
        {
            this.conflictScore=0;
        }
    }
}
