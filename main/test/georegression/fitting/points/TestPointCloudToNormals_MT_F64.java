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

package georegression.fitting.points;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import org.ddogleg.struct.DogArray;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestPointCloudToNormals_MT_F64 {
	private final Random rand = new Random(234);

	@Test void compareToSingleThread() {
		var points = new DogArray<>(Point3D_F64::new);

		for (int i = 0; i < 200; i++) {
			points.grow().setTo(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
		}

		var single = new DogArray<>(Vector3D_F64::new);
		var multi = new DogArray<>(Vector3D_F64::new);

		new PointCloudToNormals_F64().convert(points.toList(), points.toList(), single);

		var alg = new PointCloudToNormals_MT_F64();
		alg.minimumPointsConcurrent = 0; // make sure it runs it with the threaded code
		alg.convert(points.toList(), points.toList(), multi);

		assertEquals(single.size, multi.size);
		for (int i = 0; i < single.size; i++) {
			assertTrue(single.get(i).isIdentical(multi.get(i), 0.0));
		}
	}
}