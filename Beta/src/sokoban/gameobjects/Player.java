package sokoban.gameobjects;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import platform.core.GameObject;
import platform.core.Map;
import platform.core.Movement;
import platform.geometry.Angle;
import platform.geometry.Angle.Direction;
import platform.geometry.Position;
import platform.geometry.Rectangle;
import platform.geometry.Vector;
import sokoban.interactions.Collision;
import sokoban.io.ImageReader;
import static platform.geometry.Transform.*;


public class Player extends GameObject
{
	public Map map = null; // Player‐Specific
	
	private static int height = 30, width = 30;
	private static int drawPriority = 1;
	private Image image = ImageReader.getImage("PersonDown");
	
	public Player(Position position) { super(position); }
	public Player(int x, int y) { super(x, y); }
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void move(Vector vector)
	{
		Rectangle movementArea = transform(this.getArea(), vector);
		ArrayList<GameObject> solidObjects = Collision.getSolidObjectsInArea(map, movementArea);
		
		if (solidObjects.isEmpty())
		{
			Movement movement = new Movement(map, this, vector);
			map.move(movement);
			return;
		}
		
		Movement movement = new Movement(map, this, vector);
		boolean execute = true;
		for (GameObject object : solidObjects)
			execute = execute && object.movementQuery(movement);
		if (execute) map.move(movement);
	}
	
	public void draw(Graphics g)
	{
		if (this.image != null)
			g.drawImage(this.image, this.position.x, this.position.y, width, height, null);
	}
	
	public int drawPriority()
	{
		return drawPriority;
	}
	
	@Override
	public boolean movementQuery(Movement movement)
	{
		return false;
	}
	
	@Override
	public void postMovementAction(Movement movement)
	{
		if (movement.getInitialVector().angle.angle == new Angle(Direction.Right).angle)
			this.image = ImageReader.getImage("PersonRight");
		else if (movement.getInitialVector().angle.angle == new Angle(Direction.Up).angle)
			this.image = ImageReader.getImage("PersonUp");
		else if (movement.getInitialVector().angle.angle == new Angle(Direction.Left).angle)
			this.image = ImageReader.getImage("PersonLeft");
		else if (movement.getInitialVector().angle.angle == new Angle(Direction.Down).angle)
			this.image = ImageReader.getImage("PersonDown");
		return;
	}
}