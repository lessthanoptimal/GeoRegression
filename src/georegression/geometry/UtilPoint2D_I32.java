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

package georegression.geometry;

import georegression.struct.point.Point2D_I32;

import java.util.List;

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

	public static int distanceSq( Point2D_I32 a , Point2D_I32 b ) {
		int dx = b.x - a.x;
		int dy = b.y - a.y;

		return dx * dx + dy * dy;
	}

	public static double distance( int x0, int y0, int x1, int y1 ) {
		int dx = x1 - x0;
		int dy = y1 - y0;

		return Math.sqrt( dx * dx + dy * dy );
	}

	public static int distanceSq( int x0, int y0, int x1, int y1 ) {
		int dx = x1 - x0;
		int dy = y1 - y0;

		return dx * dx + dy * dy;
	}

	/**
	 * Finds the point which has the mean location of all the points in the list. This is also known
	 * as the centroid.
	 *
	 * @param list List of points
	 * @param mean Storage for mean point.  If null then a new instance will be declared
	 * @return The found mean
	 */
	public static Point2D_I32 mean(List<Point2D_I32> list, Point2D_I32 mean) {
		if( mean == null )
			mean = new Point2D_I32();

		int sumX = 0, sumY = 0;
		int N = list.size();

		for( int i = 0; i < N; i++ ) {
			Point2D_I32 p = list.get(i);

			sumX += p.x;
			sumY += p.y;
		}

		mean.x = sumX/N;
		mean.y = sumY/N;

		return mean;
	}
}
