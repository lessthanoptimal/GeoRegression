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

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.geometry.GeometryMath_F64;
import georegression.geometry.UtilPoint3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import org.ejml.alg.dense.decomposition.DecompositionFactory;
import org.ejml.alg.dense.decomposition.SingularValueDecomposition;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.SingularOps;

import java.util.List;

/**
 * <p>
 * Finds the rigid body motion which minimizes the different between the two sets of associated points in 3D.
 * Computes the SVD of the covariance and extracts the motion from the mean of the two
 * sets of points and the U and V components of SVD.
 * </p>
 * <p>
 * No paper to cite.  If anyone has one let me know.
 * </p>
 *
 * @author Peter Abeles
 */
public class MotionSe3PointSVD_F64 implements MotionTransformPoint<Se3_F64, Point3D_F64> {

	// rigid body motion
	private Se3_F64 motion = new Se3_F64();

	SingularValueDecomposition<DenseMatrix64F> svd = DecompositionFactory.svd(3,3);

	@Override
	public Se3_F64 getMotion() {
		return motion;
	}

	@Override
	public boolean process( List<Point3D_F64> fromPts, List<Point3D_F64> toPts ) {
		if( fromPts.size() != toPts.size() )
			throw new IllegalArgumentException( "There must be a 1 to 1 correspondence between the two sets of points" );

		// find the mean of both sets of points
		Point3D_F64 meanFrom = UtilPoint3D_F64.mean( fromPts );
		Point3D_F64 meanTo = UtilPoint3D_F64.mean( toPts );

		final int N = fromPts.size();

		// compute the cross-covariance matrix Sigma of the two sets of points
		// Sigma = (1/N)*sum(i=1:N,[p*x^T]) + mu_p*mu_x^T
		// for performance reasons usage of matrices is postponed
		double s11 = 0, s12 = 0, s13 = 0;
		double s21 = 0, s22 = 0, s23 = 0;
		double s31 = 0, s32 = 0, s33 = 0;

		for( int i = 0; i < N; i++ ) {
			Point3D_F64 f = fromPts.get( i );
			Point3D_F64 t = toPts.get( i );

			double dfx = f.x - meanFrom.x;
			double dfy = f.y - meanFrom.y;
			double dfz = f.z - meanFrom.z;

			double dtx = t.x - meanTo.x;
			double dty = t.y - meanTo.y;
			double dtz = t.z - meanTo.z;

			s11 += dtx*dfx;
			s12 += dtx*dfy;
			s13 += dtx*dfz;
			s21 += dty*dfx;
			s22 += dty*dfy;
			s23 += dty*dfz;
			s31 += dtz*dfx;
			s32 += dtz*dfy;
			s33 += dtz*dfz;
		}

		DenseMatrix64F Sigma = new DenseMatrix64F( 3, 3, true, s11, s12, s13, s21, s22, s23, s31, s32, s33 );

		if( !svd.decompose(Sigma) )
			throw new RuntimeException("SVD failed!?");

		DenseMatrix64F U = svd.getU(false);
		DenseMatrix64F V = svd.getV(false);

		SingularOps.descendingOrder(U,false,svd.getSingularValues(),3,V,false);
		
		if( CommonOps.det(U) < 0 ^ CommonOps.det(V) < 0 ) {
			// swap sign of the column 2
			// this only needs to happen if data is planar
			V.data[2] = -V.data[2];
			V.data[5] = -V.data[5];
			V.data[8] = -V.data[8];
		}

		CommonOps.multTransB(U, V, motion.getR());

		Point3D_F64 temp = new Point3D_F64();
		GeometryMath_F64.mult(motion.getR(),meanFrom,temp);

		motion.getT().set(meanTo.x - temp.x,meanTo.y - temp.y,meanTo.z - temp.z);

		return true;
	}


	@Override
	public int getMinimumPoints() {
		return 3;
	}
}
