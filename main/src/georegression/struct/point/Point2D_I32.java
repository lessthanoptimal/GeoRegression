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

import georegression.struct.GeoTuple_I32;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * An integer 2D point
 */
@Getter @Setter
public class Point2D_I32 extends GeoTuple_I32<Point2D_I32> {
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

	public Point2D_I32() {}

	public void setTo( int x, int y ) {
		this.x = x;
		this.y = y;
	}

	public void zero() {
		this.x = 0;
		this.y = 0;
	}

	public double distance( Point2D_I32 a ) {
		int dx = x - a.x;
		int dy = y - a.y;

		return Math.sqrt(dx*dx + dy*dy);
	}

	public double distance( int x, int y ) {
		int dx = this.x - x;
		int dy = this.y - y;

		return Math.sqrt(dx*dx + dy*dy);
	}

	/**
	 * <p>
	 * Returns the Euclidean distance squared from 'this' to 'a'. No floating point
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
		int dx = x - a.x;
		int dy = y - a.y;

		return dx*dx + dy*dy;
	}

	public int distance2( int x, int y ) {
		int dx = this.x - x;
		int dy = this.y - y;

		return dx*dx + dy*dy;
	}

	public boolean isIdentical( int x, int y ) {
		return this.x == x && this.y == y;
	}

	@Override
	public void setTo( Point2D_I32 src ) {
		this.x = src.x;
		this.y = src.y;
	}

	@Override
	public int getIdx( int index ) {
		if (index == 0)
			return x;
		else if (index == 1)
			return y;
		throw new RuntimeException("Invalid index " + index);
	}

	@Override
	public void setIdx( int index, int value ) {
		if (index == 0)
			this.x = value;
		else if (index == 1)
			this.y = value;
		else
			throw new RuntimeException("Invalid index " + index);
	}

	public void print() {
		System.out.println(toString());
	}

	@Override
	public Point2D_I32 copy() {
		return new Point2D_I32(this);
	}

	@Override
	public String toString() {
		return "Point2D_I32{" +
				"x=" + x +
				", y=" + y +
				'}';
	}

	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public Point2D_I32 createNewInstance() {
		return new Point2D_I32();
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj) return true;
		if (!(obj instanceof Point2D_I32)) return false;
		Point2D_I32 p = (Point2D_I32)obj;
		return x == p.x && y == p.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
