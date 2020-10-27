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

package georegression.fitting.polygon;

import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import org.ddogleg.struct.FastAccess;

/**
 * Interface for Convex Hull algorithms.
 *
 * @author Peter Abeles
 */
public interface FitConvexHull_F64 {
	/**
	 * Fits a convex hull to the provided set of points.
	 *
	 * @param points (Input, Output) Point that the convex hull is fit to. The list may have it's order changed
	 * @param output (Output) The found convex hull.
	 */
	void process(FastAccess<Point2D_F64> points, Polygon2D_F64 output);
}
