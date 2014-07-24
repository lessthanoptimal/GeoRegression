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

import georegression.struct.point.Vector3D_F64;
import org.ejml.data.DenseMatrix64F;

import java.util.Random;


/**
 * @author Peter Abeles
 */

// TODO create a UtilGeoTuple3D class for this and point 3d?
public class UtilVector3D_F64 {

	/**
	 * Creates a random vector where each axis is selected from a uniform distribution.
	 *
	 * @param min  minimum value
	 * @param max  maximum value
	 * @param rand random number generator
	 * @return the new random vector
	 */
	public static Vector3D_F64 createRandom( double min, double max, Random rand ) {
		double range = max - min;

		Vector3D_F64 a = new Vector3D_F64();

		a.x = range * rand.nextDouble() + min;
		a.y = range * rand.nextDouble() + min;
		a.z = range * rand.nextDouble() + min;

		return a;
	}

	/**
	 * Checks to see if the two vectors are identical to within tolerance. Each axis is checked
	 * individually.
	 *
	 * @param a   First vector.
	 * @param b   Second vector.
	 * @param tol Tolerance for equality.
	 * @return true if identical and false if not.
	 */
	public static boolean isIdentical( Vector3D_F64 a, Vector3D_F64 b, double tol ) {
		if( Math.abs( a.x - b.x ) > tol )
			return false;

		if( Math.abs( a.y - b.y ) > tol )
			return false;

		return Math.abs( a.z - b.z ) <= tol;

	}

	/**
	 * Rescales the vector such that its normal is equal to one.
	 *
	 * @param v Vector being normalized.
	 */
	public static void normalize( Vector3D_F64 v ) {
		double a = v.norm();

		v.x /= a;
		v.y /= a;
		v.z /= a;
	}

	/**
	 * Creates a matrix from the set of column vectors.  Each vector is a column in the new matrix.
	 *
	 * @param v Set of vectors. Not modified.
	 * @param R If not null the vectors are stored here.
	 * @return Matrix.
	 */
	public static DenseMatrix64F createMatrix( DenseMatrix64F R, Vector3D_F64... v ) {
		if( R == null ) {
			R = new DenseMatrix64F( 3, v.length );
		}

		for( int i = 0; i < v.length; i++ ) {
			R.set( 0, i, v[i].x );
			R.set( 1, i, v[i].y );
			R.set( 2, i, v[i].z );
		}

		return R;
	}

	/**
	 * Converts matrices into vectors.  All matrices must be vectors with 3 elements.
	 */
	public static Vector3D_F64 convert( DenseMatrix64F m ) {

		Vector3D_F64 v = new Vector3D_F64();
		v.x = (double) m.data[0];
		v.y = (double) m.data[1];
		v.z = (double) m.data[2];

		return v;
	}
}
