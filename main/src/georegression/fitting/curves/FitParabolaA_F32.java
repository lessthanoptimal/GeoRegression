/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

import georegression.struct.curve.Parabola_F32;
import georegression.struct.point.Point2D_F32;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.linsol.svd.SolveNullSpaceSvd_FDRM;
import org.ejml.interfaces.SolveNullSpace;

import java.util.List;

/**
 * Fit's a parabola to a set of points by finding the null space of A. More numerically stable than
 * {@link FitParabolaAtA_F32} but slower.
 *
 * @author Peter Abeles
 */
public class FitParabolaA_F32 {
	private SolveNullSpace<FMatrixRMaj> solver = new SolveNullSpaceSvd_FDRM();

	private FMatrixRMaj A = new FMatrixRMaj(4,4);
	private FMatrixRMaj nullspace = new FMatrixRMaj(4,1);

	/**
	 * Fits the parabola to the points.  Strongly recommended that you transform the points such that they have
	 * zero mean and a standard deviation along x and y axis, independently.
	 *
	 * @param points (Input) points
	 * @param output (Output) found parabola
	 * @return true if successful or false if it failed
	 */
	public boolean process(List<Point2D_F32> points , Parabola_F32 output ) {
		final int N = points.size();
		if( N < 3 )
			throw new IllegalArgumentException("At least 3 points required");

		A.reshape(N,4);

		for (int i = 0,index=0; i < N; i++) {
			Point2D_F32 p = points.get(i);

			float x = p.x;
			float y = p.y;

			A.data[index++] = x*x;
			A.data[index++] = x;
			A.data[index++] = y;
			A.data[index++] = 1;
		}

		if( !solver.process(A,1,nullspace) )
			return false;

		output.A = nullspace.data[0];
		output.B = nullspace.data[1];
		output.C = nullspace.data[2];
		output.D = nullspace.data[3];

		return true;
	}

	/**
	 * Fits the parabola to the weighted set of points.  Strongly recommended that you transform the points such
	 * that they have  zero mean and a standard deviation along x and y axis, independently.
	 *
	 * @param points (Input) points
	 * @param output (Output) found parabola
	 * @return true if successful or false if it failed
	 */
	public boolean process(List<Point2D_F32> points , float weights[], Parabola_F32 output ) {
		final int N = points.size();
		if( N < 3 )
			throw new IllegalArgumentException("At least 3 points required");

		A.reshape(N,4);

		for (int i = 0,index=0; i < N; i++) {
			Point2D_F32 p = points.get(i);

			float w= weights[i];
			float x = p.x;
			float y = p.y;

			A.data[index++] = w*x*x;
			A.data[index++] = w*x;
			A.data[index++] = w*y;
			A.data[index++] = w;
		}

		if( !solver.process(A,1,nullspace) )
			return false;

		output.A = nullspace.data[0];
		output.B = nullspace.data[1];
		output.C = nullspace.data[2];
		output.D = nullspace.data[3];

		output.A = nullspace.data[0];
		output.B = nullspace.data[1];
		output.C = nullspace.data[2];
		output.D = nullspace.data[3];

		return true;
	}

	public SolveNullSpace<FMatrixRMaj> getSolver() {
		return solver;
	}

	public void setSolver(SolveNullSpace<FMatrixRMaj> solver) {
		this.solver = solver;
	}
}
