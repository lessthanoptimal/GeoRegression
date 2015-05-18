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
import georegression.struct.shapes.Quadrilateral_F32;
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
		float found = Distance2D_F32.distanceSq(new LineParametric2D_F32(-2, 0, 1, 1), new Point2D_F32(4, -2));
		float expected = (float) UtilTrig_F32.distanceSq(0, 2, 4, -2);
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

	@Test
	public void distance_lineSegment_lineSegment() {
		// case of no intersection but one of the lines intersects inside
		LineSegment2D_F32 a = new LineSegment2D_F32(0,0,10,0);
		LineSegment2D_F32 b = new LineSegment2D_F32(5,2,5,10);

		assertEquals(2,Distance2D_F32.distance(a,b),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,Distance2D_F32.distance(b,a),GrlConstants.FLOAT_TEST_TOL);

		// the two lines intersect
		b.set(5, -1, 5, 1);
		assertEquals(0, Distance2D_F32.distance(a, b), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0, Distance2D_F32.distance(b, a), GrlConstants.FLOAT_TEST_TOL);

		// two lines are parallel but don't intersect
		b.set(12, 2, 2, 20);
		float expected = (float)Math.sqrt(2*2*2);
		assertEquals(expected, Distance2D_F32.distance(a, b), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected, Distance2D_F32.distance(b, a), GrlConstants.FLOAT_TEST_TOL);

		// general case where the end points are the closest
		//        one of these cases was tested above already
		b.set(5,-2,5,-10);
		assertEquals(2,Distance2D_F32.distance(a,b),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2,Distance2D_F32.distance(b,a),GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void distance_quadrilateral_point() {
		Quadrilateral_F32 quad = new Quadrilateral_F32(2,0, 2,10, 10,10, 10,0);

		// test a point to the left and right of a side.  should be the same
		assertEquals(3,Distance2D_F32.distance(quad,new Point2D_F32(-1,3)),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,Distance2D_F32.distance(quad,new Point2D_F32(5,3)),GrlConstants.FLOAT_TEST_TOL);

		// try the other sides
		assertEquals(4,Distance2D_F32.distance(quad,new Point2D_F32(5,14)),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(5,Distance2D_F32.distance(quad,new Point2D_F32(15,5)),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(6,Distance2D_F32.distance(quad,new Point2D_F32(6,-6)),GrlConstants.FLOAT_TEST_TOL);

	}
}
