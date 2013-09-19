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

import georegression.struct.line.LineSegment2D_I32;

/**
 * @author Peter Abeles
 */
public class UtilLine2D_I32 {

	/**
	 * Computes the acute angle between the two lines.  Does not check for intersection
	 *
	 * @param line0 First line
	 * @param line1 Second line
	 * @return Acute angle in radians
	 */
	public static double acuteAngle( LineSegment2D_I32 line0 , LineSegment2D_I32 line1 ) {
		int dx0 = line0.b.x - line0.a.x;
		int dy0 = line0.b.y - line0.a.y;
		int dx1 = line1.b.x - line1.a.x;
		int dy1 = line1.b.y - line1.a.y;

		double bottom = Math.sqrt(dx0*dx0 + dy0*dy0) * Math.sqrt(dx1*dx1 + dy1*dy1);
		return Math.acos((dx0*dx1 + dy0*dy1)/bottom);
	}
}
