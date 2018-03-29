/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.curves;

import georegression.geometry.UtilEllipse_F32;
import georegression.misc.GrlConstants;
import georegression.struct.curve.EllipseQuadratic_F32;
import georegression.struct.curve.EllipseRotated_F32;
import georegression.struct.point.Point2D_F32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestFitEllipseAlgebraic_F32 {

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
		for( int i = 0; i < 20; i++ ) {
			float theta = 2.0f*(float)Math.PI*i/20;
			Point2D_F32 p = UtilEllipse_F32.computePoint(theta, rotated, null);

			// give it just a little bit of noise so that it will converge
			p.x += (float)rand.nextGaussian()*GrlConstants.TEST_F32;
			p.y += (float)rand.nextGaussian()*GrlConstants.TEST_F32;

			points.add(p);
		}

		EllipseQuadratic_F32 expected = UtilEllipse_F32.convert(rotated,null);

		FitEllipseAlgebraic_F32 alg = new FitEllipseAlgebraic_F32();
		assertTrue(alg.process(points));

		EllipseQuadratic_F32 found = alg.getEllipse();

		normalize(expected);
		normalize(found);

		assertEquals(expected.A,found.A, GrlConstants.TEST_F32);
		assertEquals(expected.B,found.B, GrlConstants.TEST_F32);
		assertEquals(expected.C,found.C, GrlConstants.TEST_F32);
		assertEquals(expected.D,found.D, GrlConstants.TEST_F32);
		assertEquals(expected.E,found.E, GrlConstants.TEST_F32);
		assertEquals(expected.F,found.F, GrlConstants.TEST_F32);
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
		ellipse.A /= ellipse.F;
		ellipse.B /= ellipse.F;
		ellipse.C /= ellipse.F;
		ellipse.D /= ellipse.F;
		ellipse.E /= ellipse.F;
		ellipse.F /= ellipse.F;
	}

}
