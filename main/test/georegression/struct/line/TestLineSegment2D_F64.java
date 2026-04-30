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

package georegression.struct.line;

import org.ejml.MapPrintFormat;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public class TestLineSegment2D_F64 {
	@Test void isIdentical() {
		var a = new LineSegment2D_F64(1, 2, 3, 4);
		var b = new LineSegment2D_F64(1, 2, 3, 4);

		assertTrue(a.isIdentical(b, 0.0));

		b.a.x += UtilEjml.TEST_F64_SQ;
		assertFalse(a.isIdentical(b, 0.0));
		assertTrue(a.isIdentical(b, UtilEjml.TEST_F64_SQ*1.01));
	}

	@Test void equals() {
		var a = new LineSegment2D_F64(1,2,3,4);
		var b = new LineSegment2D_F64(1,2,3,4);

		assertEquals(a, b);
		b.a.x += UtilEjml.TEST_F64;
		assertNotEquals(a, b);
		b.a.x = 1;
		b.b.y += UtilEjml.TEST_F64;
		assertNotEquals(a, b);
	}

	@Test void formatMap() {
		var line = new LineSegment2D_F64(1, 2, 3, 4.1234);
		String found = line.formatMap(new MapPrintFormat().withPrecision(2));
		assertEquals("{a: {x: 1, y: 2}, b: {x: 3, y: 4.12}}", found);
	}
}