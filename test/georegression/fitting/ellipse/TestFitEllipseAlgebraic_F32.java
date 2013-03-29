/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
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
public class TestFitEllipseAlgebraic_F32 {

	Random rand = new Random(234);

	@Test
	public void checkCircle() {
		checkEllipse(0, 0, 3, 3, 0);
	}

	/**
	 * Give it points which perfectly describe an ellipse.   This isn't actually an easy case.  See comments
	 * in random section.
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
			points.add(UtilEllipse_F32.computePoint(theta, rotated, null));
//			System.out.println(points.get(i).x+" "+points.get(i).y);
		}

		EllipseQuadratic_F32 expected = UtilEllipse_F32.convert(rotated,null);

		FitEllipseAlgebraic_F32 alg = new FitEllipseAlgebraic_F32();
		assertTrue(alg.process(points));

		EllipseQuadratic_F32 found = alg.getEllipse();

		normalize(expected);
		normalize(found);

		assertEquals(expected.a,found.a, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.b,found.b, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.c,found.c, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.d,found.d, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.e,found.e, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.f,found.f, GrlConstants.FLOAT_TEST_TOL);
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
