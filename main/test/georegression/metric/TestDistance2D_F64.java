/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.UtilLine2D_F64;
import georegression.geometry.UtilTrig_F64;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseRotated_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
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
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test
	public void distanceSq_parametric_line() {
		double found = Distance2D_F64.distanceSq(new LineParametric2D_F64(-2, 0, 1, 1), new Point2D_F64(4, -2));
		double expected = (double) UtilTrig_F64.distanceSq(0, 2, 4, -2);
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test
	public void distanceSq_parametric_line_points() {
		double found = Distance2D_F64.distanceSq(new LineParametric2D_F64(-2, 0, 1, 1), 4, -2);
		double expected = (double) UtilTrig_F64.distanceSq(0, 2, 4, -2);
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test
	public void distance_line_segment() {
		// test inside the line
		double found = Distance2D_F64.distance( new LineSegment2D_F64( -2, 0, 3, 5 ), new Point2D_F64( 2, 0 ) );
		double expected = (double) UtilTrig_F64.distance( 0, 2, 2, 0 );
		assertEquals( expected, found, GrlConstants.TEST_F64);

		// test before the first end point
		found = Distance2D_F64.distance( new LineSegment2D_F64( -2, 0, 3, 5 ), new Point2D_F64( -5, -5 ) );
		expected = UtilTrig_F64.distance( -2, 0, -5, -5 );
		assertEquals( expected, found, GrlConstants.TEST_F64);

		// test after the second end point
		found = Distance2D_F64.distance( new LineSegment2D_F64( -2, 0, 3, 5 ), new Point2D_F64( 10, 0 ) );
		expected = UtilTrig_F64.distance( 3, 5, 10, 0 );
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test
	public void distanceSq_line_segment() {
		// test inside the line
		double found = Distance2D_F64.distanceSq( new LineSegment2D_F64( -2, 0, 3, 5 ), new Point2D_F64( 2, 0 ) );
		double expected = (double) UtilTrig_F64.distanceSq( 0, 2, 2, 0 );
		assertEquals( expected, found, GrlConstants.TEST_F64);

		// test before the first end point
		found = Distance2D_F64.distanceSq( new LineSegment2D_F64( -2, 0, 3, 5 ), new Point2D_F64( -5, -5 ) );
		expected = UtilTrig_F64.distanceSq( -2, 0, -5, -5 );
		assertEquals( expected, found, GrlConstants.TEST_F64);

		// test after the second end point
		found = Distance2D_F64.distanceSq( new LineSegment2D_F64( -2, 0, 3, 5 ), new Point2D_F64( 10, 0 ) );
		expected = UtilTrig_F64.distanceSq( 3, 5, 10, 0 );
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test
	public void distanceSq_line_segment_param() {
		// test inside the line
		double found = Distance2D_F64.distanceSq( new LineSegment2D_F64( -2, 0, 3, 5 ),  2, 0  );
		double expected = (double) UtilTrig_F64.distanceSq( 0, 2, 2, 0 );
		assertEquals( expected, found, GrlConstants.TEST_F64);

		// test before the first end point
		found = Distance2D_F64.distanceSq( new LineSegment2D_F64( -2, 0, 3, 5 ),  -5, -5 );
		expected = UtilTrig_F64.distanceSq( -2, 0, -5, -5 );
		assertEquals( expected, found, GrlConstants.TEST_F64);

		// test after the second end point
		found = Distance2D_F64.distanceSq( new LineSegment2D_F64( -2, 0, 3, 5 ), 10, 0  );
		expected = UtilTrig_F64.distanceSq( 3, 5, 10, 0 );
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test
	public void distance_general_line() {
		// easier to cherry pick points in parametric notation
		LineParametric2D_F64 parametric = new LineParametric2D_F64( -2, 0, 1, 1 );
		// convert into general format
		LineGeneral2D_F64 general = UtilLine2D_F64.convert(parametric,(LineGeneral2D_F64)null);

		double found = Distance2D_F64.distance( general , new Point2D_F64( 4, -2 ) );
		double expected = (double) UtilTrig_F64.distance( 0, 2, 4, -2 );
		assertEquals(expected, found, GrlConstants.TEST_F64);
	}

	@Test
	public void distance_generalNorm_line() {
		// easier to cherry pick points in parametric notation
		LineParametric2D_F64 parametric = new LineParametric2D_F64( -2, 0, 1, 1 );
		// convert into general format
		LineGeneral2D_F64 general = UtilLine2D_F64.convert(parametric,(LineGeneral2D_F64)null);
		general.normalize();

		// test a point and its reflection.  Should be same distance and positive
		double found = Distance2D_F64.distanceNorm(general, new Point2D_F64(4, -2));
		double expected = (double) UtilTrig_F64.distance( 0, 2, 4, -2 );
		assertEquals(expected, found, GrlConstants.TEST_F64);

		// the reflection should also be positive
		found = Distance2D_F64.distanceNorm(general, new Point2D_F64(-4, 6));
		assertEquals(expected, found, GrlConstants.TEST_F64);
	}

	@Test
	public void distance_lineSegment_lineSegment() {
		// case of no intersection but one of the lines intersects inside
		LineSegment2D_F64 a = new LineSegment2D_F64(0,0,10,0);
		LineSegment2D_F64 b = new LineSegment2D_F64(5,2,5,10);

		assertEquals(2,Distance2D_F64.distance(a,b),GrlConstants.TEST_F64);
		assertEquals(2,Distance2D_F64.distance(b,a),GrlConstants.TEST_F64);

		// the two lines intersect
		b.set(5, -1, 5, 1);
		assertEquals(0, Distance2D_F64.distance(a, b), GrlConstants.TEST_F64);
		assertEquals(0, Distance2D_F64.distance(b, a), GrlConstants.TEST_F64);

		// two lines are parallel but don't intersect
		b.set(12, 2, 2, 20);
		double expected = Math.sqrt(2*2*2);
		assertEquals(expected, Distance2D_F64.distance(a, b), GrlConstants.TEST_F64);
		assertEquals(expected, Distance2D_F64.distance(b, a), GrlConstants.TEST_F64);

		// general case where the end points are the closest
		//        one of these cases was tested above already
		b.set(5,-2,5,-10);
		assertEquals(2,Distance2D_F64.distance(a,b),GrlConstants.TEST_F64);
		assertEquals(2,Distance2D_F64.distance(b,a),GrlConstants.TEST_F64);
	}

	@Test
	public void distance_quadrilateral_point() {
		Quadrilateral_F64 quad = new Quadrilateral_F64(2,0, 2,10, 10,10, 10,0);

		// test a point to the left and right of a side.  should be the same
		assertEquals(3,Distance2D_F64.distance(quad,new Point2D_F64(-1,3)),GrlConstants.TEST_F64);
		assertEquals(3,Distance2D_F64.distance(quad,new Point2D_F64(5,3)),GrlConstants.TEST_F64);

		// try the other sides
		assertEquals(4,Distance2D_F64.distance(quad,new Point2D_F64(5,14)),GrlConstants.TEST_F64);
		assertEquals(5,Distance2D_F64.distance(quad,new Point2D_F64(15,5)),GrlConstants.TEST_F64);
		assertEquals(6,Distance2D_F64.distance(quad,new Point2D_F64(6,-6)),GrlConstants.TEST_F64);
	}

	@Test
	public void distance_polygon_point() {
		Polygon2D_F64 poly = new Polygon2D_F64(2,0, 2,10, 10,10, 10,0);

		// test a point to the left and right of a side.  should be the same
		assertEquals(3,Distance2D_F64.distance(poly,new Point2D_F64(-1,3)),GrlConstants.TEST_F64);
		assertEquals(3,Distance2D_F64.distance(poly,new Point2D_F64(5,3)),GrlConstants.TEST_F64);

		// try the other sides
		assertEquals(4,Distance2D_F64.distance(poly,new Point2D_F64(5,14)),GrlConstants.TEST_F64);
		assertEquals(5,Distance2D_F64.distance(poly,new Point2D_F64(15,5)),GrlConstants.TEST_F64);
		assertEquals(6,Distance2D_F64.distance(poly,new Point2D_F64(6,-6)),GrlConstants.TEST_F64);
	}

	@Test
	public void distanceOrigin_LineParametric() {
		LineParametric2D_F64 line = new LineParametric2D_F64(2.3,-9.5,2,-3.1);

		double expected = Distance2D_F64.distance(line,new Point2D_F64());
		double found = Distance2D_F64.distanceOrigin(line);

		assertEquals(expected,found,GrlConstants.TEST_F64);
	}

	@Test
	public void distance_ellipserotated_point() {
		EllipseRotated_F64 ellipse = new EllipseRotated_F64(4,5,4,3, GrlConstants.PId2);

		assertEquals(0, Distance2D_F64.distance(ellipse,new Point2D_F64(4+3,5)), GrlConstants.TEST_F64);
		assertEquals(0, Distance2D_F64.distance(ellipse,new Point2D_F64(4-3,5)), GrlConstants.TEST_F64);

		assertEquals(0, Distance2D_F64.distance(ellipse,new Point2D_F64(4,5-4)), GrlConstants.TEST_F64);
		assertEquals(0, Distance2D_F64.distance(ellipse,new Point2D_F64(4,5+4)), GrlConstants.TEST_F64);

		assertEquals(1, Distance2D_F64.distance(ellipse,new Point2D_F64(4+2,5)), GrlConstants.TEST_F64);
		assertEquals(1, Distance2D_F64.distance(ellipse,new Point2D_F64(4-2,5)), GrlConstants.TEST_F64);

		assertEquals(1.1, Distance2D_F64.distance(ellipse,new Point2D_F64(4+4.1,5)), GrlConstants.TEST_F64);
		assertEquals(1.1, Distance2D_F64.distance(ellipse,new Point2D_F64(4-4.1,5)), GrlConstants.TEST_F64);
	}
}
