/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
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

package georegression.struct.shapes;

/**
 * An axis aligned rectangle in 2D that is specified by its top-left (x0,y0) corner, and bottom-right (x1,y1) corner.
 * x0 <= x1 and y0 <= y1.  (x1,y1) is exclusive and not contained in the rectangle.
 */
public class RectangleCorner2D_I32 {
	/**
	 * Top-left corner
	 */
	public int x0,y0;
	/**
	 * Bottom-right corner
	 */
	public int x1,y1;

	public RectangleCorner2D_I32(int x0, int y0, int x1, int y1) {
		set(x0,y0,x1,y1);
	}

	public RectangleCorner2D_I32( RectangleCorner2D_I32 orig ) {
		set(orig);
	}

	public void set( RectangleCorner2D_I32 orig ) {
		this.x0 = orig.x0;
		this.y0 = orig.y0;
		this.x1 = orig.x1;
		this.y1 = orig.y1;
	}

	public void set(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	public RectangleCorner2D_I32() {
	}

	public int getWidth() {
		return x1-x0;
	}

	public int getHeight() {
		return y1-y0;
	}

	public int area() {
		return (y1-y0)*(x1-x0);
	}

	public int getX0() {
		return x0;
	}

	public void setX0(int x0) {
		this.x0 = x0;
	}

	public int getY0() {
		return y0;
	}

	public void setY0(int y0) {
		this.y0 = y0;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}
}
