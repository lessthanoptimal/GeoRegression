/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

package georegression.geometry;

import georegression.struct.curve.ConicGeneral_F64;
import georegression.struct.curve.ParabolaGeneral_F64;
import georegression.struct.curve.ParabolaParametric_F64;
import org.ejml.data.DMatrix3x3;
import org.ejml.data.DMatrixRMaj;
import org.jetbrains.annotations.Nullable;

public class UtilCurves_F64 {

	/**
	 * Converts symmetric 3x3 matrix back into a conic.
	 *
	 * <pre>
	 *        [A    B/2  D/2]
	 *  dst = [B/2  C    E/2]
	 *        [D/2  E/2  F  ]
	 * </pre>
	 *
	 */
	public static DMatrixRMaj convert(ConicGeneral_F64 src , @Nullable DMatrixRMaj dst )
	{
		if( dst == null )
			dst = new DMatrixRMaj(3,3);
		else
			dst.reshape(3,3);

		double B = src.b/2.0;
		double D = src.d/2.0;
		double E = src.e/2.0;

		dst.data[0] = src.a; dst.data[1] = B;     dst.data[2] = D;
		dst.data[3] = B;     dst.data[4] = src.c; dst.data[5] = E;
		dst.data[6] = D;     dst.data[7] = E;     dst.data[8] = src.f;

		return dst;
	}

	/**
	 * Converts symmetric 3x3 matrix back into a conic. Only the upper right
	 * portion of src is read.
	 *
	 * <pre>
	 *     A = DST(0,0)
	 *     B = DST(0,1)*2
	 *     D = DST(0,2)*2
	 *     C = DST(1,1)
	 *     E = DST(1,2)*2
	 *     F = DST(2,2)
	 * </pre>
	 */
	public static ConicGeneral_F64 convert(DMatrix3x3 src , @Nullable ConicGeneral_F64 dst ) {
		if( dst == null )
			dst = new ConicGeneral_F64();

		dst.a = src.a11; dst.b = 2*src.a12; dst.d = 2*src.a13;
		dst.c = src.a22; dst.e = 2*src.a23;
		dst.f = src.a33;

		return dst;
	}

	/**
	 * Converts the conic into a symmetric 3x3 matrix
	 *
	 * <pre>
	 *        [A    B/2  D/2]
	 *  dst = [B/2  C    E/2]
	 *        [D/2  E/2  F  ]
	 * </pre>
	 */
	public static DMatrix3x3 convert(ConicGeneral_F64 src , @Nullable DMatrix3x3 dst )
	{
		if( dst == null )
			dst = new DMatrix3x3();

		double B = src.b/2.0;
		double D = src.d/2.0;
		double E = src.e/2.0;

		dst.a11 = src.a; dst.a12 = B;     dst.a13 = D;
		dst.a21 = B;     dst.a22 = src.c; dst.a23 = E;
		dst.a31 = D;     dst.a32 = E;     dst.a33 = src.f;

		return dst;
	}

	/**
	 * Converts symmetric 3x3 matrix back into a conic. Only the upper right
	 * portion of src is read.
	 *
	 * <pre>
	 *     A = DST(0,0)
	 *     B = DST(0,1)*2
	 *     D = DST(0,2)*2
	 *     C = DST(1,1)
	 *     E = DST(1,2)*2
	 *     F = DST(2,2)
	 * </pre>
	 *
	 */
	public static ConicGeneral_F64 convert( DMatrixRMaj src , @Nullable ConicGeneral_F64 dst ) {
		if( dst == null )
			dst = new ConicGeneral_F64();

		dst.a = src.data[0]; dst.b = 2*src.data[1]; dst.d = 2*src.data[2];
		dst.c = src.data[4]; dst.e = 2*src.data[5];
		dst.f = src.data[8];

		return dst;
	}

	/**
	 * Converts the conic into a parabola. If the conic isn't a parabola then it is converted into one
	 * by adjusting the value of B.
	 *
	 * @param src (Input) Conic
	 * @param dst (Output) Optional storage for converted parabola
	 * @return Parabola
	 */
	public static ParabolaGeneral_F64 convert(ConicGeneral_F64 src , @Nullable ParabolaGeneral_F64 dst ) {
		if( dst == null )
			dst = new ParabolaGeneral_F64();

		// NOTE haven't put much through if this is the correct way to handle negative values of A or C
		dst.a = Math.signum(src.a) * Math.sqrt(Math.abs(src.a));
		dst.c = Math.signum(src.c) * Math.sqrt(Math.abs(src.c));
		dst.d = src.d;
		dst.e = src.e;
		dst.f = src.f;

		return dst;
	}

	/**
	 * Converts the parabola into a conic.
	 *
	 * @param src (Input) Parabola
	 * @param dst (Output) Optional storage for converted conic
	 * @return Conic
	 */
	public static ConicGeneral_F64 convert(ParabolaGeneral_F64 src , @Nullable ConicGeneral_F64 dst ) {
		if( dst == null )
			dst = new ConicGeneral_F64();

		dst.a = src.a*src.a;
		dst.b = src.a*src.c*2.0;
		dst.c = src.c*src.c;
		dst.d = src.d;
		dst.e = src.e;
		dst.f = src.f;

		return dst;
	}

//	public static void axisOfSymmetry(ParabolaGeneral_F64 curve , LineGeneral2D_F64 axis ) {
//		double A = curve.A;
//		double C = curve.C;
//		double D = curve.D;
//		double E = curve.E;
//
//		axis.A = A;
//		axis.B = C;
//		axis.C = (A*D + C*E)/(2*(A*A + C*C));
//	}
//
//	public static void vertex(ParabolaGeneral_F64 curve , Point2D_F64 vertex ) {
//
//	}

	public static ParabolaParametric_F64 convert( ParabolaGeneral_F64 src ,
												  @Nullable ParabolaParametric_F64 dst ) {
		if( dst == null )
			dst = new ParabolaParametric_F64();

		double A = src.a;
		double C = src.c;
		double D = src.d;
		double E = src.e;
		double F = src.f;

		double bottom = C*D-A*E;

		if( bottom == 0 ) {
			throw new RuntimeException("Not a parabola");
		} else {
			dst.A = -C / bottom;
			dst.B = E / bottom;
			dst.C = -C * F / bottom;

			dst.D = A / bottom;
			dst.E = -D / bottom;
			dst.F = A * F / bottom;
		}

		return dst;
	}

	public static ParabolaGeneral_F64 convert( ParabolaParametric_F64 src ,
											   @Nullable ParabolaGeneral_F64 dst ) {
		if( dst == null )
			dst = new ParabolaGeneral_F64();

		double Z = src.A*src.E - src.B*src.D;

		dst.a = src.D/Z;
		dst.c = -src.A/Z;
		dst.d = -src.E/Z;
		dst.e = src.B/Z;

		if( Math.abs(dst.a) > Math.abs(dst.c) ) {
			dst.f = src.F/Z/dst.a;
		} else if( dst.c != 0 ) {
			dst.f = -src.C/Z/dst.c;
		} else {
			dst.f = 0;
		}

		return dst;
	}

}
