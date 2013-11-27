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

import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Polygon2D_I32;
import georegression.struct.shapes.RectangleCorner2D_I32;

import java.util.List;

/**
 * Various functions related to polygons.
 *
 * @author Peter Abeles
 */
public class UtilPolygons2D_I32 {

	/**
	 * Finds the minimum area bounding rectangle which is aligned to the x and y axis around the list of points.
	 * Note (x0,y0) is inclusive and (x1,y1) is exclusive.
	 *
	 * @param points (Input) Quadrilateral
	 * @param rectangle (Output) Minimum area rectangle
	 */
	public static void bounding( List<Point2D_I32> points , RectangleCorner2D_I32 rectangle ) {

		rectangle.x0 = Integer.MAX_VALUE;
		rectangle.y0 = Integer.MAX_VALUE;
		rectangle.x1 = Integer.MIN_VALUE;
		rectangle.y1 = Integer.MIN_VALUE;

		for( int i = 0; i < points.size(); i++ ) {
			Point2D_I32 p = points.get(i);

			if( p.x < rectangle.x0 )
				rectangle.x0 = p.x;
			if( p.x > rectangle.x1 )
				rectangle.x1 = p.x;
			if( p.y < rectangle.y0 )
				rectangle.y0 = p.y;
			if( p.y > rectangle.y1 )
				rectangle.y1 = p.y;
		}

		rectangle.x1++;
		rectangle.y1++;
	}

	/**
	 * Finds the minimum area bounding rectangle which is aligned to the x and y axis around the polygon.
	 * Note (x0,y0) is inclusive and (x1,y1) is exclusive.
	 *
	 * @param quad (Input) Quadrilateral
	 * @param rectangle (Output) Minimum area rectangle
	 */
	public static void bounding( Polygon2D_I32 quad , RectangleCorner2D_I32 rectangle ) {

		UtilPolygons2D_I32.bounding(quad.vertexes.toList(),rectangle);
	}
}
