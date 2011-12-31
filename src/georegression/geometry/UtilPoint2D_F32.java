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
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.geometry;

import georegression.struct.GeoTuple2D_F32;
import georegression.struct.point.Point2D_F32;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 *
 */
public class UtilPoint2D_F32 {

	public static List<Point2D_F32> copy( List<Point2D_F32> pts ) {
		List<Point2D_F32> ret = new ArrayList<Point2D_F32>();

		for( Point2D_F32 p : pts ) {
			ret.add( p.copy() );
		}

		return ret;
	}

	public static void noiseNormal( List<Point2D_F32> pts, float sigma, Random rand ) {
		for( Point2D_F32 p : pts ) {
			p.x += (float)rand.nextGaussian() * sigma;
			p.y += (float)rand.nextGaussian() * sigma;
		}
	}

	public static float distance( float x0, float y0, float x1, float y1 ) {
		float dx = x1 - x0;
		float dy = y1 - y0;

		return (float)Math.sqrt( dx * dx + dy * dy );
	}

	public static Point2D_F32 mean( List<Point2D_F32> pts ) {
		float x = 0;
		float y = 0;

		for( Point2D_F32 p : pts ) {
			x += p.getX();
			y += p.getY();
		}

		x /= pts.size();
		y /= pts.size();

		return new Point2D_F32( x, y );
	}

	public static List<Point2D_F32> random( float min, float max, int num, Random rand ) {
		List<Point2D_F32> ret = new ArrayList<Point2D_F32>();

		float d = max - min;

		for( int i = 0; i < num; i++ ) {
			Point2D_F32 p = new Point2D_F32();
			p.x = rand.nextFloat() * d + min;
			p.y = rand.nextFloat() * d + min;

			ret.add( p );
		}

		return ret;
	}

	public static boolean isEquals( GeoTuple2D_F32 a, GeoTuple2D_F32 b, float tol ) {
		return ( (float)Math.abs( a.x - b.x ) <= tol && (float)Math.abs( a.x - b.x ) <= tol );
	}
}
