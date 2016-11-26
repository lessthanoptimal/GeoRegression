/*
 * Copyright (C) 2011-2016, Peter Abeles. All Rights Reserved.
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

import georegression.metric.Intersection2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.Polygon2D_F32;
import georegression.struct.shapes.Quadrilateral_F32;
import georegression.struct.shapes.Rectangle2D_F32;
import georegression.struct.shapes.RectangleLength2D_I32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static georegression.misc.GrlConstants.FLOAT_TEST_TOL;
import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestUtilPolygons2D_F32 {

	Random rand = new Random(234);

	@Test
	public void isConvex() {
		Polygon2D_F32 a = new Polygon2D_F32(0, 0, 5, 5, -5, 5);
		assertTrue(UtilPolygons2D_F32.isConvex(a));
		a.flip();
		assertTrue(UtilPolygons2D_F32.isConvex(a));

		Polygon2D_F32 b = new Polygon2D_F32(0, 0, 0, 5, -5, 5, -5, 0);
		assertTrue(UtilPolygons2D_F32.isConvex(b));
		b.flip();
		assertTrue(UtilPolygons2D_F32.isConvex(b));

		Polygon2D_F32 c = new Polygon2D_F32(0, 0, 0, 5, -5, 5, -0.1f, 4.5f);
		assertFalse(UtilPolygons2D_F32.isConvex(c));
		c.flip();
		assertFalse(UtilPolygons2D_F32.isConvex(c));
	}


	@Test
	public void convert_rectcorner_quad() {
		Rectangle2D_F32 r = new Rectangle2D_F32(1, 2, 5, 6);
		Quadrilateral_F32 q = new Quadrilateral_F32();

		UtilPolygons2D_F32.convert(r, q);

		assertEquals(1, q.a.x, FLOAT_TEST_TOL);
		assertEquals(2, q.a.y, FLOAT_TEST_TOL);
		assertEquals(5, q.b.x, FLOAT_TEST_TOL);
		assertEquals(2, q.b.y, FLOAT_TEST_TOL);
		assertEquals(5, q.c.x, FLOAT_TEST_TOL);
		assertEquals(6, q.c.y, FLOAT_TEST_TOL);
		assertEquals(1, q.d.x, FLOAT_TEST_TOL);
		assertEquals(6, q.d.y, FLOAT_TEST_TOL);
	}

	@Test
	public void convert_rect_poly() {
		Rectangle2D_F32 r = new Rectangle2D_F32(1,2,5,6);
		Polygon2D_F32 p = new Polygon2D_F32(4);

		UtilPolygons2D_F32.convert(r, p);

		assertEquals(1, p.get(0).x, FLOAT_TEST_TOL);
		assertEquals(2, p.get(0).y, FLOAT_TEST_TOL);
		assertEquals(5, p.get(1).x, FLOAT_TEST_TOL);
		assertEquals(2, p.get(1).y, FLOAT_TEST_TOL);
		assertEquals(5, p.get(2).x, FLOAT_TEST_TOL);
		assertEquals(6, p.get(2).y, FLOAT_TEST_TOL);
		assertEquals(1, p.get(3).x, FLOAT_TEST_TOL);
		assertEquals(6, p.get(3).y, FLOAT_TEST_TOL);
	}

	@Test
	public void convert_quad_poly() {
		Quadrilateral_F32 q = new Quadrilateral_F32(1,2,3,4,5,6,7,8);
		Polygon2D_F32 p = new Polygon2D_F32(4);

		UtilPolygons2D_F32.convert(q, p);

		assertTrue(p.get(0).distance(q.a) < FLOAT_TEST_TOL);
		assertTrue(p.get(1).distance(q.b) < FLOAT_TEST_TOL);
		assertTrue(p.get(2).distance(q.c) < FLOAT_TEST_TOL);
		assertTrue(p.get(3).distance(q.d) < FLOAT_TEST_TOL);
	}

	@Test
	public void convert_poly_quad() {
		Polygon2D_F32 r = new Polygon2D_F32(1,2, 5,2, 5,6, 1,6);
		Quadrilateral_F32 q = new Quadrilateral_F32();

		UtilPolygons2D_F32.convert(r, q);

		assertTrue(r.get(0).distance(q.a)< FLOAT_TEST_TOL);
		assertTrue(r.get(1).distance(q.b)< FLOAT_TEST_TOL);
		assertTrue(r.get(2).distance(q.c)< FLOAT_TEST_TOL);
		assertTrue(r.get(3).distance(q.d)< FLOAT_TEST_TOL);
	}

	@Test
	public void convert_rectwh_quad() {
		RectangleLength2D_I32 rect = new RectangleLength2D_I32(1, 2, 5, 6);

		Quadrilateral_F32 q = new Quadrilateral_F32();

		UtilPolygons2D_F32.convert(rect, q);

		assertEquals(1, q.a.x, FLOAT_TEST_TOL);
		assertEquals(2, q.a.y, FLOAT_TEST_TOL);
		assertEquals(5, q.b.x, FLOAT_TEST_TOL);
		assertEquals(2, q.b.y, FLOAT_TEST_TOL);
		assertEquals(5, q.c.x, FLOAT_TEST_TOL);
		assertEquals(7, q.c.y, FLOAT_TEST_TOL);
		assertEquals(1, q.d.x, FLOAT_TEST_TOL);
		assertEquals(7, q.d.y, FLOAT_TEST_TOL);
	}

	@Test
	public void bounding_quadrilateral() {
		Quadrilateral_F32 q = new Quadrilateral_F32(3, 0, 2, -3, -2, 3, 1, 5);
		Rectangle2D_F32 out = new Rectangle2D_F32();

		UtilPolygons2D_F32.bounding(q, out);

		assertEquals(-2, out.p0.x, FLOAT_TEST_TOL);
		assertEquals(-3, out.p0.y, FLOAT_TEST_TOL);
		assertEquals(3, out.p1.x, FLOAT_TEST_TOL);
		assertEquals(5, out.p1.y, FLOAT_TEST_TOL);
	}

	@Test
	public void center_quadrilateral() {
		Quadrilateral_F32 q = new Quadrilateral_F32(3, 0, 2, -3, -2, 3, 1, 5);

		Point2D_F32 pts[] = new Point2D_F32[]{q.a, q.b, q.c, q.d};

		Point2D_F32 expected = new Point2D_F32();

		for (int i = 0; i < pts.length; i++) {
			expected.x += pts[i].x;
			expected.y += pts[i].y;
		}
		expected.x /= 4.0f;
		expected.y /= 4.0f;

		Point2D_F32 found = UtilPolygons2D_F32.center(q, null);

		assertEquals(expected.x, found.x, FLOAT_TEST_TOL);
		assertEquals(expected.y, found.y, FLOAT_TEST_TOL);
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
	public void vertexAverage() {

		Polygon2D_F32 poly = new Polygon2D_F32(1, 2, 3, 4, 5, 6);

		Point2D_F32 ave = new Point2D_F32();

		UtilPolygons2D_F32.vertexAverage(poly, ave);

		assertEquals((1 + 3 + 5) / 3.0f, ave.x, FLOAT_TEST_TOL);
		assertEquals((2 + 4 + 6) / 3.0f, ave.y, FLOAT_TEST_TOL);
	}

	private static List<Point2D_F32> reverse(List<Point2D_F32> points) {
		List<Point2D_F32> reverse = new ArrayList<Point2D_F32>();

		for (int i = points.size() - 1; i >= 0; i--) {
			reverse.add(points.get(i));
		}

		return reverse;
	}

	@Test
	public void isIdentical_poly_poly() {
		Polygon2D_F32 poly1 = new Polygon2D_F32(1, 2, 3, 4, 5, 6);
		Polygon2D_F32 poly2 = new Polygon2D_F32(1, 2, 3, 4, 5, 6.1f);

		assertTrue(UtilPolygons2D_F32.isIdentical(poly1, poly2, 0.11f));
		assertFalse(UtilPolygons2D_F32.isIdentical(poly1, poly2, 0.09f));
	}

	@Test
	public void isEquivalent_poly_poly() {
		Polygon2D_F32 poly1 = new Polygon2D_F32(1, 2, 3, 4, 5, 6);
		Polygon2D_F32 poly2 = new Polygon2D_F32(1, 2, 3, 4, 5, 6);

		// create a shifted version of poly2
		for (int i = 0; i < poly1.size(); i++) {
			Polygon2D_F32 poly3 = new Polygon2D_F32(poly1.size());
			for (int j = 0; j < poly1.size(); j++) {
				poly3.vertexes.data[j] = poly2.vertexes.data[(j+i)%poly1.size()];
			}
			assertTrue(UtilPolygons2D_F32.isEquivalent(poly1,poly3, FLOAT_TEST_TOL));
		}
	}

	@Test
	public void flip() {

		// less than 3 has undrfined behavior

		Polygon2D_F32 poly = new Polygon2D_F32(3);
		List<Point2D_F32> orig = new ArrayList<Point2D_F32>();
		orig.addAll(poly.vertexes.toList());

		UtilPolygons2D_F32.flip(poly);
		assertTrue(orig.get(0)==poly.get(0));
		assertTrue(orig.get(1)==poly.get(2));
		assertTrue(orig.get(2)==poly.get(1));

		poly = new Polygon2D_F32(4);
		orig.clear();
		orig.addAll(poly.vertexes.toList());

		UtilPolygons2D_F32.flip(poly);
		assertTrue(orig.get(0)==poly.get(0));
		assertTrue(orig.get(1)==poly.get(3));
		assertTrue(orig.get(2)==poly.get(2));
		assertTrue(orig.get(3)==poly.get(1));

		poly = new Polygon2D_F32(5);
		orig.clear();
		orig.addAll(poly.vertexes.toList());

		UtilPolygons2D_F32.flip(poly);
		assertTrue(orig.get(0)==poly.get(0));
		assertTrue(orig.get(1)==poly.get(4));
		assertTrue(orig.get(2)==poly.get(3));
		assertTrue(orig.get(3)==poly.get(2));
		assertTrue(orig.get(4)==poly.get(1));
	}

	@Test
	public void shiftUp() {
		for (int i = 1; i <= 5; i++) {
			Polygon2D_F32 poly = new Polygon2D_F32(i);
			List<Point2D_F32> orig = new ArrayList<Point2D_F32>();
			orig.addAll(poly.vertexes.toList());

			UtilPolygons2D_F32.shiftUp(poly);
			for (int j = 0; j < i-1; j++) {
				assertTrue(orig.get(j+1) == poly.get(j));
			}
			assertTrue(orig.get(0) == poly.get(i-1));
		}
	}

	@Test
	public void shiftDown() {
		for (int i = 1; i <= 5; i++) {
			Polygon2D_F32 poly = new Polygon2D_F32(i);
			List<Point2D_F32> orig = new ArrayList<Point2D_F32>();
			orig.addAll(poly.vertexes.toList());

			UtilPolygons2D_F32.shiftDown(poly);
			for (int j = 1; j < i; j++) {
				assertTrue(orig.get(j-1) == poly.get(j));
			}
			assertTrue(orig.get(i-1) == poly.get(0));
		}
	}

	/**
	 * Simple and not exhaustive test of convex hull.  The low level algorithm class has a more
	 * detailed implementation.
	 */
	@Test
	public void convexHull() {
		Polygon2D_F32 output = new Polygon2D_F32();

		for (int numPoints = 10; numPoints < 20; numPoints++) {

			List<Point2D_F32> data = new ArrayList<Point2D_F32>();
			for (int i = 0; i < numPoints; i++) {
				float x = (float)rand.nextGaussian()*5;
				float y = (float)rand.nextGaussian()*5;

				data.add( new Point2D_F32(x,y) );
			}

			UtilPolygons2D_F32.convexHull(data, output);

			// check some of the properties of the convex hull
			assertTrue(output.size() <= numPoints);
			assertTrue(output.isConvex());

			for (int i = 0; i < numPoints; i++) {
				Intersection2D_F32.containConvex(output, data.get(i));
			}
		}
	}

	@Test
	public void removeAlmostParallel() {
		Polygon2D_F32 output = new Polygon2D_F32(5);

		output.get(0).set(0,0);
		output.get(1).set(2,0);
		output.get(2).set(4,0);
		output.get(3).set(4,5);
		output.get(4).set(0,5);

		UtilPolygons2D_F32.removeAlmostParallel(output, FLOAT_TEST_TOL);

		assertEquals(4, output.size());
		assertEquals(0, output.get(0).x, FLOAT_TEST_TOL);
		assertEquals(4, output.get(1).x, FLOAT_TEST_TOL);
	}
}
