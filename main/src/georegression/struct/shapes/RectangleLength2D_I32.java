/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

import java.io.Serializable;

/**
 * An axis aligned rectangle in 2D that is specified by its lower extent (x0,y0), width, and height.  The three
 * other corners are {@code (x0 + width-1,y0), (x0,y0 + height-1), (x0 + width-1,y0 + height-1)}.
 */
public class RectangleLength2D_I32 implements Serializable {
	/**
	 * Lower extent x-axis
	 */
	public int x0;
	/**
	 * Lower extent y-axis
	 */
	public int y0;
	/**
	 * Rectangle's width
	 */
	public int width;
	/**
	 * Rectangle's height
	 */
	public int height;

	public RectangleLength2D_I32() {
	}

	public RectangleLength2D_I32(int x0, int y0, int width, int height) {
		this.x0 = x0;
		this.y0 = y0;
		this.width = width;
		this.height = height;
	}

	public void setTo(int tl_x, int tl_y, int width, int height ) {
		this.x0 = tl_x;
		this.y0 = tl_y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Sets lower extent
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void setLowerExtent(int x, int y) {
		this.x0 = x;
		this.y0 = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth( int width ) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight( int height ) {
		this.height = height;
	}

	/**
	 * @return Lower-extent x-coordinate
	 */
	public int getX() {
		return x0;
	}

	/**
	 * Sets the Lower-extent x-coordinate
	 *
	 * @param x Lower-extentx-coordinate
	 */
	public void setX( int x ) {
		this.x0 = x;
	}

	/**
	 * @return Lower-extent y-coordinate
	 */
	public int getY() {
		return y0;
	}

	/**
	 * Sets the Lower-extenty-coordinate
	 *
	 * @param y Lower-extent y-coordinate
	 */
	public void setY( int y ) {
		this.y0 = y;
	}

	/**
	 * Sets this rectangle to be equal to the passed in rectangle.
	 * @param r Rectangle which this is to be set equal to
	 */
	public void setTo(RectangleLength2D_I32 r) {
		this.x0 = r.x0;
		this.y0 = r.y0;
		this.width = r.width;
		this.height = r.height;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"{" +
				"p=[ " + x0 + " , " + y0 +
				"], width=" + width + ", height=" + height + '}';
	}
}
