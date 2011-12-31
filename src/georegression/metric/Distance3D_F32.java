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

package georegression.metric;

import georegression.geometry.UtilPoint3D_F32;
import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.point.Point3D_F32;


/**
 * @author Peter Abeles
 */
public class Distance3D_F32 {
	/**
	 * Distance of the closest point between two lines.  Parallel lines are correctly
	 * handled.
	 *
	 * @param l0 First line. Not modified.
	 * @param l1 Second line. Not modified.
	 * @return Distance between the closest point on both lines.
	 */
	public static float distance( LineParametric3D_F32 l0,
								   LineParametric3D_F32 l1 ) {
		float x = l0.p.x - l1.p.x;
		float y = l0.p.y - l1.p.y;
		float z = l0.p.z - l1.p.z;

		// this solution is from: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline3d/
		float dv01v1 = MiscOps.dot( x,y,z, l1.slope );
		float dv1v0 = MiscOps.dot( l1.slope, l0.slope );
		float dv1v1 = MiscOps.dot( l1.slope, l1.slope );

		float bottom = MiscOps.dot( l0.slope, l0.slope ) * dv1v1 - dv1v0 * dv1v0;
		float t0;

		if( bottom == 0 ) {
			// handle parallel lines
			t0 = 0;
		} else {
			t0 = (dv01v1 * dv1v0 - MiscOps.dot( x,y,z, l0.slope ) * dv1v1)/bottom;
		}

		// ( d1343 + mua d4321 ) / d4343
		float t1 = ( dv01v1 + t0 * dv1v0 ) / dv1v1;

		float dx = ( l0.p.x + t0 * l0.slope.x ) - ( l1.p.x + t1 * l1.slope.x );
		float dy = ( l0.p.y + t0 * l0.slope.y ) - ( l1.p.y + t1 * l1.slope.y );
		float dz = ( l0.p.z + t0 * l0.slope.z ) - ( l1.p.z + t1 * l1.slope.z );

		return (float)Math.sqrt( dx * dx + dy * dy + dz * dz );
	}

	/**
	 * Distance from the point to the closest point on the line.
	 *
	 * @param l Line. Not modified.
	 * @param p Point. Not modified.
	 * @return distance.
	 */
	public static float distance( LineParametric3D_F32 l,
								   Point3D_F32 p ) {

		float x = l.p.x - p.x;
		float y = l.p.y - p.y;
		float z = l.p.z - p.z;

		float c = UtilPoint3D_F32.norm( x , y , z );

		float b = MiscOps.dot(x,y,z,l.slope)/l.slope.norm();

		return (float)Math.sqrt(c*c-b*b);
	}
}
