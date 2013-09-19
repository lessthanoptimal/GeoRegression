/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
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

		assertEquals(1,cube.p.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,cube.p.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,cube.p.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,cube.lengthX, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,cube.lengthY, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,cube.lengthZ, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void constructor_floats() {
		Cube3D_F32 cube = new Cube3D_F32(1,2,3,4,5,6);

		assertEquals(1,cube.p.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,cube.p.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,cube.p.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,cube.lengthX, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,cube.lengthY, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6, cube.lengthZ, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void set_cube() {
		Cube3D_F32 cube = new Cube3D_F32();
		cube.set(1,2,3,4,5,6);

		assertEquals(1, cube.p.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,cube.p.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,cube.p.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,cube.lengthX, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,cube.lengthY, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6, cube.lengthZ, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void set_floats() {
		Cube3D_F32 cubeA = new Cube3D_F32(1,2,3,4,5,6);
		Cube3D_F32 cube = new Cube3D_F32();
		cube.set(cubeA);

		assertEquals(1,cube.p.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,cube.p.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,cube.p.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,cube.lengthX, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,cube.lengthY, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6, cube.lengthZ, GrlConstants.FLOAT_TEST_TOL);
	}

}
