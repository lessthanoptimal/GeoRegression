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


import georegression.geometry.GeometryMath_F64;
import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Cube3D_F64;
import georegression.struct.shapes.CubeLength3D_F64;

/**
 * @author Peter Abeles
 */
public class Intersection3D_F64 {

	/**
	 * Finds the intersection of a line and a plane.  Returns true if they intersect at a unique point or false if
	 * there is no intersection or an infinite number of intersections.
	 *
	 * @param plane Plane
	 * @param line Line
	 * @return True if the intersection is at a unique point.  If false then no intersection or infinite.
	 */
	public static boolean intersect( PlaneNormal3D_F64 plane , LineParametric3D_F64 line , Point3D_F64 intersection ) {
		double dx = plane.p.x - line.p.x;
		double dy = plane.p.y - line.p.y;
		double dz = plane.p.z - line.p.z;

		double top = dx*plane.n.x + dy*plane.n.y + dz*plane.n.z;
		double bottom = line.slope.dot(plane.n);

		if( bottom == 0 )
			return false;

		double d = top/bottom;

		intersection.x = line.p.x + d*line.slope.x;
		intersection.y = line.p.y + d*line.slope.y;
		intersection.z = line.p.z + d*line.slope.z;

		return true;
	}

	/**
	 * Finds the intersection of a line and a plane.  Returns true if they intersect at a unique point or false if
	 * there is no intersection or an infinite number of intersections.
	 *
	 * @param plane Plane
	 * @param line Line
	 * @return True if the intersection is at a unique point.  If false then no intersection or infinite.
	 */
	public static boolean intersect( PlaneGeneral3D_F64 plane , LineParametric3D_F64 line , Point3D_F64 intersection ) {

		double top = plane.D - plane.A*line.p.x - plane.B*line.p.y - plane.C*line.p.z;
		double bottom = plane.A*line.slope.x + plane.B*line.slope.y + plane.C*line.slope.z;

		if( bottom == 0 )
			return false;

		double d = top/bottom;

		intersection.x = line.p.x + d*line.slope.x;
		intersection.y = line.p.y + d*line.slope.y;
		intersection.z = line.p.z + d*line.slope.z;

		return true;
	}

	/**
	 * Finds the line which is the intersection between the two planes.  For a valid solution to be returned
	 * the planes must not be parallel to each other.  If the planes are parallel then the slope of the returned line
	 * will have a value of zero for each element.
	 *
	 * @param a Plane
	 * @param b Plane
	 * @param line (Output) Intersection.
	 * @return true if they intersect ata line or false if not
	 */
	public static boolean intersect( PlaneGeneral3D_F64 a , PlaneGeneral3D_F64 b , LineParametric3D_F64 line ) {

		// Line's slope is the cross product of the two normal vectors
		GeometryMath_F64.cross(a.A,a.B,a.C,b.A,b.B,b.C,line.slope);

		if( line.slope.normSq() == 0 )
			return false;

		// Closest point on plane 'a' to origin (0,0,0)
		double n2 = a.A*a.A + a.B*a.B + a.C*a.C;

		double closestX = a.A*a.D/n2;
		double closestY = a.B*a.D/n2;
		double closestZ = a.C*a.D/n2;

		// Cross product between normal of 'a' and the line's slope.  This points towards the intersection
		double slopeX = a.B * line.slope.z - a.C * line.slope.y;
		double slopeY = a.C * line.slope.x - a.A * line.slope.z;
		double slopeZ = a.A * line.slope.y - a.B * line.slope.x;

		// Now find the intersection of the plane and a line containing point 'closest' and pointing towards the
		// the intersection.
		double top = b.D - b.A*closestX - b.B*closestY - b.C*closestZ;
		double bottom = b.A*slopeX + b.B*slopeY + b.C*slopeZ;

		double d = top/bottom;

		line.p.x = closestX + d*slopeX;
		line.p.y = closestY + d*slopeY;
		line.p.z = closestZ + d*slopeZ;

		return true;
	}

	/**
	 * Returns true if the point is contained inside the cube. The point is considered to be inside the cube
	 * if the following test passes for each dimension.  cube.x <= point.x < cube.x + cube.lengthX
	 *
	 *
	 * @param cube Cube
	 * @param point Point which is tested to see if it is inside the cube
	 * @return true for inside and false for not
	 */
	public static boolean contained( CubeLength3D_F64 cube , Point3D_F64 point ) {

		return( cube.p.x <= point.x && point.x < cube.p.x + cube.lengthX &&
				cube.p.y <= point.y && point.y < cube.p.y + cube.lengthY &&
				cube.p.z <= point.z && point.z < cube.p.z + cube.lengthZ );
	}

	/**
	 * Returns true if the point is contained inside the cube. The point is considered to be inside the cube
	 * if the following test passes for each dimension.  cube.x <= point.x < cube.x + cube.lengthX
	 *
	 *
	 * @param cube Cube
	 * @param point Point which is tested to see if it is inside the cube
	 * @return true for inside and false for not
	 */
	public static boolean contained( Cube3D_F64 cube , Point3D_F64 point ) {

		return( cube.p0.x <= point.x && point.x < cube.p1.x &&
				cube.p0.y <= point.y && point.y < cube.p1.y &&
				cube.p0.z <= point.z && point.z < cube.p1.z );
	}

}
