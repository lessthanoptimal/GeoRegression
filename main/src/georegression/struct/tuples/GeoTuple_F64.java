/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

package georegression.struct.tuples;

import georegression.struct.arraylike.ArrayLike_F64;

/**
 * Describes geometric objects that are composed of N double values. Where N is the dimension
 * of space the object is contained in. Points and vectors are two examples of a GeoTuple. Each
 * value is the value of the object along a dimension in the space it occupies.
 *
 * @author Peter Abeles
 */
public abstract class GeoTuple_F64<T extends GeoTuple_F64> extends GeoTuple<T> implements ArrayLike_F64 {

	/// Same as [#isEquals(ArrayLike_F64 , double)]
	public boolean isIdentical( T t, double tol ) {
		return isEquals(t, tol);
	}

	/**
	 * Generic copy routine. It is recommended that this be overridden with a faster implementation.
	 *
	 * @return An exact copy of this GeoTuple.
	 */
	@Override
	public T copy() {
		return (T)createNewInstance().setTo(this);
	}

	/**
	 * Computes the  Euclidean norm.
	 *
	 * @return norm.
	 */
	public double norm() {
		return (float)Math.sqrt(normSq());
	}

	/**
	 * Computes the square of the Euclidean norm.
	 *
	 * @return norm squared.
	 */
	public double normSq() {
		double total = 0;
		int N = getDimension();
		for (int i = 0; i < N; i++) {
			double a = get(i);
			total += a*a;
		}

		return total;
	}

	public double distance( T t ) {
		return Math.sqrt(distance2(t));
	}

	public double distance2( T t ) {
		if (t.getDimension() != getDimension())
			throw new IllegalArgumentException("Dimension of input tuple does not match");

		double total = 0;
		final int N = getDimension();
		for (int i = 0; i < N; i++) {
			double diff = Math.abs(get(i) - t.get(i));

			total += diff*diff;
		}

		return total;
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj)
			return true;

		if (!(obj instanceof GeoTuple_F64))
			return false;

		var o = (GeoTuple_F64)obj;
		final int N = getDimension();

		if (N != o.getDimension())
			return false;
		for (int i = 0; i < N; i++) {
			if (get(i) != o.get(i))
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int N = getDimension();
		int hash = 0;
		for (int i = 0; i < N; i++) {
			hash += Double.hashCode(get(i));
		}
		return hash;
	}
}
