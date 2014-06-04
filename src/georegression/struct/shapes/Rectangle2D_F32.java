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
 * An axis aligned rectangle in 2D that is specified by its top-left (tl_x,tl_y) corner, width, and height.  The three
 * other corners are (tl_x + width,tl_y), (tl_x,tl_y + height), (tl_x + width,tl_y + height).
 * </p>
 *
 * @author Peter Abeles
 */
public class Rectangle2D_F32 implements Serializable {

	/**
	 * Top-left corner x-axis
	 */
	public float tl_x;
	/**
	 * Top-left corner y-axis
	 */
	public float tl_y;
	/**
	 * Rectangle's width
	 */
	public float width;
	/**
	 * Rectangle's height
	 */
	public float height;

	public Rectangle2D_F32() {
	}

	public Rectangle2D_F32( float tl_x, float tl_y, float width, float height ) {
		this.width = width;
		this.height = height;
		this.tl_x = tl_x;
		this.tl_y = tl_y;
	}

	/**
	 * Sets the top left point
	 *
	 * @param x
	 * @param y
	 */
	public void setPoint( float x, float y ) {
		this.tl_x = x;
		this.tl_y = y;
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
	 * @return top left x-coordinate
	 */
	public float getX() {
		return tl_x;
	}

	/**
	 * Sets the top left x-coordinate
	 *
	 * @param x top left x-coordinate
	 */
	public void setX( float x ) {
		this.tl_x = x;
	}

	/**
	 * @return top left y-coordinate
	 */
	public float getY() {
		return tl_y;
	}

	/**
	 * Sets the top left y-coordinate
	 *
	 * @param y top left y-coordinate
	 */
	public void setY( float y ) {
		this.tl_y = y;
	}

	/**
	 * Sets this rectangle to be equal to the passed in rectangle.
	 * @param r Rectangle which this is to be set equal to
	 */
	public void set(Rectangle2D_I32 r) {
		this.tl_x = r.tl_x;
		this.tl_y = r.tl_y;
		this.width = r.width;
		this.height = r.height;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"{" +
				"tl=[ " + tl_x + " , " + tl_y +
				"], width=" + width + ", height=" + height + '}';
	}
}