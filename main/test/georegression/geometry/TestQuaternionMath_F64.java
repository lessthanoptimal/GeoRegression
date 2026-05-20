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
import georegression.struct.so.Quaternion_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestQuaternionMath_F64 extends GeoRegressionJUnit {
	@Test void multiply() {
		double tol = GrlConstants.TEST_F64;

		// Test 1: identity * identity = identity
		var identity = new Quaternion_F64(1, 0, 0, 0);
		var result = QuaternionMath_F64.multiply(identity, identity, null);
		assertEquals(1.0, result.w, tol);
		assertEquals(0.0, result.x, tol);
		assertEquals(0.0, result.y, tol);
		assertEquals(0.0, result.z, tol);

		// Test 2: q * identity = q
		var q = new Quaternion_F64(0.7071067811865476, 0.7071067811865476, 0, 0); // 90 deg around X
		result = QuaternionMath_F64.multiply(q, identity, null);
		assertEquals(q.w, result.w, tol);
		assertEquals(q.x, result.x, tol);
		assertEquals(q.y, result.y, tol);
		assertEquals(q.z, result.z, tol);

		// Test 3: identity * q = q
		result = QuaternionMath_F64.multiply(identity, q, null);
		assertEquals(q.w, result.w, tol);
		assertEquals(q.x, result.x, tol);
		assertEquals(q.y, result.y, tol);
		assertEquals(q.z, result.z, tol);

		// Test 4: q * q_inverse = identity
		// inverse of unit quaternion is conjugate: (w, -x, -y, -z)
		result = QuaternionMath_F64.multiply(q, q.conjugated(null), null);
		assertEquals(1.0, result.w, tol);
		assertEquals(0.0, result.x, tol);
		assertEquals(0.0, result.y, tol);
		assertEquals(0.0, result.z, tol);

		// Test 5: 90 deg around X * 90 deg around Y = known result
		// R_x(90) * R_y(90) in Hamilton convention
		var qx = new Quaternion_F64(0.7071067811865476, 0.7071067811865476, 0, 0); // 90 deg around X
		var qy = new Quaternion_F64(0.7071067811865476, 0, 0.7071067811865476, 0); // 90 deg around Y
		result = QuaternionMath_F64.multiply(qx, qy, null);
		// verify result is unit quaternion
		assertEquals(1.0, result.norm(), tol);
		// verify known values: w=0.5, x=0.5, y=0.5, z=0.5
		assertEquals(0.5, result.w, tol);
		assertEquals(0.5, result.x, tol);
		assertEquals(0.5, result.y, tol);
		assertEquals(0.5, result.z, tol);

		// Test 6: non-commutativity q1*q2 != q2*q1
		result = QuaternionMath_F64.multiply(qy, qx, null);
		// verify known values: w=0.5, x=0.5, y=0.5, z=-0.5
		assertEquals(0.5, result.w, tol);
		assertEquals(0.5, result.x, tol);
		assertEquals(0.5, result.y, tol);
		assertEquals(-0.5, result.z, tol);

		// Test 7: result of multiplying two unit quaternions is a unit quaternion
		var q1 = new Quaternion_F64(0.5, 0.5, 0.5, 0.5);
		var q2 = new Quaternion_F64(0.5, -0.5, 0.5, -0.5);
		result = QuaternionMath_F64.multiply(q1, q2, null);
		assertEquals(1.0, result.norm(), tol);
	}
}