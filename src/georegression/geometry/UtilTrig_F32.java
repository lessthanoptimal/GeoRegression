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

import georegression.struct.GeoTuple2D_F32;
import georegression.struct.GeoTuple3D_F32;
import georegression.struct.point.Vector3D_F32;

/**
 *
 *
 */
public class UtilTrig_F32 {


	public static float distance( float x0, float y0, float x1, float y1 ) {
		float dx = x1 - x0;
		float dy = y1 - y0;

		return (float)Math.sqrt( dx * dx + dy * dy );
	}

	public static float distanceSq( float x0, float y0, float x1, float y1 ) {
		float dx = x1 - x0;
		float dy = y1 - y0;

		return dx * dx + dy * dy;
	}

	public static Vector3D_F32 cross( Vector3D_F32 a, Vector3D_F32 b ) {
		float x = a.getY() * b.getZ() - a.getZ() * b.getY();
		float y = a.getZ() * b.getX() - a.getX() * b.getZ();
		float z = a.getX() * b.getY() - a.getY() * b.getX();

		return new Vector3D_F32( x, y, z );
	}

	public static float dot( float vx_a, float vy_a,
							  float vx_b, float vy_b ) {
		return vx_a * vx_b + vy_a * vy_b;
	}

	public static float dot( GeoTuple2D_F32 a , GeoTuple2D_F32 b ) {
		return a.x*b.x + a.y*b.y;
	}

	public static float dot( GeoTuple3D_F32 a , GeoTuple3D_F32 b ) {
		return a.x*b.x + a.y*b.y + a.z*b.z;
	}

	public static float acuteAngle( GeoTuple3D_F32 a , GeoTuple3D_F32 b ) {
		float dot = a.x*b.x + a.y*b.y + a.z*b.z;
		float bottom = a.norm()*b.norm();
		return (float)Math.acos(dot/bottom);
	}

	public static float acuteAngle( float vx_a, float vy_a,
									 float vx_b, float vy_b ) {
		float r_a = (float)Math.sqrt( vx_a * vx_a + vy_a * vy_a );
		float r_b = (float)Math.sqrt( vx_b * vx_b + vy_b * vy_b );

		return (float)Math.acos( ( vx_a * vx_b + vy_a * vy_b ) / ( r_a * r_b ) );
	}

	/**
	 * Normalizes the point such that the Frobenius norm is 1.
	 *
	 * @param p
	 */
	public static void normalize( GeoTuple3D_F32 p ) {
		float n = p.norm();
		p.x /= n;
		p.y /= n;
		p.z /= n;
	}
}
