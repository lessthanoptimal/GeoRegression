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

import georegression.metric.Intersection3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Box3D_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestFitPolygon3D_F64 {
	private final Random rand = new Random(345);

	@Test
	void boxAabb_empty() {
		var r = new Box3D_F64(1,2,3,4,5,6);
		assertSame(r, FitPolygon3D_F64.boxAabb(new ArrayList<>(),r));
		assertTrue(r.isEquals(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
	}

	@Test void boxAabb() {
		List<Point3D_F64> list = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			list.add(new Point3D_F64(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian()));
		}

		Box3D_F64 r = FitPolygon3D_F64.boxAabb(list, null);
		for (Point3D_F64 p : list) {
			assertTrue(Intersection3D_F64.contained2(r, p));
		}
	}
}
