/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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


import georegression.geometry.GeometryMath_F32;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.line.LineSegment3D_F32;
import georegression.struct.plane.PlaneGeneral3D_F32;
import georegression.struct.plane.PlaneNormal3D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.shapes.Box3D_F32;
import georegression.struct.shapes.BoxLength3D_F32;
import georegression.struct.shapes.Triangle3D_F32;

/**
 * @author Peter Abeles
 */
public class Intersection3D_F32 {

	/**
	 * Finds the intersection of a line and a plane.  Returns true if they intersect at a unique point or false if
	 * there is no intersection or an infinite number of intersections.
	 *
	 * @param plane Plane
	 * @param line Line
	 * @return True if the intersection is at a unique point.  If false then no intersection or infinite.
	 */
	public static boolean intersect( PlaneNormal3D_F32 plane , LineParametric3D_F32 line , Point3D_F32 intersection ) {
		float dx = plane.p.x - line.p.x;
		float dy = plane.p.y - line.p.y;
		float dz = plane.p.z - line.p.z;

		float top = dx*plane.n.x + dy*plane.n.y + dz*plane.n.z;
		float bottom = line.slope.dot(plane.n);

		if( bottom == 0 )
			return false;

		float d = top/bottom;

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
	public static boolean intersect( PlaneGeneral3D_F32 plane , LineParametric3D_F32 line , Point3D_F32 intersection ) {

		float top = plane.D - plane.A*line.p.x - plane.B*line.p.y - plane.C*line.p.z;
		float bottom = plane.A*line.slope.x + plane.B*line.slope.y + plane.C*line.slope.z;

		if( bottom == 0 )
			return false;

		float d = top/bottom;

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
	public static boolean intersect( PlaneGeneral3D_F32 a , PlaneGeneral3D_F32 b , LineParametric3D_F32 line ) {

		// Line's slope is the cross product of the two normal vectors
		GeometryMath_F32.cross(a.A,a.B,a.C,b.A,b.B,b.C,line.slope);

		if( line.slope.normSq() == 0 )
			return false;

		// Closest point on plane 'a' to origin (0,0,0)
		float n2 = a.A*a.A + a.B*a.B + a.C*a.C;

		float closestX = a.A*a.D/n2;
		float closestY = a.B*a.D/n2;
		float closestZ = a.C*a.D/n2;

		// Cross product between normal of 'a' and the line's slope.  This points towards the intersection
		float slopeX = a.B * line.slope.z - a.C * line.slope.y;
		float slopeY = a.C * line.slope.x - a.A * line.slope.z;
		float slopeZ = a.A * line.slope.y - a.B * line.slope.x;

		// Now find the intersection of the plane and a line containing point 'closest' and pointing towards the
		// the intersection.
		float top = b.D - b.A*closestX - b.B*closestY - b.C*closestZ;
		float bottom = b.A*slopeX + b.B*slopeY + b.C*slopeZ;

		float d = top/bottom;

		line.p.x = closestX + d*slopeX;
		line.p.y = closestY + d*slopeY;
		line.p.z = closestZ + d*slopeZ;

		return true;
	}

	/**
	 * <p>Finds the intersection between a 3D triangle and a line-segment.  Code ported from [1].</p>
	 *
	 * <p>
	 * [1] http://geomalgorithms.com/a06-_intersect-2.html
	 * </p>
	 *
	 * @param T Triangle in 3D space
	 * @param R Line segment in 3D space.
	 * @param output Storage for the intersection, if there is one
	 * @return -1 = triangle is degenerate (a segment or point)
	 *          0 =  disjoint (no intersect)
	 *          1 =  intersect in unique point I1
	 *          2 =  are in the same plane
	 **/
	public static int intersection( Triangle3D_F32 T , LineSegment3D_F32 R , Point3D_F32 output ) {
		return intersection(T,R,output,
				new Vector3D_F32(),new Vector3D_F32(),new Vector3D_F32(),new Vector3D_F32(),new Vector3D_F32());
	}

	/**
	 * <p>Finds the intersection between a 3D triangle and a line-segment.  Code ported from [1].  Internal
	 * working variables are provided in this interface to reduce memory creation/destruction.</p>
	 *
	 * <p>
	 * [1] http://geomalgorithms.com/a06-_intersect-2.html
	 * </p>
	 *
	 * @param T Triangle in 3D space
	 * @param R Line segment in 3D space.
	 * @param output Storage for the intersection, if there is one
	 * @param u   (internal use) triangle vectors
	 * @param v   (internal use) triangle vectors
	 * @param n   (internal use) triangle vectors
	 * @param dir (internal use) ray vector
	 * @param w0  (internal use) ray vector
	 * @return -1 = triangle is degenerate (a segment or point)
	 *          0 =  disjoint (no intersect)
	 *          1 =  intersect in unique point I1
	 *          2 =  are in the same plane
	 **/
	public static int intersection( Triangle3D_F32 T , LineSegment3D_F32 R , Point3D_F32 output ,
									Vector3D_F32 u , Vector3D_F32 v , Vector3D_F32 n,
									Vector3D_F32 dir , Vector3D_F32 w0 ) {
		float r, a, b;              // params to calc ray-plane intersect

		// get triangle edge vectors and plane normal
		u.minus(T.v1,T.v0);   // NOTE: these could be precomputed
		v.minus(T.v2,T.v0);
		n.cross(u,v);

		if ( n.normSq() == 0 )        // triangle is degenerate
			return -1;                  // do not deal with this case

		dir.minus(R.b,R.a);             // ray direction vector
		w0.minus(R.a,T.v0);
		a = -n.dot(w0);
		b = n.dot(dir);
		if (Math.abs(b) < GrlConstants.F_EPS) { // ray is  parallel to triangle plane
			if (a == 0)                       // ray lies in triangle plane
				return 2;
			else return 0;                    // ray disjoint from plane
		}

		// get intersect point of ray with triangle plane
		r = a / b;
		if (r < 0.0f)                    // ray goes away from triangle
			return 0;                   // => no intersect
		else if( r > 1.0f )              // is past the end of the line segment
			return 0;

		// intersect point of ray and plane
		output.x = R.a.x + r*dir.x;
		output.y = R.a.y + r*dir.y;
		output.z = R.a.z + r*dir.z;

		// is I inside T?
		float uu, uv, vv, wu, wv, D;
		uu = u.dot(u);
		uv = u.dot(v);
		vv = v.dot(v);
		w0.minus(output,T.v0);
		wu = w0.dot(u);
		wv = w0.dot(v);
		D = uv * uv - uu * vv;

		// get and test parametric coords
		float s, t;
		s = (uv * wv - vv * wu) / D;
		if (s < 0.0f || s > 1.0f)        // I is outside T
			return 0;
		t = (uv * wu - uu * wv) / D;
		if (t < 0.0f || (s + t) > 1.0f)  // I is outside T
			return 0;

		return 1;                      // I is in T
	}

	/**
	 * Returns true if the point is contained inside the box. The point is considered to be inside the box
	 * if the following test passes for each dimension.  box.x <= point.x < box.x + box.lengthX
	 *
	 * @param box Box
	 * @param point Point which is tested to see if it is inside the box
	 * @return true for inside and false for not
	 */
	public static boolean contained( BoxLength3D_F32 box , Point3D_F32 point ) {

		return( box.p.x <= point.x && point.x < box.p.x + box.lengthX &&
				box.p.y <= point.y && point.y < box.p.y + box.lengthY &&
				box.p.z <= point.z && point.z < box.p.z + box.lengthZ );
	}

	/**
	 * <p>
	 * Returns true if the point is contained inside the box, with an exclusive upper extent.
	 * The point is considered to be inside the box if the following test passes:<br>
	 * box.p0.x <= point.x < box.p1.x<br>
	 * box.p0.y <= point.y < box.p1.y<br>
	 * box.p0.z <= point.z < box.p1.z<br>
	 * </p>
	 *
	 * @param box Box
	 * @param point Point which is tested to see if it is inside the box
	 * @return true for inside and false for not
	 */
	public static boolean contained( Box3D_F32 box , Point3D_F32 point ) {

		return( box.p0.x <= point.x && point.x < box.p1.x &&
				box.p0.y <= point.y && point.y < box.p1.y &&
				box.p0.z <= point.z && point.z < box.p1.z );
	}

	/**
	 * <p>
	 * Returns true if the point is contained inside the box, with an inclusive upper extent.
	 * The point is considered to be inside the box if the following test passes:<br>
	 * box.p0.x <= point.x <= box.p1.x<br>
	 * box.p0.y <= point.y <= box.p1.y<br>
	 * box.p0.z <= point.z <= box.p1.z<br>
	 * </p>
	 *
	 * @param box Box
	 * @param point Point which is tested to see if it is inside the box
	 * @return true for inside and false for not
	 */
	public static boolean contained2( Box3D_F32 box , Point3D_F32 point ) {
		return( box.p0.x <= point.x && point.x <= box.p1.x &&
				box.p0.y <= point.y && point.y <= box.p1.y &&
				box.p0.z <= point.z && point.z <= box.p1.z );
	}

	/**
	 * Returns true if boxB is contained inside of or is identical to boxA.
	 *
	 * @param boxA Box
	 * @param boxB Box which is being tested to see if it is inside of boxA
	 * @return true if inside/identical or false if outside
	 */
	public static boolean contained( Box3D_F32 boxA , Box3D_F32 boxB ) {
		return( boxA.p0.x <= boxB.p0.x && boxA.p1.x >= boxB.p1.x &&
				boxA.p0.y <= boxB.p0.y && boxA.p1.y >= boxB.p1.y &&
				boxA.p0.z <= boxB.p0.z && boxA.p1.z >= boxB.p1.z );
	}

	/**
	 * Returns true if the two boxs intersect each other. p0 is inclusive and p1 is exclusive.
	 * So if the p0 edge and p1 edge overlap perfectly there is no intersection.
	 *
	 * @param boxA Box
	 * @param boxB Box
	 * @return true for intersection and false if no intersection
	 */
	public static boolean intersect( Box3D_F32 boxA , Box3D_F32 boxB ) {
		return( intersect(boxA.p0.x , boxB.p0.x , boxA.p1.x , boxB.p1.x ) &&
				intersect(boxA.p0.y , boxB.p0.y , boxA.p1.y , boxB.p1.y ) &&
				intersect(boxA.p0.z , boxB.p0.z , boxA.p1.z , boxB.p1.z ) );
	}

	protected static boolean intersect( float a0 , float b0 , float a1, float b1 ) {
		if( a0 <= b0 ) {
			return b0 < a1;
		} else {
			return a0 < b1;
		}
	}

}
