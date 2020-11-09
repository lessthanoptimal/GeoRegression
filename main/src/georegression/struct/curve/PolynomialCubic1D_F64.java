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

/**
 * Quadratic curve in 1D: f(x) = a + bx + c x<sup>2</sup> + d x<sup>3</sup>
 *
 * <p>NOTE: The coefficient order is the reverse of what you will find for sake of consistency as the order
 * of the polynomial is increased.</p>
 *
 * @author Peter Abeles
 */
public class PolynomialCubic1D_F64 implements PolynomialCurve_F64 {
	/**
	 * Coefficients
	 */
	public double a,b,c,d;

	public PolynomialCubic1D_F64(){}

	public PolynomialCubic1D_F64(double a, double b , double c, double d){
		this.a = a; this.b = b; this.c = c; this.d = d;
	}

	public double evaluate( double t ) {
		return a + b*t + c*t*t + d*t*t*t;
	}

	public void setTo(double a, double b , double c , double d )
	{
		this.a = a; this.b = b; this.c = c; this.d = d;
	}

	public void setTo(PolynomialCubic1D_F64 src )
	{
		this.a = src.a; this.b = src.b; this.c = src.c; this.d = src.d;
	}

	@Override
	public String toString() {
		FancyPrint fp = new FancyPrint();
		return "PolynomialCubic1D_F64{" +
				"a=" + fp.s(a) +
				", b=" + fp.s(b) +
				", c=" + fp.s(c) +
				", d=" + fp.s(d) +
				'}';
	}

	@Override
	public double get(int coefficient) {
		switch( coefficient ) {
			case 0: return a;
			case 1: return b;
			case 2: return c;
			case 3: return d;
		}
		throw new IllegalArgumentException("Coefficient out of range. "+coefficient);
	}

	@Override
	public void set(int coefficient, double value) {
		switch( coefficient ) {
			case 0: a=value;return;
			case 1: b=value;return;
			case 2: c=value;return;
			case 3: d=value;return;
		}
		throw new IllegalArgumentException("Coefficient out of range. "+coefficient);
	}

	@Override
	public int size() {
		return 4;
	}

	@Override
	public int degree() {
		return 3;
	}
}
