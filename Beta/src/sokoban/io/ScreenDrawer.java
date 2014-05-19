package sokoban.io;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.TreeMap;

import platform.core.GameObject;
import platform.core.Map;
import platform.core.Movement;
import platform.geometry.*;

public class ScreenDrawer
{
	private Map map;
	private Rectangle drawArea;
	private Rectangle updateArea;
	private Image background;
	
	public ScreenDrawer(Map map, Rectangle area, Image background)
	{
		this.map = map;
		this.drawArea = area;
		this.updateArea = area;
		this.background = background;
	}
	
	
	// When movement takes place, this registers the affected locations to be redrawn.
	public void registerMovement(Movement movement)
	{
		ArrayList<Rectangle> areas = movement.getAffectedAreas();
		
		for (Rectangle area : areas)
		{
			if (updateArea == null)
				updateArea = area;
			else if (area.pointUpperLeft.x < updateArea.pointUpperLeft.x)
				updateArea.pointUpperLeft.x = area.pointUpperLeft.x;
			else if (area.pointUpperLeft.y < updateArea.pointUpperLeft.y)
				updateArea.pointUpperLeft.y = area.pointUpperLeft.y;
			else if (area.pointLowerRight.x > updateArea.pointLowerRight.x)
				updateArea.pointLowerRight.x = area.pointLowerRight.x;
			else if (area.pointLowerRight.y > updateArea.pointLowerRight.y)
				updateArea.pointLowerRight.y = area.pointLowerRight.y;
		}
	}
	
	// Draws all objects which need to be drawn (those which have been moved recently).
	public boolean draw(Graphics g)
	{
		if (updateArea == null) return false;
		ArrayList<GameObject> objects = map.getObjectsInArea(updateArea);
		
		// Give priorities to each object based on its type and sort the drawing order based on these priorities.
		PriorityQueue<GameObject> drawPriorities = new PriorityQueue<GameObject>(objects.size(), new GameObjectPriorityComparator());
		for (GameObject object : objects)
			drawPriorities.add(object);
		
		// Draw the background here.
		g.drawImage(background, updateArea.pointUpperLeft.x, updateArea.pointUpperLeft.y, updateArea.pointLowerRight.x, updateArea.pointLowerRight.y,
				updateArea.pointUpperLeft.x, updateArea.pointUpperLeft.y, updateArea.pointLowerRight.x, updateArea.pointLowerRight.y, null);
		
		for (GameObject current : drawPriorities)
			current.draw(g);
		//this.updateArea = null;
		return true;
	}
	
	private class GameObjectPriorityComparator implements Comparator<GameObject>
	{
		@Override
		public int compare(GameObject object1, GameObject object2)
		{
			return object2.drawPriority() - object1.drawPriority();
		}
	}
}