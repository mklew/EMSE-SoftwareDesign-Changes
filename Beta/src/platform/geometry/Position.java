package platform.geometry;

public class Position
{
	public int x, y; // Position in pixels within the map‐space.
	
	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Position clone()
	{
		return new Position(x, y);
	}
	
	public String toString()
	{
		return "(" + this.x + "," + this.y + ")";
	}
}