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

package georegression.fitting.ellipse;

import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseQuadratic_F64;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.factory.EigenDecomposition;
import org.ejml.factory.LinearSolver;
import org.ejml.factory.LinearSolverFactory;
import org.ejml.ops.CommonOps;

import java.util.List;

/**
 * <p>
 * Fits an ellipse to a set of points in "closed form" by minimizing algebraic least-squares error.  The method used is
 * described in [1] and is a repartitioning of the solution describe in [2], with the aim of improving numerical
 * stability.  The found ellipse is described using 6 coefficients, as is shown below.
 * F(x,y) = a*x^2 + b*x*y + c*y^2 + d*x + e*y + f = 0 and b^2 - 4*ac < 0
 * <p>
 *
 * <ul>
 * <li>[1] Radim Halir and Jan Flusser, "Numerically Stable Direct Least Squares Fitting Of Ellipses" 1998</li>
 * <li>[2] Fitzgibbon, A. W., Pilu, M and Fischer, R. B.: "Direct least squares fitting of ellipses"
 * Technical Report DAIRP-794, Department of Artificial Intelligence, The University of Edinburgh, January 1996</li>
 * </ul>
 *
 * @author Peter Abeles
 */
public class FitEllipseAlgebraicLeastSquares_F64 {

	// qudratic part of design matrix
	DenseMatrix64F D1 = new DenseMatrix64F(3,1);
	// linear part of design matrix
	DenseMatrix64F D2 = new DenseMatrix64F(3,1);

	// quadratic part of scatter matrix
	DenseMatrix64F S1 = new DenseMatrix64F(3,3);
	// combined part of scatter matrix
	DenseMatrix64F S2 = new DenseMatrix64F(3,3);
	//linear part of scatter matrix
	DenseMatrix64F S3 = new DenseMatrix64F(3,3);
	// Reduced scatter matrix
	DenseMatrix64F M = new DenseMatrix64F(3,3);

	// storage for intermediate steps
	DenseMatrix64F T = new DenseMatrix64F(3,3);
	DenseMatrix64F Ta1 = new DenseMatrix64F(3,1);
	DenseMatrix64F S2_tran = new DenseMatrix64F(3,3);

	LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.linear(3);
	EigenDecomposition<DenseMatrix64F> eigen = DecompositionFactory.eig(3,true,false);

	EllipseQuadratic_F64 ellipse = new EllipseQuadratic_F64();

	public boolean process( List<Point2D_F64> points ) {
		int N = points.size();

		// Construct the design matrices.  linear and quadratic
		D1.reshape(N,3); D2.reshape(N,3);
		int index = 0;
		for( int i = 0; i < N; i++ ) {
			Point2D_F64 p = points.get(i);

			// fill in each row one at a time
			D1.data[index]   = p.x*p.x;
			D2.data[index++] = p.x;
			D1.data[index]   = p.x*p.y;
			D2.data[index++] = p.y;
			D1.data[index]   = p.y*p.y;
			D2.data[index++] = 1;
		}

		// Compute scatter matrix
		CommonOps.multTransA(D1,D1,S1);   // S1 = D1'*D1
		CommonOps.multTransA(D1, D2, S2); // S2 = D1'*D2
		CommonOps.multTransA(D2,D2,S3);   // S3 = D2'*D2

		// for getting a2 from a1
		// T = -inv(S3)*S2'
		CommonOps.transpose(S2,S2_tran);
		if( !solver.setA(S3) )
			return false;

		solver.solve(S2_tran,T);
		CommonOps.changeSign(T);

		// Compute reduced scatter matrix
		// M = S1 + S2*T
		CommonOps.mult(S2, T, M);
		CommonOps.add(M,S2,M);

		// Premultiply by inv(C1). inverse of constraint matrix
		for( int row = 0; row < 3; row++ ) {
			double m0 = M.unsafe_get(row,0);
			double m1 = M.unsafe_get(row, 1);
			double m2 = M.unsafe_get(row,2);

			M.unsafe_set(row,0,  m2 / 2);
			M.unsafe_set(row,1, -m1);
			M.unsafe_set(row,2,  m0 / 2);
		}

		if( !eigen.decompose(M) )
			return false;

		DenseMatrix64F a1 =selectBestEigenVector();
		if( a1 == null )
			return false;

		// ellipse coefficients
		CommonOps.mult(T,a1,Ta1);

		ellipse.a = a1.data[0];
		ellipse.b = a1.data[1];
		ellipse.c = a1.data[2];
		ellipse.d = Ta1.data[0];
		ellipse.e = Ta1.data[1];
		ellipse.f = Ta1.data[2];

		return true;
	}

	private DenseMatrix64F selectBestEigenVector() {
		DenseMatrix64F v1 = eigen.getEigenVector(0);
		DenseMatrix64F v2 = eigen.getEigenVector(1);
		DenseMatrix64F v3 = eigen.getEigenVector(2);

		// evaluate a'*C*a
		double cond1 = 4*v1.get(0)*v1.get(2) - v1.get(1)*v1.get(1);
		double cond2 = 4*v2.get(0)*v2.get(2) - v2.get(1)*v2.get(1);
		double cond3 = 4*v3.get(0)*v3.get(2) - v3.get(1)*v3.get(1);


		if( cond1 > 0 )
			return v1;
		if( cond2 > 0 )
			return v2;
		if( cond3 > 0 )
			return v3;

		return null;
	}

	public EllipseQuadratic_F64 getEllipse() {
		return ellipse;
	}
}
