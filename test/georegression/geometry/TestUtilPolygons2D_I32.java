/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.geometry;

import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Polygon2D_I32;
import georegression.struct.shapes.RectangleCorner2D_I32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.fail;

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

			RectangleCorner2D_I32 rectangle = new RectangleCorner2D_I32();
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

			RectangleCorner2D_I32 rectangle = new RectangleCorner2D_I32();
			UtilPolygons2D_I32.bounding(poly,rectangle);

			for( int i = 0; i < 20; i++ ) {
				Point2D_I32 p = poly.vertexes.get(i);

				if( p.x < rectangle.x0 || p.y < rectangle.y0 || p.x >= rectangle.x1 || p.y >= rectangle.y1)
					fail("Failed");
			}
		}
	}

}
