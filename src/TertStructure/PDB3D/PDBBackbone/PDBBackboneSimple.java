package TertStructure.PDB3D.PDBBackbone;

import TertStructure.RNA3DComponents.DrawOxygen;
import TertStructure.RNA3DComponents.DrawPhosphat;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import TertStructure.RNA3DComponents.DrawLine;

/**
 * Created by oliver on 06.02.15.
 * Stores coordinates of an RNA backbone
 * Returns a simplified 3D structure of backbone
 */
public class PDBBackboneSimple extends PDBBackbone {

    public Group getStructure(){
        Group backboneGrp = new Group();
        return backboneGrp;
    }
}
