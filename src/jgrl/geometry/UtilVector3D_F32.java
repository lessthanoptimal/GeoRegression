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

package jgrl.geometry;

import jgrl.struct.point.Vector3D_F32;
import org.ejml.data.DenseMatrix64F;

import java.util.Random;


/**
 * @author Peter Abeles
 */

// TODO create a UtilGeoTuple3D class for this and point 3d?
public class UtilVector3D_F32 {

	/**
	 * Creates a random vector where each axis is selected from a uniform distribution.
	 *
	 * @param min  minimum value
	 * @param max  maximum value
	 * @param rand random number generator
	 * @return the new random vector
	 */
	public static Vector3D_F32 createRandom( float min, float max, Random rand ) {
		float range = max - min;

		Vector3D_F32 a = new Vector3D_F32();

		a.x = range * rand.nextFloat() + min;
		a.y = range * rand.nextFloat() + min;
		a.z = range * rand.nextFloat() + min;

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
	public static boolean isIdentical( Vector3D_F32 a, Vector3D_F32 b, float tol ) {
		if( (float)Math.abs( a.x - b.x ) > tol )
			return false;

		if( (float)Math.abs( a.y - b.y ) > tol )
			return false;

		return (float)Math.abs( a.z - b.z ) <= tol;

	}

	/**
	 * Rescales the vector such that its normal is equal to one.
	 *
	 * @param v Vector being normalized.
	 */
	public static void normalize( Vector3D_F32 v ) {
		float a = v.norm();

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
	public static DenseMatrix64F createMatrix( DenseMatrix64F R, Vector3D_F32... v ) {
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
	public static Vector3D_F32 convert( DenseMatrix64F m ) {

		Vector3D_F32 v = new Vector3D_F32();
		v.x = (float) m.data[0];
		v.y = (float) m.data[1];
		v.z = (float) m.data[2];

		return v;
	}
}
