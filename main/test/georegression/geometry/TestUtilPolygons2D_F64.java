/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.polygon.ThreeIndexes;
import georegression.geometry.polygon.TriangulateSimpleRemoveEars_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.Rectangle2D_F64;
import georegression.struct.shapes.RectangleLength2D_I32;
import org.ddogleg.struct.DogArray;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static georegression.misc.GrlConstants.TEST_F64;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public class TestUtilPolygons2D_F64 {

	@Test void isConvex() {
		Polygon2D_F64 a = new Polygon2D_F64(0, 0, 5, 5, -5, 5);
		assertTrue(UtilPolygons2D_F64.isConvex(a));
		a.flip();
		assertTrue(UtilPolygons2D_F64.isConvex(a));

		Polygon2D_F64 b = new Polygon2D_F64(0, 0, 0, 5, -5, 5, -5, 0);
		assertTrue(UtilPolygons2D_F64.isConvex(b));
		b.flip();
		assertTrue(UtilPolygons2D_F64.isConvex(b));

		Polygon2D_F64 c = new Polygon2D_F64(0, 0, 0, 5, -5, 5, -0.1, 4.5);
		assertFalse(UtilPolygons2D_F64.isConvex(c));
		c.flip();
		assertFalse(UtilPolygons2D_F64.isConvex(c));
	}

	@Test void isSimple() {
		PolygonInfo info = new PolygonInfo();
		double tol = UtilEjml.TEST_F64;

		// A line
		Polygon2D_F64 polyLine = new Polygon2D_F64(0, 0, 5, 5);
		assertSame(info,UtilPolygons2D_F64.isSimple(polyLine,info,tol));
		assertEquals(PolygonInfo.Type.COMPLEX,info.type);

		// Triangle
		Polygon2D_F64 a = new Polygon2D_F64(0, 0, 5, 5, -5, 5);
		assertSame(info,UtilPolygons2D_F64.isSimple(a,info,tol));
		assertEquals(PolygonInfo.Type.CONVEX,info.type);
		assertTrue(info.ccw);
		assertSame(info,UtilPolygons2D_F64.isSimple(a.flip(null),info,tol));
		assertEquals(PolygonInfo.Type.CONVEX,info.type);
		assertFalse(info.ccw);

		// bent quadrilateral
		Polygon2D_F64 b = new Polygon2D_F64(0, 0, 0, 5, -5, 5, -0.1, 4.5);
		assertSame(info,UtilPolygons2D_F64.isSimple(b,info,tol));
		assertEquals(PolygonInfo.Type.SIMPLE_CONCAVE,info.type);
		assertTrue(info.ccw);
		assertSame(info,UtilPolygons2D_F64.isSimple(b.flip(null),info,tol));
		assertEquals(PolygonInfo.Type.SIMPLE_CONCAVE,info.type);
		assertFalse(info.ccw);

		// |\/|
		// |__|
		Polygon2D_F64 c = new Polygon2D_F64(new double[][]{{0, 0}, {0, 5}, {-2.5,2.5}, {-5, 5}, {-5,0}});
		assertSame(info,UtilPolygons2D_F64.isSimple(c,info,tol));
		assertEquals(PolygonInfo.Type.SIMPLE_CONCAVE,info.type);
		assertTrue(info.ccw);
		assertSame(info,UtilPolygons2D_F64.isSimple(c.flip(null),info,tol));
		assertEquals(PolygonInfo.Type.SIMPLE_CONCAVE,info.type);
		assertFalse(info.ccw);

		// crossed
		Polygon2D_F64 d = new Polygon2D_F64(new double[][]{{0, 0}, {5, 5}, {0, 5}, {5,0}});
		assertSame(info,UtilPolygons2D_F64.isSimple(d,info,tol));
		assertEquals(PolygonInfo.Type.COMPLEX,info.type);
		assertSame(info,UtilPolygons2D_F64.isSimple(d.flip(null),info,tol));
		assertEquals(PolygonInfo.Type.COMPLEX,info.type);
		// not checking ccw since it's not well defined for complex
	}

	@Test void isSelfIntersectingBrute() {
		double tol = UtilEjml.TEST_F64;
		// triangle
		var a = new Polygon2D_F64(new double[][]{{0, 0}, {5, 5}, {-5, 5}});
		assertFalse(UtilPolygons2D_F64.isSelfIntersectingBrute(a, tol));
		assertFalse(UtilPolygons2D_F64.isSelfIntersectingBrute(a.flip(null), tol));

		// square
		a = new Polygon2D_F64(new double[][]{{0, 0}, {0, 5}, {-5, 5}, {-5,0}});
		assertFalse(UtilPolygons2D_F64.isSelfIntersectingBrute(a, tol));
		assertFalse(UtilPolygons2D_F64.isSelfIntersectingBrute(a.flip(null), tol));

		// |\/|
		// |__|
		a = new Polygon2D_F64(new double[][]{{0, 0}, {0, 5}, {-2.5,2.5}, {-5, 5}, {-5,0}});
		assertFalse(UtilPolygons2D_F64.isSelfIntersectingBrute(a, tol));
		assertFalse(UtilPolygons2D_F64.isSelfIntersectingBrute(a.flip(null), tol));

		// There is a string of colinear sides but it's a rectangle
		a = new Polygon2D_F64(new double[][]{{0, 0}, {0, 2}, {0, 3}, {0, 5}, {1,5}, {1,0}});
		assertFalse(UtilPolygons2D_F64.isSelfIntersectingBrute(a, tol));
		assertFalse(UtilPolygons2D_F64.isSelfIntersectingBrute(a.flip(null), tol));

		// Two sides are co-linear but none intersecting
		a = new Polygon2D_F64(new double[][]{{0, 0}, {0, 2}, {1, 2}, {0,3}, {0, 5}, {2,5}, {2,0}});
		assertFalse(UtilPolygons2D_F64.isSelfIntersectingBrute(a, tol));
		assertFalse(UtilPolygons2D_F64.isSelfIntersectingBrute(a.flip(null), tol));

		// overlapping line 3
		a = new Polygon2D_F64(new double[][]{{0, 0}, {0, 5}, {0, 1}});
		assertTrue(UtilPolygons2D_F64.isSelfIntersectingBrute(a, tol));
		assertTrue(UtilPolygons2D_F64.isSelfIntersectingBrute(a.flip(null), tol));

		// overlapping line 4
		a = new Polygon2D_F64(new double[][]{{0, 0}, {0, 5}, {0, 1}, {0, 5}});
		assertTrue(UtilPolygons2D_F64.isSelfIntersectingBrute(a, tol));
		assertTrue(UtilPolygons2D_F64.isSelfIntersectingBrute(a.flip(null), tol));

		// crossed
		a = new Polygon2D_F64(new double[][]{{0, 0}, {5, 5}, {0, 5}, {5,0}});
		assertTrue(UtilPolygons2D_F64.isSelfIntersectingBrute(a, tol));
		assertTrue(UtilPolygons2D_F64.isSelfIntersectingBrute(a.flip(null), tol));
		UtilPolygons2D_F64.shiftDown(a);
		assertTrue(UtilPolygons2D_F64.isSelfIntersectingBrute(a, tol));
		assertTrue(UtilPolygons2D_F64.isSelfIntersectingBrute(a.flip(null), tol));
		UtilPolygons2D_F64.shiftDown(a);
		assertTrue(UtilPolygons2D_F64.isSelfIntersectingBrute(a, tol));
		assertTrue(UtilPolygons2D_F64.isSelfIntersectingBrute(a.flip(null), tol));
	}

	/**
	 * Compares triangulate to the algorithm which it calls directly
	 */
	@Test void triangulate() {
		var original = new Polygon2D_F64(new double[][]{{0,0},{2,0},{0.5,0.5},{0,2}});
		var found = new DogArray<>(ThreeIndexes::new);
		var expected = new DogArray<>(ThreeIndexes::new);
		var alg = new TriangulateSimpleRemoveEars_F64();
		alg.process(original,expected);

		UtilPolygons2D_F64.triangulate(original, found);

		assertEquals(expected.size, found.size);
		for (int i = 0; i < expected.size; i++) {
			ThreeIndexes e = expected.get(i);
			ThreeIndexes f = found.get(i);

			assertEquals(e.idx0, f.idx0);
			assertEquals(e.idx1, f.idx1);
			assertEquals(e.idx2, f.idx2);
		}
	}

	@Test void convert_rectcorner_quad() {
		Rectangle2D_F64 r = new Rectangle2D_F64(1, 2, 5, 6);
		Quadrilateral_F64 q = new Quadrilateral_F64();

		UtilPolygons2D_F64.convert(r, q);

		assertEquals(1, q.a.x, TEST_F64);
		assertEquals(2, q.a.y, TEST_F64);
		assertEquals(5, q.b.x, TEST_F64);
		assertEquals(2, q.b.y, TEST_F64);
		assertEquals(5, q.c.x, TEST_F64);
		assertEquals(6, q.c.y, TEST_F64);
		assertEquals(1, q.d.x, TEST_F64);
		assertEquals(6, q.d.y, TEST_F64);
	}

	@Test void convert_rect_poly() {
		Rectangle2D_F64 r = new Rectangle2D_F64(1,2,5,6);
		Polygon2D_F64 p = new Polygon2D_F64(4);

		UtilPolygons2D_F64.convert(r, p);

		assertEquals(1, p.get(0).x, TEST_F64);
		assertEquals(2, p.get(0).y, TEST_F64);
		assertEquals(5, p.get(1).x, TEST_F64);
		assertEquals(2, p.get(1).y, TEST_F64);
		assertEquals(5, p.get(2).x, TEST_F64);
		assertEquals(6, p.get(2).y, TEST_F64);
		assertEquals(1, p.get(3).x, TEST_F64);
		assertEquals(6, p.get(3).y, TEST_F64);
	}

	@Test void convert_quad_poly() {
		Quadrilateral_F64 q = new Quadrilateral_F64(1,2,3,4,5,6,7,8);
		Polygon2D_F64 p = new Polygon2D_F64(4);

		UtilPolygons2D_F64.convert(q, p);

		assertTrue(p.get(0).distance(q.a) < TEST_F64);
		assertTrue(p.get(1).distance(q.b) < TEST_F64);
		assertTrue(p.get(2).distance(q.c) < TEST_F64);
		assertTrue(p.get(3).distance(q.d) < TEST_F64);
	}

	@Test void convert_poly_quad() {
		Polygon2D_F64 r = new Polygon2D_F64(1,2, 5,2, 5,6, 1,6);
		Quadrilateral_F64 q = new Quadrilateral_F64();

		UtilPolygons2D_F64.convert(r, q);

		assertTrue(r.get(0).distance(q.a)< TEST_F64);
		assertTrue(r.get(1).distance(q.b)< TEST_F64);
		assertTrue(r.get(2).distance(q.c)< TEST_F64);
		assertTrue(r.get(3).distance(q.d)< TEST_F64);
	}

	@Test void convert_rectwh_quad() {
		RectangleLength2D_I32 rect = new RectangleLength2D_I32(1, 2, 5, 6);

		Quadrilateral_F64 q = new Quadrilateral_F64();

		UtilPolygons2D_F64.convert(rect, q);

		assertEquals(1, q.a.x, TEST_F64);
		assertEquals(2, q.a.y, TEST_F64);
		assertEquals(5, q.b.x, TEST_F64);
		assertEquals(2, q.b.y, TEST_F64);
		assertEquals(5, q.c.x, TEST_F64);
		assertEquals(7, q.c.y, TEST_F64);
		assertEquals(1, q.d.x, TEST_F64);
		assertEquals(7, q.d.y, TEST_F64);
	}

	@Test void bounding_quadrilateral() {
		Quadrilateral_F64 q = new Quadrilateral_F64(3, 0, 2, -3, -2, 3, 1, 5);
		Rectangle2D_F64 out = new Rectangle2D_F64();

		UtilPolygons2D_F64.bounding(q, out);

		assertEquals(-2, out.p0.x, TEST_F64);
		assertEquals(-3, out.p0.y, TEST_F64);
		assertEquals(3, out.p1.x, TEST_F64);
		assertEquals(5, out.p1.y, TEST_F64);
	}

	@Test void bounding_polygon() {
		Polygon2D_F64 q = new Polygon2D_F64(3, 0, 2, -3, -2, 3, 1, 5);
		Rectangle2D_F64 out = new Rectangle2D_F64();

		UtilPolygons2D_F64.bounding(q, out);

		assertEquals(-2, out.p0.x, TEST_F64);
		assertEquals(-3, out.p0.y, TEST_F64);
		assertEquals(3, out.p1.x, TEST_F64);
		assertEquals(5, out.p1.y, TEST_F64);
	}

	@Test void center_quadrilateral() {
		Quadrilateral_F64 q = new Quadrilateral_F64(3, 0, 2, -3, -2, 3, 1, 5);

		Point2D_F64[] pts = new Point2D_F64[]{q.a, q.b, q.c, q.d};

		Point2D_F64 expected = new Point2D_F64();

		for (Point2D_F64 pt : pts) {
			expected.x += pt.x;
			expected.y += pt.y;
		}
		expected.x /= 4.0;
		expected.y /= 4.0;

		Point2D_F64 found = UtilPolygons2D_F64.center(q, null);

		assertEquals(expected.x, found.x, TEST_F64);
		assertEquals(expected.y, found.y, TEST_F64);
	}

	@Test void isCCW() {
		// check convex case
		List<Point2D_F64> list = new ArrayList<>();
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

	@Test void vertexAverage() {
		Polygon2D_F64 poly = new Polygon2D_F64(1, 2, 3, 4, 5, 6);

		Point2D_F64 ave = new Point2D_F64();

		UtilPolygons2D_F64.vertexAverage(poly, ave);

		assertEquals((1 + 3 + 5) / 3.0, ave.x, TEST_F64);
		assertEquals((2 + 4 + 6) / 3.0, ave.y, TEST_F64);
	}

	private static List<Point2D_F64> reverse(List<Point2D_F64> points) {
		List<Point2D_F64> reverse = new ArrayList<>();

		for (int i = points.size() - 1; i >= 0; i--) {
			reverse.add(points.get(i));
		}

		return reverse;
	}

	@Test void isIdentical_poly_poly() {
		Polygon2D_F64 poly1 = new Polygon2D_F64(1, 2, 3, 4, 5, 6);
		Polygon2D_F64 poly2 = new Polygon2D_F64(1, 2, 3, 4, 5, 6.1);

		assertTrue(UtilPolygons2D_F64.isIdentical(poly1, poly2, 0.11));
		assertFalse(UtilPolygons2D_F64.isIdentical(poly1, poly2, 0.09));
	}

	@Test void isEquivalent_poly_poly() {
		Polygon2D_F64 poly1 = new Polygon2D_F64(1, 2, 3, 4, 5, 6);
		Polygon2D_F64 poly2 = new Polygon2D_F64(1, 2, 3, 4, 5, 6);

		// create a shifted version of poly2
		for (int i = 0; i < poly1.size(); i++) {
			Polygon2D_F64 poly3 = new Polygon2D_F64(poly1.size());
			for (int j = 0; j < poly1.size(); j++) {
				poly3.vertexes.data[j] = poly2.vertexes.data[(j+i)%poly1.size()];
			}
			assertTrue(UtilPolygons2D_F64.isEquivalent(poly1,poly3, TEST_F64));
		}
	}

	@Test void flip() {
		// less than 3 has undefined behavior
		Polygon2D_F64 poly = new Polygon2D_F64(3);
		List<Point2D_F64> orig = new ArrayList<>(poly.vertexes.toList());

		UtilPolygons2D_F64.flip(poly);
		assertSame(orig.get(0), poly.get(0));
		assertSame(orig.get(1), poly.get(2));
		assertSame(orig.get(2), poly.get(1));

		poly = new Polygon2D_F64(4);
		orig.clear();
		orig.addAll(poly.vertexes.toList());

		UtilPolygons2D_F64.flip(poly);
		assertSame(orig.get(0), poly.get(0));
		assertSame(orig.get(1), poly.get(3));
		assertSame(orig.get(2), poly.get(2));
		assertSame(orig.get(3), poly.get(1));

		poly = new Polygon2D_F64(5);
		orig.clear();
		orig.addAll(poly.vertexes.toList());

		UtilPolygons2D_F64.flip(poly);
		assertSame(orig.get(0), poly.get(0));
		assertSame(orig.get(1), poly.get(4));
		assertSame(orig.get(2), poly.get(3));
		assertSame(orig.get(3), poly.get(2));
		assertSame(orig.get(4), poly.get(1));
	}

	@Test void shiftUp() {
		for (int i = 1; i <= 5; i++) {
			Polygon2D_F64 poly = new Polygon2D_F64(i);
			List<Point2D_F64> orig = new ArrayList<>(poly.vertexes.toList());

			UtilPolygons2D_F64.shiftUp(poly);
			for (int j = 0; j < i-1; j++) {
				assertSame(orig.get(j + 1), poly.get(j));
			}
			assertSame(orig.get(0), poly.get(i - 1));
		}
	}

	@Test void shiftDown() {
		for (int i = 1; i <= 5; i++) {
			Polygon2D_F64 poly = new Polygon2D_F64(i);
			List<Point2D_F64> orig = new ArrayList<>(poly.vertexes.toList());

			UtilPolygons2D_F64.shiftDown(poly);
			for (int j = 1; j < i; j++) {
				assertSame(orig.get(j - 1), poly.get(j));
			}
			assertSame(orig.get(i - 1), poly.get(0));
		}
	}

	@Test void removeAlmostParallel() {
		Polygon2D_F64 output = new Polygon2D_F64(5);

		output.get(0).setTo(0,0);
		output.get(1).setTo(2,0);
		output.get(2).setTo(4,0);
		output.get(3).setTo(4,5);
		output.get(4).setTo(0,5);

		UtilPolygons2D_F64.removeAlmostParallel(output, TEST_F64);

		assertEquals(4, output.size());
		assertEquals(0, output.get(0).x, TEST_F64);
		assertEquals(4, output.get(1).x, TEST_F64);
	}

	@Test void removeAdjacentDuplicates() {
		Polygon2D_F64 output = new Polygon2D_F64(9);

		output.get(0).setTo(0,0);
		output.get(1).setTo(0,0);
		output.get(2).setTo(2,0);
		output.get(3).setTo(4,0);
		output.get(4).setTo(4,0);
		output.get(5).setTo(4,0);
		output.get(6).setTo(4,5);
		output.get(7).setTo(0,5);
		output.get(8).setTo(0,5);

		UtilPolygons2D_F64.removeAdjacentDuplicates(output, TEST_F64);

		assertEquals(5, output.size());
		assertTrue(output.get(0).isIdentical(0,0));
		assertTrue(output.get(1).isIdentical(2,0));
		assertTrue(output.get(2).isIdentical(4,0));
		assertTrue(output.get(3).isIdentical(4,5));
		assertTrue(output.get(4).isIdentical(0,5));


		// test a pathological case
		output = new Polygon2D_F64(1);
		output.get(0).setTo(0,0);
		UtilPolygons2D_F64.removeAdjacentDuplicates(output, TEST_F64);
		assertEquals(1, output.size());
	}

	@Test void hasAdjacentDuplicates() {
		Polygon2D_F64 output = new Polygon2D_F64(0,0,1,1,2,2,3,3,0,0);

		assertTrue(UtilPolygons2D_F64.hasAdjacentDuplicates(output, TEST_F64));

		output = new Polygon2D_F64(0,0,0,0,2,2,3,3);
		assertTrue(UtilPolygons2D_F64.hasAdjacentDuplicates(output, TEST_F64));

		output = new Polygon2D_F64(0,0,1,1,2,2,2,2);
		assertTrue(UtilPolygons2D_F64.hasAdjacentDuplicates(output, TEST_F64));

		output = new Polygon2D_F64(0,0,1,1,2,2,3,3);
		assertFalse(UtilPolygons2D_F64.hasAdjacentDuplicates(output, TEST_F64));

		output = new Polygon2D_F64(0,0);
		assertFalse(UtilPolygons2D_F64.hasAdjacentDuplicates(output, TEST_F64));
	}

	@Test void getSideLength() {
		Polygon2D_F64 output = new Polygon2D_F64(3);

		output.get(0).setTo(0,0);
		output.get(1).setTo(2,0);
		output.get(2).setTo(2,3);

		assertEquals(2,output.getSideLength(0), TEST_F64);
		assertEquals(3,output.getSideLength(1), TEST_F64);
		assertEquals(Math.sqrt(2*2+3*3),output.getSideLength(2), TEST_F64);

	}

	@Test void averageOfClosestPointError() {
		Polygon2D_F64 a = new Polygon2D_F64(4);

		a.get(0).setTo(1,1);
		a.get(1).setTo(4,1);
		a.get(2).setTo(4,4);
		a.get(3).setTo(1,4);

		assertEquals(0,UtilPolygons2D_F64.averageOfClosestPointError(a,a,100),TEST_F64);

		// same polygon just rotated order of points
		Polygon2D_F64 b = new Polygon2D_F64(4);
		b.get(0).setTo(1,4);
		b.get(1).setTo(1,1);
		b.get(2).setTo(4,1);
		b.get(3).setTo(4,4);

		assertEquals(0,UtilPolygons2D_F64.averageOfClosestPointError(a,b,100),TEST_F64);

		// make b different from a
		b.get(2).setTo(8,1);
		b.get(3).setTo(8,4);

		double errorAB = UtilPolygons2D_F64.averageOfClosestPointError(a,b,100);
		double errorBA = UtilPolygons2D_F64.averageOfClosestPointError(b,a,100);

		assertTrue(errorAB>errorBA);
	}
}
