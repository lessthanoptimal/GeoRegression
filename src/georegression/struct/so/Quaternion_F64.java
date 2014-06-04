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

package georegression.struct.so;


import java.io.Serializable;

/**
 * <p>
 * Specifies a 3D rotation using a quaternion.
 * </p>
 *
 * <p>
 * If the quaternion is a unit quaternion then the following is true:<br>
 * q = cos(theta/2) + u*sin(theta/2)<br>
 * where 'theta' is the angle of rotation, u=(x,y,z) is the unit axis of rotation.
 * </p>
 *
 * @author Peter Abeles
 */
public class Quaternion_F64 implements Serializable {
	/**
	 * Describes the angle of rotation.  See above for how it is encoded.
	 */
	public double w;
	/**
	 * Axis of rotation
	 */
	public double x,y,z;

	public Quaternion_F64() {
		w = 1;
	}

	public Quaternion_F64(double w, double x, double y, double z) {
		set(w, x, y, z);
	}

	public void set( Quaternion_F64 quaternion ) {
		this.w = quaternion.w;
		this.x = quaternion.x;
		this.y = quaternion.y;
		this.z = quaternion.z;
	}

	public void set( double w, double x, double y, double z ) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Converts the quaternion into a unit quaternion.
	 */
	public void normalize() {
		double n = Math.sqrt( w * w + x * x + y * y + z * z);

		w /= n;
		x /= n;
		y /= n;
		z /= n;
	}

   @Override
   public String toString() {
      return getClass().getSimpleName()+"{ w = "+ w +" axis( "+ x +" "+ y +" "+ z +") }";
   }
}
