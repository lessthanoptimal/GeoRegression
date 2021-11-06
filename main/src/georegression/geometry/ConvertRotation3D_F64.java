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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.so.Quaternion_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.factory.DecompositionFactory_DDRM;
import org.ejml.interfaces.decomposition.SingularValueDecomposition;
import org.jetbrains.annotations.Nullable;


/**
 * Provides functions to convert between different parameterizations of 3D rotation matrices.
 *
 * @author Peter Abeles
 */
public class ConvertRotation3D_F64 {

	/**
	 * Converts {@link georegression.struct.so.Rodrigues_F64} into a rotation matrix.
	 *
	 * @param rodrigues rotation defined using rotation axis angle notation.
	 * @param R where the results will be stored. If null a new matrix is declared internally.
	 * @return rotation matrix.
	 */
	public static DMatrixRMaj rodriguesToMatrix( Rodrigues_F64 rodrigues, @Nullable DMatrixRMaj R ) {
		return rodriguesToMatrix(
				rodrigues.unitAxisRotation.x,
				rodrigues.unitAxisRotation.y,
				rodrigues.unitAxisRotation.z,
				rodrigues.theta, R);
	}

	/**
	 * Converts axis angle ({@link Rodrigues_F64}) into a rotation matrix with out needing to declare a storage
	 * variable.
	 *
	 * @param axisX x-component of normalized rotation vector
	 * @param axisY y-component of normalized rotation vector
	 * @param axisZ z-component of normalized rotation vector
	 * @param theta magnitude of rotation in radians
	 * @param R (Optional) storage for 3x3 rotation matrix. If null one will be declared internally.
	 * @return Rotation matrix.
	 */
	public static DMatrixRMaj rodriguesToMatrix( double axisX , double axisY , double axisZ , double theta,
												 @Nullable DMatrixRMaj R ) {
		R = checkDeclare3x3( R );

		//noinspection UnnecessaryLocalVariable
		double x = axisX, y = axisY, z = axisZ;

		double c = Math.cos( theta );
		double s = Math.sin( theta );
		double oc = 1.0 - c;

		R.data[0] = c + x * x * oc;
		R.data[1] = x * y * oc - z * s;
		R.data[2] = x * z * oc + y * s;
		R.data[3] = y * x * oc + z * s;
		R.data[4] = c + y * y * oc;
		R.data[5] = y * z * oc - x * s;
		R.data[6] = z * x * oc - y * s;
		R.data[7] = z * y * oc + x * s;
		R.data[8] = c + z * z * oc;

		return R;
	}

	/**
	 * <p>Converts{@link georegression.struct.so.Rodrigues_F64} into an euler rotation of different types</p>
	 *
	 * @param rodrigues rotation defined using rotation axis angle notation.
	 * @param type Type of Euler rotation
	 * @param euler (Output) Optional storage for Euler rotation
	 * @return The Euler rotation.
	 */
	public static double[] rodriguesToEuler(Rodrigues_F64 rodrigues , EulerType type , @Nullable double []euler )
	{
		DMatrixRMaj R = rodriguesToMatrix(rodrigues,null);
		return matrixToEuler(R,type,euler);
	}

	/**
	 * <p>
	 * Converts {@link georegression.struct.so.Rodrigues_F64} into a unit {@link georegression.struct.so.Quaternion_F64}.
	 * </p>
	 *
	 * @param rodrigues The angle of rotation around the rotation axis.
	 * @param quat Storage for quaternion coordinate. If null a new quaternion is created. Modified.
	 * @return unit quaternion coordinate.
	 */
	public static Quaternion_F64 rodriguesToQuaternion( Rodrigues_F64 rodrigues,
														@Nullable Quaternion_F64 quat ) {
		if( quat == null )
			quat = new Quaternion_F64();

		quat.w = Math.cos( rodrigues.theta / 2.0 );
		double s = Math.sin( rodrigues.theta / 2.0 );

		quat.x = rodrigues.unitAxisRotation.x * s;
		quat.y = rodrigues.unitAxisRotation.y * s;
		quat.z = rodrigues.unitAxisRotation.z * s;

		return quat;
	}

