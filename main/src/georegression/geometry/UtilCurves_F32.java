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

package georegression.geometry;

import georegression.struct.curve.ConicGeneral_F32;
import georegression.struct.curve.ParabolaGeneral_F32;
import georegression.struct.curve.ParabolaParametric_F32;

/**
 * @author Peter Abeles
 */
public class UtilCurves_F32 {
	/**
	 * Converts the conic into a parabola. If the conic isn't a parabola then it is converted into one
	 * by adjusting the value of B.
	 *
	 * @param src (Input) Conic
	 * @param dst (Output) Optional storage for converted parabola
	 * @return Parabola
	 */
	public static ParabolaGeneral_F32 convert(ConicGeneral_F32 src , ParabolaGeneral_F32 dst ) {
		if( dst == null )
			dst = new ParabolaGeneral_F32();

		// NOTE haven't put much through if this is the correct way to handle negative values of A or C
		dst.A = (float)Math.signum(src.A) * (float)Math.sqrt(Math.abs(src.A));
		dst.C = (float)Math.signum(src.C) * (float)Math.sqrt(Math.abs(src.C));
		dst.D = src.D;
		dst.E = src.E;
		dst.F = src.F;

		return dst;
	}

	/**
	 * Converts the parabola into a conic.
	 *
	 * @param src (Input) Parabola
	 * @param dst (Output) Optional storage for converted conic
	 * @return Conic
	 */
	public static ConicGeneral_F32 convert(ParabolaGeneral_F32 src , ConicGeneral_F32 dst ) {
		if( dst == null )
			dst = new ConicGeneral_F32();

		dst.A = src.A*src.A;
		dst.B = src.A*src.C*2.0f;
		dst.C = src.C*src.C;
		dst.D = src.D;
		dst.E = src.E;
		dst.F = src.F;

		return dst;
	}

//	public static void axisOfSymmetry(ParabolaGeneral_F32 curve , LineGeneral2D_F32 axis ) {
//		float A = curve.A;
//		float C = curve.C;
//		float D = curve.D;
//		float E = curve.E;
//
//		axis.A = A;
//		axis.B = C;
//		axis.C = (A*D + C*E)/(2*(A*A + C*C));
//	}
//
//	public static void vertex(ParabolaGeneral_F32 curve , Point2D_F32 vertex ) {
//
//	}

	public static ParabolaParametric_F32 convert( ParabolaGeneral_F32 src , ParabolaParametric_F32 dst ) {
		if( dst == null )
			dst = new ParabolaParametric_F32();

		float A = src.A;
		float C = src.C;
		float D = src.D;
		float E = src.E;
		float F = src.F;

		float bottom = C*D-A*E;

		dst.A = -C/bottom;
		dst.B = E/bottom;
		dst.C = -C*F/bottom;

		dst.D = A/bottom;
		dst.E = -D/bottom;
		dst.F = A*F/bottom;

		return dst;
	}

	public static ParabolaGeneral_F32 convert( ParabolaParametric_F32 src , ParabolaGeneral_F32 dst ) {
		if( dst == null )
			dst = new ParabolaGeneral_F32();

		float Z = src.A*src.E - src.B*src.D;

		dst.A = src.D/Z;
		dst.C = -src.A/Z;
		dst.D = -src.E/Z;
		dst.E = src.B/Z;

		if( (float)Math.abs(dst.A) > (float)Math.abs(dst.C) ) {
			dst.F = src.F/Z/dst.A;
		} else if( dst.C != 0 ) {
			dst.F = -src.C/Z/dst.C;
		} else {
			dst.F = 0;
		}

		return dst;
	}

}
