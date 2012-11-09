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

	public static Point2D_F64 mean( List<Point2D_F64> pts ) {
		double x = 0;
		double y = 0;

		for( Point2D_F64 p : pts ) {
			x += p.getX();
			y += p.getY();
		}

		x /= pts.size();
		y /= pts.size();

		return new Point2D_F64( x, y );
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
