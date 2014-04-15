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

package georegression.metric;

import georegression.metric.alg.DistancePointTriangle3D_F32;
import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.line.LineSegment3D_F32;
import georegression.struct.plane.PlaneGeneral3D_F32;
import georegression.struct.plane.PlaneNormal3D_F32;
import georegression.struct.point.Point3D_F32;


/**
 * Functions related to finding the closest point(s) on one shape from another shape.
 *
 * @author Peter Abeles
 */
public class ClosestPoint3D_F32 {
	/**
	 * Returns the point which minimizes the distance between the two lines in 3D.  If the
	 * two lines are parallel the result is undefined.
	 *
	 * @param l0  first line. Not modified.
	 * @param l1  second line. Not modified.
	 * @param ret (Optional) Storage for the closest point. If null a new point is declared. Modified.
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
	 * <p>
	 * Finds the closest point on line lo to l1 and on l1 to l0.  The solution is returned in
	 * 'param' as a value of 't' for each line.
	 * </p>
	 * <p>
	 * point on l0 = l0.a + param[0]*l0.slope<br>
	 * point on l1 = l1.a + param[1]*l1.slope
	 * </p>
	 * @param l0  first line. Not modified.
	 * @param l1  second line. Not modified.
	 * @param param  param[0] for line0 location and param[1] for line1 location.
	 *
	 * @return False if the lines are parallel or true if a solution was found.
	 */
	public static boolean closestPoints(LineParametric3D_F32 l0,
										LineParametric3D_F32 l1,
										float param[])
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
	 * @param ret  Storage for the solution.  Can be same as instance as 'pt'. If null is passed in a new point is created. Modified.
	 */
	public static Point3D_F32 closestPoint(LineParametric3D_F32 line,
										   Point3D_F32 pt,
										   Point3D_F32 ret) {
		if( ret == null ) {
			ret = new Point3D_F32();
		}

		float dx = pt.x - line.p.x;
		float dy = pt.y - line.p.y;
		float dz = pt.z - line.p.z;

		float n = line.slope.norm();

		float d = (line.slope.x*dx + line.slope.y*dy + line.slope.z*dz) / n;

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
	public static Point3D_F32 closestPoint( PlaneNormal3D_F32 plane , Point3D_F32 point , Point3D_F32 found ) {
		if( found == null )
			found = new Point3D_F32();

		float A = plane.n.x;
		float B = plane.n.y;
		float C = plane.n.z;
		float D = plane.n.x*plane.p.x + plane.n.y*plane.p.y + plane.n.z*plane.p.z;

		float top = A*point.x + B*point.y + C*point.z - D;

		float n2 = A*A + B*B + C*C;

		found.x = point.x - A*top/n2;
		found.y = point.y - B*top/n2;
		found.z = point.z - C*top/n2;

		return found;
	}

	/**
	 * Finds the closest point on the plane to the specified point.
	 *
	 * @param plane The plane
	 * @param point The point
	 * @param found (Optional) Storage for the closest point.  Can be same as instance as 'pt'. If null a new point is declared internally.
	 * @return The closest point
	 */
	public static Point3D_F32 closestPoint( PlaneGeneral3D_F32 plane , Point3D_F32 point , Point3D_F32 found ) {
		if( found == null )
			found = new Point3D_F32();

		float top = plane.A*point.x + plane.B*point.y + plane.C*point.z - plane.D;

		float n2 = plane.A*plane.A + plane.B*plane.B + plane.C*plane.C;

		found.x = point.x - plane.A*top/n2;
		found.y = point.y - plane.B*top/n2;
		found.z = point.z - plane.C*top/n2;

		return found;
	}

	/**
	 * Finds the closest point on a line segment to the specified point.
	 *
	 * @param line Line on which the closest point is being found.  Not modified.
	 * @param pt   The point whose closest point is being looked for.  Not modified.
	 * @param ret  (Optional) Storage for the solution.  Can be same as instance as 'pt'. If null is passed in a new point is created. Modified.
	 */
	public static Point3D_F32 closestPoint(LineSegment3D_F32 line,
										   Point3D_F32 pt,
										   Point3D_F32 ret) {
		if( ret == null ) {
			ret = new Point3D_F32();
		}

		float dx = pt.x - line.a.x;
		float dy = pt.y - line.a.y;
		float dz = pt.z - line.a.z;

		float slope_x = line.b.x - line.a.x;
		float slope_y = line.b.y - line.a.y;
		float slope_z = line.b.z - line.a.z;

		float n = (float) (float)Math.sqrt(slope_x*slope_x + slope_y*slope_y + slope_z*slope_z);

		float d = (slope_x*dx + slope_y*dy + slope_z*dz) / n;

		// if it is past the end points just return one of the end points
		if( d <= 0 ) {
			ret.set(line.a);
		} else if( d >= n ) {
			ret.set(line.b);
		} else {
			ret.x = line.a.x + d * slope_x / n;
			ret.y = line.a.y + d * slope_y / n;
			ret.z = line.a.z + d * slope_z / n;
		}

		return ret;
	}

	/**
	 * Find the point which minimizes its distance from the two line segments.
	 *
	 * @param l0 First line.  Not modified.
	 * @param l1 Second line.  Not modified.
	 * @param ret (Optional) Storage for the solution.  Can be same as instance as 'pt'. If null is passed in a new point is created. Modified.
	 */
	public static Point3D_F32 closestPoint( LineSegment3D_F32 l0 , LineSegment3D_F32 l1 , Point3D_F32 ret ) {
		if( ret == null ) {
			ret = new Point3D_F32();
		}

		ret.x = l0.a.x - l1.a.x;
		ret.y = l0.a.y - l1.a.y;
		ret.z = l0.a.z - l1.a.z;

		float slope0_x = l0.b.x - l0.a.x;
		float slope0_y = l0.b.y - l0.a.y;
		float slope0_z = l0.b.z - l0.a.z;

		float slope1_x = l1.b.x - l1.a.x;
		float slope1_y = l1.b.y - l1.a.y;
		float slope1_z = l1.b.z - l1.a.z;

		// normalize the slopes for easier math
		float n0 = (float) (float)Math.sqrt(slope0_x*slope0_x + slope0_y*slope0_y + slope0_z*slope0_z);
		float n1 = (float) (float)Math.sqrt(slope1_x*slope1_x + slope1_y*slope1_y + slope1_z*slope1_z);

		slope0_x /= n0; slope0_y /= n0; slope0_z /= n0;
		slope1_x /= n1; slope1_y /= n1; slope1_z /= n1;

		// this solution is from: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline3d/
		float dv01v1 = ret.x*slope1_x +  ret.y*slope1_y +  ret.z*slope1_z;
		float dv01v0 = ret.x*slope0_x +  ret.y*slope0_y +  ret.z*slope0_z;
		float dv1v0 = slope1_x*slope0_x + slope1_y*slope0_y + slope1_z*slope0_z;

		float t0 = dv01v1 * dv1v0 - dv01v0;
		float bottom = 1 - dv1v0 * dv1v0;
		if( bottom == 0 )
			return null;

		t0 /= bottom;

		// restrict it to be on the line
		if( t0 < 0 )
			return closestPoint(l1,l0.a,ret);
		if( t0 > 1 )
			return closestPoint(l1,l0.b,ret);

		// ( d1343 + mua d4321 ) / d4343
		float t1 = ( dv01v1 + t0 * dv1v0 );

		if( t1 < 0 )
			return closestPoint(l0,l1.a,ret);
		if( t1 > 1 )
			return closestPoint(l0,l1.b,ret);

		ret.x = (float) 0.5f * ( ( l0.a.x + t0 * slope0_x ) + ( l1.a.x + t1 * slope1_x ) );
		ret.y = (float) 0.5f * ( ( l0.a.y + t0 * slope0_y ) + ( l1.a.y + t1 * slope1_y ) );
		ret.z = (float) 0.5f * ( ( l0.a.z + t0 * slope0_z ) + ( l1.a.z + t1 * slope1_z ) );

		return ret;
	}

	/**
	 * Closest point from a 3D triangle to a point.
	 *
	 * @see DistancePointTriangle3D_F32
	 *
	 * @param vertexA Vertex in a 3D triangle.
	 * @param vertexB Vertex in a 3D triangle.
	 * @param vertexC Vertex in a 3D triangle.
	 * @param point Point for which the closest point on the triangle is found
	 * @param ret (Optional) Storage for the solution.  If null is passed in a new point is created. Modified.
	 * @return The closest point
	 */
	public static Point3D_F32 closestPoint( Point3D_F32 vertexA, Point3D_F32 vertexB, Point3D_F32 vertexC,
											Point3D_F32 point , Point3D_F32 ret) {

		if( ret == null ) {
			ret = new Point3D_F32();
		}

		DistancePointTriangle3D_F32 alg = new DistancePointTriangle3D_F32();
		alg.setTriangle(vertexA,vertexB,vertexC);

		alg.closestPoint(point,ret);

		return ret;
	}
}
