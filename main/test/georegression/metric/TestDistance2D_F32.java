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

import georegression.geometry.UtilLine2D_F32;
import georegression.geometry.UtilTrig_F32;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Peter Abeles
 */
public class TestDistance2D_F32 {

	@Test
	public void distance_parametric_line() {
		float found = Distance2D_F32.distance( new LineParametric2D_F32( -2, 0, 1, 1 ), new Point2D_F32( 4, -2 ) );
		float expected = (float) UtilTrig_F32.distance( 0, 2, 4, -2 );
		assertEquals( expected, found, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void distanceSq_parametric_line() {
		float found = Distance2D_F32.distanceSq( new LineParametric2D_F32( -2, 0, 1, 1 ), new Point2D_F32( 4, -2 ) );
		float expected = (float) UtilTrig_F32.distanceSq( 0, 2, 4, -2);
		assertEquals( expected, found, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void distance_line_segment() {
		// test inside the line
		float found = Distance2D_F32.distance( new LineSegment2D_F32( -2, 0, 3, 5 ), new Point2D_F32( 2, 0 ) );
		float expected = (float) UtilTrig_F32.distance( 0, 2, 2, 0 );
		assertEquals( expected, found, GrlConstants.FLOAT_TEST_TOL );

		// test before the first end point
		found = Distance2D_F32.distance( new LineSegment2D_F32( -2, 0, 3, 5 ), new Point2D_F32( -5, -5 ) );
		expected = UtilTrig_F32.distance( -2, 0, -5, -5 );
		assertEquals( expected, found, GrlConstants.FLOAT_TEST_TOL );

		// test after the second end point
		found = Distance2D_F32.distance( new LineSegment2D_F32( -2, 0, 3, 5 ), new Point2D_F32( 10, 0 ) );
		expected = UtilTrig_F32.distance( 3, 5, 10, 0 );
		assertEquals( expected, found, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void distance_general_line() {
		// easier to cherry pick points in parametric notation
		LineParametric2D_F32 parametric = new LineParametric2D_F32( -2, 0, 1, 1 );
		// convert into general format
		LineGeneral2D_F32 general = UtilLine2D_F32.convert(parametric,(LineGeneral2D_F32)null);

		float found = Distance2D_F32.distance( general , new Point2D_F32( 4, -2 ) );
		float expected = (float) UtilTrig_F32.distance( 0, 2, 4, -2 );
		assertEquals(expected, found, GrlConstants.FLOAT_TEST_TOL);
	}
}
