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

import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Polygon2D_I32;
import georegression.struct.shapes.RectangleCorner2D_I32;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestIntersection2D_I32 {

	@Test
	public void containConvex() {
		Polygon2D_I32 poly = new Polygon2D_I32(4);
		poly.vertexes.data[0].set(-10,-10);
		poly.vertexes.data[1].set(10, -10);
		poly.vertexes.data[2].set(10, 10);
		poly.vertexes.data[3].set(-10, 10);

		Point2D_I32 online = new Point2D_I32(10,-10);
		Point2D_I32 inside = new Point2D_I32(5,5);
		Point2D_I32 outside = new Point2D_I32(15,5);

		assertFalse(Intersection2D_I32.containConvex(poly,online));
		assertTrue(Intersection2D_I32.containConvex(poly,inside));
		assertFalse(Intersection2D_I32.containConvex(poly,outside));

		// change the order of the vertexes
		poly.vertexes.data[0].set(-10, 10);
		poly.vertexes.data[1].set(10, 10);
		poly.vertexes.data[2].set(10, -10);
		poly.vertexes.data[3].set(-10,-10);

		assertFalse(Intersection2D_I32.containConvex(poly,online));
		assertTrue(Intersection2D_I32.containConvex(poly,inside));
		assertFalse(Intersection2D_I32.containConvex(poly,outside));
	}

	@Test
	public void containConcave_rectangle() {
		Polygon2D_I32 poly = new Polygon2D_I32(4);
		poly.vertexes.data[0].set(-1,-1);
		poly.vertexes.data[1].set(1, -1);
		poly.vertexes.data[2].set(1, 1);
		poly.vertexes.data[3].set(-1, 1);

		assertTrue(Intersection2D_I32.containConcave(poly, new Point2D_I32(0,0)));

		// perimeter cases intentionally not handled here

		assertFalse(Intersection2D_I32.containConcave(poly, new Point2D_I32(2,0)));
		assertFalse(Intersection2D_I32.containConcave(poly, new Point2D_I32(-2,0)));
		assertFalse(Intersection2D_I32.containConcave(poly, new Point2D_I32(0,2)));
		assertFalse(Intersection2D_I32.containConcave(poly, new Point2D_I32(0,-2)));
	}

	@Test
	public void containConcave_concave() {
		Polygon2D_I32 poly = new Polygon2D_I32(5);
		poly.vertexes.data[0].set(-10,-10);
		poly.vertexes.data[1].set( 0, 0);
		poly.vertexes.data[2].set(10, -10);
		poly.vertexes.data[3].set(10, 10);
		poly.vertexes.data[4].set(-10, 10);

		assertTrue(Intersection2D_I32.containConcave(poly, new Point2D_I32(0,5)));
		assertTrue(Intersection2D_I32.containConcave(poly, new Point2D_I32(-7,-2)));
		assertTrue(Intersection2D_I32.containConcave(poly, new Point2D_I32(7,-2)));

		assertFalse(Intersection2D_I32.containConcave(poly, new Point2D_I32(0,-5)));

		assertFalse(Intersection2D_I32.containConcave(poly, new Point2D_I32(20,0)));
		assertFalse(Intersection2D_I32.containConcave(poly, new Point2D_I32(-20,0)));
		assertFalse(Intersection2D_I32.containConcave(poly, new Point2D_I32(0,20)));
		assertFalse(Intersection2D_I32.containConcave(poly, new Point2D_I32(0,-20)));
	}
	
	@Test
	public void intersects_rect_corners() {
		// check several positive cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,0,100,120),true);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(10,12,99,119),true);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(50,50,200,200),true);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-10,-10,10,10),true);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(90,-10,105,1),true);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(90,5,105,105),true);

		// negative cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(200,200,300,305),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-200,-200,-10,-10),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,-20,100,-5),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,125,100,130),false);

		// edge cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,0,0,0),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(100,120,100,120),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-10,0,0,120),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(100,0,105,120),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,-10,100,0),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,120,100,125),false);
	}

	private void check( RectangleCorner2D_I32 a , RectangleCorner2D_I32 b , boolean expected ) {
		assertTrue(expected==Intersection2D_I32.intersects(a,b));
	}

	@Test
	public void intersection_rect_corners() {
		// check several positive cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,0,100,120),
				new RectangleCorner2D_I32(0,0,100,120));
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(10,12,99,119),
				new RectangleCorner2D_I32(10,12,99,119));
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(50,50,200,200),
				new RectangleCorner2D_I32(50,50,100,120));
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-10,-10,10,10),
				new RectangleCorner2D_I32(0,0,10,10));
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(90,-10,105,1),
				new RectangleCorner2D_I32(90,0,100,1));
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(90,5,105,105),
				new RectangleCorner2D_I32(90,5,100,105));

		// negative cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(200,200,300,305),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-200,-200,-10,-10),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,-20,100,-5),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,125,100,130),null);

		// edge cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,0,0,0),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(100,120,100,120),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-10,0,0,120),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(100,0,105,120),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,-10,100,0),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,120,100,125),null);
	}

	private void check( RectangleCorner2D_I32 a , RectangleCorner2D_I32 b , RectangleCorner2D_I32 expected ) {
		if( expected == null ) {
			assertFalse(Intersection2D_I32.intersection(a, b, null));
			return;
		}

		RectangleCorner2D_I32 found = new RectangleCorner2D_I32();
		assertTrue(Intersection2D_I32.intersection(a, b, found));

		assertEquals(expected.x0,found.x0);
		assertEquals(expected.x1,found.x1);
		assertEquals(expected.y0,found.y0);
		assertEquals(expected.y1,found.y1);
	}
}
