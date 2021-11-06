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

import georegression.geometry.curves.TangentLinesTwoEllipses_F64;
import georegression.geometry.curves.TestTangentLinesTwoEllipses_F64;
import georegression.metric.Intersection2D_F64;
import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.struct.curve.EllipseQuadratic_F64;
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public class TestUtilEllipse_F64 {

	Random rand = new Random(234);

	@Test
	void convert_back_forth() {
		convert_back_forth(0,0,4.5,3,0);
		convert_back_forth(1,2,4.5,3,0);
		convert_back_forth(0,0,4.5,3,(double)Math.PI/4);
		convert_back_forth(0,0,4.5,3,0.1);
		convert_back_forth(-2,1.5,4.5,3,-0.1);

		convert_back_forth(1,2,3,1.5,0);
		convert_back_forth(1,2,3,1.5,1.5);

		// see if it can handle a circle
		convert_back_forth(0,0,3,3,0);
	}

	@Test
	void convert_back_forth_random() {

		for( int i = 0; i < 100; i++ ) {
			double x = (rand.nextDouble()-0.5)*2;
			double y = (rand.nextDouble()-0.5)*2;
			double theta = (rand.nextDouble()-0.5)*(double)Math.PI;
			double b = rand.nextDouble()*2+0.1;
			double a = b+rand.nextDouble();

			convert_back_forth(x,y,a,b,theta);
		}
	}

	public void convert_back_forth( double x0 , double y0, double a, double b, double phi ) {
		// should be scale invariant
		convert_back_forth(x0,y0,a,b,phi,1);
		convert_back_forth(x0,y0,a,b,phi,-1);
	}

	public void convert_back_forth( double x0 , double y0, double a, double b, double phi , double scale ) {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(x0,y0,a,b,phi);
		EllipseQuadratic_F64 quad = new EllipseQuadratic_F64();
		EllipseRotated_F64 found = new EllipseRotated_F64();

		UtilEllipse_F64.convert(rotated,quad);

		quad.A *= scale;
		quad.B *= scale;
		quad.C *= scale;
		quad.D *= scale;
		quad.E *= scale;
		quad.F *= scale;

		UtilEllipse_F64.convert(quad,found);

		assertEquals(rotated.center.x,found.center.x, GrlConstants.TEST_F64);
		assertEquals(rotated.center.y,found.center.y, GrlConstants.TEST_F64);
		assertEquals(rotated.a,found.a, GrlConstants.TEST_F64);
		assertEquals(rotated.b,found.b, GrlConstants.TEST_F64);
		assertEquals(rotated.phi,found.phi, GrlConstants.TEST_F64);
	}

	@Test
	void convert_rotated_to_quad() {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(1,2,4.5,3,0.2);

		Point2D_F64 p = UtilEllipse_F64.computePoint(0.45,rotated,null);

		double eval = UtilEllipse_F64.evaluate(p.x,p.y,rotated);
		assertEquals(1,eval, GrlConstants.TEST_F64);

		EllipseQuadratic_F64 quad = new EllipseQuadratic_F64();
		UtilEllipse_F64.convert(rotated,quad);
		eval = UtilEllipse_F64.evaluate(p.x,p.y,quad);
		assertEquals(0,eval, GrlConstants.TEST_F64);
	}

	/**
	 * Tests computePoint and evaluate(rotated) by computes points around the ellipse and seeing if they
	 * meet the expected results.
	 */
	@Test
	void computePoint_evaluate_rotated() {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(1,2,4.5,3,0.2);

		for( int i = 0; i < 100; i++ ) {
			double t = Math.PI*2*i/100.0;
			Point2D_F64 p = UtilEllipse_F64.computePoint(t,rotated,null);
			double eval = UtilEllipse_F64.evaluate(p.x,p.y,rotated);
			assertEquals(1,eval, GrlConstants.TEST_F64);
		}
	}

	@Test
	void computePoint_evaluate_quadratic() {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(1,2,4.5,3,0.2);
		EllipseQuadratic_F64 quad = new EllipseQuadratic_F64();
		UtilEllipse_F64.convert(rotated,quad);

		for( int i = 0; i < 100; i++ ) {
			double t = GrlConstants.PI*2*i/100.0;
			Point2D_F64 p = UtilEllipse_F64.computePoint(t,rotated,null);
			double eval = UtilEllipse_F64.evaluate(p.x,p.y,quad);
			assertEquals(0,eval, GrlConstants.TEST_F64);
		}
	}

	/**
	 * Try a few simple cases
	 */
	@Test
	void computePoint_rotated() {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(1,2,3,2,GrlConstants.PId2);

		Point2D_F64 p = UtilEllipse_F64.computePoint(0,rotated,null);
		assertEquals(1.0,p.x, GrlConstants.TEST_F64);
		assertEquals(2+3,p.y, GrlConstants.TEST_F64);

		p = UtilEllipse_F64.computePoint(GrlConstants.PId2,rotated,null);
		assertEquals(-1.0,p.x, GrlConstants.TEST_F64);
		assertEquals( 2.0,p.y, GrlConstants.TEST_F64);
	}

	@Test
	void computeAngle() {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(1,2,4.5,3,0.2);

		for( int i = 0; i <= 100; i++ ) {
			double t = GrlConstants.PI*2*i/100.0 - GrlConstants.PI;
			Point2D_F64 p = UtilEllipse_F64.computePoint(t,rotated,null);
			double found = UtilEllipse_F64.computeAngle(p,rotated);
//			System.out.println(t+" "+found);
			assertTrue(UtilAngle.dist(t, found) <= GrlConstants.TEST_F64);
		}
	}


	@Test
	void computeTangent_rotated() {
		double delta = GrlConstants.TEST_F64;

		// axis aligned case
		EllipseRotated_F64 rotated = new EllipseRotated_F64(1,2,4.5,3,0);

		for (int i = 0; i < 20; i++) {
			double theta = i*GrlConstants.PI*2.0/20.0;
			checkTangent(theta,rotated,delta);
		}

		// rotated case
		rotated = new EllipseRotated_F64(1,2,4.5,3,0.4);

		for (int i = 0; i < 20; i++) {
			double theta = i*GrlConstants.PI*2.0/20.0;
			checkTangent(theta,rotated,delta);
		}
	}

	private void checkTangent( double t , EllipseRotated_F64 ellipse , double delta ) {
		Vector2D_F64 found = UtilEllipse_F64.computeTangent(t,ellipse,null);
		Vector2D_F64 expected = numericalTangent(t,ellipse,delta);

		double error0 = found.distance(expected);
		expected.x *= -1;
		expected.y *= -1;
		double error1 = found.distance(expected);

		double error = Math.min(error0,error1);

		assertEquals(0,error,Math.sqrt(delta));
	}

	private Vector2D_F64 numericalTangent( double t , EllipseRotated_F64 ellipse , double delta ) {

		Point2D_F64 a = UtilEllipse_F64.computePoint(t-delta,ellipse,null);
		Point2D_F64 b = UtilEllipse_F64.computePoint(t+delta,ellipse,null);

		Vector2D_F64 output = new Vector2D_F64();
		output.x = (b.x-a.x)/(2.0*delta);
		output.y = (b.y-a.y)/(2.0*delta);

		output.normalize();

		return output;
	}

	@Test
	void tangentLines_point_ellipse() {

		// simple case with a circle at the origin
		checkTangentLines( -2,2, new EllipseRotated_F64(0,0,2,2,0));
		checkTangentLines( 0,-10, new EllipseRotated_F64(0,0,2,2,0));
		checkTangentLines( -10,0, new EllipseRotated_F64(0,0,2,2,0));
		checkTangentLines( -10,1, new EllipseRotated_F64(0,0,2,2,0));
		checkTangentLines( 1,-10, new EllipseRotated_F64(0,0,2,2,0));

		// test failure case. Inside
		checkTangentLinesFail( 0,0, new EllipseRotated_F64(0,0,2,2,0));
		checkTangentLinesFail( 0.05,0, new EllipseRotated_F64(0,0,2,2,0));
		checkTangentLinesFail( 0,0.1, new EllipseRotated_F64(0,0,2,2,0));
		checkTangentLinesFail( 0.05,0.1, new EllipseRotated_F64(0,0,2,2,0));

		// same, but not circular
		checkTangentLines( -2,2, new EllipseRotated_F64(0,0,2,3,0));
		checkTangentLines( 0,-10, new EllipseRotated_F64(0,0,2,3,0));
		checkTangentLines( -10,0, new EllipseRotated_F64(0,0,2,3,0));
		checkTangentLines( -10,1, new EllipseRotated_F64(0,0,2,3,0));
		checkTangentLines( 1,-10, new EllipseRotated_F64(0,0,2,3,0));

		// same, but translated
		double x = 1.2, y = -0.5;
		checkTangentLines( -2+x , 2  +y, new EllipseRotated_F64(x,y,2,3,0));
		checkTangentLines(  0+x , -10+y, new EllipseRotated_F64(x,y,2,3,0));
		checkTangentLines( -10+x, 0  +y, new EllipseRotated_F64(x,y,2,3,0));
		checkTangentLines( -10+x, 1  +y, new EllipseRotated_F64(x,y,2,3,0));
		checkTangentLines(  1+x , -10+y, new EllipseRotated_F64(x,y,2,3,0));

		// same, but translated and rotated
		checkTangentLines( -3+x , 3  +y, new EllipseRotated_F64(x,y,2,3,0.1));
		checkTangentLines(  0+x , -10+y, new EllipseRotated_F64(x,y,2,3,0.1));
		checkTangentLines( -10+x, 0  +y, new EllipseRotated_F64(x,y,2,3,0.1));
		checkTangentLines( -10+x, 1  +y, new EllipseRotated_F64(x,y,2,3,0.1));
		checkTangentLines(  1+x , -10+y, new EllipseRotated_F64(x,y,2,3,0.1));
	}

	public void checkTangentLinesFail( double x, double y , EllipseRotated_F64 ellipse ) {
		Point2D_F64 pt = new Point2D_F64(x,y);

		Point2D_F64 pointA = new Point2D_F64();
		Point2D_F64 pointB = new Point2D_F64();

		assertFalse(UtilEllipse_F64.tangentLines(pt,ellipse,pointA,pointB));
	}

	public void checkTangentLines( double x, double y , EllipseRotated_F64 ellipse ) {
		Point2D_F64 pt = new Point2D_F64(x,y);

		Point2D_F64 pointA = new Point2D_F64();
		Point2D_F64 pointB = new Point2D_F64();

		assertTrue(UtilEllipse_F64.tangentLines(pt,ellipse,pointA,pointB));

		LineGeneral2D_F64 lineA = UtilLine2D_F64.convert(pt,pointA,(LineGeneral2D_F64)null);
		LineGeneral2D_F64 lineB = UtilLine2D_F64.convert(pt,pointB,(LineGeneral2D_F64)null);

		// the point should pass through both lines
		assertEquals(0, lineA.evaluate(pt.x,pt.y), GrlConstants.TEST_F64);
		assertEquals(0, lineB.evaluate(pt.x,pt.y), GrlConstants.TEST_F64);

		// if it's tangent there should only be one point of intersection
		Point2D_F64 pA = new Point2D_F64();
		Point2D_F64 pB = new Point2D_F64();

		assertTrue( 0 < Intersection2D_F64.intersection(lineA,ellipse,pA,pB, GrlConstants.TEST_F64));
		assertEquals(0,pA.distance(pB) , Math.sqrt(GrlConstants.TEST_F64) );
		assertTrue( 0 < Intersection2D_F64.intersection(lineB,ellipse,pA,pB, GrlConstants.TEST_F64));
		assertEquals(0,pA.distance(pB) , Math.sqrt(GrlConstants.TEST_F64 *20.0) );

		// Make sure the lines are not identical
		boolean idential = true;
		idential &= Math.abs( lineA.A - lineB.A ) <= GrlConstants.TEST_F64;
		idential &= Math.abs( lineA.B - lineB.B ) <= GrlConstants.TEST_F64;
		idential &= Math.abs( lineA.C - lineB.C ) <= GrlConstants.TEST_F64;

		assertFalse( idential );
	}

	/**
	 * Very basic unit test. The more rigerous one is in
	 * {@link TestTangentLinesTwoEllipses_F64}
	 */
	@Test
	void tangentLines_ellipse_ellipse() {

		EllipseRotated_F64 ellipseA = new EllipseRotated_F64(0,1,4,2,0.1);
		EllipseRotated_F64 ellipseB = new EllipseRotated_F64(-6,1.2,1.5,0.8,-0.6);

		Point2D_F64 tangentA0 = new Point2D_F64();
		Point2D_F64 tangentA1 = new Point2D_F64();
		Point2D_F64 tangentA2 = new Point2D_F64();
		Point2D_F64 tangentA3 = new Point2D_F64();

		Point2D_F64 tangentB0 = new Point2D_F64();
		Point2D_F64 tangentB1 = new Point2D_F64();
		Point2D_F64 tangentB2 = new Point2D_F64();
		Point2D_F64 tangentB3 = new Point2D_F64();

		UtilEllipse_F64.tangentLines(ellipseA,ellipseB,
				tangentA0,tangentA1,tangentA2,tangentA3,
				tangentB0,tangentB1,tangentB2,tangentB3);

		Point2D_F64 fooA0 = new Point2D_F64();
		Point2D_F64 fooA1 = new Point2D_F64();
		Point2D_F64 fooA2 = new Point2D_F64();
		Point2D_F64 fooA3 = new Point2D_F64();

		Point2D_F64 fooB0 = new Point2D_F64();
		Point2D_F64 fooB1 = new Point2D_F64();
		Point2D_F64 fooB2 = new Point2D_F64();
		Point2D_F64 fooB3 = new Point2D_F64();

		// see if it produces the same results as invoking the algorithm directly
		TangentLinesTwoEllipses_F64 alg = new TangentLinesTwoEllipses_F64(GrlConstants.TEST_F64,10);

		alg.process(ellipseA,ellipseB, fooA0,fooA1,fooA2,fooA3, fooB0,fooB1,fooB2,fooB3);

		assertEquals( 0, fooA0.distance(tangentA0), GrlConstants.TEST_F64);
		assertEquals( 0, fooA1.distance(tangentA1), GrlConstants.TEST_F64);
		assertEquals( 0, fooA2.distance(tangentA2), GrlConstants.TEST_F64);
		assertEquals( 0, fooA3.distance(tangentA3), GrlConstants.TEST_F64);

		assertEquals( 0, fooB0.distance(tangentB0), GrlConstants.TEST_F64);
		assertEquals( 0, fooB1.distance(tangentB1), GrlConstants.TEST_F64);
		assertEquals( 0, fooB2.distance(tangentB2), GrlConstants.TEST_F64);
		assertEquals( 0, fooB3.distance(tangentB3), GrlConstants.TEST_F64);

	}
}
