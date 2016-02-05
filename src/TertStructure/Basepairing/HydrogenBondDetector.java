package TertStructure.Basepairing;

import GUI.PDB123PrintLog;
import SecStructure.RNA2D.Rna2DEdge;
import TertStructure.PDB3D.PDBNucleotide.*;
import TertStructure.RNA3DComponents.DrawHydrogen;
import TertStructure.RNA3DComponents.DrawHydrogenBond;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oliver on 25.01.16.
 */
public class HydrogenBondDetector {
    private Map<Integer, PDBNucleotide> ntMap;
    private int firstNtIndex, lastNtIndex;
    private PDB123PrintLog printLog;
    private static double MINBONDANGLE = 110.0;
    private static double MAXBONDDISTANCE = 3.0;
    private ArrayList<Rna2DEdge> edge2DList;
    private Group structure3D;

    public HydrogenBondDetector(PDB123PrintLog printLog) {
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

    public void setRna2DEdge(ArrayList<Rna2DEdge> edge2DList) {this.edge2DList=edge2DList;}

    public void setStructure3D(Group structure3D) {this.structure3D = structure3D;}

    public void detectHDB()
    {
        // Container for hydrogen bond visualizations
        Group hydrogenBonds = new Group();
        ArrayList<Integer> ntIndices = new ArrayList<>();
        for (int i = firstNtIndex; i <= lastNtIndex; i++)
        {
            if (ntMap.containsKey(i)) ntIndices.add(i);
        }
        boolean[][] distanceMatrix = new boolean[ntIndices.size()][ntIndices.size()];
        for (int i = 0; i < ntIndices.size(); i++)
            for (int j = i; j < ntIndices.size(); j++)
            {
                PDBNucleotide nt1 =ntMap.get(ntIndices.get(i));
                PDBNucleotide nt2 =ntMap.get(ntIndices.get(j));
                Point3D p1 = nt1.getCentralCoordinate();
                Point3D p2 = nt2.getCentralCoordinate();
                distanceMatrix[i][j] = (p1.distance(p2)<=10);
                if ((p1.distance(p2)<=10)) {
                    Group currentHDB = makeHDB(nt1, nt2);
                    if (currentHDB !=  null) hydrogenBonds.getChildren().add(currentHDB);
                }
            }

        structure3D.getChildren().add(hydrogenBonds);
    }
    private Group makeHDB(PDBNucleotide nt1, PDBNucleotide nt2)
    {

        // Check if nt1 or nt2 are already base paired
        if (nt1.isBasePaired() || nt2.isBasePaired()) return null;
        // Identify nt1 and nt2
        String nt1Type = nt1.getType();
        String nt2Type = nt2.getType();
        // Check if nt1 and nt2 are different
        // If not, return
        if (nt1Type.equals(nt2Type)) return null;
       String ntPair = nt1Type+"_"+nt2Type;
        // Container for HDB visualization of current nt pair
        // Function in switch case returns either a set of DrawHydrogenBonds
        // or null.
        Group ntPairHDB = new Group();
        switch (ntPair){
            case("U_A"): return makeAdenineUracileBasepair((PDBAdenosine) nt2, (PDBUridine) nt1);
            case("A_U"): return makeAdenineUracileBasepair((PDBAdenosine) nt1, (PDBUridine) nt2);
            case("G_C"): return makeCytosineGuanineBasepair((PDBCytidine)nt2, (PDBGuanosine) nt1);
            case("C_G"): return makeCytosineGuanineBasepair((PDBCytidine)nt1, (PDBGuanosine) nt2);
        }
        // If nucleotide pair is not ura/ade or gua/cyt return null
        return null;

    }

    private Group makeAdenineUracileBasepair(PDBAdenosine adeNt, PDBUridine uraNt){
        // Container for hydrogen bond (HDB) visualization lines
        // Only returned if a stable base pair between ade and ura
        // is confirmed
        Group hdbAdeUra = new Group();
        // Remember if HDB had to be detected without hydrogen atom(s)
        Boolean missingHydrogen = false;
        // Add common boolean property to both h.bonds
        BooleanProperty adeUraSelected = new SimpleBooleanProperty(false);
        // Check first potential hydrogenbond
        // between ade NH2(6) and ura O4
        // ade NH2(6) has two hydrogens: H61 and H62
        // check first which one is closer to ura O4
        Point3D adeH61 = adeNt.getAdenine().getH61();
        Point3D adeH62 = adeNt.getAdenine().getH62();
        Point3D adeN6 = adeNt.getAdenine().getN6();
        Point3D uraO4 = uraNt.getUracil().getO4();
        // Exit here if Donor Atom positions are undefined
        if (adeN6 ==null || uraO4 == null)
        {
            return null;
        }
       // If protons are not defined, look at the distance between adeN6 and uraO4
        // Add 1 Angstrom to max. allowed distance
        else if (adeH61==null || adeH62 ==null)
        {
            if (adeN6.distance(uraO4) <= MAXBONDDISTANCE+1)
            {
                hdbAdeUra.getChildren().add(new DrawHydrogenBond(adeN6, uraO4, adeNt, uraNt, adeUraSelected));
                missingHydrogen=true;
            }
            else
            {
                return  null;
            }

        }
        else {
            // First case: ade H61 closer to ura O4 than ade H62
            if (adeH61.distance(uraO4) <= adeH62.distance(uraO4)) {
                // Check hydrogen bond angle between ade H61 and ura O4
                if ((adeH61.distance(uraO4) <= MAXBONDDISTANCE) && (adeH61.angle(adeN6, uraO4) >= MINBONDANGLE)) {
                    // angle is large enough to allow stable HDB
                    hdbAdeUra.getChildren().add(new DrawHydrogenBond(adeH61, uraO4, adeNt, uraNt, adeUraSelected));
                } else {
                    // If angle is not large enough for stable HDB,
                    // return null object
                    return null;
                }
            }
            // Alternative case: ade H62 closer to ura O4 than ade H61
            if (adeH61.distance(uraO4) > adeH62.distance(uraO4)) {
                // Check hydrogen bond angle between ade H62 and ura O4
                if ((adeH62.distance(uraO4) <= MAXBONDDISTANCE) && (adeH62.angle(adeN6, uraO4) >= MINBONDANGLE)) {
                    // angle is large enough to allow stable HDB
                    hdbAdeUra.getChildren().add(new DrawHydrogenBond(adeH62, uraO4, adeNt, uraNt, adeUraSelected));
                } else {
                    // If angle is not large enough for stable HDB,
                    // return null object
                    return null;
                }
            }
        }
        // Check second potential HDB
        // between ade N1 and ura NH(3)
        Point3D adeN1 = adeNt.getAdenine().getN1();
        Point3D uraN3 = uraNt.getUracil().getN3();
        Point3D uraH3 = uraNt.getUracil().getH3();
        // Exit here if any donor position is undefined
        if (adeN1==null || uraN3 ==null)
        {
            return null;
        }
        // If protons are not defined, look at the distance between adeN1 and uraN3
        // Add 1 Angstrom to max. allowed distance
        else if(uraH3 ==null)
        {
            if (adeN1.distance(uraN3) <= MAXBONDDISTANCE+1)
            {
                hdbAdeUra.getChildren().add(new DrawHydrogenBond(adeN1, uraN3, adeNt, uraNt, adeUraSelected));
                missingHydrogen=true;
            }
            else
            {
                return  null;
            }

        }
        else
        {
            if ((uraH3.distance(adeN1) <= MAXBONDDISTANCE) && uraH3.angle(adeN1, uraN3) >= MINBONDANGLE) {
                hdbAdeUra.getChildren().add(new DrawHydrogenBond(adeN1, uraH3, adeNt, uraNt, adeUraSelected));
            } else {
                return null;
            }
        }
        printLog.printLogMessage("Found HDB between ade "+adeNt.getResIndex()+" and ura "+uraNt.getResIndex());
        if (missingHydrogen) printLog.printLogMessage("WARNING: HDB was estimated w/o Hydrogen atom!");
        // Store base pairing information in nucleotide classes
        uraNt.setBasePaired(true);
        uraNt.setBasePairedTo(adeNt);
        adeNt.setBasePaired(true);
        adeNt.setBasePairedTo(uraNt);
        // Add basepair to 2D rna graph
        Rna2DEdge edge = new Rna2DEdge(uraNt.getRna2DNode(), adeNt.getRna2DNode(), adeUraSelected);
        edge.setEdgeStyle("hydroBond");
        edge2DList.add(edge);
        return hdbAdeUra;


    }
    private Group makeCytosineGuanineBasepair(PDBCytidine cytNt, PDBGuanosine guaNt){
        // Container for hydrogen bond (HDB) visualization lines
        // Only returned if a stable base pair between gua and cyt
        // is confirmed
        Group hdbGuaCyt = new Group();
        // Remember if HDB had to be detected without hydrogen atom(s)
        Boolean missingHydrogen = false;
        // Add common boolean property to all three h.bonds
        BooleanProperty cytGuaSelected = new SimpleBooleanProperty(false);
        // Check first potential hydrogenbond
        // between gua O6 and cyt NH2(4)
        // cyt NH2(4) has two hydrogens: H41 and H42
        // check first which one is closer to gua O6
        Point3D guaO6 = guaNt.getGuanine().getO6();
        Point3D cytN4 = cytNt.getCytosine().getN4();
        Point3D cytH41 = cytNt.getCytosine().getH41();
        Point3D cytH42 = cytNt.getCytosine().getH42();
        // Exit here if any donor position is undefined
        if (guaO6==null || cytN4 ==null)
        {
            return null;
        }
        // If protons are not defined, look at the distance between guaO6 and cytN4
        // Add 1 Angstrom to max. allowed distance
        else if (cytH41 ==null|| cytH42 ==null)
        {
            if (guaO6.distance(cytN4) <= MAXBONDDISTANCE+1)
            {
                hdbGuaCyt.getChildren().add(new DrawHydrogenBond(guaO6, cytN4, guaNt, cytNt, cytGuaSelected));
                missingHydrogen=true;
            }
            else
            {
                return  null;
            }
        }
        else
        {
            // First case: cyt H41 closer to gua O6 than cyt H42
            if (cytH41.distance(guaO6) <= cytH42.distance(guaO6)) {
                // Check hydrogen bond angle between cyt H41 and gua O6
                if ((cytH41.distance(guaO6) <= MAXBONDDISTANCE) && (cytH41.angle(cytN4, guaO6) >= MINBONDANGLE)) {
                    // angle is large enough to allow stable HDB
                    hdbGuaCyt.getChildren().add(new DrawHydrogenBond(cytH41, guaO6, guaNt, cytNt, cytGuaSelected));
                } else {
                    // If angle is not large enough for stable HDB,
                    // return null object
                    return null;
                }
            }

        // Alternative case: cyt H42 closer to gua O6 than cyt H41
        if (cytH41.distance(guaO6) > cytH42.distance(guaO6))
        {
            // Check hydrogen bond angle between cyt H41 and gua O6
            if ((cytH42.distance(guaO6) <= MAXBONDDISTANCE) && (cytH42.angle(cytN4,guaO6) >= MINBONDANGLE))
            {
                // angle is large enough to allow stable HDB
                hdbGuaCyt.getChildren().add(new DrawHydrogenBond(cytH42, guaO6, guaNt, cytNt, cytGuaSelected));
            }
            else
            {
                // If angle is not large enough for stable HDB,
                // return null object
                return null;
            }
        }
        }
        // Check second potential hydrogenbond
        // between gua N1 and cyt N3
        Point3D guaN1 = guaNt.getGuanine().getN1();
        Point3D guaH1 = guaNt.getGuanine().getH1();
        Point3D cytN3 = cytNt.getCytosine().getN3();
        // Exit here if any donor position is undefined
        if (guaN1==null || cytN3 ==null)
        {
            return null;
        }
        // If protons are not defined, look at the distance between guaN1 and cytN3
        // Add 1 Angstrom to max. allowed distance
        else if (guaH1 ==null)
        {
            if (guaN1.distance(cytN3) <= MAXBONDDISTANCE+1)
            {
                hdbGuaCyt.getChildren().add(new DrawHydrogenBond(guaN1, cytN3, guaNt, cytNt, cytGuaSelected));
                missingHydrogen=true;
            }
            else
            {
                return  null;
            }
        }
        else
        {
            if ((guaH1.distance(cytN3) <= MAXBONDDISTANCE) && guaH1.angle(guaN1, cytN3) >= MINBONDANGLE) {
                hdbGuaCyt.getChildren().add(new DrawHydrogenBond(guaH1, cytN3, guaNt, cytNt, cytGuaSelected));
            } else {
                return null;
            }
        }
        // Check third potential hydrogenbond
        // between gua NH2(2) and cyt O2
        // gua NH2(2) has two hydrogens: H21 and H22
        // check first which one is closer to cyt O2
        Point3D guaN2 = guaNt.getGuanine().getN2();
        Point3D guaH21 = guaNt.getGuanine().getH21();
        Point3D guaH22 = guaNt.getGuanine().getH22();
        Point3D cytO2 = cytNt.getCytosine().getO2();
        // Exit here if any donor position is undefined
        if (guaN2==null || cytO2 ==null)
        {
            return null;
        }
        // If protons are not defined, look at the distance between guaN2 and cytO2
        // Add 1 Angstrom to max. allowed distance
        if (guaH21 ==null || guaH22 ==null)
        {
            if (guaN2.distance(cytO2) <= MAXBONDDISTANCE+1)
            {
                hdbGuaCyt.getChildren().add(new DrawHydrogenBond(guaN2, cytO2, guaNt, cytNt, cytGuaSelected));
                missingHydrogen=true;
            }
            else
            {
                return  null;
            }
        }
        else
        {
            // First case: gua H21 closer to cyt O2 than gua H22
            if (guaH21.distance(cytO2) <= guaH22.distance(cytO2)) {
                // Check hydrogen bond angle between gua H21 and cyt O2
                if ((guaH21.distance(cytO2) <= MAXBONDDISTANCE) && (guaH21.angle(guaN2, cytO2) >= MINBONDANGLE)) {
                    // angle is large enough to allow stable HDB
                    hdbGuaCyt.getChildren().add(new DrawHydrogenBond(guaH21, cytO2, guaNt, cytNt, cytGuaSelected));
                } else {
                    // If angle is not large enough for stable HDB,
                    // return null object
                    return null;
                }
            }
            // Alternative case: gua H22 closer to cyt O2 than gua H21
            if (guaH21.distance(cytO2) > guaH22.distance(cytO2)) {
                // Check hydrogen bond angle between gua H22 and cyt O2
                if ((guaH22.distance(cytO2) <= MAXBONDDISTANCE) && (guaH22.angle(guaN2, cytO2) >= MINBONDANGLE)) {
                    // angle is large enough to allow stable HDB
                    hdbGuaCyt.getChildren().add(new DrawHydrogenBond(guaH22, cytO2, guaNt, cytNt, cytGuaSelected));
                } else {
                    // If angle is not large enough for stable HDB,
                    // return null object
                    return null;
                }
            }
        }
        printLog.printLogMessage("Found HDB between gua "+guaNt.getResIndex()+" and cyt "+cytNt.getResIndex());
        if (missingHydrogen) printLog.printLogMessage("WARNING: HDB was estimated w/o Hydrogen atom!");
        // Store base pairing information in nucleotide classes
        guaNt.setBasePaired(true);
        guaNt.setBasePairedTo(cytNt);
        cytNt.setBasePaired(true);
        cytNt.setBasePairedTo(guaNt);

        // Add basepair to 2D rna graph
        Rna2DEdge edge = new Rna2DEdge(guaNt.getRna2DNode(), cytNt.getRna2DNode(), cytGuaSelected);
        edge.setEdgeStyle("hydroBond");
        edge2DList.add(edge);
        return  hdbGuaCyt;
    }



}
