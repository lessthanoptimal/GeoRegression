/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Peter Abeles
 */
public class TestLineSegment2D_F64 {
	@Test
	void equals() {
		LineSegment2D_F64 a = new LineSegment2D_F64(1,2,3,4);
		LineSegment2D_F64 b = new LineSegment2D_F64(1,2,3,4);

		assertEquals(a, b);
		b.a.x += UtilEjml.TEST_F64;
		assertNotEquals(a, b);
		b.a.x = 1;
		b.b.y += UtilEjml.TEST_F64;
		assertNotEquals(a, b);
	}
}