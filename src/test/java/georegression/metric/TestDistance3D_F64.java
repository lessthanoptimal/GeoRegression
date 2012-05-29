/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.metric;

import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.point.Point3D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Peter Abeles
 */
public class TestDistance3D_F64 {

	@Test
	public void distance_line_line() {
		// test intersection
		LineParametric3D_F64 l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		LineParametric3D_F64 l1 = new LineParametric3D_F64(0,0,0,0,1,0);

		assertEquals(0,Distance3D_F64.distance( l0,l1 ), GrlConstants.DOUBLE_TEST_TOL );

		// test the lines separated over the z-axis
		l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		l1 = new LineParametric3D_F64(0,0,2,0,1,0);

		assertEquals(2,Distance3D_F64.distance( l0,l1 ), GrlConstants.DOUBLE_TEST_TOL );

		// test parallel but no closest point
		l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		l1 = new LineParametric3D_F64(0,0,2,1,0,0);

		assertEquals(2,Distance3D_F64.distance( l0,l1 ), GrlConstants.DOUBLE_TEST_TOL );

		// test identical lines
		l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		l1 = new LineParametric3D_F64(0,0,0,1,0,0);

		assertEquals(0,Distance3D_F64.distance( l0,l1 ), GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void distance_line_point() {
		// a point above the line
		LineParametric3D_F64 l = new LineParametric3D_F64(1,2,3,0,1,0);
		Point3D_F64 p = new Point3D_F64( 3 , 2 , 3);

		assertEquals(2,Distance3D_F64.distance( l,p ), GrlConstants.DOUBLE_TEST_TOL );

		// a point on the line
		l.getSlope().set( 1 , 0 , 0 );
		assertEquals(0,Distance3D_F64.distance( l,p ), GrlConstants.DOUBLE_TEST_TOL );
	}
}
