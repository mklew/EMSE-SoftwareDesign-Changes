package platform.core;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.TreeSet;

import platform.geometry.Angle;
import platform.geometry.Position;
import platform.geometry.Rectangle;
import platform.geometry.Vector;
import platform.geometry.Angle.Direction;
import sokoban.gameobjects.Player;
import sokoban.gameobjects.Target;
import sokoban.io.MapReader;
import sokoban.io.ScreenDrawer;


public class Map
{
	// Sokoban‐Specific Settings and Variables
	private static final int x = 600, y = 600;
	private static final int tileSize = 30; // This is the size of each square the player my occupy.
	private Player player = null;
	private ArrayList<Target> targets = new ArrayList<Target>();
	
	
	private LinkedList<GameObject> objectList = new LinkedList<GameObject>();
	private TreeMap<Integer, LinkedList<GameObject>> horizontalPositions = new TreeMap<Integer, LinkedList<GameObject>>();
	private TreeMap<Integer, LinkedList<GameObject>> verticalPositions = new TreeMap<Integer, LinkedList<GameObject>>();
	
	private ArrayList<Movement> movements = new ArrayList<Movement>();
	private ScreenDrawer drawer;
	
	public Map(String filename) throws Exception
	{
		MapReader reader = new MapReader(filename, tileSize, new Rectangle(new Position(0,0), new Position(x,y)));
		
		Collection<GameObject> gameObjects = reader.getGameObjects();
		for (GameObject object : gameObjects)
		{
			// This identifies which object is the player, and provides the player with this map (needed for movements).
			if (object instanceof Player)
			{
				if (player == null)
				{
					player = (Player) object;
					player.map = this;
				}
				else throw new java.io.IOException();
			}
			else if (object instanceof Target)
			{
				targets.add((Target) object);
			}
			addObject(object);
		}
		
		if (player == null) throw new java.io.IOException();
		
		drawer = new ScreenDrawer(this, new Rectangle(new Position(0,0), new Position(x,y)), reader.getBackground());
	}
	
	// Receives a request to attempt a movement as provided by the user interface.
	public void move(Angle angle)
	{
		this.player.move(new Vector(angle, tileSize));
	}
	
	// Actually conducts a movement which has been built together by the player object to be executed.
	public void move(Movement movement)
	{
		movement.execute(this);
		this.movements.add(movement);
		drawer.registerMovement(movement);
		
		// Trigger the postMovementActions of all objects affected by these movements.
		ArrayList<Rectangle> affectedAreas = movement.getAffectedAreas();
		ArrayList<GameObject> affectedObjects = new ArrayList<GameObject>();
		for (Rectangle area : affectedAreas)
		{
			ArrayList<GameObject> areaObjects = getObjectsInArea(area);
			for (GameObject object : areaObjects)
			{
				if (!affectedObjects.contains(object))
					affectedObjects.add(object);
			}
		}
		
		for (GameObject object : affectedObjects)
			object.postMovementAction(movement);
	}
	
	// Receives user requests through the user interface to reverse the last movement.
	public void undoMovement()
	{
		if (movements.size() < 1) return;
		int lastMovement = movements.size() - 1;
		movements.get(lastMovement).undo(this);
		this.movements.remove(lastMovement);
	}
	
	// Receives a request to draw the map and passes this request on to the ScreenDrawer object.
	public boolean draw(Graphics g)
	{
		return drawer.draw(g);
	}
	
