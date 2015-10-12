/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestArea2D_F64 {

	@Test
	public void triangle() {
		Point2D_F64 a = new Point2D_F64(0,0);
		Point2D_F64 b = new Point2D_F64(5,0);
		Point2D_F64 c = new Point2D_F64(0,3);

		double expected = 0.5*5*3;

		double found = Area2D_F64.triangle(a,b,c);

		assertEquals(expected,found,GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void quadrilateral() {
		Quadrilateral_F64 q = new Quadrilateral_F64(0,0,2,0,2,3,0,3);

		assertEquals(2*3,Area2D_F64.quadrilateral(q), GrlConstants.DOUBLE_TEST_TOL);

		// see if it can handle the convex case
		q = new Quadrilateral_F64(0,0,2,0,1,1,0,4);

		double expected = Area2D_F64.triangle(q.a,q.b,q.c) + Area2D_F64.triangle(q.a,q.c,q.d);

		for (int i = 0; i < 4; i++) {
			assertEquals(expected,Area2D_F64.quadrilateral(q), GrlConstants.DOUBLE_TEST_TOL);
			Point2D_F64 tmp = q.a;
			q.a = q.b;
			q.b = q.c;
			q.c = q.d;
			q.d = tmp;
		}
	}

	/**
	 * Test convex caused for the simple polygon area algorithm
	 */
	@Test
	public void polygonSimple_convex() {
		Polygon2D_F64 t = new Polygon2D_F64(0,0,5,0,0,3);
		assertEquals(0.5*5*3,Area2D_F64.polygonSimple(t), GrlConstants.DOUBLE_TEST_TOL);

		Polygon2D_F64 q = new Polygon2D_F64(0,0,2,0,2,3,0,3);
		assertEquals(2*3,Area2D_F64.polygonSimple(q), GrlConstants.DOUBLE_TEST_TOL);

		Polygon2D_F64 p = new Polygon2D_F64(0,0,2,0,2,3,1,5,0,3);
		double pt = Area2D_F64.triangle(p.get(2),p.get(3),p.get(4));
		assertEquals(2*3+pt,Area2D_F64.polygonSimple(p), GrlConstants.DOUBLE_TEST_TOL);

		t.flip();
		assertEquals(0.5 * 5 * 3, Area2D_F64.polygonSimple(t), GrlConstants.DOUBLE_TEST_TOL);

		q.flip();
		assertEquals(2 * 3, Area2D_F64.polygonSimple(q), GrlConstants.DOUBLE_TEST_TOL);

		p.flip();
		assertEquals(2*3+pt, Area2D_F64.polygonSimple(p), GrlConstants.DOUBLE_TEST_TOL);
	}

	/**
	 * Test concave caused for the simple polygon area algorithm
	 */
	@Test
	public void polygonSimple_concave() {
		Polygon2D_F64 t_inside = new Polygon2D_F64(5,5, 3,3, 0,5);
		double area_inside = Area2D_F64.polygonSimple(t_inside);

		Polygon2D_F64 t = new Polygon2D_F64(0,0, 5,0, 5,5, 3,3, 0,5);
		assertEquals(5*5-area_inside,Area2D_F64.polygonSimple(t), GrlConstants.DOUBLE_TEST_TOL);

		double area_q_full = Area2D_F64.polygonSimple(new Polygon2D_F64(0,3, 5,5, 5,0));
		Polygon2D_F64 q = new Polygon2D_F64(0,3, 5,5,  3,3, 5,0);
		assertEquals(area_q_full - area_inside,Area2D_F64.polygonSimple(q), GrlConstants.DOUBLE_TEST_TOL);

		t.flip();
		assertEquals(5*5-area_inside, Area2D_F64.polygonSimple(t), GrlConstants.DOUBLE_TEST_TOL);

		q.flip();
		assertEquals(area_q_full - area_inside, Area2D_F64.polygonSimple(q), GrlConstants.DOUBLE_TEST_TOL);
	}

}
