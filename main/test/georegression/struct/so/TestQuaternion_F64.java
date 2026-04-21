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

package georegression.struct.so;

import georegression.misc.GrlConstants;
import org.ejml.MapPrintFormat;
import org.ejml.MatrixPrintFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestQuaternion_F64 {
	@Test void setTo_quat() {
		var found = new Quaternion_F64().setTo(new Quaternion_F64(1, 2, 3, 4));
		assertEquals(1, found.w);
		assertEquals(2, found.x);
		assertEquals(3, found.y);
		assertEquals(4, found.z);
	}

	@Test void setTo_values() {
		var found = new Quaternion_F64().setTo(1, 2, 3, 4);
		assertEquals(1, found.w);
		assertEquals(2, found.x);
		assertEquals(3, found.y);
		assertEquals(4, found.z);
	}

	@Test void setToIdentity() {
		var found = new Quaternion_F64().setTo(0, 2, 3, 4);
		assertSame(found, found.setToIdentity());
		assertEquals(1, found.w);
		assertEquals(0, found.x);
		assertEquals(0, found.y);
		assertEquals(0, found.z);
	}

	@Test void norm() {
		double found = new Quaternion_F64().setTo(1, 2, 3, 4).norm();
		double expected = Math.sqrt(1 + 4 + 9 + 16);
		assertEquals(expected, found);
	}

	@Test void normalize() {
		var q = new Quaternion_F64(2, 1, 3, 4);
		assertNotEquals(1.0, q.norm(), GrlConstants.TEST_F64);
		q.normalize();
		assertEquals(1.0, q.norm(), GrlConstants.TEST_F64);
	}

	@Test void normalized() {
		var q = new Quaternion_F64(2, 1, 3, 4);
		assertEquals(1.0, q.normalized(null).norm(), GrlConstants.TEST_F64);
		assertTrue(q.isIdentical(new Quaternion_F64(2, 1, 3, 4), GrlConstants.TEST_F64));
	}

	@Test void conjugated() {
		var q = new Quaternion_F64(2, 1, 3, 4);
		Quaternion_F64 found = q.conjugated(null);

		// Test using the definition
		assertEquals(q.w, found.w);
		assertEquals(q.x, -found.x);
		assertEquals(q.y, -found.y);
		assertEquals(q.z, -found.z);
	}

	@Test void format_matric() {
		var a = new Quaternion_F64(1, 2, 0.00000213, 0.5);
		String found = a.format(new MatrixPrintFormat().fsetPrecision(2));
		assertEquals("{1, 2, 2.13e-06, 0.5}", found);
	}
	@Test void format_map() {
		var a = new Quaternion_F64(1, 2, 0.00000213, 0.5);
		String found = a.format(new MapPrintFormat().fsetPrecision(2));
		assertEquals("{w: 1, x: 2, y: 2.13e-06, z: 0.5}", found);
	}
}
