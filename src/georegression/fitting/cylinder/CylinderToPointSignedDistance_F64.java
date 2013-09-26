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

package georegression.fitting.cylinder;

import georegression.metric.Distance3D_F64;
import georegression.metric.MiscOps;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import org.ddogleg.optimization.functions.FunctionNtoM;

import java.util.List;

/**
 * Computes the signed Euclidean distance between a cylinder and a set of points, see
 * {@link Distance3D_F64#distance(georegression.struct.shapes.Cylinder3D_F64, georegression.struct.point.Point3D_F64)}.
 * The cylinder is encoded in the input as follows:<br>
 * <pre>
 * cylinder.line.p.x = (double) input[0];
 * cylinder.line.p.y = (double) input[1];
 * cylinder.line.p.z = (double) input[2];
 * cylinder.line.slope.x = (double) input[3];
 * cylinder.line.slope.y = (double) input[4];
 * cylinder.line.slope.z = (double) input[5];
 * cylinder.radius = (double) input[6];
 * </pre>
 * For use in least-squares non-linear minimization.
 *
 * @author Peter Abeles
 */
public class CylinderToPointSignedDistance_F64 implements FunctionNtoM {
	// model of the cylinder
	private Cylinder3D_F64 cylinder = new Cylinder3D_F64();

	// points whose distance from the sphere is being computed
	private List<Point3D_F64> points;

	public void setPoints(List<Point3D_F64> points) {
		this.points = points;
	}

	@Override
	public int getN() {
		return 7;
	}

	@Override
	public int getM() {
		return points.size();
	}

	@Override
	public void process( /**/double[] input, /**/double[] output) {
		cylinder.line.p.x = (double) input[0];
		cylinder.line.p.y = (double) input[1];
		cylinder.line.p.z = (double) input[2];
		cylinder.line.slope.x = (double) input[3];
		cylinder.line.slope.y = (double) input[4];
		cylinder.line.slope.z = (double) input[5];
		cylinder.radius = (double) input[6];

		Point3D_F64 cp = cylinder.line.p;
		Vector3D_F64 cs = cylinder.line.slope;

		// just need to compute this once
		double slopeDot = cs.dot(cs);
		double slopeNorm = Math.sqrt(slopeDot);

		for( int i = 0; i < points.size(); i++ ) {
			Point3D_F64 p = points.get(i);

			double x = cp.x - p.x;
			double y = cp.y - p.y;
			double z = cp.z - p.z;

			double cc = x*x + y*y + z*z;

			double b = MiscOps.dot(x, y, z, cs)/slopeNorm;

			double distance = cc-b*b;

			// round off error can make distanceSq go negative when it is very close to zero
			if( distance < 0 ) {
				distance = 0;
			} else {
				distance = Math.sqrt(distance);
			}

			output[i] = distance - cylinder.radius;
		}
	}
}
