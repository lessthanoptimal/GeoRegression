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

import georegression.struct.curve.Cubic1D_F64;
import georegression.struct.curve.Quadratic1D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import org.ejml.data.DMatrix3x3;
import org.ejml.data.DMatrix4x4;
import org.ejml.dense.fixed.CommonOps_DDF3;
import org.ejml.dense.fixed.CommonOps_DDF4;

import javax.annotation.Nullable;
import java.util.List;

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
	 * <p>WARNING: This type of quadratic polynomial can't handle lines along the y-axis.</p>
	 *
	 * @param points List of points the curve is to be fit too. Must have at least 3 elements.
	 * @param output (Optional) storage for the curve
	 * @return The fitted curve
	 */
	public static Quadratic1D_F64 fit(List<Point2D_F64> points , @Nullable Quadratic1D_F64 output ) {
		if( output == null )
			output = new Quadratic1D_F64();

		if( !fit(points,output,new DMatrix3x3()) )
			throw new RuntimeException("fit failed");

		return output;
	}

	public static Quadratic1D_F64 fit_S32(List<Point2D_I32> points , @Nullable Quadratic1D_F64 output ) {
		if( output == null )
			output = new Quadratic1D_F64();

		if( !fit_S32(points,output,new DMatrix3x3()) )
			throw new RuntimeException("fit failed");

		return output;
	}

	public static Cubic1D_F64 fit(List<Point2D_F64> points , @Nullable Cubic1D_F64 output ) {
		if( output == null )
			output = new Cubic1D_F64();

		if( !fit(points,output,new DMatrix4x4()) )
			throw new RuntimeException("fit failed");

		return output;
	}

	public static Cubic1D_F64 fit_S32(List<Point2D_I32> points , @Nullable Cubic1D_F64 output ) {
		if( output == null )
			output = new Cubic1D_F64();

		if( !fit_S32(points,output,new DMatrix4x4()) )
			throw new RuntimeException("fit failed");

		return output;
	}

	/**
	 * Low level version of {@link #fit(List, Quadratic1D_F64)} which allows internal work variables
	 * to be passed in
	 */
	public static boolean fit(List<Point2D_F64> points , Quadratic1D_F64 output , DMatrix3x3 work ) {
		if( points.size() < 3 )
			throw new IllegalArgumentException("At least 3 points are required");

		final int N = points.size();

		// Unrolled pseudo inverse
		// coef = inv(A^T*A)*A^T*y
		double a00=N,a01=0,a02=0;
		double a10=0,a11=0,a12=0;
		double a20=0,a21=0,a22=0;

		double b0=0,b1=0,b2=0;

		for (int i = 0; i < N; i++) {
			Point2D_F64 p = points.get(i);

			double x2 = p.x*p.x;

			a01 += p.x;
			a02 += x2;
			a12 += x2*p.x;
			a22 += x2*x2;

			b0 += p.y;
			b1 += p.x*p.y;
			b2 += x2*p.y;
		}
		a10 = a01; a11 = a02;
		a20 = a02; a21 = a12;

		DMatrix3x3 A = work;
		A.set(a00,a01,a02,a10,a11,a12,a20,a21,a22);

		if( !CommonOps_DDF3.invert(A,A) ) // TODO use a symmetric inverse. Should be slightly faster
			return false;

		// output = inv(A)*B      Unrolled here for speed
		output.a = A.a11*b0 + A.a12*b1 + A.a13*b2;
		output.b = A.a21*b0 + A.a22*b1 + A.a23*b2;
		output.c = A.a31*b0 + A.a32*b1 + A.a33*b2;

		return true;
	}

	/**
	 * Low level version of {@link #fit(List, Quadratic1D_F64)} which allows internal work variables
	 * to be passed in
	 */
	public static boolean fit_S32(List<Point2D_I32> points , Quadratic1D_F64 output , DMatrix3x3 work ) {
		if( points.size() < 3 )
			throw new IllegalArgumentException("At least 3 points are required");

		final int N = points.size();

		// Unrolled pseudo inverse
		// coef = inv(A^T*A)*A^T*y
		double a00=N,a01=0,a02=0;
		double a10=0,a11=0,a12=0;
		double a20=0,a21=0,a22=0;

		double b0=0,b1=0,b2=0;

		for (int i = 0; i < N; i++) {
			Point2D_I32 p = points.get(i);

			int x2 = p.x*p.x;

			a01 += p.x;
			a02 += x2;
			a12 += x2*p.x;
			a22 += x2*x2;

			b0 += p.y;
			b1 += p.x*p.y;
			b2 += x2*p.y;
		}
		a10 = a01; a11 = a02;
		a20 = a02; a21 = a12;

		DMatrix3x3 A = work;
		A.set(a00,a01,a02,a10,a11,a12,a20,a21,a22);

		if( !CommonOps_DDF3.invert(A,A) )// TODO use a symmetric inverse. Should be slightly faster
			return false;

		// output = inv(A)*B      Unrolled here for speed
		output.a = A.a11*b0 + A.a12*b1 + A.a13*b2;
		output.b = A.a21*b0 + A.a22*b1 + A.a23*b2;
		output.c = A.a31*b0 + A.a32*b1 + A.a33*b2;

		return true;
	}

	/**
	 * Low level version of {@link #fit(List, Quadratic1D_F64)} which allows internal work variables
	 * to be passed in
	 */
	public static boolean fit(List<Point2D_F64> points , Cubic1D_F64 output , DMatrix4x4 A ) {
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
	 * Low level version of {@link #fit(List, Quadratic1D_F64)} which allows internal work variables
	 * to be passed in
	 */
	public static boolean fit_S32(List<Point2D_I32> points , Cubic1D_F64 output , DMatrix4x4 A ) {
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
}
