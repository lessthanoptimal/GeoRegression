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

package georegression.metric;

import georegression.fitting.ellipse.ClosestPointEllipse_F64;
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
		ClosestPointEllipse_F64 alg = new ClosestPointEllipse_F64( GrlConstants.DOUBLE_TEST_TOL , 100 );
		alg.setEllipse(ellipse);
		alg.process(p);

		assertEquals(alg.getClosest().x,found.x,GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(alg.getClosest().y,found.y,GrlConstants.DOUBLE_TEST_TOL);
	}
}
