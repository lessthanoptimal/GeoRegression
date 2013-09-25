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

package georegression.fitting.sphere;

import georegression.struct.point.Point3D_F32;
import georegression.struct.shapes.Sphere3D_F32;
import org.ddogleg.optimization.functions.FunctionNtoMxN;

import java.util.List;

/**
 * Jacobian of {@link SphereToPointSignedDistance_F32}.
 *
 * @author Peter Abeles
 */
// DESIGN NOTE: Could speed up by coupling it to the distance function.  That way distance would only need
//              to be computed once.
public class SphereToPointSignedDistanceJacobian_F32 implements FunctionNtoMxN {

	// model of the sphere
	private Sphere3D_F32 sphere = new Sphere3D_F32();

	// points whose distance from the sphere is being computed
	private List<Point3D_F32> points;

	public void setPoints(List<Point3D_F32> points) {
		this.points = points;
	}

	@Override
	public int getN() {
		return 4;
	}

	@Override
	public int getM() {
		return points.size();
	}

	@Override
	public void process( /**/double[] input, /**/double[] output) {
		sphere.center.x = (float) input[0];
		sphere.center.y = (float) input[1];
		sphere.center.z = (float) input[2];
		sphere.radius = (float) input[3];

		int index = 0;
		for( int i = 0; i < points.size(); i++ ) {
			Point3D_F32 p = points.get(i);
			float euclidean = sphere.center.distance(p);

			output[index++] = (sphere.center.x - p.x)/euclidean;
			output[index++] = (sphere.center.y - p.y)/euclidean;
			output[index++] = (sphere.center.z - p.z)/euclidean;
			output[index++] = -1;
		}
	}
}
