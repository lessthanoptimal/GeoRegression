/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestLineparametric3D_F64 {
	@Test void isIdentical() {
		var a = new LineParametric3D_F64(1, 2, 3, 4, 5, 6);

		assertTrue(a.isIdentical(new LineParametric3D_F64(1, 2, 3, 4, 5, 6), 0.0));
		assertTrue(a.isIdentical(new LineParametric3D_F64(1, 2, 3, 4.1, 5, 6), 0.1));
		assertFalse(a.isIdentical(new LineParametric3D_F64(1, 2, 3, 4.11, 5, 6), 0.1));
	}

	@Test void equals() {
		var a = new LineParametric3D_F64(1, 2, 3, 4, 5, 6);
		var b = new LineParametric3D_F64(1, 2, 3, 4, 5, 6);

		assertEquals(a, b);
		b.p.x += UtilEjml.TEST_F64;
		assertNotEquals(a, b);
		b.p.x = 1;
		b.slope.y += UtilEjml.TEST_F64;
		assertNotEquals(a, b);
	}
}