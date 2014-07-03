/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.geometry.GeometryMath_F64;
import georegression.geometry.UtilPoint3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.SingularValueDecomposition;
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

	SingularValueDecomposition<DenseMatrix64F> svd = DecompositionFactory.svd(3, 3,true,true,false);

	@Override
	public Se3_F64 getTransformSrcToDst() {
		return motion;
	}

	@Override
	public boolean process( List<Point3D_F64> srcPts, List<Point3D_F64> dstPts) {
		if( srcPts.size() != dstPts.size() )
			throw new IllegalArgumentException( "There must be a 1 to 1 correspondence between the two sets of points" );

		// find the mean of both sets of points
		Point3D_F64 meanSrc = UtilPoint3D_F64.mean(srcPts, null );
		Point3D_F64 meanDst = UtilPoint3D_F64.mean(dstPts, null );

		final int N = srcPts.size();

		// compute the cross-covariance matrix Sigma of the two sets of points
		// Sigma = (1/N)*sum(i=1:N,[p*x^T]) + mu_p*mu_x^T
		// for performance reasons usage of matrices is postponed
		double s11 = 0, s12 = 0, s13 = 0;
		double s21 = 0, s22 = 0, s23 = 0;
		double s31 = 0, s32 = 0, s33 = 0;

		for( int i = 0; i < N; i++ ) {
			Point3D_F64 f = srcPts.get( i );
			Point3D_F64 t = dstPts.get( i );

			double dfx = f.x - meanSrc.x;
			double dfy = f.y - meanSrc.y;
			double dfz = f.z - meanSrc.z;

			double dtx = t.x - meanDst.x;
			double dty = t.y - meanDst.y;
			double dtz = t.z - meanDst.z;

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

		DenseMatrix64F U = svd.getU(null,false);
		DenseMatrix64F V = svd.getV(null,false);

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
		GeometryMath_F64.mult(motion.getR(),meanSrc,temp);

		motion.getT().set(meanDst.x - temp.x,meanDst.y - temp.y,meanDst.z - temp.z);

		return true;
	}


	@Override
	public int getMinimumPoints() {
		return 3;
	}
}
