package sokoban.gameobjects;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import platform.core.GameObject;
import platform.core.Map;
import platform.core.Movement;
import platform.geometry.Angle;
import platform.geometry.Position;
import sokoban.io.ImageReader;

public class Target extends GameObject
{
	private static int height = 30, width = 30;
	private static Image image = ImageReader.getImage("Target");
	private static int drawPriority = 3;
	
	public Target(Position position) { super(position); }
	public Target(int x, int y) { super(x, y); }
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void draw(Graphics g)
	{
		if (Target.image != null)
			g.drawImage(Target.image, this.position.x, this.position.y, width, height, null);
	}
	
	public int drawPriority()
	{
		return drawPriority;
	}
	
	@Override
	public boolean movementQuery(Movement movement)
	{
		return true;
	}
	
	@Override
	public void postMovementAction(Movement movement)
	{
		return;
	}
	
	public boolean solved(Map map)
	{
		ArrayList<GameObject> overlappingObjects = map.getObjectsInArea(super.getArea());
		for (GameObject currentObject : overlappingObjects)
			if (currentObject instanceof Crate)
				return true;
		return false;
	}
}