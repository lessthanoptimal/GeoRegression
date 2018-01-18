/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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
	 * Returns true if the polygon is ordered in a counter-clockwise order.  This is done by summing up the interior
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
	 * Returns true if the cross product would result in a strictly positive z (e.g. z > 0 ). If true then
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
}
