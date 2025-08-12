/*
 * Copyright (C) 2025, Peter Abeles. All Rights Reserved.
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

package georegression.transform.se;

import georegression.struct.point.Vector3D_F64;
import georegression.struct.se.Se3_F64;
import org.ejml.data.DMatrixRMaj;

import java.util.ArrayList;
import java.util.List;

/**
 * Computes the average SE3 from a list
 */
public class AverageSe3_F64 {
	AverageRotationMatrix_F64 averageRotation = new AverageRotationMatrix_F64();

	/// Workspace for rotation matrices
	List<DMatrixRMaj> rotations = new ArrayList<>();

	public void process( List<Se3_F64> poses, Se3_F64 output ) {
		output.reset();

		// Do nothing if it's empy
		if (poses.isEmpty())
			return;

		rotations.clear();
		for (int i = 0; i < poses.size(); i++) {
			rotations.add(poses.get(i).R);

			Vector3D_F64 T =  poses.get(i).T;
			output.T.x += T.x;
			output.T.y += T.y;
			output.T.z += T.z;
		}

		output.T.divideIP(poses.size());

		averageRotation.process(rotations, output.R);
	}
}
