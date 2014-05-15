/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package georegression.metric;

import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Polygon2D_I32;
import georegression.struct.shapes.Rectangle2D_I32;
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

	/**
	 * Checks to see if the point is contained inside the convex polygon.  If the
	 * point is an the polygon's perimeter it is considered to NOT be inside.
	 *
	 * @param polygon Convex polygon. Not modified.
	 * @param pt Point. Not modified.
	 * @return True if the point is contained inside the polygon.
	 */
	// Ported from internet code 12/2011
	// http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
	public static boolean containConvex( Polygon2D_I32 polygon , Point2D_I32 pt )
	{
		final int N = polygon.size();

		boolean c = false;
		for (int i = 0, j = N-1; i < N; j = i++) {
			Point2D_I32 a = polygon.vertexes.data[i];
			Point2D_I32 b = polygon.vertexes.data[j];

			if ( ((a.y>pt.y) != (b.y>pt.y)) && (pt.x < (b.x-a.x) * (pt.y-a.y) / (b.y-a.y) + a.x) )
				c = !c;
		}
		return c;
	}

	/**
	 * Checks to see if the point is contained inside the concave polygon.
	 *
	 * NOTE: Points which lie along the perimeter may or may not be considered as inside
	 *
	 * @param polygon Convex polygon. Not modified.
	 * @param pt Point. Not modified.
	 * @return True if the point is contained inside the polygon.
	 */
	public static boolean containConcave( Polygon2D_I32 polygon , Point2D_I32 pt )
	{
		final int N = polygon.size();

		int left=0;
		int right=0;
		for (int i = 0; i < N-1; i++) {
			Point2D_I32 a = polygon.vertexes.data[i];
			Point2D_I32 b = polygon.vertexes.data[i+1];

			if( (pt.y >= a.y && pt.y < b.y) || (pt.y >= b.y && pt.y < a.y) ) {
				// location of line segment along x-axis at y = pt.y
				double x = b.y==a.y ? pt.x : (pt.y-a.y)*(b.x-a.x)/(double)(b.y-a.y) + a.x;

				if( x <= pt.x )
					left++;
				else if( x > pt.x )
					right++;
			}
		}

		Point2D_I32 a = polygon.vertexes.data[N-1];
		Point2D_I32 b = polygon.vertexes.data[0];

		if( (pt.y >= a.y && pt.y < b.y) || (pt.y >= b.y && pt.y < a.y) ) {
			// location of line segment along x-axis at y = pt.y
			double x = b.y==a.y ? pt.x : (pt.y-pt.y)*(b.x-a.x)/(double)(b.y-a.y) + a.x;

			if( x <= pt.x )
				left++;
			else if( x > pt.x )
				right++;
		}

		return (left % 2 == 1 && right % 2 == 1);
	}

	/**
	 * True if the point is contained inside the rectangle
	 *
	 * @param a Rectangle
	 * @param x x-coordinate of point
	 * @param y y-coordinate of point
	 * @return true if the point is inside and false it is not
	 */
	public static boolean contains( Rectangle2D_I32 a, int x, int y ) {
		if( a.getX() <= x && a.getX() + a.getWidth() > x ) {
			return a.getY() <= y && a.getY() + a.getHeight() > y;
		}
		return false;
	}

}
