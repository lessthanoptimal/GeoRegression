/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Polygon2D_I32;
import georegression.struct.shapes.Rectangle2D_I32;

import java.util.List;

/**
 * Various functions related to polygons.
 *
 * @author Peter Abeles
 */
public class UtilPolygons2D_I32 {

	/**
	 * Finds the minimum area bounding rectangle which is aligned to the x and y axis around the list of points.
	 * Note (x0,y0) is inclusive and (x1,y1) is exclusive.
	 *
	 * @param points (Input) Quadrilateral
	 * @param rectangle (Output) Minimum area rectangle
	 */
	public static void bounding( List<Point2D_I32> points , Rectangle2D_I32 rectangle ) {

		rectangle.x0 = Integer.MAX_VALUE;
		rectangle.y0 = Integer.MAX_VALUE;
		rectangle.x1 = Integer.MIN_VALUE;
		rectangle.y1 = Integer.MIN_VALUE;

		for( int i = 0; i < points.size(); i++ ) {
			Point2D_I32 p = points.get(i);

			if( p.x < rectangle.x0 )
				rectangle.x0 = p.x;
			if( p.x > rectangle.x1 )
				rectangle.x1 = p.x;
			if( p.y < rectangle.y0 )
				rectangle.y0 = p.y;
			if( p.y > rectangle.y1 )
				rectangle.y1 = p.y;
		}

		rectangle.x1++;
		rectangle.y1++;
	}

	/**
	 * Finds the minimum area bounding rectangle which is aligned to the x and y axis around the polygon.
	 * Note (x0,y0) is inclusive and (x1,y1) is exclusive.
	 *
	 * @param quad (Input) Quadrilateral
	 * @param rectangle (Output) Minimum area rectangle
	 */
	public static void bounding( Polygon2D_I32 quad , Rectangle2D_I32 rectangle ) {

		UtilPolygons2D_I32.bounding(quad.vertexes.toList(),rectangle);
	}

	/**
	 * Returns true if the polygon is ordered in a counter-clockwise order. This is done by summing up the interior
	 * angles.
	 *
	 * @param polygon List of ordered points which define a polygon
	 * @return true if CCW and false if CW
	 */
	public static boolean isCCW( List<Point2D_I32> polygon ) {
		final int N = polygon.size();
		int sign = 0;
		for (int i = 0; i < N; i++) {
			int j = (i+1)%N;
			int k = (i+2)%N;

			if( isPositiveZ(polygon.get(i),polygon.get(j),polygon.get(k))) {
				sign++;
			} else {
				sign--;
			}
		}

		return sign < 0;
	}

	/**
	 * Flips the order of points inside the polygon. The first index will remain the same will otherwise be reversed
	 *
	 * @param a Polygon of order 3 or more.
	 */
	public static void flip( Polygon2D_I32 a ) {
		int N = a.size();
		int H = N/2;

		for (int i = 1; i <= H; i++) {
			int j = N-i;
			Point2D_I32 tmp = a.vertexes.data[i];
			a.vertexes.data[i] = a.vertexes.data[j];
			a.vertexes.data[j] = tmp;
		}
	}

	/**
	 * Determines if the polugon is convex or concave.
	 *
	 * @param poly Polygon
	 * @return true if convex and false if concave
	 */
	public static boolean isConvex( Polygon2D_I32 poly ) {
		// if the cross product of all consecutive triples is positive or negative then it is convex

		final int N = poly.size();
		int numPositive = 0;
		for (int i = 0; i < N; i++) {
			int j = (i+1)%N;
			int k = (i+2)%N;

			Point2D_I32 a = poly.vertexes.data[i];
			Point2D_I32 b = poly.vertexes.data[j];
			Point2D_I32 c = poly.vertexes.data[k];

			int dx0 = a.x-b.x;
			int dy0 = a.y-b.y;

			int dx1 = c.x-b.x;
			int dy1 = c.y-b.y;

			int z = dx0 * dy1 - dy0 * dx1;
			if( z > 0 )
				numPositive++;
			// z can be zero if there are duplicate points.
			// not sure if it should throw an exception if its "bad" or not
		}

		return( numPositive == 0 || numPositive == N );
	}

	/**
	 * Returns true if the cross product would result in a strictly positive z (e.g. z &gt; 0 ). If true then
	 * the order is clockwise.
	 *
	 * v0 = a-b
	 * v1 = c-b
	 *
	 * @param a first point in sequence
	 * @param b second point in sequence
	 * @param c third point in sequence
	 * @return true if positive z
	 */
	public static boolean isPositiveZ(Point2D_I32 a, Point2D_I32 b, Point2D_I32 c  ) {
		int dx0 = a.x-b.x;
		int dy0 = a.y-b.y;

		int dx1 = c.x-b.x;
		int dy1 = c.y-b.y;

		int z = dx0 * dy1 - dy0 * dx1;

		return z > 0 ;
	}

	/**
	 * Checks to see if the vertexes of the two polygon's are the same up to the specified tolerance
	 *
	 * @param a Polygon
	 * @param b Polygon
	 * @return true if identical up to tolerance or false if not
	 */
	public static boolean isIdentical(Polygon2D_I32 a , Polygon2D_I32 b  ) {
		if( a.size() != b.size())
			return false;

		for (int i = 0; i < a.size(); i++) {
			if( !a.get(i).equals(b.get(i))  )
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
	 * @return true if identical up to tolerance or false if not
	 */
	public static boolean isEquivalent( Polygon2D_I32 a , Polygon2D_I32 b  ) {
		if( a.size() != b.size())
			return false;

		// first find two vertexes which are the same
		Point2D_I32 a0 = a.get(0);
		int match = -1;
		for (int i = 0; i < b.size(); i++) {
			if( a0.equals(b.get(i)) ) {
				match = i;
				break;
			}
		}

		if( match < 0 )
			return false;

		// now go in a circle and see if they all line up
		for (int i = 1; i < b.size(); i++) {
			Point2D_I32 ai = a.get(i);
			Point2D_I32 bi = b.get((match+i)%b.size());

			if( !ai.equals(bi)  ) {
				return false;
			}
		}
		return true;
	}

}
