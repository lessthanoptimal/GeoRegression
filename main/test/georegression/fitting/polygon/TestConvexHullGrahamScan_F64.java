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
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import org.ddogleg.struct.DogArray;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestConvexHullGrahamScan_F64 extends CommonConvexHullChecks_F64 {
	@Override FitConvexHull_F64 createAlgorithm() {
		return new ConvexHullGrahamScan_F64();
	}

	/**
	 * Pathological case for this specific algorithm. Multiple points will have the same angle relative to the pivot
	 * point
	 */
	@Test void multiple_points_same_angle() {
		var points = new DogArray<>(Point2D_F64::new);
		points.grow().setTo(0,0);
		points.grow().setTo(2,0);
		points.grow().setTo(2,2);
		points.grow().setTo(0,2);
		points.grow().setTo(0.5,0);
		points.grow().setTo(1.0,0);
		points.grow().setTo(0,0.5);
		points.grow().setTo(0,1.5);

		var alg = new ConvexHullGrahamScan_F64();
		var found = new Polygon2D_F64();
		alg.process(points, found);

		assertEquals(4, found.size());
		assertTrue(UtilPolygons2D_F64.isCCW(found));
		assertEquals(0.0, found.get(0).distance(0,0), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(1).distance(2,0), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(2).distance(2,2), UtilEjml.TEST_F64);
		assertEquals(0.0, found.get(3).distance(0,2), UtilEjml.TEST_F64);
	}

	@Test void isCW() {
		Point2D_F64 a = new Point2D_F64(2,2);
		Point2D_F64 b = new Point2D_F64(3,2);
		Point2D_F64 c = new Point2D_F64(2,3);

		assertEquals(-1, ConvexHullGrahamScan_F64.isCW(a,b,c));
		assertEquals(1, ConvexHullGrahamScan_F64.isCW(a,c,b));
		assertEquals(0, ConvexHullGrahamScan_F64.isCW(a,c,c));
	}

	@Test void compareAngle() {
		var alg = new ConvexHullGrahamScan_F64();
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
