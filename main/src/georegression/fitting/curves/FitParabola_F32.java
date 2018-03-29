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

package georegression.fitting.curves;

import georegression.fitting.FitShapeToPoints_F32;
import georegression.struct.curve.ConicGeneral_F32;
import georegression.struct.curve.ParabolaGeneral_F32;
import georegression.struct.point.Point2D_F32;

import java.util.List;

/**
 * Fits a parabola to the set of points. A parabola is defined as a non-linear equation but can be estimated
 * using non-linear optimization and Lagrange multipliers.
 *
 * @author Peter Abeles
 */
public class FitParabola_F32 implements FitShapeToPoints_F32<Point2D_F32,ParabolaGeneral_F32> {

	FitShapeToPoints_F32<Point2D_F32,ConicGeneral_F32> fitConic;
	ConicGeneral_F32 conic = new ConicGeneral_F32();

	public FitParabola_F32(FitShapeToPoints_F32<Point2D_F32, ConicGeneral_F32> fitConic) {
		this.fitConic = fitConic;
	}

	@Override
	public boolean process(List<Point2D_F32> points, ParabolaGeneral_F32 output)
	{
		// initial estimate for the parameters
		fitConic.process(points,conic);

		// TODO refine this estimate

		return false;
	}

	@Override
	public boolean process(List<Point2D_F32> points, float[] weights, ParabolaGeneral_F32 output)
	{
		// initial estimate for the parameters
		fitConic.process(points,weights,conic);

		// TODO refine this estimate

		return false;
	}
}