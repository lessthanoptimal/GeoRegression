/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

import georegression.GeoRegressionJUnit;
import georegression.delaunay.DelaunayIncrementalWalk.CandidateTriangle;
import georegression.delaunay.DelaunayIncrementalWalk.OtherSide;
import georegression.geometry.UtilPolygons2D_F64;
import georegression.metric.Intersection2D_F64;
import georegression.struct.Mesh2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Triangle2D_F64;
import org.ddogleg.struct.DogArray;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDelaunayIncrementalWalk extends GeoRegressionJUnit {
	@Test void all_grid() {
		var points = new DogArray<>(Point2D_F64::new);
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 6; col++) {
				points.grow().setTo(1 + col, 2 + row);
			}
		}
		var alg = allChecks(points);

		// special grid test. No side can be longer than sqrt(2)
		alg.toMesh(null).forLines(( a, b, c, d, line ) -> {
			double length = line.getLength2();
			assertTrue(length <= 2.00001 && length >= 0.99999);
			return true;
		});
	}

	@Test void all_random() {
		var points = new DogArray<>(Point2D_F64::new);
		for (int i = 0; i < 30; i++) {
			points.grow().setTo(rand.nextDouble()*3 - 1.5, rand.nextDouble()*3 - 1.5);
		}

		allChecks(points);
	}

	// Runs everything and checks results using properties of Delaunay
	private static DelaunayIncrementalWalk allChecks( DogArray<Point2D_F64> points ) {
		var alg = new DelaunayIncrementalWalk();
//		alg.setVerbose(System.out, null);

		alg.process(( idx, p ) -> {
			p.setTo(points.get(idx));
			return true;
		}, points.size);

		// Check results by looking at graph properties
		assertTrue(alg.getGraph().verifyTrianglesAreCCW());
		assertEquals(points.size, alg.getGraph().points.size());
		// sanity check the number of triangles
		int triCount = alg.getGraph().triangleCount();
		assertTrue(triCount >= points.size() && triCount <= points.size()*3);

		// Convert into a mesh and apply general mesh tests
		Mesh2D_F64 mesh = alg.toMesh(null);
		assertFalse(mesh.doEdgesIntersect());

		// All points should be in at least one triangle
		assertEquals(points.size, mesh.countPointsInTriangles());

		// Check one property of a Delaunay triangulation
		assertTrue(mesh.checkCircumcircleCCW(UtilEjml.EPS));

		return alg;
	}

	@Test void toMesh() {
		var alg = new DelaunayIncrementalWalk();
		var graph = alg.getGraph();

		alg.reorderToOriginal.resize(4, ( idx ) -> idx);
		graph.addPoint(1, 2);
		graph.addPoint(1, 3);
		graph.addPoint(2, 4);
		graph.addPoint(2, 5);

		graph.addTriangle(0, 2, 1);
		graph.addTriangle(0, 2, 3);
		int ref = graph.addTriangle(3, 1, 2);
		graph.addTriangle(3, 1, 2);

		// create an empty spot
		graph.removeTriangle(ref);

		var mesh = new Mesh2D_F64();

		// add a bunch of points to make sure its reset
		for (int i = 0; i < 10; i++) {
			mesh.points.append(0, 0);
		}

		alg.toMesh(mesh);

		// check that the mesh has the expected number of elements
		assertEquals(graph.points.size(), mesh.points.size());
		assertEquals(graph.triangleCount(), mesh.triangleCount());

		// make sure no invalid values were copied
		for (int i = 0; i < mesh.triangles.size; i++) {
			assertTrue(mesh.triangles.get(i) >= 0);
		}
	}

	// Test adding and removing the large triangle
	@Test void largeTriangle() {
		// give it points with different sets of bounds to verify bounds calculation
		largeTriangles(-1, -2, 1, 2);
		largeTriangles(1, 2, 3, 4);
		largeTriangles(-3, -4, -2, -1);
	}

	void largeTriangles( double x0, double y0, double x1, double y1 ) {
		var alg = new DelaunayIncrementalWalk();
		var graph = alg.getGraph();

		// fill it with all the points
		addPoints(x0, y0, x1, y1, alg);

		alg.addInitialLargeTriangles();
		assertTrue(graph.verifyTrianglesAreCCW());
		assertEquals(2, graph.triangleCount());

		// Load the triangles so we can verify that every point is inside
		var tri1 = new Triangle2D_F64();
		var tri2 = new Triangle2D_F64();
		graph.getCorners(0, tri1);
		graph.getCorners(3, tri2);

		// verify that all points are strictly inside, except for the special boundary points
		for (int i = 0; i < graph.points.size() - 4; i++) {
			Point2D_F64 p = graph.points.getTemp(i);

			assertTrue(1 == Intersection2D_F64.insideTriangle(tri1, p) ||
					1 == Intersection2D_F64.insideTriangle(tri2, p));
		}

		// Remove the large triangle, it should be empty now
		alg.removeInitialLargeTriangles();
		assertEquals(0, graph.triangleCount());
		assertEquals(20, graph.points.size());
	}

	private void addPoints( double x0, double y0, double x1, double y1, DelaunayIncrementalWalk alg ) {
		var points = new DogArray<>(Point2D_F64::new);

		// add corners to ensure we know the exact bounds
		points.grow().setTo(x0, y0);
		points.grow().setTo(x1, y1);

		// random points now
		for (int i = 0; i < 20; i++) {
			double x = (x1 - x0)*rand.nextDouble() + x0;
			double y = (y1 - y0)*rand.nextDouble() + y0;
			alg.getGraph().addPoint(x, y);
		}
	}

	@Test void shufflePointIndexes() {
		int N = 50;

		var alg = new DelaunayIncrementalWalk();
		alg.shufflePointIndexes(N);

		// See if every index was referenced once
		var found = new boolean[N];
		int inorderCount = 0;
		for (int i = 0; i < N; i++) {
			int original = alg.reorderToOriginal.get(i);
			assertFalse(found[original]);
			found[original] = true;

			if (original == i)
				inorderCount++;
		}

		// Check to see that only a few points by luck are in the original order still
		assertTrue(inorderCount < N/10);
	}

	@Test void traverseThenInsert() {
		var alg = new DelaunayIncrementalWalk();
		var graph = alg.getGraph();

		// create a sequence of triangles it will need to traverse
		graph.addPoint(0, 0);
		graph.addPoint(0, 2);
		graph.addPoint(2, 0);
		graph.addPoint(2, 2);
		graph.addPoint(4, 0);

		graph.addPoint(4, 2);
		graph.addPoint(6, 0);
		graph.addPoint(6, 2);
		graph.addPoint(6, 4);
		graph.addPoint(5.2, 1.5);

		int tri0 = graph.addTriangle(0, 2, 1);
		graph.addTriangle(1, 2, 3);
		graph.addTriangle(2, 4, 3);
		graph.addTriangle(3, 4, 5);
		graph.addTriangle(4, 7, 5); // where it will be put
		graph.addTriangle(4, 6, 7);
		graph.addTriangle(5, 7, 8);

		assertTrue(graph.verifyTrianglesAreCCW());

		alg.getGraph().mostRecentlyAddedTriangle = tri0;
		alg.traverseThenInsert(9);

		// See if it was inserted inside the triangle
		assertEquals(9, graph.triangleCount());
		assertTrue(graph.verifyTrianglesAreCCW());

		// Verify the triangle it was inserted into got removed
		assertEquals(-1, graph.findTriangle(4, 7, 5));
	}

	@Test void insertPointInTriangle() {
		var alg = new DelaunayIncrementalWalk();
		var graph = alg.getGraph();

		graph.addPoint(2, 1);
		graph.addPoint(0, 0);
		graph.addPoint(4, 0);
		graph.addPoint(2, 2);

		// Add points the "other" triangles. These are designed to not require swapping
		graph.addPoint(-4, 8);
		graph.addPoint(8, 8);
		graph.addPoint(2, -10);

		graph.addTriangle(2, 1, 6);
		graph.addTriangle(1, 3, 4);
		graph.addTriangle(3, 2, 5);
		assertTrue(graph.verifyTrianglesAreCCW());

		// Add the triangle the point is being inserted into
		int tri0 = graph.addTriangle(1, 2, 3);

		// sanity check
		assertEquals(4, graph.triangleCount());

		var candidate = new CandidateTriangle();
		candidate.id = tri0;
		graph.getTriangle(tri0, candidate.corners);
		graph.getCorners(candidate.corners, candidate.location);

		// Insert the new point
		alg.insertPointInTriangle(candidate, 0, 2, 1);

		// Check the changes
		assertEquals(6, graph.triangleCount());
		assertNotEquals(-1, graph.findTriangle(1, 2, 0));
		assertNotEquals(-1, graph.findTriangle(2, 3, 0));
		assertNotEquals(-1, graph.findTriangle(3, 1, 0));
		assertTrue(graph.verifyTrianglesAreCCW());
	}

	@Test void insertPointOnLine() {
		var alg = new DelaunayIncrementalWalk();
		var graph = alg.getGraph();

		// two triangles where the point will be inserted right in the middle of on a line
		graph.addPoint(-2, 0);
		graph.addPoint(0, -2);
		graph.addPoint(2, 0);
		graph.addPoint(0, 2);

		// add far away points for "other" triangles. No swapping.
		graph.addPoint(-5, -5);
		graph.addPoint(5, -5);
		graph.addPoint(5, 5);
		graph.addPoint(-5, 5);

		// Add points which will be inserted
		graph.addPoint(0, 0);

		// Two triangles that shape a line the point will be on
		int tri0 = graph.addTriangle(0, 1, 2);
		int tri1 = graph.addTriangle(0, 2, 3);

		// "other" triangles
		graph.addTriangle(4, 1, 0);
		graph.addTriangle(5, 2, 1);
		graph.addTriangle(6, 3, 2);
		graph.addTriangle(7, 0, 3);
		assertTrue(graph.verifyTrianglesAreCCW());

		var candidate = new CandidateTriangle();
		candidate.id = tri0;
		graph.getTriangle(tri0, candidate.corners);
		graph.getCorners(candidate.corners, candidate.location);

		// Insert the new point
		int side = UtilPolygons2D_F64.closestSideTriangle(candidate.location, 0, 0);
		alg.insertPointOnLine(candidate, side, 8, 0, 0);
		assertEquals(8, graph.triangleCount());
		assertNotEquals(-1, graph.findTriangle(0, 1, 8));
		assertNotEquals(-1, graph.findTriangle(1, 2, 8));
		assertNotEquals(-1, graph.findTriangle(2, 3, 8));
		assertNotEquals(-1, graph.findTriangle(3, 0, 8));
		assertTrue(graph.verifyTrianglesAreCCW());
	}

	@Test void checkThenSwapEdge() {
		var alg = new DelaunayIncrementalWalk();
		var graph = alg.getGraph();

		// Create 3 triangles. One pair will not change and one will require a swap
		graph.addPoint(0, 0);
		graph.addPoint(4, 0);
		graph.addPoint(2, 2);
		graph.addPoint(5, 2);
		graph.addPoint(0.9, 1);

		int tri0 = graph.addTriangle(0, 1, 2);
		int tri1 = graph.addTriangle(2, 1, 3);
		int tri2 = graph.addTriangle(0, 2, 4);
		assertTrue(graph.verifyTrianglesAreCCW());

		// There should be no change here
		alg.checkThenSwapEdge(tri0, 1, 0.0, 0.0);

		// This verifies that there is no change
		assertEquals(tri0, graph.findTriangle(0, 1, 2));

		// Now it should change
		alg.checkThenSwapEdge(tri2, 0, 0.9, 1);
		assertNotEquals(-1, graph.findTriangle(0, 1, 4));
		assertNotEquals(-1, graph.findTriangle(1, 2, 4));
		assertTrue(graph.verifyTrianglesAreCCW());
	}

	@Test void triangleOnOtherSide() {
		var alg = new DelaunayIncrementalWalk();
		var graph = alg.getGraph();
		// construct 3 triangles. 2 of the triangles share a side with triangle 0.
		graph.addPoint(0, 0);
		graph.addPoint(1, 0);
		graph.addPoint(1, 1);
		graph.addPoint(2, 0);
		graph.addPoint(0, 1);

		int tri0 = graph.addTriangle(0, 1, 2);
		int tri1 = graph.addTriangle(2, 1, 3);
		int tri2 = graph.addTriangle(0, 2, 4);

		var found = new OtherSide();

		// test it with a hand computed solution
		assertEquals(tri1, alg.triangleOnOtherSide(tri0, 1, found));
		assertEquals(tri1, found.triangleID);
		assertEquals(2, found.cornerID);

		// swap the triangles being tested
		assertEquals(tri0, alg.triangleOnOtherSide(tri1, 0, found));
		assertEquals(tri0, found.triangleID);
		assertEquals(0, found.cornerID);

		// test negative case
		assertEquals(-1, alg.triangleOnOtherSide(tri2, 1, found));
	}
}
