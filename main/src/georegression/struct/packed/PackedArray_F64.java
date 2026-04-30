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

package georegression.struct.packed;

import lombok.Getter;
import org.ddogleg.struct.DogArray_F64;
import org.ejml.MapPrintFormat;
import org.ejml.MatrixPrintFormat;
import org.ejml.UtilEjml;

public abstract class PackedArray_F64<T> implements PackedArray<T> {
	/** Degrees of freedom */
	protected final int DOF;

	/** Stores tuple in a single continuous array */
	@Getter protected final DogArray_F64 array;

	public PackedArray_F64( int DOF, int length ) {
		this.DOF = DOF;
		this.array = new DogArray_F64(length);
	}

	@Override public void removeSwap( int index ) {
		int idx0 = index*DOF;
		int idx1 = idx0 + DOF;
		int tail = array.size - DOF;
		for (int j = idx0; j < idx1; j++) {
			array.data[j] = array.data[tail++];
		}
		array.size -= DOF;
	}

	@Override public PackedArray_F64<T> reset() {
		array.reset();
		return this;
	}

	@Override public PackedArray_F64<T> reserve( int numTuples ) {
		array.reserve(numTuples*DOF);
		return this;
	}

	@Override public int size() {
		return array.size/DOF;
	}

	@Override public boolean isEquals( PackedArray<T> b ) {return array.isEquals(((PackedArray_F64<T>)b).array);}

	/** True if the two arrays are equal to within the specified tolerance */
	public boolean isEquals( PackedArray<T> b, double tol ) {return array.isEquals(((PackedArray_F64<T>)b).array, tol);}

	@Override public void resize( int size ) {
		array.resize(size*DOF, 0.0);
	}

	public String format( MatrixPrintFormat format ) {
		char decimal = format.decimal;
		var builder = new StringBuilder();
		builder.append(format.prefix);
		for (int i = 0; i < size(); i++) {
			int idxRow = i*DOF;

			builder.append(format.rowPrefix);
			for (int col = 0; col < DOF; col++) {
				double value = array.data[idxRow+col];
				builder.append(UtilEjml.fancyString2(value, format.getPrecision(), decimal));
				if (col < DOF-1)
					builder.append(format.colSeparator);
			}
			builder.append(format.rowSuffix);
			if (i < size() - 1)
				builder.append(format.rowSeparator);
		}
		builder.append(format.suffix);
		return builder.toString();
	}

	protected String formatMap( MapPrintFormat format, String[] keys ) {
		if (keys.length != DOF)
			throw new IllegalArgumentException("Number of keys should match DOF");

		char decimal = format.decimal;
		var builder = new StringBuilder();
		builder.append(format.listPrefix);
		for (int i = 0; i < size(); i++) {
			int idxRow = i*DOF;

			builder.append(format.itemPrefix);
			for (int col = 0; col < DOF; col++) {
				double value = array.data[idxRow+col];
				builder.append(keys[col]).append(format.valueSeparator);
				builder.append(UtilEjml.fancyString2(value, format.getPrecision(), decimal));
				if (col < DOF-1)
					builder.append(format.pairSeparator);
			}
			builder.append(format.itemSuffix);
			if (i < size() - 1)
				builder.append(format.itemSeparator);
		}
		builder.append(format.listSuffix);
		return builder.toString();
	}
}
