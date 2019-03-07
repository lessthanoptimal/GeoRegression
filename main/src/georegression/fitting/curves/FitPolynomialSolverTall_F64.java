/*
 * Copyright (C) 2011-2019, Peter Abeles. All Rights Reserved.
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

import georegression.struct.curve.PolynomialCurve_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.factory.LinearSolverFactory_DDRM;
import org.ejml.interfaces.linsol.LinearSolverDense;

/**
 * Fits a polynomial using a {@link LinearSolverDense} and the equation A*X=B.
 * Different solvers can be selected to trade efficiency and robustness.
 */
public class FitPolynomialSolverTall_F64 {

	LinearSolverDense<DMatrixRMaj> solver;

	DMatrixRMaj A = new DMatrixRMaj(1,1);
	DMatrixRMaj b = new DMatrixRMaj(1,1);
	DMatrixRMaj x = new DMatrixRMaj(1,1);

	public FitPolynomialSolverTall_F64( LinearSolverDense<DMatrixRMaj> solver ) {
		this.solver = solver;
	}

	public FitPolynomialSolverTall_F64() {
		this(LinearSolverFactory_DDRM.qrp(true,false));
	}

	/**
	 * Fits the polynomial curve to the data.
	 *
	 * @param data interleaved input output
	 * @param offset first element in data
	 * @param length number of elements to read in data
	 * @param output The fitted polynomial
	 * @return true if solver worked or false if it didn't
	 */
	public boolean process(double[] data, int offset , int length , PolynomialCurve_F64 output ) {
		int N = length/2;

		int numCoefs = output.size();

		A.reshape(N,numCoefs);
		b.reshape(N,1);
		x.reshape(numCoefs,1);

		int end = offset+length;
		for (int i = offset, idxA=0; i < end; i += 2) {
			double x = data[i];
			double y = data[i+1];

			double pow = 1.0;
			for (int j = 0; j < numCoefs; j++) {
				A.data[idxA++] = pow;
				pow *= x;
			}

			b.data[i/2] = y;
		}

		if( !solver.setA(A) )
			return false;
		solver.solve(b,x);

		for (int i = 0; i < numCoefs; i++) {
			output.set(i, x.data[i]);
		}
		return true;
	}
}
