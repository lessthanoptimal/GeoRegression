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
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestPointCloudToNormals_F64 {
	private final Random rand = new Random(234);

	/**
	 * Given a known plane, compute the normals for all the points the plane and compare
	 */
	@Test void pointsOnAPlane() {
		var points = new DogArray<>(Point3D_F64::new);

		for (int i = 0; i < 100; i++) {
			points.grow().setTo(rand.nextGaussian(), rand.nextGaussian(), 0);
		}

		var found = new DogArray<>(Vector3D_F64::new);
		var alg = new PointCloudToNormals_F64();
		alg.convert(points.toList(), points.toList(), found);

		for (int i = 0; i < found.size; i++) {
			Vector3D_F64 v = found.get(i);
			// make sure z is pointed up
			if (v.z < 0)
				v.scale(-1);
			assertEquals(0.0, v.distance(0.0, 0.0, 1.0), UtilEjml.TEST_F64);
		}
	}

	@Test void removeInvalidPoints() {
		var vectors = new DogArray<>(Vector3D_F64::new);

		for (int i = 0; i < 100; i++) {
			vectors.grow().setTo(rand.nextGaussian(), rand.nextGaussian(),  rand.nextGaussian());
		}
		vectors.get(10).x = Double.NaN;
		vectors.get(25).x = Double.NaN;

		PointCloudToNormals_F64.removeInvalidPoints(vectors);

		assertEquals(98, vectors.size);
	}
}