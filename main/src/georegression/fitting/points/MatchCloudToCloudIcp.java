/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

import georegression.fitting.MotionTransformPoint;
import georegression.fitting.points.IterativeClosestPoint.Distance;
import georegression.misc.StoppingCondition;
import georegression.struct.GeoTuple;
import georegression.struct.InvertibleTransform;
import org.ddogleg.nn.NearestNeighbor;
import org.ddogleg.nn.NnData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Abstract implementation of ICP for point cloud fitting using an
 * {@link InvertibleTransform arbitrary transform}.
 *
 * @author Peter Abeles
 * @see IterativeClosestPoint
 */
@SuppressWarnings("NullAway.Init")
public abstract class MatchCloudToCloudIcp<SE extends InvertibleTransform, P extends GeoTuple>
		implements MatchCloudToCloud<SE, P> {
	// Nearest-neighbor algorithm
	NearestNeighbor<P> nn;
	NearestNeighbor.Search<P> searchNN;

	// computes the distance betwene two points
	Distance<P> distance;

	// reference to destination list
	List<P> source;
	// maximum distance apart two points can be. Euclidean squared
	double maxDistanceSq;
	// storage for NN results
	NnData<P> storageNN = new NnData<>();

	// the actual ICP algorithm
	IterativeClosestPoint<SE, P> icp;

	/**
	 * Configures ICP
	 *
	 * @param motion        estimates motion between two sets of associated points
	 * @param nn            Nearest-Neighbor search
	 * @param maxDistanceSq Maximum distance between two paired points. Euclidean squared
	 * @param stop          Stopping criteria for ICP iterations
	 */
	protected MatchCloudToCloudIcp(MotionTransformPoint<SE, P> motion,
								   NearestNeighbor<P> nn,
								   Distance<P> distance,
								   double maxDistanceSq, StoppingCondition stop) {
		this.maxDistanceSq = maxDistanceSq;
		this.nn = nn;
		this.distance = distance;
		icp = new IterativeClosestPoint<>(stop, motion, distance);
		icp.setModel(new Model());

		searchNN = nn.createSearch();
	}

	@Override
	public void setSource(List<P> source) {
		this.source = source;
	}

	@Override
	public void setDestination(List<P> destination) {
		nn.setPoints(destination, false);
	}

	@Override
	public boolean compute() {
		return icp.process(source);
	}

	@Override
	public SE getSourceToDestination() {
		return icp.getPointsToModel();
	}

	@Override
	public boolean isModifiedSource() {
		return true;
	}

	@Override
	public int getMatchedSourcePoints() {
		return icp.getTotalMatched();
	}

	public IterativeClosestPoint<SE, P> getIcp() {
		return icp;
	}

	private class Model implements ClosestPointToModel<P> {
		@Override
		public @Nullable P findClosestPoint(P target) {
			if (searchNN.findNearest(target, maxDistanceSq, storageNN)) {
				return storageNN.point;
			} else {
				return null;
			}
		}
	}
}
