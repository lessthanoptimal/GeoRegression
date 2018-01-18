/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestBoxLength3D_F32 {

	@Test
	public void constructor_box() {
		BoxLength3D_F32 boxA = new BoxLength3D_F32(1,2,3,4,5,6);
		BoxLength3D_F32 box = new BoxLength3D_F32(boxA);

		assertEquals(1,box.p.x, GrlConstants.TEST_F32);
		assertEquals(2,box.p.y, GrlConstants.TEST_F32);
		assertEquals(3,box.p.z, GrlConstants.TEST_F32);
		assertEquals(4,box.lengthX, GrlConstants.TEST_F32);
		assertEquals(5,box.lengthY, GrlConstants.TEST_F32);
		assertEquals(6,box.lengthZ, GrlConstants.TEST_F32);
	}

	@Test
	public void constructor_floats() {
		BoxLength3D_F32 box = new BoxLength3D_F32(1,2,3,4,5,6);

		assertEquals(1,box.p.x, GrlConstants.TEST_F32);
		assertEquals(2,box.p.y, GrlConstants.TEST_F32);
		assertEquals(3,box.p.z, GrlConstants.TEST_F32);
		assertEquals(4,box.lengthX, GrlConstants.TEST_F32);
		assertEquals(5,box.lengthY, GrlConstants.TEST_F32);
		assertEquals(6, box.lengthZ, GrlConstants.TEST_F32);
	}

	@Test
	public void set_box() {
		BoxLength3D_F32 box = new BoxLength3D_F32();
		box.set(1,2,3,4,5,6);

		assertEquals(1, box.p.x, GrlConstants.TEST_F32);
		assertEquals(2,box.p.y, GrlConstants.TEST_F32);
		assertEquals(3,box.p.z, GrlConstants.TEST_F32);
		assertEquals(4,box.lengthX, GrlConstants.TEST_F32);
		assertEquals(5,box.lengthY, GrlConstants.TEST_F32);
		assertEquals(6, box.lengthZ, GrlConstants.TEST_F32);
	}

	@Test
	public void set_floats() {
		BoxLength3D_F32 boxA = new BoxLength3D_F32(1,2,3,4,5,6);
		BoxLength3D_F32 box = new BoxLength3D_F32();
		box.set(boxA);

		assertEquals(1,box.p.x, GrlConstants.TEST_F32);
		assertEquals(2,box.p.y, GrlConstants.TEST_F32);
		assertEquals(3,box.p.z, GrlConstants.TEST_F32);
		assertEquals(4,box.lengthX, GrlConstants.TEST_F32);
		assertEquals(5,box.lengthY, GrlConstants.TEST_F32);
		assertEquals(6, box.lengthZ, GrlConstants.TEST_F32);
	}

	@Test
	public void getCorner() {
		BoxLength3D_F32 boxA = new BoxLength3D_F32(1,2,3,4,5,6);

		List<Point3D_F32> expected = new ArrayList<>();
		expected.add( new Point3D_F32(1,2,3));
		expected.add( new Point3D_F32(1+4,2,3));
		expected.add( new Point3D_F32(1,2+5,3));
		expected.add( new Point3D_F32(1,2,3+6));
		expected.add( new Point3D_F32(1+4,2+5,3));
		expected.add( new Point3D_F32(1+4,2,3+6));
		expected.add( new Point3D_F32(1+4,2+5,3+6));
		expected.add( new Point3D_F32(1,2+5,3+6));

		for( Point3D_F32 e : expected ) {
			boolean matched = false;
			for (int i = 0; i < 8; i++) {
				if( e.distance(boxA.getCorner(i,null)) <= GrlConstants.TEST_F32 ) {
					matched = true;
					break;
				}
			}
			assertTrue(matched);
		}
	}

}
