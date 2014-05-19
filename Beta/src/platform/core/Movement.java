package platform.core;

import java.util.ArrayList;

import platform.geometry.*;
import static platform.geometry.Transform.*;

public class Movement
{
	private class ObjectMovement
	{
		GameObject object;
		Position oldPosition;
		Position newPosition;
		
		public ObjectMovement(GameObject object, Vector movement)
		{
			this.object = object;
			this.oldPosition = object.position.clone();
			this.newPosition = transform(object.position, movement);
		}
	}
	
	private Map map;
	private ArrayList<ObjectMovement> movements = new ArrayList<ObjectMovement>();
	private Vector initialVector;
	
	public Movement(Map map, GameObject object, Vector vector)
	{
		this.map = map;
		movements.add(new ObjectMovement(object, vector));
		this.initialVector = vector;
	}
	
	public void add(GameObject object, Vector vector)
	{
		movements.add(new ObjectMovement(object, vector));
	}
	
	public void execute(Map map)
	{
		for (ObjectMovement currentMovement : movements)
			map.moveObject(currentMovement.object, currentMovement.newPosition);
	}
	
	public void undo(Map map)
	{
		for (ObjectMovement currentMovement : movements)
			map.moveObject(currentMovement.object, currentMovement.oldPosition);
	}
	
	public Map getMap()
	{
		return this.map;
	}
	
	public Vector getInitialVector()
	{
		return this.initialVector;
	}
	
	/* This returns a rectangle containing the entire area affected by this movement. All moved
	 * objects will be located within this rectangle. */
	public ArrayList<Rectangle> getAffectedAreas()
	{
		ArrayList<Rectangle> areas = new ArrayList<Rectangle>();
		
		int height, width;
		for (ObjectMovement movement : movements)
		{
			height = movement.object.getHeight();
			width = movement.object.getWidth();
			Rectangle oldArea = new Rectangle(movement.oldPosition, new Position(movement.oldPosition.x + width, movement.oldPosition.y + height));
			Rectangle newArea = new Rectangle(movement.newPosition, new Position(movement.newPosition.x + width, movement.newPosition.y + height));
			areas.add(oldArea);
			areas.add(newArea);
		}
		
		return areas;
	}
}