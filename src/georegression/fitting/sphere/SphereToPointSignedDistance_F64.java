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

import georegression.metric.Distance3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import org.ddogleg.optimization.functions.FunctionNtoM;

import java.util.List;

/**
 * Computes the signed Euclidean distance between a sphere and a set of points, see
 * {@link Distance3D_F64#distance(georegression.struct.shapes.Sphere3D_F64, georegression.struct.point.Point3D_F64)}.
 * The sphere is encoded in the input as follows:<br>
 * <pre>
 * sphere.center.x = input[0];
 * sphere.center.y = input[1];
 * sphere.center.z = input[2];
 * sphere.radius = input[3];
 * </pre>
 * For use in least-squares non-linear minimization.
 *
 * @author Peter Abeles
 */
public class SphereToPointSignedDistance_F64 implements FunctionNtoM {

	// model of the sphere
	private Sphere3D_F64 sphere = new Sphere3D_F64();

	// points whose distance from the sphere is being computed
	private List<Point3D_F64> points;

	public void setPoints(List<Point3D_F64> points) {
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
		sphere.center.x = (double) input[0];
		sphere.center.y = (double) input[1];
		sphere.center.z = (double) input[2];
		sphere.radius = (double) input[3];

		for( int i = 0; i < points.size(); i++ ) {
			output[i] = Distance3D_F64.distance(sphere,points.get(i));
		}
	}
}
