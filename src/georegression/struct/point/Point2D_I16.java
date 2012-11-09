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

/**
 * A point in 2D composed of shorts
 */
public class Point2D_I16 {
	public short x;
	public short y;

	public Point2D_I16( short x, short y ) {
		this.x = x;
		this.y = y;
	}

	public Point2D_I16() {
	}

	public void set( Point2D_I16 pt ) {
		this.x = pt.x;
		this.y = pt.y;
	}

	public void set( int x, int y ) {
		this.x = (short)x;
		this.y = (short)y;
	}

	public void setX( int x ) {
		this.x = (short)x;
	}

	public void setY( int y ) {
		this.y = (short)y;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public Point2D_I16 copy() {
		return new Point2D_I16( x, y );
	}

	@Override
	public String toString() {
		return "Point2D_I16{ x= " + x +", y= " + y +'}';
	}
}