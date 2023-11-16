/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.cylinder;

import georegression.geometry.GeometryMath_F64;
import georegression.metric.ClosestPoint3D_F64;
import georegression.metric.Distance3D_F64;
import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.point.PointNormal3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import org.ddogleg.fitting.modelset.ModelGenerator;

import java.util.List;

/**
 * Given a list of two point and surface normal pairs, first a cylinder using an analytic equation.
 */
public class GenerateCylinderFromPointNormals_F64 implements ModelGenerator<Cylinder3D_F64, PointNormal3D_F64> {

	LineParametric3D_F64 lineA = new LineParametric3D_F64();
	LineParametric3D_F64 lineB = new LineParametric3D_F64();

	@Override public boolean generate( List<PointNormal3D_F64> dataSet, Cylinder3D_F64 output ) {
		if (dataSet.size() == 2) {
			return twoPointFormula(dataSet.get(0), dataSet.get(1), output);
		}

		return false;
	}

	/**
	 * If there is no noise and the two points don't lie at the same location or have the same surface normal, then
	 * the following equation is valid.
	 *
	 * @return true if no error detected
	 */
	public boolean twoPointFormula( PointNormal3D_F64 a, PointNormal3D_F64 b, Cylinder3D_F64 output ) {

		// The closest point between the two lines defined by the surface normals and each point lies on
		// the axis of the cylinder
		lineA.p = a.p;
		lineA.slope = a.n;
		lineB.p = b.p;
		lineB.slope = b.n;

		if (null == ClosestPoint3D_F64.closestPoint(lineA, lineB, output.line.p))
			return false;

		// The cross product will point along the axis
		GeometryMath_F64.cross(a.n, b.n, output.line.slope);

		// Find the radius by averaging the two distances
		// Typically using the average of multiple solutions is more stable and accurate.
		double ra = Distance3D_F64.distance(output.line, a.p);
		double rb = Distance3D_F64.distance(output.line, b.p);
		output.radius = (ra + rb)/2.0;

		// If the two points are at the same location then an infinite number of cylinders will match
		// If the two points have the same surface normal then the radius can't be determined

		return true;
	}

	@Override public int getMinimumPoints() {
		return 2;
	}
}
