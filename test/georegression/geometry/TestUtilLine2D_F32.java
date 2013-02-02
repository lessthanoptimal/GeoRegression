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
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LinePolar2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Peter Abeles
 */
public class TestUtilLine2D_F32 {

	@Test
	public void convert_polar_parametric() {
		LinePolar2D_F32 polar = new LinePolar2D_F32();
		LineParametric2D_F32 para = new LineParametric2D_F32();

		polar.distance = 5;
		polar.angle = (float)Math.PI/2;

		UtilLine2D_F32.convert(polar,para);

		assertEquals(para.p.x,0, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(para.p.y,5, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Math.abs(para.slope.x),1, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(para.slope.y,0, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_segment_parametric() {
		LineSegment2D_F32 segment = new LineSegment2D_F32();
		LineParametric2D_F32 para = new LineParametric2D_F32();

		segment.a.set(0,0);
		segment.b.set(5,0);

		UtilLine2D_F32.convert(segment,para);

		assertEquals(para.p.x,0, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(para.p.y,0, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Math.abs(para.slope.x),5, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(para.slope.y,0, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_parametric_polar() {
		LineParametric2D_F32 para = new LineParametric2D_F32();
		LinePolar2D_F32 polar = new LinePolar2D_F32();

		para.slope.set(1,0);
		para.setPoint(0,5);
		UtilLine2D_F32.convert(para,polar);
		assertEquals(polar.distance,5, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(polar.angle,Math.PI/2, GrlConstants.FLOAT_TEST_TOL);

		para.slope.set(1,-1);
		para.setPoint(-5,-5);
		UtilLine2D_F32.convert(para,polar);
		assertEquals(polar.distance,-5*Math.sqrt(2), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(polar.angle,Math.PI/4, GrlConstants.FLOAT_TEST_TOL);

		fail("Don't think these tests are sufficient.  when it decides to go negative is probably not correct");
	}

	@Test
	public void convert_parametric_general() {
		LineParametric2D_F32 para = new LineParametric2D_F32();
		LineGeneral2D_F32 general = new LineGeneral2D_F32();

		para.slope.set(1,0.5f);
		para.p.set(0.75f,0.34f);

		// pick a point on the line
		Point2D_F32 p = new Point2D_F32(para.p.x + para.slope.x*2,para.p.y + para.slope.y*2);

		// convert to general notation
		UtilLine2D_F32.convert(para,general);

		// test the basic properties of this line equation
		float val = general.A*p.x + general.B*p.y + general.C;

		assertEquals(0,val,1e-8);
	}

	@Test
	public void convert_general_parametric() {
		LineGeneral2D_F32 general = new LineGeneral2D_F32();
		LineParametric2D_F32 para = new LineParametric2D_F32();

		// pick some arbitrary line
		general.set(1,2,3);

		// convert to parametric notation
		UtilLine2D_F32.convert(general,para);

		// pick a point on the parametric line
		Point2D_F32 p = new Point2D_F32(para.p.x + para.slope.x*2,para.p.y + para.slope.y*2);

		// See if that same point is on the general equation
		float val = general.A*p.x + general.B*p.y + general.C;

		assertEquals(0,val,1e-8);
	}
}
