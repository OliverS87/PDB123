package TertStructure.RNA3DComponents;

import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;

/**
 * Created by oliver on 15.12.15.
 * Create 3D mesh of a Purin from Point3Ds
 */
public class DrawPyrimidine {

    private ArrayList<Point3D> pyrimidineXYZ;

    public DrawPyrimidine(Point3D N1, Point3D C2, Point3D N3, Point3D C4, Point3D C5, Point3D C6) {
        pyrimidineXYZ = new ArrayList<>();
        pyrimidineXYZ.add(N1);
        pyrimidineXYZ.add(C2);
        pyrimidineXYZ.add(N3);
        pyrimidineXYZ.add(C4);
        pyrimidineXYZ.add(C5);
        pyrimidineXYZ.add(C6);
    }

    public MeshView getPyrimidine() {
        float[] points = new float[3 * 6];
        // Ordering:
        // N1 C2 N3 C4 C5 C6
        for (int i = 0; i < 6; i++) {
            Point3D curr = pyrimidineXYZ.get(i);
            points[3 * i + 0] = (float) curr.getX();
            points[3 * i + 1] = (float) curr.getY();
            points[3 * i + 2] = (float) curr.getZ();
        }
        float[] texCoords = {
                0, 0.8f, // t0
                0.5f, 1, // t1
                1, 0.8f, // t2
                1, 0.2f, // t3
                0.5f, 0, // t4
                0, 0.2f, // t5

        };
        int[] faces = {
                0, 0, 1, 1, 2, 2, // f1 ccw (front)
                0, 0, 2, 2, 1, 1, // f1 cw (back)
                0, 0, 2, 2, 5, 5, // f2 ccw (front)
                0, 0, 5, 5, 2, 2, // f2 cw (back)
                2, 2, 3, 3, 5, 5, // f3 ccw (front)
                2, 2, 5, 5, 3, 3, // f3 cw (back)
                5, 5, 3, 3, 4, 4, // f4 ccw (front)
                5, 5, 4, 4, 3, 3, // f4 cw (back)
        };
        int[] smoothing = {1, 1, 1, 1, 1, 1, 1, 1};
        TriangleMesh pyrimdineMesh = new TriangleMesh();
        pyrimdineMesh.getPoints().addAll(points);
        pyrimdineMesh.getTexCoords().addAll(texCoords);
        pyrimdineMesh.getFaces().addAll(faces);
        pyrimdineMesh.getFaceSmoothingGroups().addAll(smoothing);
        MeshView pyrimidineView = new MeshView(pyrimdineMesh);
        return pyrimidineView;
    }
}
