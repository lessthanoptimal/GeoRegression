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

import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Polygon2D_I32;
import georegression.struct.shapes.Rectangle2D_I32;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public class TestUtilPolygons2D_I32 {

	Random rand = new Random(234);

	@Test
	void isConvex() {
		Polygon2D_I32 a = new Polygon2D_I32(0, 0, 5, 5, -5, 5);
		assertTrue(UtilPolygons2D_I32.isConvex(a));
		a.flip();
		assertTrue(UtilPolygons2D_I32.isConvex(a));

		Polygon2D_I32 b = new Polygon2D_I32(0, 0, 0, 5, -5, 5, -5, 0);
		assertTrue(UtilPolygons2D_I32.isConvex(b));
		b.flip();
		assertTrue(UtilPolygons2D_I32.isConvex(b));

		Polygon2D_I32 c = new Polygon2D_I32(0, 0, 0, 5, -5, 5, -1, 4);
		assertFalse(UtilPolygons2D_I32.isConvex(c));
		c.flip();
		assertFalse(UtilPolygons2D_I32.isConvex(c));
	}

	@Test
	void flip() {

		// less than 3 has undrfined behavior

		Polygon2D_I32 poly = new Polygon2D_I32(3);
		List<Point2D_I32> orig = new ArrayList<>();
		orig.addAll(poly.vertexes.toList());

		UtilPolygons2D_I32.flip(poly);
		assertSame(orig.get(0), poly.get(0));
		assertSame(orig.get(1), poly.get(2));
		assertSame(orig.get(2), poly.get(1));

		poly = new Polygon2D_I32(4);
		orig.clear();
		orig.addAll(poly.vertexes.toList());

		UtilPolygons2D_I32.flip(poly);
		assertSame(orig.get(0), poly.get(0));
		assertSame(orig.get(1), poly.get(3));
		assertSame(orig.get(2), poly.get(2));
		assertSame(orig.get(3), poly.get(1));

		poly = new Polygon2D_I32(5);
		orig.clear();
		orig.addAll(poly.vertexes.toList());

		UtilPolygons2D_I32.flip(poly);
		assertSame(orig.get(0), poly.get(0));
		assertSame(orig.get(1), poly.get(4));
		assertSame(orig.get(2), poly.get(3));
		assertSame(orig.get(3), poly.get(2));
		assertSame(orig.get(4), poly.get(1));
	}

	@Test
	void isIdentical_poly_poly() {
		Polygon2D_I32 poly1 = new Polygon2D_I32(1, 2, 3, 4, 5, 6);
		Polygon2D_I32 poly2 = new Polygon2D_I32(1, 2, 3, 4, 5, 6);

		assertTrue(UtilPolygons2D_I32.isIdentical(poly1, poly2));
		poly2.get(2).x += 1;
		assertFalse(UtilPolygons2D_I32.isIdentical(poly1, poly2));
	}

	@Test
	void isEquivalent_poly_poly() {
		Polygon2D_I32 poly1 = new Polygon2D_I32(1, 2, 3, 4, 5, 6);
		Polygon2D_I32 poly2 = new Polygon2D_I32(1, 2, 3, 4, 5, 6);

		// create a shifted version of poly2
		for (int i = 0; i < poly1.size(); i++) {
			Polygon2D_I32 poly3 = new Polygon2D_I32(poly1.size());
			for (int j = 0; j < poly1.size(); j++) {
				poly3.vertexes.data[j] = poly2.vertexes.data[(j+i)%poly1.size()];
			}
			assertTrue(UtilPolygons2D_I32.isEquivalent(poly1,poly3));
		}
	}

	@Test
	void bounding_points_rect() {
		List<Point2D_I32> poly = new ArrayList<Point2D_I32>();

		for( int trial = 0; trial < 10; trial++ ) {
			poly.clear();
			for( int i = 0; i < 20; i++ ) {
				int x = rand.nextInt(5)-2;
				int y = rand.nextInt(5)-2;

				poly.add(new Point2D_I32(x, y));
			}

			Rectangle2D_I32 rectangle = new Rectangle2D_I32();
			UtilPolygons2D_I32.bounding(poly,rectangle);

			for( int i = 0; i < 20; i++ ) {
				Point2D_I32 p = poly.get(i);

				if( p.x < rectangle.x0 || p.y < rectangle.y0 || p.x >= rectangle.x1 || p.y >= rectangle.y1)
					fail("Failed");
			}
		}
	}

	@Test
	void bounding_poly_rect() {
		Polygon2D_I32 poly = new Polygon2D_I32();

		for( int trial = 0; trial < 10; trial++ ) {
			poly.vertexes.reset();
			for( int i = 0; i < 20; i++ ) {
				int x = rand.nextInt(5)-2;
				int y = rand.nextInt(5)-2;

				poly.vertexes.grow().setTo(x,y);
			}

			Rectangle2D_I32 rectangle = new Rectangle2D_I32();
			UtilPolygons2D_I32.bounding(poly,rectangle);

			for( int i = 0; i < 20; i++ ) {
				Point2D_I32 p = poly.vertexes.get(i);

				if( p.x < rectangle.x0 || p.y < rectangle.y0 || p.x >= rectangle.x1 || p.y >= rectangle.y1)
					fail("Failed");
			}
		}
	}

	@Test
	void isCCW() {
		// check convex case
		List<Point2D_I32> list = new ArrayList<Point2D_I32>();
		list.add(new Point2D_I32(2, 2));
		list.add(new Point2D_I32(4, 2));
		list.add(new Point2D_I32(4, 4));
		assertTrue(UtilPolygons2D_I32.isCCW(list));
		assertFalse(UtilPolygons2D_I32.isCCW(reverse(list)));

		// check concave case
		list.add(new Point2D_I32(2, 4));
		list.add(new Point2D_I32(3, 3));
		assertTrue(UtilPolygons2D_I32.isCCW(list));
		assertFalse(UtilPolygons2D_I32.isCCW(reverse(list)));
	}

	private static List<Point2D_I32> reverse(List<Point2D_I32> points) {
		List<Point2D_I32> reverse = new ArrayList<Point2D_I32>();

		for (int i = points.size() - 1; i >= 0; i--) {
			reverse.add(points.get(i));
		}

		return reverse;
	}

	@Test
	void isPositiveZ() {
		Point2D_I32 a = new Point2D_I32(2, 2);
		Point2D_I32 b = new Point2D_I32(4, 2);
		Point2D_I32 c = new Point2D_I32(4, 4);

		assertTrue(UtilPolygons2D_I32.isPositiveZ(c,b,a));
		assertFalse(UtilPolygons2D_I32.isPositiveZ(a,b,c));
	}

}
