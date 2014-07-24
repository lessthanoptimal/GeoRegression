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

package georegression.metric;

import georegression.geometry.UtilPoint2D_F64;
import georegression.geometry.UtilPoint2D_I32;
import georegression.struct.line.LineSegment2D_I32;
import georegression.struct.point.Point2D_I32;

/**
 * @author Peter Abeles
 */
public class Distance2D_I32 {
	/**
	 * <p>
	 * Returns the distance the closest point on a line segment is from the specified point.
	 * The closest point is bounded to be along the line segment.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p The point. Not modified.
	 * @return Distance the closest point on the line is away from the point.
	 */
	public static double distance( LineSegment2D_I32 line,
								   Point2D_I32 p ) {
		int a = line.b.x - line.a.x;
		int b = line.b.y - line.a.y;

		double t = a * ( p.x - line.a.x ) + b * ( p.y - line.a.y );
		t /= ( a * a + b * b );

		// if the point of intersection is past the end points return the distance
		// from the closest end point
		if( t < 0 ) {
			return UtilPoint2D_I32.distance(line.a.x, line.a.y, p.x, p.y);
		} else if( t > 1.0 )
			return UtilPoint2D_I32.distance( line.b.x, line.b.y, p.x, p.y );

		// return the distance of the closest point on the line
		return UtilPoint2D_F64.distance(line.a.x + t * a, line.a.y + t * b, p.x, p.y);
	}
}
