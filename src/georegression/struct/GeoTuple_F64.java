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

package georegression.struct;

/**
 * Describes geometric objects that are composed of N double values.  Where N is the dimension
 * of space the object is contained in.  Points and vectors are two examples of a GeoTuple.  Each
 * value is the value of the object along a dimension in the space it occupies.
 *
 * @author Peter Abeles
 */
public abstract class GeoTuple_F64<T extends GeoTuple_F64> extends GeoTuple<T> {

	/**
	 * Checks to see if the two GeoTuple have values which are nearly the same.  False is always
	 * returned if the dimension is different.
	 *
	 * @param t   The GeoTuple it is being compared against.
	 * @param tol How similar each element must be for them to be considered identical.
	 * @return if they are identical or not.
	 */
	public boolean isIdentical( T t, double tol ) {
		if( t.getDimension() != getDimension() )
			return false;

		int N = getDimension();
		for( int i = 0; i < N; i++ ) {
			double diff = Math.abs( getIndex( i ) - t.getIndex( i ) );

			if( diff > tol )
				return false;
		}

		return true;
	}

	/**
	 * Generic copy routine.  It is recommended that this be overriden with a faster implementation.
	 *
	 * @return An exact copy of this GeoTuple.
	 */
	public T copy() {
		T ret = createNewInstance();

		int N = getDimension();
		for( int i = 0; i < N; i++ ) {
			ret.setIndex( i, getIndex( i ) );
		}

		return ret;
	}

	/**
	 * Computes the  Euclidean norm.
	 *
	 * @return norm.
	 */
	public double norm() {
		return (float) Math.sqrt( normSq() );
	}

	/**
	 * Computes the square of the Euclidean norm.
	 *
	 * @return norm squared.
	 */
	public double normSq() {
		double total = 0;
		int N = getDimension();
		for( int i = 0; i < N; i++ ) {
			double a = getIndex( i );
			total += a * a;
		}

		return total;
	}

	public double distance( T t ) {
		return Math.sqrt( distance2( t ) );
	}

	public double distance2( T t ) {
		if( t.getDimension() != getDimension() )
			throw new IllegalArgumentException( "Dimension of input tuple does not match" );

		double total = 0;
		final int N = getDimension();
		for( int i = 0; i < N; i++ ) {
			double diff = Math.abs( getIndex( i ) - t.getIndex( i ) );

			total += diff * diff;
		}

		return total;
	}

	/**
	 * Returns the value of the tuple along the specified coordinate system axis.
	 *
	 * @param index Which axis in the coordinate system.
	 * @return Its value.
	 */
	public abstract double getIndex( int index );

	public abstract void setIndex( int index, double value );
}
