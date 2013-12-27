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

package georegression.struct.point;

import georegression.struct.GeoTuple2D_F64;

/**
 * A vector in 2D composed of double
 */
@SuppressWarnings({"unchecked"})
public class Vector2D_F64 extends GeoTuple2D_F64<Vector2D_F64> {

	public Vector2D_F64( double x, double y ) {
		set( x, y );
	}

	public Vector2D_F64() {
	}

	public Vector2D_F64( Vector2D_F64 pt ) {
		set( pt.x, pt.y );
	}

	@Override
	public Vector2D_F64 createNewInstance() {
		return new Vector2D_F64();
	}

	public void set( Vector2D_F64 orig ) {
		_set( orig );
	}

	public Vector2D_F64 copy() {
		return new Vector2D_F64( this );
	}

	public void normalize() {
		double r = norm();
		x /= r;
		y /= r;
	}

	public String toString() {
		return "V( " + x + " " + y + " )";
	}
}