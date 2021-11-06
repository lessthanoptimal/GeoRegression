/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

import georegression.fitting.FitShapeToPoints_F64;
import georegression.struct.curve.ConicGeneral_F64;
import georegression.struct.point.Point2D_F64;
import org.ejml.data.DMatrix6x6;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.fixed.CommonOps_DDF6;
import org.ejml.dense.row.linsol.svd.SolveNullSpaceSvd_DDRM;
import org.ejml.interfaces.SolveNullSpace;
import org.ejml.ops.DConvertMatrixStruct;

import java.util.List;

/**
 * Fit's a parabola to a set of points by finding the null space of A<sup>T</sup>*A. This is faster but less
 * numerically stable than techniques which don't explicitly square the matrix.
 *
 * @author Peter Abeles
 */
public class FitConicAtA_F64 implements FitShapeToPoints_F64<Point2D_F64,ConicGeneral_F64> {
	private SolveNullSpace<DMatrixRMaj> solver = new SolveNullSpaceSvd_DDRM();

	private DMatrix6x6 ATA = new DMatrix6x6();
	private DMatrixRMaj tmp = new DMatrixRMaj(6,6);
	private DMatrixRMaj nullspace = new DMatrixRMaj(6,1);

	/**
	 * Fits the conic to the points. Strongly recommended that you transform the points such that they have
	 * zero mean and a standard deviation along x and y axis, independently.
	 *
	 * @param points (Input) points
	 * @param output (Output) found conic
	 * @return true if successful or false if it failed
	 */
	@Override
	public boolean process(List<Point2D_F64> points , ConicGeneral_F64 output ) {
		final int N = points.size();
		if( N < 3 )
			throw new IllegalArgumentException("At least 3 points required");

		CommonOps_DDF6.fill(ATA,0);

		for (int i = 0; i < N; i++) {
			Point2D_F64 p = points.get(i);

			double x = p.x;
			double y = p.y;
			double xx = x*x;
			double xxx = xx*x;
			double yy = y*y;
			double yyy = yy*y;

			ATA.a11 += xx*xx;
			ATA.a12 += xxx*y;
			ATA.a13 += xx*yy;
			ATA.a14 += xxx;
			ATA.a15 += xx*y;
			ATA.a16 += xx;

			ATA.a22 += xx*yy;
			ATA.a23 += x*yyy;
//			ATA.a24 += xx*y;
			ATA.a25 += x*yy;
			ATA.a26 += x*y;

			ATA.a33 += yyy*y;
//			ATA.a34 += x*yy;
			ATA.a35 += yyy;
			ATA.a36 += yy;

//			ATA.a44 += xx;
			ATA.a45 += x*y;
			ATA.a46 += x;

//			ATA.a55 += yy;
			ATA.a56 += y;

		}
		ATA.a21 = ATA.a12;
		ATA.a24 = ATA.a15;

		ATA.a31 = ATA.a13;
		ATA.a32 = ATA.a23;
		ATA.a34 = ATA.a25;

		ATA.a41 = ATA.a14;
		ATA.a42 = ATA.a24;
		ATA.a43 = ATA.a34;
		ATA.a44 = ATA.a16;

		ATA.a51 = ATA.a15;
		ATA.a52 = ATA.a25;
		ATA.a53 = ATA.a35;
		ATA.a54 = ATA.a45;
		ATA.a55 = ATA.a36;

		ATA.a61 = ATA.a16;
		ATA.a62 = ATA.a26;
		ATA.a63 = ATA.a36;
		ATA.a64 = ATA.a56;
		ATA.a65 = ATA.a56;
		ATA.a66 = N;

		DConvertMatrixStruct.convert(ATA,tmp);

		if( !solver.process(tmp,1,nullspace) )
			return false;

		output.A = nullspace.data[0];
		output.B = nullspace.data[1];
		output.C = nullspace.data[2];
		output.D = nullspace.data[3];
		output.E = nullspace.data[4];
		output.F = nullspace.data[5];

		return true;
	}

	/**
	 * Fits the conic to the weighted set of points. Strongly recommended that you transform the points such
	 * that they have  zero mean and a standard deviation along x and y axis, independently.
	 *
	 * @param points (Input) points
	 * @param output (Output) found conic
	 * @return true if successful or false if it failed
	 */
	@Override
	public boolean process(List<Point2D_F64> points , double weights[], ConicGeneral_F64 output ) {
		final int N = points.size();
		if( N < 3 )
			throw new IllegalArgumentException("At least 3 points required");

		CommonOps_DDF6.fill(ATA,0);

		for (int i = 0; i < N; i++) {
			Point2D_F64 p = points.get(i);

			double w = weights[i];
			w *= w;
			double x = p.x;
			double y = p.y;
			double xx = x*x;
			double xxx = xx*x;
			double yy = y*y;
			double yyy = yy*y;

			ATA.a11 += w*xx*xx;
			ATA.a12 += w*xxx*y;
			ATA.a13 += w*xx*yy;
			ATA.a14 += w*xxx;
			ATA.a15 += w*xx*y;
			ATA.a16 += w*xx;

			ATA.a22 += w*xx*yy;
			ATA.a23 += w*x*yyy;
//			ATA.a24 += w*xx*y;
			ATA.a25 += w*x*yy;
			ATA.a26 += w*x*y;

			ATA.a33 += w*yyy*y;
//			ATA.a34 += w*x*yy;
			ATA.a35 += w*yyy;
			ATA.a36 += w*yy;

//			ATA.a44 += w*xx;
			ATA.a45 += w*x*y;
			ATA.a46 += w*x;

//			ATA.a55 += w*yy;
			ATA.a56 += w*y;

			ATA.a66 += w;
		}
		ATA.a21 = ATA.a12;
		ATA.a24 = ATA.a15;

		ATA.a31 = ATA.a13;
		ATA.a32 = ATA.a23;
		ATA.a34 = ATA.a25;

		ATA.a41 = ATA.a14;
		ATA.a42 = ATA.a24;
		ATA.a43 = ATA.a34;
		ATA.a44 = ATA.a16;

		ATA.a51 = ATA.a15;
		ATA.a52 = ATA.a25;
		ATA.a53 = ATA.a35;
		ATA.a54 = ATA.a45;
		ATA.a55 = ATA.a36;

		ATA.a61 = ATA.a16;
		ATA.a62 = ATA.a26;
		ATA.a63 = ATA.a36;
		ATA.a64 = ATA.a56;
		ATA.a65 = ATA.a56;

		DConvertMatrixStruct.convert(ATA,tmp);

		if( !solver.process(tmp,1,nullspace) )
			return false;

		output.A = nullspace.data[0];
		output.B = nullspace.data[1];
		output.C = nullspace.data[2];
		output.D = nullspace.data[3];
		output.E = nullspace.data[4];
		output.F = nullspace.data[5];

		return true;
	}

	public SolveNullSpace<DMatrixRMaj> getSolver() {
		return solver;
	}

	public void setSolver(SolveNullSpace<DMatrixRMaj> solver) {
		this.solver = solver;
	}
}
