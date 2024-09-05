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

package georegression.fitting.points;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import org.ddogleg.DDoglegConcurrency;
import org.ddogleg.struct.DogArray;
import pabeles.concurrency.GrowArray;

import java.util.List;

/**
 * A concurrent implementation of {@link PointCloudToNormals_F64}.
 */
public class PointCloudToNormals_MT_F64 extends PointCloudToNormals_F64 {

	/** There needs to be at least this many points for it to use the concurrent implementation */
	public int minimumPointsConcurrent = 200;

	GrowArray<Helper> concurrentHelper = new GrowArray<>(Helper::new);

	@Override
	public void convert( List<Point3D_F64> input, List<Point3D_F64> cloud, DogArray<Vector3D_F64> output ) {
		output.resize(input.size());
		nn.setPoints(cloud, false);

		if (input.size() < minimumPointsConcurrent) {
			convert(0, input.size(), input, output, helper);
		} else {
			// Take advantage that every normal is computed independently of each other
			DDoglegConcurrency.loopBlocks(0, input.size(), concurrentHelper, ( helper, idx0, idx1 ) -> {
				convert(idx0, idx1, input, output, helper);
			});
		}

		removeInvalidPoints(output);
	}
}
