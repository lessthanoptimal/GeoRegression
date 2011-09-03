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

package georegression.geometry;

import georegression.struct.point.Point2D_I32;

/**
 *
 *
 */
public class UtilPoint2D_I32 {

	public static double distance( Point2D_I32 a , Point2D_I32 b ) {
		int dx = b.x - a.x;
		int dy = b.y - a.y;

		return Math.sqrt( dx * dx + dy * dy );
	}

	public static double distance( int x0, int y0, int x1, int y1 ) {
		int dx = x1 - x0;
		int dy = y1 - y0;

		return Math.sqrt( dx * dx + dy * dy );
	}
}