	/**
	 * Converts a unit {@link Quaternion_F64} into {@link Rodrigues_F64}.
	 * @param quat (Input) Unit quaternion
	 * @param rodrigues (Optional) Storage for rodrigues coodinate. If null a new instance is created.
	 * @return rodrigues
	 */
	public static Rodrigues_F64 quaternionToRodrigues( Quaternion_F64 quat,
													   @Nullable Rodrigues_F64 rodrigues ) {
		if( rodrigues == null )
			rodrigues = new Rodrigues_F64();

		rodrigues.unitAxisRotation.setTo( quat.x, quat.y, quat.z);
		rodrigues.unitAxisRotation.normalize();

		rodrigues.theta = 2.0 * Math.acos( quat.w);

		return rodrigues;
	}

	/**
	 * <p>Converts a quaternion into an euler rotation of different types</p>
	 *
	 * @param q (Input) Normalized quaternion. Not modified.
	 * @param type Type of Euler rotation
	 * @param euler (Output) Optional storage for Euler rotation
	 * @return The Euler rotation.
	 */
	public static double[] quaternionToEuler(Quaternion_F64 q , EulerType type , @Nullable double []euler )
	{
		DMatrixRMaj R = quaternionToMatrix(q,null);
		return matrixToEuler(R,type,euler);
	}

	/**
	 * <p>Converts a rotation matrix into an Euler angle of different types</p>
	 *
	 * @param R (Input) Rotation matrix. Not modified.
	 * @param type Type of Euler rotation
	 * @param euler (Output) Optional storage for Euler rotation
	 * @return The Euler rotation.
	 */
	public static double[] matrixToEuler(DMatrixRMaj R , EulerType type , @Nullable double[] euler ) {
		if( euler == null )
			euler = new double[3];

		switch(type){
			case ZYX:
				TanSinTan(-2,1,  3,  -6,9,  5,-7,4,8,  R,euler);
				break;

			case ZYZ:
				TanCosTan(8,-7,  9,   6,3,  5,-7,4,8,  R,euler);
				break;

			case ZXY:
				TanSinTan(4,5,  -6,   3,9,  1,8,-2,7,  R,euler);
				break;

			case ZXZ:
				TanCosTan(7,8,   9,  3,-6,  1,8,-2,7,  R,euler);
				break;

			case YXZ:
				TanSinTan(-7,9,  8, -2,5,  1,-6,3,4,   R,euler);
				break;

			case YXY:
				TanCosTan(4,-6,  5,  2,8,  1,-6,3,4,   R,euler);
				break;

			case YZX:
				TanSinTan(3,1,  -2,  8,5,  9,4,-7,6,   R,euler);
				break;

			case YZY:
				TanCosTan(6,4,   5,  8,-2, 9,4,-7,6,   R,euler);
				break;

			case XYZ:
				TanSinTan(8,9,  -7,  4,1,  5,3,-6,2,   R,euler);
				break;

			case XYX:
				TanCosTan(2,3,   1,  4,-7, 5,3,-6,2,   R,euler);
				break;

			case XZY:
				TanSinTan(-6,5,  4, -7,1,  9,-2,8,3,   R,euler);
				break;

			case XZX:
				TanCosTan(3,-2,  1,  7,4,  9,-2,8,3,   R,euler);
				break;

			default:
				throw new IllegalArgumentException("Unknown rotation sequence");
		}

		return euler;
	}

	private static void TanSinTan( int y0 , int x0 , int sin1 , int y2 , int x2 ,
								   int cos0a , int cos0b , int sin0a , int sin0b,
								   DMatrixRMaj R , double euler[] ) {

		double val_y0 = get(R,y0);
		double val_x0 = get(R,x0);
		double val_sin1 = get(R,sin1);
		double val_y2 = get(R,y2);
		double val_x2 = get(R,x2);

		if( 1.0-Math.abs(val_sin1) <= GrlConstants.EPS ) {

			double sign = Math.signum(val_sin1);

			double sin0 = (get(R,sin0a)+sign*get(R,sin0b))/2.0;
			double cos0 = (get(R,cos0a)+sign*get(R,cos0b))/2.0;

			euler[0] = Math.atan2(sin0,cos0);
			euler[1] = sign * Math.PI/2.0;
			euler[2] = 0;
		} else {
			euler[0] = Math.atan2(val_y0,val_x0);
			euler[1] = Math.asin(val_sin1);
			euler[2] = Math.atan2(val_y2,val_x2);
		}
	}

