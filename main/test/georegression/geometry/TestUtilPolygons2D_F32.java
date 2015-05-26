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
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.Polygon2D_F32;
import georegression.struct.shapes.Quadrilateral_F32;
import georegression.struct.shapes.Rectangle2D_F32;
import georegression.struct.shapes.RectangleLength2D_I32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestUtilPolygons2D_F32 {

	@Test
	public void convert_rectcorner_quad() {
		Rectangle2D_F32 r = new Rectangle2D_F32(1,2,5,6);
		Quadrilateral_F32 q = new Quadrilateral_F32();

		UtilPolygons2D_F32.convert(r,q);

		assertEquals( 1,q.a.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 2,q.a.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 5,q.b.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 2,q.b.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 5,q.c.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 6,q.c.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 1,q.d.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 6,q.d.y, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_rectwh_quad() {
		RectangleLength2D_I32 rect = new RectangleLength2D_I32(1,2,5,6);

		Quadrilateral_F32 q = new Quadrilateral_F32();

		UtilPolygons2D_F32.convert(rect,q);

		assertEquals( 1,q.a.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 2,q.a.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 5,q.b.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 2,q.b.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 5,q.c.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 7,q.c.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 1,q.d.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 7,q.d.y, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void bounding_quadrilateral() {
		Quadrilateral_F32 q = new Quadrilateral_F32(3,0,2,-3,-2,3,1,5);
		Rectangle2D_F32 out = new Rectangle2D_F32();

		UtilPolygons2D_F32.bounding(q,out);

		assertEquals(-2,out.p0.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(-3,out.p0.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 3,out.p1.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( 5,out.p1.y, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void center_quadrilateral() {
		Quadrilateral_F32 q = new Quadrilateral_F32(3,0,2,-3,-2,3,1,5);

		Point2D_F32 pts[] = new Point2D_F32[]{q.a,q.b,q.c,q.d};

		Point2D_F32 expected = new Point2D_F32();

		for (int i = 0; i < pts.length; i++) {
			expected.x += pts[i].x;
			expected.y += pts[i].y;
		}
		expected.x /= 4.0f;
		expected.y /= 4.0f;

		Point2D_F32 found = UtilPolygons2D_F32.center(q,null);

		assertEquals(expected.x,found.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.y,found.y,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void isCCW() {
		// check convex case
		List<Point2D_F32> list = new ArrayList<Point2D_F32>();
		list.add(new Point2D_F32(1, 1));
		list.add(new Point2D_F32(2, 1));
		list.add(new Point2D_F32(2, 2));
		assertTrue(UtilPolygons2D_F32.isCCW(list));
		assertFalse(UtilPolygons2D_F32.isCCW(reverse(list)));

		// check concave case
		list.add(new Point2D_F32(1, 2));
		list.add(new Point2D_F32(1.5f, 1.5f));
		assertTrue(UtilPolygons2D_F32.isCCW(list));
		assertFalse(UtilPolygons2D_F32.isCCW(reverse(list)));
	}

	@Test
	public void reverseOrder() {

		Polygon2D_F32 poly3 = new Polygon2D_F32(3);
		Polygon2D_F32 poly4 = new Polygon2D_F32(4);

		List<Point2D_F32> orig3 = new ArrayList<Point2D_F32>();
		List<Point2D_F32> orig4 = new ArrayList<Point2D_F32>();

		orig3.addAll(poly3.vertexes.toList());
		orig4.addAll(poly4.vertexes.toList());

		UtilPolygons2D_F32.reverseOrder(poly3);
		UtilPolygons2D_F32.reverseOrder(poly4);

		assertTrue(poly3.get(0)==orig3.get(0));
		assertTrue(poly3.get(1)==orig3.get(2));
		assertTrue(poly3.get(2)==orig3.get(1));

		assertTrue(poly4.get(0)==orig4.get(0));
		assertTrue(poly4.get(1)==orig4.get(3));
		assertTrue(poly4.get(2)==orig4.get(2));
		assertTrue(poly4.get(3)==orig4.get(1));
	}

	@Test
	public void vertexAverage() {

		Polygon2D_F32 poly = new Polygon2D_F32(1,2,3,4,5,6);

		Point2D_F32 ave = new Point2D_F32();

		UtilPolygons2D_F32.vertexAverage(poly,ave);

		assertEquals((1+3+5)/3.0f,ave.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals((2+4+6)/3.0f,ave.y,GrlConstants.FLOAT_TEST_TOL);
	}

	private static List<Point2D_F32> reverse( List<Point2D_F32> points ) {
		List<Point2D_F32> reverse = new ArrayList<Point2D_F32>();

		for (int i = points.size()-1; i >= 0; i--) {
			reverse.add( points.get(i));
		}

		return reverse;
	}
}
