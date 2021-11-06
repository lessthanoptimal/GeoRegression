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

package georegression.metric;

import georegression.geometry.UtilPoint2D_F64;
import georegression.geometry.polygon.AreaIntersectionPolygon2D_F64;
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.Rectangle2D_F64;
import org.jetbrains.annotations.Nullable;


/**
 * Functions related to finding the distance of one shape from another shape. This is often
 * closely related to finding the {@link ClosestPoint3D_F64 closest point}.
 *
 * @author Peter Abeles
 */
// TODO distance between two line segments, line lines
// handle parallel overlapping cases by returning zero
public class Distance2D_F64 {


	/**
	 * <p>
	 * Returns the Euclidean distance of the closest point on the line from a point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p The point. Not modified.
	 * @return Distance the closest point on the line is away from the point.
	 */
	public static double distance( LineParametric2D_F64 line, Point2D_F64 p ) {
		return Math.sqrt(distanceSq(line, p));
	}

	/**
	 * <p>
	 * Returns the Euclidean distance of the closest point on the line from a point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param x Point's x-coordinate
	 * @param y Point's y-coordinate
	 * @return Distance the closest point on the line is away from the point.
	 */
	public static double distance( LineParametric2D_F64 line, double x , double y ) {
		return Math.sqrt(distanceSq(line, x,y));
	}

	/**
	 * <p>
	 * Returns the Euclidean distance squared of the closest point on the line from a point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p The point. Not modified.
	 * @return Euclidean distance squared to the closest point on the line is away from the point.
	 */
	public static double distanceSq( LineParametric2D_F64 line, Point2D_F64 p ) {
		double scale = Math.max(Math.abs(line.slope.x),Math.abs(line.slope.y));

		double t = ClosestPoint2D_F64.closestPointT( line, p , scale);

		double a = (line.slope.x/scale) * t + line.p.x;
		double b = (line.slope.y/scale) * t + line.p.y;

		double dx = p.x - a;
		double dy = p.y - b;

		return dx * dx + dy * dy;
	}

	/**
	 * <p>
	 * Returns the Euclidean distance squared of the closest point on the line from a point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param x Point's x-coordinate
	 * @param y Point's y-coordinate
	 * @return Euclidean distance squared to the closest point on the line is away from the point.
	 */
	public static double distanceSq( LineParametric2D_F64 line, double x , double y ) {
		double scale = Math.max(Math.abs(line.slope.x),Math.abs(line.slope.y));

		double t = ClosestPoint2D_F64.closestPointT( line, x, y , scale);

		double a = (line.slope.x/scale) * t + line.p.x;
		double b = (line.slope.y/scale) * t + line.p.y;

		double dx = x - a;
		double dy = y - b;

		return dx * dx + dy * dy;
	}

	/**
	 * <p>
	 * Returns the Euclidean distance of the closest point on a line segment to the specified point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p The point. Not modified.
	 * @return Euclidean distance of the closest point on a line is away from a point.
	 */
	public static double distance( LineSegment2D_F64 line, Point2D_F64 p ) {
		return Math.sqrt(distanceSq(line, p));
	}

	/**
	 * <p>
	 * Returns the Euclidean distance of the closest point on a line segment to the specified point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param x Point's x-coordinate
	 * @param y Point's y-coordinate
	 * @return Euclidean distance of the closest point on a line is away from a point.
	 */
	public static double distance( LineSegment2D_F64 line, double x , double y) {
		return Math.sqrt(distanceSq(line, x,y));
	}

	/**
	 * <p>
	 * Returns the Euclidean distance squared of the closest point on a line segment to the specified point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p The point. Not modified.
	 * @return Euclidean distance squared of the closest point on a line is away from a point.
	 */
	public static double distanceSq( LineSegment2D_F64 line, Point2D_F64 p ) {
		double a = line.b.x - line.a.x;
		double b = line.b.y - line.a.y;

		double t = a * ( p.x - line.a.x ) + b * ( p.y - line.a.y );
		t /= ( a * a + b * b );

		// if the point of intersection is past the end points return the distance
		// from the closest end point
		if( t < 0 ) {
			return UtilPoint2D_F64.distanceSq(line.a.x, line.a.y, p.x, p.y);
		} else if( t > 1.0 )
			return UtilPoint2D_F64.distanceSq(line.b.x, line.b.y, p.x, p.y);

		// return the distance of the closest point on the line
		return UtilPoint2D_F64.distanceSq(line.a.x + t * a, line.a.y + t * b, p.x, p.y);
	}