	private static void TanCosTan( int y0 , int x0 , int cos1 , int y2 , int x2 ,
								   int cos0a , int cos0b , int sin0a , int sin0b,
								  DMatrixRMaj R , double euler[] ) {

		double val_y0 = get(R,y0);
		double val_x0 = get(R,x0);
		double val_cos1 = get(R,cos1);
		double val_y2 = get(R,y2);
		double val_x2 = get(R,x2);

		if( 1.0-Math.abs(val_cos1) <= GrlConstants.EPS ) {
			double sin0 = (get(R,sin0a)+get(R,sin0b))/2.0;
			double cos0 = (get(R,cos0a)+get(R,cos0b))/2.0;

			euler[0] = Math.atan2(sin0,cos0);
			euler[1] = 0;
			euler[2] = 0;
		} else {
			euler[0] = Math.atan2(val_y0,val_x0);
			euler[1] = Math.acos(val_cos1);
			euler[2] = Math.atan2(val_y2,val_x2);
		}
	}

	/**
	 * If the index is negative it returns the negative of the value at -index. Starts at 0
	 */
	private static double get( DMatrixRMaj M , int index ) {
		if( index < 0 ) {
			return -M.data[-index-1];
		} else {
			return M.data[index-1];
		}
	}

	/**
	 * Extracts quaternions from the provided rotation matrix.
	 *
	 * @param R (Input) rotation matrix
	 * @param quat (Output) Optional storage for quaternion. If null a new class will be used.
	 * @return unit quaternion representation of the rotation matrix.
	 */
	public static Quaternion_F64 matrixToQuaternion( DMatrixRMaj R, @Nullable Quaternion_F64 quat ) {

		if( quat == null )
			quat = new Quaternion_F64();

		// algorithm from:
		// http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/
		//
		// Designed to minimize numerical error by not dividing by very small numbers

		double m00 = R.unsafe_get(0,0);
		double m01 = R.unsafe_get(0,1);
		double m02 = R.unsafe_get(0,2);
		double m10 = R.unsafe_get(1,0);
		double m11 = R.unsafe_get(1,1);
		double m12 = R.unsafe_get(1,2);
		double m20 = R.unsafe_get(2,0);
		double m21 = R.unsafe_get(2,1);
		double m22 = R.unsafe_get(2,2);

		double trace = m00 + m11 + m22;

		if (trace > 0) {
			double S = Math.sqrt(trace+1.0) * 2; // S=4*qw
			quat.w = 0.25 * S;
			quat.x = (m21 - m12) / S;
			quat.y = (m02 - m20) / S;
			quat.z = (m10 - m01) / S;
		} else if ((m00 > m11)&&(m00 > m22)) {
			double S = Math.sqrt(1.0 + m00 - m11 - m22) * 2; // S=4*qx
			quat.w = (m21 - m12) / S;
			quat.x = 0.25 * S;
			quat.y = (m01 + m10) / S;
			quat.z = (m02 + m20) / S;
		} else if (m11 > m22) {
			double S = Math.sqrt(1.0 + m11 - m00 - m22) * 2; // S=4*qy
			quat.w = (m02 - m20) / S;
			quat.x = (m01 + m10) / S;
			quat.y = 0.25 * S;
			quat.z = (m12 + m21) / S;
		} else {
			double S = Math.sqrt(1.0 + m22 - m00 - m11) * 2; // S=4*qz
			quat.w = (m10 - m01) / S;
			quat.x = (m02 + m20) / S;
			quat.y = (m12 + m21) / S;
			quat.z = 0.25 * S;
		}

		return quat;
	}

