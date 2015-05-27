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

package georegression.struct.shapes;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestBox3D_F32 {

	@Test
	public void constructor_box() {
		Box3D_F32 boxA = new Box3D_F32(1,2,3,4,5,6);
		Box3D_F32 box = new Box3D_F32(boxA);

		assertEquals(1,box.p0.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,box.p0.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,box.p0.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,box.p1.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,box.p1.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,box.p1.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void constructor_floats() {
		Box3D_F32 box = new Box3D_F32(1,2,3,4,5,6);

		assertEquals(1,box.p0.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,box.p0.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,box.p0.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,box.p1.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,box.p1.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,box.p1.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void set_box() {
		Box3D_F32 box = new Box3D_F32();
		box.set(1,2,3,4,5,6);

		assertEquals(1,box.p0.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,box.p0.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,box.p0.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,box.p1.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,box.p1.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,box.p1.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void set_floats() {
		Box3D_F32 boxA = new Box3D_F32(1,2,3,4,5,6);
		Box3D_F32 box = new Box3D_F32();
		box.set(boxA);

		assertEquals(1,box.p0.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,box.p0.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,box.p0.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,box.p1.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,box.p1.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,box.p1.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void area() {
		Box3D_F32 box = new Box3D_F32(1,2,3,4,5,6);

		float expected = 3*3*3;
		assertEquals(expected,box.area(),GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void getLengthX() {
		Box3D_F32 box = new Box3D_F32(1,2,3,4,6,8);

		assertEquals(3,box.getLengthX(),GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void getLengthY() {
		Box3D_F32 box = new Box3D_F32(1,2,3,4,6,8);

		assertEquals(4,box.getLengthY(),GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void getLengthZ() {
		Box3D_F32 box = new Box3D_F32(1,2,3,4,6,8);

		assertEquals(5,box.getLengthZ(),GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void center() {
		Point3D_F32 center = new Box3D_F32(1,2,3,4,5,6).center(null);

		assertEquals(2.5f,center.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3.5f,center.y,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4.5f,center.z,GrlConstants.FLOAT_TEST_TOL);
	}

}
