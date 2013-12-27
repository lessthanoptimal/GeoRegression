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

package georegression.fitting.plane;

import georegression.struct.plane.PlaneGeneral3D_F32;
import org.ddogleg.fitting.modelset.ModelCodec;

/**
 * Encodes and decodes {@link PlaneGeneral3D_F32} into float[].
 * <pre>
 * outputModel.A = input[0];
 * outputModel.B = input[1];
 * outputModel.C = input[2];
 * outputModel.D = input[3];
 * </pre>
 *
 * @author Peter Abeles
 */
public class CodecPlaneGeneral3D_F32 implements ModelCodec<PlaneGeneral3D_F32> {

	@Override
	public void decode(/**/double[] input, PlaneGeneral3D_F32 outputModel) {
		outputModel.A = (float) input[0];
		outputModel.B = (float) input[1];
		outputModel.C = (float) input[2];
		outputModel.D = (float) input[3];
	}

	@Override
	public void encode(PlaneGeneral3D_F32 inputModel, /**/double[] output) {
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
