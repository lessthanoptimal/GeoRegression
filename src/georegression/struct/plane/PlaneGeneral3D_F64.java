/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
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

package georegression.struct.plane;

/**
 * <p>
 * Represents the line using four parameters such that any point on the planes obeys the
 * following formula, A*x + B*y + C*z = D.  Any 3D plane can be represented using this notation.
 * This formulation is also known as scalar.
 * </p>
 *
 * @author Peter Abeles
 */
public class PlaneGeneral3D_F64 {
	/**
	 * Coefficients which define the plane.
	 */
	public double A,B,C,D;

	public PlaneGeneral3D_F64(double a, double b, double c, double d) {
		set(a,b,c,d);
	}

	public PlaneGeneral3D_F64() {
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

	public void set(double a, double b, double c, double d) {
		this.A = a;
		this.B = b;
		this.C = c;
		this.D = d;
	}
}
