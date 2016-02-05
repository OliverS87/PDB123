package PrimStructure;

import SelectionModel.PDB123SelectionModel;
import SelectionModel.Selectable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
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
    private String ntId;
    private BooleanProperty greyScaleOnHL = new SimpleBooleanProperty(false);


    public NucleotideLetter(String letter, BooleanProperty isSelected, int resIndex) {
        this.letter = letter;
        this.isSelected = isSelected;
        this.setText(letter);
        this.underlineProperty().bind(isSelected);
        letterColor();

        //mouseClickListener();
        PDB123SelectionModel.registerSelectable(this);
        PDB123SelectionModel.addMouseHandler(this);
        setFont(Font.font ("Verdana", FontWeight.BOLD, 24));
        Tooltip.install(this, new Tooltip(ntId + " " + resIndex));
    }
    //private void mouseClickListener()
    //{
      //  this.setOnMouseClicked(event -> {
        //    isSelected.setValue(!isSelected.getValue());
        //});
    //}


    private void letterColor()
    {

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
    }

    @Override
    public boolean isSelected() {
        return isSelected.getValue();
    }

    @Override
    public void setSelected(boolean sel) {
    isSelected.setValue(sel);
    }

    @Override
    public void setHLGreyscale(boolean hl) {
        greyScaleOnHL.setValue(hl);
    }
}
