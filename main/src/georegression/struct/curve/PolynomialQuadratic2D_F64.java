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
 * Quadratic curve in 2D: f(x,y) = a + b&middot;x + c&middot;y + d&middot;xy + e&middot;x<sup>2</sup> + f&middot;y<sup>2</sup>.
 *
 * <p>NOTE: The coefficient order is the reverse of what you will find for sake of consistency as the order
 * of the polynomial is increased.</p>
 *
 * @author Peter Abeles
 */
public class PolynomialQuadratic2D_F64 {
	/**
	 * Coefficients
	 */
	public double a,b,c,d,e,f;

	public PolynomialQuadratic2D_F64(){}

	public PolynomialQuadratic2D_F64(double a, double b , double c, double d, double e, double f){
		this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f;
	}

	public double evaluate( double x , double y  ) {
		return a + b*x + c*y + d*x*y + e*x*x + f*y*y;
	}

	public void setTo(double a, double b , double c, double d, double e, double f)
	{
		this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f;
	}

	public void setTo(PolynomialQuadratic2D_F64 src )
	{
		this.a = src.a; this.b = src.b; this.c = src.c; this.d = src.d; this.e = src.e; this.f = src.f;
	}

	@Override
	public String toString() {
		FancyPrint fp = new FancyPrint();
		return "PolynomialQuadratic2D_F64{" +
				"a=" + fp.p(a) +
				", b=" + fp.p(b) +
				", c=" + fp.p(c) +
				", d=" + fp.p(d) +
				", e=" + fp.p(e) +
				", f=" + fp.p(f) +
				'}';
	}
}
