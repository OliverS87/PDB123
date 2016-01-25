package TertStructure.RNAMesh3D;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Created by oliver on 25.01.16.
 */
public class DrawNitrogen extends Sphere {
    public DrawNitrogen(Point3D xyz){
        // Do not show nitrogen if coordinate is undefined
        if (xyz==null){
            this.setVisible(false);
        }
        else{
        this.setRadius(0.35*(7./15.));
        this.setTranslateX(xyz.getX());
        this.setTranslateY(xyz.getY());
        this.setTranslateZ(xyz.getZ());
        this.setMaterial(new PhongMaterial(Color.VIOLET));
    }}
}
