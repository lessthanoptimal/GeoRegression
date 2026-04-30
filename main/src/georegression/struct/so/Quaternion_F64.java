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

package georegression.struct.so;

import org.ejml.MapFormattable;
import org.ejml.MapPrintFormat;
import org.ejml.MatrixPrintFormat;
import org.ejml.UtilEjml;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/// Specifies a 3D rotation using a quaternion. q = w + x\*i + y\*j + z\*k, where (w,x,y,z) are the parameters of
/// the quaternion and (i,j,k) are unit vectors representing the Cartesian axis.
///
/// If the quaternion is a unit quaternion then the following is true:
///
/// q = cos(theta/2) + (x\*i + y\*j + z\*k)\*sin(theta/2)
///
/// where 'theta' is the angle of rotation, (x,y,z) is the unit axis of rotation.
public class Quaternion_F64 implements Serializable, MapFormattable {
	/// Describes the angle of rotation. See above for how it is encoded.
	public double w;
	/// Axis of rotation
	public double x, y, z;

	public Quaternion_F64() {
		w = 1;
	}

	public Quaternion_F64( double w, double x, double y, double z ) {
		setTo(w, x, y, z);
	}

	public Quaternion_F64 setTo( Quaternion_F64 quaternion ) {
		this.w = quaternion.w;
		this.x = quaternion.x;
		this.y = quaternion.y;
		this.z = quaternion.z;
		return this;
	}

	public Quaternion_F64 setTo( double w, double x, double y, double z ) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/// Sets the quaternion to identity and returns a reference to itself
	public Quaternion_F64 setToIdentity() {
		this.w = 1;
		this.x = this.y = this.z = 0;
		return this;
	}

	/// Converts the quaternion into a unit quaternion.
	public void normalize() {
		double n = norm();

		w /= n;
		x /= n;
		y /= n;
		z /= n;
	}

	/// Returns a new quaternion which is equal to 'this', but normalized
	public Quaternion_F64 normalized( @Nullable Quaternion_F64 out ) {
		if (out == null)
			out = new Quaternion_F64();
		out.setTo(this).normalize();
		return out;
	}

	/// Returns a new quaternion which is the conjugate of 'this'
	public Quaternion_F64 conjugated( @Nullable Quaternion_F64 out ) {
		if (out == null)
			out = new Quaternion_F64();
		out.w = this.w;
		out.x = -this.x;
		out.y = -this.y;
		out.z = -this.z;
		return out;
	}

	/// Returns the f-norm of this quaternion
	public double norm() {
		return Math.sqrt(w*w + x*x + y*y + z*z);
	}

	/// Returns true if the two quaternions are identical. Note that they might be equivalent and fail this test
	public boolean isIdentical( Quaternion_F64 q, double tol ) {
		if (Math.abs(q.w - w) > tol)
			return false;
		if (Math.abs(q.x - x) > tol)
			return false;
		if (Math.abs(q.y - y) > tol)
			return false;
		if (Math.abs(q.z - z) > tol)
			return false;
		return true;
	}

	/// Returns true if the two quaternions are identical. Note that they might be equivalent and fail this test
	public boolean isIdentical( double qw, double qx, double qy, double qz, double tol ) {
		if (Math.abs(qw - w) > tol)
			return false;
		if (Math.abs(qx - x) > tol)
			return false;
		if (Math.abs(qy - y) > tol)
			return false;
		if (Math.abs(qz - z) > tol)
			return false;
		return true;
	}

	/// Converts into a string using a Matrix like format.
	public String format( MatrixPrintFormat format ) {
		char decimal = format.decimal;
		return format.getRowPrefix() +
				UtilEjml.fancyString2(w, format.getPrecision(), decimal) +
				format.getColSeparator() +
				UtilEjml.fancyString2(x, format.getPrecision(), decimal) +
				format.getColSeparator() +
				UtilEjml.fancyString2(y, format.getPrecision(), decimal) +
				format.getColSeparator() +
				UtilEjml.fancyString2(z, format.getPrecision(), decimal) +
				format.getRowSuffix();
	}

	/// Converts into a string using a Map like format.
	@Override public String formatMap( MapPrintFormat format ) {
		return format.itemPrefix +
				format.pair("w", w, true) +
				format.pair("x", x, true) +
				format.pair("y", y, true) +
				format.pair("z", z, false) +
				format.itemSuffix;
	}

	@Override public String toString() { return MapPrintFormat.DEFAULT.toString(this); }
}
