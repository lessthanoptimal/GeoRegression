/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.line;

/**
 * <p>
 * Represents the line using three parameters such that any point on the line obeys the
 * following formula,, A*x + B*y + C = 0.  Any 2D line can be represented using this notation.
 * This formulation is also known as standard and implicit.
 * </p>
 *
 * @author Peter Abeles
 */
public class LineGeneral2D_F32 {

	/**
	 * Coefficients which define the line.
	 */
	public float A,B,C;

	public LineGeneral2D_F32(float a, float b, float c) {
		set(a,b,c);
	}

	public LineGeneral2D_F32() {
	}

	public float getA() {
		return A;
	}

	public float getB() {
		return B;
	}

	public float getC() {
		return C;
	}

	public void set(float a, float b, float c) {
		this.A = a;
		this.B = b;
		this.C = c;
	}
}
