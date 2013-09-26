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

import georegression.struct.shapes.Cylinder3D_F64;
import org.ddogleg.fitting.modelset.ModelCodec;

/**
 * Encodes and decodes {@link Cylinder3D_F64} into double[].
 * <pre>
 * cylinder.line.p.x = input[0];
 * cylinder.line.p.y = input[1];
 * cylinder.line.p.z = input[2];
 * cylinder.line.slope.x = input[3];
 * cylinder.line.slope.y = input[4];
 * cylinder.line.slope.z = input[5];
 * cylinder.radius = input[6];
 * </pre>
 *
 * @author Peter Abeles
 */
public class CodecCylinder3D_F64 implements ModelCodec<Cylinder3D_F64> {
	@Override
	public void decode( /**/double[] input, Cylinder3D_F64 cylinder) {
		cylinder.line.p.x = (double) input[0];
		cylinder.line.p.y = (double) input[1];
		cylinder.line.p.z = (double) input[2];
		cylinder.line.slope.x = (double) input[3];
		cylinder.line.slope.y = (double) input[4];
		cylinder.line.slope.z = (double) input[5];
		cylinder.radius = (double) input[6];
	}

	@Override
	public void encode(Cylinder3D_F64 cylinder, /**/double[] param) {
		param[0] = cylinder.line.p.x;
		param[1] = cylinder.line.p.y;
		param[2] = cylinder.line.p.z;
		param[3] = cylinder.line.slope.x;
		param[4] = cylinder.line.slope.y;
		param[5] = cylinder.line.slope.z;
		param[6] = cylinder.radius;
	}

	@Override
	public int getParamLength() {
		return 7;
	}
}
