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

import georegression.geometry.UtilVector2D_F64;
import georegression.struct.GeoTuple2D_F64;

/**
 * Spacial vector in 2D
 */
@SuppressWarnings({"unchecked"})
public class Vector2D_F64 extends GeoTuple2D_F64<Vector2D_F64> {

	public Vector2D_F64( GeoTuple2D_F64 orig ) {
		this(orig.x, orig.y);
	}

	public Vector2D_F64( double x, double y ) {
		setTo(x, y);
	}

	public Vector2D_F64() {}

	public Vector2D_F64( Vector2D_F64 pt ) {
		setTo(pt.x, pt.y);
	}

	@Override
	public Vector2D_F64 createNewInstance() {
		return new Vector2D_F64();
	}

	public void set( Vector2D_F64 orig ) {
		_setTo(orig);
	}

	@Override
	public Vector2D_F64 copy() {
		return new Vector2D_F64(this);
	}

	public void normalize() {
		double r = norm();
		x /= r;
		y /= r;
	}

	/**
	 * In-place minus operation. this = a - b.
	 *
	 * @param a Point
	 * @param b Point
	 */
	public void minus( Point2D_F64 a, Point2D_F64 b ) {
		x = a.x - b.x;
		y = a.y - b.y;
	}

	@Override
	public String toString() {
		return toString("V");
	}

	/**
	 * Dot product between this and 'a' = this.x * a.x + this.y * a.y
	 *
	 * @param a A vector
	 * @return dot product.
	 */
	public double dot( Vector2D_F64 a ) {
		return x*a.x + y*a.y;
	}

	/**
	 * Returns the acute angle between the two vectors. Computed using the dot product.
	 *
	 * @param a Vector
	 * @return Acute angle in radians between 'this' and 'a'.
	 */
	public double acute( Vector2D_F64 a ) {
		return UtilVector2D_F64.acute(this, a);
	}
}