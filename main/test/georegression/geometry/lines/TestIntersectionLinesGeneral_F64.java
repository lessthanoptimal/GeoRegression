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

package georegression.geometry.lines;

import georegression.geometry.UtilLine2D_F64;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestIntersectionLinesGeneral_F64 {
	Random rand = new Random(235);

	/**
	 * Simple noise free case with many lines
	 */
	@Test void manyLines() {
		// All lines will have this one point in common, making it the point of intersection
		Point2D_F64 center = new Point2D_F64(12.2,-19.6);
		Point2D_F64 point = new Point2D_F64();
		Point3D_F64 found = new Point3D_F64();
		IntersectionLinesGeneral_F64 alg = new IntersectionLinesGeneral_F64();

		// Increase the number of lines passed in
		for (int total = 2; total < 30; total++) {
			// Randomly generate lines by randomly creating a second point, which then defines the line
			List<LineGeneral2D_F64> lines = new ArrayList<>();
			for (int i = 0; i < total; i++) {
				point.x = rand.nextGaussian()*20;
				point.y = rand.nextGaussian()*20;

				lines.add(UtilLine2D_F64.convert(center,point,(LineGeneral2D_F64) null));
			}

			// Compare to known solution
			assertTrue(alg.process(lines, found));
			assertEquals(center.x, found.x/found.z, UtilEjml.TEST_F64);
			assertEquals(center.y, found.y/found.z, UtilEjml.TEST_F64);
		}
	}
}
