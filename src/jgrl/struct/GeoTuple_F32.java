/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct;

/**
 * Describes geometric objects that are composed of N float values.  Where N is the dimension
 * of space the object is contained in.  Points and vectors are two examples of a GeoTuple.  Each
 * value is the value of the object along a dimension in the space it occupies.
 *
 * @author Peter Abeles
 */
public abstract class GeoTuple_F32<T extends GeoTuple_F32> extends GeoTuple<T> {

	/**
	 * Checks to see if the two GeoTuple have values which are nearly the same.  False is always
	 * returned if the dimension is different.
	 *
	 * @param t   The GeoTuple it is being compared against.
	 * @param tol How similar each element must be for them to be considered identical.
	 * @return if they are identical or not.
	 */
	public boolean isIdentical(T t, float tol) {
		if (t.getDimension() != getDimension())
			return false;

		int N = getDimension();
		for (int i = 0; i < N; i++) {
			float diff = (float) Math.abs(getIndex(i) - t.getIndex(i));

			if (diff > tol)
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
		for (int i = 0; i < N; i++) {
			ret.setIndex(i, getIndex(i));
		}

		return ret;
	}

	/**
	 * Computes the  Euclidean norm.
	 *
	 * @return norm.
	 */
	public float norm() {
		return (float) (float) Math.sqrt(normSq());
	}

	/**
	 * Computes the square of the Euclidean norm.
	 *
	 * @return norm squared.
	 */
	public float normSq() {
		float total = 0;
		int N = getDimension();
		for (int i = 0; i < N; i++) {
			float a = getIndex(i);
			total += a * a;
		}

		return total;
	}

	public float distance(T t) {
		return (float) Math.sqrt(distance2(t));
	}

	public float distance2(T t) {
		if (t.getDimension() != getDimension())
			throw new IllegalArgumentException("Dimension of input tuple does not match");

		float total = 0;
		final int N = getDimension();
		for (int i = 0; i < N; i++) {
			float diff = (float) Math.abs(getIndex(i) - t.getIndex(i));

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
	public abstract float getIndex(int index);

	public abstract void setIndex(int index, float value);
}
