package PrimStructure;

import SelectionModel.PDB123SelectionModel;
import SelectionModel.Selectable;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created by oliver on 02.02.16.
 * Text representation of a single nucleotide letter
 * Adds selection functionality to a letter
 */
public class NucleotideLetter extends Text implements Selectable
{
    private String letter;
    private BooleanProperty isSelected;

    public NucleotideLetter(String letter, BooleanProperty isSelected, int resIndex) {
        this.letter = letter;
        this.isSelected = isSelected;
        this.setText(letter);
        // Underline text when nucleotide is selected
        this.underlineProperty().bind(isSelected);
        setFont(Font.font ("Verdana", FontWeight.BOLD, 24));
        Tooltip.install(this, new Tooltip(getNtFullName() + " " + resIndex));
        // Register instance at PDB123SelectionModel
        // and add MouseHandler
        PDB123SelectionModel.registerSelectable(this);
        PDB123SelectionModel.addMouseHandler(this);

    }



    private String getNtFullName()
    {

        String ntId="";
        switch (letter) {
            case ("A"):
                ntId = "Adenine";

                break;
            case ("C"):
                ntId = "Cytosine";

                break;
            case ("G"):
                ntId = "Guanine";

                break;
            case ("T"):
                ntId = "Thymine";

                break;
            case ("U"):
                ntId = "Uracile";

                break;
         }
        return ntId;
    }

    @Override
    public boolean isSelected() {
        return isSelected.getValue();
    }

    @Override
    public void setSelected(boolean sel) {
    isSelected.setValue(sel);
    }

}
