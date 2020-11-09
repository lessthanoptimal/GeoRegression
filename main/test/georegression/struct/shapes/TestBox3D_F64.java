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

package georegression.struct.shapes;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestBox3D_F64 {

	@Test
	void constructor_box() {
		Box3D_F64 boxA = new Box3D_F64(1,2,3,4,5,6);
		Box3D_F64 box = new Box3D_F64(boxA);

		assertEquals(1,box.p0.x, GrlConstants.TEST_F64);
		assertEquals(2,box.p0.y, GrlConstants.TEST_F64);
		assertEquals(3,box.p0.z, GrlConstants.TEST_F64);
		assertEquals(4,box.p1.x, GrlConstants.TEST_F64);
		assertEquals(5,box.p1.y, GrlConstants.TEST_F64);
		assertEquals(6,box.p1.z, GrlConstants.TEST_F64);
	}

	@Test
	void constructor_floats() {
		Box3D_F64 box = new Box3D_F64(1,2,3,4,5,6);

		assertEquals(1,box.p0.x, GrlConstants.TEST_F64);
		assertEquals(2,box.p0.y, GrlConstants.TEST_F64);
		assertEquals(3,box.p0.z, GrlConstants.TEST_F64);
		assertEquals(4,box.p1.x, GrlConstants.TEST_F64);
		assertEquals(5,box.p1.y, GrlConstants.TEST_F64);
		assertEquals(6,box.p1.z, GrlConstants.TEST_F64);
	}

	@Test
	void set_box() {
		Box3D_F64 box = new Box3D_F64();
		box.setTo(1,2,3,4,5,6);

		assertEquals(1,box.p0.x, GrlConstants.TEST_F64);
		assertEquals(2,box.p0.y, GrlConstants.TEST_F64);
		assertEquals(3,box.p0.z, GrlConstants.TEST_F64);
		assertEquals(4,box.p1.x, GrlConstants.TEST_F64);
		assertEquals(5,box.p1.y, GrlConstants.TEST_F64);
		assertEquals(6,box.p1.z, GrlConstants.TEST_F64);
	}

	@Test
	void set_floats() {
		Box3D_F64 boxA = new Box3D_F64(1,2,3,4,5,6);
		Box3D_F64 box = new Box3D_F64();
		box.setTo(boxA);

		assertEquals(1,box.p0.x, GrlConstants.TEST_F64);
		assertEquals(2,box.p0.y, GrlConstants.TEST_F64);
		assertEquals(3,box.p0.z, GrlConstants.TEST_F64);
		assertEquals(4,box.p1.x, GrlConstants.TEST_F64);
		assertEquals(5,box.p1.y, GrlConstants.TEST_F64);
		assertEquals(6,box.p1.z, GrlConstants.TEST_F64);
	}

	@Test
	void area() {
		Box3D_F64 box = new Box3D_F64(1,2,3,4,5,6);

		double expected = 3*3*3;
		assertEquals(expected,box.area(),GrlConstants.TEST_F64);
	}

	@Test
	void getLengthX() {
		Box3D_F64 box = new Box3D_F64(1,2,3,4,6,8);

		assertEquals(3,box.getLengthX(),GrlConstants.TEST_F64);
	}

	@Test
	void getLengthY() {
		Box3D_F64 box = new Box3D_F64(1,2,3,4,6,8);

		assertEquals(4,box.getLengthY(),GrlConstants.TEST_F64);
	}

	@Test
	void getLengthZ() {
		Box3D_F64 box = new Box3D_F64(1,2,3,4,6,8);

		assertEquals(5,box.getLengthZ(),GrlConstants.TEST_F64);
	}

	@Test
	void center() {
		Point3D_F64 center = new Box3D_F64(1,2,3,4,5,6).center(null);

		assertEquals(2.5,center.x,GrlConstants.TEST_F64);
		assertEquals(3.5,center.y,GrlConstants.TEST_F64);
		assertEquals(4.5,center.z,GrlConstants.TEST_F64);
	}

}
