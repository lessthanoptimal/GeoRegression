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

package georegression.examples;

import georegression.metric.Distance2D_F64;
import georegression.metric.Intersection2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;

/**
 * This example demonstrates how to metric values which describe the related to lines and points.
 *
 * @author Peter Abeles
 */
public class ExampleMetricLine {

	public static void distancePointToLine() {
		System.out.println("Distance of points to lines");
		// two points which are used to define a line
		Point2D_F64 a = new Point2D_F64(0,0);
		Point2D_F64 b = new Point2D_F64(10,10);
		// a point on the line
		Point2D_F64 c = new Point2D_F64(20,20);
		// a point off the line
		Point2D_F64 d = new Point2D_F64(5,8);

		// define a line segment using two points
		LineSegment2D_F64 ls = new LineSegment2D_F64(a,b);
		// convert the line segment into a parametric line
		LineParametric2D_F64 lp = new LineParametric2D_F64(a.x,a.y,b.x-a.x,b.y-a.y);

		// Point C should lie along the infinite line but be outside of the finite line segment
		System.out.println("Distance of line segment to point c     : "+ Distance2D_F64.distance(ls,c));
		System.out.println("Distance of parametric line to point c  : "+ Distance2D_F64.distance(lp,c));
		// Point D should be the same distance from both lines
		System.out.println("Distance of line segment to point d     : "+ Distance2D_F64.distance(ls,d));
		System.out.println("Distance of parametric line to point d  : "+ Distance2D_F64.distance(lp,d));
	}

	public static void intersectionLineSegment() {
		LineSegment2D_F64 lineA = new LineSegment2D_F64(-10,-10,10,10);
		LineSegment2D_F64 lineB = new LineSegment2D_F64(-10,10,10,-10);
		LineSegment2D_F64 lineC = new LineSegment2D_F64(-10,-9,10,11);
		LineSegment2D_F64 lineD = new LineSegment2D_F64(-20,-20,-18,-18);
		LineSegment2D_F64 lineE = new LineSegment2D_F64(-20,-20,-9,-9);

		System.out.println("Intersection between line segments");
		System.out.println("A and B (one)  : "+ Intersection2D_F64.intersection(lineA,lineB,null));
		System.out.println("A and C (none) : "+ Intersection2D_F64.intersection(lineA,lineC,null));
		System.out.println("A and D (none) : "+ Intersection2D_F64.intersection(lineA,lineD,null));
		System.out.println("A and E (many) : "+ Intersection2D_F64.intersection(lineA,lineE,null));

	}

	public static void intersectionParametric() {
		LineParametric2D_F64 lineA = new LineParametric2D_F64(0,0,1,1);
		LineParametric2D_F64 lineB = new LineParametric2D_F64(0,0,1,-1);
		LineParametric2D_F64 lineC = new LineParametric2D_F64(1,0,1,1);
		LineParametric2D_F64 lineD = new LineParametric2D_F64(-1,-1,1,1);

		System.out.println("Intersection between lines");
		System.out.println("A and B (one)  : "+ Intersection2D_F64.intersection(lineA,lineB));
		System.out.println("A and C (none) : "+ Intersection2D_F64.intersection(lineA,lineC));
		System.out.println("A and D (many) : "+ Intersection2D_F64.intersection(lineA,lineD));
	}

	public static void main( String args[] ) {
		distancePointToLine();
		System.out.println();
		intersectionLineSegment();
		System.out.println();
		intersectionParametric();
	}
}
