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

package georegression.fitting.cylinder;

import georegression.metric.Distance3D_F32;
import georegression.metric.MiscOps;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.shapes.Cylinder3D_F32;
import org.ddogleg.optimization.functions.FunctionNtoM;

import java.util.List;

/**
 * Computes the signed Euclidean distance between a cylinder and a set of points, see
 * {@link Distance3D_F32#distance(georegression.struct.shapes.Cylinder3D_F32, georegression.struct.point.Point3D_F32)}.
 *
 * See {@link CodecCylinder3D_F32} for how the model is parametrized.
 *
 * @author Peter Abeles
 */
public class CylinderToPointSignedDistance_F32 implements FunctionNtoM {
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
	public int getNumOfInputsN() {
		return 7;
	}

	@Override
	public int getNumOfOutputsM() {
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

		for( int i = 0; i < points.size(); i++ ) {
			Point3D_F32 p = points.get(i);

			float x = cp.x - p.x;
			float y = cp.y - p.y;
			float z = cp.z - p.z;

			float cc = x*x + y*y + z*z;

			float b = MiscOps.dot(x, y, z, cs)/slopeNorm;

			float distance = cc-b*b;

			// round off error can make distanceSq go negative when it is very close to zero
			if( distance < 0 ) {
				distance = 0;
			} else {
				distance = (float)Math.sqrt(distance);
			}

			output[i] = distance - cylinder.radius;
		}
	}
}