	/**
	 * Converts a rotation matrix into {@link georegression.struct.so.Rodrigues_F64}.
	 *
	 * @param R Rotation matrix.
	 * @param rodrigues Storage used for solution. If null a new instance is declared.
	 * @return The found axis and rotation angle.
	 */
	public static Rodrigues_F64 matrixToRodrigues( DMatrixRMaj R, @Nullable Rodrigues_F64 rodrigues ) {
		if( rodrigues == null ) {
			rodrigues = new Rodrigues_F64();
		}
		// parts of this are from wikipedia
		// http://en.wikipedia.org/wiki/Rotation_representation_%28mathematics%29#Rotation_matrix_.E2.86.94_Euler_axis.2Fangle

		double diagSum = ( (R.unsafe_get( 0, 0 ) + R.unsafe_get( 1, 1 ) + R.unsafe_get( 2, 2 )) - 1.0 ) / 2.0;

		double absDiagSum = Math.abs(diagSum);
		
		if( absDiagSum <= 1.0 && 1.0-absDiagSum > 10.0*GrlConstants.EPS ) {
			// if numerically stable use a faster technique
			rodrigues.theta = Math.acos(diagSum);
			double bottom = 2.0 * Math.sin(rodrigues.theta);

			// in cases where bottom is close to zero that means theta is also close to zero and the vector
			// doesn't matter that much
			rodrigues.unitAxisRotation.x = (R.unsafe_get(2, 1) - R.unsafe_get(1, 2)) / bottom;
			rodrigues.unitAxisRotation.y = (R.unsafe_get(0, 2) - R.unsafe_get(2, 0)) / bottom;
			rodrigues.unitAxisRotation.z = (R.unsafe_get(1, 0) - R.unsafe_get(0, 1)) / bottom;

			// in extreme underflow situations the result can be unnormalized
			rodrigues.unitAxisRotation.normalize();

			// In theory this might be more stable
			// rotationAxis( R, rodrigues.unitAxisRotation);
		} else {

			// this handles the special case where the bottom is very very small or equal to zero
			if( diagSum >= 1.0 )
				rodrigues.theta = 0;
			else if( diagSum <= -1.0 )
				rodrigues.theta = Math.PI;
			else
				rodrigues.theta = Math.acos(diagSum);

			// compute the value of x,y,z up to a sign ambiguity
			rodrigues.unitAxisRotation.x = Math.sqrt((R.get(0, 0) + 1) / 2);
			rodrigues.unitAxisRotation.y = Math.sqrt((R.get(1, 1) + 1) / 2);
			rodrigues.unitAxisRotation.z = Math.sqrt((R.get(2, 2) + 1) / 2);

			double x = rodrigues.unitAxisRotation.x;
			double y = rodrigues.unitAxisRotation.y;
			double z = rodrigues.unitAxisRotation.z;

			if (Math.abs(R.get(1, 0) - 2 * x * y) > GrlConstants.EPS) {
				x *= -1;
			}
			if (Math.abs(R.get(2, 0) - 2 * x * z) > GrlConstants.EPS) {
				z *= -1;
			}
			if (Math.abs(R.get(2,1) - 2 * z * y) > GrlConstants.EPS) {
				y *= -1;
				x *= -1;
			}

			rodrigues.unitAxisRotation.x = x;
			rodrigues.unitAxisRotation.y = y;
			rodrigues.unitAxisRotation.z = z;
		}

		return rodrigues;
	}

	/**
	 * Creates a rotation matrix about the x-axis.
	 *
	 * @param ang the angle it rotates a point by in radians.
	 * @param R (Output) Optional storage for rotation matrix. Modified.
	 * @return The 3 by 3 rotation matrix.
	 */
	public static DMatrixRMaj rotX( double ang, @Nullable DMatrixRMaj R ) {
		if( R == null )
			R = new DMatrixRMaj( 3, 3 );

		setRotX( ang, R );

		return R;
	}

