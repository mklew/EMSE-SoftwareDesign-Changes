package platform.core;

import platform.geometry.Position;
import platform.geometry.Rectangle;

import java.awt.*;

public abstract class GameObject
{
	public Position position; // Position within the containing map space in pixels.
	
	public GameObject(Position position)
	{
		this.position = position.clone();
	}
	
	public GameObject(int x, int y)
	{
		this.position = new Position(x, y);
	}
	
	public abstract int getHeight();
	public abstract int getWidth();
	public abstract void draw(Graphics g); // Draws the relevant sprites to the passed graphics.
	public abstract int drawPriority(); // Recturns the priority used for drawing. Lower numbers indicate a higher priority (drawn on top).
	public abstract boolean movementQuery(Movement movement); // This is for approving or declining movement requests.
	public abstract void postMovementAction(Movement movement); // This is the action that gets triggered when the object is pushed.
	
	public Rectangle getArea()
	{
		return new Rectangle(position.clone(), new Position(position.x + getWidth() - 1, position.y + getHeight() - 1));
	}

    public boolean isTarget () {
        return false;
    }

    public boolean isGreenPoint () {
        return false;
    }
}