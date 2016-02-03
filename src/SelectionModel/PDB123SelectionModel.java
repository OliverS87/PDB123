package SelectionModel;

import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

/**
 * Created by oliver on 02.02.16.
 */
public class PDB123SelectionModel<Selectable> extends MultipleSelectionModel<Selectable> {
    @Override
    public ObservableList<Integer> getSelectedIndices() {
        return null;
    }

    @Override
    public ObservableList<Selectable> getSelectedItems() {
        return null;
    }

    @Override
    public void selectIndices(int index, int... indices) {

    }

    @Override
    public void selectAll() {

    }

    @Override
    public void clearAndSelect(int index) {

    }

    @Override
    public void select(int index) {

    }

    @Override
    public void select(Selectable obj) {

    }

    @Override
    public void clearSelection(int index) {

    }

    @Override
    public void clearSelection() {

    }

    @Override
    public boolean isSelected(int index) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void selectPrevious() {

    }

    @Override
    public void selectNext() {

    }

    @Override
    public void selectFirst() {

    }

    @Override
    public void selectLast() {

    }
}
