package SelectionModel;

import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;
import javafx.scene.Node;

import java.util.ArrayList;

/**
 * Created by oliver on 03.02.16.
 */
public class PDB123SelectionModel
{
    private static ArrayList<Selectable> selectablesLst;

    public static void initSelectionModel()
    {
        selectablesLst = new ArrayList<>();
    }
    public static void unselectAll()
    {
        for (Selectable s: selectablesLst
             ) {
            s.setSelected(false);
        }
    }
    public static void registerSelectable(Selectable s)
    {
        selectablesLst.add(s);
    }

    public static void addMouseHandler(Node n)
    {
       n.setOnMouseClicked(event -> {
           if (!event.isShiftDown())
           {
               boolean nodeIsSelected = ((Selectable) n).isSelected();
               unselectAll();
               ((Selectable) n).setSelected(!nodeIsSelected);
           }
           if(event.isShiftDown())
           {
               ((Selectable) n).setSelected(!((Selectable) n).isSelected());
           }
       });
    }
}
