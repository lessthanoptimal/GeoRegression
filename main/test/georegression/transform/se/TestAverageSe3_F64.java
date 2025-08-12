/*
 * Copyright (C) 2025, Peter Abeles. All Rights Reserved.
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

package georegression.transform.se;

import georegression.geometry.ConvertRotation3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.se.Se3_F64;
import georegression.struct.se.SpecialEuclideanOps_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAverageSe3_F64 {
	@Test void basic() {
		var list = new ArrayList<Se3_F64>();

		// Give it a set of 3 transforms with a known solution
		list.add(SpecialEuclideanOps_F64.axisXyz(2, 3, 4, 1.2, 0, 0, null));
		list.add(SpecialEuclideanOps_F64.axisXyz(1, 3, 4, 1.1, 0, 0, null));
		list.add(SpecialEuclideanOps_F64.axisXyz(3, 3, 4, 1.3, 0, 0, null));

		var found = new Se3_F64();
		var alg = new AverageSe3_F64();
		alg.process(list, found);

		double[] xyz = ConvertRotation3D_F64.matrixToEuler(found.R, EulerType.XYZ, null);
		assertEquals(0.0, found.T.distance(2, 3, 4), GrlConstants.TEST_F64);
		assertArrayEquals(new double[]{1.2, 0, 0}, xyz, GrlConstants.TEST_F64);
	}
}
