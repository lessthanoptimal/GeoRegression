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

package georegression.struct.curve;

import org.ejml.MapPrintFormat;

/// Quadratic curve in 1D: f(x) = a + bx + c x<sup>2</sup> + d x<sup>3</sup>
///
/// NOTE: The coefficient order is the reverse of what you will find for sake of consistency as the order
/// of the polynomial is increased.
public class PolynomialCubic1D_F64 implements PolynomialCurve_F64 {
	/// Coefficients
	public double a, b, c, d;

	public PolynomialCubic1D_F64() {}

	public PolynomialCubic1D_F64( double a, double b, double c, double d ) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public double evaluate( double t ) {
		return a + b*t + c*t*t + d*t*t*t;
	}

	public PolynomialCubic1D_F64 setTo( double a, double b, double c, double d ) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		return this;
	}

	public PolynomialCubic1D_F64 setTo( PolynomialCubic1D_F64 src ) {
		this.a = src.a;
		this.b = src.b;
		this.c = src.c;
		this.d = src.d;
		return this;
	}

	@Override public void zero() {
		setTo(0, 0, 0, 0);
	}

	@Override public String toString() {
		return getClass().getSimpleName() + formatMap(MapPrintFormat.DEFAULT);
	}

	@Override public double get( int coefficient ) {
		return switch (coefficient) {
			case 0 -> a;
			case 1 -> b;
			case 2 -> c;
			case 3 -> d;
			default -> throw new IllegalArgumentException("Coefficient out of range. " + coefficient);
		};
	}

	@Override public void set( int coefficient, double value ) {
		switch (coefficient) {
			case 0 -> a = value;
			case 1 -> b = value;
			case 2 -> c = value;
			case 3 -> d = value;
			default -> throw new IllegalArgumentException("Coefficient out of range. " + coefficient);
		}
	}

	@Override public String formatMap( MapPrintFormat format ) {
		return format.itemPrefix +
				format.pair("a", a, true) +
				format.pair("b", b, true) +
				format.pair("c", c, true) +
				format.pair("d", d, false) +
				format.itemSuffix;
	}

	@Override public int length() {
		return 4;
	}

	@Override public int degree() {
		return 3;
	}

}
