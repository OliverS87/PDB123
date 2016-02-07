package TertStructure.Basepairing;

import GUI.LogMessagesUI.PDB123PrintLog;
import GUI.SettingsUI.PDB123SettingsPresenter;
import SecStructure.RNA2D.Rna2DEdge;
import TertStructure.PDB3D.PDBNucleotide.*;
import TertStructure.RNA3DComponents.DrawHydrogenBond;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oliver on 25.01.16.
 * Detect potential H-Bonds in the 3D structure
 * of an RNA. Structure is represented by a collection
 * of PDBNucleotides, each representing one nucleotide position.
 * To form a h-bond, nucleotides need to be in close proximity. Canonical
 * h-bonds are formed between 3 (G-C) or 2 (A-T) donor-acceptor pairs.
 * At first, nucleotide pairs with low distance to each other are identified.
 * These candidate pairs are then further analysed by their
 * donor-acceptor pair distances. If distances for all 2 or 3 pairs is low
 * enough to allow an h-bond to be formed, 2D and 3D representations
 * of a hydrogen bond are generated.
 */
public class HydrogenBondDetector {
    // PDBNucleotides stored in a hashmap
    // PDBNucleotides are retrieved with their
    // sequence index position as key.
    // First and last index position are known.
    private Map<Integer, PDBNucleotide> ntMap;
    private int firstNtIndex, lastNtIndex;
    // Print messages on main UI
    private PDB123PrintLog printLog;
    // Cut-off values: Min. required angle between donor-acceptor atoms
    // and max. allowed distance between donor-acceptor atoms
    private Double MINBONDANGLE, MAXBONDDISTANCE;
    // Reference to containers for 2D and 3D elements
    private ArrayList<Rna2DEdge> edge2DList;
    private Group structure3D;
    // Reference to settings window
    private PDB123SettingsPresenter settings;
    // Show h-bonds in 3D subscene?
    private BooleanProperty showHBonds;

    // Set references/values by constructor or setters:
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

    public void setRna2DEdge(ArrayList<Rna2DEdge> edge2DList) {
        this.edge2DList = edge2DList;
    }

    public void setStructure3D(Group structure3D) {
        this.structure3D = structure3D;
    }

    public void setSettings(PDB123SettingsPresenter settings) {
        this.settings = settings;
    }

    public void setShowHBonds(BooleanProperty showHBonds) {
        this.showHBonds = showHBonds;
    }

    // Main function for the detection of H-bonds
    public void detectHDB() {
        // Add sensitivity factor to hydrogen bond detection limit value
        // Sensitivity factor is set in window settings
        MINBONDANGLE = settings.detectionSensitivityProperty().doubleValue() * 125;
        MAXBONDDISTANCE = 3. / settings.detectionSensitivityProperty().doubleValue();
        // Container for 3D hydrogen bond visualizations
        Group hydrogenBonds = new Group();
        // Get a list of all present sequence positions
        ArrayList<Integer> ntIndices = new ArrayList<>();
        for (int i = firstNtIndex; i <= lastNtIndex; i++) {
            if (ntMap.containsKey(i)) ntIndices.add(i);
        }
        // Go over all possible pairs of nucleotides. If the center position
        // of two nucleotides differs by less then 10 Angstrom, that pair
        // is analysed in detail by makeHDB(). If h-bonds are predicted between
        // the pair, 3D hydrogen bonds are returned and added to Group hydrogenBonds.
        // As side-effect, a 2D h-bond is added to ArrayList<Rna2Dedges> edge2DList.
        boolean[][] distanceMatrix = new boolean[ntIndices.size()][ntIndices.size()];
        for (int i = 0; i < ntIndices.size(); i++)
            for (int j = i; j < ntIndices.size(); j++) {
                PDBNucleotide nt1 = ntMap.get(ntIndices.get(i));
                PDBNucleotide nt2 = ntMap.get(ntIndices.get(j));
                Point3D p1 = nt1.getCentralCoordinate();
                Point3D p2 = nt2.getCentralCoordinate();
                // Is distance between nt1 and nt2 less than 10 Angstrom?
                distanceMatrix[i][j] = (p1.distance(p2) <= 10);
                // If true, try to derive h-bonds between nt1 and nt2
                // with makeHDB();
                // If no h-bonds are possible, function will return null.
                if ((p1.distance(p2) <= 10)) {
                    Group currentHDB = makeHDB(nt1, nt2);
                    if (currentHDB != null) hydrogenBonds.getChildren().add(currentHDB);
                }
            }
        // Connect the visibility property of 3D h-bonds to boolean property showHBonds.
        hydrogenBonds.visibleProperty().bind(showHBonds);
        // Add h-bonds to Group containing other 3D structures
        structure3D.getChildren().add(hydrogenBonds);
    }

