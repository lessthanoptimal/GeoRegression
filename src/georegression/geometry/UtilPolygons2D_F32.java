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

package georegression.geometry;

import georegression.struct.shapes.Quadrilateral_F32;
import georegression.struct.shapes.RectangleCorner2D_F32;

/**
 * Various functions related to polygons.
 *
 * @author Peter Abeles
 */
public class UtilPolygons2D_F32 {

	/**
	 * Converts a rectangle into a quadrilateral
	 *
	 * @param input Rectangle.
	 * @param output Quadrilateral.  Modified.
	 */
	public static void convert( RectangleCorner2D_F32 input , Quadrilateral_F32 output ) {
		output.a.x = input.x0;
		output.a.y = input.y0;

		output.b.x = input.x1;
		output.b.y = input.y0;

		output.c.x = input.x1;
		output.c.y = input.y1;

		output.d.x = input.x0;
		output.d.y = input.y1;
	}

	/**
	 * Finds the minimum area bounding rectangle around the quadrilateral.
	 *
	 * @param quad (Input) Quadrilateral
	 * @param rectangle (Output) Minimum area rectangle
	 */
	public static void bounding( Quadrilateral_F32 quad , RectangleCorner2D_F32 rectangle ) {

		rectangle.x0 = (float)Math.min(quad.a.x,quad.b.x);
		rectangle.x0 = (float)Math.min(rectangle.x0,quad.c.x);
		rectangle.x0 = (float)Math.min(rectangle.x0,quad.d.x);

		rectangle.y0 = (float)Math.min(quad.a.y,quad.b.y);
		rectangle.y0 = (float)Math.min(rectangle.y0,quad.c.y);
		rectangle.y0 = (float)Math.min(rectangle.y0,quad.d.y);

		rectangle.x1 = (float)Math.max(quad.a.x,quad.b.x);
		rectangle.x1 = (float)Math.max(rectangle.x1,quad.c.x);
		rectangle.x1 = (float)Math.max(rectangle.x1,quad.d.x);

		rectangle.y1 = (float)Math.max(quad.a.y,quad.b.y);
		rectangle.y1 = (float)Math.max(rectangle.y1,quad.c.y);
		rectangle.y1 = (float)Math.max(rectangle.y1,quad.d.y);
	}
}
