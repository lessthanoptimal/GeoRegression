/*
 * Copyright (C)  2020, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.points;

import georegression.misc.StoppingCondition;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se2_F64;
import georegression.struct.se.Se3_F64;
import org.junit.jupiter.api.Nested;

/**
 * @author Peter Abeles
 */
public class TestMatchCloudToCloudIcp {

	@Nested
	class Cloud2D extends GeneralCloudToCloudChecksSe2 {
		@Override
		public MatchCloudToCloud<Se2_F64, Point2D_F64> create() {
			return FactoryIterativeClosestPoint.cloudIcp2D_F64(0.1, new StoppingCondition(200, 1e-6));
		}
	}

	@Nested
	class Cloud3D extends GeneralCloudToCloudChecksSe3 {
		@Override
		public MatchCloudToCloud<Se3_F64, Point3D_F64> create() {
			return FactoryIterativeClosestPoint.cloudIcp3D_F64(0.1, new StoppingCondition(200, 1e-6));
		}
	}
}
