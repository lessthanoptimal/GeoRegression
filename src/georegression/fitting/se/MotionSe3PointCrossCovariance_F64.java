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
import georegression.geometry.RotationMatrixGenerator;
import georegression.geometry.UtilPoint3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.so.Quaternion_F64;
import org.ejml.simple.SimpleEVD;
import org.ejml.simple.SimpleMatrix;

import java.util.List;

/**
 * <p>
 * Finds the rigid body motion which minimizes the different between the two sets of associated points in 3D.  Computes
 * the quaternions directly in closed form.
 * </p>

 * <p>
 * The mean square error function that is minimized is:<br>
 * f(q) = (1/N) sum( i=1:N , ||x_i - R(q_r)*p_i + q_t||<sup>2</sup> )<br>
 * where q is the transformation parameterized with q_r  quaternions and q_t translation, x is the set of 'to' points
 * and p is the set of 'from' points.
 * </p>

 * <p>
 * The basic cross-covariance method sketch in Paul J. Besl and Neil D. McKay, "A Method for Registration of 3-D Shapes" IEEE
 * Transactions on Pattern Analysis and Machine Intelligence, Vol 14, No. 2, Feb. 1992, is implemented below.
 * </p>
 *
 * @author Peter Abeles
 */
public class MotionSe3PointCrossCovariance_F64 implements MotionTransformPoint<Se3_F64, Point3D_F64> {

	// first 4 elements are quaternions and last 3 are translation
	private double param[] = new double[7];

	// rigid body motion
	private Se3_F64 motion = new Se3_F64();

	// temporarily stores the quaternion
	private Quaternion_F64 quat = new Quaternion_F64();

	public double[] getParam() {
		return param;
	}

	@Override
	public Se3_F64 getMotion() {
		return motion;
	}

	@Override
	public boolean process( List<Point3D_F64> fromPts, List<Point3D_F64> toPts ) {
		if( fromPts.size() != toPts.size() )
			throw new IllegalArgumentException( "There must be a 1 to 1 correspondence between the two sets of points" );

		// find the mean of both sets of points
		Point3D_F64 meanFrom = UtilPoint3D_F64.mean( fromPts , null );
		Point3D_F64 meanTo = UtilPoint3D_F64.mean( toPts , null );

		final int N = fromPts.size();

		// compute the cross-covariance matrix Sigma of the two sets of points
		// Sigma = (1/N)*sum(i=1:N,[p*x^T]) + mu_p*mu_x^T
		// for performance reasons usage of matrices is postponed
		double s11 = 0, s12 = 0, s13 = 0;
		double s21 = 0, s22 = 0, s23 = 0;
		double s31 = 0, s32 = 0, s33 = 0;

		// mu*mu^2
		double m11 = meanFrom.x * meanTo.x, m12 = meanFrom.x * meanTo.y, m13 = meanFrom.x * meanTo.z;
		double m21 = meanFrom.y * meanTo.x, m22 = meanFrom.y * meanTo.y, m23 = meanFrom.y * meanTo.z;
		double m31 = meanFrom.z * meanTo.x, m32 = meanFrom.z * meanTo.y, m33 = meanFrom.z * meanTo.z;

		for( int i = 0; i < N; i++ ) {
			Point3D_F64 f = fromPts.get( i );
			Point3D_F64 t = toPts.get( i );

			s11 += f.x * t.x;
			s12 += f.x * t.y;
			s13 += f.x * t.z;
			s21 += f.y * t.x;
			s22 += f.y * t.y;
			s23 += f.y * t.z;
			s31 += f.z * t.x;
			s32 += f.z * t.y;
			s33 += f.z * t.z;
		}

		s11 = s11 / N - m11;
		s12 = s12 / N - m12;
		s13 = s13 / N - m13;
		s21 = s21 / N - m21;
		s22 = s22 / N - m22;
		s23 = s23 / N - m23;
		s31 = s31 / N - m31;
		s32 = s32 / N - m32;
		s33 = s33 / N - m33;

		SimpleMatrix Sigma = new SimpleMatrix( 3, 3, true, s11, s12, s13, s21, s22, s23, s31, s32, s33 );

//        Sigma.print();

		SimpleMatrix Delta = new SimpleMatrix( 3, 1, true, s23 - s32, s31 - s13, s12 - s21 );

		SimpleMatrix Q = new SimpleMatrix( 4, 4 );
		SimpleMatrix BR = Sigma.plus( Sigma.transpose() ).minus( SimpleMatrix.identity( 3 ).scale( Sigma.trace() ) );

		Q.set( 0, 0, Sigma.trace() );
		Q.insertIntoThis( 0, 1, Delta.transpose() );
		Q.insertIntoThis( 1, 0, Delta );
		Q.insertIntoThis( 1, 1, BR );

//        Q.print();

		extractQuaternionFromQ( Q );

		// translation = meanTo - R*meanFrom
		GeometryMath_F64.mult( motion.getR(), meanFrom, meanFrom );
		Vector3D_F64 T = motion.getT();
		param[4] = T.x = meanTo.x - meanFrom.x;
		param[5] = T.y = meanTo.y - meanFrom.y;
		param[6] = T.z = meanTo.z - meanFrom.z;

		return true;
	}

	/**
	 * The unit eigenvector corresponding to the maximum eigenvalue of Q is the rotation
	 * parameterized as a quaternion.
	 */
	private void extractQuaternionFromQ( SimpleMatrix q ) {
		SimpleEVD evd = q.eig();
		int indexMax = evd.getIndexMax();

		SimpleMatrix v_max = evd.getEigenVector( indexMax );

		quat.w = (double) v_max.get( 0 );
		quat.x = (double) v_max.get( 1 );
		quat.y = (double) v_max.get( 2 );
		quat.z = (double) v_max.get( 3 );
		quat.normalize();

		RotationMatrixGenerator.quaternionToMatrix( quat, motion.getR() );
	}

	@Override
	public int getMinimumPoints() {
		return 3;
	}
}
