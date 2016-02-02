package TertStructure.Center3D;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

/**
 * Created by oliver on 28.01.16.
 */
public class Center3D {
    private ObservableList<Point3D> pointList;
    private Point3D centerPoint;

    public Center3D() {
        centerPoint = new Point3D(0,0,0);
        this.pointList = FXCollections.observableArrayList();
        pointList.addListener(new WeakListChangeListener<Point3D>(c -> calculateCenter()));
    }
    public void addPoint(Point3D newPoint)
    {
        pointList.add(newPoint);
    }
    public Point3D getCenterPoint(){return centerPoint;}
    public void clear(){pointList.clear();}
    private void calculateCenter()
    {
        double avgX, avgY, avgZ;
        avgX=avgY=avgZ=0;
        for (Point3D p:pointList
             ) {
            avgX += p.getX();
            avgY += p.getY();
            avgZ += p.getZ();
        }
        avgX/=pointList.size();
        avgY/=pointList.size();
        avgZ/=pointList.size();
        centerPoint = new Point3D(avgX,avgY,avgZ);

    }
    public static void main (String[] args)
    {
        System.out.println(Rotate.X_AXIS.toString());
    }

}
