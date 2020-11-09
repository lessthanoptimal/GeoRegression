/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.geometry.curves;

import georegression.geometry.UtilEllipse_F64;
import georegression.geometry.UtilVector2D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public class TestTangentLinesTwoEllipses_F64 {

	Point2D_F64 tangentA0 = new Point2D_F64(); Point2D_F64 tangentA1 = new Point2D_F64();
	Point2D_F64 tangentA2 = new Point2D_F64(); Point2D_F64 tangentA3 = new Point2D_F64();
	Point2D_F64 tangentB0 = new Point2D_F64(); Point2D_F64 tangentB1 = new Point2D_F64();
	Point2D_F64 tangentB2 = new Point2D_F64(); Point2D_F64 tangentB3 = new Point2D_F64();

	@Test
	void process() {
		TangentLinesTwoEllipses_F64 alg = new TangentLinesTwoEllipses_F64(GrlConstants.TEST_F64, 20);

		for( int i = 0; i < 20; i++ ) {
			double theta = i*GrlConstants.PI/20 - GrlConstants.PI/2.0;
//			System.out.println(i+"  Theta = "+theta);
			check( new EllipseRotated_F64(0,10,5,4,theta), new EllipseRotated_F64(0,0,4,3,0), alg);
			check( new EllipseRotated_F64(0,10,4,4,theta), new EllipseRotated_F64(0,0,4,4,0), alg);
			check( new EllipseRotated_F64(2.5,10,5,4,theta), new EllipseRotated_F64(0,0,4.9,3, GrlConstants.PId2), alg);
		}
	}

	public void check( EllipseRotated_F64 ellipseA , EllipseRotated_F64 ellipseB , TangentLinesTwoEllipses_F64 alg ) {
		assertTrue(alg.process(ellipseA,ellipseB,
				tangentA0, tangentA1, tangentA2, tangentA3,
				tangentB0, tangentB1, tangentB2, tangentB3));

		assertTrue(alg.isConverged());

		// make sure all the points are unique
		checkResults(ellipseA, ellipseB, true);
	}

	@Test
	void initialize() {
		TangentLinesTwoEllipses_F64 alg = new TangentLinesTwoEllipses_F64(GrlConstants.TEST_F64, 20);

		EllipseRotated_F64 ellipseA = new EllipseRotated_F64(0,10,5,4,0);
		EllipseRotated_F64 ellipseB = new EllipseRotated_F64(0,0,4,3, Math.PI/2.0);

		assertTrue(alg.initialize(ellipseA,ellipseB,
				tangentA0, tangentA1, tangentA2, tangentA3,
				tangentB0, tangentB1, tangentB2, tangentB3));

		checkResults(ellipseA, ellipseB, false);
	}

	private void checkResults(EllipseRotated_F64 ellipseA, EllipseRotated_F64 ellipseB ,
							  boolean completeTest ) {
		// make sure all the points are unique
		assertFalse(tangentA0.distance(tangentA1) <= GrlConstants.TEST_F64);
		assertFalse(tangentA0.distance(tangentA2) <= GrlConstants.TEST_F64);
		assertFalse(tangentA0.distance(tangentA3) <= GrlConstants.TEST_F64);
		assertFalse(tangentA1.distance(tangentA2) <= GrlConstants.TEST_F64);
		assertFalse(tangentA1.distance(tangentA3) <= GrlConstants.TEST_F64);
		assertFalse(tangentA2.distance(tangentA3) <= GrlConstants.TEST_F64);

		assertFalse(tangentB0.distance(tangentB1) <= GrlConstants.TEST_F64);

		if( completeTest ) {
			assertFalse(tangentB0.distance(tangentB2) <= GrlConstants.TEST_F64);
			assertFalse(tangentB0.distance(tangentB3) <= GrlConstants.TEST_F64);
			assertFalse(tangentB1.distance(tangentB2) <= GrlConstants.TEST_F64);
			assertFalse(tangentB1.distance(tangentB3) <= GrlConstants.TEST_F64);
			assertFalse(tangentB2.distance(tangentB3) <= GrlConstants.TEST_F64);

			// make sure each pair is tangent
			checkIsTangent(tangentA0, tangentB0, ellipseA, ellipseB);
			checkIsTangent(tangentA1, tangentB1, ellipseA, ellipseB);
			checkIsTangent(tangentA2, tangentB2, ellipseA, ellipseB);
			checkIsTangent(tangentA3, tangentB3, ellipseA, ellipseB);
		}
	}

	private void checkIsTangent( Point2D_F64 a , Point2D_F64 b ,
								 EllipseRotated_F64 ellipseA, EllipseRotated_F64 ellipseB)
	{
		double ta = UtilEllipse_F64.computeAngle(a,ellipseA);
		double tb = UtilEllipse_F64.computeAngle(b,ellipseB);

		double slopeX = b.x-a.x;
		double slopeY = b.y-a.y;
		double r = Math.sqrt( slopeX*slopeX + slopeY*slopeY);

		slopeX /= r;
		slopeY /= r;

		Vector2D_F64 slopeA = UtilEllipse_F64.computeTangent(ta,ellipseA,null);
		Vector2D_F64 slopeB = UtilEllipse_F64.computeTangent(tb,ellipseB,null);

		assertTrue(UtilVector2D_F64.identicalSign(slopeX,slopeY, slopeA.x, slopeA.y, GrlConstants.TEST_SQ_F64));
		assertTrue(UtilVector2D_F64.identicalSign(slopeX,slopeY, slopeB.x, slopeB.y, GrlConstants.TEST_SQ_F64));

	}

	@Test
	void selectTangent() {
		TangentLinesTwoEllipses_F64 alg = new TangentLinesTwoEllipses_F64(GrlConstants.TEST_F64, 20);

		EllipseRotated_F64 ellipse = new EllipseRotated_F64(0,10,2,2,0);

		Point2D_F64 a = new Point2D_F64(2,5);
		Point2D_F64 srcA = new Point2D_F64(2,11.5);
		Point2D_F64 found = new Point2D_F64();

		alg.centerLine.setTo(0,0,0,10);
		assertTrue(alg.selectTangent(a,srcA,ellipse,found, false));

		assertEquals(2, found.x, GrlConstants.TEST_F64);
		assertEquals(10, found.y, GrlConstants.TEST_F64);

		assertEquals(1.5*1.5, alg.sumDifference, GrlConstants.TEST_F64);

		assertTrue(alg.selectTangent(a,srcA,ellipse,found, true));
		assertNotEquals(2, found.x, GrlConstants.TEST_F64);
		assertNotEquals(10, found.y, GrlConstants.TEST_F64);
	}
}
