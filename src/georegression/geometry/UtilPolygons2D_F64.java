package georegression.geometry;

import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.RectangleCorner2D_F64;

/**
 * Various functions related to polygons.
 *
 * @author Peter Abeles
 */
public class UtilPolygons2D_F64 {

	/**
	 * Finds the minimum area bounding rectangle around the quadrilateral.
	 *
	 * @param quad (Input) Quadrilateral
	 * @param rectangle (Output) Minimum area rectangle
	 */
	public static void bounding( Quadrilateral_F64 quad , RectangleCorner2D_F64 rectangle ) {

		rectangle.x0 = Math.min(quad.a.x,quad.b.x);
		rectangle.x0 = Math.min(rectangle.x0,quad.c.x);
		rectangle.x0 = Math.min(rectangle.x0,quad.d.x);

		rectangle.y0 = Math.min(quad.a.y,quad.b.y);
		rectangle.y0 = Math.min(rectangle.y0,quad.c.y);
		rectangle.y0 = Math.min(rectangle.y0,quad.d.y);

		rectangle.x1 = Math.max(quad.a.x,quad.b.x);
		rectangle.x1 = Math.max(rectangle.x1,quad.c.x);
		rectangle.x1 = Math.max(rectangle.x1,quad.d.x);

		rectangle.y1 = Math.max(quad.a.y,quad.b.y);
		rectangle.y1 = Math.max(rectangle.y1,quad.c.y);
		rectangle.y1 = Math.max(rectangle.y1,quad.d.y);
	}
}
