/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
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

import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;


/**
 * Functions related to finding the closest point(s) on one shape from another shape.
 *
 * @author Peter Abeles
 */
public class ClosestPoint3D_F32 {
	/**
	 * Returns the point which is closest to the intersection of the two lines in 3D.  If the
	 * two lines are parallel the result is undefined.
	 *
	 * @param l0  first line. Not modified.
	 * @param l1  second line. Not modified.
	 * @param ret Point of intersection. If null a new point is declared. Modified.
	 * @return Closest point between two lines.
	 */
	public static Point3D_F32 closestPoint(LineParametric3D_F32 l0,
										   LineParametric3D_F32 l1,
										   Point3D_F32 ret) {
		if( ret == null ) {
			ret = new Point3D_F32();
		}

		ret.x = l0.p.x - l1.p.x;
		ret.y = l0.p.y - l1.p.y;
		ret.z = l0.p.z - l1.p.z;

		// this solution is from: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline3d/
		float dv01v1 = MiscOps.dot( ret, l1.slope );
		float dv1v0 = MiscOps.dot( l1.slope, l0.slope );
		float dv1v1 = MiscOps.dot( l1.slope, l1.slope );

		float t0 = dv01v1 * dv1v0 - MiscOps.dot( ret, l0.slope ) * dv1v1;
		float bottom = MiscOps.dot( l0.slope, l0.slope ) * dv1v1 - dv1v0 * dv1v0;
		if( bottom == 0 )
			return null;

		t0 /= bottom;

		// ( d1343 + mua d4321 ) / d4343
		float t1 = ( dv01v1 + t0 * dv1v0 ) / dv1v1;

		ret.x = (float) 0.5f * ( ( l0.p.x + t0 * l0.slope.x ) + ( l1.p.x + t1 * l1.slope.x ) );
		ret.y = (float) 0.5f * ( ( l0.p.y + t0 * l0.slope.y ) + ( l1.p.y + t1 * l1.slope.y ) );
		ret.z = (float) 0.5f * ( ( l0.p.z + t0 * l0.slope.z ) + ( l1.p.z + t1 * l1.slope.z ) );

		return ret;
	}

	/**
	 * Finds the closest point on line lo to l1 and on l1 to l0.  The solution is returned in
	 * 'param' as a value of 't' for each line.
	 *
	 * @param l0  first line. Not modified.
	 * @param l1  second line. Not modified.
	 * @param param  param[0] for line0 location and param[1] for line1 location.
	 *
	 * @return False if the lines are parallel or true if a solution was found.
	 */
	public static boolean closestPoints( LineParametric3D_F32 l0,
										 LineParametric3D_F32 l1,
										 float param[] )
	{
		float dX = l0.p.x - l1.p.x;
		float dY = l0.p.y - l1.p.y;
		float dZ = l0.p.z - l1.p.z;

		// this solution is from: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline3d/
		float dv01v1 = MiscOps.dot( dX , dY , dZ, l1.slope );
		float dv1v0 = MiscOps.dot( l1.slope, l0.slope );
		float dv1v1 = MiscOps.dot( l1.slope, l1.slope );

		float t0 = dv01v1 * dv1v0 - MiscOps.dot( dX , dY , dZ, l0.slope ) * dv1v1;
		float bottom = MiscOps.dot( l0.slope, l0.slope ) * dv1v1 - dv1v0 * dv1v0;
		if( bottom == 0 )
			return false;

		t0 /= bottom;

		// ( d1343 + mua d4321 ) / d4343
		float t1 = ( dv01v1 + t0 * dv1v0 ) / dv1v1;

		param[0] = t0;
		param[1] = t1;

		return true;
	}

	/**
	 * Finds the closest point on a line to the specified point.
	 *
	 * @param line Line on which the closest point is being found.  Not modified.
	 * @param pt   The point whose closest point is being looked for.  Not modified.
	 * @param ret  Storage for the solution.  If null is passed in a new point is created. Modified.
	 */
	public static Point3D_F32 closestPoint(LineParametric3D_F32 line,
										   Point3D_F32 pt,
										   Point3D_F32 ret) {
		if( ret == null ) {
			ret = new Point3D_F32();
		}

		Vector3D_F32 ab = new Vector3D_F32( line.p, pt );

		float n = line.slope.norm();

		float d = line.slope.dot( ab ) / n;

		ret.x = line.p.x + d * line.slope.x / n;
		ret.y = line.p.y + d * line.slope.y / n;
		ret.z = line.p.z + d * line.slope.z / n;

		return ret;
	}

}
