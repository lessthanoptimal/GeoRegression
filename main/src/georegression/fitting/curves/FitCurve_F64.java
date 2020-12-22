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

package georegression.fitting.curves;

import georegression.struct.curve.PolynomialCubic1D_F64;
import georegression.struct.curve.PolynomialQuadratic1D_F64;
import georegression.struct.curve.PolynomialQuadratic2D_F64;
import org.ejml.data.DMatrix3x3;
import org.ejml.data.DMatrix4x4;
import org.ejml.data.DMatrix6x6;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.fixed.CommonOps_DDF3;
import org.ejml.dense.fixed.CommonOps_DDF4;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.factory.LinearSolverFactory_DDRM;
import org.ejml.interfaces.linsol.LinearSolverDense;
import org.ejml.ops.DConvertMatrixStruct;
import org.jetbrains.annotations.Nullable;

/**
 * Fits different types of curves to points
 *
 * @author Peter Abeles
 */
public class FitCurve_F64 {

	/**
	 * <p>Fits points to the quadratic polynomial. It's recommended that you apply a linear transform to the points
	 * to ensure that they have zero mean and a standard deviation of 1. Then reverse the transform on the result.
	 * A solution is found using the pseudo inverse and inverse by minor matrices.</p>
	 *
	 * <p>For a more numerically stable algorithm see {@link #fitQRP(double[], int, int, PolynomialQuadratic1D_F64)}</p>
	 *
	 * @param data Interleaved data [input[0], output[0], ....
	 * @param offset first index in data
	 * @param length number of elements in data that are to be read. must be divisible by 2
	 * @param output (Optional) storage for the curve
	 * @return The fitted curve
	 */
	public static boolean fitMM(double[] data, int offset , int length ,
								PolynomialQuadratic1D_F64 output , @Nullable DMatrix3x3 work ) {
		if( work == null )
			work = new DMatrix3x3();

		final int N = length/2;

		// Unrolled pseudo inverse
		// coef = inv(A^T*A)*A^T*y
		double sx0=N,sx1=0,sx2=0;
		double             sx3=0;
		double             sx4=0;

		double b0=0,b1=0,b2=0;

		int end = offset+length;
		for (int i = offset; i < end; i += 2 ) {
			double x = data[i];
			double y = data[i+1];

			double x2 = x*x;

			sx1 += x;
			sx2 += x2;
			sx3 += x2*x;
			sx4 += x2*x2;

			b0 += y;
			b1 += x*y;
			b2 += x2*y;
		}

		DMatrix3x3 A = work;
		A.setTo( sx0,sx1,sx2,
				 sx1,sx2,sx3,
				 sx2,sx3,sx4);

		if( !CommonOps_DDF3.invert(A,A) ) // TODO use a symmetric inverse. Should be slightly faster
			return false;

		// output = inv(A)*B      Unrolled here for speed
		output.a = A.a11*b0 + A.a12*b1 + A.a13*b2;
		output.b = A.a21*b0 + A.a22*b1 + A.a23*b2;
		output.c = A.a31*b0 + A.a32*b1 + A.a33*b2;

		return true;
	}

	/**
	 * <p>Fits points to the quadratic polynomial using a QRP linear solver.</p>
	 *
	 * @see FitPolynomialSolverTall_F64
	 *
	 * @param data Interleaved data [input[0], output[0], ....
	 * @param offset first index in data
	 * @param length number of elements in data that are to be read. must be divisible by 2
	 * @param output (Optional) storage for the curve
	 * @return The fitted curve
	 */
	public static boolean fitQRP( double[] data, int offset , int length ,
								  PolynomialQuadratic1D_F64 output ) {
		FitPolynomialSolverTall_F64 solver = new FitPolynomialSolverTall_F64();

		return solver.process(data,offset,length,output);
	}

