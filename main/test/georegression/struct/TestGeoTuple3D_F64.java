/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

import static org.junit.jupiter.api.Assertions.*;

public class TestGeoTuple3D_F64 {
	@Test
	void plusIP() {
		Dummy a = new Dummy(1,2,3);
		Dummy b = new Dummy(4,5,6);

		a.plusIP(b);

		assertEquals(5,a.x, GrlConstants.TEST_F64);
		assertEquals(7,a.y, GrlConstants.TEST_F64);
		assertEquals(9,a.z, GrlConstants.TEST_F64);
	}

	@Test
	void plus() {
		Dummy a = new Dummy(1,2,3);
		Dummy b = new Dummy(4,5,6);

		Dummy c = a.plus(b);

		assertEquals(1,a.x, GrlConstants.TEST_F64);
		assertEquals(2,a.y, GrlConstants.TEST_F64);
		assertEquals(3,a.z, GrlConstants.TEST_F64);

		assertEquals(5,c.x, GrlConstants.TEST_F64);
		assertEquals(7,c.y, GrlConstants.TEST_F64);
		assertEquals(9,c.z, GrlConstants.TEST_F64);
	}

	@Test
	void plusIP_alpha() {
		Dummy a = new Dummy(1,2,3);
		Dummy b = new Dummy(4,5,6);

		a.plusIP(2.0, b);

		assertEquals(9,a.x, GrlConstants.TEST_F64);
		assertEquals(12,a.y, GrlConstants.TEST_F64);
		assertEquals(15,a.z, GrlConstants.TEST_F64);
	}

	@Test
	void plus_alpha() {
		Dummy a = new Dummy(1,2,3);
		Dummy b = new Dummy(4,5,6);

		Dummy c = a.plus(2.0, b);

		assertEquals(1,a.x, GrlConstants.TEST_F64);
		assertEquals(2,a.y, GrlConstants.TEST_F64);
		assertEquals(3,a.z, GrlConstants.TEST_F64);

		assertEquals(9,c.x, GrlConstants.TEST_F64);
		assertEquals(12,c.y, GrlConstants.TEST_F64);
		assertEquals(15,c.z, GrlConstants.TEST_F64);
	}

	@Test
	void timesIP() {
		Dummy a = new Dummy(1,2,3);

		a.timesIP(2.5);

		assertEquals(2.5,a.x, GrlConstants.TEST_F64);
		assertEquals(5,a.y, GrlConstants.TEST_F64);
		assertEquals(7.5,a.z, GrlConstants.TEST_F64);
	}

	@Test
	void scale() {
		Dummy a = new Dummy(1,2,3);

		a.scale(2.5);

		assertEquals(2.5,a.x, GrlConstants.TEST_F64);
		assertEquals(5,a.y, GrlConstants.TEST_F64);
		assertEquals(7.5,a.z, GrlConstants.TEST_F64);
	}

	@Test
	void divideIP() {
		Dummy a = new Dummy(1,2,3);

		a.divideIP(2.5);

		assertEquals(1.0/2.5,a.x, GrlConstants.TEST_F64);
		assertEquals(2.0/2.5,a.y, GrlConstants.TEST_F64);
		assertEquals(3.0/2.5,a.z, GrlConstants.TEST_F64);
	}

	@Test
	void times() {
		Dummy a = new Dummy(1,2,3);

		Dummy b = a.times(2.5);

		assertEquals(1,a.x, GrlConstants.TEST_F64);
		assertEquals(2,a.y, GrlConstants.TEST_F64);
		assertEquals(3,a.z, GrlConstants.TEST_F64);

		assertEquals(2.5,b.x, GrlConstants.TEST_F64);
		assertEquals(5,b.y, GrlConstants.TEST_F64);
		assertEquals(7.5, b.z, GrlConstants.TEST_F64);
	}

	@Test
	void distance_xyz(){
		Dummy a = new Dummy(1,2,3);

		double found = a.distance(2,-1,4);
		assertEquals(Math.sqrt(1+9+1),found,GrlConstants.TEST_F64);
	}

	@Test void isNaN() {
		var a = new Dummy(1,2,3);
		assertFalse(a.isNaN());

		a.x = Double.NaN;
		assertTrue(a.isNaN());
		a.x = 1;
		a.y = Double.NaN;
		assertTrue(a.isNaN());
		a.y = 1;
		a.z = Double.NaN;
		assertTrue(a.isNaN());

		// Sanity check
		a.z = 1;
		assertFalse(a.isNaN());
	}

	@Test void isInfinite() {
		var a = new Dummy(1,2,3);
		assertFalse(a.isInfinite());

		a.x = Double.POSITIVE_INFINITY;
		assertTrue(a.isInfinite());
		a.x = 1;
		a.y = Double.POSITIVE_INFINITY;
		assertTrue(a.isInfinite());
		a.y = 1;
		a.z = Double.POSITIVE_INFINITY;
		assertTrue(a.isInfinite());

		// Sanity check
		a.z = 1;
		assertFalse(a.isInfinite());
	}

	public static class Dummy extends GeoTuple3D_F64<Dummy> {
		public Dummy() {
		}

		public Dummy(double x, double y, double z) {
			super(x, y, z);
		}

		@Override
		public Dummy createNewInstance() {
			return new Dummy();
		}
	}
}
