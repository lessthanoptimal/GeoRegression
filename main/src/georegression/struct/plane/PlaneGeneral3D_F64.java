/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

package georegression.struct.plane;

import lombok.Getter;
import lombok.Setter;
import org.ejml.FancyPrint;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * Represents the line using four parameters such that any point on the planes obeys the
 * following formula, A*x + B*y + C*z = D. Any 3D plane can be represented using this notation.
 * This formulation is also known as scalar.
 * </p>
 *
 * <p>
 * If in Hessian normal form, then (A,B,C) is the unit normal, and D is distance of the plane from the origin. The
 * sign of D determines the side on the plane on which the origin is located. {@link #normalize()}
 * </p>
 *
 * <p>
 * NOTE: The normal of the plane is the vector (A,B,C)
 * </p>
 *
 * @author Peter Abeles
 */
@Getter @Setter
public class PlaneGeneral3D_F64 implements Serializable {
	/** Coefficients which define the plane. */
	public double A, B, C, D;

	public PlaneGeneral3D_F64( PlaneGeneral3D_F64 src ) {
		setTo(src);
	}

	public PlaneGeneral3D_F64( double a, double b, double c, double d ) {
		setTo(a, b, c, d);
	}

	public PlaneGeneral3D_F64() {}

	public PlaneGeneral3D_F64 setTo( double a, double b, double c, double d ) {
		this.A = a;
		this.B = b;
		this.C = c;
		this.D = d;
		return this;
	}

	public PlaneGeneral3D_F64 setTo( PlaneGeneral3D_F64 src ) {
		this.A = src.A;
		this.B = src.B;
		this.C = src.C;
		this.D = src.D;
		return this;
	}

	public void zero() {
		setTo(0, 0, 0, 0);
	}

	public double evaluate( double x, double y, double z ) {
		return A*x + B*y + C*z + D;
	}

	/**
	 * Ensures that A*A + B*B + C*C == 1
	 */
	public void normalize() {
		double n = Math.sqrt(A*A + B*B + C*C);
		A /= n;
		B /= n;
		C /= n;
		D /= n;
	}

	public PlaneGeneral3D_F64 copy() {
		return new PlaneGeneral3D_F64().setTo(this);
	}

	public boolean isIdentical( PlaneGeneral3D_F64 o ) {
		return A == o.A && B == o.B && C == o.C && D == o.D;
	}

	@Override
	public String toString() {
		FancyPrint fancy = new FancyPrint();
		return getClass().getSimpleName() +
				"( A = " + fancy.s(A) + " B = " + fancy.s(B) +
				" C = " + fancy.s(C) + " D = " + fancy.s(D) + " )";
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj) return true;
		if (!(obj instanceof PlaneGeneral3D_F64)) return false;
		return isIdentical((PlaneGeneral3D_F64)obj);
	}

	@Override
	public int hashCode() {
		return Objects.hash(A, B, C, D);
	}
}
