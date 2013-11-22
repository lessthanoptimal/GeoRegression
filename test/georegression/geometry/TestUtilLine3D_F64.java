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
import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.line.LineSegment3D_F64;
import georegression.struct.point.Point3D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilLine3D_F64 {

	@Test
	public void convert_ls_lp() {
		LineSegment3D_F64 ls = new LineSegment3D_F64(1,2,3,6,8,10);

		LineParametric3D_F64 lp = UtilLine3D_F64.convert(ls,null);

		assertEquals(lp.p.x,1, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(lp.p.y,2, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(lp.p.z,3, GrlConstants.DOUBLE_TEST_TOL);

		assertEquals(lp.slope.x,5, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(lp.slope.y,6, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(lp.slope.z,7, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void computeT() {
		LineParametric3D_F64 line = new LineParametric3D_F64(1,2,3,-4,1.5,0.23);

		double t0 = -3.4;
		double t1 = 1.2;

		Point3D_F64 p0 = line.getPointOnLine(t0);
		Point3D_F64 p1 = line.getPointOnLine(t1);

		assertEquals(t0,UtilLine3D_F64.computeT(line,p0),GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(t1,UtilLine3D_F64.computeT(line,p1),GrlConstants.DOUBLE_TEST_TOL);
	}
}
