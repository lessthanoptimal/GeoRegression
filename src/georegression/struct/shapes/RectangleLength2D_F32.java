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

import java.io.Serializable;

/**
 * <p>
 * An axis aligned rectangle in 2D that is specified by its lower extent (x0,y0), width, and height.  The three
 * other corners are (x0 + width,y0), (x0,y0 + height), (x0 + width,y0 + height).
 * </p>
 *
 * @author Peter Abeles
 */
public class RectangleLength2D_F32 implements Serializable {

	/**
	 * Lower extent x-axis
	 */
	public float x0;
	/**
	 * Lower extent y-axis
	 */
	public float y0;
	/**
	 * Rectangle's width
	 */
	public float width;
	/**
	 * Rectangle's height
	 */
	public float height;

	public RectangleLength2D_F32() {
	}

	public RectangleLength2D_F32(float x0, float y0, float width, float height) {
		this.width = width;
		this.height = height;
		this.x0 = x0;
		this.y0 = y0;
	}

	/**
	 * Sets lower extent
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void setLowerExtent( float x, float y ) {
		this.x0 = x;
		this.y0 = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth( float width ) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight( float height ) {
		this.height = height;
	}

	/**
	 * @return Lower-extent x-coordinate
	 */
	public float getX() {
		return x0;
	}

	/**
	 * Sets the lower-extent x-coordinate
	 *
	 * @param x Lower-extent x-coordinate
	 */
	public void setX( float x ) {
		this.x0 = x;
	}

	/**
	 * @return Lower-extent y-coordinate
	 */
	public float getY() {
		return y0;
	}

	/**
	 * Sets the lower-extent y-coordinate
	 *
	 * @param y Lower-extent y-coordinate
	 */
	public void setY( float y ) {
		this.y0 = y;
	}

	/**
	 * Sets this rectangle to be equal to the passed in rectangle.
	 * @param r Rectangle which this is to be set equal to
	 */
	public void set(RectangleLength2D_I32 r) {
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