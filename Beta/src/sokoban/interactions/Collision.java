package sokoban.interactions;

import java.util.ArrayList;

import platform.core.GameObject;
import platform.core.Map;
import platform.geometry.Rectangle;
import sokoban.gameobjects.*;

public class Collision
{
	public static boolean solid(GameObject object)
	{
		if (object instanceof Player) return true;
		else if (object instanceof Crate) return true;
		else if (object instanceof Wall) return true;
		else return false;
	}
	
	public static ArrayList<GameObject> getSolidObjectsInArea(Map map, Rectangle area)
	{
		ArrayList<GameObject> allObjects = map.getObjectsInArea(area);
		ArrayList<GameObject> solidObjects = new ArrayList<GameObject>();
		
		if (allObjects != null)
		{
			for (GameObject current : allObjects)
				if (solid(current)) solidObjects.add(current);
		}
		else return null;
		
		return solidObjects;
	}
}