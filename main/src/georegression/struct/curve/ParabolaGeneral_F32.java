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

package georegression.struct.curve;

import org.ejml.UtilEjml;

/**
 * Parabola is a specific type of conic that is defined below using 5 coefficients.
 *
 * <p>(A*x + C*y)<sup>2</sup> + D*x + E*y + F = 0</p>
 *
 * @author Peter Abeles
 */
public class ParabolaGeneral_F32 {

	/**
	 * Coefficients.
	 */
	public float A,C,D,E,F;

	public float evaluate( float x , float y ) {
		float inner = A*x + C*y;
		return inner*inner + D*x + E*y +F;
	}

	/**
	 * Returns true if any of its parameters have an uncountable number
	 */
	public boolean hasUncountable() {
		return UtilEjml.isUncountable(A) || UtilEjml.isUncountable(C)
				|| UtilEjml.isUncountable(D) || UtilEjml.isUncountable(E) ||
				UtilEjml.isUncountable(F);
	}
}
