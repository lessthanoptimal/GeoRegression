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

import georegression.GeoRegressionJUnit;
import georegression.misc.GrlConstants;
import org.ejml.MapPrintFormat;
import org.ejml.MatrixPrintFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPoint2D_I32 extends GeoRegressionJUnit {

	@Test void distance2() {
		Point2D_I32 a = new Point2D_I32(1,2);
		Point2D_I32 b = new Point2D_I32(3,5);

		assertEquals(4+9,a.distance2(b));
	}

	@Test void distance() {
		Point2D_I32 a = new Point2D_I32(1,2);
		Point2D_I32 b = new Point2D_I32(3,5);

		assertEquals(Math.sqrt(2*2 + 3*3),a.distance(b), GrlConstants.TEST_F64);
	}

	@Test void copy() {
		Point2D_I32 a = new Point2D_I32(1,2);
		Point2D_I32 b = a.copy();

		assertEquals(a.x,b.x);
		assertEquals(a.y,b.y);
	}

	@Test void format_Matrix() {
		var a = new Point2D_I32(1, 2);
		String found = a.format(new MatrixPrintFormat());
		assertEquals("{1, 2}", found);
	}

	@Test void formatMap() {
		var a = new Point2D_I32(1, 2);
		String found = a.formatMap(new MapPrintFormat());
		assertEquals("{x: 1, y: 2}", found);
	}
}
