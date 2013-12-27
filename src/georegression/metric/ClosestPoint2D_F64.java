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

package georegression.metric;

import georegression.fitting.ellipse.ClosestPointEllipseAngle_F64;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseRotated_F64;


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
	 * @param line	Line along which the closest point is being found.
	 * @param p	   Point.
	 * @param storage Where the solution is stored.  If null a new instance is created. Modified.
	 * @return Closest point on the line.
	 */
	public static Point2D_F64 closestPoint( LineParametric2D_F64 line,
											Point2D_F64 p,
											Point2D_F64 storage ) {
		if( storage == null )
			storage = new Point2D_F64();

		double t = closestPointT( line, p );

		storage.x = line.p.x + line.slope.x * t;
		storage.y = line.p.y + line.slope.y * t;

		return storage;
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
	 * Computes the closest point on an ellipse to the provided point.  If there are multiple solutions then one
	 * is arbitrarily chosen.
	 *
	 * NOTE: When optimizing consider calling {@link georegression.fitting.ellipse.ClosestPointEllipseAngle_F64} directly instead.
	 *
	 * @param ellipse Ellipse
	 * @param p Point
	 * @return Closest point on the ellipse
	 */
	public static Point2D_F64 closestPoint( EllipseRotated_F64 ellipse , Point2D_F64 p ) {
		ClosestPointEllipseAngle_F64 alg = new ClosestPointEllipseAngle_F64(GrlConstants.DOUBLE_TEST_TOL,30);
		alg.setEllipse(ellipse);
		alg.process(p);
		return alg.getClosest();
	}

}
