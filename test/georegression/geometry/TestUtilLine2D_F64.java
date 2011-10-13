/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.geometry;


import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LinePolar2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilLine2D_F64 {

	@Test
	public void convert_polar_parametric() {
		LinePolar2D_F64 polar = new LinePolar2D_F64();
		LineParametric2D_F64 para = new LineParametric2D_F64();

		polar.distance = 5;
		polar.angle = Math.PI/2;

		UtilLine2D_F64.convert(polar,para);

		assertEquals(para.p.x,0, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(para.p.y,5, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(Math.abs(para.slope.x),1, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(para.slope.y,0, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void convert_segment_parametric() {
		LineSegment2D_F64 segment = new LineSegment2D_F64();
		LineParametric2D_F64 para = new LineParametric2D_F64();

		segment.a.set(0,0);
		segment.b.set(5,0);

		UtilLine2D_F64.convert(segment,para);

		assertEquals(para.p.x,0, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(para.p.y,0, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(Math.abs(para.slope.x),5, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(para.slope.y,0, GrlConstants.DOUBLE_TEST_TOL);
	}
}
