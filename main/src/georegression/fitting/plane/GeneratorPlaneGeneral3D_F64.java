/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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
import georegression.struct.point.Point3D_F64;
import org.ddogleg.fitting.modelset.ModelGenerator;

import java.util.List;

/**
 * @author Peter Abeles
 */
public class GeneratorPlaneGeneral3D_F64 implements ModelGenerator<PlaneGeneral3D_F64, Point3D_F64> {
	FitPlane3D_F64 fitter = new FitPlane3D_F64();

	PlaneNormal3D_F64 normalPlane = new PlaneNormal3D_F64();

	@Override
	public boolean generate(List<Point3D_F64> list, PlaneGeneral3D_F64 found) {
		if (!fitter.svd(list, normalPlane.p, normalPlane.n))
			return false;
		UtilPlane3D_F64.convert(normalPlane, found);
		return true;
	}

	@Override
	public int getMinimumPoints() {
		return 4;
	}
}
