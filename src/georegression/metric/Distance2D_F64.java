/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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

package georegression.metric;

import georegression.geometry.UtilPoint2D_F64;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;


/**
 * Functions related to finding the distance of one shape from another shape.  This is often
 * closely related to finding the {@link ClosestPoint3D_F64 closest point}.
 *
 * @author Peter Abeles
 */
// TODO distance between two line segments, line lines
// handle parallel overlapping cases by returning zero
public class Distance2D_F64 {


	/**
	 * <p>
	 * Returns the distance of the closest point on the line segment from the provided line.
	 * Does NOT assume the line's slope is normalized to one.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p	The point. Not modified.
	 * @return Distance the closest point on the line is away from the point.
	 */
	public static double distance( LineParametric2D_F64 line,
								   Point2D_F64 p ) {
		return Math.sqrt(distanceSq(line, p));
	}

	/**
	 * <p>
	 * Returns the square of the distance to the closest point on the line segment from the provided line.
	 * Using this function avoids the expensive square root computation.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p	The point. Not modified.
	 * @return Distance squared to the closest point on the line is away from the point.
	 */
	public static double distanceSq( LineParametric2D_F64 line,
									 Point2D_F64 p ) {
		double t = ClosestPoint2D_F64.closestPointT( line, p );

		double a = line.slope.x * t + line.p.x;
		double b = line.slope.y * t + line.p.y;

		double dx = p.x - a;
		double dy = p.y - b;

		return dx * dx + dy * dy;
	}

	/**
	 * <p>
	 * Returns the distance the closest point on a line segment is from the specified point.
	 * The closest point is bounded to be along the line segment.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p The point. Not modified.
	 * @return Distance the closest point on the line is away from the point.
	 */
	public static double distance( LineSegment2D_F64 line,
								   Point2D_F64 p ) {
		double a = line.b.x - line.a.x;
		double b = line.b.y - line.a.y;

		double t = a * ( p.x - line.a.x ) + b * ( p.y - line.a.y );
		t /= ( a * a + b * b );

		// if the point of intersection is past the end points return the distance
		// from the closest end point
		if( t < 0 ) {
			return UtilPoint2D_F64.distance( line.a.x, line.a.y, p.x, p.y );
		} else if( t > 1.0 )
			return UtilPoint2D_F64.distance( line.b.x, line.b.y, p.x, p.y );

		// return the distance of the closest point on the line
		return UtilPoint2D_F64.distance( line.a.x + t * a, line.a.y + t * b, p.x, p.y );
	}

	/**
	 * <p>
	 * Returns the distance the closest point the line is from the specified point.
	 * </p>
	 *
	 * @param line A line. Not modified.
	 * @param p The point. Not modified.
	 * @return Distance of the closest point on the line to the specified point.
	 */
	public static double distance( LineGeneral2D_F64 line , Point2D_F64 p ) {
		return (Math.abs(line.A*p.x + line.B*p.y + line.C) / Math.sqrt( line.A*line.A + line.B*line.B ));
	}
}
