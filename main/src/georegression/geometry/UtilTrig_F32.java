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
