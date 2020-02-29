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

package georegression.metric;

import georegression.geometry.UtilTrig_F64;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric2D_I32;
import georegression.struct.line.LineSegment2D_I32;
import georegression.struct.point.Point2D_I32;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestDistance2D_I32 {
	@Test
	void distance_line_segment() {
		// test inside the line
		double found = Distance2D_I32.distance( new LineSegment2D_I32( -2, 0, 3, 5 ), new Point2D_I32( 2, 0 ) );
		double expected = (double) UtilTrig_F64.distance(0, 2, 2, 0);
		assertEquals( expected, found, GrlConstants.TEST_F64);

		// test before the first end point
		found = Distance2D_I32.distance( new LineSegment2D_I32( -2, 0, 3, 5 ), new Point2D_I32( -5, -5 ) );
		expected = UtilTrig_F64.distance( -2, 0, -5, -5 );
		assertEquals( expected, found, GrlConstants.TEST_F64);

		// test after the second end point
		found = Distance2D_I32.distance( new LineSegment2D_I32( -2, 0, 3, 5 ), new Point2D_I32( 10, 0 ) );
		expected = UtilTrig_F64.distance( 3, 5, 10, 0 );
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test
	void distance_line_parametric() {
		double found = Distance2D_I32.distance( new LineParametric2D_I32( -2, 0, 5, 5 ), new Point2D_I32( 2, 0 ) );
		double expected = (double) UtilTrig_F64.distance(0, 2, 2, 0);
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test
	void distanceSq_line_parametric() {
		double found = Distance2D_I32.distanceSq( new LineParametric2D_I32( -2, 0, 5, 5 ), new Point2D_I32( 2, 0 ) );
		double expected = (double) UtilTrig_F64.distanceSq(0, 2, 2, 0);
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}
}
