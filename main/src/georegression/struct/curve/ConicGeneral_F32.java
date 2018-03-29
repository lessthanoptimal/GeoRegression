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
 * <p>A*x<sup>2</sup> + B*x*y + C*y<sup>2</sup> + D*x + E*y + F=0</p>
 *
 * <p>All coefficients are real numbers and A,B,C are all not zero. The discriminant is defined as
 * B<sup>2</sup> - 4*A*C.</p>
 *
 * <p><b>Ellipse:</b>  B<sup>2</sup> - 4*A*C &lt; 0</p>
 * <p><b>Parabola:</b>  B<sup>2</sup> - 4*A*C = 0</p>
 * <p><b>Hyperbola:</b>  B<sup>2</sup> - 4*A*C &gt; 0</p>
 *
 * @author Peter Abeles
 */
public class ConicGeneral_F32 {
	/**
	 * Coefficients
	 */
	public float A,B,C,D,E,F;

	public float evaluate( float x , float y ) {
		return A*x*x + B*x*y + C*y*y + D*x + E*y + F;
	}

	/**
	 * Returns true if any of its parameters have an uncountable number
	 */
	public boolean hasUncountable() {
		return UtilEjml.isUncountable(A) || UtilEjml.isUncountable(B) || UtilEjml.isUncountable(C)
				|| UtilEjml.isUncountable(D) || UtilEjml.isUncountable(E) || UtilEjml.isUncountable(F);
	}
}
