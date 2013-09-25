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
 * Specifies a 3D rotation using a unit quaternion.
 *
 * @author Peter Abeles
 */
public class Quaternion_F32 {
	public float q1;
	public float q2;
	public float q3;
	public float q4;

	public Quaternion_F32() {
		q1 = 1;
	}

	public Quaternion_F32(float q1, float q2, float q3, float q4) {
		set(q1,q2,q3,q4);
	}

	public void set( Quaternion_F32 quaternion ) {
		this.q1 = quaternion.q1;
		this.q2 = quaternion.q2;
		this.q3 = quaternion.q3;
		this.q4 = quaternion.q4;
	}

	public void set( float q1, float q2, float q3, float q4 ) {
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
		this.q4 = q4;
	}

	/**
	 * Normalizes the elements to one
	 */
	public void normalize() {
		float n = (float)Math.sqrt( q1 * q1 + q2 * q2 + q3 * q3 + q4 * q4 );

		q1 /= n;
		q2 /= n;
		q3 /= n;
		q4 /= n;
	}
}
