/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.UtilPlane3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import org.ddogleg.fitting.modelset.ModelGenerator;

import java.util.List;

/**
 * Estimates a {@link PlaneGeneral3D_F64} from {@link PlaneNormal3D_F64}. A bit silly, but needed when the
 * surface is described using a point and their normal.
 */
public class GeneratorPlaneFromPlane_F64 implements ModelGenerator<PlaneGeneral3D_F64, PlaneNormal3D_F64> {
	@Override public boolean generate( List<PlaneNormal3D_F64> dataSet, PlaneGeneral3D_F64 output ) {
		if (dataSet.size() != 1)
			throw new IllegalArgumentException("Must have one and only one point");

		UtilPlane3D_F64.convert(dataSet.get(0), output);
		return true;
	}

	@Override public int getMinimumPoints() {
		return 1;
	}
}
