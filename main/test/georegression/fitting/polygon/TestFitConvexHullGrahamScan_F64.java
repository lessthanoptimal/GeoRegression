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

package georegression.fitting.polygon;

import georegression.geometry.UtilPolygons2D_F64;
import georegression.metric.Intersection2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import org.ddogleg.struct.FastQueue;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestFitConvexHullGrahamScan_F64 {
	Random rand = new Random(324);

	/**
	 * Trivial case where there are 4 points that form a square and are axis-aligned
	 */
	@Test void square_axis_aligned() {
		var points = new FastQueue<>(Point2D_F64::new);
		points.grow().set(0,0);
		points.grow().set(2,0);
		points.grow().set(2,2);
		points.grow().set(0,2);

		var alg = new FitConvexHullGrahamScan_F64();
		var found = new Polygon2D_F64();
		alg.process(points, found);

		assertEquals(4, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
		assertEquals(0.0, found.get(0).distance(0,0), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(1).distance(2,0), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(2).distance(2,2), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(3).distance(0,2), UtilEjml.TEST_F64);

		// Should yield the same results even if the order has changed
		points.reverse();
		alg.process(points, found);
		assertEquals(4, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
	}

	/**
	 * Square again but with a few points inside for good measure. Should not change solution
	 */
	@Test void square_axis_aligned_stuff_inside() {
		var points = new FastQueue<>(Point2D_F64::new);
		points.grow().set(1,1);
		points.grow().set(0.5,1.5);
		points.grow().set(0,0);
		points.grow().set(2,0);
		points.grow().set(2,2);
		points.grow().set(0,2);

		var alg = new FitConvexHullGrahamScan_F64();
		var found = new Polygon2D_F64();
		alg.process(points, found);

		assertEquals(4, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
		assertEquals(0.0, found.get(0).distance(0,0), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(1).distance(2,0), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(2).distance(2,2), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(3).distance(0,2), UtilEjml.TEST_F64);
	}

	/**
	 * Rotated square.
	 */
	@Test void square_off_axis() {
		var points = new FastQueue<>(Point2D_F64::new);
		points.grow().set(1,0);
		points.grow().set(3,2);
		points.grow().set(1,4);
		points.grow().set(-1,2);

		var alg = new FitConvexHullGrahamScan_F64();
		var found = new Polygon2D_F64();
		alg.process(points, found);

		assertEquals(4, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
		assertTrue(UtilPolygons2D_F64.isConvex(found));
	}

	/**
	 * Fit a triangle. Smallest well defined polygon.
	 */
	@Test void triangle() {
		var points = new FastQueue<>(Point2D_F64::new);
		points.grow().set(2,0);
		points.grow().set(2,2);
		points.grow().set(0,0);

		var alg = new FitConvexHullGrahamScan_F64();
		var found = new Polygon2D_F64();
		alg.process(points, found);

		assertEquals(3, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
		assertTrue(UtilPolygons2D_F64.isConvex(found));
	}

	/**
	 * Simple scenario but multiple points have the exact same coordinate
	 */
	@Test void multiple_points_same_location() {
		var points = new FastQueue<>(Point2D_F64::new);
		points.grow().set(2,0);
		points.grow().set(2,2);
		points.grow().set(0,0);
		points.grow().set(2,0);
		points.grow().set(2,2);
		points.grow().set(0,0);

		var alg = new FitConvexHullGrahamScan_F64();
		var found = new Polygon2D_F64();
		alg.process(points, found);

		assertEquals(3, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
		assertTrue(UtilPolygons2D_F64.isConvex(found));

		// turn it into a triangle and see if it still works
		points.grow().set(0,2);
		points.grow().set(0,2);
		alg.process(points, found);
		assertEquals(4, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
		assertTrue(UtilPolygons2D_F64.isConvex(found));

	}

	/**
	 * Pathological case for this specific algorithm. Multiple points will have the same angle relative to the pivot
	 * point
	 */
	@Test void multiple_points_same_angle() {
		var points = new FastQueue<>(Point2D_F64::new);
		points.grow().set(0,0);
		points.grow().set(2,0);
		points.grow().set(2,2);
		points.grow().set(0,2);
		points.grow().set(0.5,0);
		points.grow().set(1.0,0);
		points.grow().set(0,0.5);
		points.grow().set(0,1.5);

		var alg = new FitConvexHullGrahamScan_F64();
		var found = new Polygon2D_F64();
		alg.process(points, found);

		assertEquals(4, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
		assertEquals(0.0, found.get(0).distance(0,0), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(1).distance(2,0), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(2).distance(2,2), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(3).distance(0,2), UtilEjml.TEST_F64);
	}

	/**
	 * Randomly generate a point cloud and test it using the properties of convexity
	 */
	@Test void random() {
		var points = new FastQueue<>(Point2D_F64::new);
		var alg = new FitConvexHullGrahamScan_F64();
		var found = new Polygon2D_F64();

		for (int trial = 0; trial < 40; trial++) {

			points.reset();
			int N = rand.nextInt(20)+3;
			for (int i = 0; i < N; i++) {
				points.grow().set(rand.nextGaussian(), rand.nextGaussian());
			}

			alg.process(points, found);

			assertTrue(found.size() <= N);
			assertTrue(UtilPolygons2D_F64.isCCW(found));
			assertTrue(UtilPolygons2D_F64.isConvex(found));

			points.forEach(p-> assertTrue(Intersection2D_F64.containsConvex2(found,p.x,p.y)));
		}
	}

	/**
	 * one and two point sets are pathological
	 */
	@Test void one_and_two_points() {
		var points = new FastQueue<>(Point2D_F64::new);
		points.grow().set(0,0);

		var alg = new FitConvexHullGrahamScan_F64();
		var found = new Polygon2D_F64();
		alg.process(points, found);
		assertEquals(1, found.size());

		points.grow().set(2,2);
		alg.process(points, found);
		assertEquals(2, found.size());
	}

	/**
	 * All points lie along a line.
	 */
	@Test void line() {
		var points = new FastQueue<>(Point2D_F64::new);
		points.grow().set(0,0);
		points.grow().set(1,0);
		points.grow().set(2,0);
		points.grow().set(3,0);

		var alg = new FitConvexHullGrahamScan_F64();
		var found = new Polygon2D_F64();
		alg.process(points, found);
		assertEquals(2, found.size());
		assertEquals(0.0, found.get(0).distance(0,0), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(1).distance(3,0), UtilEjml.TEST_F64);
	}

	@Test void isCCW() {
		Point2D_F64 a = new Point2D_F64(2,2);
		Point2D_F64 b = new Point2D_F64(3,2);
		Point2D_F64 c = new Point2D_F64(2,3);

		assertEquals(-1, FitConvexHullGrahamScan_F64.isCW(a,b,c));
		assertEquals(1, FitConvexHullGrahamScan_F64.isCW(a,c,b));
		assertEquals(0, FitConvexHullGrahamScan_F64.isCW(a,c,c));
	}

	@Test void compareAngle() {
		var alg = new FitConvexHullGrahamScan_F64();
		Comparator<Point2D_F64> compare = alg.sorter.getComparator();
		Point2D_F64 a = new Point2D_F64(2,2);
		Point2D_F64 b = new Point2D_F64(3,2);
		Point2D_F64 c = new Point2D_F64(2,3);

		alg.pivot = a;

		assertEquals(-1,compare.compare(b,c));
		assertEquals(1,compare.compare(c,b));
		assertEquals(0,compare.compare(c,c));

		Point2D_F64 d = new Point2D_F64(2,4);
		assertEquals(1,compare.compare(d,c));
		assertEquals(-1,compare.compare(c,d));
	}
}
