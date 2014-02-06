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

package georegression.geometry;

import georegression.struct.point.Vector3D_F64;
import georegression.struct.so.Quaternion_F32;
import georegression.struct.so.Quaternion_F64;
import georegression.struct.so.Rodrigues_F32;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.UtilEjml;
import org.ejml.data.Complex64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;
import org.ejml.interfaces.decomposition.SingularValueDecomposition;
import org.ejml.ops.CommonOps;


/**
 * @author Peter Abeles
 */
// todo rename to Rotation3D ?
// TODO return a specialized matrix instead?
public class RotationMatrixGenerator {

	/**
	 * Converts {@link georegression.struct.so.Rodrigues_F64} into a rotation matrix.
	 *
	 * @param rodrigues rotation defined using rotation axis angle notation.
	 * @param R		 where the results will be stored.  If null a new matrix is declared/
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
	 * Converts {@link georegression.struct.so.Rodrigues_F64} into {@link georegression.struct.so.Quaternion_F64}.
	 * </p>
	 *
	 * @param rodrigues The angle of rotation around the rotation axis.
	 * @param quat	  Storage for quaternion coordinate.  If null a new quaternion is created. Modified.
	 * @return quaternion coordinate.
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
	 * Converts {@link Quaternion_F64} into {@link Rodrigues_F64}.
	 * @param quat (Input) quaternion
	 * @param rodrigues (Optional) Storage for rodrigues coodinate.  If null a new instance is created.
	 * @return rodrigues
	 */
	public static Quaternion_F64 quaternionToRodrigues( Quaternion_F64 quat,
														Rodrigues_F64 rodrigues ) {
		if( rodrigues == null )
			rodrigues = new Rodrigues_F64();

		rodrigues.unitAxisRotation.set( quat.x, quat.y, quat.z);
		rodrigues.unitAxisRotation.normalize();

		rodrigues.theta = 2.0 * Math.acos( quat.w);

		return quat;
	}

	/**
	 * Extracts quaternions from the provided rotation matrix.
	 *
	 * @param R	rotation matrix
	 * @param quat storage for quaternion.  If null a new class will be declared.
	 * @return quaternion representation of the rotation matrix.
	 */
	public static Quaternion_F64 matrixToQuaternion( DenseMatrix64F R, Quaternion_F64 quat ) {
		// first get rodrigues
		Rodrigues_F64 r = matrixToRodrigues( R, null );

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
		
		if( absDiagSum < 1 ) {
			// if numerically stable use a faster technique
			rodrigues.theta = Math.acos( diagSum );
			double bottom = 2.0 * Math.sin( rodrigues.theta );

			// in cases where bottom is close to zero that means theta is also close to zero and the vector
			// doesn't matter that much
			rodrigues.unitAxisRotation.x = ( R.get( 2, 1 ) - R.get( 1, 2 ) ) / bottom;
			rodrigues.unitAxisRotation.y = ( R.get( 0, 2 ) - R.get( 2, 0 ) ) / bottom;
			rodrigues.unitAxisRotation.z = ( R.get( 1, 0 ) - R.get( 0, 1 ) ) / bottom;

			// in extreme underflow situations the result can be unnormalized
			rodrigues.unitAxisRotation.normalize();

			// In theory this might be more stable
			// rotationAxis( R, rodrigues.unitAxisRotation);
		} else {
			// the largest sum of diagonal elements is 3, thus if absDiagSum is more than 1 it must be rounding error
			rodrigues.theta = 0;
			rodrigues.unitAxisRotation.set( 1, 0, 0 );
		}

		return rodrigues;
	}

	/**
	 * <p>
	 * Determines the axis of rotation for a given rotation matrix using eigenvalue decomposition.
	 * </p>
	 * <p/>
	 * <p>
	 * EVD can be used since R*v = v, where clearly the axis of rotation 'v' is an eigen vector
	 * that has an eigenvalue of 1.  This technique is numerically stable under all conditions.
	 * </p>
	 *
	 * @param R A rotation matrix.
	 * @return axis of rotation vector.
	 */
	public static Vector3D_F64 rotationAxis( DenseMatrix64F R, Vector3D_F64 ret ) {
		if( ret == null )
			ret = new Vector3D_F64();

		EigenDecomposition<DenseMatrix64F> eig = DecompositionFactory.eig(R.numRows,true);

		if( !eig.decompose( R ) )
			throw new RuntimeException( "Decomposition failed" );

		// find the eigenvalue closest to one
		int bestIndex = -1;
		double best = Double.MAX_VALUE;

		for( int i = 0; i < 3; i++ ) {
			Complex64F e = eig.getEigenvalue( i );
			if( e.isReal() ) {
				double diff = Math.abs( e.getReal() - 1.0 );
				if( diff < best ) {
					best = diff;
					bestIndex = i;
				}
			}
		}

		if( bestIndex == -1 )
			throw new RuntimeException( "Couldn't find a valid eigenvalue.  If the matrix is valid report this as a bug." );

		DenseMatrix64F v = eig.getEigenVector( bestIndex );

		ret.set( v.get( 0, 0 ), v.get( 1, 0 ), v.get( 2, 0 ) );
		return ret;
	}

