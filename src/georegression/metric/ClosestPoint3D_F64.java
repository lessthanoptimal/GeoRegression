/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
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

package georegression.metric;

import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.point.Point3D_F64;


/**
 * Functions related to finding the closest point(s) on one shape from another shape.
 *
 * @author Peter Abeles
 */
public class ClosestPoint3D_F64 {
	/**
	 * Returns the point which is closest to the intersection of the two lines in 3D.  If the
	 * two lines are parallel the result is undefined.
	 *
	 * @param l0  first line. Not modified.
	 * @param l1  second line. Not modified.
	 * @param ret Point of intersection. If null a new point is declared. Modified.
	 * @return Closest point between two lines.
	 */
	public static Point3D_F64 closestPoint(LineParametric3D_F64 l0,
										   LineParametric3D_F64 l1,
										   Point3D_F64 ret) {
		if( ret == null ) {
			ret = new Point3D_F64();
		}

		ret.x = l0.p.x - l1.p.x;
		ret.y = l0.p.y - l1.p.y;
		ret.z = l0.p.z - l1.p.z;

		// this solution is from: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline3d/
		double dv01v1 = MiscOps.dot( ret, l1.slope );
		double dv1v0 = MiscOps.dot( l1.slope, l0.slope );
		double dv1v1 = MiscOps.dot( l1.slope, l1.slope );

		double t0 = dv01v1 * dv1v0 - MiscOps.dot( ret, l0.slope ) * dv1v1;
		double bottom = MiscOps.dot( l0.slope, l0.slope ) * dv1v1 - dv1v0 * dv1v0;
		if( bottom == 0 )
			return null;

		t0 /= bottom;

		// ( d1343 + mua d4321 ) / d4343
		double t1 = ( dv01v1 + t0 * dv1v0 ) / dv1v1;

		ret.x = (double) 0.5 * ( ( l0.p.x + t0 * l0.slope.x ) + ( l1.p.x + t1 * l1.slope.x ) );
		ret.y = (double) 0.5 * ( ( l0.p.y + t0 * l0.slope.y ) + ( l1.p.y + t1 * l1.slope.y ) );
		ret.z = (double) 0.5 * ( ( l0.p.z + t0 * l0.slope.z ) + ( l1.p.z + t1 * l1.slope.z ) );

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
	public static boolean closestPoints( LineParametric3D_F64 l0,
										 LineParametric3D_F64 l1,
										 double param[] )
	{
		double dX = l0.p.x - l1.p.x;
		double dY = l0.p.y - l1.p.y;
		double dZ = l0.p.z - l1.p.z;

		// this solution is from: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline3d/
		double dv01v1 = MiscOps.dot( dX , dY , dZ, l1.slope );
		double dv1v0 = MiscOps.dot( l1.slope, l0.slope );
		double dv1v1 = MiscOps.dot( l1.slope, l1.slope );

		double t0 = dv01v1 * dv1v0 - MiscOps.dot( dX , dY , dZ, l0.slope ) * dv1v1;
		double bottom = MiscOps.dot( l0.slope, l0.slope ) * dv1v1 - dv1v0 * dv1v0;
		if( bottom == 0 )
			return false;

		t0 /= bottom;

		// ( d1343 + mua d4321 ) / d4343
		double t1 = ( dv01v1 + t0 * dv1v0 ) / dv1v1;

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
	public static Point3D_F64 closestPoint(LineParametric3D_F64 line,
										   Point3D_F64 pt,
										   Point3D_F64 ret) {
		if( ret == null ) {
			ret = new Point3D_F64();
		}

		double dx = pt.x - line.p.x;
		double dy = pt.y - line.p.y;
		double dz = pt.z - line.p.z;

		double n = line.slope.norm();

		double d = (line.slope.x*dx + line.slope.y*dy + line.slope.z*dz) / n;

		ret.x = line.p.x + d * line.slope.x / n;
		ret.y = line.p.y + d * line.slope.y / n;
		ret.z = line.p.z + d * line.slope.z / n;

		return ret;
	}

	/**
	 * Finds the closest point on the plane to the specified point.
	 *
	 * @param plane The plane
	 * @param point The point
	 * @param found (Optional) Storage for the closest point.  If null a new point is declared internally.
	 * @return The closest point
	 */
	public static Point3D_F64 closestPoint( PlaneGeneral3D_F64 plane , Point3D_F64 point , Point3D_F64 found ) {
		if( found == null )
			found = new Point3D_F64();

		double top = plane.A*point.x + plane.B*point.y + plane.C*point.z - plane.D;

		double n2 = plane.A*plane.A + plane.B*plane.B + plane.C*plane.C;

		found.x = point.x - plane.A*top/n2;
		found.y = point.y - plane.B*top/n2;
		found.z = point.z - plane.C*top/n2;

		return found;
	}

}
