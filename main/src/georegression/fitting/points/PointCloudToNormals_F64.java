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

import georegression.fitting.plane.FitPlane3D_F64;
import georegression.helper.KdTreePoint3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import org.ddogleg.nn.FactoryNearestNeighbor;
import org.ddogleg.nn.NearestNeighbor;
import org.ddogleg.nn.NnData;
import org.ddogleg.struct.DogArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes in a point cloud and returns the same point cloud with surface norms. In this implementation, we use a KDTree
 * to find all the N local neighbors of each point. A plane is fit to those neighbors and the normal extracted from that.
 * The sign of the normal is arbitrary as additional information is needed.
 */
public class PointCloudToNormals_F64 {
	NearestNeighbor<Point3D_F64> nn = FactoryNearestNeighbor.kdtree(new KdTreePoint3D_F64());

	Helper helper = new Helper();

	public int numNeighbors = 3;

	/**
	 * Computes surface normals for all the inputs using the provided cloud.
	 *
	 * @param input (Input) Points which will have their normals computed
	 * @param cloud (Input) The point cloud which will be sampled for the local planes
	 * @param output (Output) Storage for the found normals of the input
	 */
	public void convert( List<Point3D_F64> input, List<Point3D_F64> cloud, DogArray<Vector3D_F64> output ) {
		output.resize(input.size());
		nn.setPoints(cloud, false);

		convert(0, input.size(), input, output, helper);

		removeInvalidPoints(output);
	}

	/**
	 * Convert all the points within the specified range.
	 */
	protected void convert( int idx0, int idx1,
							List<Point3D_F64> input, DogArray<Vector3D_F64> output, Helper helper) {

		final NearestNeighbor.Search<Point3D_F64> search = helper.search;
		final DogArray<NnData<Point3D_F64>> found = helper.found;
		final FitPlane3D_F64 planeFitter = helper.planeFitter;
		final List<Point3D_F64> inputForFitter = helper.inputForFitter;

		for (int pointIdx = idx0; pointIdx < idx1; pointIdx++) {
			// Find close by points
			Point3D_F64 target = input.get(pointIdx);
			search.findNearest(target, -1, numNeighbors - 1, found);

			// Put into a format that the plane fitting algorithm understands
			inputForFitter.clear();
			for (int foundIdx = 0; foundIdx < found.size; foundIdx++) {
				inputForFitter.add(found.get(foundIdx).point);
			}

			// Find the normal for this plane, assume the target point is on the plane
			Vector3D_F64 normal = output.get(pointIdx);
			if (!planeFitter.solvePoint(inputForFitter, target, normal)) {
				// Mark it as invalid so that it's filtered later on
				normal.x = Double.NaN;
			}
		}
	}

	/**
	 * Points which could not have a normal computed are removed
	 */
	static void removeInvalidPoints( DogArray<Vector3D_F64> output ) {
		for (int i = 0; i < output.size; ) {
			Vector3D_F64 normal = output.get(i);
			if (Double.isNaN(normal.x)) {
				output.removeSwap(i);
			} else {
				i++;
			}
		}
	}

	/**
	 * Contains everything that a single thread needs to search for points
	 */
	protected class Helper {
		NearestNeighbor.Search<Point3D_F64> search = nn.createSearch();
		DogArray<NnData<Point3D_F64>> found = new DogArray<>(NnData::new);
		FitPlane3D_F64 planeFitter = new FitPlane3D_F64();
		List<Point3D_F64> inputForFitter = new ArrayList<>();
	}
}
