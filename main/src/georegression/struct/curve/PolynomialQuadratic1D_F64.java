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
 * Quadratic curve in 1D: f(x) = a + bx + c x<sup>2</sup>.
 *
 * <p>NOTE: The coefficient order is the reverse of what you will find for sake of consistency as the order
 * of the polynomial is increased.</p>
 *
 * @author Peter Abeles
 */
public class PolynomialQuadratic1D_F64 {
	/**
	 * Coefficients
	 */
	public double a,b,c;

	public PolynomialQuadratic1D_F64(){}

	public PolynomialQuadratic1D_F64(double a, double b , double c){
		this.a = a; this.b = b; this.c = c;
	}

	public double evaluate( double t ) {
		return a + b*t + c*t*t;
	}

	public void set( double a, double b , double c )
	{
		this.a = a; this.b = b; this.c = c;
	}

	public void set( PolynomialQuadratic1D_F64 src )
	{
		this.a = src.a; this.b = src.b; this.c = src.c;
	}
}
