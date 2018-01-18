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

package georegression.geometry.algs;

import georegression.geometry.UtilPolygons2D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.shapes.Polygon2D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestAreaIntersectionPolygon_F32 {
	/**
	 * Unit tests provided by the original author
	 */
	@Test
	public void fromOriginalAuthor() {
		float a1[][] = {{2,3}, {2,3}, {2,3}, {2,4}, {3,3}, {2,3}, {2,3}};
		float b1[][] = {{1,1}, {1,4}, {4,4}, {4,1}, {1,1}}; // 1/2, 1/2
		// The redundant vertices above are to provoke errors
		// as good test cases should.
		// It is not necessary to duplicate the first vertex at the end.

		float a2[][] = {{1,7}, {4,7}, {4, 6}, {2,6}, {2, 3}, {4,3}, {4,2}, {1,2}};
		float b2[][] = {{3,1}, {5,1}, {5,4}, {3,4}, {3,5}, {6,5}, {6,0}, {3,0}}; // 0, 9

		float a3[][] = {{1,1}, {1,2}, {2,1}, {2,2}};
		float b3[][] = {{0,0}, {0,4}, {4,4}, {4,0}}; // 0, 1/2

		float a4[][] = {{0,0}, {3,0}, {3,2}, {1,2}, {1,1}, {2,1}, {2,3}, {0,3}};
		float b4[][] = {{0,0}, {0,4}, {4,4}, {4,0}}; // -9, 11

		float a5[][] = {{0,0}, {1,0}, {0,1}};
		float b5[][] = {{0,0}, {0,1}, {1,1}, {1,0}}; // -1/2, 1/2

		float a6[][] = {{1, 3} , {2, 3} , {2, 0} , {1, 0} };
		float b6[][] = {{0, 1} , {3, 1} , {3, 2} , {0, 2} }; // -1, 3

		float a7[][] = {{0,0}, {0,2}, {2,2}, {2,0}};
		float b7[][] = {{1, 1}, {3, 1}, {3, 3}, {1, 3}}; // -1, 4

		float a8[][] = {{0,0}, {0,4}, {4,4}, {4,0}};
		float b8[][] = {{1,1}, {1,2}, {2,2}, {2,1}}; // 1, 16

		check(a1,b1,0.5f,0.5f);
		check(a2,b2,0,9);
		check(a3,b3,0,0.5f);
		check(a4,b4,-9,11);
		check(a5,b5,-0.5f,0.5f);
		check(a6,b6,-1,3);
		check(a7,b7,-1,4);
		check(a8,b8,1,16);
	}

	private void check( float[][] a , float[][] b, float expectedA , float expectedB ) {

		AreaIntersectionPolygon2D_F32 alg = new AreaIntersectionPolygon2D_F32();

		float foundA = alg.computeArea(convert(a),convert(b));
		float foundB = alg.computeArea(convert(a),convert(a));

		assertEquals( foundA , expectedA, GrlConstants.TEST_SQ_F32);
		assertEquals( foundB , expectedB, GrlConstants.TEST_SQ_F32);
	}

	private static Polygon2D_F32 convert( float[][] a ) {
		Polygon2D_F32 p = new Polygon2D_F32(a.length);

		for (int i = 0; i < a.length; i++) {
			p.set(i, a[i][0], a[i][1]);
		}
		return p;
	}

	/**
	 * Unit test which attempts to explain why negative area is possible. I think it has to do with the ordering
	 * of vertexes
	 */
	@Test
	public void whyNegative() {
		AreaIntersectionPolygon2D_F32 alg = new AreaIntersectionPolygon2D_F32();

		Polygon2D_F32 A = new Polygon2D_F32(new float[][]{{0,0},{2,0},{2,4},{0,4}});
		Polygon2D_F32 B = A.copy();

		float found0 = alg.computeArea(A,B);
		assertEquals( 2*4, found0, GrlConstants.TEST_SQ_F32);

		UtilPolygons2D_F32.shiftDown(B);
		float found1 = alg.computeArea(A,B);
		assertEquals( 2*4, found1, GrlConstants.TEST_SQ_F32);

		B.flip();
		float found2 = alg.computeArea(A,B);
		assertEquals( -2*4, found2, GrlConstants.TEST_SQ_F32);

	}
}