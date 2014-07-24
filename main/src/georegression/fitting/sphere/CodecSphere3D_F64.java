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

import georegression.struct.shapes.Sphere3D_F64;
import org.ddogleg.fitting.modelset.ModelCodec;

/**
 * Encodes and decodes {@link georegression.struct.shapes.Sphere3D_F64} into double[].
 * <pre>
 * sphere.center.x = input[0];
 * sphere.center.y = input[1];
 * sphere.center.z = input[2];
 * sphere.radius = input[3];
 * </pre>
 *
 * @author Peter Abeles
 */
public class CodecSphere3D_F64 implements ModelCodec<Sphere3D_F64> {
	@Override
	public void decode( /**/double[] input, Sphere3D_F64 sphere) {
		sphere.center.x = (double) input[0];
		sphere.center.y = (double) input[1];
		sphere.center.z = (double) input[2];
		sphere.radius = (double) input[3];
	}

	@Override
	public void encode(Sphere3D_F64 sphere, /**/double[] param) {
		param[0] = sphere.center.x;
		param[1] = sphere.center.y;
		param[2] = sphere.center.z;
		param[3] = sphere.radius;
	}

	@Override
	public int getParamLength() {
		return 7;
	}
}
