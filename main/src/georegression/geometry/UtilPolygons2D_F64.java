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

package georegression.geometry;

import georegression.geometry.algs.AndrewMonotoneConvexHull_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.Rectangle2D_F64;
import georegression.struct.shapes.RectangleLength2D_I32;

import java.util.List;

/**
 * Various functions related to polygons.
 *
 * @author Peter Abeles
 */
public class UtilPolygons2D_F64 {

	/**
	 * Determines if the polugon is convex or concave.
	 *
	 * @param poly Polygon
	 * @return true if convex and false if concave
	 */
	public static boolean isConvex( Polygon2D_F64 poly ) {
		// if the cross product of all consecutive triples is positive or negative then it is convex

		final int N = poly.size();
		int numPositive = 0;
		for (int i = 0; i < N; i++) {
			int j = (i+1)%N;
			int k = (i+2)%N;

			Point2D_F64 a = poly.vertexes.data[i];
			Point2D_F64 b = poly.vertexes.data[j];
			Point2D_F64 c = poly.vertexes.data[k];

			double dx0 = a.x-b.x;
			double dy0 = a.y-b.y;

			double dx1 = c.x-b.x;
			double dy1 = c.y-b.y;

			double z = dx0 * dy1 - dy0 * dx1;
			if( z > 0 )
				numPositive++;
			// z can be zero if there are duplicate points.
			// not sure if it should throw an exception if its "bad" or not
		}

		return( numPositive == 0 || numPositive == N );
	}

	/**
	 * Converts a rectangle into a quadrilateral
	 *
	 * @param input Rectangle.
	 * @param output Quadrilateral.  Modified.
	 */
	public static void convert( Rectangle2D_F64 input , Quadrilateral_F64 output ) {
		output.a.x = input.p0.x;
		output.a.y = input.p0.y;

		output.b.x = input.p1.x;
		output.b.y = input.p0.y;

		output.c.x = input.p1.x;
		output.c.y = input.p1.y;

		output.d.x = input.p0.x;
		output.d.y = input.p1.y;
	}

	/**
	 * Converts a rectangle into a polygon
	 *
	 * @param input Rectangle.
	 * @param output Polygon2D_F64.  Modified.
	 */
	public static void convert( Rectangle2D_F64 input , Polygon2D_F64 output ) {
		if (output.size() != 4)
			throw new IllegalArgumentException("polygon of order 4 expected");

		output.get(0).set(input.p0.x, input.p0.y);
		output.get(1).set(input.p1.x, input.p0.y);
		output.get(2).set(input.p1.x, input.p1.y);
		output.get(3).set(input.p0.x, input.p1.y);
	}

	/**
	 * Converts a quadrilateral into a polygon
	 *
	 * @param input Quadrilateral.
	 * @param output Polygon2D_F64.  Modified.
	 */
	public static void convert( Quadrilateral_F64 input , Polygon2D_F64 output ) {
		if (output.size() != 4)
			throw new IllegalArgumentException("polygon of order 4 expected");

		output.get(0).set(input.a);
		output.get(1).set(input.b);
		output.get(2).set(input.c);
		output.get(3).set(input.d);
	}

	/**
	 * Converts a polygon into a quadrilateral
	 *
	 * @param input polygon.
	 * @param output Quadrilateral.  Modified.
	 */
	public static void convert( Polygon2D_F64 input , Quadrilateral_F64 output ) {
		if( input.size() != 4 )
			throw new IllegalArgumentException("Expected 4-sided polygon as input");

		output.a.set(input.get(0));
		output.b.set(input.get(1));
		output.c.set(input.get(2));
		output.d.set(input.get(3));
	}

	/**
	 * Converts a rectangle into a quadrilateral
	 *
	 * @param input Rectangle.
	 * @param output Quadrilateral.  Modified.
	 */
	public static void convert( RectangleLength2D_I32 input , Quadrilateral_F64 output ) {
		output.a.x = input.x0;
		output.a.y = input.y0;

		output.b.x = input.x0+input.width-1;
		output.b.y = input.y0;

		output.c.x = input.x0+input.width-1;
		output.c.y = input.y0+input.height-1;

		output.d.x = input.x0;
		output.d.y = input.y0+input.height-1;
	}

	/**
	 * Finds the minimum area bounding rectangle around the quadrilateral.
	 *
	 * @param quad (Input) Quadrilateral
	 * @param rectangle (Output) Minimum area rectangle
	 */
	public static void bounding( Quadrilateral_F64 quad , Rectangle2D_F64 rectangle ) {

		rectangle.p0.x = Math.min(quad.a.x,quad.b.x);
		rectangle.p0.x = Math.min(rectangle.p0.x,quad.c.x);
		rectangle.p0.x = Math.min(rectangle.p0.x,quad.d.x);

		rectangle.p0.y = Math.min(quad.a.y,quad.b.y);
		rectangle.p0.y = Math.min(rectangle.p0.y,quad.c.y);
		rectangle.p0.y = Math.min(rectangle.p0.y,quad.d.y);

		rectangle.p1.x = Math.max(quad.a.x,quad.b.x);
		rectangle.p1.x = Math.max(rectangle.p1.x,quad.c.x);
		rectangle.p1.x = Math.max(rectangle.p1.x,quad.d.x);

		rectangle.p1.y = Math.max(quad.a.y,quad.b.y);
		rectangle.p1.y = Math.max(rectangle.p1.y,quad.c.y);
		rectangle.p1.y = Math.max(rectangle.p1.y,quad.d.y);
	}

