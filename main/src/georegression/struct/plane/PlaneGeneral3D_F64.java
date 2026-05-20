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

package georegression.struct.plane;

import georegression.struct.arraylike.ArrayLike_F64;
import lombok.Getter;
import lombok.Setter;
import org.ejml.MapFormattable;
import org.ejml.MapPrintFormat;

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
public class PlaneGeneral3D_F64 implements Serializable, MapFormattable, ArrayLike_F64 {
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

	/// Converts into a [String] using a Map like format.
	@Override public String formatMap( MapPrintFormat format ) {
		return format.itemPrefix +
				format.pair("A" ,A, true) +
				format.pair("B" ,B, true) +
				format.pair("C" ,C, true) +
				format.pair("D" ,D, false) +
				format.itemSuffix;
	}

	@Override public String toString() { return MapPrintFormat.DEFAULT.toString(this); }

	@Override public double get( int index ) {
		return switch (index) {
			case 0 -> A;
			case 1 -> B;
			case 2 -> C;
			case 3 -> D;
			default -> throw new ArrayIndexOutOfBoundsException();
		};
	}

	@Override public void set( int index, double value ) {
		switch (index) {
			case 0 -> A = value;
			case 1 -> B = value;
			case 2 -> C = value;
			case 3 -> D = value;
			default -> throw new ArrayIndexOutOfBoundsException();
		}
	}

	@Override public int length() {
		return 4;
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
