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

import georegression.geometry.polygon.ThreeIndexes;
import georegression.geometry.polygon.TriangulateSimpleRemoveEars_F64;
import georegression.metric.Distance2D_F64;
import georegression.metric.Intersection2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.Rectangle2D_F64;
import georegression.struct.shapes.RectangleLength2D_I32;
import org.ddogleg.struct.DogArray;
import org.jetbrains.annotations.Nullable;

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
	 * Checks to see if a polygon is simple or not, see [1]. If complex then the
	 *
	 * <p>[1] Numerical Recipes. Third Edition. Section 21.4</p>
	 *
	 * @param p (Input) polygon.
	 * @param tol (Input) tolerance for testing if lines are colinear
	 * @return true if simple or false if complex.
	 */
	public static PolygonInfo isSimple(Polygon2D_F64 p, @Nullable PolygonInfo result, double tol) {
		if (result==null)
			result = new PolygonInfo();
		result.reset();

		// Check to see if it's too small to be a valid polygon
		if (p.size() < 3) {
			result.type = PolygonInfo.Type.COMPLEX;
			return result;
		}

		final int N = p.size();

		int wind = 0; // winding number
 		boolean concave = false; // true concave

		double crsp = 0.0; // previous cross product

		// vector of previous side
		double p0 = p.get(N-1).x - p.get(N-2).x;
		double p1 = p.get(N-1).y - p.get(N-2).y;

		for (int i = 0, ii=N-1; i < N; ii=i,i++) {
			Point2D_F64 pa = p.get(ii);
			Point2D_F64 pb = p.get(i);

			// vector of current side
			double d0 = pb.x - pa.x;
			double d1 = pb.y - pa.y;

			double crs = p0*d1-p1*d0; // cross product of vectors
			if (crs*crsp < 0.0)
				concave = true; // there is a sign change and this means a concavity was found
			if (p1 <= 0.0) {
				if (d1 > 0.0 && crs > 0.0)
					wind++;
			} else {
				if (d1 <= 0.0 && crs < 0.0)
					wind--;
			}
			p0 = d0;
			p1 = d1;

			// update cross product if it has a sign, i.e. not two parallel line.
			if (crs != 0.0)
				crsp = crs;
		}

		if (Math.abs(wind) != 1) {
			result.type = PolygonInfo.Type.COMPLEX;
		} else if (!concave) {
			result.type = PolygonInfo.Type.CONVEX;
			result.ccw = wind > 0;
		} else if (isSelfIntersectingBrute(p, tol)) {
			// Need to examine all pairs of edges for intersections now. This implements the brute force O(N^2) algorithm
			// found in numerical recipes. They suggest doing something smarter than this for N>10
			// See Computational Geometry in C Section 7.7 for a better approach
			result.type = PolygonInfo.Type.COMPLEX;
		} else {
			result.ccw = wind > 0;
			result.type = PolygonInfo.Type.SIMPLE_CONCAVE;
		}

		return result;
	}

	/**
	 * Brute force algorithm which checks for self intersection.
	 *
	 * @param p (Input) the polygon
	 * @param tol (Input) tolerance for testing if lines are colinear
	 * @return true if self intersection
	 */
	public static boolean isSelfIntersectingBrute(Polygon2D_F64 p, double tol) {
		final int N = p.size();

		for (int i = 0, j = N-1; i < N; j=i,i++) {
			Point2D_F64 a = p.get(j);
			Point2D_F64 b = p.get(i);

			for (int ii = i+1, jj=i; ii < N; jj=ii, ii++) {
				Point2D_F64 aa = p.get(jj);
				Point2D_F64 bb = p.get(ii);
				if (Intersection2D_F64.intersects2(a, b, aa, bb, tol))
					return true;
			}
		}

		return false;
	}

	/**
	 * Triangulates the simple polygon in O(N^2). Output is represented by sets of indexes where
	 * each index is the index of the vertex in the input polygon.
	 *
	 * @see TriangulateSimpleRemoveEars_F64
	 *
	 * @param p (Input) Polygon
	 * @param triangles (Output) Triangles.
	 */
	public static void triangulate(Polygon2D_F64 p, DogArray<ThreeIndexes> triangles) {
		var alg = new TriangulateSimpleRemoveEars_F64();
		alg.process(p,triangles);
	}

	/**
	 * Converts a rectangle into a quadrilateral
	 *
	 * @param input Rectangle.
	 * @param output Quadrilateral. Modified.
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
	 * @param output Polygon2D_F64. Modified.
	 */
	public static void convert( Rectangle2D_F64 input , Polygon2D_F64 output ) {
		if (output.size() != 4)
			throw new IllegalArgumentException("polygon of order 4 expected");

		output.get(0).setTo(input.p0.x, input.p0.y);
		output.get(1).setTo(input.p1.x, input.p0.y);
		output.get(2).setTo(input.p1.x, input.p1.y);
		output.get(3).setTo(input.p0.x, input.p1.y);
	}

	/**
	 * Converts a quadrilateral into a polygon
	 *
	 * @param input Quadrilateral.
	 * @param output Polygon2D_F64. Modified.
	 */
	public static void convert( Quadrilateral_F64 input , Polygon2D_F64 output ) {
		if (output.size() != 4)
			throw new IllegalArgumentException("polygon of order 4 expected");

		output.get(0).setTo(input.a);
		output.get(1).setTo(input.b);
		output.get(2).setTo(input.c);
		output.get(3).setTo(input.d);
	}

	/**
	 * Converts a polygon into a quadrilateral
	 *
	 * @param input polygon.
	 * @param output Quadrilateral. Modified.
	 */
	public static void convert( Polygon2D_F64 input , Quadrilateral_F64 output ) {
		if( input.size() != 4 )
			throw new IllegalArgumentException("Expected 4-sided polygon as input");

		output.a.setTo(input.get(0));
		output.b.setTo(input.get(1));
		output.c.setTo(input.get(2));
		output.d.setTo(input.get(3));
	}

	/**
	 * Converts a rectangle into a quadrilateral
	 *
	 * @param input Rectangle.
	 * @param output Quadrilateral. Modified.
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
	 * Finds the minimum area bounding rectangle around the quadrilateral that is aligned with coordinate
	 * system axises.
	 *
	 * @param polygon (Input) Polygon
	 * @param rectangle (Output) Minimum area rectangle
	 */
	public static void bounding( Polygon2D_F64 polygon , Rectangle2D_F64 rectangle ) {

		rectangle.p0.setTo(polygon.get(0));
		rectangle.p1.setTo(polygon.get(0));

		for (int i = 0; i < polygon.size(); i++) {
			Point2D_F64 p = polygon.get(i);
			if( p.x < rectangle.p0.x ) {
				rectangle.p0.x = p.x;
			} else if( p.x > rectangle.p1.x ) {
				rectangle.p1.x = p.x;
			}

			if( p.y < rectangle.p0.y ) {
				rectangle.p0.y = p.y;
			} else if( p.y > rectangle.p1.y ) {
				rectangle.p1.y = p.y;
			}
		}
	}

	/**
	 * Computes the center or average point in the quadrilateral.
	 *
	 * @param quad (Input) Quadrilateral
	 * @param center (output) Center point of the quadrilateral. Can be null.
	 * @return The center point.
	 */
	public static Point2D_F64 center( Quadrilateral_F64 quad , @Nullable Point2D_F64 center ) {
		if( center == null )
			center = new Point2D_F64();

		center.x = quad.a.x + quad.b.x + quad.c.x + quad.d.x;
		center.y = quad.a.y + quad.b.y + quad.c.y + quad.d.y;

		center.x /= 4.0;
		center.y /= 4.0;

		return center;
	}

	/**
	 * Returns true if the polygon is ordered in a counter-clockwise order. This is done by summing up the interior
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
		average.setIdx(0,0);

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
	 * Flips the order of points inside the polygon. The first index will remain the same will otherwise be reversed
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
	 * Shifts all the vertexes in the polygon up one element. Wraps around at the end
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
	 * Shifts all the vertexes in the polygon up one element. Wraps around at the end
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
	 * Removes a node from a polygon if the two lines its attached two are almost parallel
	 * @param polygon The polygon being modified
	 * @param tol Tolerance in radians
	 */
	public static void removeAlmostParallel( Polygon2D_F64 polygon , double tol ) {

		for (int i = 0; i < polygon.vertexes.size(); ) {
			int j = (i+1)%polygon.vertexes.size();
			int k = (i+2)%polygon.vertexes.size();

			Point2D_F64 p0 = polygon.vertexes.get(i);
			Point2D_F64 p1 = polygon.vertexes.get(j);
			Point2D_F64 p2 = polygon.vertexes.get(k);

			double angle = UtilVector2D_F64.acute(p1.x-p0.x,p1.y-p0.y,p2.x-p1.x,p2.y-p1.y);

			if( angle <= tol) {
				polygon.vertexes.remove(j);
				if( j < i )
					i = polygon.vertexes.size()-1;
			} else {
				i++;
			}
		}
	}

	/**
	 * Remove a point if it's identical to a neighbor
	 *
	 * @param polygon The polygon being modified
	 * @param tol Tolerance in radians
	 */
	public static void removeAdjacentDuplicates( Polygon2D_F64 polygon , double tol ) {

		for (int i = polygon.vertexes.size()-1,j=0; i >= 0 && polygon.size()>1; j=i,i--) {
			if( polygon.get(i).isIdentical(polygon.get(j),tol)) {
				polygon.vertexes.remove(i);
			}
		}
	}

	/**
	 * Remove a point if it's identical to a neighbor
	 *
	 * @param polygon The polygon being modified
	 * @param tol Tolerance in radians
	 */
	public static boolean hasAdjacentDuplicates( Polygon2D_F64 polygon , double tol ) {

		for (int i = polygon.vertexes.size()-1,j=0; i >= 0 && polygon.size()>1; j=i,i--) {
			if( polygon.get(i).isIdentical(polygon.get(j),tol)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Compute the error as a function of the distance between the model and target. The target is sampled at regular
	 * intervals and for each of these points the closest point on the model is found. The returned metric is the
	 * average of difference between paired points.
	 *
	 * NOTE: A different answer will be returned depending on which polygon is the model and which one is the target.
	 *
	 * @param model Model polygon
	 * @param target Target polygon
	 * @return average of closest point error
	 */
	public static double averageOfClosestPointError(Polygon2D_F64 model , Polygon2D_F64 target , int numberOfSamples ) {
		LineSegment2D_F64 line = new LineSegment2D_F64();

		double[] cornerLocationsB = new double[target.size()+1];
		double totalLength = 0;
		for (int i = 0; i < target.size(); i++) {
			Point2D_F64 b0 = target.get(i%target.size());
			Point2D_F64 b1 = target.get((i+1)%target.size());

			cornerLocationsB[i] = totalLength;
			totalLength += b0.distance(b1);
		}
		cornerLocationsB[target.size()] = totalLength;

		Point2D_F64 pointOnB = new Point2D_F64();
		double error = 0;
		int cornerB = 0;
		for (int k = 0; k < numberOfSamples; k++) {
			// Find the point on B to match to a point on A
			double location = totalLength*k/numberOfSamples;

			while (location > cornerLocationsB[cornerB + 1]) {
				cornerB++;
			}
			Point2D_F64 b0 = target.get(cornerB);
			Point2D_F64 b1 = target.get((cornerB+1)%target.size());

			double locationCornerB = cornerLocationsB[cornerB];
			double fraction = (location-locationCornerB)/(cornerLocationsB[cornerB+1]-locationCornerB);

			pointOnB.x = (b1.x-b0.x)*fraction + b0.x;
			pointOnB.y = (b1.y-b0.y)*fraction + b0.y;

			// find the best fit point on A to the point in B
			double best = Double.MAX_VALUE;
			for (int i = 0; i < model.size()+1; i++) {
				line.a = model.get(i%model.size());
				line.b = model.get((i+1)%model.size());

				double d = Distance2D_F64.distance(line,pointOnB);
				if( d < best ) {
					best = d;
				}
			}
			error += best;
		}

		return error/numberOfSamples;
	}
}
