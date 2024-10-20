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

package georegression.struct.so;

import java.io.Serializable;

/**
 * <p>
 * Specifies a 3D rotation using a quaternion. q = w + x*i + y*j + z*k, where (w,x,y,z) are the parameters of
 * the quaternion and (i,j,k) are unit vectors representing the Cartesian axis.
 * </p>
 *
 * <p>
 * If the quaternion is a unit quaternion then the following is true:<br>
 * q = cos(theta/2) + (x*i + y*j + z*k)*sin(theta/2)<br>
 * where 'theta' is the angle of rotation, (x,y,z) is the unit axis of rotation.
 * </p>
 *
 * @author Peter Abeles
 */
public class Quaternion_F64 implements Serializable {
	/**
	 * Describes the angle of rotation. See above for how it is encoded.
	 */
	public double w;
	/**
	 * Axis of rotation
	 */
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

	/**
	 * Converts the quaternion into a unit quaternion.
	 */
	public void normalize() {
		double n = Math.sqrt(w*w + x*x + y*y + z*z);

		w /= n;
		x /= n;
		y /= n;
		z /= n;
	}

	/** Returns true if the two quaternions are identical. Note that they might be equivalent and fail this test */
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{ w = " + w + " axis( " + x + " " + y + " " + z + ") }";
	}
}
