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
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.metric;

import georegression.geometry.UtilLine2D_F64;
import georegression.geometry.UtilTrig_F64;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Peter Abeles
 */
public class TestDistance2D_F64 {

	@Test
	public void distance_parametric_line() {
		double found = Distance2D_F64.distance( new LineParametric2D_F64( -2, 0, 1, 1 ), new Point2D_F64( 4, -2 ) );
		double expected = (double) UtilTrig_F64.distance( 0, 2, 4, -2 );
		assertEquals( expected, found, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void distanceSq_parametric_line() {
		double found = Distance2D_F64.distanceSq( new LineParametric2D_F64( -2, 0, 1, 1 ), new Point2D_F64( 4, -2 ) );
		double expected = (double) UtilTrig_F64.distanceSq( 0, 2, 4, -2);
		assertEquals( expected, found, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void distance_line_segment() {
		// test inside the line
		double found = Distance2D_F64.distance( new LineSegment2D_F64( -2, 0, 3, 5 ), new Point2D_F64( 2, 0 ) );
		double expected = (double) UtilTrig_F64.distance( 0, 2, 2, 0 );
		assertEquals( expected, found, GrlConstants.DOUBLE_TEST_TOL );

		// test before the first end point
		found = Distance2D_F64.distance( new LineSegment2D_F64( -2, 0, 3, 5 ), new Point2D_F64( -5, -5 ) );
		expected = UtilTrig_F64.distance( -2, 0, -5, -5 );
		assertEquals( expected, found, GrlConstants.DOUBLE_TEST_TOL );

		// test after the second end point
		found = Distance2D_F64.distance( new LineSegment2D_F64( -2, 0, 3, 5 ), new Point2D_F64( 10, 0 ) );
		expected = UtilTrig_F64.distance( 3, 5, 10, 0 );
		assertEquals( expected, found, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void distance_general_line() {
		// easier to cherry pick points in parametric notation
		LineParametric2D_F64 parametric = new LineParametric2D_F64( -2, 0, 1, 1 );
		// convert into general format
		LineGeneral2D_F64 general = UtilLine2D_F64.convert(parametric,(LineGeneral2D_F64)null);

		double found = Distance2D_F64.distance( general , new Point2D_F64( 4, -2 ) );
		double expected = (double) UtilTrig_F64.distance( 0, 2, 4, -2 );
		assertEquals(expected, found, GrlConstants.DOUBLE_TEST_TOL);
	}
}
