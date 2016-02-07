package TertStructure.RNA3DComponents;

import SelectionModel.PDB123SelectionModel;
import SelectionModel.Selectable;
import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point3D;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by oliver on 26.01.16.
 * Selectable line representing a 3D H-Bond
 * Has references to the two 3D nucleotides it
 * is connecting
 */
public class DrawHydrogenBond extends DrawLine implements Selectable {
    private BooleanProperty isSelected;
    private Color unselectedColor = Color.web("#99ddff");
    private Color selectedColor = Color.web("#cc0000");

    @Override
    public boolean isSelected() {
        return isSelected.getValue();
    }

    @Override
    public void setSelected(boolean sel) {
        this.isSelected.setValue(sel);
    }


    public DrawHydrogenBond(Point3D start, Point3D end, PDBNucleotide nt1, PDBNucleotide nt2, BooleanProperty isSelected) {
        super(start, end, 4);
        this.isSelected = isSelected;
        PhongMaterial hdbPhong = new PhongMaterial();
        // Change color of H-Bond depending on  selection status
        hdbPhong.diffuseColorProperty().bind(Bindings.when(isSelected).then(selectedColor).otherwise(unselectedColor));
        this.setMaterial(hdbPhong);
        // Register Hydrogenbond at mouse selection model
        PDB123SelectionModel.registerSelectable(this);
        PDB123SelectionModel.addMouseHandler(this);
        // Set tooltip
        Tooltip.install(this, new Tooltip("H-Bond between " + nt1.getType() + nt1.getResIndex() + " and " + nt2.getType() + nt2.getResIndex()));
        // Set nt1 and nt2 to "selected" when this h-bond is selected
        addHBondSelectionListener(nt1, nt2);
    }

    // Set nt1 and nt2 to "selected" when this h-bond is selected
    private void addHBondSelectionListener(PDBNucleotide nt1, PDBNucleotide nt2) {
        isSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                nt1.setSelected(true);
                nt2.setSelected(true);
            }
        });
    }


}