	/**
	 * Sets the values in the specified matrix to a rotation matrix about the x-axis.
	 *
	 * @param ang the angle it rotates a point by in radians.
	 * @param R (Output) Storage for rotation matrix. Modified.
	 */
	public static void setRotX( double ang, DMatrixRMaj R ) {
		double c = Math.cos( ang );
		double s = Math.sin( ang );

		R.set( 0, 0, 1 );
		R.set( 1, 1, c );
		R.set( 1, 2, -s );
		R.set( 2, 1, s );
		R.set( 2, 2, c );
	}

	/**
	 * Creates a rotation matrix about the y-axis.
	 *
	 * @param ang the angle it rotates a point by in radians.
	 * @param R (Output) Optional storage for rotation matrix. Modified.
	 * @return The 3 by 3 rotation matrix.
	 */
	public static DMatrixRMaj rotY( double ang, @Nullable DMatrixRMaj R ) {
		R = checkDeclare3x3( R );

		setRotY( ang, R );

		return R;
	}

	/**
	 * Sets the values in the specified matrix to a rotation matrix about the y-axis.
	 *
	 * @param ang the angle it rotates a point by in radians.
	 * @param r   A 3 by 3 matrix. Is modified.
	 */
	public static void setRotY( double ang, DMatrixRMaj r ) {
		double c = Math.cos( ang );
		double s = Math.sin( ang );

		r.set( 0, 0, c );
		r.set( 0, 2, s );
		r.set( 1, 1, 1 );
		r.set( 2, 0, -s );
		r.set( 2, 2, c );
	}

	/**
	 * Creates a rotation matrix about the z-axis.
	 *
	 * @param ang the angle it rotates a point by in radians.
	 * @param R (Output) Optional storage for rotation matrix. Modified.
	 * @return The 3 by 3 rotation matrix.
	 */
	public static DMatrixRMaj rotZ( double ang, @Nullable DMatrixRMaj R ) {
		R = checkDeclare3x3( R );

		setRotZ( ang, R );

		return R;
	}

	/**
	 * Sets the values in the specified matrix to a rotation matrix about the z-axis.
	 *
	 * @param ang the angle it rotates a point by in radians.
	 * @param r   A 3 by 3 matrix. Is modified.
	 */
	public static void setRotZ( double ang, DMatrixRMaj r ) {
		double c = Math.cos( ang );
		double s = Math.sin( ang );

		r.set( 0, 0, c );
		r.set( 0, 1, -s );
		r.set( 1, 0, s );
		r.set( 1, 1, c );
		r.set( 2, 2, 1 );
	}

	/**
	 * Converts an Euler coordinate into a rotation matrix. Different type of Euler coordinates are accepted.
	 * @param type Which Euler coordinate is the input in
	 * @param rotA Angle of rotation around axis A. First rotation
	 * @param rotB Angle of rotation around axis B   Second rotation
	 * @param rotC Angle of rotation around axis C   Third rotation
	 * @param R (Output) Optional storage for output rotation matrix
	 * @return Rotation matrix
	 */
	public static DMatrixRMaj eulerToMatrix( EulerType type ,
											 double rotA, double rotB, double rotC,
											 @Nullable DMatrixRMaj R ) {
		R = checkDeclare3x3( R );

		DMatrixRMaj R_a = rotationAboutAxis( type.getAxisA(), rotA, null );
		DMatrixRMaj R_b = rotationAboutAxis( type.getAxisB(), rotB, null );
		DMatrixRMaj R_c = rotationAboutAxis( type.getAxisC(), rotC, null );

		DMatrixRMaj A = new DMatrixRMaj( 3, 3 );

		CommonOps_DDRM.mult( R_b, R_a, A );
		CommonOps_DDRM.mult( R_c, A, R );

		return R;
	}

