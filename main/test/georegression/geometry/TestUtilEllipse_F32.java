/*
 * Copyright (C) 2011-2016, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.algs.TangentLinesTwoEllipses_F32;
import georegression.metric.Intersection2D_F32;
import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Vector2D_F32;
import georegression.struct.shapes.EllipseQuadratic_F32;
import georegression.struct.shapes.EllipseRotated_F32;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestUtilEllipse_F32 {

	Random rand = new Random(234);

	@Test
	public void convert_back_forth() {
		convert_back_forth(0,0,4.5f,3,0);
		convert_back_forth(1,2,4.5f,3,0);
		convert_back_forth(0,0,4.5f,3,(float)Math.PI/4);
		convert_back_forth(0,0,4.5f,3,0.1f);
		convert_back_forth(-2,1.5f,4.5f,3,-0.1f);

		convert_back_forth(1,2,3,1.5f,0);
		convert_back_forth(1,2,3,1.5f,1.5f);

		// see if it can handle a circle
		convert_back_forth(0,0,3,3,0);
	}

	@Test
	public void convert_back_forth_random() {

		for( int i = 0; i < 100; i++ ) {
			float x = (rand.nextFloat()-0.5f)*2;
			float y = (rand.nextFloat()-0.5f)*2;
			float theta = (rand.nextFloat()-0.5f)*(float)Math.PI;
			float b = rand.nextFloat()*2+0.1f;
			float a = b+rand.nextFloat();

			convert_back_forth(x,y,a,b,theta);
		}
	}

	public void convert_back_forth( float x0 , float y0, float a, float b, float phi ) {
		// should be scale invariant
		convert_back_forth(x0,y0,a,b,phi,1);
		convert_back_forth(x0,y0,a,b,phi,-1);
	}

	public void convert_back_forth( float x0 , float y0, float a, float b, float phi , float scale ) {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(x0,y0,a,b,phi);
		EllipseQuadratic_F32 quad = new EllipseQuadratic_F32();
		EllipseRotated_F32 found = new EllipseRotated_F32();

		UtilEllipse_F32.convert(rotated,quad);

		quad.a *= scale;
		quad.b *= scale;
		quad.c *= scale;
		quad.d *= scale;
		quad.e *= scale;
		quad.f *= scale;

		UtilEllipse_F32.convert(quad,found);

		assertEquals(rotated.center.x,found.center.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(rotated.center.y,found.center.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(rotated.a,found.a, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(rotated.b,found.b, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(rotated.phi,found.phi, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_rotated_to_quad() {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(1,2,4.5f,3,0.2f);

		Point2D_F32 p = UtilEllipse_F32.computePoint(0.45f,rotated,null);

		float eval = UtilEllipse_F32.evaluate(p.x,p.y,rotated);
		assertEquals(1,eval, GrlConstants.FLOAT_TEST_TOL);

		EllipseQuadratic_F32 quad = new EllipseQuadratic_F32();
		UtilEllipse_F32.convert(rotated,quad);
		eval = UtilEllipse_F32.evaluate(p.x,p.y,quad);
		assertEquals(0,eval, GrlConstants.FLOAT_TEST_TOL);
	}

	/**
	 * Tests computePoint and evaluate(rotated) by computes points around the ellipse and seeing if they
	 * meet the expected results.
	 */
	@Test
	public void computePoint_evaluate_rotated() {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(1,2,4.5f,3,0.2f);

		for( int i = 0; i < 100; i++ ) {
			float t = (float)Math.PI*2*i/100.0f;
			Point2D_F32 p = UtilEllipse_F32.computePoint(t,rotated,null);
			float eval = UtilEllipse_F32.evaluate(p.x,p.y,rotated);
			assertEquals(1,eval, GrlConstants.FLOAT_TEST_TOL);
		}
	}

	@Test
	public void computePoint_evaluate_quadratic() {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(1,2,4.5f,3,0.2f);
		EllipseQuadratic_F32 quad = new EllipseQuadratic_F32();
		UtilEllipse_F32.convert(rotated,quad);

		for( int i = 0; i < 100; i++ ) {
			float t = GrlConstants.F_PI*2*i/100.0f;
			Point2D_F32 p = UtilEllipse_F32.computePoint(t,rotated,null);
			float eval = UtilEllipse_F32.evaluate(p.x,p.y,quad);
			assertEquals(0,eval, GrlConstants.FLOAT_TEST_TOL);
		}
	}

	/**
	 * Try a few simple cases
	 */
	@Test
	public void computePoint_rotated() {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(1,2,3,2,GrlConstants.F_PId2);

		Point2D_F32 p = UtilEllipse_F32.computePoint(0,rotated,null);
		assertEquals(1.0f,p.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2+3,p.y, GrlConstants.FLOAT_TEST_TOL);

		p = UtilEllipse_F32.computePoint(GrlConstants.F_PId2,rotated,null);
		assertEquals(-1.0f,p.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 2.0f,p.y, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void computeAngle() {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(1,2,4.5f,3,0.2f);

		for( int i = 0; i <= 100; i++ ) {
			float t = GrlConstants.F_PI*2*i/100.0f - GrlConstants.F_PI;
			Point2D_F32 p = UtilEllipse_F32.computePoint(t,rotated,null);
			float found = UtilEllipse_F32.computeAngle(p,rotated);
//			System.out.println(t+" "+found);
			assertTrue(UtilAngle.dist(t, found) <= GrlConstants.FLOAT_TEST_TOL);
		}
	}


	@Test
	public void computeTangent_rotated() {
		float delta = GrlConstants.FLOAT_TEST_TOL;

		// axis aligned case
		EllipseRotated_F32 rotated = new EllipseRotated_F32(1,2,4.5f,3,0);

		for (int i = 0; i < 20; i++) {
			float theta = i*GrlConstants.F_PI*2.0f/20.0f;
			checkTangent(theta,rotated,delta);
		}

		// rotated case
		rotated = new EllipseRotated_F32(1,2,4.5f,3,0.4f);

		for (int i = 0; i < 20; i++) {
			float theta = i*GrlConstants.F_PI*2.0f/20.0f;
			checkTangent(theta,rotated,delta);
		}
	}

	private void checkTangent( float t , EllipseRotated_F32 ellipse , float delta ) {
		Vector2D_F32 found = UtilEllipse_F32.computeTangent(t,ellipse,null);
		Vector2D_F32 expected = numericalTangent(t,ellipse,delta);

		float error0 = found.distance(expected);
		expected.x *= -1;
		expected.y *= -1;
		float error1 = found.distance(expected);

		float error = (float)Math.min(error0,error1);

		assertEquals(0,error,Math.sqrt(delta));
	}

	private Vector2D_F32 numericalTangent( float t , EllipseRotated_F32 ellipse , float delta ) {

		Point2D_F32 a = UtilEllipse_F32.computePoint(t-delta,ellipse,null);
		Point2D_F32 b = UtilEllipse_F32.computePoint(t+delta,ellipse,null);

		Vector2D_F32 output = new Vector2D_F32();
		output.x = (b.x-a.x)/(2.0f*delta);
		output.y = (b.y-a.y)/(2.0f*delta);

		output.normalize();

		return output;
	}

	@Test
	public void tangentLines_point_ellipse() {

		// simple case with a circle at the origin
		checkTangentLines( -2,2, new EllipseRotated_F32(0,0,2,2,0));
		checkTangentLines( 0,-10, new EllipseRotated_F32(0,0,2,2,0));
		checkTangentLines( -10,0, new EllipseRotated_F32(0,0,2,2,0));
		checkTangentLines( -10,1, new EllipseRotated_F32(0,0,2,2,0));
		checkTangentLines( 1,-10, new EllipseRotated_F32(0,0,2,2,0));

		// test failure case.  Inside
		checkTangentLinesFail( 0,0, new EllipseRotated_F32(0,0,2,2,0));
		checkTangentLinesFail( 0.05f,0, new EllipseRotated_F32(0,0,2,2,0));
		checkTangentLinesFail( 0,0.1f, new EllipseRotated_F32(0,0,2,2,0));
		checkTangentLinesFail( 0.05f,0.1f, new EllipseRotated_F32(0,0,2,2,0));

		// same, but not circular
		checkTangentLines( -2,2, new EllipseRotated_F32(0,0,2,3,0));
		checkTangentLines( 0,-10, new EllipseRotated_F32(0,0,2,3,0));
		checkTangentLines( -10,0, new EllipseRotated_F32(0,0,2,3,0));
		checkTangentLines( -10,1, new EllipseRotated_F32(0,0,2,3,0));
		checkTangentLines( 1,-10, new EllipseRotated_F32(0,0,2,3,0));

		// same, but translated
		float x = 1.2f, y = -0.5f;
		checkTangentLines( -2+x , 2  +y, new EllipseRotated_F32(x,y,2,3,0));
		checkTangentLines(  0+x , -10+y, new EllipseRotated_F32(x,y,2,3,0));
		checkTangentLines( -10+x, 0  +y, new EllipseRotated_F32(x,y,2,3,0));
		checkTangentLines( -10+x, 1  +y, new EllipseRotated_F32(x,y,2,3,0));
		checkTangentLines(  1+x , -10+y, new EllipseRotated_F32(x,y,2,3,0));

		// same, but translated and rotated
		checkTangentLines( -3+x , 3  +y, new EllipseRotated_F32(x,y,2,3,0.1f));
		checkTangentLines(  0+x , -10+y, new EllipseRotated_F32(x,y,2,3,0.1f));
		checkTangentLines( -10+x, 0  +y, new EllipseRotated_F32(x,y,2,3,0.1f));
		checkTangentLines( -10+x, 1  +y, new EllipseRotated_F32(x,y,2,3,0.1f));
		checkTangentLines(  1+x , -10+y, new EllipseRotated_F32(x,y,2,3,0.1f));
	}

	public void checkTangentLinesFail( float x, float y , EllipseRotated_F32 ellipse ) {
		Point2D_F32 pt = new Point2D_F32(x,y);

		Point2D_F32 pointA = new Point2D_F32();
		Point2D_F32 pointB = new Point2D_F32();

		assertFalse(UtilEllipse_F32.tangentLines(pt,ellipse,pointA,pointB));
	}

	public void checkTangentLines( float x, float y , EllipseRotated_F32 ellipse ) {
		Point2D_F32 pt = new Point2D_F32(x,y);

		Point2D_F32 pointA = new Point2D_F32();
		Point2D_F32 pointB = new Point2D_F32();

		assertTrue(UtilEllipse_F32.tangentLines(pt,ellipse,pointA,pointB));

		LineGeneral2D_F32 lineA = UtilLine2D_F32.convert(pt,pointA,(LineGeneral2D_F32)null);
		LineGeneral2D_F32 lineB = UtilLine2D_F32.convert(pt,pointB,(LineGeneral2D_F32)null);

		// the point should pass through both lines
		assertEquals(0, lineA.evaluate(pt.x,pt.y), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0, lineB.evaluate(pt.x,pt.y), GrlConstants.FLOAT_TEST_TOL);

		// if it's tangent there should only be one point of intersection
		Point2D_F32 pA = new Point2D_F32();
		Point2D_F32 pB = new Point2D_F32();

		assertTrue( 0 < Intersection2D_F32.intersection(lineA,ellipse,pA,pB, GrlConstants.FLOAT_TEST_TOL));
		assertEquals(0,pA.distance(pB) , (float)Math.sqrt(GrlConstants.FLOAT_TEST_TOL) );
		assertTrue( 0 < Intersection2D_F32.intersection(lineB,ellipse,pA,pB, GrlConstants.FLOAT_TEST_TOL));
		assertEquals(0,pA.distance(pB) , (float)Math.sqrt(GrlConstants.FLOAT_TEST_TOL*20.0f) );

		// Make sure the lines are not identical
		boolean idential = true;
		idential &= (float)Math.abs( lineA.A - lineB.A ) <= GrlConstants.FLOAT_TEST_TOL;
		idential &= (float)Math.abs( lineA.B - lineB.B ) <= GrlConstants.FLOAT_TEST_TOL;
		idential &= (float)Math.abs( lineA.C - lineB.C ) <= GrlConstants.FLOAT_TEST_TOL;

		assertFalse( idential );
	}

	/**
	 * Very basic unit test.  The more rigerous one is in
	 * {@link georegression.geometry.algs.TestTangentLinesTwoEllipses_F32}
	 */
	@Test
	public void tangentLines_ellipse_ellipse() {

		EllipseRotated_F32 ellipseA = new EllipseRotated_F32(0,1,4,2,0.1f);
		EllipseRotated_F32 ellipseB = new EllipseRotated_F32(-6,1.2f,1.5f,0.8f,-0.6f);

		Point2D_F32 tangentA0 = new Point2D_F32();
		Point2D_F32 tangentA1 = new Point2D_F32();
		Point2D_F32 tangentA2 = new Point2D_F32();
		Point2D_F32 tangentA3 = new Point2D_F32();

		Point2D_F32 tangentB0 = new Point2D_F32();
		Point2D_F32 tangentB1 = new Point2D_F32();
		Point2D_F32 tangentB2 = new Point2D_F32();
		Point2D_F32 tangentB3 = new Point2D_F32();

		UtilEllipse_F32.tangentLines(ellipseA,ellipseB,
				tangentA0,tangentA1,tangentA2,tangentA3,
				tangentB0,tangentB1,tangentB2,tangentB3);

		Point2D_F32 fooA0 = new Point2D_F32();
		Point2D_F32 fooA1 = new Point2D_F32();
		Point2D_F32 fooA2 = new Point2D_F32();
		Point2D_F32 fooA3 = new Point2D_F32();

		Point2D_F32 fooB0 = new Point2D_F32();
		Point2D_F32 fooB1 = new Point2D_F32();
		Point2D_F32 fooB2 = new Point2D_F32();
		Point2D_F32 fooB3 = new Point2D_F32();

		// see if it produces the same results as invoking the algorithm directly
		TangentLinesTwoEllipses_F32 alg = new TangentLinesTwoEllipses_F32(GrlConstants.FLOAT_TEST_TOL,10);

		alg.process(ellipseA,ellipseB, fooA0,fooA1,fooA2,fooA3, fooB0,fooB1,fooB2,fooB3);

		assertEquals( 0, fooA0.distance(tangentA0), GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 0, fooA1.distance(tangentA1), GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 0, fooA2.distance(tangentA2), GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 0, fooA3.distance(tangentA3), GrlConstants.FLOAT_TEST_TOL);

		assertEquals( 0, fooB0.distance(tangentB0), GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 0, fooB1.distance(tangentB1), GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 0, fooB2.distance(tangentB2), GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 0, fooB3.distance(tangentB3), GrlConstants.FLOAT_TEST_TOL);

	}
}
