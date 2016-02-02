package PrimStructure;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by oliver on 02.02.16.
 * Text representation of a single nucleotide letter
 * Adds selection functionality to a letter
 */
public class NucleotideLetter extends Text
{
    private String letter;
    private BooleanProperty isSelected;
    private String ntId;
    private  Color ltColor;

    public NucleotideLetter(String letter, BooleanProperty isSelected, int resIndex) {
        this.letter = letter;
        this.isSelected = isSelected;
        this.setText(letter);
        letterColor();
        this.setFill(ltColor);
        isSelectedListener();
        mouseClickListener();
        setFont(Font.font ("Verdana", 20));
        Tooltip.install(this, new Tooltip(ntId + " " + resIndex));
    }
    private void mouseClickListener()
    {
        this.setOnMouseClicked(event -> {
            isSelected.setValue(!isSelected.getValue());
        });
    }

    private void isSelectedListener()
    {
        isSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue)
            {
                this.setFill(ltColor.invert());
                setFont(Font.font ("Verdana", 22));
            }
            else
            {
                this.setFill(ltColor);
                setFont(Font.font ("Verdana", 20));
            }
        });
    }
    private void letterColor()
    {
        ltColor = Color.BLACK;
        switch (letter) {
            case ("A"):
                ntId = "Adenine";
                ltColor = Color.DARKGREEN;
                break;
            case ("C"):
                ntId = "Cytosine";
                ltColor = Color.YELLOW;
                break;
            case ("G"):
                ntId = "Guanine";
                ltColor = Color.CYAN;
                break;
            case ("T"):
                ntId = "Thymine";
                ltColor = Color.HOTPINK;
                break;
            case ("U"):
                ntId = "Uracile";
                ltColor = Color.HOTPINK;
                break;
         }
    }
}
