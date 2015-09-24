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
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.Polygon2D_F32;
import georegression.struct.shapes.Quadrilateral_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestArea2D_F32 {

	@Test
	public void triangle() {
		Point2D_F32 a = new Point2D_F32(0,0);
		Point2D_F32 b = new Point2D_F32(5,0);
		Point2D_F32 c = new Point2D_F32(0,3);

		float expected = 0.5f*5*3;

		float found = Area2D_F32.triangle(a,b,c);

		assertEquals(expected,found,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void quadrilateral() {
		Quadrilateral_F32 q = new Quadrilateral_F32(0,0,2,0,2,3,0,3);

		assertEquals(2*3,Area2D_F32.quadrilateral(q), GrlConstants.FLOAT_TEST_TOL);

		// see if it can handle the convex case
		q = new Quadrilateral_F32(0,0,2,0,1,1,0,4);

		float expected = Area2D_F32.triangle(q.a,q.b,q.c) + Area2D_F32.triangle(q.a,q.c,q.d);

		for (int i = 0; i < 4; i++) {
			assertEquals(expected,Area2D_F32.quadrilateral(q), GrlConstants.FLOAT_TEST_TOL);
			Point2D_F32 tmp = q.a;
			q.a = q.b;
			q.b = q.c;
			q.c = q.d;
			q.d = tmp;
		}
	}

	@Test
	public void polygonConvex() {
		Polygon2D_F32 t = new Polygon2D_F32(0,0,5,0,0,3);
		assertEquals(0.5f*5*3,Area2D_F32.polygonConvex(t), GrlConstants.FLOAT_TEST_TOL);

		Polygon2D_F32 q = new Polygon2D_F32(0,0,2,0,2,3,0,3);
		assertEquals(2*3,Area2D_F32.polygonConvex(q), GrlConstants.FLOAT_TEST_TOL);

		Polygon2D_F32 p = new Polygon2D_F32(0,0,2,0,2,3,1,5,0,3);
		float pt = Area2D_F32.triangle(p.get(2),p.get(3),p.get(4));
		assertEquals(2*3+pt,Area2D_F32.polygonConvex(p), GrlConstants.FLOAT_TEST_TOL);

		t.flip();
		assertEquals(0.5f * 5 * 3, Area2D_F32.polygonConvex(t), GrlConstants.FLOAT_TEST_TOL);

		q.flip();
		assertEquals(2 * 3, Area2D_F32.polygonConvex(q), GrlConstants.FLOAT_TEST_TOL);

		p.flip();
		assertEquals(2*3+pt, Area2D_F32.polygonConvex(p), GrlConstants.FLOAT_TEST_TOL);
	}

}
