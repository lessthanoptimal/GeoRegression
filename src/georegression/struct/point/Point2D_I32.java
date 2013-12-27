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

	public void set( Point2D_I32 v ) {
		this.x = v.x;
		this.y = v.y;
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

	public double distance( Point2D_I32 a ) {
		int dx = x-a.x;
		int dy = y-a.y;

		return Math.sqrt(dx*dx + dy*dy);
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
