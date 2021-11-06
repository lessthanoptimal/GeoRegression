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

package georegression.fitting.sphere;

import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import org.ddogleg.optimization.functions.FunctionNtoMxN;
import org.ejml.data.DMatrixRMaj;

import java.util.List;

//CUSTOM ignore org.ejml.data.DMatrixRMaj;
//CUSTOM ignore DMatrixRMaj;

/**
 * Jacobian of {@link SphereToPointSignedDistance_F64}.
 *
 * @author Peter Abeles
 */
// DESIGN NOTE: Could speed up by coupling it to the distance function. That way distance would only need
//              to be computed once.
@SuppressWarnings("NullAway.Init")
public class SphereToPointSignedDistanceJacobian_F64 implements FunctionNtoMxN< /**/DMatrixRMaj > {

	// model of the sphere
	private final Sphere3D_F64 sphere = new Sphere3D_F64();

	// points whose distance from the sphere is being computed
	private List<Point3D_F64> points;

	// used to convert double[] into shape parameters
	private final CodecSphere3D_F64 codec = new CodecSphere3D_F64();

	public void setPoints(List<Point3D_F64> points) {
		this.points = points;
	}

	@Override
	public int getNumOfInputsN() {
		return 4;
	}

	@Override
	public int getNumOfOutputsM() {
		return points.size();
	}

	@Override
	public void process( /**/double[] input, /**/DMatrixRMaj output) {
		codec.decode(input,sphere);

		int index = 0;
		for( int i = 0; i < points.size(); i++ ) {
			Point3D_F64 p = points.get(i);
			double euclidean = sphere.center.distance(p);

			output.data[index++] = (sphere.center.x - p.x)/euclidean;
			output.data[index++] = (sphere.center.y - p.y)/euclidean;
			output.data[index++] = (sphere.center.z - p.z)/euclidean;
			output.data[index++] = -1;
		}
	}

	@Override
	public /**/DMatrixRMaj declareMatrixMxN() {
		return new /**/DMatrixRMaj(getNumOfOutputsM(),getNumOfInputsN());
	}
}
