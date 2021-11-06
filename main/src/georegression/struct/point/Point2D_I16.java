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

import java.util.Objects;

/**
 * A point in 2D composed of shorts
 */
public class Point2D_I16 extends GeoTuple_I32<Point2D_I16> {
	public short x;
	public short y;

	public Point2D_I16( short x, short y ) {
		this.x = x;
		this.y = y;
	}

	public Point2D_I16( Point2D_I16 orig ) {
		this.x = orig.x;
		this.y = orig.y;
	}

	public Point2D_I16() {
	}

	public void setTo( int x, int y ) {
		this.x = (short)x;
		this.y = (short)y;
	}

	public void zero() {
		setTo(0, 0);
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

	public boolean isIdentical( int x, int y ) {
		return this.x == x && this.y == y;
	}

	@Override
	public void setTo( Point2D_I16 src ) {
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
			this.x = (short)value;
		else if (index == 1)
			this.y = (short)value;
		else
			throw new RuntimeException("Invalid index " + index);
	}

	public void print() {
		System.out.println(toString());
	}

	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public Point2D_I16 createNewInstance() {
		return new Point2D_I16();
	}

	@Override
	public Point2D_I16 copy() {
		return new Point2D_I16(x, y);
	}

	@Override
	public String toString() {
		return "Point2D_I16{ x= " + x + ", y= " + y + '}';
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj)
			return true;

		if (!(obj instanceof Point2D_I16))
			return false;

		var o = (Point2D_I16)obj;
		return x == o.x && y == o.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}