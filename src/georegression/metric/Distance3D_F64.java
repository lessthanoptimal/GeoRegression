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
import georegression.struct.shapes.Cylinder3D_F64;
import georegression.struct.shapes.Sphere3D_F64;


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

		// round off error can make distanceSq go negative when it is very close to zero
		double distanceSq = dx * dx + dy * dy + dz * dz;
		if( distanceSq < 0 )
			return 0;
		else
			return Math.sqrt( distanceSq );
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

		double cc = x*x + y*y + z*z;

		// could avoid a square root here by computing b*b directly
		// however that is most likely more prone to numerical overflow since the numerator will need to be squared
		// before division can reduce its "power"
		double b = MiscOps.dot(x,y,z,l.slope)/l.slope.norm();

		double distanceSq = cc-b*b;

		// round off error can make distanceSq go negative when it is very close to zero
		if( distanceSq < 0 ) {
			return 0;
		} else {
			return Math.sqrt(distanceSq);
		}
	}

	/**
	 * Distance between a plane and a point. A signed distance is returned, where a positive value is returned if
	 * the point is on the same side of the plane as the normal and the opposite if it's on the other.
	 *
	 * @param plane The plane
	 * @param point The point
	 * @return Signed distance
	 */
	public static double distance( PlaneGeneral3D_F64 plane , Point3D_F64 point ) {
		double top = plane.A*point.x + plane.B*point.y + plane.C*point.z - plane.D;

		return top / Math.sqrt( plane.A*plane.A + plane.B*plane.B + plane.C*plane.C);
	}

	/**
	 * Returns the signed distance a point is from the sphere's surface.  If the point is outside of the sphere
	 * it's distance will be positive.  If it is inside it will be negative.
	 * <p></p>
	 * distance = ||sphere.center - point|| - r
	 *
	 * @param sphere The sphere
	 * @param point The point
	 * @return Signed distance
	 */
	public static double distance( Sphere3D_F64 sphere , Point3D_F64 point ) {

		double r = point.distance(sphere.center);
		return r-sphere.radius;
	}

	/**
	 * Returns the signed distance a point is from the cylinder's surface.  If the point is outside of the cylinder
	 * it's distance will be positive.  If it is inside it will be negative.
	 *
	 * @param cylinder The cylinder
	 * @param point The point
	 * @return Signed distance
	 */
	public static double distance( Cylinder3D_F64 cylinder, Point3D_F64 point ) {

		double r = Distance3D_F64.distance(cylinder.line,point);

		return r - cylinder.radius;
	}
}
