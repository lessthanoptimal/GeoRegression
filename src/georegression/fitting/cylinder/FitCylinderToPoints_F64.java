/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.fitting.cylinder;

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
	private /**/double ftol;
	private /**/double gtol;

	/**
	 * Constructor which provides access to all tuning parameters
	 *
	 * @param optimizer Optimization algorithm
	 * @param maxIterations Maximum number of iterations that the optimizer can perform. Try 100
	 * @param ftol Convergence tolerance. See {@link UnconstrainedLeastSquares}.
	 * @param gtol Convergence tolerance. See {@link UnconstrainedLeastSquares}.
	 */
	public FitCylinderToPoints_F64(UnconstrainedLeastSquares optimizer,
								   int maxIterations, /**/double ftol, /**/double gtol) {
		this.optimizer = optimizer;
		this.maxIterations = maxIterations;
		this.ftol = ftol;
		this.gtol = gtol;
	}

	/**
	 * Simplified constructor.  Only process access to the maximum number of iterations.
	 * @param maxIterations Maximum number of iterations.  Try 100
	 */
	public FitCylinderToPoints_F64( int maxIterations ) {
		this(FactoryOptimization.leastSquaresLM(1e-3, false),maxIterations,1e-12,0);
	}

	@Override
	public Cylinder3D_F64 createModelInstance() {
		return new Cylinder3D_F64();
	}

	@Override
	public boolean fitModel(List<Point3D_F64> dataSet, Cylinder3D_F64 initial, Cylinder3D_F64 found) {

		param[0] = (double) initial.line.p.x;
		param[1] = (double) initial.line.p.y;
		param[2] = (double) initial.line.p.z;
		param[3] = (double) initial.line.slope.x;
		param[4] = (double) initial.line.slope.y;
		param[5] = (double) initial.line.slope.z;
		param[6] = (double) initial.radius;

		function.setPoints(dataSet);
		jacobian.setPoints(dataSet);

		optimizer.setFunction(function,jacobian);
		optimizer.initialize(param,ftol,gtol);

		for( int i = 0; i < maxIterations; i++ ) {
			if( optimizer.iterate() )
				break;
		}

		/**/double paramOptimized[] = optimizer.getParameters();

		found.line.p.x = (double) paramOptimized[0];
		found.line.p.y = (double) paramOptimized[1];
		found.line.p.z = (double) paramOptimized[2];
		found.line.slope.x = (double) paramOptimized[3];
		found.line.slope.y = (double) paramOptimized[4];
		found.line.slope.z = (double) paramOptimized[5];
		found.radius = (double) paramOptimized[6];

		return true;
	}
}
