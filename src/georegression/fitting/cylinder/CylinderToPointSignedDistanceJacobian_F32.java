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

import georegression.metric.MiscOps;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.shapes.Cylinder3D_F32;
import org.ddogleg.optimization.functions.FunctionNtoMxN;

import java.util.List;

/**
 * Jacobian of {@link CylinderToPointSignedDistance_F32}.
 *
 * @author Peter Abeles
 */
public class CylinderToPointSignedDistanceJacobian_F32 implements FunctionNtoMxN {
	// model of the cylinder
	private Cylinder3D_F32 cylinder = new Cylinder3D_F32();

	// points whose distance from the sphere is being computed
	private List<Point3D_F32> points;

	// used to convert float[] into shape parameters
	private CodecCylinder3D_F32 codec = new CodecCylinder3D_F32();

	public void setPoints(List<Point3D_F32> points) {
		this.points = points;
	}

	@Override
	public int getInputsN() {
		return 7;
	}

	@Override
	public int getOutputsM() {
		return points.size();
	}

	@Override
	public void process( /**/double[] input, /**/double[] output) {
		codec.decode(input,cylinder);

		Point3D_F32 cp = cylinder.line.p;
		Vector3D_F32 cs = cylinder.line.slope;

		// just need to compute this once
		float slopeDot = cs.dot(cs);
		float slopeNorm = (float)Math.sqrt(slopeDot);

		int index = 0;
		for( int i = 0; i < points.size(); i++ ) {
			Point3D_F32 p = points.get(i);

			float x = cp.x - p.x;
			float y = cp.y - p.y;
			float z = cp.z - p.z;

			float cc = x*x + y*y + z*z;

			float xdots = MiscOps.dot(x, y, z, cs);
			float b = xdots/slopeNorm;

			float distance = cc-b*b;

			// round off error can make distanceSq go negative when it is very close to zero
			if( distance < 0 ) {
				output[index++] = 0;
				output[index++] = 0;
				output[index++] = 0;

				output[index++] = 0;
				output[index++] = 0;
				output[index++] = 0;

				output[index++] = -1;

			} else {
				distance = (float)Math.sqrt(distance);

				output[index++] = (cp.x - p.x - xdots*cs.x/slopeDot)/distance;
				output[index++] = (cp.y - p.y - xdots*cs.y/slopeDot)/distance;
				output[index++] = (cp.z - p.z - xdots*cs.z/slopeDot)/distance;

				output[index++] = -xdots*( (cp.x - p.x)/slopeDot - (xdots/slopeDot)*(cs.x/slopeDot))/distance;
				output[index++] = -xdots*( (cp.y - p.y)/slopeDot - (xdots/slopeDot)*(cs.y/slopeDot))/distance;
				output[index++] = -xdots*( (cp.z - p.z)/slopeDot - (xdots/slopeDot)*(cs.z/slopeDot))/distance;

				output[index++] = -1;
			}
		}
	}
}
