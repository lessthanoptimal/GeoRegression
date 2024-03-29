/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point2D_F64;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * <p>
 * An axis aligned rectangle in 2D that is specified by its lower extent (x0,y0), width, and height. The three
 * other corners are (x0 + width,y0), (x0,y0 + height), (x0 + width,y0 + height).
 * </p>
 *
 * @author Peter Abeles
 */
@Getter @Setter
public class RectangleLength2D_F64 implements Serializable {
	/** Lower extent x-axis */
	public double x0;
	/** Lower extent y-axis */
	public double y0;
	/** Rectangle's width */
	public double width;
	/** Rectangle's height */
	public double height;

	public RectangleLength2D_F64() {}

	public RectangleLength2D_F64( double x0, double y0, double width, double height ) {
		this.width = width;
		this.height = height;
		this.x0 = x0;
		this.y0 = y0;
	}

	public RectangleLength2D_F64 setTo( double x0, double y0, double width, double height ) {
		this.width = width;
		this.height = height;
		this.x0 = x0;
		this.y0 = y0;
		return this;
	}

	public RectangleLength2D_F64 setTo( RectangleLength2D_F64 src ) {
		this.x0 = src.x0;
		this.y0 = src.y0;
		this.width = src.width;
		this.height = src.height;
		return this;
	}

	/**
	 * Sets lower extent
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void setLowerExtent( double x, double y ) {
		this.x0 = x;
		this.y0 = y;
	}

	/**
	 * Provides access to corners in the order specified below.
	 * <pre>
	 * [0] = (x0, y0)
	 * [1] = (x0 + w, y0)
	 * [2] = (x0 + w, y0 + h)
	 * [3] = (x0, y0 + h)
	 * </pre>
	 * where w = width, and h = height.
	 *
	 * @param index Which corner
	 * @param corner (Optional) storage for the corner
	 * @return The corner
	 */
	public Point2D_F64 getCorner( int index, @Nullable Point2D_F64 corner ) {
		if (corner == null)
			corner = new Point2D_F64();
		switch (index) {
			case 0 -> corner.setTo(x0, y0);
			case 1 -> corner.setTo(x0 + width, y0);
			case 2 -> corner.setTo(x0 + width, y0 + height);
			case 3 -> corner.setTo(x0, y0 + height);
			default -> throw new IllegalArgumentException("index must be from 0 to 3.");
		}
		return corner;
	}

	/**
	 * Sets this rectangle to be equal to the passed in rectangle.
	 *
	 * @param r Rectangle which this is to be set equal to
	 */
	public RectangleLength2D_F64 setTo( RectangleLength2D_I32 r ) {
		this.x0 = r.x0;
		this.y0 = r.y0;
		this.width = r.width;
		this.height = r.height;
		return this;
	}

	/** Sets the value of all fields to zero */
	public void zero() {
		x0 = y0 = width = height = 0.0;
	}

	public double getX() {return x0;}

	public double getY() {return y0;}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" +
				"p=[ " + x0 + " , " + y0 +
				"], width=" + width + ", height=" + height + '}';
	}
}