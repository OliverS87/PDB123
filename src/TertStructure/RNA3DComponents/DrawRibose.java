package TertStructure.RNA3DComponents;

import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;

/**
 * Created by oliver on 14.12.15.
 */
public class DrawRibose {
    private ArrayList<Point3D> riboseXYZ;

    public DrawRibose(Point3D C1, Point3D C2, Point3D C3, Point3D C4, Point3D O4)
    {
        riboseXYZ=new ArrayList<>();
        riboseXYZ.add(C1);
        riboseXYZ.add(C2);
        riboseXYZ.add(C3);
        riboseXYZ.add(C4);
        riboseXYZ.add(O4);

    }
    public MeshView getRibose()
    {
        float[] points = new float[3*5];
        // Ordering:
        // C1 C2 C3 C4 O4
        for (int i = 0; i < 5; i++){
            Point3D curr = riboseXYZ.get(i);
            points[3*i+0]=(float)curr.getX();
            points[3*i+1]=(float)curr.getY();
            points[3*i+2]=(float)curr.getZ();
        }
        float[] texCoords = {
                0,0.5f, // t0
                0.1f,1, // t1
                0.9f,1, // t2
                1,0.5f, // t3
                0.5f,0  // t4
        };
        int[] faces = {
                0,0,3,3,4,4, // f1 ccw (front)
                0,0,4,4,3,3, // f1 cw (back)
                0,0,1,1,3,3, // f2 ccw (front)
                0,0,3,3,1,1, // f2 cw (back)
                1,1,2,2,3,3, // f3 ccw (back)
                1,1,3,3,2,2  // f3 cw (back)
        };

        // insert smoothing here
        int[] smoothing = {1, 1, 1,1, 1, 1};
        TriangleMesh riboseMesh = new TriangleMesh();
        riboseMesh.getPoints().addAll(points);
        riboseMesh.getTexCoords().addAll(texCoords);
        riboseMesh.getFaces().addAll(faces);
        riboseMesh.getFaceSmoothingGroups().addAll(smoothing);
        MeshView riboseView = new MeshView(riboseMesh);

        return riboseView;
    }
}
