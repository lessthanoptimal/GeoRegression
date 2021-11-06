/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

package georegression.struct.point;

import georegression.geometry.GeometryMath_F64;
import georegression.geometry.UtilVector3D_F64;
import georegression.struct.GeoTuple3D_F64;

/**
 * Spacial vector in 3D. A vector defines a direction.
 */
public class Vector3D_F64 extends GeoTuple3D_F64<Vector3D_F64> {

	public Vector3D_F64( GeoTuple3D_F64 orig ) {this(orig.x, orig.y, orig.z);}

	public Vector3D_F64( double x, double y, double z ) {super(x, y, z);}

	public Vector3D_F64() {}

	/**
	 * Defines the vector using two points. v = b - a;
	 *
	 * @param a (Input) Point
	 * @param b (Input) Point
	 */
	public Vector3D_F64( Point3D_F64 a, Point3D_F64 b ) {
		x = b.getX() - a.getX();
		y = b.getY() - a.getY();
		z = b.getZ() - a.getZ();
	}

	@Override
	public void setTo( Vector3D_F64 v ) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	/**
	 * In-place minus operation. this = a - b.
	 *
	 * @param a Point
	 * @param b Point
	 */
	public void minus( Point3D_F64 a, Point3D_F64 b ) {
		x = a.x - b.x;
		y = a.y - b.y;
		z = a.z - b.z;
	}

	/**
	 * In-place divide operation. x /= value;
	 *
	 * @param value The value each component is divided by
	 */
	public void divide( double value ) {
		x /= value;
		y /= value;
		z /= value;
	}

	@Override public Vector3D_F64 copy() {
		return new Vector3D_F64(x, y, z);
	}

	/**
	 * Assigns this vector to the results of crossing the two passed in vectors.
	 *
	 * @param a Vector
	 * @param b Vector
	 */
	public void crossSetTo( Vector3D_F64 a, Vector3D_F64 b ) {
		GeometryMath_F64.cross(a, b, this);
	}

	/**
	 * Crosses this matrix with 'b' and returns the result
	 */
	public Vector3D_F64 crossWith( Vector3D_F64 b ) {
		Vector3D_F64 c = new Vector3D_F64();
		GeometryMath_F64.cross(this, b, c);
		return c;
	}

	@Override public Vector3D_F64 createNewInstance() {return new Vector3D_F64();}

	@Override public String toString() {return toString("V");}

	public void normalize() {
		// carefully normalize to avoid numerical overflow
		double m = Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));

		double x_n = x/m;
		double y_n = y/m;
		double z_n = z/m;
		double v = Math.sqrt(x_n*x_n + y_n*y_n + z_n*z_n);

		x = x_n/v;
		y = y_n/v;
		z = z_n/v;
	}

	/**
	 * Dot product between this and 'a' = this.x * a.x + this.y * a.y + this.z * a.z
	 *
	 * @param a A vector
	 * @return dot product.
	 */
	public double dot( Vector3D_F64 a ) {
		return x*a.x + y*a.y + z*a.z;
	}

	public double dot( double x, double y, double z ) {
		return this.x*x + this.y*y + this.z*z;
	}

	/**
	 * Returns the acute angle between the two vectors. Computed using the dot product.
	 *
	 * @param a Vector
	 * @return Acute angle in radians between 'this' and 'a'.
	 */
	public double acute( Vector3D_F64 a ) {return UtilVector3D_F64.acute(this, a);}
}
