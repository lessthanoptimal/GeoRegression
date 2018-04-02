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

import georegression.fitting.FitShapeToPoints_F64;
import georegression.geometry.UtilCurves_F64;
import georegression.struct.curve.ConicGeneral_F64;
import georegression.struct.curve.ParabolaGeneral_F64;
import georegression.struct.point.Point2D_F64;
import org.ddogleg.optimization.ConvergeOptParam;
import org.ddogleg.optimization.FactoryOptimization;
import org.ddogleg.optimization.UnconstrainedLeastSquares;
import org.ddogleg.optimization.UtilOptimize;
import org.ddogleg.optimization.functions.FunctionNtoM;
import org.ddogleg.optimization.functions.FunctionNtoMxN;
import org.ejml.data.DMatrixRMaj;

import java.util.List;

/**
 * Fits a parabola to the set of points. A parabola is defined as a non-linear equation but can be estimated
 * using non-linear optimization.
 *
 * @author Peter Abeles
 */
public class FitParabola_F64 implements FitShapeToPoints_F64<Point2D_F64,ParabolaGeneral_F64> {

	// Estimate initial parameters for parabola using conic
	FitShapeToPoints_F64<Point2D_F64,ConicGeneral_F64> fitConic;
	ConicGeneral_F64 conic = new ConicGeneral_F64();

	// nonlinear optimization
	UnconstrainedLeastSquares<DMatrixRMaj> minimizer = FactoryOptimization.leastSquaresLM(1e-3,false);
	ConvergeOptParam converge = new ConvergeOptParam(20,1e-8,1e-4);

	// reference to input list
	List<Point2D_F64> points;
	double []weights;

	// Workspace for optimization
	double initial[] = new double[5];
	Function function = new Function();
	Gradient gradient = new Gradient();
	FunctionWeight functionW = new FunctionWeight();
	GradientWeight gradientW = new GradientWeight();

	public FitParabola_F64(FitShapeToPoints_F64<Point2D_F64, ConicGeneral_F64> fitConic) {
		this.fitConic = fitConic;
	}

	public FitParabola_F64() {
		this( new FitConicAtA_F64() );
	}

	@Override
	public boolean process(List<Point2D_F64> points, ParabolaGeneral_F64 output)
	{
		this.points = points;

		// initial estimate for the parameters
		if( !fitConic.process(points,conic) )
			return false;

		// refine this estimate
		UtilCurves_F64.convert(conic,output);
		initial[0] = output.A;
		initial[1] = output.C;
		initial[2] = output.D;
		initial[3] = output.E;
		initial[4] = output.F;
		minimizer.setFunction(function,gradient);
		minimizer.initialize(initial,converge.ftol,converge.gtol);
		UtilOptimize.process(minimizer,converge.maxIterations);

		return true;
	}

	@Override
	public boolean process(List<Point2D_F64> points, double[] weights, ParabolaGeneral_F64 output)
	{
		this.points = points;
		this.weights = weights;

		// initial estimate for the parameters
		if( !fitConic.process(points,weights,conic) )
			return false;

		// Non iterative refinement
		UtilCurves_F64.convert(conic,output);
		initial[0] = output.A;
		initial[1] = output.C;
		initial[2] = output.D;
		initial[3] = output.E;
		initial[4] = output.F;
		minimizer.setFunction(functionW,gradientW);
		minimizer.initialize(initial,converge.ftol,converge.gtol);
		UtilOptimize.process(minimizer,converge.maxIterations);

		return true;
	}

	public UnconstrainedLeastSquares<DMatrixRMaj> getMinimizer() {
		return minimizer;
	}

	public void setMinimizer(UnconstrainedLeastSquares<DMatrixRMaj> minimizer) {
		this.minimizer = minimizer;
	}

	public ConvergeOptParam getConverge() {
		return converge;
	}

	public void setConverge(ConvergeOptParam converge) {
		this.converge = converge;
	}

	class Function implements FunctionNtoM {

		ParabolaGeneral_F64 parabola = new ParabolaGeneral_F64();

		@Override
		public int getNumOfInputsN() {
			return 5;
		}

		@Override
		public int getNumOfOutputsM() {
			return points.size();
		}

		@Override
		public void process(double[] input, double[] output) {
			parabola.set(input[0],input[1],input[2],input[3],input[4]);

			for( int i = 0; i < points.size(); i++ ) {
				Point2D_F64 p = points.get(i);
				output[i] = parabola.evaluate(p.x,p.y);
			}
		}

	}

	class Gradient implements FunctionNtoMxN<DMatrixRMaj> {

		@Override
		public int getNumOfInputsN() {
			return 5;
		}

		@Override
		public int getNumOfOutputsM() {
			return points.size();
		}

		@Override
		public void process(double[] input, DMatrixRMaj output) {
			double A = input[0];
			double C = input[1];

			final int N = points.size();
			for( int i = 0,index=0; i < N; i++ ) {
				Point2D_F64 p = points.get(i);

				double AC2 = 2.0*(A*p.x + C*p.y);

				output.data[index++] = p.x*AC2;
				output.data[index++] = p.y*AC2;
				output.data[index++] = p.x;
				output.data[index++] = p.y;
				output.data[index++] = 1;
			}
		}

		@Override
		public DMatrixRMaj declareMatrixMxN() {
			return new DMatrixRMaj(getNumOfOutputsM(),getNumOfInputsN());
		}
	}

	class FunctionWeight implements FunctionNtoM {

		ParabolaGeneral_F64 parabola = new ParabolaGeneral_F64();

		@Override
		public int getNumOfInputsN() {
			return 5;
		}

		@Override
		public int getNumOfOutputsM() {
			return points.size();
		}

		@Override
		public void process(double[] input, double[] output) {
			parabola.set(input[0],input[1],input[2],input[3],input[4]);

			for( int i = 0; i < points.size(); i++ ) {
				Point2D_F64 p = points.get(i);
				output[i] = parabola.evaluate(p.x,p.y)*weights[i];
			}
		}

	}

	class GradientWeight implements FunctionNtoMxN<DMatrixRMaj> {

		@Override
		public int getNumOfInputsN() {
			return 5;
		}

		@Override
		public int getNumOfOutputsM() {
			return points.size();
		}

		@Override
		public void process(double[] input, DMatrixRMaj output) {
			double A = input[0];
			double C = input[1];

			final int N = points.size();
			for( int i = 0,index=0; i < N; i++ ) {
				Point2D_F64 p = points.get(i);
				double w = weights[i];

				double AC2 = 2.0*(A*p.x + C*p.y);

				output.data[index++] = w*p.x*AC2;
				output.data[index++] = w*p.y*AC2;
				output.data[index++] = w*p.x;
				output.data[index++] = w*p.y;
				output.data[index++] = w;
			}
		}

		@Override
		public DMatrixRMaj declareMatrixMxN() {
			return new DMatrixRMaj(getNumOfOutputsM(),getNumOfInputsN());
		}
	}
}
