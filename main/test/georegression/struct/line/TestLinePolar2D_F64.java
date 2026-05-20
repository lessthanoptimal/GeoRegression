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

package georegression.struct.line;

import georegression.GeoRegressionJUnit;
import org.ejml.MapPrintFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLinePolar2D_F64 extends GeoRegressionJUnit {
	@Test void formatMap() {
		var line = new LinePolar2D_F64(1, 4.1234);
		String found = line.formatMap(new MapPrintFormat().withPrecision(2));
		assertEquals("{distance: 1, angle: 4.12}", found);
	}
}
