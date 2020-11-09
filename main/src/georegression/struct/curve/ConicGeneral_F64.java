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

import org.ejml.FancyPrint;
import org.ejml.UtilEjml;

import java.io.Serializable;

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
 * <p>
 * NOTE: these parameters are unique only up to a scale factor.
 * </p>
 *
 * @author Peter Abeles
 */
public class ConicGeneral_F64 implements Serializable
{
	/**
	 * Coefficients
	 */
	public double A,B,C,D,E,F;

	public ConicGeneral_F64(double a, double b, double c, double d, double e, double f) {
		A = a; B = b; C = c;
		D = d; E = e; F = f;
	}

	public ConicGeneral_F64( ConicGeneral_F64 original ) {
		setTo(original);
	}

	public ConicGeneral_F64() {
	}

	public double evaluate(double x , double y ) {
		return A*x*x + B*x*y + C*y*y + D*x + E*y + F;
	}

	/**
	 * Returns true if any of its parameters have an uncountable number
	 */
	public boolean hasUncountable() {
		return UtilEjml.isUncountable(A) || UtilEjml.isUncountable(B) || UtilEjml.isUncountable(C)
				|| UtilEjml.isUncountable(D) || UtilEjml.isUncountable(E) || UtilEjml.isUncountable(F);
	}

	public boolean isEllipse( double tol ) {
		return B*B+tol < 4*A*C;
	}

	public boolean isParabola( double tol ) {
		return Math.abs(B*B - 4*A*C) <= tol ;
	}

	public boolean isHyperbola( double tol ) {
		return B*B-tol > 4*A*C;
	}

	public void setTo(ConicGeneral_F64 original ) {
		this.A = original.A;
		this.B = original.B;
		this.C = original.C;
		this.D = original.D;
		this.E = original.E;
		this.F = original.F;
	}

	public void setTo(double a, double b, double c, double d, double e, double f) {
		A = a; B = b; C = c;
		D = d; E = e; F = f;
	}

	public ConicGeneral_F64 copy() {
		return new ConicGeneral_F64(A,B,C,D,E,F);
	}

	@Override
	public String toString() {
		FancyPrint fp = new FancyPrint();
		return "ConicGeneral_F64{" +
				"A=" + fp.p(A) +
				", B=" + fp.p(B) +
				", C=" + fp.p(C) +
				", D=" + fp.p(D) +
				", E=" + fp.p(E) +
				", F=" + fp.p(F) +
				'}';
	}
}
