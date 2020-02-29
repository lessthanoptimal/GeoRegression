/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.point.Point2D_F64;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestClosestPointEllipseAngle_F64 {

	Random rand = new Random(234);

	@Test
	void easyTest() {
		checkSolution(0,0,3,1.5,0,5,6);
		checkSolution(1,2,3,1.5,0.2,5,6);
		checkSolution(1,2,3,1.5,0.2,1,2);
	}

	@Test
	void random() {
		for( int i = 0; i < 100; i++ ) {
			double x0 = (rand.nextDouble()-0.5)*5;
			double y0 = (rand.nextDouble()-0.5)*5;
			double b = rand.nextDouble()*3+0.1;
			double a = b + rand.nextDouble();
			double phi = rand.nextDouble()*(double)Math.PI*2;
			double x = (rand.nextDouble()-0.5)*10;
			double y = (rand.nextDouble()-0.5)*10;

			checkSolution(x0,y0,a,b,phi,x,y);
		}
	}

	public void checkSolution( double x0 , double y0, double a, double b, double phi , double x , double y ) {
		EllipseRotated_F64 ellipse = new EllipseRotated_F64(x0,y0,a,b,phi);

		ClosestPointEllipseAngle_F64 alg = new ClosestPointEllipseAngle_F64(GrlConstants.TEST_F64,200);

		Point2D_F64 p = new Point2D_F64(x,y);
		alg.setEllipse(ellipse);
		alg.process(p);

		Point2D_F64 found = alg.getClosest();

		// is it on the ellipse?
		assertEquals(1, UtilEllipse_F64.evaluate(found.x,found.y,ellipse),GrlConstants.TEST_F64);

		// skip local test for center
		if( x0 == x && y0 == y )
			return;

		double dist = found.distance(p);
		double dist0 = p.distance(UtilEllipse_F64.computePoint(alg.getTheta()+0.05,ellipse,null));
		double dist1 = p.distance(UtilEllipse_F64.computePoint(alg.getTheta()-0.05,ellipse,null));

		// is it a local optimal?
		// inflate slightly to take in account floating point precision
		assertTrue( dist < dist0*1.001 );
		assertTrue( dist < dist1*1.001 );
	}

}
