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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestBoxLength3D_F32 {

	@Test
	public void constructor_box() {
		BoxLength3D_F32 boxA = new BoxLength3D_F32(1,2,3,4,5,6);
		BoxLength3D_F32 box = new BoxLength3D_F32(boxA);

		assertEquals(1,box.p.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,box.p.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,box.p.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,box.lengthX, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,box.lengthY, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,box.lengthZ, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void constructor_floats() {
		BoxLength3D_F32 box = new BoxLength3D_F32(1,2,3,4,5,6);

		assertEquals(1,box.p.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,box.p.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,box.p.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,box.lengthX, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,box.lengthY, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6, box.lengthZ, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void set_box() {
		BoxLength3D_F32 box = new BoxLength3D_F32();
		box.set(1,2,3,4,5,6);

		assertEquals(1, box.p.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,box.p.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,box.p.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,box.lengthX, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,box.lengthY, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6, box.lengthZ, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void set_floats() {
		BoxLength3D_F32 boxA = new BoxLength3D_F32(1,2,3,4,5,6);
		BoxLength3D_F32 box = new BoxLength3D_F32();
		box.set(boxA);

		assertEquals(1,box.p.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,box.p.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,box.p.z, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,box.lengthX, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,box.lengthY, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6, box.lengthZ, GrlConstants.FLOAT_TEST_TOL);
	}

}
