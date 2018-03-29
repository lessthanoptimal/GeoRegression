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

import georegression.struct.curve.QuadraticPolynomial2D_F32;
import georegression.struct.point.Point2D_F32;
import org.ejml.data.FMatrix3x3;
import org.ejml.dense.fixed.CommonOps_FDF3;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Fits different types of curves to points
 *
 * @author Peter Abeles
 */
public class FitCurve_F32 {
	/**
	 * <p>Fits points to the quadratic polynomial. It's recommended that you apply a linear transform to the points
	 * to ensure that they have zero mean and a standard deviation of 1. Then reverse the transform on the result.
	 * A solution is using the pseudo inverse and inverse by minor matrices.</p>
	 *
	 * <p>WARNING: This type of quadratic polynomial can't handle lines along the y-axis.</p>
	 *
	 * @param points List of points the curve is to be fit too. Must have at least 3 elements.
	 * @param output (Optional) storage for the curve
	 * @return The fitted curve
	 */
	public static QuadraticPolynomial2D_F32 fit(List<Point2D_F32> points , @Nullable QuadraticPolynomial2D_F32 output ) {
		if( output == null )
			output = new QuadraticPolynomial2D_F32();

		fit(points,output,new FMatrix3x3());

		return output;
	}

	/**
	 * Low level version of {@link #fit(List, QuadraticPolynomial2D_F32)} which allows internal work variables
	 * to be passed in
	 */
	public static void fit(List<Point2D_F32> points , QuadraticPolynomial2D_F32 output , FMatrix3x3 work ) {
		if( points.size() < 3 )
			throw new IllegalArgumentException("At least 3 points are required");

		final int N = points.size();

		// Unrolled pseudo inverse
		// coef = inv(A^T*A)*A^T*y
		float a00=N,a01=0,a02=0;
		float a10=0,a11=0,a12=0;
		float a20=0,a21=0,a22=0;

		float b0=0,b1=0,b2=0;

		for (int i = 0; i < N; i++) {
			Point2D_F32 p = points.get(i);

			float x2 = p.x*p.x;

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

		FMatrix3x3 A = work;
		A.set(a00,a01,a02,a10,a11,a12,a20,a21,a22);

		CommonOps_FDF3.invert(A,A);// TODO use a symmetric inverse. Should be slightly faster

		// output = inv(A)*B      Unrolled here for speed
		output.a0 = A.a11*b0 + A.a12*b1 + A.a13*b2;
		output.a1 = A.a21*b0 + A.a22*b1 + A.a23*b2;
		output.a2 = A.a31*b0 + A.a32*b1 + A.a33*b2;
	}
}
