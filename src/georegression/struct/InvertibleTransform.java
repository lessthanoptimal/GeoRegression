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

package georegression.struct;

/**
 * <p>
 * Any transform which has an unique inverse. T(u) = v and u = T<sup>-1</sup>(v).
 * </p>
 * <p/>
 * <p>
 * Functions are provided to determining the dimensionality of the transform, inverting the transform,
 * and concating two transforms.
 * </p>
 * <p/>
 * <p>
 * Design Note: A function to apply the transform has not been provided to any
 * data structures (e.g. {@link GeoTuple}).  Instead that has been pushed off onto specialized static
 * functions in other classes due to the large number of needed functions.
 * </p>
 *
 * @author Peter Abeles
 */
public interface InvertibleTransform<T extends InvertibleTransform> {

	/**
	 * Returns the dimension of the space which this transform operates on.
	 *
	 * @return space's dimension
	 */
	public int getDimension();

	/**
	 * Creates a new instance of the same SpecialEuclidean as this class.
	 *
	 * @return A new instance.
	 */
	public T createInstance();

	/**
	 * Assigns 'this' to the value of target.
	 *
	 * @param target The new value of 'this'.
	 */
	public void set( T target );

	/**
	 * <p>
	 * Computes a transform which is the equivalent to applying 'this' then
	 * the 'second' transform.
	 * </p>
	 * <p/>
	 * <p>
	 * For example:<br>
	 * <br>
	 * Point A = tran2( tran1( A ) );<br>
	 * Point A = tran12( A );<br>
	 * <br>
	 * where tran12 = tran1.concat( tran2 , null );
	 * </p>
	 *
	 * <p>
	 * NOTE: 'second', 'result', and 'this' must all be unique instances.
	 * </p>
	 *
	 * @param second The second transform which is applied.  Not modified.
	 * @param result A transform which is equivalent to applying the first then the second.
	 *               If null then a new instance is declared. Modified.
	 * @return The equivalent transform.
	 */
	public T concat( T second, T result );

	/**
	 * <p>
	 * Computes a transform which is the inverse of this transform. The 'this' matrix can be passed
	 * in as an input.
	 * </p>
	 * <p/>
	 * <p>
	 * Example:<br>
	 * Point A = tran(B);<br>
	 * Point B = inv(A);<br>
	 * <br>
	 * where inv = invert( tran );
	 * </p>
	 *
	 * @param inverse Where the inverse will be stored.  If null a new instance is created.  Modified.
	 * @return The inverse transform.
	 */
	public T invert( T inverse );

	/**
	 * Sets the transform to its initial state of no transform.
	 */
	public void reset();
}
