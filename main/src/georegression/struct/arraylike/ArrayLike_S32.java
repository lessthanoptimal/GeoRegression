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

package georegression.struct.arraylike;

import org.ejml.MatrixFormattable;
import org.ejml.MatrixPrintFormat;

/// Interface for data structures which can be represented as an array. Provides a common way access elements
/// and convert into an actual array
public interface ArrayLike_S32 extends MatrixFormattable {
	/// Access value at element
	int get( int index );

	/// Set value at element
	void set( int index, int value );

	/// Number of elements in the array
	int length();

	/// Converts data structures into an array
	default int[] toArray() {
		var out = new int[length()];
		for (int i = 0; i < out.length; i++) {
			out[i] = get(i);
		}
		return out;
	}

	/// Prints as a row vector
	default String format( MatrixPrintFormat format ) {
		int size = length();
		var builder = new StringBuilder();
		builder.append(format.getRowPrefix());
		for (int i = 0; i < size - 1; i++) {
			builder.append(get(i));
			builder.append(format.getColSeparator());
		}
		builder.append(get(size - 1));
		builder.append(format.getRowSuffix());
		return builder.toString();
	}

	/// Returns true if the two data structures have elements which are all within the specified tolerance
	/// of each other
	default boolean isEquals( ArrayLike_S32 a, int tol ) {
		if (length() != a.length())
			return false;

		int length = length();
		for (int i = 0; i < length; i++) {
			if (Math.abs(get(i) - a.get(i)) > tol) {
				return false;
			}
		}
		return true;
	}
}
