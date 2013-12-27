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

package georegression.geometry;

import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.RectangleCorner2D_F64;

/**
 * Various functions related to polygons.
 *
 * @author Peter Abeles
 */
public class UtilPolygons2D_F64 {

	/**
	 * Converts a rectangle into a quadrilateral
	 *
	 * @param input Rectangle.
	 * @param output Quadrilateral.  Modified.
	 */
	public static void convert( RectangleCorner2D_F64 input , Quadrilateral_F64 output ) {
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
	public static void bounding( Quadrilateral_F64 quad , RectangleCorner2D_F64 rectangle ) {

		rectangle.x0 = Math.min(quad.a.x,quad.b.x);
		rectangle.x0 = Math.min(rectangle.x0,quad.c.x);
		rectangle.x0 = Math.min(rectangle.x0,quad.d.x);

		rectangle.y0 = Math.min(quad.a.y,quad.b.y);
		rectangle.y0 = Math.min(rectangle.y0,quad.c.y);
		rectangle.y0 = Math.min(rectangle.y0,quad.d.y);

		rectangle.x1 = Math.max(quad.a.x,quad.b.x);
		rectangle.x1 = Math.max(rectangle.x1,quad.c.x);
		rectangle.x1 = Math.max(rectangle.x1,quad.d.x);

		rectangle.y1 = Math.max(quad.a.y,quad.b.y);
		rectangle.y1 = Math.max(rectangle.y1,quad.c.y);
		rectangle.y1 = Math.max(rectangle.y1,quad.d.y);
	}
}
