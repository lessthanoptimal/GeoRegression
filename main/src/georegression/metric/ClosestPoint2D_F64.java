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

import georegression.fitting.curves.ClosestPointEllipseAngle_F64;
import georegression.misc.GrlConstants;
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import org.jetbrains.annotations.Nullable;


/**
 * Functions related to finding the closest point(s) on one shape from another shape.
 *
 * @author Peter Abeles
 */
public class ClosestPoint2D_F64 {

	/**
	 * <p>
	 * Finds the closest point on 'line' to the specified point.
	 * </p>
	 *
	 * @param line Line along which the closest point is to be found.
	 * @param p Point.
	 * @param output Where the solution is stored. If null a new instance is created. Modified.
	 * @return Closest point on the line.
	 */
	public static Point2D_F64 closestPoint( LineGeneral2D_F64 line,
											Point2D_F64 p,
											@Nullable Point2D_F64 output ) {
		if( output == null )
			output = new Point2D_F64();

		double AA = line.A*line.A;
		double AB = line.A*line.B;
		double BB = line.B*line.B;

		output.y = AA*p.y - AB*p.x - line.B*line.C;
		output.y /= AA+BB;

		output.x = BB*p.x - AB*p.y - line.A*line.C;
		output.x /= AA+BB;

		return output;
	}

	/**
	 * <p>
	 * Finds the closest point on 'line' to the specified point.
	 * </p>
	 *
	 * @param line	Line along which the closest point is being found.
	 * @param p	   Point.
	 * @param output Where the solution is stored. If null a new instance is created. Modified.
	 * @return Closest point on the line.
	 */
	public static Point2D_F64 closestPoint( LineParametric2D_F64 line,
											Point2D_F64 p,
											@Nullable Point2D_F64 output ) {
		if( output == null )
			output = new Point2D_F64();

		double t = closestPointT( line, p );

		output.x = line.p.x + line.slope.x * t;
		output.y = line.p.y + line.slope.y * t;

		return output;
	}

	/**
	 * <p>
	 * Computes the closest point along the line as a function of 't':<br>
	 * [x, y] = [x_0, y_0] + t·[slopeX, slopeY]
	 * </p>
	 *
	 * @param line The line along which the closest point is being found. Not modified.
	 * @param p	A point. Not modified.
	 * @return Distance as a function of 't'
	 */
	public static double closestPointT( LineParametric2D_F64 line,
										Point2D_F64 p ) {
		double t = line.slope.x * ( p.x - line.p.x ) + line.slope.y * ( p.y - line.p.y );
		t /= line.slope.x * line.slope.x + line.slope.y * line.slope.y;

		return t;
	}

	/**
	 * <p>
	 * Computes the closest point along the line as a function of 't':<br>
	 * [x, y] = [x_0, y_0] + t·[slopeX/scale, slopeY/scale]
	 * </p>
	 *
	 * @param line The line along which the closest point is being found. Not modified.
	 * @param scale Scale factor. used to avoid numerical issues
	 * @param p	A point. Not modified.
	 * @return Distance as a function of 't'
	 */
	public static double closestPointT( LineParametric2D_F64 line,
										Point2D_F64 p , double scale ) {
		double sx = line.slope.x/scale;
		double sy = line.slope.y/scale;

		double t = sx * ( p.x - line.p.x ) + sy * ( p.y - line.p.y );
		t /= sx * sx + sy * sy;

		return t;
	}

	/**
	 * <p>
	 * Computes the closest point along the line as a function of 't':<br>
	 * [x, y] = [x_0, y_0] + t·[slopeX, slopeY]
	 * </p>
	 *
	 * @param line The line along which the closest point is being found. Not modified.
	 * @param x Point's x-coordinate
	 * @param y Point's y-coordinate
	 * @return Distance as a function of 't'
	 */
	public static double closestPointT( LineParametric2D_F64 line, double x, double y) {
		double t = line.slope.x * ( x - line.p.x ) + line.slope.y * ( y - line.p.y );
		t /= line.slope.x * line.slope.x + line.slope.y * line.slope.y;

		return t;
	}

	/**
	 * <p>
	 * Computes the closest point along the line as a function of 't':<br>
	 * [x, y] = [x_0, y_0] + t·[slopeX, slopeY]
	 * </p>
	 *
	 * @param line The line along which the closest point is being found. Not modified.
	 * @param scale Scale factor. used to avoid numerical issues
	 * @param x Point's x-coordinate
	 * @param y Point's y-coordinate
	 * @return Distance as a function of 't'
	 */
	public static double closestPointT( LineParametric2D_F64 line, double x, double y, double scale ) {
		double sx = line.slope.x/scale;
		double sy = line.slope.y/scale;

		double t = sx * ( x - line.p.x ) + sy * ( y - line.p.y );
		t /= sx * sx + sy * sy;

		return t;
	}

	/**
	 * Finds the closest point on the line segment to the provided point 'p'.
	 *
	 * @param line Line segment
	 * @param p Point`
	 * @param output Optional storage for the closet point on the line to p. If null a new instance is created.
	 * @return Closest point on the line to the point
	 */
	public static Point2D_F64 closestPoint( LineSegment2D_F64 line,
											Point2D_F64 p,
											@Nullable Point2D_F64 output ) {

		if( output == null )
			output = new Point2D_F64();

		double slopeX = line.b.x - line.a.x;
		double slopeY = line.b.y - line.a.y;

		double t = slopeX * ( p.x - line.a.x ) + slopeY * ( p.y - line.a.y );
		t /= slopeX*slopeX + slopeY*slopeY;

		if( t < 0 )
			t = 0;
		else if( t > 1 )
			t = 1;

		output.x = line.a.x + slopeX * t;
		output.y = line.a.y + slopeY * t;

		return output;
	}

	/**
	 * Computes the closest point on an ellipse to the provided point. If there are multiple solutions then one
	 * is arbitrarily chosen.
	 *
	 * NOTE: When optimizing consider calling {@link ClosestPointEllipseAngle_F64} directly instead.
	 *
	 * @param ellipse Ellipse
	 * @param p Point
	 * @return Closest point on the ellipse
	 */
	public static Point2D_F64 closestPoint( EllipseRotated_F64 ellipse , Point2D_F64 p ) {
		ClosestPointEllipseAngle_F64 alg = new ClosestPointEllipseAngle_F64(GrlConstants.TEST_F64,30);
		alg.setEllipse(ellipse);
		alg.process(p);
		return alg.getClosest();
	}

}