	/**
	 * <p>
	 * Given a rotation matrix and its axis of rotation compute the rotation matrix's rotation
	 * angle.  This angle can be combined with the axisOfRotation to reconstruct the rotation matrix.
	 * </p>
	 * <p/>
	 * <p>
	 * A series of cross products and dot products are used.  Provides an alternative approach to
	 * {@link #matrixToRodrigues}.  Not sure if there is any advantage.
	 * </p>
	 *
	 * @param R Rotation matrix. Not modified.
	 * @param axisOfRotation Axis of rotation of the provided rotation matrix. Not modified.
	 * @return angle of rotation -&pi; &le; &theta; &le; &pi;
	 */
	public static double rotationAngle( DenseMatrix64F R, Vector3D_F64 axisOfRotation ) {
		// each row in R is perpendicular to each other
		// at least two of them is not parallel to the axis of rotation
		// find the row which is closest to being perpendicular
		int row = 0;
		double smallestDot = Double.MAX_VALUE;
		for( int i = 0; i < 3; i++ ) {
			double dot = R.get( i, 0 ) * axisOfRotation.getX() +
					R.get( i, 1 ) * axisOfRotation.getY()
					+ R.get( i, 2 ) * axisOfRotation.getZ();
			double v = Math.abs( dot );
			if( v < smallestDot ) {
				row = i;
				smallestDot = v;
			}
		}

		// extract the selected row
		Vector3D_F64 a = new Vector3D_F64();
		a.set( R.get( row, 0 ), R.get( row, 1 ), R.get( row, 2 ) );

		// compute the cross product of that vector against the rotation axis
		// to find a vector which is perpendicular to the axis of rotation
		Vector3D_F64 b = axisOfRotation.cross( a );
		b.normalize();

		// rotate the found vector
		GeometryMath_F64.mult( R, b, a );

		// compute the angle of rotation's magnitude using the dot product theorem
		double theta = Math.acos( GeometryMath_F64.dot( a, b ) );

		// find the direction using cross product
		Vector3D_F64 c = b.cross( a );
		double dot = c.dot( axisOfRotation );

		// the dot product will be positive if they are pointing in the same direction
		if( dot < 0 )
			theta = -theta;

		return theta;
	}

	/**
	 * Creates a rotation matrix about the x-axis.
	 *
	 * @param ang the angle it rotates a point by in radians.
	 * @param R
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
	 * @param r   A 3 by 3 matrix. Is modified.
	 */
	public static void setRotX( double ang, DenseMatrix64F r ) {
		double c = Math.cos( ang );
		double s = Math.sin( ang );

		r.set( 0, 0, 1 );
		r.set( 1, 1, c );
		r.set( 1, 2, -s );
		r.set( 2, 1, s );
		r.set( 2, 2, c );
	}

