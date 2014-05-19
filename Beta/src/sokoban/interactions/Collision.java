package sokoban.interactions;

import platform.core.GameObject;
import platform.core.Map;
import platform.geometry.Rectangle;
import sokoban.gameobjects.Crate;
import sokoban.gameobjects.Dog;
import sokoban.gameobjects.Player;
import sokoban.gameobjects.Wall;

import java.util.ArrayList;

public class Collision {
    public static boolean solid (GameObject object) {
        if (object instanceof Player) return true;
        else if (object instanceof Crate) return true;
        else if (object instanceof Wall) return true;
        else if (object instanceof Dog) return true; // THIS IS SO BAD IT HURTS
        else return false;
    }

    public static ArrayList<GameObject> getSolidObjectsInArea (Map map, Rectangle area) {
        ArrayList<GameObject> allObjects = map.getObjectsInArea(area);
        ArrayList<GameObject> solidObjects = new ArrayList<GameObject>();

        if (allObjects != null) {
            for (GameObject current : allObjects)
                if (solid(current)) solidObjects.add(current);
        } else return null;

        return solidObjects;
    }

    public static ArrayList<GameObject> getTargetObjectsInArea (Map map, Rectangle area) {
        ArrayList<GameObject> allObjects = map.getObjectsInArea(area);
        ArrayList<GameObject> targetObjects = new ArrayList<GameObject>();

        if (allObjects != null) {
            for (GameObject current : allObjects)
                if (current.isTarget()) targetObjects.add(current);
        } else return targetObjects;

        return targetObjects;
    }
}