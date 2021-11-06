/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LinePolar2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static georegression.geometry.UtilLine2D_F64.acuteAngle;
import static georegression.geometry.UtilLine2D_F64.acuteAngleN;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public class TestUtilLine2D_F64 {

	private final Random rand = new Random(234234);

	@Test void acuteAngle_general() {
		assertEquals(0, acuteAngle(new LineGeneral2D_F64(1, 0, 0.5), new LineGeneral2D_F64(1, 0, 6)),
				GrlConstants.TEST_F64);
		assertEquals(0, acuteAngle(new LineGeneral2D_F64(0, 2, 0.5), new LineGeneral2D_F64(0, 2, 6)),
				GrlConstants.TEST_F64);
		assertEquals(Math.PI, acuteAngle(new LineGeneral2D_F64(2, 2, 0.5), new LineGeneral2D_F64(-3, -3, 6)),
				GrlConstants.TEST_F64);
		assertEquals(Math.PI/2, acuteAngle(new LineGeneral2D_F64(2, 2, 0.5), new LineGeneral2D_F64(-4, 4, 6)),
				GrlConstants.TEST_F64);

		// pathological cause with numerical round off. acos( -1.000000000123 )
		double a = Math.cos(Math.PI/4.0);
		assertEquals(Math.PI, acuteAngle(new LineGeneral2D_F64(a, a, 0.5), new LineGeneral2D_F64(-a, -a, 6)),
				GrlConstants.TEST_F64);
	}

	@Test void acuteAngleN_general() {
		double a = Math.cos(Math.PI/4.0);
		double tol = GrlConstants.TEST_F64 *10.0;// float case needs more tolerance

		assertEquals(0, acuteAngleN(new LineGeneral2D_F64(1, 0, 0.5), new LineGeneral2D_F64(1, 0, 6)),tol);
		assertEquals(0, acuteAngleN(new LineGeneral2D_F64(0, 1, 0.5), new LineGeneral2D_F64(0, 1, 6)),tol);
		assertEquals(Math.PI, acuteAngleN(new LineGeneral2D_F64(a, a, 0.5), new LineGeneral2D_F64(-a, -a, 6)),tol);
		assertEquals(Math.PI/2, acuteAngleN(new LineGeneral2D_F64(a, a, 0.5), new LineGeneral2D_F64(-a, a, 6)),tol);
	}

	@Test void convert_segment_parametric() {
		LineSegment2D_F64 segment = new LineSegment2D_F64();
		LineParametric2D_F64 para = new LineParametric2D_F64();

		segment.a.setTo(1,2);
		segment.b.setTo(5,0);

		UtilLine2D_F64.convert(segment,para);

		assertEquals(para.p.x,1, GrlConstants.TEST_F64);
		assertEquals(para.p.y,2, GrlConstants.TEST_F64);
		assertEquals(para.slope.x,4, GrlConstants.TEST_F64);
		assertEquals(para.slope.y,-2, GrlConstants.TEST_F64);
	}

	@Test void convert_segment_general() {
		LineSegment2D_F64 segment = new LineSegment2D_F64();

		segment.a.setTo(0,2);
		segment.b.setTo(5,6);

		LineGeneral2D_F64 general = UtilLine2D_F64.convert(segment,(LineGeneral2D_F64)null);

		// see if the two end points lie on the general line
		assertEquals(0,general.evaluate(segment.a.x,segment.a.y), GrlConstants.TEST_F64);
		assertEquals(0,general.evaluate(segment.b.x,segment.b.y), GrlConstants.TEST_F64);
	}

	@Test void convert_segment2pt_general() {
		Point2D_F64 a = new Point2D_F64(2,6);
		Point2D_F64 b = new Point2D_F64(7,8);

		LineGeneral2D_F64 general = UtilLine2D_F64.convert(a,b,(LineGeneral2D_F64)null);

		// see if the two end points lie on the general line
		assertEquals(0,general.evaluate(a.x,a.y), GrlConstants.TEST_F64);
		assertEquals(0,general.evaluate(b.x,b.y), GrlConstants.TEST_F64);
	}

	@Test void convert_segment2pt_parametric() {
		Point2D_F64 a = new Point2D_F64(2,6);
		Point2D_F64 b = new Point2D_F64(7,8);

		LineParametric2D_F64 para = UtilLine2D_F64.convert(a,b,(LineParametric2D_F64) null);

		assertEquals(para.p.x,2, GrlConstants.TEST_F64);
		assertEquals(para.p.y,6, GrlConstants.TEST_F64);
		assertEquals(para.slope.x,5, GrlConstants.TEST_F64);
		assertEquals(para.slope.y,2, GrlConstants.TEST_F64);
	}

	@Test void convert_pt_angle_parametric() {
		Point2D_F64 a = new Point2D_F64(2,6);
		double angle = Math.PI;

		LineParametric2D_F64 para = UtilLine2D_F64.convert(a,angle,(LineParametric2D_F64) null);

		assertEquals(para.p.x,2, GrlConstants.TEST_F64);
		assertEquals(para.p.y,6, GrlConstants.TEST_F64);
		assertEquals(para.slope.x,-1, GrlConstants.TEST_F64);
		assertEquals(para.slope.y,0, GrlConstants.TEST_F64);
	}

	@Test void convert_polar_parametric() {
		LinePolar2D_F64 polar = new LinePolar2D_F64();
		LineParametric2D_F64 para = new LineParametric2D_F64();

		polar.distance = 5;
		polar.angle = Math.PI/2;

		UtilLine2D_F64.convert(polar,para);

		assertEquals(para.p.x,0, GrlConstants.TEST_F64);
		assertEquals(para.p.y,5, GrlConstants.TEST_F64);
		assertEquals(Math.abs(para.slope.x),1, GrlConstants.TEST_F64);
		assertEquals(para.slope.y,0, GrlConstants.TEST_F64);
	}

	@Test void convert_polar_general() {
		LinePolar2D_F64 polar = new LinePolar2D_F64();

		polar.distance = 5;
		polar.angle = Math.PI/3.0;

		LineGeneral2D_F64 found = UtilLine2D_F64.convert(polar,(LineGeneral2D_F64)null);
		LineParametric2D_F64 para = UtilLine2D_F64.convert(polar,(LineParametric2D_F64)null);
		LineGeneral2D_F64 expected = UtilLine2D_F64.convert(para,(LineGeneral2D_F64)null);
		expected.normalize();
		// handle the sign ambiguity
		if( expected.A*found.A < 0 || expected.B*found.B < 0) {
			found.A *= -1;
			found.B *= -1;
			found.C *= -1;
		}

		assertEquals(expected.A,found.A,GrlConstants.TEST_F64);
		assertEquals(expected.B,found.B,GrlConstants.TEST_F64);
		assertEquals(expected.C,found.C,GrlConstants.TEST_F64);
	}

	@Test void convert_general_polar() {
		LineGeneral2D_F64 general = new LineGeneral2D_F64(2,-3,-5);
		LinePolar2D_F64 found = UtilLine2D_F64.convert(general,(LinePolar2D_F64)null);

		// find two points on the line using the polar equation
		double c = (double) Math.cos(found.angle);
		double s = (double) Math.sin(found.angle);

		double x0 = c*found.distance;
		double y0 = s*found.distance;

		double x1 = x0 - s;
		double y1 = y0 + c;

		// see if they are also on the general line equation
		assertEquals(0,general.evaluate(x0,y0), GrlConstants.TEST_F64);
		assertEquals(0,general.evaluate(x1,y1), GrlConstants.TEST_F64);
	}

	@Test void convert_parametric_polar() {
		LineParametric2D_F64 para = new LineParametric2D_F64();
		LinePolar2D_F64 polar = new LinePolar2D_F64();

		para.slope.setTo(1,0);
		para.setPoint(0,5);
		UtilLine2D_F64.convert(para,polar);
		assertEquals(polar.distance,5, GrlConstants.TEST_F64);
		assertEquals(polar.angle,Math.PI/2, GrlConstants.TEST_F64);

		para.slope.setTo(1,-1);
		para.setPoint(-5,-5);
		UtilLine2D_F64.convert(para,polar);
		assertEquals(polar.distance,5*Math.sqrt(2), GrlConstants.TEST_F64);
		assertEquals(polar.angle,-Math.PI+Math.PI/4, GrlConstants.TEST_F64);
	}

	@Test void convert_BackAndForth_parametric_polar() {
		LineParametric2D_F64 para = new LineParametric2D_F64();
		LinePolar2D_F64 polar = new LinePolar2D_F64();
		LinePolar2D_F64 found = new LinePolar2D_F64();

		for (int i = 0; i < 100; i++) {
			polar.distance = (rand.nextDouble() - 0.5) * 3;
			polar.angle = 2.0*(rand.nextDouble() - 0.5) * Math.PI;

			UtilLine2D_F64.convert(polar, para);
			UtilLine2D_F64.convert(para, found);

			normalize(polar);
			normalize(found);

			assertEquals(polar.angle, found.angle, GrlConstants.TEST_F64);
			assertEquals(polar.distance,found.distance,GrlConstants.TEST_F64);
		}
	}

	private void normalize( LinePolar2D_F64 l ) {
		if( l.distance < 0 ) {
			l.distance = -l.distance;
			l.angle = UtilAngle.bound(l.angle + Math.PI);
		}
	}

	@Test void convert_parametric_general() {
		LineParametric2D_F64 para = new LineParametric2D_F64();
		LineGeneral2D_F64 general = new LineGeneral2D_F64();

		para.slope.setTo(1,0.5);
		para.p.setTo(0.75,0.34);

		// pick a point on the line
		Point2D_F64 p = new Point2D_F64(para.p.x + para.slope.x*2,para.p.y + para.slope.y*2);

		// convert to general notation
		UtilLine2D_F64.convert(para,general);

		// test the basic properties of this line equation
		double val = general.A*p.x + general.B*p.y + general.C;

		assertEquals(0,val, GrlConstants.TEST_F64);
	}

	@Test void convert_general_parametric() {
		LineGeneral2D_F64 general = new LineGeneral2D_F64();
		LineParametric2D_F64 para = new LineParametric2D_F64();

		// pick some arbitrary line
		general.setTo(1,2,3);

		// convert to parametric notation
		UtilLine2D_F64.convert(general,para);

		// pick a point on the parametric line
		Point2D_F64 p = new Point2D_F64(para.p.x + para.slope.x*2,para.p.y + para.slope.y*2);

		// See if that same point is on the general equation
		double val = general.A*p.x + general.B*p.y + general.C;

		assertEquals(0,val, GrlConstants.TEST_F64);
	}

	@Test void area2() {
		assertEquals( 2.0*4.0, UtilLine2D_F64.area2(cr(1,1), cr(3,1), cr(3,5)), UtilEjml.TEST_F64);
		assertEquals(-2.0*4.0, UtilLine2D_F64.area2(cr(3,5), cr(3,1), cr(1,1)), UtilEjml.TEST_F64);
	}

	@Test void isColinear() {
		assertFalse(UtilLine2D_F64.isColinear(cr(1,1), cr(3,1), cr(3,5), UtilEjml.TEST_F64));
		assertFalse(UtilLine2D_F64.isColinear(cr(1,1), cr(3,1), cr(-3,-5), UtilEjml.TEST_F64));
		assertTrue(UtilLine2D_F64.isColinear(cr(1,1), cr(2,1), cr(5,1), UtilEjml.TEST_F64));
		assertTrue(UtilLine2D_F64.isColinear(cr(1,1), cr(1,2), cr(1,5), UtilEjml.TEST_F64));

		// Test the tolerance
		assertTrue(UtilLine2D_F64.isColinear(cr(1,1), cr(2,1), cr(5,1.0999), 0.1));
		assertFalse(UtilLine2D_F64.isColinear(cr(1,1), cr(2,1), cr(5,1.10001), 0.1));
	}

	@Test void isBetweenColinear() {
		assertFalse(UtilLine2D_F64.isBetweenColinear(cr(1,1), cr(2,1), cr(5,1)));
		assertTrue(UtilLine2D_F64.isBetweenColinear(cr(1,1), cr(2,1), cr(1.5,1)));
		assertFalse(UtilLine2D_F64.isBetweenColinear(cr(1,1), cr(1,2), cr(1,5)));
		assertTrue(UtilLine2D_F64.isBetweenColinear(cr(1,1), cr(1,2), cr(1,1.5)));

		assertTrue(UtilLine2D_F64.isBetweenColinear(cr(1,1), cr(1,2), cr(1,1)));
		assertTrue(UtilLine2D_F64.isBetweenColinear(cr(1,1), cr(1,2), cr(1,2)));
	}

	@Test void isBetweenColinearExclusive() {
		assertFalse(UtilLine2D_F64.isBetweenColinearExclusive(cr(1, 1), cr(2, 1), cr(5, 1)));
		assertTrue(UtilLine2D_F64.isBetweenColinearExclusive(cr(1, 1), cr(2, 1), cr(1.5, 1)));
		assertFalse(UtilLine2D_F64.isBetweenColinearExclusive(cr(1, 1), cr(1, 2), cr(1, 5)));
		assertTrue(UtilLine2D_F64.isBetweenColinearExclusive(cr(1, 1), cr(1, 2), cr(1, 1.5)));

		assertFalse(UtilLine2D_F64.isBetweenColinearExclusive(cr(1,1), cr(1,2), cr(1,1)));
		assertFalse(UtilLine2D_F64.isBetweenColinearExclusive(cr(1,1), cr(1,2), cr(1,2)));
	}

		/** short hand */
	private static Point2D_F64 cr(double x , double y) {
		return new Point2D_F64(x,y);
	}
}
