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

package georegression.transform.twist;

import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.GeometryMath_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.MatrixFeatures_DDRM;
import org.jetbrains.annotations.Nullable;

/**
 * Operations related to twists.
 *
 * @author Peter Abeles
 */
public class TwistOps_F64 {

	/**
	 * Converts the {@link Se3_F64} object into homogenous notation.<br>
	 *
	 * [ R , T; 0 , 1]
	 *
	 * @param transform transform in regular SE3 format. If null a new matrix will be declared
	 * @param H (Optional) Storage for homogenous 4x4 matrix. If null a new matrix is declared.
	 * @return Homogenous matrix
	 */
	public static DMatrixRMaj homogenous( Se3_F64 transform , @Nullable DMatrixRMaj H ) {
		if( H == null ) {
			H = new DMatrixRMaj(4,4);
		} else {
			H.reshape(4,4);
		}
		CommonOps_DDRM.insert(transform.R,H,0,0);
		H.data[3] = transform.T.x;
		H.data[7] = transform.T.y;
		H.data[11] = transform.T.z;

		H.data[12] =0;H.data[13] =0;H.data[14] =0;H.data[15] =1;

		return H;
	}

	/**
	 * Converts the twist coordinate into homogenous format.<br>
	 * H = [hat(w), v; 0 , 0]
	 *
	 * @param twist Twist coordinate
	 * @param H (Optional) Storage for homogenous 4x4 matrix. If null a new matrix is declared.
	 * @return Homogenous matrix
	 */
	public static DMatrixRMaj homogenous( TwistCoordinate_F64 twist , @Nullable DMatrixRMaj H ) {
		if( H == null ) {
			H = new DMatrixRMaj(4,4);
		} else {
			H.reshape(4,4);
			H.data[12] = 0; H.data[13] = 0; H.data[14] = 0; H.data[15] = 0;
		}

		H.data[0] =  0;         H.data[1] = -twist.w.z; H.data[2]  =  twist.w.y; H.data[3]  = twist.v.x;
		H.data[4] =  twist.w.z; H.data[5] = 0;          H.data[6]  = -twist.w.x; H.data[7]  = twist.v.y;
		H.data[8] = -twist.w.y; H.data[9] =  twist.w.x; H.data[10] =  0;         H.data[11] = twist.v.z;

		return H;
	}

	/**
	 * <p>
	 *     Computes the exponential map for a twist:<br>
	 *     exp(hat(xi)*theta) = [ SO , (I-SO)*(w cross v) + w*w<sup>T</sup>v&Theta; 0 , 1]<br>
	 *     SO = exp(hat(w)*theta)
	 * </p>
	 *
	 * See page 42 in R. Murray, et. al. "A Mathematical Introduction to ROBOTIC MANIPULATION" 1994
	 *
	 * @param twist Twist coordinate
	 * @param theta Magnitude of rotation
	 * @param motion Storage for SE(3). If null a new instance will be returned.
	 * @return The transformation.
	 */
	public static Se3_F64 exponential(TwistCoordinate_F64 twist , double theta , @Nullable Se3_F64 motion ) {
		if( motion == null ) {
			motion = new Se3_F64();
		}

		double w_norm = twist.w.norm();

		if( w_norm == 0.0 ) {
			CommonOps_DDRM.setIdentity(motion.R);
			motion.T.x = twist.v.x*theta;
			motion.T.y = twist.v.y*theta;
			motion.T.z = twist.v.z*theta;
			return motion;
		}

		DMatrixRMaj R = motion.getR();

		// First handle the SO region. This Rodrigues equation
		double wx = twist.w.x/w_norm, wy = twist.w.y/w_norm, wz = twist.w.z/w_norm;

		ConvertRotation3D_F64.rodriguesToMatrix(wx,wy,wz, theta*w_norm, R);

		theta *= w_norm;

		// Now compute the translational component
		// (I - SO)*(w cross v) + w*w'*v*theta
		double vx = twist.v.x, vy = twist.v.y, vz = twist.v.z;

		double wv_x = wy*vz - wz*vy;
		double wv_y = wz*vx - wx*vz;
		double wv_z = wx*vy - wy*vx;

		double left_x = (1 - R.data[0])*wv_x -      R.data[1]*wv_y  -      R.data[2]*wv_z;
		double left_y =     -R.data[3]*wv_x  + (1 - R.data[4])*wv_y -      R.data[5]*wv_z;
		double left_z =     -R.data[6]*wv_x  -      R.data[7]*wv_y  + (1 - R.data[8])*wv_z;

		double right_x = (wx*wx*vx + wx*wy*vy + wx*wz*vz)*theta;
		double right_y = (wy*wx*vx + wy*wy*vy + wy*wz*vz)*theta;
		double right_z = (wz*wx*vx + wz*wy*vy + wz*wz*vz)*theta;

		motion.T.x = (double)left_x + right_x;
		motion.T.y = (double)left_y + right_y;
		motion.T.z = (double)left_z + right_z;
		motion.T.divide(w_norm);

		return motion;
	}

	/**
	 * Converts a rigid body motion into a twist coordinate. The value of theta used to generate the motion
	 * is assumed to be one.
	 *
	 * @param motion (Input) The SE(3) transformation
	 * @param twist (Output) Storage for twist.
	 * @return magnitude of the motion
	 */
	public static TwistCoordinate_F64 twist( Se3_F64 motion , @Nullable TwistCoordinate_F64 twist ) {
		if( twist == null )
			twist = new TwistCoordinate_F64();

		if(MatrixFeatures_DDRM.isIdentity(motion.R, GrlConstants.TEST_F64)) {
			twist.w.setTo(0,0,0);
			twist.v.setTo(motion.T);
		} else {
			Rodrigues_F64 rod = new Rodrigues_F64();
			ConvertRotation3D_F64.matrixToRodrigues(motion.R,rod);

			twist.w.setTo(rod.unitAxisRotation);
			double theta = rod.theta;

			// A = (I-SO)*hat(w) + w*w'*theta
			DMatrixRMaj A = CommonOps_DDRM.identity(3);
			CommonOps_DDRM.subtract(A,motion.R, A);

			DMatrixRMaj w_hat = GeometryMath_F64.crossMatrix(twist.w,null);
			DMatrixRMaj tmp = A.copy();
			CommonOps_DDRM.mult(tmp,w_hat,A);

			Vector3D_F64 w = twist.w;
			A.data[0] += w.x*w.x*theta; A.data[1] += w.x*w.y*theta; A.data[2] += w.x*w.z*theta;
			A.data[3] += w.y*w.x*theta; A.data[4] += w.y*w.y*theta; A.data[5] += w.y*w.z*theta;
			A.data[6] += w.z*w.x*theta; A.data[7] += w.z*w.y*theta; A.data[8] += w.z*w.z*theta;

			DMatrixRMaj y = new DMatrixRMaj(3,1);
			y.data[0] = motion.T.x;
			y.data[1] = motion.T.y;
			y.data[2] = motion.T.z;

			DMatrixRMaj x = new DMatrixRMaj(3,1);

			CommonOps_DDRM.solve(A,y,x);

			twist.w.scale(rod.theta);
			twist.v.x = (double) x.data[0];
			twist.v.y = (double) x.data[1];
			twist.v.z = (double) x.data[2];
			twist.v.scale(rod.theta);
		}
		return twist;
	}
}
