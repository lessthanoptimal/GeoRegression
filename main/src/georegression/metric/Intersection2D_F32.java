/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.UtilEllipse_F32;
import georegression.geometry.algs.AreaIntersectionPolygon2D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.curve.EllipseRotated_F32;
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.shapes.Polygon2D_F32;
import georegression.struct.shapes.Quadrilateral_F32;
import georegression.struct.shapes.Rectangle2D_F32;
import georegression.struct.shapes.RectangleLength2D_F32;


/**
 * Functions relating to finding the points at which two shapes intersect with each other.
 *
 * @author Peter Abeles
 */
public class Intersection2D_F32 {

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
	public static boolean containConvex( Polygon2D_F32 polygon , Point2D_F32 pt )
	{
		final int N = polygon.size();

		boolean c = false;
		for (int i = 0, j = N-1; i < N; j = i++) {
			Point2D_F32 a = polygon.vertexes.data[i];
			Point2D_F32 b = polygon.vertexes.data[j];
			
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
	public static boolean containConcave( Polygon2D_F32 polygon , Point2D_F32 pt )
	{
		final int N = polygon.size();

		int left=0;
		int right=0;
		for (int i = 0; i < N-1; i++) {
			Point2D_F32 a = polygon.vertexes.data[i];
			Point2D_F32 b = polygon.vertexes.data[i+1];

			if( (pt.y >= a.y && pt.y < b.y) || (pt.y >= b.y && pt.y < a.y) ) {
				// location of line segment along x-axis at y = pt.y
				float x = b.y==a.y ? pt.x : (pt.y-a.y)*(b.x-a.x)/(b.y-a.y) + a.x;

				if( x <= pt.x )
					left++;
				else if( x > pt.x )
					right++;
			}
		}

		Point2D_F32 a = polygon.vertexes.data[N-1];
		Point2D_F32 b = polygon.vertexes.data[0];

		if( (pt.y >= a.y && pt.y < b.y) || (pt.y >= b.y && pt.y < a.y) ) {
			// location of line segment along x-axis at y = pt.y
			float x = b.y==a.y ? pt.x : (pt.y-pt.y)*(b.x-a.x)/(b.y-a.y) + a.x;

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
	public static boolean contains( Quadrilateral_F32 quad , Point2D_F32 pt ) {
		return containTriangle(quad.a, quad.b, quad.d, pt) ||
				containTriangle(quad.b, quad.c, quad.d, pt);

	}

	/**
	 * Returns true of the the point is inside the triangle.
	 *
	 * This function is simply an unrolled version of {@link #containConcave(Polygon2D_F32, Point2D_F32)}.
	 *
	 * @param a vertex in triangle
	 * @param b vertex in triangle
	 * @param c vertex in triangle
	 * @param pt Point which is being tested for containment inside of triangle
	 * @return true if the point is inside of triangle
	 */
	public static boolean containTriangle( Point2D_F32 a , Point2D_F32 b , Point2D_F32 c , Point2D_F32 pt )
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
	public static Point2D_F32 intersection( LineParametric2D_F32 a, LineParametric2D_F32 b , Point2D_F32 ret ) {
		float t_b = a.getSlopeX() * ( b.getY() - a.getY() ) - a.getSlopeY() * ( b.getX() - a.getX() );
		float bottom = a.getSlopeY() * b.getSlopeX() - b.getSlopeY() * a.getSlopeX();

		if( bottom == 0 )
			return null;

		t_b /= bottom;

		float x = b.getSlopeX() * t_b + b.getX();
		float y = b.getSlopeY() * t_b + b.getY();

		if( ret == null )
			ret = new Point2D_F32();
		ret.set(x,y);
		return ret;
	}

	/**
	 * Finds the point of intersection between two lines.  The point of intersection is specified as a point along
	 * the parametric line 'a'.  (x,y) = (x_0,y_0) + t*(slope_x,slope_y), where 't' is the location returned.
	 *
	 * @param a Line.
	 * @param b Line.
	 * @return The location along 'target'.  If the lines do not intersect or have infinite intersections Float.NaN is returned.
	 */
	public static float intersection( LineParametric2D_F32 a, LineParametric2D_F32 b ) {
		float t_a = b.getSlopeX() * ( a.getY() - b.getY() ) - b.getSlopeY() * ( a.getX() - b.getX() );
		float bottom = b.getSlopeY() * a.getSlopeX() - a.getSlopeY() * b.getSlopeX();

		if( bottom == 0 )
			return Float.NaN;

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
	public static Point2D_F32 intersection( LineSegment2D_F32 l_0, LineSegment2D_F32 l_1,
											Point2D_F32 ret ) {

		float a0 = l_0.b.x - l_0.a.x;
		float b0 = l_0.b.y - l_0.a.y;
		float a1 = l_1.b.x - l_1.a.x;
		float b1 = l_1.b.y - l_1.a.y;

		float top = b0 * ( l_1.a.x - l_0.a.x ) + a0 * ( l_0.a.y - l_1.a.y );
		float bottom = a0 * b1 - b0 * a1;

		if( bottom == 0 )
			return null;
		float t_1 = top / bottom;

		// does not intersect along the second line segment
		if( t_1 < 0 || t_1 > 1 )
			return null;

		top = b1 * ( l_0.a.x - l_1.a.x ) + a1 * ( l_1.a.y - l_0.a.y );
		bottom = a1 * b0 - b1 * a0;

		float t_0 = top / bottom;

		// does not intersect along the first line segment
		if( t_0 < 0 || t_0 > 1 )
			return null;

		if( ret == null ) {
			ret = new Point2D_F32();
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
	public static Point3D_F32 intersection( LineGeneral2D_F32 a , LineGeneral2D_F32 b , Point3D_F32 ret )
	{
		if( ret == null )
			ret = new Point3D_F32();
		
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
	public static Point2D_F32 intersection( LineGeneral2D_F32 a , LineGeneral2D_F32 b , Point2D_F32 ret )
	{
		if( ret == null )
			ret = new Point2D_F32();

		// compute the intersection as the cross product of 'a' and 'b'
		ret.x = a.B * b.C - a.C * b.B;
		ret.y = a.C * b.A - a.A * b.C;
		float z = a.A * b.B - a.B * b.A;

		if( z == 0 )
			return null;

		ret.x /= z;
		ret.y /= z;

		return ret;
	}

	/**
	 * Finds the point of intersection between the two lines defined by the set sets of points passed in.
	 * @param lineA0 Point on line A
	 * @param lineA1 Point on line A
	 * @param lineB0 Point on line B
	 * @param lineB1 Point on line B
	 * @param output (Optional) storage for point of intersection
	 * @return Point of intersection or null if the lines are parallel
	 */
	public static Point2D_F32 intersection( Point2D_F32 lineA0 , Point2D_F32 lineA1 ,
											Point2D_F32 lineB0 , Point2D_F32 lineB1 ,
											Point2D_F32 output )
	{
		if( output == null )
			output = new Point2D_F32();

		float slopeAx = lineA1.x - lineA0.x;
		float slopeAy = lineA1.y - lineA0.y;
		float slopeBx = lineB1.x - lineB0.x;
		float slopeBy = lineB1.y - lineB0.y;

		float top = slopeAy * ( lineB0.x - lineA0.x ) + slopeAx * (lineA0.y - lineB0.y );
		float bottom = slopeAx * slopeBy - slopeAy * slopeBx;

		if( bottom == 0 )
			return null;
		float t = top / bottom;

		output.x = lineB0.x + t*slopeBx;
		output.y = lineB0.y + t*slopeBy;

		return output;
	}

	/**
	 * Finds the point of intersection between a line and a line segment.  The point of intersection
	 * is specified as the distance along the parametric line.  If no intersection is found then
	 * Float.NaN is returned.
	 *
	 * @param target A line whose location along which the point of intersection is being found.  Not modified.
	 * @param l	  Line segment which is being tested for intersection. Not modified.
	 * @return The location along 'target'.  If the lines do not intersect or have infinite intersections Float.NaN is returned.
	 */
	public static float intersection( LineParametric2D_F32 target, LineSegment2D_F32 l ) {
		float a1 = l.b.x - l.a.x;
		float b1 = l.b.y - l.a.y;

		float top = target.slope.y * ( l.a.x - target.p.x ) + target.slope.x * ( target.p.y - l.a.y );
		float bottom = target.slope.x * b1 - target.slope.y * a1;

		if( bottom == 0 )
			return Float.NaN;

		float t_1 = top / bottom;

		// does not intersect along the second line segment
		if( t_1 < 0 || t_1 > 1 )
			return Float.NaN;

		top = b1 * ( target.p.x - l.a.x ) + a1 * ( l.a.y - target.p.y );
		bottom = a1 * target.slope.y - b1 * target.slope.x;

		return top / bottom;
	}

	/**
	 * Finds the area of the intersection of two polygons.
	 *
	 * @se AreaIntersectionPolygon2D_F32
	 *
	 * @param a (Input) Polygon 2D
	 * @param b (Input) Polygon 2D
	 * @return Area of intersection.
	 */
	public static float intersection( Polygon2D_F32 a , Polygon2D_F32 b ) {
		AreaIntersectionPolygon2D_F32 alg = new AreaIntersectionPolygon2D_F32();
		return (float)Math.abs(alg.computeArea(a,b));
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
	public static boolean contains( RectangleLength2D_F32 a, float x, float y ) {
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
	public static boolean contains2( RectangleLength2D_F32 a, float x, float y ) {
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
	public static boolean contains( Rectangle2D_F32 a, float x, float y ) {
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
	public static boolean contains2( Rectangle2D_F32 a, float x, float y ) {
		return( a.p0.x <= x && a.p1.x >= x && a.p0.y <= y && a.p1.y >= y );
	}

	/**
	 * Tests to see if the provided point lies on or is contained inside the ellipse
	 *
	 * @param ellipse  Ellipse
	 * @param x x-coordinate of point being tested for containment
	 * @param y y-coordinate of point being tested for containment
	 * @return true if inside and false if output
	 */
	public static boolean contains(EllipseRotated_F32 ellipse , float x , float y ) {
		return (UtilEllipse_F32.evaluate(x,y, ellipse) <= 1.0f );
	}

	public static RectangleLength2D_F32 intersection( RectangleLength2D_F32 a, RectangleLength2D_F32 b ) {
		float tl_x, tl_y, w, h;

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


		return new RectangleLength2D_F32( tl_x, tl_y, w, h );
	}

	/**
	 * Checks to see if the two rectangles intersect each other
	 *
	 * @param a Rectangle
	 * @param b Rectangle
	 * @return true if intersection
	 */
	public static boolean intersects( Rectangle2D_F32 a , Rectangle2D_F32 b ) {
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
	public static boolean intersection( Rectangle2D_F32 a , Rectangle2D_F32 b , Rectangle2D_F32 result ) {
		if( !intersects(a,b) )
			return false;

		result.p0.x = (float)Math.max(a.p0.x,b.p0.x);
		result.p1.x = (float)Math.min(a.p1.x,b.p1.x);
		result.p0.y = (float)Math.max(a.p0.y,b.p0.y);
		result.p1.y = (float)Math.min(a.p1.y,b.p1.y);

		return true;
	}

	/**
	 * Returns the area of the intersection of two rectangles.
	 *
	 * @param a Rectangle
	 * @param b Rectangle
	 * @return area of intersection
	 */
	public static float intersectionArea( Rectangle2D_F32 a , Rectangle2D_F32 b ) {
		if( !intersects(a,b) )
			return 0;

		float x0 = (float)Math.max(a.p0.x,b.p0.x);
		float x1 = (float)Math.min(a.p1.x,b.p1.x);
		float y0 = (float)Math.max(a.p0.y,b.p0.y);
		float y1 = (float)Math.min(a.p1.y,b.p1.y);

		return (x1-x0)*(y1-y0);
	}

	/**
	 * Determines the location(s) that a line and ellipse intersect.  Returns the number of intersections found.
	 * NOTE: Due to floating point errors, it's possible for a single solution to returned as two points.
	 *
	 * @param line Line
	 * @param ellipse Ellipse
	 * @param intersection0 Storage for first point of intersection.
	 * @param intersection1 Storage for second point of intersection.
	 * @param EPS Numerical precision.  Set to a negative value to use default
	 * @return Number of intersections.  Possible values are 0, 1, or 2.
	 */
	public static int intersection( LineGeneral2D_F32 line , EllipseRotated_F32 ellipse ,
									Point2D_F32 intersection0 , Point2D_F32 intersection1 , float EPS ) {

		if( EPS < 0 ) {
			EPS = GrlConstants.F_EPS;
		}

		// First translate the line so that coordinate origin is the same as the ellipse
		float C = line.C + (line.A*ellipse.center.x + line.B*ellipse.center.y);

		// Now rotate the line
		float cphi = (float)Math.cos(ellipse.phi);
		float sphi = (float)Math.sin(ellipse.phi);
		float A =  line.A*cphi + line.B*sphi;
		float B = -line.A*sphi + line.B*cphi;

		// Now solve for the intersections with the coordinate system centered and aligned to the ellipse
		// There are two different ways to solve for this.  Pick the axis with the largest slope
		// to avoid the pathological case
		float a2 = ellipse.a*ellipse.a;
		float b2 = ellipse.b*ellipse.b;

		float x0,y0;
		float x1,y1;

		int totalIntersections;

		if( (float)Math.abs(A) > (float)Math.abs(B) ) {
			float alpha = -C/A;
			float beta = -B/A;

			float aa = beta*beta/a2 + 1.0f/b2;
			float bb = 2.0f*alpha*beta/a2;
			float cc = alpha*alpha/a2 - 1.0f;

			float inner = bb*bb -4.0f*aa*cc;
			if( (float)Math.abs(inner/aa) < EPS ) { // divide by aa for scale invariance
				totalIntersections = 1;
				inner = inner < 0 ? 0 : inner;
			} else if( inner < 0 ) {
				return 0;
			} else {
				totalIntersections = 2;
			}
			float right = (float)Math.sqrt(inner);
			y0 = (-bb + right)/(2.0f*aa);
			y1 = (-bb - right)/(2.0f*aa);

			x0 =  -(C + B*y0)/A;
			x1 =  -(C + B*y1)/A;
		} else {
			float alpha = -C/B;
			float beta = -A/B;

			float aa = beta*beta/b2 + 1.0f/a2;
			float bb = 2.0f*alpha*beta/b2;
			float cc = alpha*alpha/b2-1.0f;

			float inner = bb*bb -4.0f*aa*cc;
			if( (float)Math.abs(inner/aa) < EPS ) { // divide by aa for scale invariance
				totalIntersections = 1;
				inner = inner < 0 ? 0 : inner;
			} else if( inner < 0 ) {
				return 0;
			} else {
				totalIntersections = 2;
			}
			float right = (float)Math.sqrt(inner);
			x0 = (-bb + right)/(2.0f*aa);
			x1 = (-bb - right)/(2.0f*aa);

			y0 = -(A*x0 + C)/B;
			y1 = -(A*x1 + C)/B;
		}

		// go back into world coordinate system
		intersection0.x = x0*cphi - y0*sphi + ellipse.center.x;
		intersection0.y = x0*sphi + y0*cphi + ellipse.center.y;

		intersection1.x = x1*cphi - y1*sphi + ellipse.center.x;
		intersection1.y = x1*sphi + y1*cphi + ellipse.center.y;

		return totalIntersections;
	}
}
