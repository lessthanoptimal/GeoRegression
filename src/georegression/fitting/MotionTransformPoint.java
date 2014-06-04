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

package georegression.fitting;

import georegression.struct.GeoTuple;
import georegression.struct.InvertibleTransform;

import java.util.List;

/**
 * Interface for finding a transform that when applied to the 'from' points will minimize
 * the difference between the corresponding point in the 'to' list.
 *
 * @author Peter Abeles
 */
public interface MotionTransformPoint<T extends InvertibleTransform, P extends GeoTuple> {

	/**
	 * Returns the estimated motion from the 'from' coordinate system into the 'to' coordinate system.
	 *
	 * @return motion
	 */
	public T getMotion();

	/**
	 * Processes the sets of corresponding points and finds a transformation which when applied
	 * to 'fromPts' will minimize the difference with the 'toPts'.
	 *
	 * @param fromPts The points which are to be transformed.  Not modified.
	 * @param toPts   The points that are being compared against. Not modified.
	 * @return true if the computation successfully produced a solution and false if not.
	 */
	public boolean process( List<P> fromPts, List<P> toPts );

	/**
	 * Minimum number of points required to compute a model.
	 *
	 * @return The number of points required to compute a model.
	 */
	public int getMinimumPoints();
}
