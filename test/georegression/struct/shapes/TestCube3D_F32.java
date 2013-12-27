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
public class TestCube3D_F32 {

	@Test
	public void constructor_cube() {
		Cube3D_F32 cubeA = new Cube3D_F32(1,2,3,4,5,6);
		Cube3D_F32 cube = new Cube3D_F32(cubeA);

		assertEquals(1,cube.p0.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,cube.p0.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,cube.p0.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,cube.p1.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,cube.p1.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,cube.p1.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void constructor_floats() {
		Cube3D_F32 cube = new Cube3D_F32(1,2,3,4,5,6);

		assertEquals(1,cube.p0.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,cube.p0.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,cube.p0.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,cube.p1.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,cube.p1.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,cube.p1.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void set_cube() {
		Cube3D_F32 cube = new Cube3D_F32();
		cube.set(1,2,3,4,5,6);

		assertEquals(1,cube.p0.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,cube.p0.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,cube.p0.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,cube.p1.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,cube.p1.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,cube.p1.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void set_floats() {
		Cube3D_F32 cubeA = new Cube3D_F32(1,2,3,4,5,6);
		Cube3D_F32 cube = new Cube3D_F32();
		cube.set(cubeA);

		assertEquals(1,cube.p0.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,cube.p0.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,cube.p0.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,cube.p1.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,cube.p1.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,cube.p1.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void area() {
		Cube3D_F32 cube = new Cube3D_F32(1,2,3,4,5,6);

		float expected = 3*3*3;
		assertEquals(expected,cube.area(),GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void getLengthX() {
		Cube3D_F32 cube = new Cube3D_F32(1,2,3,4,6,8);

		assertEquals(3,cube.getLengthX(),GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void getLengthY() {
		Cube3D_F32 cube = new Cube3D_F32(1,2,3,4,6,8);

		assertEquals(4,cube.getLengthY(),GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void getLengthZ() {
		Cube3D_F32 cube = new Cube3D_F32(1,2,3,4,6,8);

		assertEquals(5,cube.getLengthZ(),GrlConstants.FLOAT_TEST_TOL);
	}

}
