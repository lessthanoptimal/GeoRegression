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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Box3D_F64;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilPoint3D_F64 {
	@Test
	public void distance() {
		double found = UtilPoint3D_F64.distance(1,2,3,4,-3,-4);
		assertEquals(9.1104,found,1e-3);
	}

	@Test
	public void distanceSq() {
		double found = UtilPoint3D_F64.distanceSq(1, 2, 3, 4, -3, -4);
		assertEquals(83, found, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void boundingBox() {
		List<Point3D_F64> list = new ArrayList<Point3D_F64>();

		list.add( new Point3D_F64(1,1,1));
		list.add( new Point3D_F64(2,3,1));
		list.add( new Point3D_F64(1.5,2,5));

		Box3D_F64 cube = new Box3D_F64();
		UtilPoint3D_F64.boundingBox(list, cube);

		assertEquals(0,cube.getP0().distance(list.get(0)),GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0,cube.getP1().distance(new Point3D_F64(2,3,5)),1e-8);
	}
}
