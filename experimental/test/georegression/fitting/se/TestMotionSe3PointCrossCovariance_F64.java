/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import org.junit.jupiter.api.Disabled;

/**
 * @author Peter Abeles
 */
@Disabled
public class TestMotionSe3PointCrossCovariance_F64 extends GeneralMotionSe3Tests_F64 {

	@Override
	MotionTransformPoint<Se3_F64, Point3D_F64> createAlg() {
		return new MotionSe3PointCrossCovariance_F64();
	}

}
