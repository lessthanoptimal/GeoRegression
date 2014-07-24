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
