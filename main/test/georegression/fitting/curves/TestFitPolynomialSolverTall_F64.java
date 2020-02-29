/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.curves;

import georegression.struct.curve.PolynomialGeneral1D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFitPolynomialSolverTall_F64
{
	Random rand = new Random(234);

	/**
	 * Fit randomly generated data with no noise for various degree polynomials
	 */
	@Test
	void perfect() {
		FitPolynomialSolverTall_F64 alg = new FitPolynomialSolverTall_F64();

		for (int degree = 0; degree < 5; degree++) {
			PolynomialGeneral1D_F64 poly = new PolynomialGeneral1D_F64(degree);

			for (int i = 0; i <= degree; i++) {
				poly.set(i, rand.nextGaussian());
			}
			int N = 30;
			double[] data = new double[N*2];
			for (int i = 0; i < N; i++) {
				double x = rand.nextGaussian();
				double y = poly.evaluate(x);
				data[i*2  ] = x;
				data[i*2+1] = y;
			}

			PolynomialGeneral1D_F64 found = new PolynomialGeneral1D_F64(degree);
			assertTrue(alg.process(data,0, data.length,found));

			for (int i = 0; i < poly.size(); i++) {
				assertEquals(poly.get(i),found.get(i), UtilEjml.TEST_F64);
			}
		}
	}
}
