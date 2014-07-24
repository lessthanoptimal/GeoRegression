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
import georegression.struct.GeoTuple3D_F64;
import georegression.struct.point.Vector3D_F64;

/**
 *
 *
 */
public class UtilTrig_F64 {


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

	public static Vector3D_F64 cross( Vector3D_F64 a, Vector3D_F64 b ) {
		double x = a.getY() * b.getZ() - a.getZ() * b.getY();
		double y = a.getZ() * b.getX() - a.getX() * b.getZ();
		double z = a.getX() * b.getY() - a.getY() * b.getX();

		return new Vector3D_F64( x, y, z );
	}

	public static double dot( double vx_a, double vy_a,
							  double vx_b, double vy_b ) {
		return vx_a * vx_b + vy_a * vy_b;
	}

	public static double dot( GeoTuple2D_F64 a , GeoTuple2D_F64 b ) {
		return a.x*b.x + a.y*b.y;
	}

	public static double dot( GeoTuple3D_F64 a , GeoTuple3D_F64 b ) {
		return a.x*b.x + a.y*b.y + a.z*b.z;
	}

	public static double acuteAngle( GeoTuple3D_F64 a , GeoTuple3D_F64 b ) {
		double dot = a.x*b.x + a.y*b.y + a.z*b.z;
		double bottom = a.norm()*b.norm();
		return Math.acos(dot/bottom);
	}

	public static double acuteAngle( double vx_a, double vy_a,
									 double vx_b, double vy_b ) {
		double r_a = Math.sqrt( vx_a * vx_a + vy_a * vy_a );
		double r_b = Math.sqrt( vx_b * vx_b + vy_b * vy_b );

		return Math.acos( ( vx_a * vx_b + vy_a * vy_b ) / ( r_a * r_b ) );
	}

	/**
	 * Normalizes the point such that the Frobenius norm is 1.
	 *
	 * @param p
	 */
	public static void normalize( GeoTuple3D_F64 p ) {
		double n = p.norm();
		p.x /= n;
		p.y /= n;
		p.z /= n;
	}
}
