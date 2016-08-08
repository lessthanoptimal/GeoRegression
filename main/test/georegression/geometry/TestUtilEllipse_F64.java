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
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import georegression.struct.shapes.EllipseQuadratic_F64;
import georegression.struct.shapes.EllipseRotated_F64;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestUtilEllipse_F64 {

	Random rand = new Random(234);

	@Test
	public void convert_back_forth() {
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
	public void convert_back_forth_random() {

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

		quad.a *= scale;
		quad.b *= scale;
		quad.c *= scale;
		quad.d *= scale;
		quad.e *= scale;
		quad.f *= scale;

		UtilEllipse_F64.convert(quad,found);

		assertEquals(rotated.center.x,found.center.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(rotated.center.y,found.center.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(rotated.a,found.a, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(rotated.b,found.b, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(rotated.phi,found.phi, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void convert_rotated_to_quad() {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(1,2,4.5,3,0.2);

		Point2D_F64 p = UtilEllipse_F64.computePoint(0.45,rotated,null);

		double eval = UtilEllipse_F64.evaluate(p.x,p.y,rotated);
		assertEquals(1,eval, GrlConstants.DOUBLE_TEST_TOL);

		EllipseQuadratic_F64 quad = new EllipseQuadratic_F64();
		UtilEllipse_F64.convert(rotated,quad);
		eval = UtilEllipse_F64.evaluate(p.x,p.y,quad);
		assertEquals(0,eval, GrlConstants.DOUBLE_TEST_TOL);
	}

	/**
	 * Tests computePoint and evaluate(rotated) by computes points around the ellipse and seeing if they
	 * meet the expected results.
	 */
	@Test
	public void computePoint_evaluate_rotated() {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(1,2,4.5,3,0.2);

		for( int i = 0; i < 100; i++ ) {
			double t = Math.PI*2*i/100.0;
			Point2D_F64 p = UtilEllipse_F64.computePoint(t,rotated,null);
			double eval = UtilEllipse_F64.evaluate(p.x,p.y,rotated);
			assertEquals(1,eval, GrlConstants.DOUBLE_TEST_TOL);
		}
	}

	@Test
	public void computePoint_evaluate_quadratic() {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(1,2,4.5,3,0.2);
		EllipseQuadratic_F64 quad = new EllipseQuadratic_F64();
		UtilEllipse_F64.convert(rotated,quad);

		for( int i = 0; i < 100; i++ ) {
			double t = GrlConstants.PI*2*i/100.0;
			Point2D_F64 p = UtilEllipse_F64.computePoint(t,rotated,null);
			double eval = UtilEllipse_F64.evaluate(p.x,p.y,quad);
			assertEquals(0,eval, GrlConstants.DOUBLE_TEST_TOL);
		}
	}

	@Test
	public void computeAngle() {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(1,2,4.5,3,0.2);

		for( int i = 0; i <= 100; i++ ) {
			double t = GrlConstants.PI*2*i/100.0 - GrlConstants.PI;
			Point2D_F64 p = UtilEllipse_F64.computePoint(t,rotated,null);
			double found = UtilEllipse_F64.computeAngle(p,rotated);
//			System.out.println(t+" "+found);
			assertTrue(UtilAngle.dist(t, found) <= GrlConstants.DOUBLE_TEST_TOL);
		}
	}


	@Test
	public void computeTangent_rotated() {
		double delta = GrlConstants.DOUBLE_TEST_TOL;

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
}
