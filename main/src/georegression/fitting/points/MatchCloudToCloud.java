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

import georegression.struct.GeoTuple;
import georegression.struct.InvertibleTransform;

import java.util.List;

/**
 * Generalized interface for finding the transform which minimizes the difference between two point clouds.
 * A point cloud is a set of points.
 *
 * @author Peter Abeles
 */
// TODO add accessors to number of points matched in source list
public interface MatchCloudToCloud<SE extends InvertibleTransform, T extends GeoTuple> {

	/**
	 * Provides the list of source points.
	 *
	 * <p>WARNING: Do not modify the passed in list until after processing has finished.</p>
	 *
	 * @param points Point cloud. Call {@link #isModifiedSource()} to see if the list is modified.
	 */
	void setSource( List<T> points );

	/**
	 * Sets the destination point cloud. In algorithms which involve a costly processing step on one of the
	 * point clouds, this is the point cloud it should be done on.
	 *
	 * <p>WARNING: Do not modify the passed in list until after processing has finished.</p>
	 *
	 * @param points Point cloud. Not modified.
	 */
	void setDestination( List<T> points );

	/**
	 * Computes the transform between the two point clouds. Returns if it was successful or not.
	 *
	 * @return True if match between the two point clouds could be found. False otherwise
	 */
	boolean compute();

	/**
	 * Returns the found transform from source to destination. Must call {@link #compute} first.
	 * @return transform
	 */
	SE getSourceToDestination();

	/**
	 * If true then the input list to source is modified.
	 * @return true if source list is modified, false if not modified
	 */
	boolean isModifiedSource();

	/**
	 * Returns the number of points from the source list which were matched to to points in the destination list.
	 * Depending on the algorithm this may or may not be a good measure fit goodness.
	 *
	 * @return Number of matched points.
	 */
	int getMatchedSourcePoints();

}
