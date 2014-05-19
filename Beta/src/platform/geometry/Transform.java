package platform.geometry;

import static java.lang.Math.*;

public class Transform
{
	public static Rectangle transform(Rectangle rectangle, Vector vector)
	{
		double Δx = cos(vector.angle.angle) * vector.magnitude;
		double Δy = sin(vector.angle.angle) * vector.magnitude;
		
		Position point1 = rectangle.pointUpperLeft.clone();
		point1.x += Δx;
		point1.y += Δy;
		Position point2 = rectangle.pointLowerRight.clone();
		point2.x += Δx;
		point2.y += Δy;
		
		return new Rectangle(point1, point2);
	}
	
	public static Position transform(Position point, Vector vector)
	{
		double Δx = cos(vector.angle.angle) * vector.magnitude;
		double Δy = sin(vector.angle.angle) * vector.magnitude;
		
		Position transformedPoint = point.clone();
		transformedPoint.x += Δx;
		transformedPoint.y += Δy;
		
		return transformedPoint;
	}
}