/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.Rectangle2D_F64;
import georegression.struct.shapes.RectangleLength2D_F64;


/**
 * Functions relating to finding the points at which two shapes intersect with each other.
 *
 * @author Peter Abeles
 */
public class Intersection2D_F64 {

	// todo comment
	// todo how are parallel lines handled?

	/**
	 * <p>
	 * Checks to see if the point is contained inside the convex polygon.  If the
	 * point is an the polygon's perimeter it is considered to NOT be inside.
	 * </p>
	 *
	 * <p>
	 * Clockwise or counter-clockwise order of the polygon does not matter.
	 * </p>
	 *
	 * @param polygon Convex polygon. Not modified.
	 * @param pt Point. Not modified.
	 * @return True if the point is contained inside the polygon.
	 */
	// Ported from internet code 12/2011
	// http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
	public static boolean containConvex( Polygon2D_F64 polygon , Point2D_F64 pt )
	{
		final int N = polygon.size();

		boolean c = false;
		for (int i = 0, j = N-1; i < N; j = i++) {
			Point2D_F64 a = polygon.vertexes.data[i];
			Point2D_F64 b = polygon.vertexes.data[j];
			
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
	public static boolean containConcave( Polygon2D_F64 polygon , Point2D_F64 pt )
	{
		final int N = polygon.size();

		int left=0;
		int right=0;
		for (int i = 0; i < N-1; i++) {
			Point2D_F64 a = polygon.vertexes.data[i];
			Point2D_F64 b = polygon.vertexes.data[i+1];

			if( (pt.y >= a.y && pt.y < b.y) || (pt.y >= b.y && pt.y < a.y) ) {
				// location of line segment along x-axis at y = pt.y
				double x = b.y==a.y ? pt.x : (pt.y-a.y)*(b.x-a.x)/(b.y-a.y) + a.x;

				if( x <= pt.x )
					left++;
				else if( x > pt.x )
					right++;
			}
		}

		Point2D_F64 a = polygon.vertexes.data[N-1];
		Point2D_F64 b = polygon.vertexes.data[0];

		if( (pt.y >= a.y && pt.y < b.y) || (pt.y >= b.y && pt.y < a.y) ) {
			// location of line segment along x-axis at y = pt.y
			double x = b.y==a.y ? pt.x : (pt.y-pt.y)*(b.x-a.x)/(b.y-a.y) + a.x;

			if( x <= pt.x )
				left++;
			else if( x > pt.x )
				right++;
		}

		return (left % 2 == 1 && right % 2 == 1);
	}

	/**
	 * True if the point is contained inside the quadrilateral.
	 *
	 * @param quad quadrilateral
	 * @param pt point
	 * @return true if the point is inside and false if it is not.
	 */
	public static boolean contains( Quadrilateral_F64 quad , Point2D_F64 pt ) {
		return containTriangle(quad.a, quad.b, quad.d, pt) ||
				containTriangle(quad.b, quad.c, quad.d, pt);

	}

	/**
	 * Returns true of the the point is inside the triangle.
	 *
	 * This function is simply an unrolled version of {@link #containConcave(Polygon2D_F64, Point2D_F64)}.
	 *
	 * @param a vertex in triangle
	 * @param b vertex in triangle
	 * @param c vertex in triangle
	 * @param pt Point which is being tested for containment inside of triangle
	 * @return true if the point is inside of triangle
	 */
	public static boolean containTriangle( Point2D_F64 a , Point2D_F64 b , Point2D_F64 c , Point2D_F64 pt )
	{
		boolean ret = false;

		if ( ((a.y>pt.y) != (b.y>pt.y)) && (pt.x < (b.x-a.x) * (pt.y-a.y) / (b.y-a.y) + a.x) )
			ret = true;

		if ( ((b.y>pt.y) != (c.y>pt.y)) && (pt.x < (c.x-b.x) * (pt.y-b.y) / (c.y-b.y) + b.x) )
			ret = !ret;

		if ( ((c.y>pt.y) != (a.y>pt.y)) && (pt.x < (a.x-c.x) * (pt.y-c.y) / (a.y-c.y) + c.x) )
			ret = !ret;

		return ret;
	}

	/**
	 * Finds the point of intersection between two lines and returns the point.
	 *
	 * @param a Line.
	 * @param b Line.
	 * @param ret storage for the point of intersection. If null a new point will be declared.
	 * @return If the two lines intersect it returns the point of intersection.  null if they don't intersect or have infinite intersections.
	 */
	public static Point2D_F64 intersection( LineParametric2D_F64 a, LineParametric2D_F64 b , Point2D_F64 ret ) {
		double t_b = a.getSlopeX() * ( b.getY() - a.getY() ) - a.getSlopeY() * ( b.getX() - a.getX() );
		double bottom = a.getSlopeY() * b.getSlopeX() - b.getSlopeY() * a.getSlopeX();

		if( bottom == 0 )
			return null;

		t_b /= bottom;

		double x = b.getSlopeX() * t_b + b.getX();
		double y = b.getSlopeY() * t_b + b.getY();

		if( ret == null )
			ret = new Point2D_F64();
		ret.set(x,y);
		return ret;
	}

	/**
	 * Finds the point of intersection between two lines.  The point of intersection is specified as a point along
	 * the parametric line 'a'.  (x,y) = (x_0,y_0) + t*(slope_x,slope_y), where 't' is the location returned.
	 *
	 * @param a Line.
	 * @param b Line.
	 * @return The location along 'target'.  If the lines do not intersect or have infinite intersections Double.NaN is returned.
	 */
	public static double intersection( LineParametric2D_F64 a, LineParametric2D_F64 b ) {
		double t_a = b.getSlopeX() * ( a.getY() - b.getY() ) - b.getSlopeY() * ( a.getX() - b.getX() );
		double bottom = b.getSlopeY() * a.getSlopeX() - a.getSlopeY() * b.getSlopeX();

		if( bottom == 0 )
			return Double.NaN;

		return t_a/bottom;
	}

	/**
	 * Finds the point of intersection between two lines segments.
	 *
	 * @param l_0 Line segment.
	 * @param l_1 line segment.
	 * @param ret storage for the point of intersection. If null a new point will be declared.
	 * @return If the two lines intersect it returns the point of intersection.  null if they don't intersect or have infinite intersections.
	 */
	public static Point2D_F64 intersection( LineSegment2D_F64 l_0, LineSegment2D_F64 l_1,
											Point2D_F64 ret ) {

		double a0 = l_0.b.x - l_0.a.x;
		double b0 = l_0.b.y - l_0.a.y;
		double a1 = l_1.b.x - l_1.a.x;
		double b1 = l_1.b.y - l_1.a.y;

		double top = b0 * ( l_1.a.x - l_0.a.x ) + a0 * ( l_0.a.y - l_1.a.y );
		double bottom = a0 * b1 - b0 * a1;

		if( bottom == 0 )
			return null;
		double t_1 = top / bottom;

		// does not intersect along the second line segment
		if( t_1 < 0 || t_1 > 1 )
			return null;

		top = b1 * ( l_0.a.x - l_1.a.x ) + a1 * ( l_1.a.y - l_0.a.y );
		bottom = a1 * b0 - b1 * a0;

		double t_0 = top / bottom;

		// does not intersect along the first line segment
		if( t_0 < 0 || t_0 > 1 )
			return null;

		if( ret == null ) {
			ret = new Point2D_F64();
		}

		ret.set( l_1.a.x + a1 * t_1, l_1.a.y + b1 * t_1 );

		return ret;
	}

	/**
	 * <p>
	 * Finds the intersection of two lines as a 2D point in homogeneous coordinates.  Because the
	 * solution is found in homogeneous coordinates it can even handle parallel lines which "intersect
	 * at infinity".
	 * </p>
	 * 
	 * <p>
	 * A 2D point in homogeneous coordinates is expressed as the triple (x,y,z), which can be converted
	 * into the standard notation as x' = x/z and y'= y/z. If the lines are parallel and intersect at
	 * infinity then z=0 and the above conversion will fail.
	 * </p>
	 * 
	 * @param a Line
	 * @param b Line
	 * @param ret Storage for point of intersection.
	 * @return Point of intersection represented in homogeneous coordinates.
	 */
	public static Point3D_F64 intersection( LineGeneral2D_F64 a , LineGeneral2D_F64 b , Point3D_F64 ret )
	{
		if( ret == null )
			ret = new Point3D_F64();
		
		// compute the intersection as the cross product of 'a' and 'b'
		ret.x = a.B * b.C - a.C * b.B;
		ret.y = a.C * b.A - a.A * b.C;
		ret.z = a.A * b.B - a.B * b.A;

		return ret;
	}

	/**
	 * <p>
	 * Finds the intersection of two lines as a 2D point in coordinates. If the lines are parallel
	 * then null is returned.
	 * </p>
	 *
	 * @param a Line
	 * @param b Line
	 * @param ret Storage for point of intersection.
	 * @return Point of intersection in 2D coordinates.  null if intersection at infinity
	 */
	public static Point2D_F64 intersection( LineGeneral2D_F64 a , LineGeneral2D_F64 b , Point2D_F64 ret )
	{
		if( ret == null )
			ret = new Point2D_F64();

		// compute the intersection as the cross product of 'a' and 'b'
		ret.x = a.B * b.C - a.C * b.B;
		ret.y = a.C * b.A - a.A * b.C;
		double z = a.A * b.B - a.B * b.A;

		if( z == 0 )
			return null;

		ret.x /= z;
		ret.y /= z;

		return ret;
	}

	/**
	 * Finds the point of intersection between a line and a line segment.  The point of intersection
	 * is specified as the distance along the parametric line.  If no intersection is found then
	 * Double.NaN is returned.
	 *
	 * @param target A line whose location along which the point of intersection is being found.  Not modified.
	 * @param l	  Line segment which is being tested for intersection. Not modified.
	 * @return The location along 'target'.  If the lines do not intersect or have infinite intersections Double.NaN is returned.
	 */
	public static double intersection( LineParametric2D_F64 target, LineSegment2D_F64 l ) {
		double a1 = l.b.x - l.a.x;
		double b1 = l.b.y - l.a.y;

		double top = target.slope.y * ( l.a.x - target.p.x ) + target.slope.x * ( target.p.y - l.a.y );
		double bottom = target.slope.x * b1 - target.slope.y * a1;

		if( bottom == 0 )
			return Double.NaN;

		double t_1 = top / bottom;

		// does not intersect along the second line segment
		if( t_1 < 0 || t_1 > 1 )
			return Double.NaN;

		top = b1 * ( target.p.x - l.a.x ) + a1 * ( l.a.y - target.p.y );
		bottom = a1 * target.slope.y - b1 * target.slope.x;

		return top / bottom;
	}

	/**
	 * <p>
	 * Checks to see if the specified point is inside the rectangle.  A point is inside
	 * if it is &ge; the lower extend and &lt; the upper extent.
	 * </p>
	 * <p>
	 * inside = x &ge; x0 AND x &lt; x0+width AND y &ge; y0 AND y &lt; y0+height
	 * </p>
	 * @param a Rectangle.
	 * @param x x-coordinate of point being tested for containment
	 * @param y y-coordinate of point being tested for containment
	 * @return true if inside and false if output
	 */
	public static boolean contains( RectangleLength2D_F64 a, double x, double y ) {
		if( a.getX() <= x && a.getX() + a.getWidth() > x ) {
			return a.getY() <= y && a.getY() + a.getHeight() > y;
		}
		return false;
	}

	/**
	 * <p>
	 * Checks to see if the specified point is inside the rectangle.  A point is inside
	 * if it is &ge; the lower extend and &le; the upper extent.
	 * </p>
	 * <p>
	 * {@code inside = x &ge; x0 AND x &le; x0+width and y &ge; y0 AND y &le; y0+height}
	 * </p>
	 * @param a Rectangle.
	 * @param x x-coordinate of point being tested for containment
	 * @param y y-coordinate of point being tested for containment
	 * @return true if inside and false if output
	 */
	public static boolean contains2( RectangleLength2D_F64 a, double x, double y ) {
		if( a.getX() <= x && a.getX() + a.getWidth() >= x ) {
			return a.getY() <= y && a.getY() + a.getHeight() >= y;
		}
		return false;
	}

	/**
	 * <p>
	 * Checks to see if the specified point is inside the rectangle.  A point is inside
	 * if it is &ge; the lower extend and &lt; the upper extent.
	 * </p>
	 * <p>
	 * inside = x &ge; x0 AND x &le; x1 AND y &ge; y0 AND y &le; y1
	 * </p>
	 * @param a Rectangle.
	 * @param x x-coordinate of point being tested for containment
	 * @param y y-coordinate of point being tested for containment
	 * @return true if inside and false if output
	 */
	public static boolean contains( Rectangle2D_F64 a, double x, double y ) {
		return( a.p0.x <= x && a.p1.x > x && a.p0.y <= y && a.p1.y > y );
	}

	/**
	 * <p>
	 * Checks to see if the specified point is inside the rectangle.  A point is inside
	 * if it is &ge; the lower extend and &le; the upper extent.
	 * </p>
	 * <p>
	 * inside = x &ge; x0 AND x &le; x1 AND y &ge; y0 AND y &le; y1
	 * </p>
	 * @param a Rectangle.
	 * @param x x-coordinate of point being tested for containment
	 * @param y y-coordinate of point being tested for containment
	 * @return true if inside and false if output
	 */
	public static boolean contains2( Rectangle2D_F64 a, double x, double y ) {
		return( a.p0.x <= x && a.p1.x >= x && a.p0.y <= y && a.p1.y >= y );
	}

	public static RectangleLength2D_F64 intersection( RectangleLength2D_F64 a, RectangleLength2D_F64 b ) {
		double tl_x, tl_y, w, h;

		if( a.getX() >= b.getX() ) {
			if( a.getX() >= b.getX() + b.getWidth() )
				return null;

			tl_x = a.getX();
			w = b.getX() + b.getWidth() - a.getX();
		} else {
			if( a.getX() + a.getWidth() <= b.getX() )
				return null;

			tl_x = b.getX();
			w = a.getX() + a.getWidth() - b.getX();
		}

		if( a.getY() >= b.getY() ) {
			if( a.getY() >= b.getY() + b.getHeight() )
				return null;

			tl_y = a.getY();
			h = b.getY() + b.getHeight() - a.getY();
		} else {
			if( a.getY() + a.getHeight() <= b.getY() )
				return null;

			tl_y = b.getY();
			h = a.getY() + a.getHeight() - b.getY();
		}


		return new RectangleLength2D_F64( tl_x, tl_y, w, h );
	}

	/**
	 * Checks to see if the two rectangles intersect each other
	 *
	 * @param a Rectangle
	 * @param b Rectangle
	 * @return true if intersection
	 */
	public static boolean intersects( Rectangle2D_F64 a , Rectangle2D_F64 b ) {
		return( a.p0.x < b.p1.x && a.p1.x > b.p0.x && a.p0.y < b.p1.y && a.p1.y > b.p0.y );
	}

	/**
	 * Finds the intersection between two rectangles.  If the rectangles don't intersect then false is returned.
	 *
	 * @param a Rectangle
	 * @param b Rectangle
	 * @param result Storage for the found intersection
	 * @return true if intersection
	 */
	public static boolean intersection( Rectangle2D_F64 a , Rectangle2D_F64 b , Rectangle2D_F64 result ) {
		if( !intersects(a,b) )
			return false;

		result.p0.x = Math.max(a.p0.x,b.p0.x);
		result.p1.x = Math.min(a.p1.x,b.p1.x);
		result.p0.y = Math.max(a.p0.y,b.p0.y);
		result.p1.y = Math.min(a.p1.y,b.p1.y);

		return true;
	}

	/**
	 * Returns the area of the intersection of two rectangles.
	 *
	 * @param a Rectangle
	 * @param b Rectangle
	 * @return area of intersection
	 */
	public static double intersectionArea( Rectangle2D_F64 a , Rectangle2D_F64 b ) {
		if( !intersects(a,b) )
			return 0;

		double x0 = Math.max(a.p0.x,b.p0.x);
		double x1 = Math.min(a.p1.x,b.p1.x);
		double y0 = Math.max(a.p0.y,b.p0.y);
		double y1 = Math.min(a.p1.y,b.p1.y);

		return (x1-x0)*(y1-y0);
	}
}
