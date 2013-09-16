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
public class RectangleCorner2D_F32 {
	/**
	 * Top-left corner
	 */
	public float x0,y0;
	/**
	 * Bottom-right corner
	 */
	public float x1,y1;

	public RectangleCorner2D_F32(float x0, float y0, float x1, float y1) {
		set(x0,y0,x1,y1);
	}

	public RectangleCorner2D_F32(RectangleCorner2D_F32 orig) {
		set(orig);
	}

	public void set( RectangleCorner2D_F32 orig ) {
		this.x0 = orig.x0;
		this.y0 = orig.y0;
		this.x1 = orig.x1;
		this.y1 = orig.y1;
	}

	public void set(float x0, float y0, float x1, float y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	public RectangleCorner2D_F32() {
	}

	public float getWidth() {
		return x1-x0;
	}

	public float getHeight() {
		return y1-y0;
	}

	public float area() {
		return (y1-y0)*(x1-x0);
	}

	public float getX0() {
		return x0;
	}

	public void setX0(float x0) {
		this.x0 = x0;
	}

	public float getY0() {
		return y0;
	}

	public void setY0(float y0) {
		this.y0 = y0;
	}

	public float getX1() {
		return x1;
	}

	public void setX1(float x1) {
		this.x1 = x1;
	}

	public float getY1() {
		return y1;
	}

	public void setY1(float y1) {
		this.y1 = y1;
	}

	public String toString() {
		return getClass().getSimpleName()+"( "+x0+" "+y0+" "+x1+" "+y1+" )";
	}
}
