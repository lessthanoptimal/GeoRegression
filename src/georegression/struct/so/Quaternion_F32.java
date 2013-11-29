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

package georegression.struct.so;


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
public class Quaternion_F32 {
	/**
	 * Describes the angle of rotation.  See above for how it is encoded.
	 */
	public float w;
	/**
	 * Axis of rotation
	 */
	public float x,y,z;

	public Quaternion_F32() {
		w = 1;
	}

	public Quaternion_F32(float w, float x, float y, float z) {
		set(w, x, y, z);
	}

	public void set( Quaternion_F32 quaternion ) {
		this.w = quaternion.w;
		this.x = quaternion.x;
		this.y = quaternion.y;
		this.z = quaternion.z;
	}

	public void set( float w, float x, float y, float z ) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Converts the quaternion into a unit quaternion.
	 */
	public void normalize() {
		float n = (float)Math.sqrt( w * w + x * x + y * y + z * z);

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
