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

import georegression.struct.curve.PolynomialCubic1D_F64;
import georegression.struct.curve.PolynomialQuadratic1D_F64;
import georegression.struct.curve.PolynomialQuadratic2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import georegression.struct.point.Point3D_F64;
import org.ejml.data.DMatrix3x3;
import org.ejml.data.DMatrix4x4;
import org.ejml.data.DMatrix6x6;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.fixed.CommonOps_DDF3;
import org.ejml.dense.fixed.CommonOps_DDF4;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.factory.LinearSolverFactory_DDRM;
import org.ejml.interfaces.linsol.LinearSolverDense;
import org.ejml.ops.ConvertDMatrixStruct;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Fits different types of curves to points
 *
 * @author Peter Abeles
 */
public class FitCurve_F64 {

	public static PolynomialCubic1D_F64 fit(List<Point2D_F64> points , @Nullable PolynomialCubic1D_F64 output ) {
		if( output == null )
			output = new PolynomialCubic1D_F64();

		if( !fit(points,output,new DMatrix4x4()) )
			throw new RuntimeException("fit failed");

		return output;
	}

	public static PolynomialCubic1D_F64 fit_S32(List<Point2D_I32> points , @Nullable PolynomialCubic1D_F64 output ) {
		if( output == null )
			output = new PolynomialCubic1D_F64();

		if( !fit_S32(points,output,new DMatrix4x4()) )
			throw new RuntimeException("fit failed");

		return output;
	}

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
		A.set(  sx0,sx1,sx2,
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
	 * Low level version of {@link #fit(List, PolynomialCubic1D_F64)} which allows internal work variables
	 * to be passed in
	 */
	public static boolean fit(List<Point2D_F64> points , PolynomialCubic1D_F64 output , DMatrix4x4 A ) {
		if( points.size() < 4 )
			throw new IllegalArgumentException("At least 4 points are required");

		final int N = points.size();

		// Unrolled pseudo inverse
		// coef = inv(A^T*A)*A^T*y
		double sx1 = 0, sx2 = 0, sx3 = 0, sx4 = 0, sx5 = 0, sx6 = 0;

		double b0=0,b1=0,b2=0,b3=0;

		for (int i = 0; i < N; i++) {
			Point2D_F64 p = points.get(i);

			double x2 = p.x*p.x;
			double x3 = x2*p.x;
			double x4 = x2*x2;

			sx1 += p.x; sx2 += x2; sx3 += x3;
			sx4 += x4; sx5 += x4*p.x;
			sx6 += x4*x2;

			b0 += p.y;
			b1 += p.x*p.y;
			b2 += x2*p.y;
			b3 += x3*p.y;
		}

		A.set(  N,  sx1,sx2,sx3,
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
	 * Low level version of {@link #fit(List, PolynomialCubic1D_F64)} which allows internal work variables
	 * to be passed in
	 */
	public static boolean fit_S32(List<Point2D_I32> points , PolynomialCubic1D_F64 output , DMatrix4x4 A ) {
		if( points.size() < 4 )
			throw new IllegalArgumentException("At least 4 points are required");

		final int N = points.size();

		// Unrolled pseudo inverse
		// coef = inv(A^T*A)*A^T*y
		double a00=N,a01=0,a02=0,a03=0;
		double       a11=0,a12=0,a13=0;
		double             a22=0,a23=0;
		double                   a33=0;

		double b0=0,b1=0,b2=0,b3=0;

		for (int i = 0; i < N; i++) {
			Point2D_I32 p = points.get(i);

			int x2 = p.x*p.x;
			int x3 = x2*p.x;
			int x4 = x2*x2;

			a01 += p.x; a02 += x2; a03 += x3;
			a11 += x2;  a12 += x3; a13 += x4;
			a22 += x4; a23 += x4*p.x;
			a33 += x4*x2;

			b0 += p.y;
			b1 += p.x*p.y;
			b2 += x2*p.y;
			b3 += x3*p.y;
		}

		A.set(  a00,a01,a02,a03,
				a01,a11,a12,a13,
				a02,a12,a22,a23,
				a03,a13,a23,a33);

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
	 * Low level version of {@link #fit(List, PolynomialQuadratic2D_F64)} which allows internal work variables
	 * to be passed in
	 */
	public static boolean fit(List<Point3D_F64> points , PolynomialQuadratic2D_F64 output ) {

		if( points.size() < 6 )
			throw new IllegalArgumentException("At least 6 points are required");

		final int N = points.size();

		// Unrolled pseudo inverse
		// coef = inv(A^T*A)*A^T*y
		double sx1=0, sy1=0, sx1y1=0, sx2=0, sy2=0, sx2y1=0, sx1y2=0, sx2y2=0, sx3=0, sy3=0, sx3y1=0, sx1y3=0, sx4=0, sy4=0;

		double b0=0,b1=0,b2=0,b3=0,b4=0,b5=0;

		for (int i = 0; i < N; i++) {
			Point3D_F64 p = points.get(i);

			double x2 = p.x*p.x;
			double x3 = x2*p.x;
			double x4 = x2*x2;
			double y2 = p.y*p.y;
			double y3 = y2*p.y;
			double y4 = y2*y2;

			sx1 += p.x; sx2 += x2; sx3 += x3;sx4 += x4;
			sy1 += p.y; sy2 += y2; sy3 += y3;sy4 += y4;
			sx1y1 += p.x*p.y; sx2y1 += x2*p.y; sx1y2 += p.x*y2; sx2y2 +=x2*y2;
			sx3y1 += x3*p.y; sx1y3 += p.x*y3;

			b0 += p.z;
			b1 += p.x*p.z;
			b2 += p.y*p.z;
			b3 += p.x*p.y*p.z;
			b4 += x2*p.z;
			b5 += y2*p.z;
		}

		// using a fixed size matrix because the notation is much nicer
		DMatrix6x6 A = new DMatrix6x6();
		A.set(  N    ,sx1,  sy1  ,sx1y1,sx2  ,sy2  ,
				sx1  ,sx2,  sx1y1,sx2y1,sx3  ,sx1y2,
				sy1  ,sx1y1,sy2  ,sx1y2,sx2y1,sy3  ,
				sx1y1,sx2y1,sx1y2,sx2y2,sx3y1,sx1y3,
				sx2  ,sx3  ,sx2y1,sx3y1,sx4  ,sx2y2,
				sy2  ,sx1y2,sy3  ,sx1y3,sx2y2,sy4   );

		DMatrixRMaj _A = new DMatrixRMaj(6,6);
		ConvertDMatrixStruct.convert(A,_A);

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
