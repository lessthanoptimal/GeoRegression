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

package georegression.delaunay;

import georegression.GeoStandardJUnit;
import georegression.struct.TriIndex;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Triangle2D_F64;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestPackedDelaunayGraph extends GeoStandardJUnit {
	// this automated test needs to be added to GeoRegression
//	@Test void reset() {
//		fail("Implement");
//	}

	@Test void addPoint() {
		var alg = new PackedDelaunayGraph();
		alg.addPoint(1, 2);
		assertEquals(0.0, alg.points.getTemp(0).distance(1, 2));
		assertEquals(1, alg.vertexes.size);
	}

	@Test void addTriangle() {
		var alg = new PackedDelaunayGraph();
		for (int i = 0; i < 5; i++) {
			alg.addPoint(i, 2);
		}

		int ref0 = alg.addTriangle(0, 1, 2);
		int ref1 = alg.addTriangle(1, 1, 2);
		int ref2 = alg.addTriangle(2, 1, 4);
		assertEquals(3, alg.triangleCount());

		var corners = new TriIndex();

		// See if the triangle was saved correctly
		alg.getTriangle(ref0, corners);
		assertTrue(corners.isIdentical(0, 1, 2));
		alg.getTriangle(ref1, corners);
		assertTrue(corners.isIdentical(1, 1, 2));
		alg.getTriangle(ref2, corners);
		assertTrue(corners.isIdentical(2, 1, 4));
	}

	// Tests recycle logic in addTriangle
	@Test void addTriangle_recycle() {
		var alg = new PackedDelaunayGraph();
		for (int i = 0; i < 5; i++) {
			alg.addPoint(i, 2);
		}

		int ref0 = alg.addTriangle(0, 1, 2);
		int ref1 = alg.addTriangle(1, 1, 2);
		int ref2 = alg.addTriangle(2, 1, 4);
		assertEquals(3, alg.triangleCount());

		alg.removeTriangle(ref1);
		assertEquals(2, alg.triangleCount());
		assertEquals(1, alg.unusedTriangles.size);


		int ref3 = alg.addTriangle(4, 0, 1);
		assertEquals(3, alg.triangleCount());

		// See if the triangle was saved correctly
		var corners = new TriIndex();
		alg.getTriangle(ref0, corners);
		assertTrue(corners.isIdentical(0, 1, 2));
		alg.getTriangle(ref2, corners);
		assertTrue(corners.isIdentical(2, 1, 4));
		alg.getTriangle(ref3, corners);
		assertTrue(corners.isIdentical(4, 0, 1));

		// See if it recycled
		assertEquals(0, alg.unusedTriangles.size);
		assertEquals(3*3, alg.triangles.size());
	}

	@Test void removeTriangle() {
		var alg = new PackedDelaunayGraph();
		for (int i = 0; i < 6; i++) {
			alg.addPoint(i, 2);
		}

		int ref0 = alg.addTriangle(0, 1, 2);
		int ref1 = alg.addTriangle(1, 3, 2);

		alg.removeTriangle(ref1);

		// Check internal data structures and the vertex to triangle references
		assertEquals(1, alg.triangleCount());
		assertEquals(1, alg.unusedTriangles.size);
		assertEquals(ref0, alg.vertexes.get(0).getTriangle(0));
		assertEquals(0, alg.vertexes.get(0).getCorner(0));
		assertEquals(ref0, alg.vertexes.get(1).getTriangle(0));
		assertEquals(1, alg.vertexes.get(1).getCorner(0));
		assertEquals(ref0, alg.vertexes.get(2).getTriangle(0));
		assertEquals(2, alg.vertexes.get(2).getCorner(0));

		// See if all vertexes have the expected number of connects
		for (int i = 0; i < 6; i++) {
			assertEquals(i < 3 ? 1 : 0, alg.vertexes.get(i).size());
		}

		// see if the triangle was filled in correctly
		for (int i = 3; i < 6; i++) {
			assertEquals(-1, alg.triangles.get(i));
		}
	}

	@Test void getCorners() {
		var alg = new PackedDelaunayGraph();
		for (int i = 0; i < 6; i++) {
			alg.addPoint(i, 2 + i);
		}

		int ref0 = alg.addTriangle(0, 1, 2);

		var corners = new TriIndex();
		var triangle = new Triangle2D_F64();

		alg.getTriangle(ref0, corners);
		alg.getCorners(corners, triangle);

		for (int i = 0; i < 3; i++) {
			assertEquals(0.0, triangle.get(i).distance(i, 2 + i));
		}
	}

	@Test void getTriangleCorner() {
		var alg = new PackedDelaunayGraph();
		for (int i = 0; i < 6; i++) {
			alg.addPoint(i, 2 + i);
		}

		int ref0 = alg.addTriangle(0, 1, 2);
		var p = new Point2D_F64();
		for (int i = 0; i < 3; i++) {
			alg.getTriangleCorner(ref0, i, p);
			assertEquals(0.0, p.distance(i, 2 + i));
		}
	}

	@Test void cornerPoint() {
		var alg = new PackedDelaunayGraph();
		for (int i = 0; i < 6; i++) {
			alg.addPoint(i, 2 + i);
		}

		int ref0 = alg.addTriangle(3, 2, 4);
		assertEquals(3, alg.cornerPoint(ref0, 0));
		assertEquals(2, alg.cornerPoint(ref0, 1));
		assertEquals(4, alg.cornerPoint(ref0, 2));
	}

	@Test void cornerPointW() {
		var alg = new PackedDelaunayGraph();
		for (int i = 0; i < 6; i++) {
			alg.addPoint(i, 2 + i);
		}

		int ref0 = alg.addTriangle(3, 2, 4);
		assertEquals(3, alg.cornerPointW(ref0, 0));
		assertEquals(2, alg.cornerPointW(ref0, 1));
		assertEquals(4, alg.cornerPointW(ref0, 2));
		assertEquals(3, alg.cornerPointW(ref0, 3));
		assertEquals(2, alg.cornerPointW(ref0, 4));
		assertEquals(4, alg.cornerPointW(ref0, 5));
	}

	@Nested class TestVertex {

//		@Test void reset() {
//			fail("Implement");
//		}

		@Test void size() {
			var alg = new PackedDelaunayGraph.Vertex();
			assertEquals(0, alg.size());

			for (int i = 0; i < 3; i++) {
				alg.connect(5, 6);
				assertEquals(i + 1, alg.size());
			}
		}

		@Test void getTriangle_getCorner() {
			var alg = new PackedDelaunayGraph.Vertex();

			for (int i = 0; i < 3; i++) {
				alg.connect(5 + i, i + 2);
			}

			assertEquals(5, alg.getTriangle(0));
			assertEquals(6, alg.getTriangle(1));
			assertEquals(7, alg.getTriangle(2));

			assertEquals(2, alg.getCorner(0));
			assertEquals(3, alg.getCorner(1));
			assertEquals(4, alg.getCorner(2));
		}

		@Test void disconnect() {
			var alg = new PackedDelaunayGraph.Vertex();

			// connect triangles
			for (int i = 0; i < 3; i++) {
				alg.connect(5 + i, i + 2);
			}
			assertEquals(3, alg.size());

			// Remove the first one
			alg.disconnect(5);

			// make sure there are two remaining and they have the expected ID
			assertEquals(2, alg.size());
			assertTrue(alg.findTriangle(6) >= 0);
			assertTrue(alg.findTriangle(7) >= 0);

			// Remove the last two and see if it blows up
			alg.disconnect(7);
			alg.disconnect(6);
			assertEquals(0, alg.size());
		}

		@Test void getAllTriangles() {
			var alg = new PackedDelaunayGraph.Vertex();

			var found = new HashSet<Integer>();
			alg.getAllTriangles(found);
			assertTrue(found.isEmpty());

			// Add 3 triangles
			for (int i = 0; i < 3; i++) {
				alg.connect(5 + i, i + 2);
			}

			alg.getAllTriangles(found);
			assertEquals(3, found.size());
			for (int i = 0; i < 3; i++) {
				assertTrue(found.contains(5 + i));
			}
		}
	}
}