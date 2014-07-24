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

package georegression.geometry;

import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.plane.PlaneTangent3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;

/**
 * @author Peter Abeles
 */
public class UtilPlane3D_F64 {

	/**
	 * Converts a plane in normal form into a general equation
	 *
	 * @param input Plane in normal form.
	 * @param output (Optional) Storage for output plane in general form. If null a new instance will be declared.
	 * @return The convert plane in general form.
	 */
	public static PlaneGeneral3D_F64 convert( PlaneNormal3D_F64 input , PlaneGeneral3D_F64 output ) {
		if( output == null )
			output = new PlaneGeneral3D_F64();

		Vector3D_F64 n = input.n;
		Point3D_F64 p = input.p;

		output.A = n.x;
		output.B = n.y;
		output.C = n.z;
		output.D = n.x*p.x + n.y*p.y + n.z*p.z;

		return output;
	}

	/**
	 * <p>
	 * Converts a plane in general form into normal form.  The point on the plane in normal form will be the
	 * closest point to the origin.
	 * </p>
	 *
	 * <p>NOTE: The normal is not normalized.</p>
	 *
	 * @param input Plane in general form.
	 * @param output (Optional) Storage for output plane in normal form. If null a new instance will be declared.
	 * @return The plane in normal form.
	 */
	public static PlaneNormal3D_F64 convert( PlaneGeneral3D_F64 input , PlaneNormal3D_F64 output ) {

		if( output == null )
			output = new PlaneNormal3D_F64();

		double top = -input.D;
		double n2 = input.A*input.A + input.B*input.B + input.C*input.C;

		output.p.x = -input.A*top/n2;
		output.p.y = -input.B*top/n2;
		output.p.z = -input.C*top/n2;

		output.n.set(input.A,input.B,input.C);

		return output;
	}

	/**
	 * Converts a plane in tangent form into a plane in normal form
	 *
	 * @param input Plane in tangent form.
	 * @param output (Optional) Storage for output plane in normal form. If null a new instance will be declared.
	 * @return The convert plane in general form.
	 */
	public static PlaneNormal3D_F64 convert( PlaneTangent3D_F64 input , PlaneNormal3D_F64 output ) {
		if( output == null )
			output = new PlaneNormal3D_F64();

		// the value of input is a vector normal to the plane and a point on the plane.
		output.n.x = input.x;
		output.n.y = input.y;
		output.n.z = input.z;

		output.p.set(input);

		return output;
	}

	/**
	 * Converts the plane into Hessian normal form.   This is done by dividing each coefficient by the
	 * Euclidean norm of (A,B,C).
	 *
	 * @param plane The input plane.  Modified.
	 */
	public static void hessianNormalForm( PlaneGeneral3D_F64 plane ) {
		double n = Math.sqrt(plane.A*plane.A + plane.B*plane.B + plane.C*plane.C);
		plane.A /= n;
		plane.B /= n;
		plane.C /= n;
		plane.D /= n;
	}

	/**
	 * Applies the plane's definition to test to see if a point is one the plane
	 *
	 * @param plane Equation of the plane
	 * @param point Equation of the point.
	 * @return zero if the point is one the plane.
	 */
	public static double evaluate( PlaneGeneral3D_F64 plane , Point3D_F64 point ) {
		return plane.A*point.x + plane.B*point.y + plane.C*point.z - plane.D;
	}

	/**
	 * Applies the plane's definition to test to see if a point is one the plane
	 *
	 * @param plane Equation of the plane
	 * @param point Equation of the point.
	 * @return zero if the point is one the plane.
	 */
	public static double evaluate( PlaneNormal3D_F64 plane , Point3D_F64 point ) {
		double dx = point.x - plane.p.x;
		double dy = point.y - plane.p.y;
		double dz = point.z - plane.p.z;

		return plane.n.x*dx + plane.n.y*dy + plane.n.z*dz;
	}

	/**
	 * Returns true if the two plane equations are equal to within tolerance.  Planes are converted into
	 * generalized format and normalized to take in account scale ambiguity.
	 *
	 * @param a plane
	 * @param b plane
	 * @param tol Tolerance for equality
	 * @return true if equals and false if not
	 */
	public static boolean equals( PlaneNormal3D_F64 a , PlaneNormal3D_F64 b , double tol ) {
		PlaneGeneral3D_F64 genA = UtilPlane3D_F64.convert(a,null);
		PlaneGeneral3D_F64 genB = UtilPlane3D_F64.convert(b,null);

		double normA = Math.sqrt(genA.A*genA.A + genA.B*genA.B + genA.C*genA.C + genA.D*genA.D);
		double normB = Math.sqrt(genB.A*genB.A + genB.B*genB.B + genB.C*genB.C + genB.D*genB.D);

		genA.A /= normA; genA.B /= normA; genA.C /= normA; genA.D /= normA;
		genB.A /= normB; genB.B /= normB; genB.C /= normB; genB.D /= normB;

		// handle the sign ambiguity by checking both directions.  This is actualy a bit trickier than it would
		// see at first glance.  might be a better way
		int numMatch0 = 0;

		if( Math.abs(genA.A - genB.A) <= tol )
			numMatch0++;
		if( Math.abs(genA.B - genB.B) <= tol )
			numMatch0++;
		if( Math.abs(genA.C - genB.C) <= tol )
			numMatch0++;
		if( Math.abs(genA.D - genB.D) <= tol )
			numMatch0++;

		if( numMatch0 == 4 )
			return true;

		if( Math.abs(genA.A + genB.A) > tol )
			return false;
		if( Math.abs(genA.B + genB.B) > tol )
			return false;
		if( Math.abs(genA.C + genB.C) > tol )
			return false;
		if( Math.abs(genA.D + genB.D) > tol )
			return false;

		return true;
	}
}