	/**
	 * <p>
	 * Returns the Euclidean distance squared of the closest point on a line segment to the specified point.
	 * </p>
	 *
	 * @param line Line segment. Not modified.
	 * @param x Point's x-coordinate
	 * @param y Point's y-coordinate
	 * @return Euclidean distance squared of the closest point on a line is away from a point.
	 */
	public static double distanceSq( LineSegment2D_F64 line, double x , double y ) {
		double a = line.b.x - line.a.x;
		double b = line.b.y - line.a.y;

		double t = a * ( x - line.a.x ) + b * ( y - line.a.y );
		t /= ( a * a + b * b );

		// if the point of intersection is past the end points return the distance
		// from the closest end point
		if( t < 0 ) {
			return UtilPoint2D_F64.distanceSq(line.a.x, line.a.y, x, y);
		} else if( t > 1.0 )
			return UtilPoint2D_F64.distanceSq(line.b.x, line.b.y, x, y);

		// return the distance of the closest point on the line
		return UtilPoint2D_F64.distanceSq(line.a.x + t * a, line.a.y + t * b, x, y);
	}


	/**
	 * Finds the distance between the two line segments
	 * @param segmentA Line segment. Not modified.
	 * @param segmentB Line segment. Not modified.
	 * @return Euclidean distance of the closest point between the two line segments.
	 */
	public static double distance( LineSegment2D_F64 segmentA , LineSegment2D_F64 segmentB ) {
		return Math.sqrt(distanceSq(segmentA, segmentB));
	}

	/**
	 * Finds the distance squared between the two line segments
	 * @param segmentA Line segment. Not modified.
	 * @param segmentB Line segment. Not modified.
	 * @return Euclidean distance squared of the closest point between the two line segments.
	 */
	public static double distanceSq( LineSegment2D_F64 segmentA , LineSegment2D_F64 segmentB ) {

		// intersection of the two lines relative to A
		double slopeAX = segmentA.slopeX();
		double slopeAY = segmentA.slopeY();
		double slopeBX = segmentB.slopeX();
		double slopeBY = segmentB.slopeY();

		double ta = slopeBX*( segmentA.a.y - segmentB.a.y ) - slopeBY*( segmentA.a.x - segmentB.a.x );
		double bottom = slopeBY*slopeAX - slopeAY*slopeBX;

		// see they intersect
		if( bottom != 0 ) {
			// see if the intersection is inside of lineA
			ta /= bottom;
			if( ta >= 0 && ta <= 1.0 ) {
				// see if the intersection is inside of lineB
				double tb = slopeAX*( segmentB.a.y - segmentA.a.y ) - slopeAY*( segmentB.a.x - segmentA.a.x );
				tb /= slopeAY*slopeBX - slopeBY*slopeAX;
				if( tb >= 0 && tb <= 1.0 )
					return 0;
			}
		}

		double closest = Double.MAX_VALUE;
		closest = Math.min(closest,distanceSq(segmentA, segmentB.a));
		closest = Math.min(closest,distanceSq(segmentA, segmentB.b));
		closest = Math.min(closest,distanceSq(segmentB, segmentA.a));
		closest = Math.min(closest,distanceSq(segmentB, segmentA.b));

		return closest;
	}

	/**
	 * Returns the Euclidean distance of the closest point on the quadrilateral to the provided point.
	 *
	 * @param quad Quadrilateral
	 * @param p Point
	 * @return Distance apart
	 */
	public static double distance( Quadrilateral_F64 quad , Point2D_F64 p ) {
		return Math.sqrt(distanceSq(quad,p));
	}

	/**
	 * Returns the Euclidean distance squared of the closest point on the quadrilateral to the provided point.
	 *
	 * @param quad Quadrilateral
	 * @param p Point
	 * @return Distance squared apart
	 */
	public static double distanceSq( Quadrilateral_F64 quad , Point2D_F64 p ) {
		LineSegment2D_F64 seg = LineSegment2D_F64.wrap(quad.a, quad.b);
		double a = distanceSq(seg, p);
		seg.a = quad.b;seg.b = quad.c;
		a = Math.min(a,distanceSq(seg,p));
		seg.a = quad.c;seg.b = quad.d;
		a = Math.min(a,distanceSq(seg,p));
		seg.a = quad.d;seg.b = quad.a;
		return Math.min(a, distanceSq(seg, p));
	}

	/**
	 * Returns the Euclidean distance of the closest point on the Polygon to the provided point.
	 *
	 * @param poly Polygon2D
	 * @param p Point
	 * @return Distance squared apart
	 */
	public static double distance( Polygon2D_F64 poly , Point2D_F64 p ) {
		return Math.sqrt(distanceSq(poly, p, null));
	}

