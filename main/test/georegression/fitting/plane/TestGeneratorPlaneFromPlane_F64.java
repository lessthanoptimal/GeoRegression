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

package georegression.fitting.plane;

import georegression.geometry.UtilPlane3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorPlaneFromPlane_F64 {
	@Test void simple() {
		var alg = new GeneratorPlaneFromPlane_F64();
		var input = new PlaneNormal3D_F64(1,2,3,4,5,6);
		var found = new PlaneGeneral3D_F64();
		alg.generate(List.of(input), found);

		PlaneGeneral3D_F64 expected = UtilPlane3D_F64.convert(input, null);

		assertEquals(expected.A, found.A);
		assertEquals(expected.B, found.B);
		assertEquals(expected.C, found.C);
		assertEquals(expected.D, found.D);
	}
}
