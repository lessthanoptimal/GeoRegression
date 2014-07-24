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

package georegression.fitting.sphere;

import georegression.metric.Distance3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import org.ddogleg.optimization.functions.FunctionNtoM;

import java.util.List;

/**
 * Computes the signed Euclidean distance between a sphere and a set of points, see
 * {@link Distance3D_F64#distance(georegression.struct.shapes.Sphere3D_F64, georegression.struct.point.Point3D_F64)}.
 *
 * See {@link georegression.fitting.sphere.CodecSphere3D_F64} for how the model is parametrized.
 * For use in least-squares non-linear minimization.
 *
 * @author Peter Abeles
 */
public class SphereToPointSignedDistance_F64 implements FunctionNtoM {

	// model of the sphere
	private Sphere3D_F64 sphere = new Sphere3D_F64();

	// points whose distance from the sphere is being computed
	private List<Point3D_F64> points;

	// used to convert double[] into shape parameters
	private CodecSphere3D_F64 codec = new CodecSphere3D_F64();

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
	public void process( /**/double[] input, /**/double[] output) {
		codec.decode(input,sphere);

		for( int i = 0; i < points.size(); i++ ) {
			output[i] = Distance3D_F64.distance(sphere,points.get(i));
		}
	}
}
