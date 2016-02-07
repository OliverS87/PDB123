package SelectionModel;


/**
 * Created by oliver on 02.02.16.
 * Various elements on the main UI are supposed
 * to be selectable by mouse click. To allow the selection
 * or deselection of multiple elements at the same time,
 * all these elements need to be accessible from the
 * same data structure, e.g. an ArrayList. Therefore, a
 * common interface for this different types of objects is needed.
 * This is provided here.
 */
public interface Selectable {
    // Return if object is currently selected
     boolean isSelected();
    // Set selection status of object to sel
     void setSelected(boolean sel);

}
