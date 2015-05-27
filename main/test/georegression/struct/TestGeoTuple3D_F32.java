/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

/**
 * @author Peter Abeles
 */
public class TestGeoTuple3D_F32 {
	@Test
	public void plusIP() {
		Dummy a = new Dummy(1,2,3);
		Dummy b = new Dummy(4,5,6);

		a.plusIP(b);

		assertEquals(5,a.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(7,a.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(9, a.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void plus() {
		Dummy a = new Dummy(1,2,3);
		Dummy b = new Dummy(4,5,6);

		Dummy c = a.plus(b);

		assertEquals(1,a.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,a.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,a.z, GrlConstants.FLOAT_TEST_TOL);

		assertEquals(5,c.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(7,c.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(9,c.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void timesIP() {
		Dummy a = new Dummy(1,2,3);

		a.timesIP(2.5f);

		assertEquals(2.5f,a.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,a.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(7.5f,a.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void scale() {
		Dummy a = new Dummy(1,2,3);

		a.scale(2.5f);

		assertEquals(2.5f,a.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,a.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(7.5f,a.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void times() {
		Dummy a = new Dummy(1,2,3);

		Dummy b = a.times(2.5f);

		assertEquals(1,a.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,a.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,a.z, GrlConstants.FLOAT_TEST_TOL);

		assertEquals(2.5f,b.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,b.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(7.5f, b.z, GrlConstants.FLOAT_TEST_TOL);
	}

	public static class Dummy extends GeoTuple3D_F32<Dummy> {
		public Dummy() {
		}

		public Dummy(float x, float y, float z) {
			super(x, y, z);
		}

		@Override
		public Dummy createNewInstance() {
			return new Dummy();
		}
	}
}
