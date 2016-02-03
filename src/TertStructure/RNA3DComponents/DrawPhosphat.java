package TertStructure.RNA3DComponents;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Created by oliver on 25.01.16.
 * Simple representation of a phosphate atom
 */
public class DrawPhosphat extends Sphere {
    private static  double ATOMRADIUS = 0.98;
    public DrawPhosphat(Point3D phosphatCoordinates){
        // Do not show phosphate if coordinates are undefined
        if (phosphatCoordinates == null){
            this.setVisible(false);
        }
        else{
        this.setRadius(ATOMRADIUS/2);
        this.setTranslateX(phosphatCoordinates.getX());
        this.setTranslateY(phosphatCoordinates.getY());
        this.setTranslateZ(phosphatCoordinates.getZ());
        this.setMaterial(new PhongMaterial(Color.PURPLE));
    }}
}