    private Group makeHDB(PDBNucleotide nt1, PDBNucleotide nt2) {
        // Check if nt1 or nt2 are already base paired
        if (nt1.isBasePaired() || nt2.isBasePaired()) return null;
        // Identify nt1 and nt2
        String nt1Type = nt1.getType();
        String nt2Type = nt2.getType();
        // Setup an identifier for the current pair
        String ntPair = nt1Type + "_" + nt2Type;
        // Container for HDB visualization of current nt pair
        // Function in switch case returns either a set of DrawHydrogenBonds
        // or null.
        switch (ntPair) {
            case ("U_A"):
                return makeAdenineUracileBasepair((PDBAdenosine) nt2, (PDBUridine) nt1);
            case ("A_U"):
                return makeAdenineUracileBasepair((PDBAdenosine) nt1, (PDBUridine) nt2);
            case ("G_C"):
                return makeCytosineGuanineBasepair((PDBCytidine) nt2, (PDBGuanosine) nt1);
            case ("C_G"):
                return makeCytosineGuanineBasepair((PDBCytidine) nt1, (PDBGuanosine) nt2);
        }
        // If nucleotide pair is not ura/ade or gua/cyt return null
        return null;

    }

    // Check if a A-U basepair can be formed
    // if true, return 3D h-bonds and add 2D h-bonds to edges2DList
    // if false, return null
    private Group makeAdenineUracileBasepair(PDBAdenosine adeNt, PDBUridine uraNt) {
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
        if (adeN6 == null || uraO4 == null) {
            return null;
        }
        // If protons are not defined, look at the distance between adeN6 and uraO4
        else if (adeH61 == null || adeH62 == null) {
            if (adeN6.distance(uraO4) <= MAXBONDDISTANCE) {
                hdbAdeUra.getChildren().add(new DrawHydrogenBond(adeN6, uraO4, adeNt, uraNt, adeUraSelected));
                missingHydrogen = true;
            } else {
                return null;
            }

        } else {
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
        if (adeN1 == null || uraN3 == null) {
            return null;
        }
        // If protons are not defined, look at the distance between adeN1 and uraN3
        else if (uraH3 == null) {
            if (adeN1.distance(uraN3) <= MAXBONDDISTANCE) {
                hdbAdeUra.getChildren().add(new DrawHydrogenBond(adeN1, uraN3, adeNt, uraNt, adeUraSelected));
                missingHydrogen = true;
            } else {
                return null;
            }

        } else {
            if ((uraH3.distance(adeN1) <= MAXBONDDISTANCE) && uraH3.angle(adeN1, uraN3) >= MINBONDANGLE) {
                hdbAdeUra.getChildren().add(new DrawHydrogenBond(adeN1, uraH3, adeNt, uraNt, adeUraSelected));
            } else {
                return null;
            }
        }
        printLog.printLogMessage("Found HDB between ade " + adeNt.getResIndex() + " and ura " + uraNt.getResIndex());
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

    // Check if a G-C basepair can be formed
    // if true, return 3D h-bonds and add 2D h-bonds to edges2DList
    // if false, return null
    private Group makeCytosineGuanineBasepair(PDBCytidine cytNt, PDBGuanosine guaNt) {
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
        if (guaO6 == null || cytN4 == null) {
            return null;
        }
        // If protons are not defined, look at the distance between guaO6 and cytN4
        else if (cytH41 == null || cytH42 == null) {
            if (guaO6.distance(cytN4) <= MAXBONDDISTANCE) {
                hdbGuaCyt.getChildren().add(new DrawHydrogenBond(guaO6, cytN4, guaNt, cytNt, cytGuaSelected));
                missingHydrogen = true;
            } else {
                return null;
            }
        } else {
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
            if (cytH41.distance(guaO6) > cytH42.distance(guaO6)) {
                // Check hydrogen bond angle between cyt H41 and gua O6
                if ((cytH42.distance(guaO6) <= MAXBONDDISTANCE) && (cytH42.angle(cytN4, guaO6) >= MINBONDANGLE)) {
                    // angle is large enough to allow stable HDB
                    hdbGuaCyt.getChildren().add(new DrawHydrogenBond(cytH42, guaO6, guaNt, cytNt, cytGuaSelected));
                } else {
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
        if (guaN1 == null || cytN3 == null) {
            return null;
        }
        // If protons are not defined, look at the distance between guaN1 and cytN3
        else if (guaH1 == null) {
            if (guaN1.distance(cytN3) <= MAXBONDDISTANCE) {
                hdbGuaCyt.getChildren().add(new DrawHydrogenBond(guaN1, cytN3, guaNt, cytNt, cytGuaSelected));
                missingHydrogen = true;
            } else {
                return null;
            }
        } else {
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
        if (guaN2 == null || cytO2 == null) {
            return null;
        }
        // If protons are not defined, look at the distance between guaN2 and cytO2
        if (guaH21 == null || guaH22 == null) {
            if (guaN2.distance(cytO2) <= MAXBONDDISTANCE) {
                hdbGuaCyt.getChildren().add(new DrawHydrogenBond(guaN2, cytO2, guaNt, cytNt, cytGuaSelected));
                missingHydrogen = true;
            } else {
                return null;
            }
        } else {
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
        printLog.printLogMessage("Found HDB between gua " + guaNt.getResIndex() + " and cyt " + cytNt.getResIndex());
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
        return hdbGuaCyt;
    }


}
