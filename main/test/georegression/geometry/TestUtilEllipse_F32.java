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
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.EllipseQuadratic_F32;
import georegression.struct.shapes.EllipseRotated_F32;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
			float t = (float)Math.PI*2*i/100.0f;
			Point2D_F32 p = UtilEllipse_F32.computePoint(t,rotated,null);
			float eval = UtilEllipse_F32.evaluate(p.x,p.y,quad);
			assertEquals(0,eval, GrlConstants.FLOAT_TEST_TOL);
		}
	}

	@Test
	public void computeAngle() {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(1,2,4.5f,3,0.2f);

		for( int i = 0; i <= 100; i++ ) {
			float t = (float)Math.PI*2*i/100.0f - (float)Math.PI;
			Point2D_F32 p = UtilEllipse_F32.computePoint(t,rotated,null);
			float found = UtilEllipse_F32.computeAngle(p,rotated);
//			System.out.println(t+" "+found);
			assertTrue(UtilAngle.dist(t, found) <= GrlConstants.FLOAT_TEST_TOL);
		}
	}

}
