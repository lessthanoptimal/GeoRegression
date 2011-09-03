/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
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
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.shapes;

/**
 * <p>
 * Describes a 2D rectangle aligned along the coordinate system's axes.
 * </p>
 * <p/>
 * <p>
 * The "top left" point of the rectangle is defined by tl_x and tl_y.  The remaining
 * three corners are found by adding width and/or height to that point.
 * </p>
 *
 * @author Peter Abeles
 */
public class Rectangle2D_F32 {

	// width and height of the rectangle
	public float width;
	public float height;
	// location of the r
	public float tl_x;
	public float tl_y;

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
}