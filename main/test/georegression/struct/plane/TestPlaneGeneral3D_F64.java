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

package georegression.struct.plane;

import georegression.geometry.UtilPoint3D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlaneGeneral3D_F64 {
	@Test void evaluate() {
		var a = new PlaneGeneral3D_F64(0, 0, 1, 4);

		assertEquals(0.0, a.evaluate(1, 2, -4), UtilEjml.TEST_F64);
		assertEquals(0.0, a.evaluate(-1, 2, -4), UtilEjml.TEST_F64);
		assertNotEquals(0.0, a.evaluate(1, 2, 1), UtilEjml.TEST_F64);
	}

	@Test void normalize() {
		var a = new PlaneGeneral3D_F64(1, 2, 3, 4);
		a.normalize();

		assertEquals(1.0, UtilPoint3D_F64.norm(a.A, a.B, a.C), UtilEjml.TEST_F64);

		double div = UtilPoint3D_F64.norm(1, 2, 3);
		assertEquals(4.0/div, a.D, UtilEjml.TEST_F64);
	}

	@Test void equals() {
		var a = new PlaneGeneral3D_F64(1, 2, 3, 4);
		assertTrue(a.equals(new PlaneGeneral3D_F64(1, 2, 3, 4)));
		assertFalse(a.equals(new PlaneGeneral3D_F64(1, 3, 3, 4)));
		assertFalse(a.equals(new PlaneGeneral3D_F64(2, 2, 3, 4)));
		assertFalse(a.equals(new PlaneGeneral3D_F64(1, 2, 0, 4)));
		assertFalse(a.equals(new PlaneGeneral3D_F64(1, 2, 3, 0)));
	}
}
