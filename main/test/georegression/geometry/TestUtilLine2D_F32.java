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


import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LinePolar2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;
import org.junit.Test;

import java.util.Random;

import static georegression.geometry.UtilLine2D_F32.acuteAngle;
import static georegression.geometry.UtilLine2D_F32.acuteAngleN;
import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilLine2D_F32 {

	Random rand = new Random(234234);

	@Test
	public void acuteAngle_general() {
		assertEquals(0, acuteAngle(new LineGeneral2D_F32(1, 0, 0.5f), new LineGeneral2D_F32(1, 0, 6)),
				GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0, acuteAngle(new LineGeneral2D_F32(0, 2, 0.5f), new LineGeneral2D_F32(0, 2, 6)),
				GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Math.PI, acuteAngle(new LineGeneral2D_F32(2, 2, 0.5f), new LineGeneral2D_F32(-3, -3, 6)),
				GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Math.PI/2, acuteAngle(new LineGeneral2D_F32(2, 2, 0.5f), new LineGeneral2D_F32(-4, 4, 6)),
				GrlConstants.FLOAT_TEST_TOL);

		// pathological cause with numerical round off.  acos( -1.000000000123f )
		float a = (float)Math.cos(Math.PI/4.0f);
		assertEquals(Math.PI, acuteAngle(new LineGeneral2D_F32(a, a, 0.5f), new LineGeneral2D_F32(-a, -a, 6)),
				GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void acuteAngleN_general() {

		float a = (float)Math.cos(Math.PI/4.0f);
		float tol = GrlConstants.FLOAT_TEST_TOL*10.0f;// float case needs more tolerance

		assertEquals(0, acuteAngleN(new LineGeneral2D_F32(1, 0, 0.5f), new LineGeneral2D_F32(1, 0, 6)),tol);
		assertEquals(0, acuteAngleN(new LineGeneral2D_F32(0, 1, 0.5f), new LineGeneral2D_F32(0, 1, 6)),tol);
		assertEquals(Math.PI, acuteAngleN(new LineGeneral2D_F32(a, a, 0.5f), new LineGeneral2D_F32(-a, -a, 6)),tol);
		assertEquals(Math.PI/2, acuteAngleN(new LineGeneral2D_F32(a, a, 0.5f), new LineGeneral2D_F32(-a, a, 6)),tol);
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
	public void convert_segment_general() {
		LineSegment2D_F32 segment = new LineSegment2D_F32();

		segment.a.set(0,2);
		segment.b.set(5,6);

		LineGeneral2D_F32 general = UtilLine2D_F32.convert(segment,(LineGeneral2D_F32)null);

		// see if the two end points lie on the general line
		assertEquals(0,general.evaluate(segment.a.x,segment.a.y), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0,general.evaluate(segment.b.x,segment.b.y), GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_segment2pt_general() {
		Point2D_F32 a = new Point2D_F32(2,6);
		Point2D_F32 b = new Point2D_F32(7,8);

		LineGeneral2D_F32 general = UtilLine2D_F32.convert(a,b,(LineGeneral2D_F32)null);

		// see if the two end points lie on the general line
		assertEquals(0,general.evaluate(a.x,a.y), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0,general.evaluate(b.x,b.y), GrlConstants.FLOAT_TEST_TOL);
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
	public void convert_polar_general() {
		LinePolar2D_F32 polar = new LinePolar2D_F32();

		polar.distance = 5;
		polar.angle = (float)Math.PI/3.0f;

		LineGeneral2D_F32 found = UtilLine2D_F32.convert(polar,(LineGeneral2D_F32)null);
		LineParametric2D_F32 para = UtilLine2D_F32.convert(polar,(LineParametric2D_F32)null);
		LineGeneral2D_F32 expected = UtilLine2D_F32.convert(para,(LineGeneral2D_F32)null);
		expected.normalize();
		// handle the sign ambiguity
		if( expected.A*found.A < 0 || expected.B*found.B < 0) {
			found.A *= -1;
			found.B *= -1;
			found.C *= -1;
		}

		assertEquals(expected.A,found.A,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.B,found.B,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.C,found.C,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_general_polar() {
		LineGeneral2D_F32 general = new LineGeneral2D_F32(2,-3,-5);
		LinePolar2D_F32 found = UtilLine2D_F32.convert(general,(LinePolar2D_F32)null);

		// find two points on the line using the polar equation
		float c = (float) (float)Math.cos(found.angle);
		float s = (float) (float)Math.sin(found.angle);

		float x0 = c*found.distance;
		float y0 = s*found.distance;

		float x1 = x0 - s;
		float y1 = y0 + c;

		// see if they are also on the general line equation
		assertEquals(0,general.evaluate(x0,y0), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0,general.evaluate(x1,y1), GrlConstants.FLOAT_TEST_TOL);
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

			assertEquals(polar.angle, found.angle, GrlConstants.FLOAT_TEST_TOL);
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
