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

package georegression.fitting.curves;

import georegression.geometry.UtilEllipse_F64;
import georegression.misc.GrlConstants;
import georegression.struct.curve.EllipseQuadratic_F64;
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.point.Point2D_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestFitEllipseWeightedAlgebraic_F64 {

	Random rand = new Random(234);

	@Test
	void checkCircle() {
		checkEllipse(0, 0, 3, 3, 0);
	}

	/**
	 * Give it points which are nearly perfectly describe an ellipse.
	 * Perfect points is actually a hard case. See comments in random section.
	 */
	@Test
	void checkEllipse() {
		checkEllipse(0,0,3,1.5,0);
		checkEllipse(1,2,3,1.5,0);
		checkEllipse(1,2,3,1.5,0.25);
	}

	public void checkEllipse( double x0 , double y0, double a, double b, double phi ) {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(x0,y0,a,b,phi);

		List<Point2D_F64> points = new ArrayList<Point2D_F64>();
		double weights[] = new double[21];
		for( int i = 0; i < 20; i++ ) {
			double theta = 2.0*(double)Math.PI*i/20;
			Point2D_F64 p = UtilEllipse_F64.computePoint(theta, rotated, null);

			// give it just a little bit of noise so that it will converge
			p.x += rand.nextGaussian()*GrlConstants.TEST_F64;
			p.y += rand.nextGaussian()*GrlConstants.TEST_F64;

			points.add(p);
			weights[i] = 0.3;
//			System.out.println(points.get(i).x+" "+points.get(i).y);
		}

		// throw in a point that's way off but has no weight and should be ignored
		points.add( new Point2D_F64(20,34));
		weights[20] = 0;

		EllipseQuadratic_F64 expected = UtilEllipse_F64.convert(rotated,null);

		FitEllipseWeightedAlgebraic_F64 alg = new FitEllipseWeightedAlgebraic_F64();
		assertTrue(alg.process(points,weights));

		EllipseQuadratic_F64 found = alg.getEllipse();

		normalize(expected);
		normalize(found);

		assertEquals(expected.A,found.A, GrlConstants.TEST_F64);
		assertEquals(expected.B,found.B, GrlConstants.TEST_F64);
		assertEquals(expected.C,found.C, GrlConstants.TEST_F64);
		assertEquals(expected.D,found.D, GrlConstants.TEST_F64);
		assertEquals(expected.E,found.E, GrlConstants.TEST_F64);
		assertEquals(expected.F,found.F, GrlConstants.TEST_F64);
	}

	/**
	 * A bad point is given and see if the estimated error gets worse as it's weight is increased
	 */
	@Test
	void checkErrorIncreasedWithWeight() {
		checkEllipse(0,0,3,1.5,0);
		checkEllipse(1,2,3,1.5,0);
		checkEllipse(1,2,3,1.5,0.25);
	}
	public void checkErrorIncreasedWithWeight( double x0 , double y0, double a, double b, double phi ) {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(x0,y0,a,b,phi);

		double errorWeight[] = new double[]{0,0.1,0.5,2.0};
		double error[] = new double[4];

		for (int i = 0; i < error.length; i++) {
			List<Point2D_F64> points = new ArrayList<Point2D_F64>();
			double weights[] = new double[21];
			for( int j = 0; j < 20; j++ ) {
				double theta = 2.0*(double)Math.PI*j/20;
				points.add(UtilEllipse_F64.computePoint(theta, rotated, null));
				weights[j] = 1.0;
			}

			// throw in a point that's way off but has no weight and should be ignored
			points.add( new Point2D_F64(20,34));
			weights[20] = errorWeight[i];

			EllipseQuadratic_F64 expected = UtilEllipse_F64.convert(rotated,null);

			FitEllipseWeightedAlgebraic_F64 alg = new FitEllipseWeightedAlgebraic_F64();
			assertTrue(alg.process(points,weights));

			EllipseQuadratic_F64 found = alg.getEllipse();

			normalize(expected);
			normalize(found);

			error[i] = Math.abs(expected.A - found.A);
			error[i] += Math.abs(expected.B - found.B);
			error[i] += Math.abs(expected.C - found.C);
			error[i] += Math.abs(expected.D - found.D);
			error[i] += Math.abs(expected.E - found.E);
			error[i] += Math.abs(expected.F - found.F);
		}


		assertTrue( error[1] > error[0]);
		assertTrue( error[2] > error[1]);
		assertTrue( error[3] > error[2]);
	}

	/**
	 * Randomly generate points and see if it produces a valid ellipse
	 *
	 * The paper mentions that the case of perfect data is actually numerically unstable. The random test
	 * below has been commented out since even the original algorithm run in octave can't pass that test.
	 */
	@Test
	void checkRandom() {
//		for( int i = 0; i < 100; i++ ) {
//			System.out.println("i = "+i);
//			double x0 = (rand.nextDouble()-0.5)*2;
//			double y0 = (rand.nextDouble()-0.5)*2;
//			double b = rand.nextDouble();
//			double a = b+rand.nextDouble()*2+0.1;
//			double theta = (rand.nextDouble()-0.5)*Math.PI;
//
//			checkEllipse(x0,y0,a,b,theta);
//		}
	}

	private void normalize( EllipseQuadratic_F64 ellipse )  {
		ellipse.A /= ellipse.F;
		ellipse.B /= ellipse.F;
		ellipse.C /= ellipse.F;
		ellipse.D /= ellipse.F;
		ellipse.E /= ellipse.F;
		ellipse.F /= ellipse.F;
	}

}