	public ArrayList<GameObject> getObjectsInArea(Rectangle area)
	{
		ArrayList<GameObject> horizontalOverlaps = new ArrayList<GameObject>();
		HashSet<GameObject> verticalOverlaps = new HashSet<GameObject>();
		
		// Find horizontal overlaps.
		NavigableMap<Integer, LinkedList<GameObject>> overlapMap = horizontalPositions.subMap(area.pointUpperLeft.x, true, area.pointLowerRight.x, true);
		Collection<LinkedList<GameObject>> overlapLists = overlapMap.values();
		for (LinkedList<GameObject> currentObjects : overlapLists)
		{
			for (GameObject current : currentObjects)
			{
				if (!horizontalOverlaps.contains(current)) horizontalOverlaps.add(current);
			}
		}
		
		// Find vertical overlaps.
		overlapMap = verticalPositions.subMap(area.pointUpperLeft.y, true, area.pointLowerRight.y, true);
		overlapLists = overlapMap.values();
		for (LinkedList<GameObject> currentObjects : overlapLists)
		{
			for (GameObject current : currentObjects)
				if (!verticalOverlaps.contains(current)) verticalOverlaps.add(current);
		}
		
		ArrayList<GameObject> overlaps = new ArrayList<GameObject>();
		// For a possibility to be a real overlap, overlapping must take place on both axes.
		for (GameObject possibility : horizontalOverlaps)
		{
			if (verticalOverlaps.contains(possibility)) overlaps.add(possibility);
		}
		
		return overlaps;
	}
	
	// Adds a new object to the map.
	public void addObject(GameObject object)
	{
		Rectangle area = object.getArea();
		insertIntoPositionTree(horizontalPositions, area.pointUpperLeft.x, object);
		insertIntoPositionTree(verticalPositions, area.pointUpperLeft.y, object);
		insertIntoPositionTree(horizontalPositions, area.pointLowerRight.x, object);
		insertIntoPositionTree(verticalPositions, area.pointLowerRight.y, object);
		objectList.add(object);
	}
	
	// Removes an object from the map.
	public void removeObject(GameObject object)
	{
		Rectangle area = object.getArea();
		removeFromPositionTree(horizontalPositions, area.pointUpperLeft.x, object);
		removeFromPositionTree(verticalPositions, area.pointUpperLeft.y, object);
		removeFromPositionTree(horizontalPositions, area.pointLowerRight.x, object);
		removeFromPositionTree(verticalPositions, area.pointLowerRight.y, object);
		objectList.remove(object);
	}
	
	// Moves an object already in the map to a new location in the map.
	public void moveObject(GameObject object, Position newPosition)
	{
		// First remove the old coordinate pointers.
		Rectangle area = object.getArea();
		removeFromPositionTree(horizontalPositions, area.pointUpperLeft.x, object);
		removeFromPositionTree(verticalPositions, area.pointUpperLeft.y, object);
		removeFromPositionTree(horizontalPositions, area.pointLowerRight.x, object);
		removeFromPositionTree(verticalPositions, area.pointLowerRight.y, object);
		
		// Then add the new ones.
		object.position = newPosition.clone();
		area = object.getArea();
		insertIntoPositionTree(horizontalPositions, area.pointUpperLeft.x, object);
		insertIntoPositionTree(verticalPositions, area.pointUpperLeft.y, object);
		insertIntoPositionTree(horizontalPositions, area.pointLowerRight.x, object);
		insertIntoPositionTree(verticalPositions, area.pointLowerRight.y, object);
	}
	
	// Inserts a game object into one of the position trees used to identify objects at a location.
	private void insertIntoPositionTree(TreeMap<Integer, LinkedList<GameObject>> tree, int coordinate, GameObject object)
	{
		LinkedList<GameObject> current;
		// Check if the coordinate value is already in the tree.
		if (tree.containsKey(coordinate)) current = tree.get(coordinate);
		else
		{
			current = new LinkedList<GameObject>();
			tree.put(coordinate, current);
		}
		
		current.add(object);
	}

	// Removes a game object from one of the position trees used to identify objects at a location.
	private void removeFromPositionTree(TreeMap<Integer, LinkedList<GameObject>> tree, int coordinate, GameObject object)
	{
			LinkedList<GameObject> objectListAtCoordinate = tree.get(coordinate);
			objectListAtCoordinate.remove(object);
			if (objectListAtCoordinate.isEmpty())
				tree.remove(coordinate);
	}

	// Returns whether the current map has been beaten.
	public boolean solved()
	{
		boolean solved = true;
		for (Target current : this.targets)
			solved = solved && current.solved(this);
		return solved;
	}
}