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

package georegression.metric;

import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Polygon2D_I32;
import georegression.struct.shapes.Rectangle2D_I32;
import georegression.struct.shapes.RectangleLength2D_I32;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public class TestIntersection2D_I32 {

	@Test
	void containConvex() {
		Polygon2D_I32 poly = new Polygon2D_I32(4);
		poly.vertexes.data[0].setTo(-10,-10);
		poly.vertexes.data[1].setTo(10, -10);
		poly.vertexes.data[2].setTo(10, 10);
		poly.vertexes.data[3].setTo(-10, 10);

		Point2D_I32 online = new Point2D_I32(10,-10);
		Point2D_I32 inside = new Point2D_I32(5,5);
		Point2D_I32 outside = new Point2D_I32(15,5);

		assertFalse(Intersection2D_I32.containsConvex(poly,online));
		assertTrue(Intersection2D_I32.containsConvex(poly,inside));
		assertFalse(Intersection2D_I32.containsConvex(poly,outside));

		// change the order of the vertexes
		poly.vertexes.data[0].setTo(-10, 10);
		poly.vertexes.data[1].setTo(10, 10);
		poly.vertexes.data[2].setTo(10, -10);
		poly.vertexes.data[3].setTo(-10,-10);

		assertFalse(Intersection2D_I32.containsConvex(poly,online));
		assertTrue(Intersection2D_I32.containsConvex(poly,inside));
		assertFalse(Intersection2D_I32.containsConvex(poly,outside));
	}

	@Test
	void containConcave_rectangle() {
		Polygon2D_I32 poly = new Polygon2D_I32(4);
		poly.vertexes.data[0].setTo(-1,-1);
		poly.vertexes.data[1].setTo(1, -1);
		poly.vertexes.data[2].setTo(1, 1);
		poly.vertexes.data[3].setTo(-1, 1);

		assertTrue(Intersection2D_I32.containsConcave(poly, new Point2D_I32(0,0)));

		// perimeter cases intentionally not handled here

		assertFalse(Intersection2D_I32.containsConcave(poly, new Point2D_I32(2,0)));
		assertFalse(Intersection2D_I32.containsConcave(poly, new Point2D_I32(-2,0)));
		assertFalse(Intersection2D_I32.containsConcave(poly, new Point2D_I32(0,2)));
		assertFalse(Intersection2D_I32.containsConcave(poly, new Point2D_I32(0,-2)));
	}

	@Test
	void containConcave_concave() {
		Polygon2D_I32 poly = new Polygon2D_I32(5);
		poly.vertexes.data[0].setTo(-10,-10);
		poly.vertexes.data[1].setTo( 0, 0);
		poly.vertexes.data[2].setTo(10, -10);
		poly.vertexes.data[3].setTo(10, 10);
		poly.vertexes.data[4].setTo(-10, 10);

		assertTrue(Intersection2D_I32.containsConcave(poly, new Point2D_I32(0,5)));
		assertTrue(Intersection2D_I32.containsConcave(poly, new Point2D_I32(-7,-2)));
		assertTrue(Intersection2D_I32.containsConcave(poly, new Point2D_I32(7,-2)));

		assertFalse(Intersection2D_I32.containsConcave(poly, new Point2D_I32(0,-5)));

		assertFalse(Intersection2D_I32.containsConcave(poly, new Point2D_I32(20,0)));
		assertFalse(Intersection2D_I32.containsConcave(poly, new Point2D_I32(-20,0)));
		assertFalse(Intersection2D_I32.containsConcave(poly, new Point2D_I32(0,20)));
		assertFalse(Intersection2D_I32.containsConcave(poly, new Point2D_I32(0,-20)));
	}
	
	@Test
	void intersects_rect_corners() {
		// check several positive cases
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,0,100,120),true);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(10,12,99,119),true);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(50,50,200,200),true);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(-10,-10,10,10),true);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(90,-10,105,1),true);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(90,5,105,105),true);

		// negative cases
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(200,200,300,305),false);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(-200,-200,-10,-10),false);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,-20,100,-5),false);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,125,100,130),false);

		// edge cases
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,0,0,0),false);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(100,120,100,120),false);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(-10,0,0,120),false);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(100,0,105,120),false);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,-10,100,0),false);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,120,100,125),false);
	}

	private void check( Rectangle2D_I32 a , Rectangle2D_I32 b , boolean expected ) {
		assertTrue(expected==Intersection2D_I32.intersects(a,b));
	}

	@Test
	void intersection_rect_corners() {
		// check several positive cases
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,0,100,120),
				new Rectangle2D_I32(0,0,100,120));
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(10,12,99,119),
				new Rectangle2D_I32(10,12,99,119));
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(50,50,200,200),
				new Rectangle2D_I32(50,50,100,120));
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(-10,-10,10,10),
				new Rectangle2D_I32(0,0,10,10));
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(90,-10,105,1),
				new Rectangle2D_I32(90,0,100,1));
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(90,5,105,105),
				new Rectangle2D_I32(90,5,100,105));

		// negative cases
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(200,200,300,305),null);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(-200,-200,-10,-10),null);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,-20,100,-5),null);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,125,100,130),null);

		// edge cases
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,0,0,0),null);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(100,120,100,120),null);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(-10,0,0,120),null);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(100,0,105,120),null);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,-10,100,0),null);
		check( new Rectangle2D_I32(0,0,100,120),new Rectangle2D_I32(0,120,100,125),null);
	}

	private void check( Rectangle2D_I32 a , Rectangle2D_I32 b , @Nullable Rectangle2D_I32 expected ) {
		if( expected == null ) {
			assertFalse(Intersection2D_I32.intersection(a, b, new Rectangle2D_I32()));
			return;
		}

		Rectangle2D_I32 found = new Rectangle2D_I32();
		assertTrue(Intersection2D_I32.intersection(a, b, found));

		assertEquals(expected.x0,found.x0);
		assertEquals(expected.x1,found.x1);
		assertEquals(expected.y0,found.y0);
		assertEquals(expected.y1,found.y1);
	}

	@Test
	void contains_rectLength_pt() {
		RectangleLength2D_I32 rect = new RectangleLength2D_I32(-10,-5,5,10);

		assertTrue(Intersection2D_I32.contains(rect,-10,-5));
		assertTrue(Intersection2D_I32.contains(rect,-6,4));

		assertFalse(Intersection2D_I32.contains(rect,-11,-5));
		assertFalse(Intersection2D_I32.contains(rect,-10,-6));
		assertFalse(Intersection2D_I32.contains(rect,-5,4));
		assertFalse(Intersection2D_I32.contains(rect,-6,5));
	}

	@Test
	void contains_rect_pt() {
		Rectangle2D_I32 rect = new Rectangle2D_I32(-10,-5,5,10);

		assertTrue(Intersection2D_I32.contains(rect,-10,-5));
		assertTrue(Intersection2D_I32.contains(rect,4,9));

		assertFalse(Intersection2D_I32.contains(rect,-11,-3));
		assertFalse(Intersection2D_I32.contains(rect,-5,-6));
		assertFalse(Intersection2D_I32.contains(rect, 5,-3));
		assertFalse(Intersection2D_I32.contains(rect,-5, 10));
	}
}
