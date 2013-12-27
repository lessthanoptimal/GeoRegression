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

	public String toString() {
		return "RectangleCorner2D_I32( "+x0+" "+y0+" "+x1+" "+y1+" )";
	}
}
