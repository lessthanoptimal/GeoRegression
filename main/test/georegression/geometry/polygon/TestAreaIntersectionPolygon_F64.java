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

package georegression.geometry.polygon;

import georegression.geometry.UtilPolygons2D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.shapes.Polygon2D_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestAreaIntersectionPolygon_F64 {
	/**
	 * Unit tests provided by the original author
	 */
	@Test void fromOriginalAuthor() {
		double[][] a1 = {{2,3}, {2,3}, {2,3}, {2,4}, {3,3}, {2,3}, {2,3}};
		double[][] b1 = {{1,1}, {1,4}, {4,4}, {4,1}, {1,1}}; // 1/2, 1/2
		// The redundant vertices above are to provoke errors
		// as good test cases should.
		// It is not necessary to duplicate the first vertex at the end.

		double[][] a2 = {{1,7}, {4,7}, {4, 6}, {2,6}, {2, 3}, {4,3}, {4,2}, {1,2}};
		double[][] b2 = {{3,1}, {5,1}, {5,4}, {3,4}, {3,5}, {6,5}, {6,0}, {3,0}}; // 0, 9

		double[][] a3 = {{1,1}, {1,2}, {2,1}, {2,2}};
		double[][] b3 = {{0,0}, {0,4}, {4,4}, {4,0}}; // 0, 1/2

		double[][] a4 = {{0,0}, {3,0}, {3,2}, {1,2}, {1,1}, {2,1}, {2,3}, {0,3}};
		double[][] b4 = {{0,0}, {0,4}, {4,4}, {4,0}}; // -9, 11

		double[][] a5 = {{0,0}, {1,0}, {0,1}};
		double[][] b5 = {{0,0}, {0,1}, {1,1}, {1,0}}; // -1/2, 1/2

		double[][] a6 = {{1, 3} , {2, 3} , {2, 0} , {1, 0} };
		double[][] b6 = {{0, 1} , {3, 1} , {3, 2} , {0, 2} }; // -1, 3

		double[][] a7 = {{0,0}, {0,2}, {2,2}, {2,0}};
		double[][] b7 = {{1, 1}, {3, 1}, {3, 3}, {1, 3}}; // -1, 4

		double[][] a8 = {{0,0}, {0,4}, {4,4}, {4,0}};
		double[][] b8 = {{1,1}, {1,2}, {2,2}, {2,1}}; // 1, 16

		check(a1,b1,0.5,0.5);
		check(a2,b2,0,9);
		check(a3,b3,0,0.5);
		check(a4,b4,-9,11);
		check(a5,b5,-0.5,0.5);
		check(a6,b6,-1,3);
		check(a7,b7,-1,4);
		check(a8,b8,1,16);
	}

	private void check( double[][] a , double[][] b, double expectedA , double expectedB ) {
		AreaIntersectionPolygon2D_F64 alg = new AreaIntersectionPolygon2D_F64();

		double foundA = alg.computeArea(convert(a),convert(b));
		double foundB = alg.computeArea(convert(a),convert(a));

		assertEquals( foundA , expectedA, GrlConstants.TEST_SQ_F64);
		assertEquals( foundB , expectedB, GrlConstants.TEST_SQ_F64);
	}

	private static Polygon2D_F64 convert( double[][] a ) {
		Polygon2D_F64 p = new Polygon2D_F64(a.length);

		for (int i = 0; i < a.length; i++) {
			p.set(i, a[i][0], a[i][1]);
		}
		return p;
	}

	/**
	 * Unit test which attempts to explain why negative area is possible. I think it has to do with the ordering
	 * of vertexes
	 */
	@Test void whyNegative() {
		AreaIntersectionPolygon2D_F64 alg = new AreaIntersectionPolygon2D_F64();

		Polygon2D_F64 A = new Polygon2D_F64(new double[][]{{0,0},{2,0},{2,4},{0,4}});
		Polygon2D_F64 B = A.copy();

		double found0 = alg.computeArea(A,B);
		assertEquals( 2*4, found0, GrlConstants.TEST_SQ_F64);

		UtilPolygons2D_F64.shiftDown(B);
		double found1 = alg.computeArea(A,B);
		assertEquals( 2*4, found1, GrlConstants.TEST_SQ_F64);

		B.flip();
		double found2 = alg.computeArea(A,B);
		assertEquals( -2*4, found2, GrlConstants.TEST_SQ_F64);
	}
}