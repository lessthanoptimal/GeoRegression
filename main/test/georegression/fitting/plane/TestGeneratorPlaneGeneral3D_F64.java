/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.plane;

import georegression.geometry.UtilPoint3D_F64;
import georegression.metric.Distance3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.point.Point3D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestGeneratorPlaneGeneral3D_F64 {
	Random rand = new Random(2435);

	@Test void generate() {
		var normal = new PlaneNormal3D_F64(1, -2, 3, -0.1, 0.3, 0.5);
		normal.n.normalize();
		List<Point3D_F64> points = UtilPoint3D_F64.random(normal, 10.0, 30, rand);

		var found = new PlaneGeneral3D_F64();

		var alg = new GeneratorPlaneGeneral3D_F64();
		assertTrue(alg.generate(points, found));

		for (Point3D_F64 p : points) {
			assertEquals(0.0, Distance3D_F64.distanceSigned(found, p), UtilEjml.TEST_F64);
		}
	}

	@Test void getMinimumPoints() {
		assertEquals(4, new GeneratorPlaneGeneral3D_F64().getMinimumPoints());
	}
}
