/*
 * Copyright (C) 2025, Peter Abeles. All Rights Reserved.
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

package georegression.struct;

import georegression.GeoStandardJUnit;
import georegression.misc.GrlConstants;
import georegression.struct.shapes.Triangle2D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMesh2D_F64 extends GeoStandardJUnit {
	@Test void stripUnusedPoints() {
		var alg = new Mesh2D_F64();

		// shouldn't blow up if empty
		assertSame(alg, alg.stripUnusedPoints());

		// Add points, but no triangle
		alg.points.append(0, 0);
		alg.points.append(1, 0);
		assertSame(alg, alg.stripUnusedPoints());
		assertEquals(0, alg.points.size());

		// Add points and a triangle that uses them all
		alg.points.append(0, 0);
		alg.points.append(1, 0);
		alg.points.append(1, 1);
		alg.addTriangle(0, 1, 2);
		assertSame(alg, alg.stripUnusedPoints());
		assertEquals(3, alg.points.size());

		// Add a point that should be removed
		alg.points.append(2, 0);
		assertSame(alg, alg.stripUnusedPoints());
		assertEquals(3, alg.points.size());
	}

	@Test void triangleCount() {
		var alg = new Mesh2D_F64();
		assertEquals(0, alg.triangleCount());

		for (int i = 0; i < 3; i++) {
			alg.addTriangle(0, 0, 0);
			assertEquals(1 + i, alg.triangleCount());
		}
	}

	@Test void pointCount() {
		var alg = new Mesh2D_F64();
		assertEquals(0, alg.pointCount());

		for (int i = 0; i < 3; i++) {
			alg.points.append(1, 1);
			assertEquals(1 + i, alg.pointCount());
		}
	}

	@Test void addTriangle() {
		var alg = new Mesh2D_F64();

		for (int i = 0; i < 3; i++) {
			assertEquals(i*3, alg.addTriangle(i, 0, i + 1));
		}
		for (int i = 0; i < 3; i++) {
			int idx = i*3;
			assertEquals(i, alg.triangles.get(idx));
			assertEquals(0, alg.triangles.get(idx + 1));
			assertEquals(i + 1, alg.triangles.get(idx + 2));
		}
	}

	@Test void getTriangle_Coordinates() {
		var mesh = new Mesh2D_F64();
		mesh.points.append(0, 0);
		mesh.points.append(1, 0);
		mesh.points.append(1, 1);
		int ref = mesh.addTriangle(2, 1, 0);

		var triangle = new Triangle2D_F64();
		mesh.getTriangle(ref, triangle);
		assertEquals(0.0, triangle.v0.distance(1, 1), UtilEjml.TEST_F64);
		assertEquals(0.0, triangle.v1.distance(1, 0), UtilEjml.TEST_F64);
		assertEquals(0.0, triangle.v2.distance(0, 0), UtilEjml.TEST_F64);
	}

	// Adds a grid with no strict line segment intersections
	@Test void doEdgesIntersect_StrictOnly() {
		int rows = 6;
		int cols = 5;

		var mesh = new Mesh2D_F64();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				mesh.points.append(col - 2, row - 1);
			}
		}

		for (int row = 0; row < rows - 1; row++) {
			for (int col = 0; col < cols - 1; col++) {
				int p0 = row*cols + col;
				int p1 = row*cols + col + 1;
				int p2 = (row + 1)*cols + col + 1;
				int p3 = (row + 1)*cols + col;

				mesh.addTriangle(p0, p2, p1);
				mesh.addTriangle(p0, p2, p3);
			}
		}

		// Nothing should strictly intersect, just the end points
		assertFalse(mesh.doEdgesIntersect());
	}

	// A triangle will be added that has an edge which will cross over another
	@Test void doEdgesIntersect_Crossing() {
		var mesh = new Mesh2D_F64();
		mesh.points.append(0, 0);
		mesh.points.append(1, 0);
		mesh.points.append(1, 1);
		mesh.points.append(0, 1);

		mesh.addTriangle(0, 1, 2);
		mesh.addTriangle(0, 2, 3);

		assertFalse(mesh.doEdgesIntersect());

		mesh.addTriangle(0, 3, 1);
		assertTrue(mesh.doEdgesIntersect());
	}

	@Test void countPointsInTriangles() {
		var mesh = new Mesh2D_F64();
		mesh.points.append(0, 0);
		mesh.points.append(1, 0);
		mesh.points.append(1, 1);
		mesh.points.append(0, 1);

		// One point isn't referenced
		mesh.addTriangle(0, 1, 2);
		assertEquals(3, mesh.countPointsInTriangles());

		// All are referenced and some more than once
		mesh.addTriangle(0, 2, 3);
		assertEquals(4, mesh.countPointsInTriangles());
	}

	@Test void checkCircumcircleCCW() {
		var alg = new Mesh2D_F64();

		// nothing is there, so nothing can fail
		assertTrue(alg.checkCircumcircleCCW(GrlConstants.TEST_F64));

		// add two sets of points which are far apart
		alg.points.append(0, 0);
		alg.points.append(1, 0);
		alg.points.append(1, 1);
		alg.points.append(10, 0);
		alg.points.append(11, 0);
		alg.points.append(11, 1);

		alg.addTriangle(0, 1, 2);
		alg.addTriangle(3, 4, 5);

		assertTrue(alg.checkCircumcircleCCW(GrlConstants.TEST_F64));

		// Add a new triangle which will contain other points
		alg.addTriangle(0, 1, 5);
		assertFalse(alg.checkCircumcircleCCW(GrlConstants.TEST_F64));
	}

	// Pass in a square, which is degenerate and see if it causes a failure. It shouldn't.
	@Test void checkCircumcircleCCW_square() {
		var alg = new Mesh2D_F64();

		// Check a grid, which is pathological. Points outside the triangle will be
		// exactly on the border
		alg.points.append(0, 0);
		alg.points.append(1, 0);
		alg.points.append(1, 1);
		alg.points.append(0, 1);

		alg.addTriangle(0, 1, 2);
		alg.addTriangle(0, 2, 3);

		assertTrue(alg.checkCircumcircleCCW(GrlConstants.TEST_F64));

		// sanity check, add a failure
		alg.points.append(0.5, 0.5);
		assertFalse(alg.checkCircumcircleCCW(GrlConstants.TEST_F64));
	}

	@Test void pruneTriangles() {
		var alg = new Mesh2D_F64();

		// Add points. Their values don't matter
		for (int i = 0; i < 10; i++) {
			alg.points.append(0, 0);
		}

		// do nothing with nothing
		assertEquals(0, alg.pruneTriangles(( id, v0, v1, v2, shape ) -> true));

		// Just one triangle
		alg.addTriangle(0, 1, 2);
		assertEquals(1, alg.pruneTriangles(( id, v0, v1, v2, shape ) -> true));
		assertEquals(0, alg.triangleCount());
		assertEquals(10, alg.pointCount());

		// Remove one in the middle
		alg.addTriangle(0, 1, 2);
		alg.addTriangle(3, 4, 5);
		alg.addTriangle(3, 6, 7);

		assertEquals(1, alg.pruneTriangles(( id, v0, v1, v2, shape ) -> v1 == 4));
		assertEquals(2, alg.triangleCount());
		assertEquals(10, alg.pointCount());

		assertTrue(0 == alg.triangles.get(0) && 1 == alg.triangles.get(1) && 2 == alg.triangles.get(2));
		assertTrue(3 == alg.triangles.get(3) && 6 == alg.triangles.get(4) && 7 == alg.triangles.get(5));
	}
}
