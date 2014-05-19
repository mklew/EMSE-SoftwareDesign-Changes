package platform.geometry;

public class Rectangle
{
	public final Position pointUpperLeft, pointLowerRight;
	
	public Rectangle(Position pointUpperLeft, Position pointLowerRight)
	{
		this.pointUpperLeft = pointUpperLeft.clone();
		this.pointLowerRight = pointLowerRight.clone();
	}
}