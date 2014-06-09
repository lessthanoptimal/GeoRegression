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


import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LinePolar2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilLine2D_F32 {

	Random rand = new Random(234234);

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
		assertEquals(polar.distance,5*Math.sqrt(2), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(polar.angle,-Math.PI+Math.PI/4, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_BackAndForth_parametric_polar() {
		LineParametric2D_F32 para = new LineParametric2D_F32();
		LinePolar2D_F32 polar = new LinePolar2D_F32();
		LinePolar2D_F32 found = new LinePolar2D_F32();

		for (int i = 0; i < 100; i++) {
			polar.distance = (rand.nextFloat() - 0.5f) * 3;
			polar.angle = 2.0f*(rand.nextFloat() - 0.5f) * (float)Math.PI;

			UtilLine2D_F32.convert(polar, para);
			UtilLine2D_F32.convert(para, found);

			normalize(polar);
			normalize(found);

			assertEquals(polar.angle,found.angle,GrlConstants.FLOAT_TEST_TOL);
			assertEquals(polar.distance,found.distance,GrlConstants.FLOAT_TEST_TOL);
		}
	}

	private void normalize( LinePolar2D_F32 l ) {
		if( l.distance < 0 ) {
			l.distance = -l.distance;
			l.angle = (float) UtilAngle.bound(l.angle+Math.PI);
		}
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
