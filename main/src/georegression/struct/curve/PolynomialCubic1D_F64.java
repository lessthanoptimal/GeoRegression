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

import org.ejml.FancyPrint;

/**
 * Quadratic curve in 1D: f(x) = a + bx + c x<sup>2</sup> + d x<sup>3</sup>
 *
 * <p>NOTE: The coefficient order is the reverse of what you will find for sake of consistency as the order
 * of the polynomial is increased.</p>
 *
 * @author Peter Abeles
 */
public class PolynomialCubic1D_F64 {
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

	public void set( double a, double b , double c , double d )
	{
		this.a = a; this.b = b; this.c = c; this.d = d;
	}

	public void set( PolynomialCubic1D_F64 src )
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
}
