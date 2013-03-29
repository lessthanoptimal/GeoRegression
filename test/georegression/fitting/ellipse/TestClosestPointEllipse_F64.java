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

import georegression.geometry.UtilEllipse_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseRotated_F64;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestClosestPointEllipse_F64 {

	Random rand = new Random(234);

	@Test
	public void easyTest() {
		checkSolution(0,0,3,1.5,0,5,6);
		checkSolution(1,2,3,1.5,0.2,5,6);
		checkSolution(1,2,3,1.5,0.2,1,2);
	}

	@Test
	public void random() {
		for( int i = 0; i < 100; i++ ) {
			double x0 = (rand.nextDouble()-0.5)*5;
			double y0 = (rand.nextDouble()-0.5)*5;
			double b = rand.nextDouble()*3+0.1;
			double a = b + rand.nextDouble();
			double phi = rand.nextDouble()*(double)Math.PI*2;
			double x = (rand.nextDouble()-0.5)*10;
			double y = (rand.nextDouble()-0.5)*10;

			checkSolution(x0,y0,b,a,phi,x,y);
		}
	}

	public void checkSolution( double x0 , double y0, double a, double b, double phi , double x , double y ) {
		EllipseRotated_F64 ellipse = new EllipseRotated_F64(x0,y0,a,b,phi);

		ClosestPointEllipse_F64 alg = new ClosestPointEllipse_F64(GrlConstants.DOUBLE_TEST_TOL,200);

		Point2D_F64 p = new Point2D_F64(x,y);
		alg.setEllipse(ellipse);
		alg.process(p);

		Point2D_F64 found = alg.getClosest();

		// is it on the ellipse?
		assertEquals(1, UtilEllipse_F64.evaluate(found.x,found.y,ellipse),GrlConstants.DOUBLE_TEST_TOL);

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
