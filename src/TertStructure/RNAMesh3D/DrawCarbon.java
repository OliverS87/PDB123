package TertStructure.RNAMesh3D;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Created by oliver on 25.01.16.
 * Simple representation of a carbon atom
 */
public class DrawCarbon extends Sphere {
    private static double ATOMRADIUS = 0.67;
    public DrawCarbon(Point3D carbonXYZ){
        // Do not show carbon if coordinate is undefined
        if (carbonXYZ==null){
            this.setVisible(false);
        }
        else{
        this.setRadius(ATOMRADIUS);
        this.setTranslateX(carbonXYZ.getX());
        this.setTranslateY(carbonXYZ.getY());
        this.setTranslateZ(carbonXYZ.getZ());
        this.setMaterial(new PhongMaterial(Color.CHOCOLATE));
        }
    }
}
