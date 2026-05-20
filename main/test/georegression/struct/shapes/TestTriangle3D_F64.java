/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

import georegression.GeoRegressionJUnit;
import org.ejml.MapPrintFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTriangle3D_F64 extends GeoRegressionJUnit {
	@Test void formatMap() {
		var a = new Triangle3D_F64().setTo(1,2,3,4.1234,5,6,7,8,9);
		String found = a.formatMap(new MapPrintFormat().withPrecision(2));
		assertEquals("{v0: {x: 1, y: 2, z: 3}, v1: {x: 4.12, y: 5, z: 6}, v2: {x: 7, y: 8, z: 9}}", found);
	}
}
