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

import jgrl.struct.line.LineParametric3D_F32;
import jgrl.struct.point.Point3D_F32;


/**
 * @author Peter Abeles
 */
public class Distance3D_F32 {
	/**
	 * Distance that two lines are apart from each other.
	 */
	// todo should handle parallel lines
	public static float distance( LineParametric3D_F32 l0,
								   LineParametric3D_F32 l1 ) {
		Point3D_F32 ret = new Point3D_F32();

		ret.x = l0.p.x - l1.p.x;
		ret.y = l0.p.y - l1.p.y;
		ret.z = l0.p.z - l1.p.z;

		// this solution is from: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline3d/
		float dv01v1 = MiscOps.dot( ret, l1.slope );
		float dv1v0 = MiscOps.dot( l1.slope, l0.slope );
		float dv1v1 = MiscOps.dot( l1.slope, l1.slope );

		float t0 = dv01v1 * dv1v0 - MiscOps.dot( ret, l0.slope ) * dv1v1;
		t0 /= MiscOps.dot( l0.slope, l0.slope ) * dv1v1 - dv1v0 * dv1v0;

		// ( d1343 + mua d4321 ) / d4343
		float t1 = ( dv01v1 + t0 * dv1v0 ) / dv1v1;

		float dx = ( l0.p.x + t0 * l0.slope.x ) - ( l1.p.x + t1 * l1.slope.x );
		float dy = ( l0.p.y + t0 * l0.slope.y ) - ( l1.p.y + t1 * l1.slope.y );
		float dz = ( l0.p.z + t0 * l0.slope.z ) - ( l1.p.z + t1 * l1.slope.z );

		return (float)Math.sqrt( dx * dx + dy * dy + dz * dz );
	}

	public static float distance( LineParametric3D_F32 l,
								   Point3D_F32 p ) {
		return 0;
	}
}
