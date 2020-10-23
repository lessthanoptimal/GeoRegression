/*
 * Copyright (C)  2020, Peter Abeles. All Rights Reserved.
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

import georegression.metric.Intersection2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Rectangle2D_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFitPolygon2D_F64 {
	private final Random rand = new Random(345);

	@Test void rectangleAabb_empty() {
		var r = new Rectangle2D_F64(1,2,3,4);
		assertSame(r, FitPolygon2D_F64.rectangleAabb(new ArrayList<>(),r));
		assertTrue(r.isEquals(0.0, 0.0, 0.0, 0.0, 0.0));
	}

	@Test void rectangleAabb() {
		List<Point2D_F64> list = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			list.add(new Point2D_F64(rand.nextGaussian(), rand.nextGaussian()));
		}

		Rectangle2D_F64 r = FitPolygon2D_F64.rectangleAabb(list, null);
		for (Point2D_F64 p : list) {
			assertTrue(Intersection2D_F64.contains2(r, p.x, p.y));
		}
	}
}
