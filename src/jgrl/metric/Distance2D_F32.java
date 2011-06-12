/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.metric;

import jgrl.struct.line.LineParametric2D_F32;
import jgrl.struct.line.LineSegment2D_F32;
import jgrl.struct.point.Point2D_F32;
import jgrl.struct.point.UtilPoint2D_F32;


/**
 * Functions related to finding the distance of one shape from another shape.  This is often
 * closely related to finding the {@link ClosestPoint3D_F32 closest point}/
 *
 * @author Peter Abeles
 */
public class Distance2D_F32 {


	/**
	 * <p>
	 * Returns the distance of the closest point on the line segment from the provided line.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p	The point. Not modified.
	 * @return Distance the closest point on the line is away from the point.
	 */
	public static float distance(LineParametric2D_F32 line,
								 Point2D_F32 p) {
		float t = ClosestPoint2D_F32.closestPointT(line, p);

		float a = line.slope.x * t + line.p.x;
		float b = line.slope.y * t + line.p.y;

		float dx = p.x - a;
		float dy = p.y - b;

		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * <p>
	 * Returns the distance the closest point on a line segment is from the specified point.
	 * The closest point is bounded to be along the line segment.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p	The point. Not modified.
	 * @return Distance the closest point on the line is away from the point.
	 */
	public static float distance(LineSegment2D_F32 line,
								 Point2D_F32 p) {
		float a = line.b.x - line.a.x;
		float b = line.b.y - line.a.y;

		float t = a * (p.x - line.a.x) + b * (p.y - line.a.y);
		t /= (a * a + b * b);

		// if the point of intersection is past the end points return the distance
		// from the closest end point
		if (t < 0) {
			return UtilPoint2D_F32.distance(line.a.x, line.a.y, p.x, p.y);
		} else if (t > 1.0f)
			return UtilPoint2D_F32.distance(line.a.x, line.b.y, p.x, p.y);

		// return the distance of the closest point on the line
		return UtilPoint2D_F32.distance(line.a.x + t * a, line.a.y + t * b, p.x, p.y);
	}
}
