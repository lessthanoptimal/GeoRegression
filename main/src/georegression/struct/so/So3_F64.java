/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.struct.so;

import georegression.struct.InvertibleTransform;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.jetbrains.annotations.Nullable;

/**
 * Special Orthogonal, i.e. rotation in 3D.
 *
 * @author Peter Abeles
 */
public class So3_F64 implements InvertibleTransform<So3_F64> {
	public final DMatrixRMaj R = new DMatrixRMaj(3,3);

	public So3_F64() {}

	public So3_F64( DMatrixRMaj R ) {
		this.R.setTo(R);
	}

	@Override
	public int getDimension() {
		return 9; // really 3 DOF, but it's parameterized using a rotation matrix
	}

	@Override
	public So3_F64 createInstance() {
		return new So3_F64();
	}

	@Override
	public void setTo(So3_F64 target) {
		this.R.setTo(target.R);
	}

	@Override
	public So3_F64 concat(So3_F64 second, @Nullable So3_F64 result) {
		if (result == null)
			result = new So3_F64();
		CommonOps_DDRM.mult(second.R, R, result.R);
		return result;
	}

	@Override
	public So3_F64 invert(@Nullable So3_F64 inverse) {
		if (inverse == null)
			inverse = new So3_F64();
		CommonOps_DDRM.transpose(R, inverse.R);
		return inverse;
	}

	@Override
	public void reset() {
		CommonOps_DDRM.setIdentity(R);
	}
}
