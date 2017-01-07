/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.ellipse;

import georegression.geometry.UtilEllipse_F32;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.EllipseQuadratic_F32;
import georegression.struct.shapes.EllipseRotated_F32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestFitEllipseWeightedAlgebraic_F32 {

	Random rand = new Random(234);

	@Test
	public void checkCircle() {
		checkEllipse(0, 0, 3, 3, 0);
	}

	/**
	 * Give it points which are nearly perfectly describe an ellipse.
	 * Perfect points is actually a hard case.  See comments in random section.
	 */
	@Test
	public void checkEllipse() {
		checkEllipse(0,0,3,1.5f,0);
		checkEllipse(1,2,3,1.5f,0);
		checkEllipse(1,2,3,1.5f,0.25f);
	}

	public void checkEllipse( float x0 , float y0, float a, float b, float phi ) {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(x0,y0,a,b,phi);

		List<Point2D_F32> points = new ArrayList<Point2D_F32>();
		float weights[] = new float[21];
		for( int i = 0; i < 20; i++ ) {
			float theta = 2.0f*(float)Math.PI*i/20;
			Point2D_F32 p = UtilEllipse_F32.computePoint(theta, rotated, null);

			// give it just a little bit of noise so that it will converge
			p.x += (float)rand.nextGaussian()*GrlConstants.TEST_F32;
			p.y += (float)rand.nextGaussian()*GrlConstants.TEST_F32;

			points.add(p);
			weights[i] = 0.3f;
//			System.out.println(points.get(i).x+" "+points.get(i).y);
		}

		// throw in a point that's way off but has no weight and should be ignored
		points.add( new Point2D_F32(20,34));
		weights[20] = 0;

		EllipseQuadratic_F32 expected = UtilEllipse_F32.convert(rotated,null);

		FitEllipseWeightedAlgebraic_F32 alg = new FitEllipseWeightedAlgebraic_F32();
		assertTrue(alg.process(points,weights));

		EllipseQuadratic_F32 found = alg.getEllipse();

		normalize(expected);
		normalize(found);

		assertEquals(expected.a,found.a, GrlConstants.TEST_F32);
		assertEquals(expected.b,found.b, GrlConstants.TEST_F32);
		assertEquals(expected.c,found.c, GrlConstants.TEST_F32);
		assertEquals(expected.d,found.d, GrlConstants.TEST_F32);
		assertEquals(expected.e,found.e, GrlConstants.TEST_F32);
		assertEquals(expected.f,found.f, GrlConstants.TEST_F32);
	}

	/**
	 * A bad point is given and see if the estimated error gets worse as it's weight is increased
	 */
	@Test
	public void checkErrorIncreasedWithWeight() {
		checkEllipse(0,0,3,1.5f,0);
		checkEllipse(1,2,3,1.5f,0);
		checkEllipse(1,2,3,1.5f,0.25f);
	}
	public void checkErrorIncreasedWithWeight( float x0 , float y0, float a, float b, float phi ) {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(x0,y0,a,b,phi);

		float errorWeight[] = new float[]{0,0.1f,0.5f,2.0f};
		float error[] = new float[4];

		for (int i = 0; i < error.length; i++) {
			List<Point2D_F32> points = new ArrayList<Point2D_F32>();
			float weights[] = new float[21];
			for( int j = 0; j < 20; j++ ) {
				float theta = 2.0f*(float)Math.PI*j/20;
				points.add(UtilEllipse_F32.computePoint(theta, rotated, null));
				weights[j] = 1.0f;
			}

			// throw in a point that's way off but has no weight and should be ignored
			points.add( new Point2D_F32(20,34));
			weights[20] = errorWeight[i];

			EllipseQuadratic_F32 expected = UtilEllipse_F32.convert(rotated,null);

			FitEllipseWeightedAlgebraic_F32 alg = new FitEllipseWeightedAlgebraic_F32();
			assertTrue(alg.process(points,weights));

			EllipseQuadratic_F32 found = alg.getEllipse();

			normalize(expected);
			normalize(found);

			error[i] = (float)Math.abs(expected.a - found.a);
			error[i] += (float)Math.abs(expected.b - found.b);
			error[i] += (float)Math.abs(expected.c - found.c);
			error[i] += (float)Math.abs(expected.d - found.d);
			error[i] += (float)Math.abs(expected.e - found.e);
			error[i] += (float)Math.abs(expected.f - found.f);
		}


		assertTrue( error[1] > error[0]);
		assertTrue( error[2] > error[1]);
		assertTrue( error[3] > error[2]);
	}

	/**
	 * Randomly generate points and see if it produces a valid ellipse
	 *
	 * The paper mentions that the case of perfect data is actually numerically unstable.  The random test
	 * below has been commented out since even the original algorithm run in octave can't pass that test.
	 */
	@Test
	public void checkRandom() {
//		for( int i = 0; i < 100; i++ ) {
//			System.out.println("i = "+i);
//			float x0 = (rand.nextFloat()-0.5f)*2;
//			float y0 = (rand.nextFloat()-0.5f)*2;
//			float b = rand.nextFloat();
//			float a = b+rand.nextFloat()*2+0.1f;
//			float theta = (rand.nextFloat()-0.5f)*Math.PI;
//
//			checkEllipse(x0,y0,a,b,theta);
//		}
	}

	private void normalize( EllipseQuadratic_F32 ellipse )  {
		ellipse.a /= ellipse.f;
		ellipse.b /= ellipse.f;
		ellipse.c /= ellipse.f;
		ellipse.d /= ellipse.f;
		ellipse.e /= ellipse.f;
		ellipse.f /= ellipse.f;
	}

}
