/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * <p>
 * Any transform which has an unique inverse. T(u) = v and u = T<sup>-1</sup>(v).
 * </p>
 * <p>
 * Functions are provided to determining the dimensionality of the transform, inverting the transform,
 * and concating two transforms.
 * </p>
 * <p>
 * Design Note: A function to apply the transform has not been provided to any
 * data structures (e.g. {@link GeoTuple}). Instead that has been pushed off onto specialized static
 * functions in other classes due to the large number of needed functions.
 * </p>
 *
 * @author Peter Abeles
 */
public interface InvertibleTransform<T extends InvertibleTransform> extends Serializable {

	/**
	 * Returns the dimension of the space which this transform operates on.
	 *
	 * @return space's dimension
	 */
	int getDimension();

	/**
	 * Creates a new instance of the same SpecialEuclidean as this class.
	 *
	 * @return A new instance.
	 */
	T createInstance();

	/**
	 * Assigns 'this' to the value of target.
	 *
	 * @param target The new value of 'this'.
	 * @return A reference to 'this' to enable chaining
	 */
	T setTo( T target );

	/**
	 * <p>
	 * Computes a transform which is the equivalent to applying 'this' then
	 * the 'second' transform.
	 * </p>
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
	 * @param second The second transform which is applied. Not modified.
	 * @param result A transform which is equivalent to applying the first then the second.
	 * If null then a new instance is declared. Modified.
	 * @return The equivalent transform.
	 */
	T concat( T second, @Nullable T result );

	/**
	 * <p>
	 * Computes a transform which is the inverse of this transform. The 'this' matrix can be passed
	 * in as an input.
	 * </p>
	 * <p>
	 * Example:<br>
	 * Point A = tran(B);<br>
	 * Point B = inv(A);<br>
	 * <br>
	 * where inv = invert( tran );
	 * </p>
	 *
	 * @param inverse Where the inverse will be stored. If null a new instance is created. Modified.
	 * @return The inverse transform.
	 */
	T invert( @Nullable T inverse );

	/**
	 * Computes a transform that's equivalent to 'invert(null).concat(second, result)'. The advantage of using
	 * this function is that it might have been implemented so that the inversion is implicit, which can result in
	 * no memory creation and more stable numerics.
	 *
	 * @param second The second transform which is applied. Not modified.
	 * @param result (Output) storage for rsulting transform. Can be null
	 * @return The computed transform. If result isn't null then result is returned.
	 */
	default T invertConcat(T second, @Nullable T result) {
		return (T)invert(null).concat(second, result);
	}

	/**
	 * Sets the transform to its initial state of no transform.
	 */
	void reset();
}
