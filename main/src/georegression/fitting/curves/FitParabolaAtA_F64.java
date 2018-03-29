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

import georegression.struct.curve.Parabola_F64;
import georegression.struct.point.Point2D_F64;
import org.ejml.data.DMatrix4x4;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.fixed.CommonOps_DDF4;
import org.ejml.dense.row.linsol.svd.SolveNullSpaceSvd_DDRM;
import org.ejml.interfaces.SolveNullSpace;
import org.ejml.ops.ConvertDMatrixStruct;

import java.util.List;

/**
 * Fit's a parabola to a set of points by finding the null space of A<sup>T</sup>*A. This is faster but less
 * numerically stable than techniques which don't explicitly square the matrix.
 *
 * @author Peter Abeles
 */
public class FitParabolaAtA_F64 {
	private SolveNullSpace<DMatrixRMaj> solver = new SolveNullSpaceSvd_DDRM();

	private DMatrix4x4 ATA = new DMatrix4x4();
	private DMatrixRMaj tmp = new DMatrixRMaj(4,4);
	private DMatrixRMaj nullspace = new DMatrixRMaj(4,1);

	/**
	 * Fits the parabola to the points.  Strongly recommended that you transform the points such that they have
	 * zero mean and a standard deviation along x and y axis, independently.
	 *
	 * @param points (Input) points
	 * @param output (Output) found parabola
	 * @return true if successful or false if it failed
	 */
	public boolean process(List<Point2D_F64> points , Parabola_F64 output ) {
		final int N = points.size();
		if( N < 3 )
			throw new IllegalArgumentException("At least 3 points required");

		CommonOps_DDF4.fill(ATA,0);

		for (int i = 0; i < N; i++) {
			Point2D_F64 p = points.get(i);

			double x = p.x;
			double y = p.y;
			double xx = x*x;

			ATA.a11 += xx*xx;
			ATA.a12 += xx*x;
			ATA.a13 += xx*y;
			ATA.a14 += xx;

//			ATA.a22 += xx;
			ATA.a23 += x*y;
			ATA.a24 += x;

			ATA.a33 += y*y;
			ATA.a34 += y;
		}
		ATA.a21 = ATA.a12;
		ATA.a22 = ATA.a14;

		ATA.a31 = ATA.a13;
		ATA.a32 = ATA.a23;

		ATA.a41 = ATA.a14;
		ATA.a42 = ATA.a24;
		ATA.a43 = ATA.a34;
		ATA.a44 = N;

		ConvertDMatrixStruct.convert(ATA,tmp);

		if( !solver.process(tmp,1,nullspace) )
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
	public boolean process(List<Point2D_F64> points , double weights[], Parabola_F64 output ) {
		final int N = points.size();
		if( N < 3 )
			throw new IllegalArgumentException("At least 3 points required");

		CommonOps_DDF4.fill(ATA,0);

		for (int i = 0; i < N; i++) {
			Point2D_F64 p = points.get(i);
			double w = weights[i];
			w *= w;

			double x = p.x;
			double y = p.y;
			double xx = x*x;

			ATA.a11 += w*xx*xx;
			ATA.a12 += w*xx*x;
			ATA.a13 += w*xx*y;
			ATA.a14 += w*xx;

//			ATA.a22 += w*xx;
			ATA.a23 += w*x*y;
			ATA.a24 += w*x;

			ATA.a33 += w*y*y;
			ATA.a34 += w*y;
			ATA.a44 += w;
		}
		ATA.a21 = ATA.a12;
		ATA.a22 = ATA.a14;

		ATA.a31 = ATA.a13;
		ATA.a32 = ATA.a23;

		ATA.a41 = ATA.a14;
		ATA.a42 = ATA.a24;
		ATA.a43 = ATA.a34;

		ConvertDMatrixStruct.convert(ATA,tmp);

		if( !solver.process(tmp,1,nullspace) )
			return false;

		output.A = nullspace.data[0];
		output.B = nullspace.data[1];
		output.C = nullspace.data[2];
		output.D = nullspace.data[3];

		return true;
	}

	public SolveNullSpace<DMatrixRMaj> getSolver() {
		return solver;
	}

	public void setSolver(SolveNullSpace<DMatrixRMaj> solver) {
		this.solver = solver;
	}
}
