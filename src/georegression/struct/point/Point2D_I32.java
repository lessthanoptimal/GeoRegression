/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
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

/**
 * An integer 2D point
 */
public class Point2D_I32 {
	public int x;
	public int y;

	public Point2D_I32( int x, int y ) {
		this.x = x;
		this.y = y;
	}

	public Point2D_I32( Point2D_I32 orig ) {
		this.x = orig.x;
		this.y = orig.y;
	}

	public Point2D_I32() {
	}

	public void set( int x, int y ) {
		this.x = x;
		this.y = y;
	}

	public void setX( int x ) {
		this.x = x;
	}

	public void setY( int y ) {
		this.y = y;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	/**
	 * <p>
	 * Returns the Euclidean distance squared from 'this' to 'a'.  No floating point
	 * operations are used.
	 * </p>
	 *
	 * <p>
	 * d<sup>2</sup> =  (this.x-a.x)<sup>2</sup> + (this.y-a.y)<sup>2</sup>
	 * </p>
	 *
	 * @param a Point that distance is computed from.
	 * @return Euclidean distance squared.
	 */
	public int distance2( Point2D_I32 a ) {
		int dx = x-a.x;
		int dy = y-a.y;

		return dx*dx + dy*dy;
	}

	public Point2D_I32 copy() {
		return new Point2D_I32( this );
	}

	@Override
	public String toString() {
		return "Point2D_I32{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
