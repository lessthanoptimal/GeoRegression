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

package georegression.struct.se;

import georegression.geometry.RotationMatrixGenerator;
import georegression.struct.affine.Affine2D_F32;
import georegression.struct.point.Vector3D_F32;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;


/**
 * Various operations related to {@link SpecialEuclidean} transformations.
 *
 * @author Peter Abeles
 */
public class SpecialEuclideanOps_F32 {

	/**
	 * Sets the provided transform so that it does not transform any points.
	 *
	 * @param se The transform which is to be set to no motion.
	 */
	public static void setToNoMotion( Se3_F32 se ) {
		CommonOps.setIdentity( se.getR() );
		se.getT().set( 0, 0, 0 );
	}

	/**
	 * Converts {@link Se2_F32} into {@link Affine2D_F32}.
	 * @param se (Input) Se2
	 * @param affine (Output) Equivalent affine.  If null a new object will be declared.
	 * @return Equivalent affine.
	 */
	public static Affine2D_F32 toAffine( Se2_F32 se , Affine2D_F32 affine ) {
		if( affine == null )
			affine = new Affine2D_F32();

		affine.a11 = se.c;
		affine.a12 = -se.s;
		affine.a21 = se.s;
		affine.a22 = se.c;

		affine.tx = se.T.x;
		affine.ty = se.T.y;

		return affine;
	}

	/**
	 * Converts it into a 4 by 4 homogeneous matrix.
	 *
	 * @param se  original 3D transform
	 * @param ret Where the results will be written to.  If null a new matrix is declared. Modified.
	 * @return equivalent homogeneous transform.
	 */
	public static DenseMatrix64F toHomogeneous( Se3_F32 se, DenseMatrix64F ret ) {
		if( ret == null )
			ret = new DenseMatrix64F( 4, 4 );
		else {
			ret.set( 3, 0, 0 );
			ret.set( 3, 1, 0 );
			ret.set( 3, 2, 0 );
		}

		CommonOps.insert( se.getR(), ret, 0, 0 );
		Vector3D_F32 T = se.getT();

		ret.set( 0, 3, T.x );
		ret.set( 1, 3, T.y );
		ret.set( 2, 3, T.z );
		ret.set( 3, 3, 1 );

		return ret;
	}

	/**
	 * Converts a homogeneous representation into {@link Se3_F32}.
	 *
	 * @param H   Homogeneous 4 by 4 matrix.
	 * @param ret If not null where the results are written to.
	 * @return Se3_F32 transform.
	 */
	public static Se3_F32 toSe3_F32( DenseMatrix64F H, Se3_F32 ret ) {
		if( H.numCols != 4 || H.numRows != 4 )
			throw new IllegalArgumentException( "The homogeneous matrix must be 4 by 4 by definition." );

		if( ret == null )
			ret = new Se3_F32();

		ret.setTranslation( (float) H.get( 0, 3 ), (float) H.get( 1, 3 ), (float) H.get( 2, 3 ) );

		CommonOps.extract( H, 0, 3, 0, 3, ret.getR(), 0, 0 );

		return ret;
	}

	/**
	 * Converts it into a 3 by 3 homogeneous matrix.
	 *
	 * @param se  original 2D transform
	 * @param ret Where the results will be written to.  If null a new matrix is declared. Modified.
	 * @return equivalent homogeneous transform.
	 */
	public static DenseMatrix64F toHomogeneous( Se2_F32 se, DenseMatrix64F ret ) {
		if( ret == null )
			ret = new DenseMatrix64F( 3, 3 );
		else {
			ret.set( 2, 0, 0 );
			ret.set( 2, 1, 0 );
		}

		final float c = se.getCosineYaw();
		final float s = se.getSineYaw();

		ret.set( 0, 0, c );
		ret.set( 0, 1, -s );
		ret.set( 1, 0, s );
		ret.set( 1, 1, c );
		ret.set( 0, 2, se.getX() );
		ret.set( 1, 2, se.getY() );
		ret.set( 2, 2, 1 );

		return ret;
	}

	/**
	 * Converts a homogeneous representation into {@link Se2_F32}.
	 *
	 * @param H   Homogeneous 3 by 3 matrix.
	 * @param ret If not null where the results are written to.
	 * @return Se3_F32 transform.
	 */
	public static Se2_F32 toSe2( DenseMatrix64F H, Se2_F32 ret ) {
		if( H.numCols != 3 || H.numRows != 3 )
			throw new IllegalArgumentException( "The homogeneous matrix must be 3 by 3 by definition." );

		if( ret == null )
			ret = new Se2_F32();

		ret.setTranslation( (float) H.get( 0, 2 ), (float) H.get( 1, 2 ) );

		float c = (float) H.get( 0, 0 );
		float s = (float) H.get( 1, 0 );

		ret.setYaw( (float)Math.atan2( s, c ) );

		return ret;
	}

	/**
	 * Sets the value of an {@link Se3_F32} using Euler XYZ coordinates for the rotation and
	 * a translation vector.
	 *
	 * @param rotX Rotation around X axis.
	 * @param rotY Rotation around Y axis.
	 * @param rotZ Rotation around Z axis.
	 * @param dx   Translation along x-axis.
	 * @param dy   Translation along y-axis.
	 * @param dz   Translation along z-axis.
	 * @param se   If not null then the transform is written here.
	 * @return The transform.
	 */
	public static Se3_F32 setEulerXYZ( float rotX, float rotY, float rotZ,
									   float dx, float dy, float dz,
									   Se3_F32 se ) {
		if( se == null )
			se = new Se3_F32();

		RotationMatrixGenerator.eulerXYZ( rotX, rotY, rotZ, se.getR() );
		Vector3D_F32 T = se.getT();
		T.x = dx;
		T.y = dy;
		T.z = dz;

		return se;
	}
}
