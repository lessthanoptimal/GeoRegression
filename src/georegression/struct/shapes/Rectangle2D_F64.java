/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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
public class Rectangle2D_F64 {

	// width and height of the rectangle
	public double width;
	public double height;
	// location of the r
	public double tl_x;
	public double tl_y;

	public Rectangle2D_F64() {
	}

	public Rectangle2D_F64( double tl_x, double tl_y, double width, double height ) {
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
	public void setPoint( double x, double y ) {
		this.tl_x = x;
		this.tl_y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth( double width ) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight( double height ) {
		this.height = height;
	}

	/**
	 * @return top left x-coordinate
	 */
	public double getX() {
		return tl_x;
	}

	/**
	 * Sets the top left x-coordinate
	 *
	 * @param x top left x-coordinate
	 */
	public void setX( double x ) {
		this.tl_x = x;
	}

	/**
	 * @return top left y-coordinate
	 */
	public double getY() {
		return tl_y;
	}

	/**
	 * Sets the top left y-coordinate
	 *
	 * @param y top left y-coordinate
	 */
	public void setY( double y ) {
		this.tl_y = y;
	}
}