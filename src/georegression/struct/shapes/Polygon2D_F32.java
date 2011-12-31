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
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.shapes;

import georegression.struct.point.Point2D_F32;

/**
 * Describes a polygon in 2D.
 *
 * @author Peter Abeles
 */
public class Polygon2D_F32 {

	// vertexes in the polygon
	public Point2D_F32 vertexes[];

	public Polygon2D_F32( int numVertexes ) {
		vertexes = new Point2D_F32[numVertexes];

		for( int i = 0; i < numVertexes; i++ ) {
			vertexes[i] = new Point2D_F32();
		}
	}
}
