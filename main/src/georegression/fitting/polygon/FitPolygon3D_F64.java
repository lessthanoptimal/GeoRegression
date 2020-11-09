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

import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Box3D_F64;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Functions related to fitting polygons to different inputs
 *
 * @author Peter Abeles
 */
public class FitPolygon3D_F64 {
	/**
	 * Finds the smallest Axis Aligned Bounding Box (AABB) for the set of set of points. Both
	 * the lower and upper extends are inclusive. x0 &le; p.x &le; x1.
	 *
	 * @param points (Input) set of points
	 * @param rectangle (Output) Storage for the AABB. Nullable
	 * @return The rectangular AABB
	 */
	public static Box3D_F64 boxAabb(final List<Point3D_F64> points, @Nullable Box3D_F64 rectangle) {
		if (rectangle==null)
			rectangle = new Box3D_F64();

		// If empty the output is undefined. Just return a rectangle full of zeros
		if (points.isEmpty()) {
			rectangle.setTo(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			return rectangle;
		}

		Point3D_F64 p0 = points.get(0);
		double x0 = p0.x, x1 = p0.x;
		double y0 = p0.y, y1 = p0.y;
		double z0 = p0.z, z1 = p0.z;

		for (int i = 1; i < points.size(); i++) {
			Point3D_F64 p = points.get(i);
			if (p.x < x0) x0 = p.x;
			else if (p.x > x1) x1 = p.x;
			if (p.y < y0) y0 = p.y;
			else if (p.y > y1) y1 = p.y;
			if (p.z < z0) z0 = p.z;
			else if (p.z > z1) z1 = p.z;
		}

		rectangle.setTo(x0, y0, z0, x1, y1, x1);

		return rectangle;
	}
}
