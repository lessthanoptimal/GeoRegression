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

import georegression.metric.Intersection2D_I32;
import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Rectangle2D_I32;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestFitPolygon2D_I32 {
	private final Random rand = new Random(345);

	@Test
	void rectangleAabb_empty() {
		var r = new Rectangle2D_I32(1,2,3,4);
		assertSame(r, FitPolygon2D_I32.rectangleAabb(new ArrayList<>(),r));
		assertTrue(r.isEquals(0, 0, 0, 0));
	}

	@Test void rectangleAabb() {
		List<Point2D_I32> list = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			list.add(new Point2D_I32(rand.nextInt(50)-25, rand.nextInt(50)-25));
		}

		Rectangle2D_I32 r = FitPolygon2D_I32.rectangleAabb(list, null);
		for (Point2D_I32 p : list) {
			assertTrue(Intersection2D_I32.contains(r, p.x, p.y));
		}
	}
}
