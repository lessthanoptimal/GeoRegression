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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.Rectangle2D_F64;
import georegression.struct.shapes.RectangleLength2D_I32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestUtilPolygons2D_F64 {

	@Test
	public void convert_rectcorner_quad() {
		Rectangle2D_F64 r = new Rectangle2D_F64(1,2,5,6);
		Quadrilateral_F64 q = new Quadrilateral_F64();

		UtilPolygons2D_F64.convert(r,q);

		assertEquals( 1,q.a.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 2,q.a.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 5,q.b.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 2,q.b.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 5,q.c.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 6,q.c.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 1,q.d.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 6,q.d.y, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void convert_rectwh_quad() {
		RectangleLength2D_I32 rect = new RectangleLength2D_I32(1,2,5,6);

		Quadrilateral_F64 q = new Quadrilateral_F64();

		UtilPolygons2D_F64.convert(rect,q);

		assertEquals( 1,q.a.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 2,q.a.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 5,q.b.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 2,q.b.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 5,q.c.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 7,q.c.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 1,q.d.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 7,q.d.y, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void bounding_quadrilateral() {
		Quadrilateral_F64 q = new Quadrilateral_F64(3,0,2,-3,-2,3,1,5);
		Rectangle2D_F64 out = new Rectangle2D_F64();

		UtilPolygons2D_F64.bounding(q,out);

		assertEquals(-2,out.p0.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(-3,out.p0.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 3,out.p1.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 5,out.p1.y, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void center_quadrilateral() {
		Quadrilateral_F64 q = new Quadrilateral_F64(3,0,2,-3,-2,3,1,5);

		Point2D_F64 pts[] = new Point2D_F64[]{q.a,q.b,q.c,q.d};

		Point2D_F64 expected = new Point2D_F64();

		for (int i = 0; i < pts.length; i++) {
			expected.x += pts[i].x;
			expected.y += pts[i].y;
		}
		expected.x /= 4.0;
		expected.y /= 4.0;

		Point2D_F64 found = UtilPolygons2D_F64.center(q,null);

		assertEquals(expected.x,found.x,GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(expected.y,found.y,GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void isCCW() {
		// check convex case
		List<Point2D_F64> list = new ArrayList<Point2D_F64>();
		list.add(new Point2D_F64(1, 1));
		list.add(new Point2D_F64(2, 1));
		list.add(new Point2D_F64(2, 2));
		assertTrue(UtilPolygons2D_F64.isCCW(list));
		assertFalse(UtilPolygons2D_F64.isCCW(reverse(list)));

		// check concave case
		list.add(new Point2D_F64(1, 2));
		list.add(new Point2D_F64(1.5, 1.5));
		assertTrue(UtilPolygons2D_F64.isCCW(list));
		assertFalse(UtilPolygons2D_F64.isCCW(reverse(list)));
	}

	@Test
	public void reverseOrder() {

		Polygon2D_F64 poly3 = new Polygon2D_F64(3);
		Polygon2D_F64 poly4 = new Polygon2D_F64(4);

		List<Point2D_F64> orig3 = new ArrayList<Point2D_F64>();
		List<Point2D_F64> orig4 = new ArrayList<Point2D_F64>();

		orig3.addAll(poly3.vertexes.toList());
		orig4.addAll(poly4.vertexes.toList());

		UtilPolygons2D_F64.reverseOrder(poly3);
		UtilPolygons2D_F64.reverseOrder(poly4);

		assertTrue(poly3.get(0)==orig3.get(0));
		assertTrue(poly3.get(1)==orig3.get(2));
		assertTrue(poly3.get(2)==orig3.get(1));

		assertTrue(poly4.get(0)==orig4.get(0));
		assertTrue(poly4.get(1)==orig4.get(3));
		assertTrue(poly4.get(2)==orig4.get(2));
		assertTrue(poly4.get(3)==orig4.get(1));
	}

	private static List<Point2D_F64> reverse( List<Point2D_F64> points ) {
		List<Point2D_F64> reverse = new ArrayList<Point2D_F64>();

		for (int i = points.size()-1; i >= 0; i--) {
			reverse.add( points.get(i));
		}

		return reverse;
	}
}
