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

import georegression.struct.arraylike.ArrayLike_F64;
import org.ejml.MapFormattable;
import org.ejml.MapPrintFormat;
import org.ejml.UtilEjml;

import java.io.Serializable;

/// A\*x<sup>2</sup> + B\*x\*y + C\*y<sup>2</sup> + D\*x + E\*y + F=0
///
/// All coefficients are real numbers and A,B,C are all not zero. The discriminant is defined as
/// B<sup>2</sup> - 4\*A\*C.
///
/// **Ellipse:**  B<sup>2</sup> - 4\*A\*C < 0
///
/// **Parabola:**  B<sup>2</sup> - 4\*A\*C = 0
///
/// **Hyperbola:**  B<sup>2</sup> - 4\*A\*C > 0
///
/// NOTE: these parameters are unique only up to a scale factor.
public class ConicGeneral_F64 implements Serializable, MapFormattable, ArrayLike_F64 {
	/// Coefficients
	public double a, b, c, d, e, f;

	public ConicGeneral_F64(double a, double b, double c, double d, double e, double f) {
		this.a = a; this.b = b; this.c = c;
		this.d = d; this.e = e; this.f = f;
	}

	public ConicGeneral_F64( ConicGeneral_F64 original ) {
		setTo(original);
	}

	public ConicGeneral_F64() {
	}

	public double evaluate( double x, double y ) {
		return a*x*x + b*x*y + c*y*y + d*x + e*y + f;
	}

	/// Returns true if any of its parameters have an uncountable number
	public boolean hasUncountable() {
		return UtilEjml.isUncountable(a) || UtilEjml.isUncountable(b) || UtilEjml.isUncountable(c)
				|| UtilEjml.isUncountable(d) || UtilEjml.isUncountable(e) || UtilEjml.isUncountable(f);
	}

	public boolean isEllipse( double tol ) {
		return b*b + tol < 4*a*c;
	}

	public boolean isParabola( double tol ) {
		return Math.abs(b*b - 4*a*c) <= tol;
	}

	public boolean isHyperbola( double tol ) {
		return b*b - tol > 4*a*c;
	}

	public ConicGeneral_F64 setTo( ConicGeneral_F64 original ) {
		this.a = original.a;
		this.b = original.b;
		this.c = original.c;
		this.d = original.d;
		this.e = original.e;
		this.f = original.f;
		return this;
	}

	public ConicGeneral_F64 setTo(double a, double b, double c, double d, double e, double f) {
		this.a = a; this.b = b; this.c = c;
		this.d = d; this.e = e; this.f = f;
		return this;
	}

	public void zero() {
		setTo(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	}

	public ConicGeneral_F64 copy() {
		return new ConicGeneral_F64(a, b, c, d, e, f);
	}

	@Override public double get( int index ) {
		return switch (index) {
			case 0 -> a;
			case 1 -> b;
			case 2 -> c;
			case 3 -> d;
			case 4 -> e;
			case 5 -> f;
			default -> throw new ArrayIndexOutOfBoundsException();
		};
	}

	@Override public void set( int index, double value ) {
		switch (index) {
			case 0 -> a = value;
			case 1 -> b = value;
			case 2 -> c = value;
			case 3 -> d = value;
			case 4 -> e = value;
			case 5 -> f = value;
			default -> throw new ArrayIndexOutOfBoundsException();
		}
	}

	@Override public int length() {
		return 6;
	}

	@Override public String formatMap( MapPrintFormat format ) {
		return format.itemPrefix +
				format.pair("A", a, true) +
				format.pair("B", b, true) +
				format.pair("C", c, true) +
				format.pair("D", d, true) +
				format.pair("E", e, true) +
				format.pair("F", f, false) +
				format.itemSuffix;
	}

	@Override public String toString() {return MapPrintFormat.DEFAULT.toString(this);}
}
