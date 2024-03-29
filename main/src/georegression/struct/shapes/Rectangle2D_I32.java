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

import georegression.struct.point.Point2D_I32;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * An axis aligned rectangle in 2D that is specified by its lower extent (x0,y0), and upper extent (x1,y1).
 * x0 &le; x1 and y0 &le; y1. (x1,y1) is exclusive and not contained in the rectangle.
 */
@Getter @Setter
public class Rectangle2D_I32 {
	/** Lower extent */
	public int x0, y0;
	/** Upper extent */
	public int x1, y1;

	public Rectangle2D_I32( int x0, int y0, int x1, int y1 ) {
		setTo(x0, y0, x1, y1);
	}

	public Rectangle2D_I32( Rectangle2D_I32 orig ) {
		setTo(orig);
	}

	public Rectangle2D_I32() {}

	public Rectangle2D_I32 setTo( Rectangle2D_I32 orig ) {
		this.x0 = orig.x0;
		this.y0 = orig.y0;
		this.x1 = orig.x1;
		this.y1 = orig.y1;
		return this;
	}

	public Rectangle2D_I32 setTo( int x0, int y0, int x1, int y1 ) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		return this;
	}

	/** Sets the value of all fields to zero */
	public void zero() {
		x0 = y0 = x1 = y1 = 0;
	}

	/**
	 * Makes sure x0,y0 is the lower extent and x1,y1 is the upper extent
	 */
	public void enforceExtents() {
		if (x1 < x0) {
			int tmp = x1;
			x1 = x0;
			x0 = tmp;
		}
		if (y1 < y0) {
			int tmp = y1;
			y1 = y0;
			y0 = tmp;
		}
	}

	/**
	 * Provides access to corners in the order specified below.
	 * <pre>
	 * [0] = (x0, y0)
	 * [1] = (x1, y0)
	 * [2] = (x1, y1)
	 * [3] = (x0, y1)
	 * </pre>
	 * where w = width, and h = height.
	 *
	 * @param index Which corner
	 * @param corner (Optional) storage for the corner
	 * @return The corner
	 */
	public Point2D_I32 getCorner( int index, @Nullable Point2D_I32 corner ) {
		if (corner == null)
			corner = new Point2D_I32();
		switch (index) {
			case 0 -> corner.setTo(x0, y0);
			case 1 -> corner.setTo(x1, y0);
			case 2 -> corner.setTo(x1, y1);
			case 3 -> corner.setTo(x0, y1);
			default -> throw new IllegalArgumentException("index must be from 0 to 3.");
		}
		return corner;
	}

	public boolean isEquals( int x0, int y0, int x1, int y1 ) {
		return this.x0 == x0 && this.y0 == y0 && this.x1 == x1 && this.y1 == y1;
	}

	public int getWidth() {
		return x1 - x0;
	}

	public int getHeight() {
		return y1 - y0;
	}

	public int area() {
		return (y1 - y0)*(x1 - x0);
	}

	@Override
	public String toString() {
		return "RectangleCorner2D_I32( " + x0 + " " + y0 + " " + x1 + " " + y1 + " )";
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj)
			return true;

		if (!(obj instanceof Rectangle2D_I32))
			return false;

		var o = (Rectangle2D_I32)obj;
		return x0 == o.x0 && y0 == o.y0 && x1 == o.x1 && y1 == o.y1;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(x0 + y0 + x1 + y1);
	}
}