	@SuppressWarnings("DuplicateExpressions")
	public static Quaternion_F64 eulerToQuaternion(EulerType type ,
												   double rotA, double rotB, double rotC,
												   @Nullable Quaternion_F64 q ) {
		if( q == null )
			q = new Quaternion_F64();

		double ca = Math.cos( rotA * 0.5 );
		double sa = Math.sin( rotA * 0.5 );
		double cb = Math.cos( rotB * 0.5 );
		double sb = Math.sin( rotB * 0.5 );
		double cc = Math.cos( rotC * 0.5 );
		double sc = Math.sin( rotC * 0.5 );

		double w=0,x=0,y=0,z=0;
		switch( type ) {
			case ZYX:
				w = ca*cb*cc - sa*sb*sc;
				x = cc*sa*sb + ca*cb*sc;
				y = ca*cc*sb - cb*sa*sc;
				z = cb*cc*sa + ca*sb*sc;
				break;

			case ZYZ:
				w = ca*cb*cc - cb*sa*sc;
				x = cc*sa*sb - ca*sb*sc;
				y = ca*cc*sb + sa*sb*sc;
				z = cb*cc*sa + ca*cb*sc;
				break;

			case ZXY:
				w = ca*cb*cc + sa*sb*sc;
				x = ca*cc*sb + cb*sa*sc;
				y = -cc*sa*sb + ca*cb*sc;
				z = cb*cc*sa - ca*sb*sc;
				break;

			case ZXZ:
				w = ca*cb*cc - cb*sa*sc;
				x = ca*cc*sb + sa*sb*sc;
				y = -cc*sa*sb + ca*sb*sc;
				z = cb*cc*sa + ca*cb*sc;
				break;

			case YXZ:
				w = ca*cb*cc - sa*sb*sc;
				x = ca*cc*sb - cb*sa*sc;
				y = cb*cc*sa + ca*sb*sc;
				z = cc*sa*sb + ca*cb*sc;
				break;

			case YXY:
				w = ca*cb*cc - cb*sa*sc;
				x = ca*cc*sb + sa*sb*sc;
				y = cb*cc*sa + ca*cb*sc;
				z = cc*sa*sb - ca*sb*sc;
				break;

			case YZX:
				w = ca*cb*cc + sa*sb*sc;
				x = -cc*sa*sb + ca*cb*sc;
				y = cb*cc*sa - ca*sb*sc;
				z = ca*cc*sb + cb*sa*sc;
				break;

			case YZY:
				w = ca*cb*cc - cb*sa*sc;
				x = -cc*sa*sb + ca*sb*sc;
				y = cb*cc*sa + ca*cb*sc;
				z = ca*cc*sb + sa*sb*sc;
				break;

			case XYZ:
				w = ca*cb*cc + sa*sb*sc;
				x = cb*cc*sa - ca*sb*sc;
				y = ca*cc*sb + cb*sa*sc;
				z = -cc*sa*sb + ca*cb*sc;
				break;

			case XYX:
				w = ca*cb*cc - cb*sa*sc;
				x = cb*cc*sa + ca*cb*sc;
				y = ca*cc*sb + sa*sb*sc;
				z = -cc*sa*sb + ca*sb*sc;
				break;

			case XZY:
				w = ca*cb*cc - sa*sb*sc;
				x = cb*cc*sa + ca*sb*sc;
				y = cc*sa*sb + ca*cb*sc;
				z = ca*cc*sb - cb*sa*sc;
				break;

			case XZX:
				w = ca*cb*cc - cb*sa*sc;
				x = cb*cc*sa + ca*cb*sc;
				y = cc*sa*sb - ca*sb*sc;
				z = ca*cc*sb + sa*sb*sc;
				break;

		}

		q.setTo(w,x,y,z);
		return q;
	}


	/**
	 * Creates a rotation matrix about the specified axis.
	 *
	 * @param axis  0 = x, 1 = y, 2 = z
	 * @param angle The angle it is rotated by in radians.
	 * @return The 3 by 3 rotation matrix.
	 */
	private static DMatrixRMaj rotationAboutAxis(int axis, double angle, @Nullable DMatrixRMaj R ) {
		switch( axis ) {
			case 0:
				return ConvertRotation3D_F64.rotX( angle, R );

			case 1:
				return ConvertRotation3D_F64.rotY( angle, R );

			case 2:
				return ConvertRotation3D_F64.rotZ( angle, R );

			default:
				throw new IllegalArgumentException( "Unknown which" );
		}
	}

