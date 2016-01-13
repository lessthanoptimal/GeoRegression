/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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
import georegression.struct.so.Quaternion_F32;
import georegression.struct.so.Rodrigues_F32;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.SingularValueDecomposition;
import org.ejml.ops.CommonOps;


/**
 * Provides functions to convert between different parameterizations of 3D rotation matrices.
 *
 * @author Peter Abeles
 */
public class ConvertRotation3D_F32 {

	/**
	 * Converts {@link georegression.struct.so.Rodrigues_F32} into a rotation matrix.
	 *
	 * @param rodrigues rotation defined using rotation axis angle notation.
	 * @param R where the results will be stored.  If null a new matrix is declared/
	 * @return rotation matrix.
	 */
	public static DenseMatrix64F rodriguesToMatrix( Rodrigues_F32 rodrigues, DenseMatrix64F R ) {
		R = checkDeclare3x3( R );

		float x = rodrigues.unitAxisRotation.x;
		float y = rodrigues.unitAxisRotation.y;
		float z = rodrigues.unitAxisRotation.z;

		float c = (float)Math.cos( rodrigues.theta );
		float s = (float)Math.sin( rodrigues.theta );
		float oc = 1.0f - c;

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
	 * <p>Converts{@link georegression.struct.so.Rodrigues_F32} into an euler rotation of different types</p>
	 *
	 * @param rodrigues rotation defined using rotation axis angle notation.
	 * @param type Type of Euler rotation
	 * @param euler (Output) Optional storage for Euler rotation
	 * @return The Euler rotation.
	 */
	public static float[] rodriguesToEuler(Rodrigues_F32 rodrigues , EulerType type , float []euler )
	{
		DenseMatrix64F R = rodriguesToMatrix(rodrigues,null);
		return matrixToEuler(R,type,euler);
	}

	/**
	 * <p>
	 * Converts {@link georegression.struct.so.Rodrigues_F32} into a unit {@link georegression.struct.so.Quaternion_F32}.
	 * </p>
	 *
	 * @param rodrigues The angle of rotation around the rotation axis.
	 * @param quat Storage for quaternion coordinate.  If null a new quaternion is created. Modified.
	 * @return unit quaternion coordinate.
	 */
	public static Quaternion_F32 rodriguesToQuaternion( Rodrigues_F32 rodrigues,
														Quaternion_F32 quat ) {
		if( quat == null )
			quat = new Quaternion_F32();

		quat.w = (float)Math.cos( rodrigues.theta / 2.0f );
		float s = (float)Math.sin( rodrigues.theta / 2.0f );

		quat.x = rodrigues.unitAxisRotation.x * s;
		quat.y = rodrigues.unitAxisRotation.y * s;
		quat.z = rodrigues.unitAxisRotation.z * s;

		return quat;
	}

	/**
	 * Converts a unit {@link Quaternion_F32} into {@link Rodrigues_F32}.
	 * @param quat (Input) Unit quaternion
	 * @param rodrigues (Optional) Storage for rodrigues coodinate.  If null a new instance is created.
	 * @return rodrigues
	 */
	public static Rodrigues_F32 quaternionToRodrigues( Quaternion_F32 quat,
													   Rodrigues_F32 rodrigues ) {
		if( rodrigues == null )
			rodrigues = new Rodrigues_F32();

		rodrigues.unitAxisRotation.set( quat.x, quat.y, quat.z);
		rodrigues.unitAxisRotation.normalize();

		rodrigues.theta = 2.0f * (float)Math.acos( quat.w);

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
	public static float[] quaternionToEuler(Quaternion_F32 q , EulerType type , float []euler )
	{
		DenseMatrix64F R = quaternionToMatrix(q,null);
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
	public static float[] matrixToEuler(DenseMatrix64F R , EulerType type , float[] euler ) {
		if( euler == null )
			euler = new float[3];

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
								   DenseMatrix64F R , float euler[] ) {

		float val_y0 = get(R,y0);
		float val_x0 = get(R,x0);
		float val_sin1 = get(R,sin1);
		float val_y2 = get(R,y2);
		float val_x2 = get(R,x2);

		if( 1.0f-Math.abs(val_sin1) <= GrlConstants.F_EPS ) {

			float sign = (float)Math.signum(val_sin1);

			float sin0 = (get(R,sin0a)+sign*get(R,sin0b))/2.0f;
			float cos0 = (get(R,cos0a)+sign*get(R,cos0b))/2.0f;

			euler[0] = (float)Math.atan2(sin0,cos0);
			euler[1] = sign * (float)Math.PI/2.0f;
			euler[2] = 0;
		} else {
			euler[0] = (float)Math.atan2(val_y0,val_x0);
			euler[1] = (float)Math.asin(val_sin1);
			euler[2] = (float)Math.atan2(val_y2,val_x2);
		}
	}

	private static void TanCosTan( int y0 , int x0 , int cos1 , int y2 , int x2 ,
								   int cos0a , int cos0b , int sin0a , int sin0b,
								  DenseMatrix64F R , float euler[] ) {

		float val_y0 = get(R,y0);
		float val_x0 = get(R,x0);
		float val_cos1 = get(R,cos1);
		float val_y2 = get(R,y2);
		float val_x2 = get(R,x2);

		if( 1.0f-Math.abs(val_cos1) <= GrlConstants.F_EPS ) {
			float sin0 = (get(R,sin0a)+get(R,sin0b))/2.0f;
			float cos0 = (get(R,cos0a)+get(R,cos0b))/2.0f;

			euler[0] = (float)Math.atan2(sin0,cos0);
			euler[1] = 0;
			euler[2] = 0;
		} else {
			euler[0] = (float)Math.atan2(val_y0,val_x0);
			euler[1] = (float)Math.acos(val_cos1);
			euler[2] = (float)Math.atan2(val_y2,val_x2);
		}
	}

	/**
	 * If the index is negative it returns the negative of the value at -index.  Starts at 0
	 */
	private static float get( DenseMatrix64F M , int index ) {
		if( index < 0 ) {
			return (float)-M.data[-index-1];
		} else {
			return (float)M.data[index-1];
		}
	}

	/**
	 * Extracts quaternions from the provided rotation matrix.
	 *
	 * @param R (Input) rotation matrix
	 * @param quat (Output) Optional storage for quaternion.  If null a new class will be used.
	 * @return unit quaternion representation of the rotation matrix.
	 */
	public static Quaternion_F32 matrixToQuaternion( DenseMatrix64F R, Quaternion_F32 quat ) {

		if( quat == null )
			quat = new Quaternion_F32();

		// algorithm from:
		// http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/
		//
		// Designed to minimize numerical error by not dividing by very small numbers

		float m00 = (float)R.unsafe_get(0,0);
		float m01 = (float)R.unsafe_get(0,1);
		float m02 = (float)R.unsafe_get(0,2);
		float m10 = (float)R.unsafe_get(1,0);
		float m11 = (float)R.unsafe_get(1,1);
		float m12 = (float)R.unsafe_get(1,2);
		float m20 = (float)R.unsafe_get(2,0);
		float m21 = (float)R.unsafe_get(2,1);
		float m22 = (float)R.unsafe_get(2,2);

		float trace = m00 + m11 + m22;

		if (trace > 0) {
			float S = (float)Math.sqrt(trace+1.0f) * 2; // S=4*qw
			quat.w = 0.25f * S;
			quat.x = (m21 - m12) / S;
			quat.y = (m02 - m20) / S;
			quat.z = (m10 - m01) / S;
		} else if ((m00 > m11)&(m00 > m22)) {
			float S = (float)Math.sqrt(1.0f + m00 - m11 - m22) * 2; // S=4*qx
			quat.w = (m21 - m12) / S;
			quat.x = 0.25f * S;
			quat.y = (m01 + m10) / S;
			quat.z = (m02 + m20) / S;
		} else if (m11 > m22) {
			float S = (float)Math.sqrt(1.0f + m11 - m00 - m22) * 2; // S=4*qy
			quat.w = (m02 - m20) / S;
			quat.x = (m01 + m10) / S;
			quat.y = 0.25f * S;
			quat.z = (m12 + m21) / S;
		} else {
			float S = (float)Math.sqrt(1.0f + m22 - m00 - m11) * 2; // S=4*qz
			quat.w = (m10 - m01) / S;
			quat.x = (m02 + m20) / S;
			quat.y = (m12 + m21) / S;
			quat.z = 0.25f * S;
		}

		return quat;
	}

	/**
	 * Converts a rotation matrix into {@link georegression.struct.so.Rodrigues_F32}.
	 *
	 * @param R		 Rotation matrix.
	 * @param rodrigues Storage used for solution.  If null a new instance is declared.
	 * @return The found axis and rotation angle.
	 */
	public static Rodrigues_F32 matrixToRodrigues( DenseMatrix64F R, Rodrigues_F32 rodrigues ) {
		if( rodrigues == null ) {
			rodrigues = new Rodrigues_F32();
		}
		// parts of this are from wikipedia
		// http://en.wikipedia.org/wiki/Rotation_representation_%28mathematics%29#Rotation_matrix_.E2.86f.94_Euler_axis.2Fangle

		float diagSum = ( (float)(R.unsafe_get( 0, 0 ) + R.unsafe_get( 1, 1 ) + R.unsafe_get( 2, 2 )) - 1.0f ) / 2.0f;

		float absDiagSum = (float)Math.abs(diagSum);
		
		if( absDiagSum <= 1.0f && 1.0f-absDiagSum > 10.0f*GrlConstants.F_EPS ) {
			// if numerically stable use a faster technique
			rodrigues.theta = (float)Math.acos(diagSum);
			float bottom = 2.0f * (float)Math.sin(rodrigues.theta);

			// in cases where bottom is close to zero that means theta is also close to zero and the vector
			// doesn't matter that much
			rodrigues.unitAxisRotation.x = (float)(R.unsafe_get(2, 1) - R.unsafe_get(1, 2)) / bottom;
			rodrigues.unitAxisRotation.y = (float)(R.unsafe_get(0, 2) - R.unsafe_get(2, 0)) / bottom;
			rodrigues.unitAxisRotation.z = (float)(R.unsafe_get(1, 0) - R.unsafe_get(0, 1)) / bottom;

			// in extreme underflow situations the result can be unnormalized
			rodrigues.unitAxisRotation.normalize();

			// In theory this might be more stable
			// rotationAxis( R, rodrigues.unitAxisRotation);
		} else {

			// this handles the special case where the bottom is very very small or equal to zero
			if( diagSum >= 1.0f )
				rodrigues.theta = 0;
			else if( diagSum <= -1.0f )
				rodrigues.theta = (float)Math.PI;
			else
				rodrigues.theta = (float)Math.acos(diagSum);

			// compute the value of x,y,z up to a sign ambiguity
			rodrigues.unitAxisRotation.x = (float)Math.sqrt((R.get(0, 0) + 1) / 2);
			rodrigues.unitAxisRotation.y = (float)Math.sqrt((R.get(1, 1) + 1) / 2);
			rodrigues.unitAxisRotation.z = (float)Math.sqrt((R.get(2, 2) + 1) / 2);

			float x = rodrigues.unitAxisRotation.x;
			float y = rodrigues.unitAxisRotation.y;
			float z = rodrigues.unitAxisRotation.z;

			if (Math.abs(R.get(1, 0) - 2 * x * y) > GrlConstants.F_EPS) {
				x *= -1;
			}
			if (Math.abs(R.get(2, 0) - 2 * x * z) > GrlConstants.F_EPS) {
				z *= -1;
			}
			if (Math.abs(R.get(2,1) - 2 * z * y) > GrlConstants.F_EPS) {
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
	 * @param R (Output) Optional storage for rotation matrix.  Modified.
	 * @return The 3 by 3 rotation matrix.
	 */
	public static DenseMatrix64F rotX( float ang, DenseMatrix64F R ) {
		if( R == null )
			R = new DenseMatrix64F( 3, 3 );

		setRotX( ang, R );

		return R;
	}

	/**
	 * Sets the values in the specified matrix to a rotation matrix about the x-axis.
	 *
	 * @param ang the angle it rotates a point by in radians.
	 * @param R (Output) Storage for rotation matrix.  Modified.
	 */
	public static void setRotX( float ang, DenseMatrix64F R ) {
		float c = (float)Math.cos( ang );
		float s = (float)Math.sin( ang );

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
	 * @param R (Output) Optional storage for rotation matrix.  Modified.
	 * @return The 3 by 3 rotation matrix.
	 */
	public static DenseMatrix64F rotY( float ang, DenseMatrix64F R ) {
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
	public static void setRotY( float ang, DenseMatrix64F r ) {
		float c = (float)Math.cos( ang );
		float s = (float)Math.sin( ang );

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
	 * @param R (Output) Optional storage for rotation matrix.  Modified.
	 * @return The 3 by 3 rotation matrix.
	 */
	public static DenseMatrix64F rotZ( float ang, DenseMatrix64F R ) {
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
	public static void setRotZ( float ang, DenseMatrix64F r ) {
		float c = (float)Math.cos( ang );
		float s = (float)Math.sin( ang );

		r.set( 0, 0, c );
		r.set( 0, 1, -s );
		r.set( 1, 0, s );
		r.set( 1, 1, c );
		r.set( 2, 2, 1 );
	}

	/**
	 * Converts an Euler coordinate into a rotation matrix.  Different type of Euler coordinates are accepted.
	 * @param type Which Euler coordinate is the input in
	 * @param rotA Angle of rotation around axis A.  First rotation
	 * @param rotB Angle of rotation around axis B   Second rotation
	 * @param rotC Angle of rotation around axis C   Third rotation
	 * @param R (Output) Optional storage for output rotation matrix
	 * @return Rotation matrix
	 */
	public static DenseMatrix64F eulerToMatrix( EulerType type ,
												float rotA, float rotB, float rotC,
												DenseMatrix64F R ) {
		R = checkDeclare3x3( R );

		DenseMatrix64F R_a = rotationAboutAxis( type.getAxisA(), rotA, null );
		DenseMatrix64F R_b = rotationAboutAxis( type.getAxisB(), rotB, null );
		DenseMatrix64F R_c = rotationAboutAxis( type.getAxisC(), rotC, null );

		DenseMatrix64F A = new DenseMatrix64F( 3, 3 );

		CommonOps.mult( R_b, R_a, A );
		CommonOps.mult( R_c, A, R );

		return R;
	}

	public static Quaternion_F32 eulerToQuaternion( EulerType type ,
													float rotA, float rotB, float rotC,
													Quaternion_F32 q ) {
		if( q == null )
			q = new Quaternion_F32();

		float ca = (float)Math.cos( rotA * 0.5f );
		float sa = (float)Math.sin( rotA * 0.5f );
		float cb = (float)Math.cos( rotB * 0.5f );
		float sb = (float)Math.sin( rotB * 0.5f );
		float cc = (float)Math.cos( rotC * 0.5f );
		float sc = (float)Math.sin( rotC * 0.5f );

		float w=0,x=0,y=0,z=0;
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

		q.set(w,x,y,z);
		return q;
	}


	/**
	 * Creates a rotation matrix about the specified axis.
	 *
	 * @param axis  0 = x, 1 = y, 2 = z
	 * @param angle The angle it is rotated by in radians.
	 * @return The 3 by 3 rotation matrix.
	 */
	private static DenseMatrix64F rotationAboutAxis(int axis, float angle, DenseMatrix64F R ) {
		switch( axis ) {
			case 0:
				return ConvertRotation3D_F32.rotX( angle, R );

			case 1:
				return ConvertRotation3D_F32.rotY( angle, R );

			case 2:
				return ConvertRotation3D_F32.rotZ( angle, R );

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
	 * @param orig Input approximate rotation matrix.  Not modified.
	 * @param R	The resulting approximated rotation matrix.  Modified.
	 */
	public static DenseMatrix64F approximateRotationMatrix( DenseMatrix64F orig, DenseMatrix64F R ) {
		R = checkDeclare3x3( R );

		SingularValueDecomposition<DenseMatrix64F> svd =
				DecompositionFactory.svd( orig.numRows, orig.numCols ,true,true,false);

		if( !svd.decompose( orig ) )
			throw new RuntimeException( "SVD Failed" );

		CommonOps.mult( svd.getU( null,false ), svd.getV( null,true ), R );

		// svd does not guarantee that U anv V have positive determinants.
		float det = (float)CommonOps.det( R );

		if( det < 0 )
			CommonOps.scale( -1, R );

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
	 * @param R Storage for rotation matrix.  If null a new matrix is created. Modified.
	 */
	public static DenseMatrix64F quaternionToMatrix( Quaternion_F32 quat, DenseMatrix64F R ) {
		R = checkDeclare3x3( R );

		final float q0 = quat.w;
		final float q1 = quat.x;
		final float q2 = quat.y;
		final float q3 = quat.z;

		R.set( 0, 0, q0 * q0 + q1 * q1 - q2 * q2 - q3 * q3 );
		R.set( 0, 1, 2.0f * ( q1 * q2 - q0 * q3 ) );
		R.set( 0, 2, 2.0f * ( q1 * q3 + q0 * q2 ) );

		R.set( 1, 0, 2.0f * ( q1 * q2 + q0 * q3 ) );
		R.set( 1, 1, q0 * q0 - q1 * q1 + q2 * q2 - q3 * q3 );
		R.set( 1, 2, 2.0f * ( q2 * q3 - q0 * q1 ) );

		R.set( 2, 0, 2.0f * ( q1 * q3 - q0 * q2 ) );
		R.set( 2, 1, 2.0f * ( q2 * q3 + q0 * q1 ) );
		R.set( 2, 2, q0 * q0 - q1 * q1 - q2 * q2 + q3 * q3 );

		return R;
	}

	private static DenseMatrix64F checkDeclare3x3( DenseMatrix64F R ) {
		if( R == null ) {
			R = new DenseMatrix64F( 3, 3 );
		} else {
			if( R.numRows != 3 || R.numCols != 3 )
				throw new IllegalArgumentException( "Expected 3 by 3 matrix." );
		}
		return R;
	}
}
