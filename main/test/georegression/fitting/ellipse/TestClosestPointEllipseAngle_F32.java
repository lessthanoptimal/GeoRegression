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

package georegression.fitting.ellipse;

import georegression.geometry.UtilEllipse_F32;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.EllipseRotated_F32;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestClosestPointEllipseAngle_F32 {

	Random rand = new Random(234);

	@Test
	public void easyTest() {
		checkSolution(0,0,3,1.5f,0,5,6);
		checkSolution(1,2,3,1.5f,0.2f,5,6);
		checkSolution(1,2,3,1.5f,0.2f,1,2);
	}

	@Test
	public void random() {
		for( int i = 0; i < 100; i++ ) {
			float x0 = (rand.nextFloat()-0.5f)*5;
			float y0 = (rand.nextFloat()-0.5f)*5;
			float b = rand.nextFloat()*3+0.1f;
			float a = b + rand.nextFloat();
			float phi = rand.nextFloat()*(float)Math.PI*2;
			float x = (rand.nextFloat()-0.5f)*10;
			float y = (rand.nextFloat()-0.5f)*10;

			checkSolution(x0,y0,a,b,phi,x,y);
		}
	}

	public void checkSolution( float x0 , float y0, float a, float b, float phi , float x , float y ) {
		EllipseRotated_F32 ellipse = new EllipseRotated_F32(x0,y0,a,b,phi);

		ClosestPointEllipseAngle_F32 alg = new ClosestPointEllipseAngle_F32(GrlConstants.FLOAT_TEST_TOL,200);

		Point2D_F32 p = new Point2D_F32(x,y);
		alg.setEllipse(ellipse);
		alg.process(p);

		Point2D_F32 found = alg.getClosest();

		// is it on the ellipse?
		assertEquals(1, UtilEllipse_F32.evaluate(found.x,found.y,ellipse),GrlConstants.FLOAT_TEST_TOL);

		// skip local test for center
		if( x0 == x && y0 == y )
			return;

		float dist = found.distance(p);
		float dist0 = p.distance(UtilEllipse_F32.computePoint(alg.getTheta()+0.05f,ellipse,null));
		float dist1 = p.distance(UtilEllipse_F32.computePoint(alg.getTheta()-0.05f,ellipse,null));

		// is it a local optimal?
		// inflate slightly to take in account floating point precision
		assertTrue( dist < dist0*1.001f );
		assertTrue( dist < dist1*1.001f );
	}

}
