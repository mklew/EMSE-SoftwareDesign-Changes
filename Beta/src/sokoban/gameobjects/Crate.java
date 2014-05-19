package sokoban.gameobjects;

import static platform.geometry.Transform.transform;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import platform.core.GameObject;
import platform.core.Movement;
import platform.geometry.Angle;
import platform.geometry.Position;
import platform.geometry.Rectangle;
import sokoban.interactions.Collision;
import sokoban.io.ImageReader;

public class Crate extends GameObject
{
	private static int height = 30, width = 30;
	private static Image image = ImageReader.getImage("Crate");
	private static int drawPriority = 2;
	
	public Crate(Position position) { super(position); }
	public Crate(int x, int y) { super(x, y); }
	
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
		if (Crate.image != null)
			g.drawImage(Crate.image, this.position.x, this.position.y, width, height, null);
	}
	
	public int drawPriority()
	{
		return drawPriority;
	}
	
	@Override
	public boolean movementQuery(Movement movement)
	{
		Rectangle movementArea = transform(this.getArea(), movement.getInitialVector());
		ArrayList<GameObject> solidObjects = Collision.getSolidObjectsInArea(movement.getMap(), movementArea);
		
		if (solidObjects.isEmpty())
		{
			movement.add(this, movement.getInitialVector());
			return true;
		}
		
		return false;
	}
	
	@Override
	public void postMovementAction(Movement movement)
	{
		return;
	}
}