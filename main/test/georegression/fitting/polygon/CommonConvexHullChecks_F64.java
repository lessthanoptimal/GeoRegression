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
import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import org.ddogleg.struct.DogArray;
import org.ddogleg.struct.FastAccess;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks for convex hull algorithms
 *
 * @author Peter Abeles
 */
public abstract class CommonConvexHullChecks_F64 {
	Random rand = new Random(324);

	abstract FitConvexHull_F64 createAlgorithm();

	/**
	 * Trivial case where there are 4 points that form a square and are axis-aligned
	 */
	@Test
	void square_axis_aligned() {
		var points = new DogArray<>(Point2D_F64::new);
		points.grow().setTo(0,0);
		points.grow().setTo(2,0);
		points.grow().setTo(2,2);
		points.grow().setTo(0,2);

		FitConvexHull_F64 alg = createAlgorithm();
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
		var points = new DogArray<>(Point2D_F64::new);
		points.grow().setTo(1,1);
		points.grow().setTo(0.5,1.5);
		points.grow().setTo(0,0);
		points.grow().setTo(2,0);
		points.grow().setTo(2,2);
		points.grow().setTo(0,2);

		FitConvexHull_F64 alg = createAlgorithm();
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
		var points = new DogArray<>(Point2D_F64::new);
		points.grow().setTo(1,0);
		points.grow().setTo(3,2);
		points.grow().setTo(1,4);
		points.grow().setTo(-1,2);

		FitConvexHull_F64 alg = createAlgorithm();
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
		var points = new DogArray<>(Point2D_F64::new);
		points.grow().setTo(2,0);
		points.grow().setTo(2,2);
		points.grow().setTo(0,0);

		FitConvexHull_F64 alg = createAlgorithm();
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
		var points = new DogArray<>(Point2D_F64::new);
		points.grow().setTo(2,0);
		points.grow().setTo(2,2);
		points.grow().setTo(0,0);
		points.grow().setTo(2,0);
		points.grow().setTo(2,2);
		points.grow().setTo(0,0);

		FitConvexHull_F64 alg = createAlgorithm();
		var found = new Polygon2D_F64();
		alg.process(points, found);

		assertEquals(3, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
		assertTrue(UtilPolygons2D_F64.isConvex(found));

		// turn it into a triangle and see if it still works
		points.grow().setTo(0,2);
		points.grow().setTo(0,2);
		alg.process(points, found);
		assertEquals(4, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
		assertTrue(UtilPolygons2D_F64.isConvex(found));
	}

	/**
	 * Randomly generate a point cloud and test it using the properties of convexity
	 */
	@Test void random() {
		var points = new DogArray<>(Point2D_F64::new);
		FitConvexHull_F64 alg = createAlgorithm();
		var found = new Polygon2D_F64();

		for (int trial = 0; trial < 40; trial++) {

			points.reset();
			int N = rand.nextInt(20)+3;
			for (int i = 0; i < N; i++) {
				points.grow().setTo(rand.nextGaussian(), rand.nextGaussian());
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
		var points = new DogArray<>(Point2D_F64::new);
		points.grow().setTo(0,0);

		FitConvexHull_F64 alg = createAlgorithm();
		var found = new Polygon2D_F64();
		alg.process(points, found);
		assertEquals(1, found.size());

		points.grow().setTo(2,2);
		alg.process(points, found);
		assertEquals(2, found.size());
	}

	/**
	 * All points lie along a line.
	 */
	@Test void line() {
		var points = new DogArray<>(Point2D_F64::new);
		points.grow().setTo(0,0);
		points.grow().setTo(1,0);
		points.grow().setTo(2,0);
		points.grow().setTo(3,0);

		FitConvexHull_F64 alg = createAlgorithm();
		var found = new Polygon2D_F64();
		alg.process(points, found);
		assertEquals(2, found.size());
		assertEquals(0.0, found.get(0).distance(0,0), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(1).distance(3,0), UtilEjml.TEST_F64);
	}

	@Test void pentagon() {
		// all points are in hull
		var input = new DogArray<>(Point2D_F64::new);
		input.grow().setTo(2,3);
		input.grow().setTo(2,8);
		input.grow().setTo(7,8);
		input.grow().setTo(7,3);
		input.grow().setTo(4,10);

		Polygon2D_F64 output = new Polygon2D_F64();

		ConvexHullAndrewMonotone_F64 alg = new ConvexHullAndrewMonotone_F64();

		alg.process(input,output);

		containsOnceEach(input,output);

		// add a point inside
		var input2 = new DogArray<>(Point2D_F64::new);
		input2.copyAll(input.toList(), (a,b)->b.setTo(a));
		input2.grow().setTo(4,4);

		alg.process(input2,output);
		containsOnceEach(input,output);
	}

	@Test void grid() {
		ConvexHullAndrewMonotone_F64 alg = new ConvexHullAndrewMonotone_F64();
		Polygon2D_F64 output = new Polygon2D_F64();

		int numRows = 5;
		int numCols = 6;

		double w = 1.2;

		var points = new DogArray<>(Point2D_F64::new);

		for (int row = 0; row < numRows; row++) {
			double y = row*w - 2.1;
			for (int col = 0; col < numCols; col++) {
				double x = col*w - 2.6;
				points.grow().setTo(x,y);
			}
		}
		alg.process(points,output);

		// check some of the properties of the convex hull
		assertTrue(output.size() <= points.size);
		assertTrue(output.isCCW());
		assertTrue(output.isConvex());

		for (int i = 0; i < points.size; i++) {
			Intersection2D_F64.containsConvex(output,points.get(i));
		}
	}

	void containsOnceEach(FastAccess<Point2D_F64> expected , Polygon2D_F64 output ) {
		assertTrue(UtilPolygons2D_F64.isConvex(output));

		int[] count = new int[expected.size];

		assertEquals(expected.size,output.size());

		for (int i = 0; i < expected.size; i++) {
			for (int j = 0; j < output.size(); j++) {
				if( expected.get(i).distance2(output.get(j)) <= GrlConstants.TEST_F64) {
					count[i]++;
				}
			}
		}

		for (int i = 0; i < count.length; i++) {
			assertEquals(1, count[i]);
		}
	}
}
