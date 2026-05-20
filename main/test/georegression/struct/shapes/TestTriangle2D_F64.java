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

package georegression.struct.shapes;

import georegression.GeoRegressionJUnit;
import georegression.struct.point.Point2D_F64;
import org.ejml.MapPrintFormat;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestTriangle2D_F64 extends GeoRegressionJUnit {
	@Test void zero() {
		var alg = new Triangle2D_F64().setTo(1, 2, 3, 4, 5, 6);
		alg.zero();
		assertTrue(alg.v0.isIdentical(0, 0));
		assertTrue(alg.v1.isIdentical(0, 0));
		assertTrue(alg.v2.isIdentical(0, 0));
	}

	@Test void get_idx() {
		var alg = new Triangle2D_F64().setTo(1, 2, 3, 4, 5, 6);

		assertEquals(0.0, alg.get(0).distance(1, 2));
		assertEquals(0.0, alg.get(1).distance(3, 4));
		assertEquals(0.0, alg.get(2).distance(5, 6));
	}

	@Test void set_scalar() {
		var alg = new Triangle2D_F64();

		alg.set(0, 1, 2);
		alg.set(1, 3, 4);
		alg.set(2, 5, 6);

		assertEquals(0.0, alg.get(0).distance(1, 2));
		assertEquals(0.0, alg.get(1).distance(3, 4));
		assertEquals(0.0, alg.get(2).distance(5, 6));
	}

	@Test void set_point() {
		var alg = new Triangle2D_F64();

		alg.set(0, new Point2D_F64(1, 2));
		alg.set(1, new Point2D_F64(3, 4));
		alg.set(2, new Point2D_F64(5, 6));

		assertEquals(0.0, alg.get(0).distance(1, 2));
		assertEquals(0.0, alg.get(1).distance(3, 4));
		assertEquals(0.0, alg.get(2).distance(5, 6));
	}

	@Test void getSide() {
		var alg = new Triangle2D_F64().setTo(1, 2, 3, 4, 5, 6);

		assertTrue(alg.getSide(0, null).isIdentical(1, 2, 3, 4, 0.0));
		assertTrue(alg.getSide(1, null).isIdentical(3, 4, 5, 6, 0.0));
		assertTrue(alg.getSide(2, null).isIdentical(5, 6, 1, 2, 0.0));
	}

	@Test void sideTangent() {
		var alg = new Triangle2D_F64().setTo(1, 1, 3, 1, 3, 3);

		double a = Math.sqrt(2)/2.0;
		assertTrue(alg.sideTangent(0, null).isIdentical(0, -1,0.0));
		assertTrue(alg.sideTangent(1, null).isIdentical(1, 0,0.0));
		assertTrue(alg.sideTangent(2, null).isIdentical(-a, a, UtilEjml.TEST_F64));
	}

	@Test void formatMap() {
		var a = new Triangle2D_F64().setTo(1, 2, 3.1234, 4, 5, 6);
		String found = a.formatMap(new MapPrintFormat().withPrecision(2));
		assertEquals("{v0: {x: 1, y: 2}, v1: {x: 3.12, y: 4}, v2: {x: 5, y: 6}}", found);
	}
}
