/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

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

	/**
	 * Finds the intersection between two rectangles.  If the rectangles don't intersect then false is returned.
	 *
	 * @param a Rectangle
	 * @param b Rectangle
	 * @param result Storage for the found intersection
	 * @return true if intersection
	 */
	public static boolean intersection( RectangleCorner2D_I32 a , RectangleCorner2D_I32 b , RectangleCorner2D_I32 result ) {
		if( !intersects(a,b) )
			return false;

		result.x0 = Math.max(a.x0,b.x0);
		result.x1 = Math.min(a.x1, b.x1);
		result.y0 = Math.max(a.y0,b.y0);
		result.y1 = Math.min(a.y1,b.y1);

		return true;
	}

}
