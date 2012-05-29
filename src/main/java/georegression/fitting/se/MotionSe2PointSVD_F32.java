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
import georegression.geometry.GeometryMath_F32;
import georegression.geometry.UtilPoint2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.se.Se2_F32;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

import java.util.List;

/**
 * <p>
 * Finds the rigid body motion which minimizes the different between the two sets of associated points in 3D.  The
 * rotation is computed from the SVD of a cross correlation matrix.
 * </p>
 * <p/>
 * <p>
 * The mean square error function that is minimized is:<br>
 * f(p) = (1/N) sum( i=1:N , ||x_i - R(theta)*p_i + T||<sup>2</sup> )<br>
 * where theta is the angle of rotation and T is the translation, x is the set of 'to' points
 * and p is the set of 'from' points.
 * </p>
 * <p/>
 * <p>
 * Based upon the sketch of Arun et al. 1987 provided in: D.W. Eggert, A. Loruso, R.B. Fisher, "Estimating 3-D Rigid Body Transformation:
 * A Comparison of Four Major Algorithms" Machine Vision and Applications 1997
 * </p>
 *
 * @author Peter Abeles
 */
public class MotionSe2PointSVD_F32 implements MotionTransformPoint<Se2_F32, Point2D_F32> {

	Se2_F32 motion = new Se2_F32();

	@Override
	public Se2_F32 getMotion() {
		return motion;
	}

	@Override
	public boolean process( List<Point2D_F32> fromPts, List<Point2D_F32> toPts ) {
		if( fromPts.size() != toPts.size() )
			throw new IllegalArgumentException( "There must be a 1 to 1 correspondence between the two sets of points" );

		// find the mean of both sets of points
		Point2D_F32 meanFrom = UtilPoint2D_F32.mean( fromPts );
		Point2D_F32 meanTo = UtilPoint2D_F32.mean( toPts );

		final int N = fromPts.size();

		// compute the cross-covariance matrix Sigma of the two sets of points
		// Sigma = (1/N)*sum(i=1:N,[p*x^T]) + mu_p*mu_x^T
		// for performance reasons usage of matrices is postponed
		float s11 = 0, s12 = 0;
		float s21 = 0, s22 = 0;

		// mu*mu^2
		float m11 = meanFrom.x * meanTo.x, m12 = meanFrom.x * meanTo.y;
		float m21 = meanFrom.y * meanTo.x, m22 = meanFrom.y * meanTo.y;

		for( int i = 0; i < N; i++ ) {
			Point2D_F32 f = fromPts.get( i );
			Point2D_F32 t = toPts.get( i );

			s11 += f.x * t.x;
			s12 += f.x * t.y;
			s21 += f.y * t.x;
			s22 += f.y * t.y;
		}

		s11 = s11 / N - m11;
		s12 = s12 / N - m12;
		s21 = s21 / N - m21;
		s22 = s22 / N - m22;

		SimpleMatrix Sigma = new SimpleMatrix( 2, 2, true, s11, s12, s21, s22 );

		// Compute the SVD of the cross correlation matrix
		// The rotation matrix is R = V*U^T

		SimpleSVD evd = Sigma.svd( false );
		SimpleMatrix U = evd.getU();
		SimpleMatrix V = evd.getV();

		SimpleMatrix R = V.mult( U.transpose() );

		// There are situations where R might not have a determinant of one and is instead
		// a reflection is returned
		if( R.determinant() < 0 ) {
			for( int i = 0; i < 2; i++ )
				V.set( i, 1, -V.get( i, 1 ) );
			R = V.mult( U.transpose() );
			float det = (float) R.determinant();
			if( det < 0 ) {
				System.out.println( "Crap" );
			}
		}

		// extract the yaw from the rotation matrix
		float yaw = (float)Math.atan2( R.get( 1, 0 ), R.get( 0, 0 ) );

		// save the results
		GeometryMath_F32.rotate( yaw, meanFrom, meanFrom );
		motion.getTranslation().x = meanTo.x - meanFrom.x;
		motion.getTranslation().y = meanTo.y - meanFrom.y;
		motion.setYaw( yaw );

		return true;
	}

	@Override
	public int getMinimumPoints() {
		return 3;
	}
}
