package TertStructure.RNA3DComponents;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Created by oliver on 25.01.16.
 * Simple representation of an oxygen atom
 */
public class DrawOxygen extends Sphere
{
    private static  double ATOMRADIUS = 0.48;
    public DrawOxygen(Point3D oxyCoordinates)
    {
        // Do not show oxygen if coordinate is undefined
        if (oxyCoordinates==null){
            this.setVisible(false);
        }
        else{
        this.setTranslateX(oxyCoordinates.getX());
        this.setTranslateY(oxyCoordinates.getY());
        this.setTranslateZ(oxyCoordinates.getZ());
        this.setMaterial(new PhongMaterial(Color.RED));
        this.setRadius(ATOMRADIUS/2);
            this.setOnMouseClicked(event -> System.out.println("Oxy click"));
    }}
}
