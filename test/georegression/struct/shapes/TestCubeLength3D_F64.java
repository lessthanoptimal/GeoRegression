/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.struct.shapes;

import georegression.misc.GrlConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestCubeLength3D_F64 {

	@Test
	public void constructor_cube() {
		CubeLength3D_F64 cubeA = new CubeLength3D_F64(1,2,3,4,5,6);
		CubeLength3D_F64 cube = new CubeLength3D_F64(cubeA);

		assertEquals(1,cube.p.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(2,cube.p.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(3,cube.p.z, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(4,cube.lengthX, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(5,cube.lengthY, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(6,cube.lengthZ, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void constructor_floats() {
		CubeLength3D_F64 cube = new CubeLength3D_F64(1,2,3,4,5,6);

		assertEquals(1,cube.p.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(2,cube.p.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(3,cube.p.z, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(4,cube.lengthX, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(5,cube.lengthY, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(6, cube.lengthZ, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void set_cube() {
		CubeLength3D_F64 cube = new CubeLength3D_F64();
		cube.set(1,2,3,4,5,6);

		assertEquals(1, cube.p.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(2,cube.p.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(3,cube.p.z, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(4,cube.lengthX, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(5,cube.lengthY, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(6, cube.lengthZ, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void set_floats() {
		CubeLength3D_F64 cubeA = new CubeLength3D_F64(1,2,3,4,5,6);
		CubeLength3D_F64 cube = new CubeLength3D_F64();
		cube.set(cubeA);

		assertEquals(1,cube.p.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(2,cube.p.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(3,cube.p.z, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(4,cube.lengthX, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(5,cube.lengthY, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(6, cube.lengthZ, GrlConstants.DOUBLE_TEST_TOL);
	}

}
