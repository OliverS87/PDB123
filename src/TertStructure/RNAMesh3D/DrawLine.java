package TertStructure.RNAMesh3D;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Created by oliver on 15.12.15.
 * For unknown reasons there is no simple Line3D class in JavaFX.
 * DrawLine connects two Points3D start and end with a cylindric line.
 * Inspired by: http://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
 */
public class DrawLine extends Cylinder
{
    private Point3D start, end;
    private double radius;

    public DrawLine(Point3D start, Point3D end)
    {
        this.start=start;
        this.end=end;
        this.radius=0.04;
        createLine();
    }
    public DrawLine(Point3D start, Point3D end, double radius)
    {
        this.start=start;
        this.end=end;
        this.radius=radius;
        createLine();
    }
    public DrawLine(Point3D start, Point3D end, double radius, Color col1)
    {
        this.start=start;
        this.end=end;
        this.radius=radius;
        this.setMaterial(new PhongMaterial(col1));
        createLine();
    }
    public DrawLine(Point3D start, Point3D end, Color col1)
    {
        this.start=start;
        this.end=end;
        this.radius=0.04;
        this.setMaterial(new PhongMaterial(col1));
        createLine();
    }

    private void createLine()
    {

        // If start and/or end are not set, do not proceed
        if(start==null || end ==null)
        {
            this.setVisible(false);
            return;
        }
        // New Cylinder line is centered at origin, pointing into Y-direction
        //Cylinder line = new Cylinder();
        // (1.) Move line to midpoint between start and end
        Point3D midpoint = start.midpoint(end);
        this.getTransforms().add(new Translate(midpoint.getX(), midpoint.getY(), midpoint.getZ()));
        // (2.) Set the height of the Cylinder line
        // Calculate the difference between start and end
        Point3D difference = end.subtract(start);
        double height = difference.magnitude();
        this.setHeight(height);
        this.setRadius(0.04);
        // (3.) Use some advanced trigonometry to rotate the Cylinder line into the right position
        // Axis of rotation has to be perpendicular to Y-axis, use crossproduct to get Axis
        Point3D rotateAxis = difference.crossProduct(new Point3D(0,1,0));
        // Angle of rotation can be calculated from scalar product of planes defined
        // by the Y-Axis and the plane defined by the two points start and end
        double rotAngle = Math.acos(difference.normalize().dotProduct(new Point3D(0,1,0)));
        this.getTransforms().addAll(new Rotate(-Math.toDegrees(rotAngle), rotateAxis));

        //return line;
    }

}
