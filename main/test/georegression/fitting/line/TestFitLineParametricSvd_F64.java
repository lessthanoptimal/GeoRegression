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

package georegression.fitting.line;

import georegression.metric.Distance2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.point.Point2D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestFitLineParametricSvd_F64 {
	@Test
	void unweighted() {
		LineParametric2D_F64 expected = new LineParametric2D_F64();
		expected.p.setTo(6.5,-3.5);

		for (int i = 0; i < 16; i++) {
			double theta = 2.0 * Math.PI * i/16;
			expected.slope.x = Math.cos(theta);
			expected.slope.y = Math.sin(theta);

			List<Point2D_F64> pts = new ArrayList<>();
			for (int j = 0; j < 100; j++) {
				pts.add( expected.getPointOnLine(j*200.0-40) );
			}

			FitLineParametricSvd_F64 alg = new FitLineParametricSvd_F64();
			LineParametric2D_F64 found = new LineParametric2D_F64();
			assertTrue(alg.fit(pts,found));

			double tol = (found.p.norm()+1)*UtilEjml.TEST_F64;
			assertEquals(0,Distance2D_F64.distance(expected,found.p), tol);

			// if the slope is equivalent dot product of perpendicular is zero
			found.slope.normalize();
			double a = found.slope.x*expected.slope.y - found.slope.y*expected.slope.x;
			assertEquals(0,a, UtilEjml.TEST_F64);
		}
	}

}
