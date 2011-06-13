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

package jgrl.struct.shapes;

/**
 * An axis aligned rectangle in 2D that is specified with integers
 */
public class Rectangle2DInt {
	private int tl_x;
	private int tl_y;
	private int width;
	private int height;

	public Rectangle2DInt( int tl_x, int tl_y, int width, int height ) {
		this.tl_x = tl_x;
		this.tl_y = tl_y;
		this.width = width;
		this.height = height;
	}

	public final int getX() {
		return tl_x;
	}

	public final int getY() {
		return tl_y;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}
}
