/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.so;

import georegression.fitting.MotionTransformPoint;
import georegression.struct.point.Point3D_F64;
import georegression.struct.so.So3_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.SingularOps_DDRM;
import org.ejml.dense.row.factory.DecompositionFactory_DDRM;
import org.ejml.interfaces.decomposition.SingularValueDecomposition_F64;

import java.util.List;

/**
 * <p>
 * Finds the rotation which minimizes the different between the two sets of associated points in 3D.
 * Computes the SVD of the covariance and extracts the motion from the mean of the two
 * sets of points and the U and V components of SVD.
 * </p>
 * <p>
 * No paper to cite. If anyone has one let me know.
 * </p>
 *
 * @author Peter Abeles
 */
public class MotionSo3PointSVD_F64 implements MotionTransformPoint<So3_F64, Point3D_F64> {

	// Pure rotational motion
	private So3_F64 rotation = new So3_F64();

	SingularValueDecomposition_F64<DMatrixRMaj> svd = DecompositionFactory_DDRM.svd(3, 3,true,true,false);

	@Override
	public So3_F64 getTransformSrcToDst() {
		return rotation;
	}

	@Override
	public boolean process( List<Point3D_F64> srcPts, List<Point3D_F64> dstPts) {
		if( srcPts.size() != dstPts.size() )
			throw new IllegalArgumentException( "There must be a 1 to 1 correspondence between the two sets of points" );

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

			s11 += t.x*f.x;
			s12 += t.x*f.y;
			s13 += t.x*f.z;
			s21 += t.y*f.x;
			s22 += t.y*f.y;
			s23 += t.y*f.z;
			s31 += t.z*f.x;
			s32 += t.z*f.y;
			s33 += t.z*f.z;
		}

		DMatrixRMaj Sigma = new DMatrixRMaj( 3, 3, true, s11, s12, s13, s21, s22, s23, s31, s32, s33 );

		if( !svd.decompose(Sigma) )
			return false;

		DMatrixRMaj U = svd.getU(null,false);
		DMatrixRMaj V = svd.getV(null,false);

		SingularOps_DDRM.descendingOrder(U,false,svd.getSingularValues(),3,V,false);
		
		if( CommonOps_DDRM.det(U) < 0 ^ CommonOps_DDRM.det(V) < 0 ) {
			// swap sign of the column 2
			// this only needs to happen if data is planar
			V.data[2] = -V.data[2];
			V.data[5] = -V.data[5];
			V.data[8] = -V.data[8];
		}

		CommonOps_DDRM.multTransB(U, V, rotation.R);

		return true;
	}


	@Override
	public int getMinimumPoints() {
		return 3;
	}
}
