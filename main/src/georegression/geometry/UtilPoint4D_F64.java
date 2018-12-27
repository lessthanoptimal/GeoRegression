/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Point4D_F64;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Peter Abeles
 */
public class UtilPoint4D_F64 {

	/**
	 * Checks to see if the homogenous 3D point lies on the plane at infinity
	 *
	 * @param p (Input) Homogenous point
	 * @param tol (Input) tolerance. Try EPS
	 * @return true if on plane at infinity
	 */
	public static boolean isInfiniteH(Point4D_F64 p , double tol ) {
		double n = Math.sqrt(p.x*p.x + p.y*p.y + p.z*p.z);
		return Math.abs(p.w) <= n*tol;
	}

	/**
	 * Normally distributed random point with the mean (center) specified and each axis having the same
	 * standard deviation.
	 * @param center
	 * @param stdev
	 * @param num
	 * @param rand
	 * @return
	 */
	public static List<Point4D_F64> randomN( Point4D_F64 center , double stdev, int num, Random rand ) {
		List<Point4D_F64> ret = new ArrayList<>();

		for( int i = 0; i < num; i++ ) {
			Point4D_F64 p = new Point4D_F64();
			p.x = center.x + rand.nextGaussian() * stdev;
			p.y = center.y + rand.nextGaussian() * stdev;
			p.z = center.z + rand.nextGaussian() * stdev;
			p.w = center.w + rand.nextGaussian() * stdev;

			ret.add( p );
		}

		return ret;
	}


	/**
	 * Normally distributed homogenous 3D point. w is fixed
	 *
	 */
	public static List<Point4D_F64> randomN( Point3D_F64 center , double w, double stdev, int num, Random rand ) {
		List<Point4D_F64> ret = new ArrayList<>();

		for( int i = 0; i < num; i++ ) {
			Point4D_F64 p = new Point4D_F64();
			p.x = center.x + rand.nextGaussian() * stdev;
			p.y = center.y + rand.nextGaussian() * stdev;
			p.z = center.z + rand.nextGaussian() * stdev;
			p.w = w;

			ret.add( p );
		}

		return ret;
	}

	public static List<Point4D_F64> random(double min, double max, int num, Random rand ) {
		List<Point4D_F64> ret = new ArrayList<>();

		double d = max - min;

		for( int i = 0; i < num; i++ ) {
			Point4D_F64 p = new Point4D_F64();
			p.x = rand.nextDouble() * d + min;
			p.y = rand.nextDouble() * d + min;
			p.z = rand.nextDouble() * d + min;
			p.w = rand.nextDouble() * d + min;

			ret.add( p );
		}

		return ret;
	}

	/**
	 * Converts a point from homogenous coordinates into Euclidean
	 * @param p 3D point in homogenous coordinates
	 * @return 3D point
	 */
	public static Point3D_F64 h_to_e( Point4D_F64 p ) {
		Point3D_F64 out = new Point3D_F64();
		h_to_e(p,out);
		return out;
	}
	public static void h_to_e(Point4D_F64 p , Point3D_F64 out ) {
		out.x = p.x/p.w;
		out.y = p.y/p.w;
		out.z = p.z/p.w;
	}

}
