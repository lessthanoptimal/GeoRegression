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

package georegression.fitting.cylinder;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import org.ddogleg.fitting.modelset.ModelFitter;
import org.ddogleg.optimization.FactoryOptimization;
import org.ddogleg.optimization.UnconstrainedLeastSquares;

import java.util.List;

/**
 * {@link UnconstrainedLeastSquares} fitting of 3D points to a {@link Cylinder3D_F64 cylinder}.
 *
 * @author Peter Abeles
 */
public class FitCylinderToPoints_F64 implements ModelFitter<Cylinder3D_F64,Point3D_F64> {

	// functions used by non-linear least squares solver
	private CylinderToPointSignedDistance_F64 function = new CylinderToPointSignedDistance_F64();
	private CylinderToPointSignedDistanceJacobian_F64 jacobian = new CylinderToPointSignedDistanceJacobian_F64();

	// The solver
	private UnconstrainedLeastSquares optimizer;

	// need to convert sphere to double[]
	private /**/double[] param = new /**/double[7];

	// maximum number of iterations
	private int maxIterations;

	// tolerances for optimization
	private double ftol;
	private double gtol;

	// used to convert double[] into shape parameters
	private CodecCylinder3D_F64 codec = new CodecCylinder3D_F64();

	/**
	 * Constructor which provides access to all tuning parameters
	 *
	 * @param optimizer Optimization algorithm
	 * @param maxIterations Maximum number of iterations that the optimizer can perform. Try 100
	 * @param ftol Convergence tolerance. See {@link UnconstrainedLeastSquares}.
	 * @param gtol Convergence tolerance. See {@link UnconstrainedLeastSquares}.
	 */
	public FitCylinderToPoints_F64(UnconstrainedLeastSquares optimizer,
								   int maxIterations, double ftol, double gtol) {
		this.optimizer = optimizer;
		this.maxIterations = maxIterations;
		this.ftol = ftol;
		this.gtol = gtol;
	}

	/**
	 * Simplified constructor. Only process access to the maximum number of iterations.
	 * @param maxIterations Maximum number of iterations. Try 100
	 */
	public FitCylinderToPoints_F64( int maxIterations ) {
		this(FactoryOptimization.levenbergMarquardt(null, false),maxIterations, GrlConstants.DCONV_TOL_B,0);
	}

	@Override
	public boolean fitModel(List<Point3D_F64> dataSet, Cylinder3D_F64 initial, Cylinder3D_F64 found) {

		codec.encode(initial,param);

		function.setPoints(dataSet);
		jacobian.setPoints(dataSet);

		optimizer.setFunction(function,jacobian);
		optimizer.initialize(param,ftol,gtol);

		for( int i = 0; i < maxIterations; i++ ) {
			if( optimizer.iterate() )
				break;
		}

		codec.decode(optimizer.getParameters(), found);

		return true;
	}

	@Override
	public /**/double getFitScore() {
		return optimizer.getFunctionValue();
	}
}
