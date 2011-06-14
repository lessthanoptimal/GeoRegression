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

package jgrl.metric;

import jgrl.geometry.UtilPoint3D_F64;
import jgrl.struct.line.LineParametric3D_F64;
import jgrl.struct.point.Point3D_F64;


/**
 * @author Peter Abeles
 */
public class Distance3D_F64 {
	/**
	 * Distance of the closest point between two lines.  Parallel lines are correctly
	 * handled.
	 *
	 * @param l0 First line. Not modified.
	 * @param l1 Second line. Not modified.
	 * @return Distance between the closest point on both lines.
	 */
	public static double distance( LineParametric3D_F64 l0,
								   LineParametric3D_F64 l1 ) {
		double x = l0.p.x - l1.p.x;
		double y = l0.p.y - l1.p.y;
		double z = l0.p.z - l1.p.z;

		// this solution is from: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline3d/
		double dv01v1 = MiscOps.dot( x,y,z, l1.slope );
		double dv1v0 = MiscOps.dot( l1.slope, l0.slope );
		double dv1v1 = MiscOps.dot( l1.slope, l1.slope );

		double bottom = MiscOps.dot( l0.slope, l0.slope ) * dv1v1 - dv1v0 * dv1v0;
		double t0;

		if( bottom == 0 ) {
			// handle parallel lines
			t0 = 0;
		} else {
			t0 = (dv01v1 * dv1v0 - MiscOps.dot( x,y,z, l0.slope ) * dv1v1)/bottom;
		}

		// ( d1343 + mua d4321 ) / d4343
		double t1 = ( dv01v1 + t0 * dv1v0 ) / dv1v1;

		double dx = ( l0.p.x + t0 * l0.slope.x ) - ( l1.p.x + t1 * l1.slope.x );
		double dy = ( l0.p.y + t0 * l0.slope.y ) - ( l1.p.y + t1 * l1.slope.y );
		double dz = ( l0.p.z + t0 * l0.slope.z ) - ( l1.p.z + t1 * l1.slope.z );

		return Math.sqrt( dx * dx + dy * dy + dz * dz );
	}

	/**
	 * Distance from the point to the closest point on the line.
	 *
	 * @param l Line. Not modified.
	 * @param p Point. Not modified.
	 * @return distance.
	 */
	public static double distance( LineParametric3D_F64 l,
								   Point3D_F64 p ) {

		double x = l.p.x - p.x;
		double y = l.p.y - p.y;
		double z = l.p.z - p.z;

		double c = UtilPoint3D_F64.norm( x , y , z );

		double b = MiscOps.dot(x,y,z,l.slope)/l.slope.norm();

		return Math.sqrt(c*c-b*b);
	}
}