	/**
	 * Computes the center or average point in the quadrilateral.
	 *
	 * @param quad (Input) Quadrilateral
	 * @param center (output) Center point of the quadrilateral.  Can be null.
	 * @return The center point.
	 */
	public static Point2D_F64 center( Quadrilateral_F64 quad , Point2D_F64 center ) {
		if( center == null )
			center = new Point2D_F64();

		center.x = quad.a.x + quad.b.x + quad.c.x + quad.d.x;
		center.y = quad.a.y + quad.b.y + quad.c.y + quad.d.y;

		center.x /= 4.0;
		center.y /= 4.0;

		return center;
	}

	/**
	 * Returns true if the polygon is ordered in a counter-clockwise order.  This is done by summing up the interior
	 * angles.
	 * 
	 * @param polygon List of ordered points which define a polygon
	 * @return true if CCW and false if CW
	 */
	public static boolean isCCW( List<Point2D_F64> polygon ) {
		final int N = polygon.size();
		int sign = 0;
		for (int i = 0; i < N; i++) {
			int j = (i+1)%N;
			int k = (i+2)%N;

			Point2D_F64 a = polygon.get(i);
			Point2D_F64 b = polygon.get(j);
			Point2D_F64 c = polygon.get(k);

			double dx0 = a.x-b.x;
			double dy0 = a.y-b.y;

			double dx1 = c.x-b.x;
			double dy1 = c.y-b.y;

			double z = dx0 * dy1 - dy0 * dx1;
			if( z > 0 )
				sign++;
			else
				sign--;
		}

		return sign < 0;
	}

	public static boolean isCCW( Polygon2D_F64 polygon ) {
		return isCCW(polygon.vertexes.toList());
	}

	/**
	 * Computes the average of all the vertexes
	 * @param input (input) polygon
	 * @param average (output) average point
	 */
	public static void vertexAverage(Polygon2D_F64 input, Point2D_F64 average ) {
		average.set(0,0);

		for (int i = 0; i < input.size(); i++) {
			Point2D_F64 v = input.vertexes.data[i];
			average.x += v.x;
			average.y += v.y;
		}
		average.x /= input.size();
		average.y /= input.size();
	}

	/**
	 * Checks to see if the vertexes of the two polygon's are the same up to the specified tolerance
	 *
	 * @param a Polygon
	 * @param b Polygon
	 * @param tol tolerance
	 * @return true if identical up to tolerance or false if not
	 */
	public static boolean isIdentical( Polygon2D_F64 a , Polygon2D_F64 b , double tol ) {
		if( a.size() != b.size())
			return false;

		double tol2 = tol*tol;
		for (int i = 0; i < a.size(); i++) {
			if( a.get(i).distance2(b.get(i)) > tol2 )
				return false;
		}
		return true;
	}

	/**
	 * Checks to see if the vertexes of the two polygon's are the same up to the specified tolerance and allows
	 * for a shift in their order
	 *
	 * @param a Polygon
	 * @param b Polygon
	 * @param tol tolerance
	 * @return true if identical up to tolerance or false if not
	 */
	public static boolean isEquivalent( Polygon2D_F64 a , Polygon2D_F64 b , double tol ) {
		if( a.size() != b.size())
			return false;

		double tol2 = tol*tol;

		// first find two vertexes which are the same
		Point2D_F64 a0 = a.get(0);
		int match = -1;
		for (int i = 0; i < b.size(); i++) {
			if( a0.distance2(b.get(i)) <= tol2 ) {
				match = i;
				break;
			}
		}

		if( match < 0 )
			return false;

		// now go in a circle and see if they all line up
		for (int i = 1; i < b.size(); i++) {
			Point2D_F64 ai = a.get(i);
			Point2D_F64 bi = b.get((match+i)%b.size());

			if( ai.distance2(bi) > tol2 ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Flips the order of points inside the polygon.  The first index will remain the same will otherwise be reversed
	 *
	 * @param a Polygon of order 3 or more.
	 */
	public static void flip( Polygon2D_F64 a ) {
		int N = a.size();
		int H = N/2;

		for (int i = 1; i <= H; i++) {
			int j = N-i;
			Point2D_F64 tmp = a.vertexes.data[i];
			a.vertexes.data[i] = a.vertexes.data[j];
			a.vertexes.data[j] = tmp;
		}
	}

	/**
	 * Shifts all the vertexes in the polygon up one element.  Wraps around at the end
	 * @param a Polygon
	 */
	public static void shiftUp( Polygon2D_F64 a ) {
		final int N = a.size();

		Point2D_F64 first = a.get(0);

		for (int i = 0; i < N-1; i++ ) {
			a.vertexes.data[i] = a.vertexes.data[i+1];
		}
		a.vertexes.data[N-1] = first;
	}

	/**
	 * Shifts all the vertexes in the polygon up one element.  Wraps around at the end
	 * @param a Polygon
	 */
	public static void shiftDown( Polygon2D_F64 a ) {
		final int N = a.size();

		Point2D_F64 last = a.get(N-1);

		for (int i = N-1; i > 0; i-- ) {
			a.vertexes.data[i] = a.vertexes.data[i-1];
		}
		a.vertexes.data[0] = last;
	}

	/**
	 * Computes the convex hull of the set of points.
	 *
	 * <p>
	 * NOTE: This method declares a temporary array.  If you want to avoid that invoke {@link AndrewMonotoneConvexHull_F64}
	 * directly.
	 * </p>
	 *
	 * @param points (Input) Set of points.
	 * @param hull (output) storage for convex hull.  Will be in counter-clockwise order
	 */
	public static void convexHull( List<Point2D_F64> points , Polygon2D_F64 hull ) {
		Point2D_F64[] array = new Point2D_F64[points.size()];

		for (int i = 0; i < points.size(); i++) {
			array[i] = points.get(i);
		}

		AndrewMonotoneConvexHull_F64 andrew = new AndrewMonotoneConvexHull_F64();
		andrew.process(array,array.length,hull);
	}
}
