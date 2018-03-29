/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

import java.io.Serializable;

/**
 * <p>Quadratic description on a curve in 2D using polynomial notation</p>
 *
 * <p>y = a0 + a1*x + a2*x<sup>2</sup></p>
 *
 * <p>WARNING: This form of a quadratic cannot represent a vertical line along the y-axis.</p>
 *
 * @author Peter Abeles
 */
public class QuadraticPolynomial2D_F64 implements Serializable {
	/**
	 * Polynomial's coefficients
	 */
	public double a0,a1,a2;

	/**
	 * Returns a0 + a1*x + a2*x<sup>2</sup>-y.
	 */
	public double evaluate( double x , double y ) {
		return a2*x*x + a1*x + a0 - y;
	}

	public double getA0() {
		return a0;
	}

	public void setA0(double a0) {
		this.a0 = a0;
	}

	public double getA1() {
		return a1;
	}

	public void setA1(double a1) {
		this.a1 = a1;
	}

	public double getA2() {
		return a2;
	}

	public void setA2(double a2) {
		this.a2 = a2;
	}
}
