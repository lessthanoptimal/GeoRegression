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
import georegression.struct.so.Quaternion_F64;
import georegression.struct.so.Rodrigues_F32;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.UtilEjml;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.SingularValueDecomposition;
import org.ejml.ops.CommonOps;


/**
 * Provides functions to convert between different parameterizations of 3D rotation matrices.
 *
 * @author Peter Abeles
 */
// todo rename to RotationConverter3D_F64 ?
public class RotationMatrixGenerator {

	/**
	 * Converts {@link georegression.struct.so.Rodrigues_F64} into a rotation matrix.
	 *
	 * @param rodrigues rotation defined using rotation axis angle notation.
	 * @param R where the results will be stored.  If null a new matrix is declared/
	 * @return rotation matrix.
	 */
	public static DenseMatrix64F rodriguesToMatrix( Rodrigues_F64 rodrigues, DenseMatrix64F R ) {
		R = checkDeclare3x3( R );

		double x = rodrigues.unitAxisRotation.x;
		double y = rodrigues.unitAxisRotation.y;
		double z = rodrigues.unitAxisRotation.z;

		double c = Math.cos( rodrigues.theta );
		double s = Math.sin( rodrigues.theta );
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
	 * Converts {@link georegression.struct.so.Rodrigues_F32} into a rotation matrix.
	 *
	 * @param rodrigues rotation defined using rotation axis angle notation.
	 * @param R		 where the results will be stored.  If null a new matrix is declared/
	 * @return rotation matrix.
	 */
	public static DenseMatrix64F rodriguesToMatrix( Rodrigues_F32 rodrigues, DenseMatrix64F R ) {
		R = checkDeclare3x3( R );

		double x = rodrigues.unitAxisRotation.x;
		double y = rodrigues.unitAxisRotation.y;
		double z = rodrigues.unitAxisRotation.z;

		double c = Math.cos( rodrigues.theta );
		double s = Math.sin( rodrigues.theta );
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
	 * <p>
	 * Converts {@link Rodrigues_F32} into a unit {@link Quaternion_F32}.
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
	 * <p>
	 * Converts {@link georegression.struct.so.Rodrigues_F64} into a unit {@link georegression.struct.so.Quaternion_F64}.
	 * </p>
	 *
	 * @param rodrigues The angle of rotation around the rotation axis.
	 * @param quat Storage for quaternion coordinate.  If null a new quaternion is created. Modified.
	 * @return unit quaternion coordinate.
	 */
	public static Quaternion_F64 rodriguesToQuaternion( Rodrigues_F64 rodrigues,
														Quaternion_F64 quat ) {
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
	 * Converts unit {@link Quaternion_F32} into {@link Rodrigues_F32}.
	 * @param quat (Input) Unit quaternion
	 * @param rodrigues (Optional) Storage for rodrigues coodinate.  If null a new instance is created.
	 * @return rodrigues
	 */
	public static Rodrigues_F32 quaternionToRodrigues( Quaternion_F32 quat,
													   Rodrigues_F32 rodrigues ) {
		if( rodrigues == null )
			rodrigues = new Rodrigues_F32();

		rodrigues.unitAxisRotation.set(quat.x, quat.y, quat.z);
		rodrigues.unitAxisRotation.normalize();

		rodrigues.theta = (float)(2.0 * Math.acos( quat.w));

		return rodrigues;
	}

	/**
	 * Converts a unit {@link Quaternion_F64} into {@link Rodrigues_F64}.
	 * @param quat (Input) Unit quaternion
	 * @param rodrigues (Optional) Storage for rodrigues coodinate.  If null a new instance is created.
	 * @return rodrigues
	 */
	public static Rodrigues_F64 quaternionToRodrigues( Quaternion_F64 quat,
													   Rodrigues_F64 rodrigues ) {
		if( rodrigues == null )
			rodrigues = new Rodrigues_F64();

		rodrigues.unitAxisRotation.set( quat.x, quat.y, quat.z);
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
	public static double[] quaternionToEuler(Quaternion_F64 q , EulerType type , double []euler )
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
	public static double[] matrixToEuler(DenseMatrix64F R , EulerType type , double[] euler ) {
		if( euler == null )
			euler = new double[3];

		switch(type){
			case ZYX:
				TanSinTan(-2,1,  3,  -6,9,  5,-7,4,8, R,euler);//4,5,7,8
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
				TanSinTan(-7,9,  8, -2,5,  1,-6,3,4,  R,euler);
				break;

			case YXY:
				TanCosTan(4,-6,  5,  2,8,  1,-6,3,4,  R,euler);
				break;

			case YZX:
				TanSinTan(3,1,  -2,  8,5,  9,4,-7,6,  R,euler);
				break;

			case YZY:
				TanCosTan(6,4,   5,  8,-2, 9,4,-7,6,  R,euler);
				break;

			case XYZ:
				TanSinTan(8,9,  -7,  4,1,  5,3,-6,2,  R,euler);
				break;

			case XYX:
				TanCosTan(2,3,   1,  4,-7, 5,3,-6,2,  R,euler);
				break;

			case XZY:
				TanSinTan(-6,5,  4, -7,1,  9,-2,8,3,  R,euler);
				break;

			case XZX:
				TanCosTan(3,-2,  1,  7,4,  9,-2,8,3,  R,euler);
				break;

			default:
				throw new IllegalArgumentException("Unknown rotation sequence");
		}

		return euler;
	}

	private static void TanSinTan( int y0 , int x0 , int sin1 , int y2 , int x2 ,
								   int cos0a , int cos0b , int sin0a , int sin0b,
								   DenseMatrix64F R , double euler[] ) {

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
			euler[1] = sign*Math.PI/2.0;
			euler[2] = 0;
		} else {
			euler[0] = Math.atan2(val_y0,val_x0);
			euler[1] = Math.asin(val_sin1);
			euler[2] = Math.atan2(val_y2,val_x2);
		}
	}

	private static void TanCosTan( int y0 , int x0 , int cos1 , int y2 , int x2 ,
								   int cos0a , int cos0b , int sin0a , int sin0b,
								  DenseMatrix64F R , double euler[] ) {

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
	 * If the index is negative it returns the negative of the value at -index.  Starts at 0
	 */
	private static double get( DenseMatrix64F M , int index ) {
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
	 * @param quat (Output) Optional storage for quaternion.  If null a new class will be declared.
	 * @return unit quaternion representation of the rotation matrix.
	 */
	public static Quaternion_F64 matrixToQuaternion( DenseMatrix64F R, Quaternion_F64 quat ) {
		// first get rodrigues
		Rodrigues_F64 r = matrixToRodrigues( R, (Rodrigues_F64)null );

		// then convert to quaternions
		return rodriguesToQuaternion( r, quat );
	}

	/**
	 * Converts a rotation matrix into {@link georegression.struct.so.Rodrigues_F64}.
	 *
	 * @param R		 Rotation matrix.
	 * @param rodrigues Storage used for solution.  If null a new instance is declared.
	 * @return The found axis and rotation angle.
	 */
	public static Rodrigues_F64 matrixToRodrigues( DenseMatrix64F R, Rodrigues_F64 rodrigues ) {
		if( rodrigues == null ) {
			rodrigues = new Rodrigues_F64();
		}
		// parts of this are from wikipedia
		// http://en.wikipedia.org/wiki/Rotation_representation_%28mathematics%29#Rotation_matrix_.E2.86.94_Euler_axis.2Fangle

		double diagSum = ( R.get( 0, 0 ) + R.get( 1, 1 ) + R.get( 2, 2 ) - 1.0 ) / 2.0;

		double absDiagSum = Math.abs(diagSum);
		
		if( absDiagSum <= 1.0 && 1.0-absDiagSum > 10.0*GrlConstants.EPS ) {
			// if numerically stable use a faster technique
			rodrigues.theta = Math.acos(diagSum);
			double bottom = 2.0 * Math.sin(rodrigues.theta);

			// in cases where bottom is close to zero that means theta is also close to zero and the vector
			// doesn't matter that much
			rodrigues.unitAxisRotation.x = (R.get(2, 1) - R.get(1, 2)) / bottom;
			rodrigues.unitAxisRotation.y = (R.get(0, 2) - R.get(2, 0)) / bottom;
			rodrigues.unitAxisRotation.z = (R.get(1, 0) - R.get(0, 1)) / bottom;

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
	 * Converts a rotation matrix into {@link georegression.struct.so.Rodrigues_F64}.
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
		// http://en.wikipedia.org/wiki/Rotation_representation_%28mathematics%29#Rotation_matrix_.E2.86.94_Euler_axis.2Fangle

		double diagSum = ( R.get( 0, 0 ) + R.get( 1, 1 ) + R.get( 2, 2 ) - 1.0 ) / 2.0;

		double absDiagSum = Math.abs(diagSum);

		if( absDiagSum <= 1.0 && 1.0-absDiagSum > 5.0*GrlConstants.EPS ) {
			// if numerically stable use a faster technique
			rodrigues.theta = (float)Math.acos( diagSum );
			double bottom = 2.0 * Math.sin( rodrigues.theta );

			// in cases where bottom is close to zero that means theta is also close to zero and the vector
			// doesn't matter that much
			rodrigues.unitAxisRotation.x = (float)(( R.get( 2, 1 ) - R.get( 1, 2 ) ) / bottom);
			rodrigues.unitAxisRotation.y = (float)(( R.get( 0, 2 ) - R.get( 2, 0 ) ) / bottom);
			rodrigues.unitAxisRotation.z = (float)(( R.get( 1, 0 ) - R.get( 0, 1 ) ) / bottom);

			// in extreme underflow situations the result can be unnormalized
			rodrigues.unitAxisRotation.normalize();

			// In theory this might be more stable
			// rotationAxis( R, rodrigues.unitAxisRotation);
		} else {
			// this handles the special case where the bottom is very very small or equal to zero
			if( diagSum >= 1.0 )
				rodrigues.theta = 0;
			else if( diagSum <= -1.0 )
				rodrigues.theta = GrlConstants.F_PI;
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
	public static DenseMatrix64F rotX( double ang, DenseMatrix64F R ) {
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
	public static void setRotX( double ang, DenseMatrix64F R ) {
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
	 * @param R (Output) Optional storage for rotation matrix.  Modified.
	 * @return The 3 by 3 rotation matrix.
	 */
	public static DenseMatrix64F rotY( double ang, DenseMatrix64F R ) {
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
	public static void setRotY( double ang, DenseMatrix64F r ) {
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
	 * @param R (Output) Optional storage for rotation matrix.  Modified.
	 * @return The 3 by 3 rotation matrix.
	 */
	public static DenseMatrix64F rotZ( double ang, DenseMatrix64F R ) {
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
	public static void setRotZ( double ang, DenseMatrix64F r ) {
		double c = Math.cos( ang );
		double s = Math.sin( ang );

		r.set( 0, 0, c );
		r.set( 0, 1, -s );
		r.set( 1, 0, s );
		r.set( 1, 1, c );
		r.set( 2, 2, 1 );
	}

	/**
	 * Converts an Euler coordinate into a rotation matrix.  Different type of Euler coordinates are accepted.
	 * @param type Which Euler coordinate is the input in
	 * @param rotA Angle of rotation around axis A
	 * @param rotB Angle of rotation around axis B
	 * @param rotC Angle of rotation around axis C
	 * @param R (Output) Optional storage for output rotation matrix
	 * @return Rotation matrix
	 */
	public static DenseMatrix64F eulerToMatrix( EulerType type ,
												double rotA, double rotB, double rotC,
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

	/**
	 * Creates a rotation matrix about the specified axis.
	 *
	 * @param axis  0 = x, 1 = y, 2 = z
	 * @param angle The angle it is rotated by in radians.
	 * @return The 3 by 3 rotation matrix.
	 */
	private static DenseMatrix64F rotationAboutAxis(int axis, double angle, DenseMatrix64F R ) {
		switch( axis ) {
			case 0:
				return RotationMatrixGenerator.rotX( angle, R );

			case 1:
				return RotationMatrixGenerator.rotY( angle, R );

			case 2:
				return RotationMatrixGenerator.rotZ( angle, R );

			default:
				throw new IllegalArgumentException( "Unknown which" );
		}
	}

	/**
	 * <p>
	 * Given a rotation matrix it will compute the XYZ euler angles.
	 * </p>
	 * <p>
	 * See Internet PDF "Computing Euler angles from a rotation matrix" by Gregory G. Slabaugh.
	 * </p>
	 */
	public static double[] matrixToEulerXYZ( DenseMatrix64F M , double euler[] ) {
		if( euler == null )
			euler = new double[3];

		double m31 = M.get( 2, 0 );

		double rotX, rotY, rotZ;


		if( Math.abs( Math.abs( m31 ) - 1 ) < UtilEjml.EPS ) {
			double m12 = M.get( 0, 1 );
			double m13 = M.get( 0, 2 );

			rotZ = 0;
			double gamma = Math.atan2( m12, m13 );

			if( m31 < 0 ) {
				rotY = Math.PI / 2.0;
				rotX = rotZ + gamma;
			} else {
				rotY = -Math.PI / 2.0;
				rotX = -rotZ + gamma;
			}
		} else {
			double m32 = M.get( 2, 1 );
			double m33 = M.get( 2, 2 );

			double m21 = M.get( 1, 0 );
			double m11 = M.get( 0, 0 );

			rotY = -Math.asin( m31 );
			double cosRotY = Math.cos( rotY );
			rotX = Math.atan2( m32 / cosRotY, m33 / cosRotY );
			rotZ = Math.atan2( m21 / cosRotY, m11 / cosRotY );

		}

		euler[0] = rotX;
		euler[1] = rotY;
		euler[2] = rotZ;

		return euler;
	}

	/**
	 * <p>
	 * Given a rotation matrix it will compute the XYZ euler angles.
	 * </p>
	 * <p>
	 * See Internet PDF "Computing Euler angles from a rotation matrix" by Gregory G. Slabaugh.
	 * </p>
	 */
	public static float[] matrixToEulerXYZ( DenseMatrix64F M , float euler[] ) {
		if( euler == null )
			euler = new float[3];

		double m31 = M.get( 2, 0 );

		double rotX, rotY, rotZ;


		if( Math.abs( Math.abs( m31 ) - 1 ) < GrlConstants.EPS) {
			double m12 = M.get( 0, 1 );
			double m13 = M.get( 0, 2 );

			rotZ = 0;
			double gamma = Math.atan2( m12, m13 );

			if( m31 < 0 ) {
				rotY = Math.PI / 2.0;
				rotX = rotZ + gamma;
			} else {
				rotY = -Math.PI / 2.0;
				rotX = -rotZ + gamma;
			}
		} else {
			double m32 = M.get( 2, 1 );
			double m33 = M.get( 2, 2 );

			double m21 = M.get( 1, 0 );
			double m11 = M.get( 0, 0 );

			rotY = -Math.asin( m31 );
			double cosRotY = Math.cos( rotY );
			rotX = Math.atan2( m32 / cosRotY, m33 / cosRotY );
			rotZ = Math.atan2( m21 / cosRotY, m11 / cosRotY );

		}

		euler[0] = (float)rotX;
		euler[1] = (float)rotY;
		euler[2] = (float)rotZ;

		return euler;
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
		double det = CommonOps.det( R );

		if( det < 0 )
			CommonOps.scale( -1, R );

		return R;
	}

	/**
	 * Converts euler (Z,Y,X) a.k.a. (yaw,roll,pitch) rotation coordinates into quaternions.
	 *
	 * @param rotZ rotation about z-axis. yaw
	 * @param rotY rotation about y-axis. roll
	 * @param rotX rotation about x-axis. pitch
	 * @param quat Output Quaternion
	 */
	public static void eulerZyxToQuaternion(double rotZ , double rotY , double rotX, Quaternion_F64 quat ) {

		// See: http://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles
		double cy = Math.cos( rotZ * 0.5 );
		double sy = Math.sin( rotZ * 0.5 );
		double cp = Math.cos( rotX * 0.5 );
		double sp = Math.sin( rotX * 0.5 );
		double cr = Math.cos( rotY * 0.5 );
		double sr = Math.sin( rotY * 0.5 );

		double ccc = cr * cp * cy;
		double ccs = cr * cp * sy;
		double css = cr * sp * sy;
		double sss = sr * sp * sy;
		double scc = sr * cp * cy;
		double ssc = sr * sp * cy;
		double csc = cr * sp * cy;
		double scs = sr * cp * sy;

		quat.w = ccc + sss;
		quat.x = scc - css;
		quat.y = csc + scs;
		quat.z = ccs - ssc;
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
	public static DenseMatrix64F quaternionToMatrix( Quaternion_F64 quat, DenseMatrix64F R ) {
		R = checkDeclare3x3( R );

		final double q0 = quat.w;
		final double q1 = quat.x;
		final double q2 = quat.y;
		final double q3 = quat.z;

		R.set( 0, 0, q0 * q0 + q1 * q1 - q2 * q2 - q3 * q3 );
		R.set( 0, 1, 2.0 * ( q1 * q2 - q0 * q3 ) );
		R.set( 0, 2, 2.0 * ( q1 * q3 + q0 * q2 ) );

		R.set( 1, 0, 2.0 * ( q1 * q2 + q0 * q3 ) );
		R.set( 1, 1, q0 * q0 - q1 * q1 + q2 * q2 - q3 * q3 );
		R.set( 1, 2, 2.0 * ( q2 * q3 - q0 * q1 ) );

		R.set( 2, 0, 2.0 * ( q1 * q3 - q0 * q2 ) );
		R.set( 2, 1, 2.0 * ( q2 * q3 + q0 * q1 ) );
		R.set( 2, 2, q0 * q0 - q1 * q1 - q2 * q2 + q3 * q3 );

		return R;
	}

	/**
	 * Converts a quaternion into a rotation matrix.
	 * <p>
	 * Equations is taken from: Paul J. Besl and Neil D. McKay, "A Method for Registration of 3-D Shapes" IEEE
	 * Transactions on Pattern Analysis and Machine Intelligence, Vol 14, No. 2, Feb. 1992
	 * </p>
	 *
	 * @param quat unit quaternion.
	 * @param R Storage for rotation matrix.  If null a new matrix is created. Modified.
	 */
	public static DenseMatrix64F quaternionToMatrix( Quaternion_F32 quat, DenseMatrix64F R ) {
		R = checkDeclare3x3( R );

		final double q0 = quat.w;
		final double q1 = quat.x;
		final double q2 = quat.y;
		final double q3 = quat.z;

		R.set( 0, 0, q0 * q0 + q1 * q1 - q2 * q2 - q3 * q3 );
		R.set( 0, 1, 2.0 * ( q1 * q2 - q0 * q3 ) );
		R.set( 0, 2, 2.0 * ( q1 * q3 + q0 * q2 ) );

		R.set( 1, 0, 2.0 * ( q1 * q2 + q0 * q3 ) );
		R.set( 1, 1, q0 * q0 - q1 * q1 + q2 * q2 - q3 * q3 );
		R.set( 1, 2, 2.0 * ( q2 * q3 - q0 * q1 ) );

		R.set( 2, 0, 2.0 * ( q1 * q3 - q0 * q2 ) );
		R.set( 2, 1, 2.0 * ( q2 * q3 + q0 * q1 ) );
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
