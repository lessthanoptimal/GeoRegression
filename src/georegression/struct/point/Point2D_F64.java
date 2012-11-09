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
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.point;

import georegression.struct.GeoTuple2D_F64;

/**
 * A point in 2D composed of double
 */
@SuppressWarnings({"unchecked"})
public class Point2D_F64 extends GeoTuple2D_F64<Point2D_F64> {

	public Point2D_F64( double x, double y ) {
		set( x, y );
	}

	public Point2D_F64() {
	}

	public Point2D_F64( Point2D_F64 pt ) {
		set( pt.x, pt.y );
	}

	@Override
	public Point2D_F64 createNewInstance() {
		return new Point2D_F64();
	}

	public void set( Point2D_F64 orig ) {
		_set( orig );
	}

	@Override
	public Point2D_F64 copy() {
		return new Point2D_F64( this );
	}

	public String toString() {
		return "P( " + x + " " + y + " )";
	}
}