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
import georegression.struct.point.Point3D_F32;
import georegression.struct.shapes.Cylinder3D_F32;
import georegression.struct.shapes.Sphere3D_F32;


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

		// round off error can make distanceSq go negative when it is very close to zero
		float distanceSq = dx * dx + dy * dy + dz * dz;
		if( distanceSq < 0 )
			return 0;
		else
			return (float)Math.sqrt( distanceSq );
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

		float cc = x*x + y*y + z*z;

		// could avoid a square root here by computing b*b directly
		// however that is most likely more prone to numerical overflow since the numerator will need to be squared
		// before division can reduce its "power"
		float b = MiscOps.dot(x,y,z,l.slope)/l.slope.norm();

		float distanceSq = cc-b*b;

		// round off error can make distanceSq go negative when it is very close to zero
		if( distanceSq < 0 ) {
			return 0;
		} else {
			return (float)Math.sqrt(distanceSq);
		}
	}

	/**
	 * Distance from the point to the closest point on the line segment.
	 *
	 * @param l Line. Not modified.
	 * @param p Point. Not modified.
	 * @return distance.
	 */
	public static float distance( LineSegment3D_F32 l,
								   Point3D_F32 p ) {

		float dx = p.x - l.a.x;
		float dy = p.y - l.a.y;
		float dz = p.z - l.a.z;

		float cc = dx*dx + dy*dy + dz*dz;

		float slope_x = l.b.x - l.a.x;
		float slope_y = l.b.y - l.a.y;
		float slope_z = l.b.z - l.a.z;

		float n = (float) (float)Math.sqrt(slope_x*slope_x + slope_y*slope_y + slope_z*slope_z);

		float d = (slope_x*dx + slope_y*dy + slope_z*dz) / n;

		// check end points
		if( d <= 0 )
			return p.distance(l.a);
		else if( d >= n )
			return p.distance(l.b);

		float distanceSq = cc-d*d;

		// round off error can make distanceSq go negative when it is very close to zero
		if( distanceSq < 0 ) {
			return 0;
		} else {
			return (float)Math.sqrt(distanceSq);
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
	public static float distance( PlaneGeneral3D_F32 plane , Point3D_F32 point ) {
		float top = plane.A*point.x + plane.B*point.y + plane.C*point.z - plane.D;

		return top / (float)Math.sqrt( plane.A*plane.A + plane.B*plane.B + plane.C*plane.C);
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
	public static float distance( Sphere3D_F32 sphere , Point3D_F32 point ) {

		float r = point.distance(sphere.center);
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
	public static float distance( Cylinder3D_F32 cylinder, Point3D_F32 point ) {

		float r = Distance3D_F32.distance(cylinder.line,point);

		return r - cylinder.radius;
	}

	/**
	 * Signed distance from a 3D point to 3D triangle.   The sign indicates which side of the triangle the point
	 * is on.  See {@link georegression.metric.alg.DistancePointTriangle3D_F32} for the details.
	 *
	 * @param vertexA Vertex in a 3D triangle.
	 * @param vertexB Vertex in a 3D triangle.
	 * @param vertexC Vertex in a 3D triangle.
	 * @param point Point for which the closest point on the triangle is found
	 * @return The closest point
	 */
	public static float distance( Point3D_F32 vertexA, Point3D_F32 vertexB, Point3D_F32 vertexC, Point3D_F32 point ) {

		DistancePointTriangle3D_F32 alg = new DistancePointTriangle3D_F32();
		alg.setTriangle(vertexA,vertexB,vertexC);

		Point3D_F32 cp = new Point3D_F32();

		alg.closestPoint(point,cp);

		float d = point.distance(cp);

		return alg.sign(point)*d;
	}
}