	/**
	 * Returns the Euclidean distance squared of the closest point on the Polygon to the provided point.
	 *
	 * @param poly Polygon2D
	 * @param p Point
	 * @param storage Optional storage for linesegment which is used internally to compute the distance
	 * @return Distance squared apart
	 */
	public static double distanceSq( Polygon2D_F64 poly , Point2D_F64 p , @Nullable LineSegment2D_F64 storage ) {
		if( storage == null )
			storage = new LineSegment2D_F64();

		double minimum = Double.MAX_VALUE;
		for (int i = 0; i < poly.size(); i++) {
			int j = (i+1)%poly.size();

			storage.a.setTo(poly.vertexes.data[i]);
			storage.b.setTo(poly.vertexes.data[j]);

			double d = distanceSq(storage, p);
			if( d < minimum )
				minimum = d;
		}

		return minimum;
	}

	/**
	 * <p>
	 * Returns the Euclidean distance of the closest point on the line to the specified point.
	 * </p>
	 *
	 * @param line A line. Not modified.
	 * @param p The point. Not modified.
	 * @return Euclidean distance of the closest point on the line to the specified point.
	 */
	public static double distance( LineGeneral2D_F64 line , Point2D_F64 p ) {
		return Math.abs(line.A*p.x + line.B*p.y + line.C) / Math.sqrt( line.A*line.A + line.B*line.B );
	}

	/**
	 * <p>
	 * Returns the signed Euclidean distance of the closest point on the line to the specified point.
	 * The line is assumed be normalized. See {@link LineGeneral2D_F64} for details on normalization.
	 * </p>
	 *
	 * @param line A normalized line. Not modified.
	 * @param p The point. Not modified.
	 * @return Euclidean distance of the closest point on the line to the specified point.
	 */
	public static double distanceNorm(LineGeneral2D_F64 line, Point2D_F64 p) {
		return Math.abs(line.A*p.x + line.B*p.y + line.C);
	}

	/**
	 * Returns the distance of the closest point on the line from the origin
	 * @param line Line
	 * @return Euclidean distance
	 */
	public static double distanceOrigin( LineParametric2D_F64 line ) {
		double top = line.slope.y*line.p.x - line.slope.x*line.p.y;

		return Math.abs(top)/line.slope.norm();
	}

	/**
	 * Euclidean distance of closest point on ellipse to point 'p'.
	 *
	 * @param ellipse Ellipse
	 * @param p Point
	 * @return Euclidean distance
	 */
	public static double distance(EllipseRotated_F64 ellipse , Point2D_F64 p ) {
		return Math.sqrt(distance2(ellipse, p));
	}

	/**
	 * Euclidean distance squared of closest point on ellipse to point 'p'.
	 *
	 * @param ellipse Ellipse
	 * @param p Point
	 * @return Euclidean distance squared
	 */
	public static double distance2(EllipseRotated_F64 ellipse , Point2D_F64 p ) {
		// put point into ellipse's reference frame
		double cphi = Math.cos(ellipse.phi);
		double sphi = Math.sin(ellipse.phi);

		double xc = p.x - ellipse.center.x;
		double yc = p.y - ellipse.center.y;
		double r = Math.sqrt(xc*xc + yc*yc);

		double x =  cphi*xc + sphi*yc;
		double y = -sphi*xc + cphi*yc;

		double ct = x/r;
		double st = y/r;

		x = ellipse.center.x + ellipse.a*ct*cphi - ellipse.b*st*sphi;
		y = ellipse.center.y + ellipse.a*ct*sphi + ellipse.b*st*cphi;

		return p.distance2(x,y);
	}

	/**
	 * Returns the Intersection Over Union (IoU) score
	 *
	 * @param a (Input) rectangle
	 * @param b (Input) rectangle
	 * @return IoU score
	 */
	public static double scoreIoU(Rectangle2D_F64 a, Rectangle2D_F64 b ) {
		double areaI = Intersection2D_F64.intersectionArea(a,b);

		if (areaI == 0.0)
			return 0.0;

		return areaI/(a.area() + b.area() - areaI);
	}

	/**
	 * Returns the Intersection Over Union (IoU) score for two simple polygon
	 *
	 * @param a (Input) Simple polygon
	 * @param b (Input) Simple polygon
	 * @return IoU score
	 */
	public static double scoreIoU(Polygon2D_F64 a, Polygon2D_F64 b,
								  @Nullable AreaIntersectionPolygon2D_F64 computeArea) {
		if (computeArea == null)
			computeArea = new AreaIntersectionPolygon2D_F64();

		double areaI = Math.abs(computeArea.computeArea(a, b));
		if (areaI == 0.0)
			return 0.0;

		double areaA = Area2D_F64.polygonSimple(a);
		double areaB = Area2D_F64.polygonSimple(b);

		return areaI/(areaA + areaB - areaI);
	}
}
