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

import georegression.struct.curve.ConicGeneral_F64;
import georegression.struct.curve.ParabolaGeneral_F64;

/**
 * @author Peter Abeles
 */
public class UtilCurves_F64 {
	/**
	 * Converts the conic into a parabola. If the conic isn't a parabola then it is converted into one
	 * by adjusting the value of B.
	 *
	 * @param src (Input) Conic
	 * @param dst (Output) Optional storage for converted parabola
	 * @return Parabola
	 */
	public static ParabolaGeneral_F64 convert(ConicGeneral_F64 src , ParabolaGeneral_F64 dst ) {
		if( dst == null )
			dst = new ParabolaGeneral_F64();

		dst.A = Math.sqrt(src.A);
		dst.C = Math.sqrt(src.C);
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
	public static ConicGeneral_F64 convert(ParabolaGeneral_F64 src , ConicGeneral_F64 dst ) {
		if( dst == null )
			dst = new ConicGeneral_F64();

		dst.A = src.A*src.A;
		dst.B = src.A*src.C*2.0;
		dst.C = src.C*src.C;
		dst.D = src.D;
		dst.E = src.E;
		dst.F = src.F;

		return dst;
	}
}
