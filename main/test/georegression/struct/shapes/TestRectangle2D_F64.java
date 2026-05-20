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
import georegression.misc.GrlConstants;
import org.ejml.MapPrintFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRectangle2D_F64 extends GeoRegressionJUnit {
	@Test void enforceExtents() {
		var a = new Rectangle2D_F64(-1, -2, 50, 10);
		a.enforceExtents();
		assertEquals(-1, a.p0.x, GrlConstants.TEST_F64);
		assertEquals(-2, a.p0.y, GrlConstants.TEST_F64);
		assertEquals(50, a.p1.x, GrlConstants.TEST_F64);
		assertEquals(10, a.p1.y, GrlConstants.TEST_F64);

		a = new Rectangle2D_F64(50, 10, -1, -2);
		a.enforceExtents();
		assertEquals(-1, a.p0.x, GrlConstants.TEST_F64);
		assertEquals(-2, a.p0.y, GrlConstants.TEST_F64);
		assertEquals(50, a.p1.x, GrlConstants.TEST_F64);
		assertEquals(10, a.p1.y, GrlConstants.TEST_F64);
	}

	@Test void getCorner() {
		var rect = new Rectangle2D_F64(-1, -2, 2, 3);
		assertTrue(rect.getCorner(0, null).isIdentical(-1, -2));
		assertTrue(rect.getCorner(1, null).isIdentical(2, -2));
		assertTrue(rect.getCorner(2, null).isIdentical(2, 3));
		assertTrue(rect.getCorner(3, null).isIdentical(-1, 3));
	}

	@Test void formatMap() {
		var a = new Rectangle2D_F64(-1, -2, 2.1234, 120);
		String found = a.formatMap(new MapPrintFormat().withPrecision(2));
		assertEquals("{p0: {x: -1, y: -2}, p1: {x: 2.12, y: 120}}", found);
	}
}