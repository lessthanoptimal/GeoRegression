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

package georegression.struct;

import georegression.struct.tuples.GeoTuple;
import org.ejml.MapFormattable;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/// Any transform which has a unique inverse. T(u) = v and u = T<sup>-1</sup>(v).
///
/// Functions are provided to determining the dimensionality of the transform, inverting the transform,
/// and concating two transforms.
///
/// Design Note: A function to apply the transform has not been provided to any
/// data structures (e.g. [GeoTuple]). Instead that has been pushed off onto specialized static
/// functions in other classes due to the large number of needed functions.
///
/// @author Peter Abeles
public interface InvertibleTransform<T extends InvertibleTransform> extends Serializable, MapFormattable {

	/// Returns the dimension of the space which this transform operates on.
	///
	/// @return space's dimension
	int getDimension();

	/// Creates a new instance of the same SpecialEuclidean as this class.
	///
	/// @return A new instance.
	T createInstance();

	/// Assigns 'this' to the value of target.
	///
	/// @param target The new value of 'this'.
	/// @return A reference to 'this' to enable chaining
	T setTo( T target );

	/// Computes a transform which is the equivalent to applying 'this' then
	/// the 'second' transform.
	///
	/// For example:
	///
	/// Point A = tran2( tran1( A ) );
	///
	/// Point A = tran12( A );
	///
	/// where tran12 = tran1.concat( tran2 , null );
	///
	/// NOTE: 'second', 'result', and 'this' must all be unique instances.
	///
	/// @param second The second transform which is applied. Not modified.
	/// @param result A transform which is equivalent to applying the first then the second.
	/// If null then a new instance is declared. Modified.
	/// @return The equivalent transform.
	T concat( T second, @Nullable T result );

	/// Computes 'invert(null).concat(second, result)' with more concise syntax and potentially more efficient
	/// implementation that avoids memory allocation.
	///
	/// @param second The second transform which is applied. Not modified.
	/// @param result (Output) storage for resulting transform. Can be null
	/// @return The computed transform.
	default T concatInvA( T second, @Nullable T result ) {
		return invertConcat(second, result);
	}

	/// Computes 'this.concat(second.invert(null), result)'. With more concise syntax and potentially more efficient
	/// 	/// implementation that avoids memory allocation.
	///
	/// @param second The second transform which is applied. Not modified.
	/// @param result (Output) storage for resulting transform. Can be null
	/// @return The computed transform.
	default T concatInvB( T second, @Nullable T result ) {
		return concatInvert(second, result);
	}

	/// Computes a transform that's equivalent to 'invert(null).concat(second.invert(null), result)'. The advantage of
	/// using this function is that it might have been implemented so that the inversion is implicit, which can
	/// result in no memory creation and more stable numerics.
	///
	/// @param second The second transform which is applied. Not modified.
	/// @param result (Output) storage for resulting transform. Can be null
	/// @return The computed transform.
	default T concatInvAB( T second, @Nullable T result ) {
		return concatInvA((T)second.invert(null), null);
	}

	/// Computes a transform which is the inverse of this transform. The 'this' matrix can be passed
	/// in as an input.
	///
	/// Example:
	///
	/// Point A = tran(B);
	///
	/// Point B = inv(A);
	///
	/// where inv = invert( tran );
	///
	/// @param inverse Where the inverse will be stored. If null a new instance is created. Modified.
	/// @return The inverse transform.
	T invert( @Nullable T inverse );

	/// Depreciated. Use [#concatInvA] instead
	@Deprecated
	default T invertConcat(T second, @Nullable T result) {
		return (T)invert(null).concat(second, result);
	}

	/// Depreciated. Use [#concatInvB] instead
	@Deprecated
	default T concatInvert(T second, @Nullable T result) {
		return concat((T)second.invert(null), result);
	}

	/// Sets the transform to its initial state of no transform.
	void reset();
}
