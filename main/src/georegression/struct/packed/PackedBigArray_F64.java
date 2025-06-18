/*
 * Copyright (C) 2025, Peter Abeles. All Rights Reserved.
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
import org.ddogleg.struct.BigDogArray_F64;
import org.ddogleg.struct.BigDogGrowth;

public abstract class PackedBigArray_F64<T> implements PackedArray<T> {
	/** Degrees of freedom */
	protected final int DOF;

	/** Stores tuple in a single continuous array */
	@Getter protected final BigDogArray_F64 array;

	public PackedBigArray_F64( int DOF, int reservedPoints, int blockSize, BigDogGrowth growth ) {
		this.DOF = DOF;
		this.array = new BigDogArray_F64(reservedPoints*DOF, blockSize*DOF, growth);
	}

	@Override public PackedBigArray_F64<T> reset() {
		array.reset();
		return this;
	}

	@Override public PackedBigArray_F64<T> reserve( int numTuples ) {
		array.reserve(numTuples*DOF);
		return this;
	}

	@Override public void removeSwap( int index ) {
		int idx0 = index*DOF;
		int idx1 = idx0 + DOF;
		int tail = array.size - DOF;
		for (int j = idx0; j < idx1; j++) {
			array.set(j, array.get(tail++));
		}
		array.size -= DOF;
	}

	@Override public int size() {
		return array.size/DOF;
	}

	@Override public boolean isEquals( PackedArray<T> b ) {return array.isEquivalent(((PackedBigArray_F64<T>)b).array, 0.0);}

	/** True if the two arrays are equal to within the specified tolerance */
	public boolean isEquals( PackedArray<T> b, double tol ) {return array.isEquivalent(((PackedBigArray_F64<T>)b).array, tol);}
}
