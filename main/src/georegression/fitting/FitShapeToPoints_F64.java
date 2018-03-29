/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

import java.util.List;

/**
 * General interface for fitting a shape to a set of points. Similar to
 * {@link org.ddogleg.fitting.modelset.ModelFitter} interface
 * but is more specific. No initial estimate and weights can be specified.
 * @author Peter Abeles
 */
public interface FitShapeToPoints_F64<Point,Shape> extends FitShapeToPoints<Point,Shape>
{
	/**
	 * Fits the shape to the points by minimizing some metric.
	 *
	 * @param points (Input) points
	 * @param weights (Input) Weight of each point.
	 * @param output (Output) found shape
	 * @return true if successful or false if it failed
	 */
	boolean process(List<Point> points , double weights[], Shape output );
}
