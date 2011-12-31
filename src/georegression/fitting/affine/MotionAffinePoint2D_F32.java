/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.affine;

import georegression.fitting.MotionTransformPoint;
import georegression.struct.affine.Affine2D_F32;
import georegression.struct.point.Point2D_F32;
import org.ejml.alg.dense.linsol.LinearSolver;
import org.ejml.alg.dense.linsol.LinearSolverFactory;
import org.ejml.data.DenseMatrix64F;

import java.util.List;


/**
 * Finds the best fit model parameters in the least squares sense which can describe the transform
 * from the 'fromPts' list to the 'toPts' list.
 *
 * @author Peter Abeles
 */
public class MotionAffinePoint2D_F32 implements MotionTransformPoint<Affine2D_F32, Point2D_F32> {

	private LinearSolver<DenseMatrix64F> solver;
	private DenseMatrix64F A;
	protected DenseMatrix64F x;
	private DenseMatrix64F y;

	Affine2D_F32 model = new Affine2D_F32();

	public MotionAffinePoint2D_F32() {
		solver = LinearSolverFactory.leastSquares( 100, 2 );
		x = new DenseMatrix64F( 3, 2 );
		A = new DenseMatrix64F( 0, 3 );
		y = new DenseMatrix64F( 0, 2 );
	}

	@Override
	public Affine2D_F32 getMotion() {
		return model;
	}

	/**
	 *  @inheritdoc
	 */
	@Override
	public boolean process( List<Point2D_F32> fromPts, List<Point2D_F32> toPts ) {
		// grow or shrink the matrix sizes
		int N = fromPts.size();

		if( N != toPts.size() ) {
			throw new IllegalArgumentException( "From and to lists must be the same size" );
		} else if( N < 3 ) {
			throw new IllegalArgumentException( "Must be at least 3 points" );
		}

		if( A.data.length < N * 3 ) {
			A.reshape( N, 3, true );
			y.reshape( N, 2, true );
			for( int i = 0; i < N; i++ ) {
				A.set( i, 2, 1 );
			}
		} else {
			A.reshape( N, 3, false );
			y.reshape( N, 2, false );
		}

		// put the data into the matrices
		for( int i = 0; i < N; i++ ) {
			Point2D_F32 pt2 = fromPts.get( i );
			Point2D_F32 pt1 = toPts.get( i );


			A.set( i, 0, pt2.x );
			A.set( i, 1, pt2.y );

			y.set( i, 0, pt1.x );
			y.set( i, 1, pt1.y );
		}

		// decompose A
		if( !solver.setA( A ) )
			return false;

		// solve
		solver.solve( y, x );

		// write it into the model
		model.a11 = (float) x.data[0];
		model.a12 = (float) x.data[2];
		model.tx = (float) x.data[4];
		model.a21 = (float) x.data[1];
		model.a22 = (float) x.data[3];
		model.ty = (float) x.data[5];

		return true;
	}

	@Override
	public int getMinimumPoints() {
		return 3;
	}
}
