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

package georegression.geometry;

import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.Polygon2D_F32;
import georegression.struct.shapes.Quadrilateral_F32;
import georegression.struct.shapes.Rectangle2D_F32;
import georegression.struct.shapes.RectangleLength2D_I32;

import java.util.List;

/**
 * Various functions related to polygons.
 *
 * @author Peter Abeles
 */
public class UtilPolygons2D_F32 {

	/**
	 * Converts a rectangle into a quadrilateral
	 *
	 * @param input Rectangle.
	 * @param output Quadrilateral.  Modified.
	 */
	public static void convert( Rectangle2D_F32 input , Quadrilateral_F32 output ) {
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
	 * Converts a rectangle into a quadrilateral
	 *
	 * @param input Rectangle.
	 * @param output Quadrilateral.  Modified.
	 */
	public static void convert( RectangleLength2D_I32 input , Quadrilateral_F32 output ) {
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
	public static void bounding( Quadrilateral_F32 quad , Rectangle2D_F32 rectangle ) {

		rectangle.p0.x = (float)Math.min(quad.a.x,quad.b.x);
		rectangle.p0.x = (float)Math.min(rectangle.p0.x,quad.c.x);
		rectangle.p0.x = (float)Math.min(rectangle.p0.x,quad.d.x);

		rectangle.p0.y = (float)Math.min(quad.a.y,quad.b.y);
		rectangle.p0.y = (float)Math.min(rectangle.p0.y,quad.c.y);
		rectangle.p0.y = (float)Math.min(rectangle.p0.y,quad.d.y);

		rectangle.p1.x = (float)Math.max(quad.a.x,quad.b.x);
		rectangle.p1.x = (float)Math.max(rectangle.p1.x,quad.c.x);
		rectangle.p1.x = (float)Math.max(rectangle.p1.x,quad.d.x);

		rectangle.p1.y = (float)Math.max(quad.a.y,quad.b.y);
		rectangle.p1.y = (float)Math.max(rectangle.p1.y,quad.c.y);
		rectangle.p1.y = (float)Math.max(rectangle.p1.y,quad.d.y);
	}

	/**
	 * Computes the center or average point in the quadrilateral.
	 *
	 * @param quad (Input) Quadrilateral
	 * @param center (output) Center point of the quadrilateral.  Can be null.
	 * @return The center point.
	 */
	public static Point2D_F32 center( Quadrilateral_F32 quad , Point2D_F32 center ) {
		if( center == null )
			center = new Point2D_F32();

		center.x = quad.a.x + quad.b.x + quad.c.x + quad.d.x;
		center.y = quad.a.y + quad.b.y + quad.c.y + quad.d.y;

		center.x /= 4.0f;
		center.y /= 4.0f;

		return center;
	}

	/**
	 * Returns true if the polygon is ordered in a counter-clockwise order.  This is done by summing up the interior
	 * angles.
	 * 
	 * @param polygon List of ordered points which define a polygon
	 * @return true if CCW and false if CW
	 */
	public static boolean isCCW( List<Point2D_F32> polygon ) {
		float total = 0;

		final int N = polygon.size();
		for (int i = 0; i <= N; i++) {
			Point2D_F32 a = polygon.get(i%N);
			Point2D_F32 b = polygon.get((i+1)%N);
			Point2D_F32 c = polygon.get((i+2)%N);

			float angleBA = (float)Math.atan2(a.y-b.y,a.x-b.x);
			float angleBC = (float)Math.atan2(c.y-b.y,c.x-b.x);

			total += UtilAngle.minus(angleBA, angleBC);
		}


		return total > 0;
	}

	/**
	 * Reverses the order of points in a polygon.  The first vertex will still be the first vertex
	 * @param polygon The input polygon whose vertexes are being re-ordered
	 */
	public static void reverseOrder( Polygon2D_F32 polygon ) {
		for (int i = 1; i <= polygon.size()/2; i++) {
			int j = polygon.size()-i;
			Point2D_F32 a = polygon.vertexes.data[i];
			Point2D_F32 b = polygon.vertexes.data[j];

			polygon.vertexes.data[i] = b;
			polygon.vertexes.data[j] = a;
		}
	}

//	public static boolean isCCW( Point2D_F32 a, Point2D_F32 b , Point2D_F32 c ) {
//		float angleAB = (float)Math.atan2(b.y-a.y,b.x-a.x);
//		float angleAC = (float)Math.atan2(c.y-a.y,c.x-a.x);
//
//		return UtilAngle.distanceCCW(angleAB,angleAC) < (float)Math.PI;
//	}
}