	/**
	 * Creates a rotation matrix about the y-axis.
	 *
	 * @param ang the angle it rotates a point by in radians.
	 * @param R
	 * @return The 3 by 3 rotation matrix.
	 */
	public static DenseMatrix64F rotY( double ang, DenseMatrix64F R ) {
		if( R == null )
			R = new DenseMatrix64F( 3, 3 );

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
	 * @param R
	 * @return The 3 by 3 rotation matrix.
	 */
	public static DenseMatrix64F rotZ( double ang, DenseMatrix64F R ) {
		if( R == null )
			R = new DenseMatrix64F( 3, 3 );

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
	 * Creates an Euler rotation matrix for an arbitrary ordering of rotations about axises.
	 * Which axis is rotated when is specified in the first three arguments.  0 = x axis
	 * , 1 = y axis, and 2 = z axis.  The remaining three parameters are the angles it will
	 * rotate about the respective axises.
	 *
	 * @param axisA The first axis it will rotate around.
	 * @param axisB The second axis it will rotate around.
	 * @param axisC The third axis it will rotate around.
	 * @param rotA  Rotation angle in radians about the first axis.
	 * @param rotB  Rotation angle in radians about the second axis.
	 * @param rotC  Rotation angle in radians about the third axis.
	 * @return Resulting 3 by 3 rotation matrix.
	 */
	public static DenseMatrix64F eulerArbitrary( int axisA, int axisB, int axisC,
												 double rotA, double rotB, double rotC ) {
		DenseMatrix64F R_a = createRot( axisA, rotA, null );
		DenseMatrix64F R_b = createRot( axisB, rotB, null );
		DenseMatrix64F R_c = createRot( axisC, rotC, null );

		DenseMatrix64F A = new DenseMatrix64F( 3, 3 );
		DenseMatrix64F R = new DenseMatrix64F( 3, 3 );

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
	private static DenseMatrix64F createRot( int axis, double angle, DenseMatrix64F R ) {
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
	 * Creates a rotation matrix that is equivalent to rotating around the x,
	 * y and z axises in that order.
	 *
	 * @param rotX The angle it is rotated by around the x-axis.
	 * @param rotY The angle it is rotated by around the y-axis.
	 * @param rotZ The angle it is rotated by around the z-axis.
	 * @param R
	 * @return A 3 by 3 rotation matrix.
	 */
	public static DenseMatrix64F eulerXYZ( double rotX, double rotY, double rotZ, DenseMatrix64F R ) {
		if( R == null )
			R = new DenseMatrix64F( 3, 3 );

		double c1 = Math.cos( rotX );
		double c2 = Math.cos( rotY );
		double c3 = Math.cos( rotZ );

		double s1 = Math.sin( rotX );
		double s2 = Math.sin( rotY );
		double s3 = Math.sin( rotZ );

		R.set( 0, 0, c2 * c3 );
		R.set( 0, 1, c3 * s1 * s2 - c1 * s3 );
		R.set( 0, 2, c1 * c3 * s2 + s1 * s3 );
		R.set( 1, 0, c2 * s3 );
		R.set( 1, 1, c1 * c3 + s1 * s2 * s3 );
		R.set( 1, 2, c1 * s2 * s3 - c3 * s1 );
		R.set( 2, 0, -s2 );
		R.set( 2, 1, c2 * s1 );
		R.set( 2, 2, c1 * c2 );

		return R;
	}

	/**
	 * <p>
	 * Given a rotation matrix it will compute the XYZ euler angles.
	 * </p>
	 * <p>
	 * See Internet PDF "Computing Euler angles from a rotation matrix" by Gregory G. Slabaugh.
	 * </p>                                                                                                                                                                      ose
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
		if( R == null ) {
			R = new DenseMatrix64F( 3, 3 );
		}

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
	 * Converts euler rotation coordinates into quaternions.
	 *
	 * @param euler Euler coordinates encoded as [yaw, roll, pitch].
	 * @param quat  Quaternion
	 */
	public static void eulerToQuaternions( double euler[], Quaternion_F64 quat ) {

		double yaw = euler[0];
		double roll = euler[1];
		double pitch = euler[2];

		// See: http://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles
		double cy = Math.cos( yaw * 0.5 );
		double sy = Math.sin( yaw * 0.5 );
		double cp = Math.cos( pitch * 0.5 );
		double sp = Math.sin( pitch * 0.5 );
		double cr = Math.cos( roll * 0.5 );
		double sr = Math.sin( roll * 0.5 );

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

	public static double[] quaternionsToEuler( Quaternion_F64 quaternion, double euler[] ) {

      return null;
	}

	/**
	 * Converts a quaternion into a rotation matrix.
	 * <p/>
	 * <p>
	 * Equations is taken from: Paul J. Besl and Neil D. McKay, "A Method for Registration of 3-D Shapes" IEEE
	 * Transactions on Pattern Analysis and Machine Intelligence, Vol 14, No. 2, Feb. 1992
	 * </p>
	 *
	 * @param quat unit quaternion.
	 * @param R	Storage for rotation matrix.  If null a new matrix is created. Modified.
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
	 * <p/>
	 * <p>
	 * Equations is taken from: Paul J. Besl and Neil D. McKay, "A Method for Registration of 3-D Shapes" IEEE
	 * Transactions on Pattern Analysis and Machine Intelligence, Vol 14, No. 2, Feb. 1992
	 * </p>
	 *
	 * @param quat unit quaternion.
	 * @param R	Storage for rotation matrix.  If null a new matrix is created. Modified.
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
