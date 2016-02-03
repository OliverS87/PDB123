package TertStructure.RNA3DComponents;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by oliver on 26.01.16.
 */
public class DrawHydrogenBond extends DrawLine {
    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

    public BooleanProperty isSelectedProperty() {
        return isSelected;
    }

    public DrawHydrogenBond(Point3D start, Point3D end) {
        super(start, end, 4);
        PhongMaterial hdbPhong = new PhongMaterial();

        Image img = new Image("images/HDB.png");
        hdbPhong.setBumpMap(img);
        hdbPhong.setDiffuseMap(img);
        hdbPhong.setSpecularColor(Color.TRANSPARENT);
        this.setMaterial(hdbPhong);
    }


}
