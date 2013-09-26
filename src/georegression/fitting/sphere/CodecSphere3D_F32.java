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

import georegression.struct.shapes.Sphere3D_F32;
import org.ddogleg.fitting.modelset.ModelCodec;

/**
 * Encodes and decodes {@link georegression.struct.shapes.Sphere3D_F32} into float[].
 * <pre>
 * sphere.center.x = input[0];
 * sphere.center.y = input[1];
 * sphere.center.z = input[2];
 * sphere.radius = input[3];
 * </pre>
 *
 * @author Peter Abeles
 */
public class CodecSphere3D_F32 implements ModelCodec<Sphere3D_F32> {
	@Override
	public void decode( /**/double[] input, Sphere3D_F32 sphere) {
		sphere.center.x = (float) input[0];
		sphere.center.y = (float) input[1];
		sphere.center.z = (float) input[2];
		sphere.radius = (float) input[3];
	}

	@Override
	public void encode(Sphere3D_F32 sphere, /**/double[] param) {
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
