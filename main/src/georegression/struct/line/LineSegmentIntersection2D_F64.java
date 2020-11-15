/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.struct.line;

import georegression.struct.point.Point2D_F64;

/**
 * Describes the intersection of two lines segments.
 *
 * @author Peter Abeles
 */
public class LineSegmentIntersection2D_F64 {
	/**
	 * 0 = no intersections. 1 = single point. 2 = line segment. If 1 then pa specifies the location.
	 * if 2 then pa and pb specifies the segment of intersection.
	 */
	public int type = -1;
	/** Parametric value for location of intersection on line 0 and line 1. 0.0 to 1.0, inclusive */
	public double t0, t1;
	/** Point of intersection or first point if intersection is a segment */
	public final Point2D_F64 pa = new Point2D_F64();
	/** Point of intersection or first point if intersection is a segment */
	public final Point2D_F64 pb = new Point2D_F64();

	public void reset() {
		type = -1;
		t0 = -1;
		t1 = -1;
		pa.setTo(0,0);
		pb.setTo(0,0);
	}
}
