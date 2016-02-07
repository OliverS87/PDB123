package SelectionModel;

import javafx.scene.Node;

import java.util.ArrayList;

/**
 * Created by oliver on 03.02.16.
 * Mouse selection model for PDB123.
 * Objects that are supposed to be selectable
 * have to implement the Selectable interface.
 * In addition, all object have to register themselves
 * at the PDB123SelectionModel
 * -> registerSelectable()
 * References to all selectable objects are stored in an ArrayList
 * The same mouseHandler is added to all selectable objects.
 */
public class PDB123SelectionModel
{
    // ArrayList to store all selectable objects
    private static ArrayList<Selectable> selectablesLst;
    // Initialize/Reset the selection model
    public static void initSelectionModel()
    {
        selectablesLst = new ArrayList<>();
    }
    // Unselect all registered selectable objects
    public static void unselectAll()
    {
        for (Selectable s: selectablesLst
             ) {
            s.setSelected(false);
        }
    }
    // Register a new selectable object at this selection model
    public static void registerSelectable(Selectable s)
    {
        selectablesLst.add(s);
    }
    // Add mouse handler to a selectable object
    // Two different actions, depending on status of shift-key:
    public static void addMouseHandler(Node n)
    {
       // If shift is NOT pressed:
        // Unselect all objects in selectablesLst
        // Reverse the selection status of the clicked object
        n.setOnMouseClicked(event -> {
           if (!event.isShiftDown())
           {
               boolean nodeIsSelected = ((Selectable) n).isSelected();
               unselectAll();
               ((Selectable) n).setSelected(!nodeIsSelected);
           }
            // If shift is pressed:
            // Keep the selection status of all other selectable objects in list
            // Reverse the selection status of the clicked object
           if(event.isShiftDown())
           {
               ((Selectable) n).setSelected(!((Selectable) n).isSelected());
           }
       });
    }
}
