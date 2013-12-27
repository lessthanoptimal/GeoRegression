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

package georegression.metric;

import georegression.fitting.ellipse.ClosestPointEllipseAngle_F64;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseRotated_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Peter Abeles
 */
public class TestClosestPoint2D_F64 {

	@Test
	public void closestPoint_parametric() {
		LineParametric2D_F64 line = new LineParametric2D_F64( 1, 2, -1, 1 );
		Point2D_F64 pt = new Point2D_F64( 1, 0 );

		Point2D_F64 found = ClosestPoint2D_F64.closestPoint( line, pt, null );
		assertEquals( 2, found.getX(), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 1, found.getY(), GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void closestPointT_parametric() {
		LineParametric2D_F64 line = new LineParametric2D_F64( 1, 2, -1, 1 );
		Point2D_F64 pt = new Point2D_F64( 1, 0 );

		double found = ClosestPoint2D_F64.closestPointT( line, pt );
		assertEquals( -1, found, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void closestPoint_ellipse() {
		EllipseRotated_F64 ellipse = new EllipseRotated_F64(1,2,3,2,0.1);

		Point2D_F64 p = new Point2D_F64(6,7);

		Point2D_F64 found = ClosestPoint2D_F64.closestPoint(ellipse,p);

		// compare to a known algorithm
		ClosestPointEllipseAngle_F64 alg = new ClosestPointEllipseAngle_F64( GrlConstants.DOUBLE_TEST_TOL , 100 );
		alg.setEllipse(ellipse);
		alg.process(p);

		assertEquals(alg.getClosest().x,found.x,GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(alg.getClosest().y,found.y,GrlConstants.DOUBLE_TEST_TOL);
	}
}
