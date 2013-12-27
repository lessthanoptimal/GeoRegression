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

import georegression.struct.GeoTuple2D_F64;
import georegression.struct.point.Point2D_F64;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 *
 */
public class UtilPoint2D_F64 {

	public static List<Point2D_F64> copy( List<Point2D_F64> pts ) {
		List<Point2D_F64> ret = new ArrayList<Point2D_F64>();

		for( Point2D_F64 p : pts ) {
			ret.add( p.copy() );
		}

		return ret;
	}

	public static void noiseNormal( List<Point2D_F64> pts, double sigma, Random rand ) {
		for( Point2D_F64 p : pts ) {
			p.x += rand.nextGaussian() * sigma;
			p.y += rand.nextGaussian() * sigma;
		}
	}

	public static double distance( double x0, double y0, double x1, double y1 ) {
		double dx = x1 - x0;
		double dy = y1 - y0;

		return Math.sqrt( dx * dx + dy * dy );
	}

	public static double distanceSq( double x0, double y0, double x1, double y1 ) {
		double dx = x1 - x0;
		double dy = y1 - y0;

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
	public static Point2D_F64 mean( List<Point2D_F64> list , Point2D_F64 mean ) {
		if( mean == null )
			mean = new Point2D_F64();

		double x = 0;
		double y = 0;

		for( Point2D_F64 p : list ) {
			x += p.getX();
			y += p.getY();
		}

		x /= list.size();
		y /= list.size();

		mean.set(x, y);
		return mean;
	}

	public static List<Point2D_F64> random( double min, double max, int num, Random rand ) {
		List<Point2D_F64> ret = new ArrayList<Point2D_F64>();

		double d = max - min;

		for( int i = 0; i < num; i++ ) {
			Point2D_F64 p = new Point2D_F64();
			p.x = rand.nextDouble() * d + min;
			p.y = rand.nextDouble() * d + min;

			ret.add( p );
		}

		return ret;
	}

	public static boolean isEquals( GeoTuple2D_F64 a, GeoTuple2D_F64 b, double tol ) {
		return ( Math.abs( a.x - b.x ) <= tol && Math.abs( a.x - b.x ) <= tol );
	}
}
