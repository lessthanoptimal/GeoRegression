package georegression.metric;

import georegression.struct.shapes.RectangleCorner2D_I32;

/**
 *
 *
 * @author Peter Abeles
 */
public class Intersection2D_I32 {
	/**
	 * Checks to see if the two rectangles intersect each other
	 *
	 * @param a Rectangle
	 * @param b Rectangle
	 * @return true if intersection
	 */
	public static boolean intersects( RectangleCorner2D_I32 a , RectangleCorner2D_I32 b ) {
		return( a.x0 < b.x1 && a.x1 > b.x0 && a.y0 < b.y1 && a.y1 > b.y0 );
	}
}
