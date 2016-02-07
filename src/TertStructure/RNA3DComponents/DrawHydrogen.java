package TertStructure.RNA3DComponents;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Created by oliver on 25.01.16.
 * Simple representation of a hydrogen atom
 */
public class DrawHydrogen extends Sphere {
    private static double ATOMRADIUS = 0.53;

    public DrawHydrogen(Point3D hydrogenXYZ) {
        // if coordinate is not defined
        // do not show hydrogen
        if (hydrogenXYZ == null) {
            this.setVisible(false);
        } else {
            this.setRadius(ATOMRADIUS / 2);
            this.setTranslateX(hydrogenXYZ.getX());
            this.setTranslateY(hydrogenXYZ.getY());
            this.setTranslateZ(hydrogenXYZ.getZ());
            this.setMaterial(new PhongMaterial(Color.DARKBLUE));
        }

    }
}
