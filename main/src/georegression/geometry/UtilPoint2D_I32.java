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
