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

package georegression.struct;

import georegression.misc.GrlConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestMatrix3x3_F64 {

	@Test
	void set_matrix() {
		Matrix3x3_F64 a = new Matrix3x3_F64();
		a.setTo(1, 2, 3, 4, 5, 6, 7, 8, 9);

		Matrix3x3_F64 m = new Matrix3x3_F64();
		m.setTo(a);

		assertEquals(m.a11, 1);
		assertEquals(m.a12, 2);
		assertEquals(m.a13, 3);
		assertEquals(m.a21, 4);
		assertEquals(m.a22, 5);
		assertEquals(m.a23, 6);
		assertEquals(m.a31, 7);
		assertEquals(m.a32, 8);
		assertEquals(m.a33, 9);
	}

	@Test
	void set_values() {
		Matrix3x3_F64 m = new Matrix3x3_F64();
		m.setTo(1,2,3,4,5,6,7,8,9);

		assertEquals(m.a11, 1);
		assertEquals(m.a12, 2);
		assertEquals(m.a13, 3);
		assertEquals(m.a21, 4);
		assertEquals(m.a22, 5);
		assertEquals(m.a23, 6);
		assertEquals(m.a31, 7);
		assertEquals(m.a32, 8);
		assertEquals(m.a33, 9);
	}

	@Test
	void scale() {
		Matrix3x3_F64 m = new Matrix3x3_F64();
		m.setTo(1,2,3,4,5,6,7,8,9);
		m.scale(2);

		assertEquals(2,m.a11, GrlConstants.TEST_F64);
		assertEquals(4,m.a12, GrlConstants.TEST_F64);
		assertEquals(6,m.a13, GrlConstants.TEST_F64);
		assertEquals(8,m.a21, GrlConstants.TEST_F64);
		assertEquals(10,m.a22, GrlConstants.TEST_F64);
		assertEquals(12,m.a23, GrlConstants.TEST_F64);
		assertEquals(14,m.a31, GrlConstants.TEST_F64);
		assertEquals(16,m.a32, GrlConstants.TEST_F64);
		assertEquals(18,m.a33, GrlConstants.TEST_F64);
	}
}
