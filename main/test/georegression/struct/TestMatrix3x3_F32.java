/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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

import georegression.misc.GrlConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestMatrix3x3_F32 {

	@Test
	public void set_matrix() {
		Matrix3x3_F32 a = new Matrix3x3_F32();
		a.set(1, 2, 3, 4, 5, 6, 7, 8, 9);

		Matrix3x3_F32 m = new Matrix3x3_F32();
		m.set(a);

		assertTrue(m.a11 == 1);
		assertTrue(m.a12 == 2);
		assertTrue(m.a13 == 3);
		assertTrue(m.a21 == 4);
		assertTrue(m.a22 == 5);
		assertTrue(m.a23 == 6);
		assertTrue(m.a31 == 7);
		assertTrue(m.a32 == 8);
		assertTrue(m.a33 == 9);
	}

	@Test
	public void set_values() {
		Matrix3x3_F32 m = new Matrix3x3_F32();
		m.set(1,2,3,4,5,6,7,8,9);

		assertTrue(m.a11 == 1);
		assertTrue(m.a12 == 2);
		assertTrue(m.a13 == 3);
		assertTrue(m.a21 == 4);
		assertTrue(m.a22 == 5);
		assertTrue(m.a23 == 6);
		assertTrue(m.a31 == 7);
		assertTrue(m.a32 == 8);
		assertTrue(m.a33 == 9);
	}

	@Test
	public void scale() {
		Matrix3x3_F32 m = new Matrix3x3_F32();
		m.set(1,2,3,4,5,6,7,8,9);
		m.scale(2);

		assertEquals(2,m.a11, GrlConstants.TEST_F32);
		assertEquals(4,m.a12, GrlConstants.TEST_F32);
		assertEquals(6,m.a13, GrlConstants.TEST_F32);
		assertEquals(8,m.a21, GrlConstants.TEST_F32);
		assertEquals(10,m.a22, GrlConstants.TEST_F32);
		assertEquals(12,m.a23, GrlConstants.TEST_F32);
		assertEquals(14,m.a31, GrlConstants.TEST_F32);
		assertEquals(16,m.a32, GrlConstants.TEST_F32);
		assertEquals(18,m.a33, GrlConstants.TEST_F32);
	}
}
