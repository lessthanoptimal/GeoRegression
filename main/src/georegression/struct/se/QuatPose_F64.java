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

package georegression.struct.se;

import georegression.geometry.QuaternionMath_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.so.Quaternion_F64;
import georegression.struct.tuples.GeoTuple3D_F64;
import lombok.Getter;
import org.ejml.MapPrintFormat;
import org.jetbrains.annotations.Nullable;

/// SE3 transform that encodes orientation using a [Quaternion_F64] and translation with a [Vector3D_F64]
public class QuatPose_F64 implements SpecialEuclidean<QuatPose_F64> {
	// serialization version
	public static final long serialVersionUID = 1L;

	/// Orientation/rotation encoded as a quaternion
	@Getter public Quaternion_F64 ori = new Quaternion_F64();
	/// translation vector
	@Getter public Vector3D_F64 t = new Vector3D_F64();

	public QuatPose_F64() {}

	@Override public int getDimension() {
		return 3;
	}

	@Override public QuatPose_F64 createInstance() {
		return new QuatPose_F64();
	}

	@Override public QuatPose_F64 setTo( QuatPose_F64 target ) {
		ori.setTo(target.ori);
		t.setTo(target.t);
		return this;
	}

	public QuatPose_F64 setTo( Quaternion_F64 ori, GeoTuple3D_F64<?> t ) {
		this.ori.setTo(ori);
		this.t.setTo(t.x, t.y, t.z);
		return this;
	}

	/// Normalizes the quaternion and returns this
	public QuatPose_F64 normalize() {
		this.ori.normalize();
		return this;
	}

	@Override public QuatPose_F64 concat( QuatPose_F64 second, @Nullable QuatPose_F64 result ) {
		if (result == null) {
			result = new QuatPose_F64();
		}

		// result = "second" applied after "this"
		// R = second.R * this.R,  T = second.T + second.R*this.T
		QuaternionMath_F64.multiply(second.ori, ori, result.ori);
		QuaternionMath_F64.addMult(second.t, second.ori, t, result.t);

		return result;
	}

	@Override public QuatPose_F64 invertConcat( QuatPose_F64 second, @Nullable QuatPose_F64 result ) {
		if (result == null) {
			result = new QuatPose_F64();
		}

		// result = second applied after this^-1
		// R = second.R * this.R^T,  T = second.T + second.R*(-this.R^T*this.T)
		QuaternionMath_F64.multConjB(second.ori, ori, result.ori);   // second.R * this.R^T
		QuaternionMath_F64.multTran(ori, t, result.t);               // this.R^T * this.T
		result.t.scale(-1);                                          // -this.R^T*this.T
		QuaternionMath_F64.addMult(second.t, second.ori, result.t, result.t);

		return result;
	}

	@Override public QuatPose_F64 concatInvert( QuatPose_F64 second, @Nullable QuatPose_F64 result ) {
		if (result == null) {
			result = new QuatPose_F64();
		}

		// result = second^-1 applied after this
		// R = second.R^T * this.R,  T = second.R^T*(this.T - second.T)
		QuaternionMath_F64.multConjA(second.ori, ori, result.ori);     // second.R^T * this.R
		QuaternionMath_F64.multTran(second.ori, second.t, result.t);   // second.R^T * second.T
		result.t.scale(-1);                                            // -second.R^T*second.T
		QuaternionMath_F64.addMultTran(result.t, second.ori, t, result.t);

		return result;
	}

	@Override public QuatPose_F64 invert( @Nullable QuatPose_F64 inverse ) {
		if (inverse == null) {
			inverse = new QuatPose_F64();
		}

		// inverse rotation is the conjugate, inverse translation is -this.R^T*this.T
		QuaternionMath_F64.multTran(ori, t, inverse.t);   // this.R^T * this.T  (uses ori before conjugating)
		inverse.t.scale(-1);
		ori.conjugated(inverse.ori);

		return inverse;
	}

	@Override public void reset() {
		ori.setToIdentity();
		t.zero();
	}

	@Override public String formatMap( MapPrintFormat format ) {
		return format.itemPrefix+
				format.pair("ori", ori.formatMap(format), true) +
				format.pair("t", t.formatMap(format), false) +
				format.itemSuffix;
	}
}
