package TertStructure.RNA3DComponents;

import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;

/**
 * Created by oliver on 15.12.15.
 * Create 3D mesh of a Purin from Point3Ds
 */
public class DrawPurin {

    private ArrayList<Point3D> purinXYZ;

    public DrawPurin(Point3D N1,Point3D C2, Point3D N3,Point3D C4, Point3D N9,Point3D C8, Point3D N7,Point3D C5,Point3D C6){
        purinXYZ = new ArrayList<>();
        purinXYZ.add(N1);
        purinXYZ.add(C2);
        purinXYZ.add(N3);
        purinXYZ.add(C4);
        purinXYZ.add(N9);
        purinXYZ.add(C8);
        purinXYZ.add(N7);
        purinXYZ.add(C5);
        purinXYZ.add(C6);
    }
    public MeshView getPurin()
    {
        float[] points = new float[3*9];
        // Ordering:
        // N1 C2 N3 C4 N9 C8 N7 C5 C6
        for (int i = 0; i < 9; i++){
            Point3D curr = purinXYZ.get(i);
            points[3*i+0]=(float)curr.getX();
            points[3*i+1]=(float)curr.getY();
            points[3*i+2]=(float)curr.getZ();
        }
        float[] texCoords = {
                0,0.2f, // t0
                0,0.8f, // t1
                0.2f,1, // t2
                0.5f,0.8f, // t3
                0.8f,1, // t4
                1,0.5f, // t5
                0.8f,0, // t6
                0.5f,0.2f,  // t7
                0.2f,0      // t8

        };
        int[] faces = {
                0,0,7,7,8,8, // f1 ccw (front)
                0,0,8,8,7,7, // f1 cw (back)
                0,0,3,3,7,7, // f2 ccw (front)
                0,0,7,7,3,3, // f2 cw (back)
                0,0,1,1,3,3, // f3 ccw (front)
                0,0,3,3,1,1, // f3 cw (back)
                1,1,2,2,3,3, // f4 ccw (front)
                1,1,3,3,2,2, // f4 cw (back)
                3,3,4,4,5,5, // f5 ccw (front)
                3,3,5,5,4,4, // f5 cw (back)
                3,3,5,5,7,7, // f6 ccw (front)
                3,3,7,7,5,5, // f6 cw (back)
                7,7,5,5,6,6, // f7 ccw (front)
                7,7,6,6,5,5, // f7 cw (back)

        };
        int[] smoothing = {1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        TriangleMesh sugarMesh = new TriangleMesh();
        sugarMesh.getPoints().addAll(points);
        sugarMesh.getTexCoords().addAll(texCoords);
        sugarMesh.getFaces().addAll(faces);
        sugarMesh.getFaceSmoothingGroups().addAll(smoothing);
        MeshView purinView = new MeshView(sugarMesh);
        return purinView;
    }
}
