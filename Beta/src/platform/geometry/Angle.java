package platform.geometry;

public class Angle
{
	public final double angle;
	
	public enum Direction { Left, Up, Right, Down }
	
	public Angle(double angle)
	{
		this.angle = angle;
	}
	
	public Angle(Direction direction)
	{
		if (direction == Direction.Right) angle = 0;
		else if (direction == Direction.Left) angle = Math.PI;
		// The following two are reversed due to the change in Y‐axis used by computer graphics.
		else if (direction == Direction.Up) angle = Math.PI * 1.5;
		else angle = Math.PI / 2;
	}
}