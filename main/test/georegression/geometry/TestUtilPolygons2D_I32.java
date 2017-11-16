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

package georegression.geometry;

import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Polygon2D_I32;
import georegression.struct.shapes.Rectangle2D_I32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestUtilPolygons2D_I32 {

	Random rand = new Random(234);

	@Test
	public void bounding_points_rect() {
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
	public void bounding_poly_rect() {
		Polygon2D_I32 poly = new Polygon2D_I32();

		for( int trial = 0; trial < 10; trial++ ) {
			poly.vertexes.reset();
			for( int i = 0; i < 20; i++ ) {
				int x = rand.nextInt(5)-2;
				int y = rand.nextInt(5)-2;

				poly.vertexes.grow().set(x,y);
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
	public void isCCW() {
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
	public void isPositiveZ() {
		Point2D_I32 a = new Point2D_I32(2, 2);
		Point2D_I32 b = new Point2D_I32(4, 2);
		Point2D_I32 c = new Point2D_I32(4, 4);

		assertTrue(UtilPolygons2D_I32.isPositiveZ(c,b,a));
		assertFalse(UtilPolygons2D_I32.isPositiveZ(a,b,c));
	}

}
