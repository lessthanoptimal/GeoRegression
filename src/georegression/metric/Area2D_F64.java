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

package georegression.metric;

import georegression.struct.point.Point2D_F64;

/**
 * @author Peter Abeles
 */
public class Area2D_F64 {

	/**
	 * Computes the area of an arbitrary triangle from 3-vertices.
	 *
	 * area = | a.x*(b.y - c.y) + b.x*(c.y - a.y) + c.x*(a.y - b.y) | / 2
	 *
	 * @param a Corner point 1
	 * @param b Corner point 2
	 * @param c Corner point 3
	 * @return area
	 */
	public static double triangle( Point2D_F64 a, Point2D_F64 b, Point2D_F64  c ) {
		double inner = a.x*(b.y - c.y) + b.x*(c.y - a.y) + c.x*(a.y - b.y);

		return Math.abs(inner/2.0);
	}
}
