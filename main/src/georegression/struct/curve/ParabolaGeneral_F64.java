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

package georegression.struct.curve;

import org.ejml.UtilEjml;

/**
 * Parabola is a specific type of conic that is defined below using 5 coefficients.
 *
 * <p>(A*x + C*y)<sup>2</sup> + D*x + E*y + F = 0</p>
 *
 * @author Peter Abeles
 */
public class ParabolaGeneral_F64 {

	/**
	 * Coefficients.
	 */
	public double A,C,D,E,F;

	public ParabolaGeneral_F64(double a, double c, double d, double e, double f) {
		A = a;
		C = c;
		D = d;
		E = e;
		F = f;
	}

	public ParabolaGeneral_F64( ParabolaGeneral_F64 original ) {
		this.setTo(original);
	}

	public ParabolaGeneral_F64(){}

	public void setTo(ParabolaGeneral_F64 original ) {
		this.A = original.A;
		this.C = original.C;
		this.D = original.D;
		this.E = original.E;
		this.F = original.F;
	}

	public void setTo(double a, double c, double d, double e, double f) {
		A = a;
		C = c;
		D = d;
		E = e;
		F = f;
	}

	public double evaluate(double x , double y ) {
		double inner = A*x + C*y;
		return inner*inner + D*x + E*y +F;
	}

	public void toArray( double[] array ) {
		array[0] = A;
		array[1] = C;
		array[2] = D;
		array[3] = E;
		array[4] = F;
	}

	public void fromArray( double[] array ) {
		A = array[0];
		C = array[1];
		D = array[2];
		E = array[3];
		F = array[4];
	}

	/**
	 * Returns true if any of its parameters have an uncountable number
	 */
	public boolean hasUncountable() {
		return UtilEjml.isUncountable(A) || UtilEjml.isUncountable(C)
				|| UtilEjml.isUncountable(D) || UtilEjml.isUncountable(E) ||
				UtilEjml.isUncountable(F);
	}

	public double relativeScale( ParabolaGeneral_F64 parabola ) {
		double scale = A/parabola.A;
		double max = Math.abs(parabola.A);
		if( max < Math.abs(parabola.C)) {
			max = Math.abs(parabola.C);
			scale = C/parabola.C;
		}
		if( max < Math.abs(parabola.D)) {
			max = Math.abs(parabola.D);
			scale = D/parabola.D;
		}
		if( max < Math.abs(parabola.E)) {
			max = Math.abs(parabola.E);
			scale = E/parabola.E;
		}
		if( max < Math.abs(parabola.F)) {
			max = Math.abs(parabola.F);
			scale = F/parabola.F;
		}

		if( max == 0 )
			scale = 0;
		return scale;
	}

	/**
	 * Determines if they are equivalent up to a scale factor
	 */
	public boolean isEquivalent( ParabolaGeneral_F64 parabola , double tol ) {
		double scale = relativeScale(parabola);

		if( Math.abs(A*scale-parabola.A) > tol )
			return false;
		if( Math.abs(C*scale-parabola.C) > tol )
			return false;
		if( Math.abs(D*scale-parabola.D) > tol )
			return false;
		if( Math.abs(E*scale-parabola.E) > tol )
			return false;
		if( Math.abs(F*scale-parabola.F) > tol )
			return false;

		return true;
	}
}
