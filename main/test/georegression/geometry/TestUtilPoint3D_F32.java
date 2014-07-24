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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F32;
import georegression.struct.shapes.Box3D_F32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilPoint3D_F32 {
	@Test
	public void distance() {
		float found = UtilPoint3D_F32.distance(1,2,3,4,-3,-4);
		assertEquals(9.1104f,found,1e-3);
	}

	@Test
	public void distanceSq() {
		float found = UtilPoint3D_F32.distanceSq(1, 2, 3, 4, -3, -4);
		assertEquals(83, found, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void boundingBox() {
		List<Point3D_F32> list = new ArrayList<Point3D_F32>();

		list.add( new Point3D_F32(1,1,1));
		list.add( new Point3D_F32(2,3,1));
		list.add( new Point3D_F32(1.5f,2,5));

		Box3D_F32 cube = new Box3D_F32();
		UtilPoint3D_F32.boundingBox(list, cube);

		assertEquals(0,cube.getP0().distance(list.get(0)),1e-8);
		assertEquals(0,cube.getP1().distance(new Point3D_F32(2,3,5)),1e-8);
	}
}
