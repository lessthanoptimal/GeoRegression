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

package georegression.fitting.plane;

import georegression.struct.plane.PlaneGeneral3D_F64;
import org.ddogleg.fitting.modelset.ModelCodec;

/**
 * Encodes and decodes {@link PlaneGeneral3D_F64} into double[].
 * <pre>
 * outputModel.A = input[0];
 * outputModel.B = input[1];
 * outputModel.C = input[2];
 * outputModel.D = input[3];
 * </pre>
 *
 * @author Peter Abeles
 */
public class CodecPlaneGeneral3D_F64 implements ModelCodec<PlaneGeneral3D_F64> {

	@Override
	public void decode(/**/double[] input, PlaneGeneral3D_F64 outputModel) {
		outputModel.A = (double) input[0];
		outputModel.B = (double) input[1];
		outputModel.C = (double) input[2];
		outputModel.D = (double) input[3];
	}

	@Override
	public void encode(PlaneGeneral3D_F64 inputModel, /**/double[] output) {
		output[0] = inputModel.A;
		output[1] = inputModel.B;
		output[2] = inputModel.C;
		output[3] = inputModel.D;
	}

	@Override
	public int getParamLength() {
		return 4;
	}
}
