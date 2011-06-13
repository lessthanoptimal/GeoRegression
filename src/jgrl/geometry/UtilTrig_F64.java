/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.geometry;

import jgrl.struct.point.Vector3D_F64;

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

	public static double acuteAngle( double vx_a, double vy_a,
									 double vx_b, double vy_b ) {
		double r_a = Math.sqrt( vx_a * vx_a + vy_a * vy_a );
		double r_b = Math.sqrt( vx_b * vx_b + vy_b * vy_b );

		return Math.acos( ( vx_a * vx_b + vy_a * vy_b ) / ( r_a * r_b ) );
	}
}
