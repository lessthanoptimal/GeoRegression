/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.struct.line;

import java.io.Serializable;

/**
 * <p>
 * Represents the line using three parameters such that any point on the line obeys the
 * following formula, A*x + B*y + C = 0.  Any 2D line can be represented using this notation.
 * This formulation is also known as standard and implicit.
 * </p>
 *
 * @author Peter Abeles
 */
public class LineGeneral2D_F64 implements Serializable {

	/**
	 * Coefficients which define the line.
	 */
	public double A,B,C;

	public LineGeneral2D_F64(double a, double b, double c) {
		set(a,b,c);
	}

	public LineGeneral2D_F64() {
	}

	public double getA() {
		return A;
	}

	public double getB() {
		return B;
	}

	public double getC() {
		return C;
	}

	public void set(double a, double b, double c) {
		this.A = a;
		this.B = b;
		this.C = c;
	}

	public void setA(double a) {
		A = a;
	}

	public void setB(double b) {
		B = b;
	}

	public void setC(double c) {
		C = c;
	}
}
