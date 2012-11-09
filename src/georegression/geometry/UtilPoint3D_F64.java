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

import georegression.struct.point.Point3D_F64;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 *
 */
public class UtilPoint3D_F64 {


	public static double norm( double x , double y , double z ) {
		return Math.sqrt(x*x + y*y + z*z);
	}

	public static List<Point3D_F64> copy( List<Point3D_F64> pts ) {
		List<Point3D_F64> ret = new ArrayList<Point3D_F64>();

		for( Point3D_F64 p : pts ) {
			ret.add( p.copy() );
		}

		return ret;
	}

	public static void noiseNormal( List<Point3D_F64> pts, double sigma, Random rand ) {
		for( Point3D_F64 p : pts ) {
			p.x += rand.nextGaussian() * sigma;
			p.y += rand.nextGaussian() * sigma;
			p.z += rand.nextGaussian() * sigma;
		}
	}

	public static List<Point3D_F64> random( double min, double max, int num, Random rand ) {
		List<Point3D_F64> ret = new ArrayList<Point3D_F64>();

		double d = max - min;

		for( int i = 0; i < num; i++ ) {
			Point3D_F64 p = new Point3D_F64();
			p.x = rand.nextDouble() * d + min;
			p.y = rand.nextDouble() * d + min;
			p.z = rand.nextDouble() * d + min;

			ret.add( p );
		}

		return ret;
	}

	public static Point3D_F64 mean( List<Point3D_F64> points ) {
		Point3D_F64 mean = new Point3D_F64();

		double x = 0, y = 0, z = 0;

		for( Point3D_F64 p : points ) {
			x += p.x;
			y += p.y;
			z += p.z;
		}

		mean.x = x / points.size();
		mean.y = y / points.size();
		mean.z = z / points.size();

		return mean;
	}

	public static Point3D_F64 mean( List<Point3D_F64> points , int num ) {
		Point3D_F64 mean = new Point3D_F64();

		double x = 0, y = 0, z = 0;

		for( int i = 0; i < num; i++ ) {
			Point3D_F64 p = points.get(i);
			x += p.x;
			y += p.y;
			z += p.z;
		}

		mean.x = x / num;
		mean.y = y / num;
		mean.z = z / num;

		return mean;
	}
}
