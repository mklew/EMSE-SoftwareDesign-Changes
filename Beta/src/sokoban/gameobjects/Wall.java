package sokoban.gameobjects;

import java.awt.Graphics;
import java.awt.Image;

import platform.core.GameObject;
import platform.core.Movement;
import platform.geometry.Angle;
import platform.geometry.Position;
import sokoban.io.ImageReader;

public class Wall extends GameObject
{
	private static int height = 30, width = 30;
	private static Image image = ImageReader.getImage("Wall");
	private static int drawPriority = 2;
	
	public Wall(Position position) { super(position); }
	public Wall(int x, int y) { super(x, y); }
	
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
		if (Wall.image != null)
			g.drawImage(Wall.image, this.position.x, this.position.y, width, height, null);
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
		return;
	}
}