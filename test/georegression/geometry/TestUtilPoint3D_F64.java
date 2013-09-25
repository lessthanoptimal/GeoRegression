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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Cube3D_F64;
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
	public void boundingCube() {
		List<Point3D_F64> list = new ArrayList<Point3D_F64>();

		list.add( new Point3D_F64(1,1,1));
		list.add( new Point3D_F64(2,3,1));
		list.add( new Point3D_F64(1.5,2,5));

		Cube3D_F64 cube = new Cube3D_F64();
		UtilPoint3D_F64.boundingCube(list,cube);

		assertEquals(0,cube.getP0().distance(list.get(0)),1e-8);
		assertEquals(0,cube.getP1().distance(new Point3D_F64(2,3,5)),1e-8);
	}
}