	/**
	 * <p>
	 * Finds a rotation matrix which is the optimal approximation to an arbitrary 3 by 3 matrix. Optimality
	 * is specified by the equation below:<br>
	 * <br>
	 * min ||R-Q||<sup>2</sup><sub>F</sub><br>
	 * R<br>
	 * where R is the rotation matrix and Q is the matrix being approximated.
	 * </p>
	 * <p/>
	 * <p>
	 * The technique used is based on SVD and is described in Appendix C of "A Flexible New Technique for
	 * Camera Calibration" Technical Report, updated 2002.
	 * </p>
	 * <p/>
	 * <p>
	 * Both origin and R can be the same instance.
	 * </p>
	 *
	 * @param orig Input approximate rotation matrix. Not modified.
	 * @param R (Optional) Storage for the approximated rotation matrix. Modified.
	 * @return Rotation matrix
	 */
	public static DMatrixRMaj approximateRotationMatrix( DMatrixRMaj orig, @Nullable DMatrixRMaj R ) {
		R = checkDeclare3x3( R );

		SingularValueDecomposition<DMatrixRMaj> svd =
				DecompositionFactory_DDRM.svd( orig.numRows, orig.numCols ,true,true,false);

		if( !svd.decompose( orig ) )
			throw new RuntimeException( "SVD Failed" );

		CommonOps_DDRM.mult( svd.getU( null,false ), svd.getV( null,true ), R );

		// svd does not guarantee that U anv V have positive determinants.
		double det = CommonOps_DDRM.det( R );

		if( det < 0 )
			CommonOps_DDRM.scale( -1, R );

		return R;
	}

	/**
	 * <p>Converts a unit quaternion into a rotation matrix.</p>
	 *
	 * <p>
	 * Equations is taken from: Paul J. Besl and Neil D. McKay, "A Method for Registration of 3-D Shapes" IEEE
	 * Transactions on Pattern Analysis and Machine Intelligence, Vol 14, No. 2, Feb. 1992
	 * </p>
	 *
	 * @param quat Unit quaternion.
	 * @param R Storage for rotation matrix. If null a new matrix is created. Modified.
	 * @return Rotation matrix
	 */
	public static DMatrixRMaj quaternionToMatrix( Quaternion_F64 quat, @Nullable DMatrixRMaj R ) {
		return quaternionToMatrix(quat.w,quat.x,quat.y,quat.z,R);
	}

	@SuppressWarnings("UnnecessaryLocalVariable")
	public static DMatrixRMaj quaternionToMatrix(double w, double x , double y , double z ,
												 @Nullable DMatrixRMaj R )
	{
		R = checkDeclare3x3( R );

		final double q0 = w;
		final double q1 = x;
		final double q2 = y;
		final double q3 = z;

		R.data[0] = q0*q0 + q1*q1 - q2*q2 - q3*q3; // (0,0)
		R.data[1] = 2.0*( q1*q2 - q0*q3 );         // (0,1)
		R.data[2] = 2.0*( q1*q3 + q0*q2 );         // (0,2)

		R.data[3] = 2.0*( q1*q2 + q0*q3 );         // (1,0)
		R.data[4] = q0*q0 - q1*q1 + q2*q2 - q3*q3; // (1,1)
		R.data[5] = 2.0*( q2*q3 - q0*q1 );         // (1,2)

		R.data[6] = 2.0*( q1*q3 - q0*q2 );         // (2,0)
		R.data[7] = 2.0*( q2*q3 + q0*q1 );         // (2,1)
		R.data[8] = q0*q0 - q1*q1 - q2*q2 + q3*q3; // (2,2)

		return R;
	}

	private static DMatrixRMaj checkDeclare3x3( @Nullable DMatrixRMaj R ) {
		if( R == null ) {
			return new DMatrixRMaj( 3, 3 );
		}
		R.reshape(3,3);
		return R;
	}
}
