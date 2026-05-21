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

package georegression.struct.point;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestPoint2D_F64 extends GenericGeoTupleTests_F64<Point2D_F64> {

	public TestPoint2D_F64() {
		super( new Point2D_F64(), 2 );
	}

	@Test void equals_vector() {
		var a = new Point2D_F64();
		var b = new Vector2D_F64();

		// numerically they are identical, but not the same type so this should fail
		assertNotEquals(a, b);
	}

	@Test void equals() {
		var a = new Point2D_F64(1, 2);
		assertNotEquals(new Point2D_F64(1, 3), a);
		assertEquals(new Point2D_F64(1, 2), a);
		assertNotEquals(new Vector2D_F64(1, 2), a);
	}
}
