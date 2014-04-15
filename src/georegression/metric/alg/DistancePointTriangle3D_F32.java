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

package georegression.metric.alg;

import georegression.geometry.GeometryMath_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;

/**
 * <p>Computes the closest point and distance between a triangle in 3D and the closest point on a triangle.</p>
 *
 * <p>David Eberly, "Distance Between Point and Triangle in 3D", Geometric Tools, LLC, 1999</p>
 *
 * @author Peter Abeles
 */
public class DistancePointTriangle3D_F32 {

	// description of triangle
	private Point3D_F32 B = new Point3D_F32();
	private Vector3D_F32 E0 = new Vector3D_F32();
	private Vector3D_F32 E1 = new Vector3D_F32();

	// storage for cross product of E0 and E1
	private Vector3D_F32 N = new Vector3D_F32();

	// coefficients for triangle.  0 <= s,t <= 1 and s+t <= 1
	// any point can be uniquely described using s and t
	// T(s,t) = B + s*E0 + t*S1
	private float s, t;

	// storage for B-P
	private Vector3D_F32 D = new Vector3D_F32();

	private float a,b,c,d,e;

	public void setTriangle(Point3D_F32 p0, Point3D_F32 p1, Point3D_F32 p2) {

		// convert triangle into new format
		B.set(p0);
		GeometryMath_F32.sub(p1, p0, E0);
		GeometryMath_F32.sub(p2, p0, E1);
	}

	/**
	 * Find the closest point on the triangle to P.
	 * @param P Input.  The point for which the closest point is to be found.
	 * @param closestPt Output.  The found closest point.
	 */
	public void closestPoint(Point3D_F32 P , Point3D_F32 closestPt ) {

		// D = B-P
		GeometryMath_F32.sub(B, P, D);

		a = E0.dot(E0);
		b = E0.dot(E1);
		c = E1.dot(E1);
		d = E0.dot(D);
		e = E1.dot(D);

		float det = a * c - b * b;
		s = b * e - c * d;
		t = b * d - a * e;

		if (s + t <= det) {
			if (s < 0) {
				if (t < 0) {
					region4();
				} else {
					region3();
				}
			} else if (t < 0) {
				region5();
			} else {
				region0(det);
			}
		} else {
			if (s < 0) {
				region2();
			} else if (t < 0) {
				region6();
			} else {
				region1();
			}
		}

		closestPt.x = B.x + s*E0.x  + t*E1.x;
		closestPt.y = B.y + s*E0.y  + t*E1.y;
		closestPt.z = B.z + s*E0.z  + t*E1.z;
	}

	/**
	 * Returns the signed of the vector.  If its "in front" it will be positive and negative if "behind".  In front
	 * is defined as being on the same side as the cross product of p2-p0 and p1-p0.
	 */
	public float sign( Point3D_F32 P ) {

		GeometryMath_F32.cross(E1,E0,N);

		// dot product of
		float d = N.x*(P.x-B.x) + N.y*(P.y-B.y) + N.z*(P.z-B.z);
		return (float)Math.signum(d);
	}

	protected void region0(float det) {
		float invDet = 1.0f / det;
		s *= invDet;
		t *= invDet;
	}

	protected void region1() {
		float numer = c + e - b - d;
		if (numer <= 0) {
			s = 0;
		} else {
			float denom = a - 2 * b + c;
			s = (numer >= denom) ? 1 : numer / denom;
		}
		t = 1 - s;
	}

	protected void region2() {
		float tmp0 = b + d;
		float tmp1 = c + e;
		if( tmp1 > tmp0 ) {
			float numer = tmp1 - tmp0;
			float denom = a - 2 * b + c;
			s = numer <= denom ? 1 : numer/denom;
			t = 1 - s;
		} else {
			s = 0;
			t = tmp1 <= 0 ? 1 : (e >= 0 ? 0 : -e/c);
		}
	}

	protected void region3() {
		s = 0;
		t = e >= 0 ? 0 : (-e >= c ? 1 : -e/c);
	}

	protected void region4() {
		if (d < 0) {
			t = 0;
			s = -d >= a ? 1 : -d/a;
		}else{
			s = 0;
			t = e >= 0 ? 0 : (-e >= c ? 1 : -e/c);
		}
	}

	protected void region5() {
		t = 0;
		s = d >= 0 ? 0 : (-d >= a ? 1 : -d/a );
	}

	protected void region6() {
		float tmp0 = b + e;
		float tmp1 = a + d;
		if (tmp1 > tmp0) {
			float numer = tmp1 - tmp0;
			float denom = a - 2 * b + c;
			t = numer >= denom ? 1 : numer/denom;
			s = 1 - t;
		}else{
			t = 0;
			s = tmp1 <= 0 ? 1 : (d >= 0 ? 0 : -d/a);
		}
	}
}