	/**
	 * <p>Fits points to the cubic polynomial. It's recommended that you apply a linear transform to the points
	 * to ensure that they have zero mean and a standard deviation of 1. Then reverse the transform on the result.
	 * A solution is found using the pseudo inverse and inverse by minor matrices.</p>
	 *
	 * <p>For a more numerically stable algorithm see {@link #fitQRP(double[], int, int, PolynomialQuadratic1D_F64)}</p>
	 *
	 * @param data Interleaved data [input[0], output[0], ....
	 * @param offset first index in data
	 * @param length number of elements in data that are to be read. must be divisible by 2
	 * @param output (Optional) storage for the curve
	 * @return The fitted curve
	 */
	public static boolean fitMM(double[] data, int offset , int length , PolynomialCubic1D_F64 output , @Nullable DMatrix4x4 A ) {
		if( A == null )
			A = new DMatrix4x4();

		final int N = length/2;
		if( N < 4 )
			throw new IllegalArgumentException("Need at least 4 points and not "+N);

		// Unrolled pseudo inverse
		// coef = inv(A^T*A)*A^T*y
		double sx1 = 0, sx2 = 0, sx3 = 0, sx4 = 0, sx5 = 0, sx6 = 0;

		double b0=0,b1=0,b2=0,b3=0;

		int end = offset+length;
		for (int i = offset; i < end; i += 2 ) {
			double x = data[i];
			double y = data[i+1];

			double x2 = x*x;
			double x3 = x2*x;
			double x4 = x2*x2;

			sx1 += x; sx2 += x2; sx3 += x3;
			sx4 += x4; sx5 += x4*x;
			sx6 += x4*x2;

			b0 += y;
			b1 += x*y;
			b2 += x2*y;
			b3 += x3*y;
		}

		A.setTo( N,  sx1,sx2,sx3,
				 sx1,sx2,sx3,sx4,
				 sx2,sx3,sx4,sx5,
				 sx3,sx4,sx5,sx6);

		if( !CommonOps_DDF4.invert(A,A) )// TODO use a symmetric inverse. Should be slightly faster
			return false;

		// output = inv(A)*B      Unrolled here for speed
		output.a = A.a11*b0 + A.a12*b1 + A.a13*b2 + A.a14*b3;
		output.b = A.a21*b0 + A.a22*b1 + A.a23*b2 + A.a24*b3;
		output.c = A.a31*b0 + A.a32*b1 + A.a33*b2 + A.a34*b3;
		output.d = A.a41*b0 + A.a42*b1 + A.a43*b2 + A.a44*b3;

		return true;
	}

	/**
	 * <p>Fits points to a 2D quadratic polynomial. There are two inputs and one output for each data point.
	 * It's recommended that you apply a linear transform to the points. </p>
	 *
	 * @param data Interleaved data [inputA[0], inputB[0], output[0], ....
	 * @param offset first index in data
	 * @param length number of elements in data that are to be read. must be divisible by 2
	 * @param output (Optional) storage for the curve
	 * @return The fitted curve
	 */
	public static boolean fit(double[] data, int offset , int length , PolynomialQuadratic2D_F64 output ) {

		final int N = length/3;
		if( N < 6 )
			throw new IllegalArgumentException("Need at least 6 points and not "+N);

		// Unrolled pseudo inverse
		// coef = inv(A^T*A)*A^T*y
		double sx1=0, sy1=0, sx1y1=0, sx2=0, sy2=0, sx2y1=0, sx1y2=0, sx2y2=0, sx3=0, sy3=0, sx3y1=0, sx1y3=0, sx4=0, sy4=0;

		double b0=0,b1=0,b2=0,b3=0,b4=0,b5=0;

		int end = offset+length;
		for (int i = offset; i < end; i += 3 ) {
			double x = data[i];
			double y = data[i+1];
			double z = data[i+2];

			double x2 = x*x;
			double x3 = x2*x;
			double x4 = x2*x2;
			double y2 = y*y;
			double y3 = y2*y;
			double y4 = y2*y2;

			sx1 += x; sx2 += x2; sx3 += x3;sx4 += x4;
			sy1 += y; sy2 += y2; sy3 += y3;sy4 += y4;
			sx1y1 += x*y; sx2y1 += x2*y; sx1y2 += x*y2; sx2y2 +=x2*y2;
			sx3y1 += x3*y; sx1y3 += x*y3;

			b0 += z;
			b1 += x*z;
			b2 += y*z;
			b3 += x*y*z;
			b4 += x2*z;
			b5 += y2*z;
		}

		// using a fixed size matrix because the notation is much nicer
		DMatrix6x6 A = new DMatrix6x6();
		A.setTo(N    ,sx1,  sy1  ,sx1y1,sx2  ,sy2  ,
				sx1  ,sx2,  sx1y1,sx2y1,sx3  ,sx1y2,
				sy1  ,sx1y1,sy2  ,sx1y2,sx2y1,sy3  ,
				sx1y1,sx2y1,sx1y2,sx2y2,sx3y1,sx1y3,
				sx2  ,sx3  ,sx2y1,sx3y1,sx4  ,sx2y2,
				sy2  ,sx1y2,sy3  ,sx1y3,sx2y2,sy4   );

		DMatrixRMaj _A = new DMatrixRMaj(6,6);
		DConvertMatrixStruct.convert(A,_A);

		// pseudo inverse is required to handle degenerate matrices, e.g. lines
		LinearSolverDense<DMatrixRMaj> solver = LinearSolverFactory_DDRM.pseudoInverse(true);
		if( !solver.setA(_A) )
			return false;
		solver.invert(_A);

		DMatrixRMaj B = new DMatrixRMaj(6,1,true,b0,b1,b2,b3,b4,b5);
		DMatrixRMaj Y = new DMatrixRMaj(6,1);
		CommonOps_DDRM.mult(_A,B,Y);

		// output = inv(A)*B      Unrolled here for speed
		output.a = Y.data[0];
		output.b = Y.data[1];
		output.c = Y.data[2];
		output.d = Y.data[3];
		output.e = Y.data[4];
		output.f = Y.data[5];

		return true;
	}
}
