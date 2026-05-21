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

/// Parabola is a specific type of conic that is defined below using 5 coefficients.
///
/// (A\*x + C\*y)<sup>2</sup> + D\*x + E\*y + F = 0
public class ParabolaGeneral_F64 implements MapFormattable, ArrayLike_F64 {

	/// Coefficients.
	public double a, c, d, e, f;

	public ParabolaGeneral_F64( double a, double c, double d, double e, double f ) {
		this.a = a;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}

	public ParabolaGeneral_F64( ParabolaGeneral_F64 original ) {
		this.setTo(original);
	}

	public ParabolaGeneral_F64() {}

	public ParabolaGeneral_F64 setTo( ParabolaGeneral_F64 original ) {
		this.a = original.a;
		this.c = original.c;
		this.d = original.d;
		this.e = original.e;
		this.f = original.f;
		return this;
	}

	public ParabolaGeneral_F64 setTo( double a, double c, double d, double e, double f ) {
		this.a = a;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		return this;
	}

	public void zero() {
		a = c = d = e = f = 0.0;
	}

	public double evaluate( double x, double y ) {
		double inner = a*x + c*y;
		return inner*inner + d*x + e*y + f;
	}

	/// Returns true if any of its parameters have an uncountable number
	public boolean hasUncountable() {
		return UtilEjml.isUncountable(a) || UtilEjml.isUncountable(c)
				|| UtilEjml.isUncountable(d) || UtilEjml.isUncountable(e) ||
				UtilEjml.isUncountable(f);
	}

	public double relativeScale( ParabolaGeneral_F64 parabola ) {
		double scale = a/parabola.a;
		double max = Math.abs(parabola.a);
		if (max < Math.abs(parabola.c)) {
			max = Math.abs(parabola.c);
			scale = c/parabola.c;
		}
		if (max < Math.abs(parabola.d)) {
			max = Math.abs(parabola.d);
			scale = d/parabola.d;
		}
		if (max < Math.abs(parabola.e)) {
			max = Math.abs(parabola.e);
			scale = e/parabola.e;
		}
		if (max < Math.abs(parabola.f)) {
			max = Math.abs(parabola.f);
			scale = f/parabola.f;
		}

		if (max == 0)
			scale = 0;
		return scale;
	}

	/// Determines if they are equivalent up to a scale factor
	public boolean isEquivalent( ParabolaGeneral_F64 parabola, double tol ) {
		double scale = relativeScale(parabola);

		if (Math.abs(a*scale - parabola.a) > tol)
			return false;
		if (Math.abs(c*scale - parabola.c) > tol)
			return false;
		if (Math.abs(d*scale - parabola.d) > tol)
			return false;
		if (Math.abs(e*scale - parabola.e) > tol)
			return false;
		if (Math.abs(f*scale - parabola.f) > tol)
			return false;

		return true;
	}

	@Override public double get( int index ) {
		return switch (index) {
			case 0 -> a;
			case 1 -> c;
			case 2 -> d;
			case 3 -> e;
			case 4 -> f;
			default -> throw new ArrayIndexOutOfBoundsException();
		};
	}

	@Override public void set( int index, double value ) {
		switch (index) {
			case 0 -> a = value;
			case 1 -> c = value;
			case 2 -> d = value;
			case 3 -> e = value;
			case 4 -> f = value;
			default -> throw new ArrayIndexOutOfBoundsException();
		}
	}

	@Override public int length() {
		return 5;
	}


	@Override public String formatMap( MapPrintFormat format ) {
		return format.itemPrefix +
				format.pair("A", a, true) +
				format.pair("C", c, true) +
				format.pair("D", d, true) +
				format.pair("E", e, true) +
				format.pair("F", f, false) +
				format.itemSuffix;
	}

	@Override public String toString() {return MapPrintFormat.DEFAULT.toString(this);}
}
