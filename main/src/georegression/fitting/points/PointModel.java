/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.points;

import georegression.struct.GeoTuple_F64;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The model is a set of points in 3D space. The points are simply stored as a list of points and exhaustively
 * searched each time the closest point needs to be found.
 *
 * @author Peter Abeles
 */
public class PointModel<T extends GeoTuple_F64> implements ClosestPointToModel<T> {

	private List<T> points;
	// if the closest point is greater than this distance it is rejected
	private double maxDistanceSq;

	public PointModel(List<T> points) {
		this.points = points;
		this.maxDistanceSq = Double.MAX_VALUE;
	}

	public PointModel(List<T> points, double maxDistance) {
		this.points = points;
		this.maxDistanceSq = maxDistance * maxDistance;
	}

	public void setPoints(List<T> points) {
		this.points = points;
	}

	@Override
	public @Nullable T findClosestPoint(T target) {
		if (points.size() <= 0)
			return null;

		T closestPoint = points.get(0);
		double closestDist = closestPoint.distance2(target);

		for (int i = 1; i < points.size(); i++) {
			T p = points.get(i);

			double d = p.distance2(target);

			if (d < closestDist) {
				closestDist = d;
				closestPoint = p;
			}
		}

		if (closestDist >= maxDistanceSq)
			return null;

		return closestPoint;
	}
}
