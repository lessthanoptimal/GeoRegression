/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.point;

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

	public void set( short x, short y ) {
		this.x = x;
		this.y = y;
	}

	public void setX( short x ) {
		this.x = x;
	}

	public void setY( short y ) {
		this.y = y;
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
}