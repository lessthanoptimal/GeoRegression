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

package georegression.struct.shapes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestRectangle2D_I32 {
	@Test
	void enforceExtents() {
		Rectangle2D_I32 a = new Rectangle2D_I32(-1,-2,50,10);
		a.enforceExtents();
		assertEquals(-1,a.x0);
		assertEquals(-2,a.y0);
		assertEquals(50,a.x1);
		assertEquals(10,a.y1);

		a = new Rectangle2D_I32(50,10,-1,-2);
		a.enforceExtents();
		assertEquals(-1,a.x0);
		assertEquals(-2,a.y0);
		assertEquals(50,a.x1);
		assertEquals(10,a.y1);
	}
}