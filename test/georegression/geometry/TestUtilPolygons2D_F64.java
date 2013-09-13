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
import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.RectangleCorner2D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilPolygons2D_F64 {

	@Test
	public void convert_rectcorner_quad() {
		RectangleCorner2D_F64 r = new RectangleCorner2D_F64(1,2,5,6);
		Quadrilateral_F64 q = new Quadrilateral_F64();

		UtilPolygons2D_F64.convert(r,q);

		assertEquals( 1,q.a.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 2,q.a.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 5,q.b.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 2,q.b.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 5,q.c.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 6,q.c.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 1,q.d.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 6,q.d.y, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void bounding_quadrilateral() {
		Quadrilateral_F64 q = new Quadrilateral_F64(3,0,2,-3,-2,3,1,5);
		RectangleCorner2D_F64 out = new RectangleCorner2D_F64();

		UtilPolygons2D_F64.bounding(q,out);

		assertEquals(-2,out.x0, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(-3,out.y0, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 3,out.x1, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 5,out.y1, GrlConstants.DOUBLE_TEST_TOL);
	}
}
