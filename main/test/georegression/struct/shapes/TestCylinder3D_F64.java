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

package georegression.struct.shapes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCylinder3D_F64 {
	@Test void isIdentical() {
		var c = new Cylinder3D_F64(1, 2, 3, 4, 5, 6, 7);
		assertTrue(c.isIdentical(new Cylinder3D_F64(1, 2, 3, 4, 5, 6, 7), 0.0));
		assertTrue(c.isIdentical(new Cylinder3D_F64(1, 2, 3, 4, 5, 6, 7.1), 0.1));
		assertFalse(c.isIdentical(new Cylinder3D_F64(1, 2, 3, 4, 5, 6, 7.11), 0.1));
	}
}
