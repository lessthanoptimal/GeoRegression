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

package georegression.geometry;

import georegression.GeoRegressionJUnit;
import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.so.Quaternion_F64;
import georegression.struct.tuples.GeoTuple3D_F64;
import org.ejml.data.DMatrixRMaj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestQuaternionMath_F64 extends GeoRegressionJUnit {
	double tol = GrlConstants.TEST_F64;

	// Arbitrary test quaternions designed to avoid repeated values/zeros found in others
	// tests
	Quaternion_F64 q1 = new Quaternion_F64(1, 2, 3, 4);
	Quaternion_F64 q2 = new Quaternion_F64(5, 6, 7, 8);

	Quaternion_F64 q3 = new Quaternion_F64(-2, 0.5, -1.5, 3.25);
	Quaternion_F64 q4 = new Quaternion_F64(0.75, -4, 2, -0.5);

	@Test void multiply() {
		// Test 1: identity * identity = identity
		var identity = new Quaternion_F64(1, 0, 0, 0);
		var result = QuaternionMath_F64.multiply(identity, identity, null);
		assertTrue(result.isIdentical(1, 0, 0, 0, tol));

		// Test 2: q * identity = q
		var q = new Quaternion_F64(0.7071067811865476, 0.7071067811865476, 0, 0); // 90 deg around X
		result = QuaternionMath_F64.multiply(q, identity, null);
		assertTrue(result.isIdentical(q.w, q.x, q.y, q.z, tol));

		// Test 3: identity * q = q
		result = QuaternionMath_F64.multiply(identity, q, null);
		assertTrue(result.isIdentical(q.w, q.x, q.y, q.z, tol));

		// Test 4: q * q_inverse = identity
		// inverse of unit quaternion is conjugate: (w, -x, -y, -z)
		result = QuaternionMath_F64.multiply(q, q.conjugated(null), null);
		assertTrue(result.isIdentical(1, 0, 0, 0, tol));

		// Test 5: 90 deg around X * 90 deg around Y = known result
		// R_x(90) * R_y(90) in Hamilton convention
		var qx = new Quaternion_F64(0.7071067811865476, 0.7071067811865476, 0, 0); // 90 deg around X
		var qy = new Quaternion_F64(0.7071067811865476, 0, 0.7071067811865476, 0); // 90 deg around Y
		result = QuaternionMath_F64.multiply(qx, qy, null);
		assertTrue(result.isIdentical(0.5, 0.5, 0.5, 0.5, tol));

		// Test 6: non-commutativity q1*q2 != q2*q1
		result = QuaternionMath_F64.multiply(qy, qx, null);
		assertTrue(result.isIdentical(0.5, 0.5, 0.5, -0.5, tol));

		// Test 7: result of multiplying two unit quaternions is a unit quaternion
		result = QuaternionMath_F64.multiply(
				new Quaternion_F64(0.5, 0.5, 0.5, 0.5),
				new Quaternion_F64(0.5, -0.5, 0.5, -0.5), null);
		assertTrue(result.isIdentical(0.5, -0.5, 0.5, 0.5, tol));

		// Test 8: arbitrary quaternions, every term distinct and nonzero
		result = QuaternionMath_F64.multiply(q1, q2, null);
		assertTrue(result.isIdentical(-60, 12, 30, 24, tol));
	}

	@Test void multConjA() {
		// conj(q1)*q2
		checkSame(QuaternionMath_F64.multConjA(q1, q2, null),
				QuaternionMath_F64.multiply(q1.conjugated(null), q2, null));
		checkSame(QuaternionMath_F64.multConjA(q3, q4, null),
				QuaternionMath_F64.multiply(q3.conjugated(null), q4, null));
	}

	@Test void multConjB() {
		// q1*conj(q2)
		checkSame(QuaternionMath_F64.multConjB(q1, q2, null),
				QuaternionMath_F64.multiply(q1, q2.conjugated(null), null));
		checkSame(QuaternionMath_F64.multConjB(q3, q4, null),
				QuaternionMath_F64.multiply(q3, q4.conjugated(null), null));
	}

	@Test void multConjAB() {
		// conj(q1)*conj(q2)
		checkSame(QuaternionMath_F64.multConjAB(q1, q2, null),
				QuaternionMath_F64.multiply(q1.conjugated(null), q2.conjugated(null), null));
		checkSame(QuaternionMath_F64.multConjAB(q3, q4, null),
				QuaternionMath_F64.multiply(q3.conjugated(null), q4.conjugated(null), null));
	}

	private void checkSame( Quaternion_F64 result, Quaternion_F64 expected ) {
		assertTrue(result.isIdentical(expected.w, expected.x, expected.y, expected.z, tol));
	}

	/// Tests all rotate inplace functions for 3D points
	@Test void mult_Tuple3D() {
		//@formatter:off
		Quaternion_F64 q = ConvertRotation3D_F64.eulerToQuaternion(EulerType.XYZ, 0.3, -0.7, 1.1, null);
		DMatrixRMaj R = ConvertRotation3D_F64.quaternionToMatrix(q, null);
		var v = new Vector3D_F64(1.5, -2.0, 0.75);
		var a = new Vector3D_F64(-0.4, 3.1, -1.2);

		// matrix ground truth, reused for both correctness and in-place checks
		Vector3D_F64 Rv = GeometryMath_F64.mult(R, v, new Vector3D_F64());       // R*v
		Vector3D_F64 Rtv = GeometryMath_F64.multTran(R, v, new Vector3D_F64());  // R^T*v
		var aRv = new Vector3D_F64();  GeometryMath_F64.add(a, Rv, aRv);         // a + R*v
		var aRtv = new Vector3D_F64(); GeometryMath_F64.add(a, Rtv, aRtv);       // a + R^T*v

		checkSame("mult",        QuaternionMath_F64.mult(q, v, null), Rv);
		checkSame("multTran",    QuaternionMath_F64.multTran(q, v, null), Rtv);
		checkSame("addMult",     QuaternionMath_F64.addMult(a, q, v, null), aRv);
		checkSame("addMultTran", QuaternionMath_F64.addMultTran(a, q, v, null), aRtv);

		// in-place output: src==dst for the rotations, out==a for the accumulators
		Vector3D_F64 t;
		t = v.copy(); checkSame("mult inplace",        QuaternionMath_F64.mult(q, t, t), Rv);
		t = v.copy(); checkSame("multTran inplace",    QuaternionMath_F64.multTran(q, t, t), Rtv);
		t = a.copy(); checkSame("addMult inplace",     QuaternionMath_F64.addMult(t, q, v, t), aRv);
		t = a.copy(); checkSame("addMultTran inplace", QuaternionMath_F64.addMultTran(t, q, v, t), aRtv);
		//@formatter:on
	}

	private void checkSame( String op, GeoTuple3D_F64<?> found, GeoTuple3D_F64<?> expected ) {
		assertTrue(found.isIdentical(expected.x, expected.y, expected.z, tol), op);
	}
}