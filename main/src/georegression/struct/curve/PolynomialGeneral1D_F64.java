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

package georegression.struct.curve;

/**
 * Generalized polynomial curve
 */
public class PolynomialGeneral1D_F64 implements PolynomialCurve_F64 {

	public double[] coefs;

	public PolynomialGeneral1D_F64( int degree ) {
		coefs = new double[degree+1];
	}

	public double evaluate( double t ) {
		double output = coefs[0];
		double tt = t;
		for (int i = 1; i < coefs.length; i++) {
			output += coefs[i]*tt;
			tt *= t;
		}
		return output;
	}

	@Override
	public double get(int coefficient) {
		return coefs[coefficient];
	}

	@Override
	public void set(int coefficient, double value) {
		coefs[coefficient] = value;
	}

	@Override
	public int size() {
		return coefs.length;
	}

	@Override
	public int degree() {
		return coefs.length-1;
	}
}
