/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.fitting;

import jgrl.struct.GeoTuple;
import jgrl.struct.InvertibleTransform;

import java.util.List;

/**
 * Interface for finding a transform that when applied to the 'from' points which minimize
 * the difference between the corresponding point in the 'to' list.
 *
 * @author Peter Abeles
 */
public interface MotionTransformPoint<T extends InvertibleTransform, P extends GeoTuple> {

	/**
	 * Returns the found motion which minimizes the difference between the two sets of points.
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
