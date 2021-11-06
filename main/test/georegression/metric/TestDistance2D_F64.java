/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.Rectangle2D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestDistance2D_F64 {

	@Test
	void distance_parametric_line_case0() {
		double found = Distance2D_F64.distance( new LineParametric2D_F64( -2, 0, 1, 1 ), new Point2D_F64( 4, -2 ) );
		double expected = (double) UtilTrig_F64.distance( 0, 2, 4, -2 );
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	/**
	 * Give it a very large slope
	 */
	@Test void distance_parametric_line_case1() {
		double found = Distance2D_F64.distance(
				new LineParametric2D_F64( 100,0,0,Double.MAX_VALUE),
				new Point2D_F64( 20, 20 ) );
		assertEquals( 80.0, found, GrlConstants.TEST_F64);
	}

	@Test void distance_parametric_line_points() {
		double found = Distance2D_F64.distance( new LineParametric2D_F64( -2, 0, 1, 1 ),  4, -2  );
		double expected = (double) UtilTrig_F64.distance( 0, 2, 4, -2 );
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test void distanceSq_parametric_line() {
		double found = Distance2D_F64.distanceSq(new LineParametric2D_F64(-2, 0, 1, 1), new Point2D_F64(4, -2));
		double expected = (double) UtilTrig_F64.distanceSq(0, 2, 4, -2);
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test void distanceSq_parametric_line_points() {
		double found = Distance2D_F64.distanceSq(new LineParametric2D_F64(-2, 0, 1, 1), 4, -2);
		double expected = (double) UtilTrig_F64.distanceSq(0, 2, 4, -2);
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test void distance_line_segment() {
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

	@Test void distanceSq_line_segment() {
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

	@Test void distance_line_segment_param() {
		// test inside the line
		double found = Distance2D_F64.distance( new LineSegment2D_F64( -2, 0, 3, 5 ), 2, 0 );
		double expected = (double) UtilTrig_F64.distance( 0, 2, 2, 0 );
		assertEquals( expected, found, GrlConstants.TEST_F64);

		// test before the first end point
		found = Distance2D_F64.distance( new LineSegment2D_F64( -2, 0, 3, 5 ), -5, -5 );
		expected = UtilTrig_F64.distance( -2, 0, -5, -5 );
		assertEquals( expected, found, GrlConstants.TEST_F64);

		// test after the second end point
		found = Distance2D_F64.distance( new LineSegment2D_F64( -2, 0, 3, 5 ), 10, 0 );
		expected = UtilTrig_F64.distance( 3, 5, 10, 0 );
		assertEquals( expected, found, GrlConstants.TEST_F64);
	}

	@Test void distanceSq_line_segment_param() {
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

	@Test void distance_general_line() {
		// easier to cherry pick points in parametric notation
		LineParametric2D_F64 parametric = new LineParametric2D_F64( -2, 0, 1, 1 );
		// convert into general format
		LineGeneral2D_F64 general = UtilLine2D_F64.convert(parametric,(LineGeneral2D_F64)null);

		double found = Distance2D_F64.distance( general , new Point2D_F64( 4, -2 ) );
		double expected = (double) UtilTrig_F64.distance( 0, 2, 4, -2 );
		assertEquals(expected, found, GrlConstants.TEST_F64);
	}

	@Test void distance_generalNorm_line() {
		// easier to cherry pick points in parametric notation
		LineParametric2D_F64 parametric = new LineParametric2D_F64( -2, 0, 1, 1 );
		// convert into general format
		LineGeneral2D_F64 general = UtilLine2D_F64.convert(parametric,(LineGeneral2D_F64)null);
		general.normalize();

		// test a point and its reflection. Should be same distance and positive
		double found = Distance2D_F64.distanceNorm(general, new Point2D_F64(4, -2));
		double expected = (double) UtilTrig_F64.distance( 0, 2, 4, -2 );
		assertEquals(expected, found, GrlConstants.TEST_F64);

		// the reflection should also be positive
		found = Distance2D_F64.distanceNorm(general, new Point2D_F64(-4, 6));
		assertEquals(expected, found, GrlConstants.TEST_F64);
	}

	@Test void distance_lineSegment_lineSegment() {
		// case of no intersection but one of the lines intersects inside
		LineSegment2D_F64 a = new LineSegment2D_F64(0,0,10,0);
		LineSegment2D_F64 b = new LineSegment2D_F64(5,2,5,10);

		assertEquals(2,Distance2D_F64.distance(a,b),GrlConstants.TEST_F64);
		assertEquals(2,Distance2D_F64.distance(b,a),GrlConstants.TEST_F64);

		// the two lines intersect
		b.setTo(5, -1, 5, 1);
		assertEquals(0, Distance2D_F64.distance(a, b), GrlConstants.TEST_F64);
		assertEquals(0, Distance2D_F64.distance(b, a), GrlConstants.TEST_F64);

		// two lines are parallel but don't intersect
		b.setTo(12, 2, 2, 20);
		double expected = Math.sqrt(2*2*2);
		assertEquals(expected, Distance2D_F64.distance(a, b), GrlConstants.TEST_F64);
		assertEquals(expected, Distance2D_F64.distance(b, a), GrlConstants.TEST_F64);

		// general case where the end points are the closest
		//        one of these cases was tested above already
		b.setTo(5,-2,5,-10);
		assertEquals(2,Distance2D_F64.distance(a,b),GrlConstants.TEST_F64);
		assertEquals(2,Distance2D_F64.distance(b,a),GrlConstants.TEST_F64);
	}

	@Test void distance_quadrilateral_point() {
		Quadrilateral_F64 quad = new Quadrilateral_F64(2,0, 2,10, 10,10, 10,0);

		// test a point to the left and right of a side. should be the same
		assertEquals(3,Distance2D_F64.distance(quad,new Point2D_F64(-1,3)),GrlConstants.TEST_F64);
		assertEquals(3,Distance2D_F64.distance(quad,new Point2D_F64(5,3)),GrlConstants.TEST_F64);

		// try the other sides
		assertEquals(4,Distance2D_F64.distance(quad,new Point2D_F64(5,14)),GrlConstants.TEST_F64);
		assertEquals(5,Distance2D_F64.distance(quad,new Point2D_F64(15,5)),GrlConstants.TEST_F64);
		assertEquals(6,Distance2D_F64.distance(quad,new Point2D_F64(6,-6)),GrlConstants.TEST_F64);
	}

	@Test void distance_polygon_point() {
		Polygon2D_F64 poly = new Polygon2D_F64(2,0, 2,10, 10,10, 10,0);

		// test a point to the left and right of a side. should be the same
		assertEquals(3,Distance2D_F64.distance(poly,new Point2D_F64(-1,3)),GrlConstants.TEST_F64);
		assertEquals(3,Distance2D_F64.distance(poly,new Point2D_F64(5,3)),GrlConstants.TEST_F64);

		// try the other sides
		assertEquals(4,Distance2D_F64.distance(poly,new Point2D_F64(5,14)),GrlConstants.TEST_F64);
		assertEquals(5,Distance2D_F64.distance(poly,new Point2D_F64(15,5)),GrlConstants.TEST_F64);
		assertEquals(6,Distance2D_F64.distance(poly,new Point2D_F64(6,-6)),GrlConstants.TEST_F64);
	}

	@Test void distanceOrigin_LineParametric() {
		LineParametric2D_F64 line = new LineParametric2D_F64(2.3,-9.5,2,-3.1);

		double expected = Distance2D_F64.distance(line,new Point2D_F64());
		double found = Distance2D_F64.distanceOrigin(line);

		assertEquals(expected,found,GrlConstants.TEST_F64);
	}

	@Test void distance_ellipserotated_point() {
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

	@Test void scoreIoU_Rectangle2D() {
		// area = 6
		Rectangle2D_F64 A = new Rectangle2D_F64(-1,-1,2,1);
		// area = 4
		Rectangle2D_F64 B = new Rectangle2D_F64(-2,-2,0,0);

		double expected = 1.0/(6+4-1);
		assertEquals(expected, Distance2D_F64.scoreIoU(A,B), UtilEjml.TEST_F64);

		// Test no intersection
		Rectangle2D_F64 C = new Rectangle2D_F64(10,-2,12,-1);
		assertEquals(0.0, Distance2D_F64.scoreIoU(A,C), UtilEjml.TEST_F64);
	}

	@Test void scoreIoU_SimplePolygon() {
		// Convex non-self intersecting polygon with an area of 12
		Polygon2D_F64 A = new Polygon2D_F64(new double[][]{{0,0},{4,0},{4,4},{2,2},{0,4}});
		// Square inside of A with an area of 4
		Polygon2D_F64 B = new Polygon2D_F64(new double[][]{{1,0},{3,0},{3,2},{1,2}});

		double expected = 4.0/(12.0 + 4.0 - 4.0);
		assertEquals(expected, Distance2D_F64.scoreIoU(A,B,null), UtilEjml.TEST_F64);

		// Test case with no intersection
		Polygon2D_F64 C = new Polygon2D_F64(new double[][]{{10,0},{13,0},{13,2},{10,2}});
		assertEquals(0.0, Distance2D_F64.scoreIoU(A,C,null), UtilEjml.TEST_F64);
	}
}
