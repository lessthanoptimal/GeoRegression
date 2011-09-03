/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.metric;

import jgrl.struct.line.LineParametric2D_F32;
import jgrl.struct.point.Point2D_F32;


/**
 * Functions related to finding the closest point(s) on one shape from another shape.
 *
 * @author Peter Abeles
 */
public class ClosestPoint2D_F32 {

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
	public static Point2D_F32 closestPoint( LineParametric2D_F32 line,
											Point2D_F32 p,
											Point2D_F32 storage ) {
		if( storage == null )
			storage = new Point2D_F32();

		float t = closestPointT( line, p );

		storage.x = line.p.x + line.slope.x * t;
		storage.y = line.p.y + line.slope.y * t;

		return storage;
	}

	/**
	 * <p>
	 * Computes the closest point along the line as a function of 't':<br>
	 * [x, y] = [x_0, y_0] + tÂ·[slopeX, slopeY]
	 * </p>
	 *
	 * @param line The line along which the closest point is being found. Not modified.
	 * @param p	A point. Not modified.
	 * @return Distance as a function of 't'
	 */
	public static float closestPointT( LineParametric2D_F32 line,
										Point2D_F32 p ) {
		float t = line.slope.x * ( p.x - line.p.x ) + line.slope.y * ( p.y - line.p.y );
		t /= line.slope.x * line.slope.x + line.slope.y * line.slope.y;

		return t;
	}

}
