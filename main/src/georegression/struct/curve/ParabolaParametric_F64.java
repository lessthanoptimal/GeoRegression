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

package georegression.struct.curve;

import georegression.struct.point.Point2D_F64;

/**
 * Parametric form of parabola with 4 parameters.
 *
 * https://math.stackexchange.com/questions/2044922/parametric-form-for-a-general-parabola
 *
 * @author Peter Abeles
 */
public class ParabolaParametric_F64 {
	/**
	 * Vertex of parabola
	 */
	public double x0,y0;
	/**
	 * Focal length
	 */
	public double p;
	/**
	 * Sine and cosine of theta. Theta = direction of axis of symmetric
	 */
	public double sin,cos;

	public void evaulate(double t , Point2D_F64 location ) {
		location.x = x0 - 2*p*t*sin + p*t*t*cos;
		location.y = y0 + 2*p*t*cos + p*t*t*sin;
	}
}
