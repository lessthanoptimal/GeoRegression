/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.point;

import georegression.struct.GeoTuple2D_F32;

/**
 * A vector in 2D composed of float
 */
@SuppressWarnings({"unchecked"})
public class Vector2D_F32 extends GeoTuple2D_F32<Vector2D_F32> {

	public Vector2D_F32( float x, float y ) {
		set( x, y );
	}

	public Vector2D_F32() {
	}

	public Vector2D_F32( Vector2D_F32 pt ) {
		set( pt.x, pt.y );
	}

	@Override
	public Vector2D_F32 createNewInstance() {
		return new Vector2D_F32();
	}

	public void set( Vector2D_F32 orig ) {
		_set( orig );
	}

	public Vector2D_F32 copy() {
		return new Vector2D_F32( this );
	}

	public void normalize() {
		float r = norm();
		x /= r;
		y /= r;
	}

	public String toString() {
		return "V( " + x + " " + y + " )";
	}
}