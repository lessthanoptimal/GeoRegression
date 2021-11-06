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

import georegression.struct.GeoTuple2D_F64;
import georegression.struct.GeoTuple3D_F64;
import georegression.struct.GeoTuple4D_F64;
import org.ejml.data.DMatrixRMaj;
import org.jetbrains.annotations.Nullable;


/**
 * Math operations that can be applied to geometric primitives.
 *
 * @author Peter Abeles
 */
// TODO rename to PerspectiveMath?
// todo separate off fucntions that are in homogeneous coordinates into their own class?
//      alternatively indicate by the function name?
// todo make sure all functions have unit tests
@SuppressWarnings({"unchecked", "RedundantCast", "rawtypes"})
public class GeometryMath_F64 {

	/**
	 * Creates a skew symmetric cross product matrix from the provided tuple.
	 *
	 * @param x0  Element 0.
	 * @param x1  Element 1.
	 * @param x2  Element 2.
	 * @param ret If not null the results are stored here, otherwise a new matrix is created.
	 * @return Skew symmetric cross product matrix.
	 */
	public static DMatrixRMaj crossMatrix( double x0, double x1, double x2, @Nullable DMatrixRMaj ret ) {
		if( ret == null ) {
			ret = new DMatrixRMaj( 3, 3 );
		} else {
			ret.zero();
		}

		ret.set( 0, 1, -x2 );
		ret.set( 0, 2, x1 );
		ret.set( 1, 0, x2 );
		ret.set( 1, 2, -x0 );
		ret.set( 2, 0, -x1 );
		ret.set( 2, 1, x0 );

		return ret;
	}

	/**
	 * Creates a skew symmetric cross product matrix from the provided tuple.
	 *
	 * @param v Tuple. Not modified.
	 * @param ret If not null the results are stored here, otherwise a new matrix is created.
	 * @return Skew symmetric cross product matrix.
	 */
	public static DMatrixRMaj crossMatrix( GeoTuple3D_F64 v, @Nullable DMatrixRMaj ret ) {
		if( ret == null ) {
			ret = new DMatrixRMaj( 3, 3 );
		} else {
			ret.zero();
		}

		double x = v.getX();
		double y = v.getY();
		double z = v.getZ();

		ret.set( 0, 1, -z );
		ret.set( 0, 2, y );
		ret.set( 1, 0, z );
		ret.set( 1, 2, -x );
		ret.set( 2, 0, -y );
		ret.set( 2, 1, x );

		return ret;
	}

	/**
	 * <p>
	 * Computes the cross product:<br>
	 * <br>
	 * c = a x b
	 * </p>
	 *
	 * @param a Not modified.
	 * @param b Not modified.
	 * @param c Modified.
	 */
	public static void cross( GeoTuple3D_F64 a, GeoTuple3D_F64 b, GeoTuple3D_F64 c ) {
		c.x = a.y * b.z - a.z * b.y;
		c.y = a.z * b.x - a.x * b.z;
		c.z = a.x * b.y - a.y * b.x;
	}

	/**
	 * <p>
	 * Computes the cross product:<br>
	 * <br>
	 * c = a x b
	 * </p>
	 *
	 * @param a_x x-coordinate of a
	 * @param a_y y-coordinate of a
	 * @param a_z z-coordinate of a
	 * @param b_x x-coordinate of b
	 * @param b_y y-coordinate of b
	 * @param b_z z-coordinate of b
	 * @param c Modified.
	 */
	public static void cross( double a_x, double a_y , double a_z ,
							  double b_x, double b_y , double b_z,
							  GeoTuple3D_F64 c ) {
		c.x = a_y * b_z - a_z * b_y;
		c.y = a_z * b_x - a_x * b_z;
		c.z = a_x * b_y - a_y * b_x;
	}

	/**
	 * <p>
	 * Computes the cross product:<br>
	 * <br>
	 * c = a x b<br>
	 * where 'a' is in homogeneous coordinates.
	 * </p>
	 *
	 * @param a Homogeneous coordinates, z = 1 assumed. Not modified.
	 * @param b Not modified.
	 * @param c Modified.
	 */
	public static void cross( GeoTuple2D_F64 a, GeoTuple3D_F64 b, GeoTuple3D_F64 c ) {
		c.x = a.y * b.z - b.y;
		c.y = b.x - a.x * b.z;
		c.z = a.x * b.y - a.y * b.x;
	}

	/**
	 * <p>
	 * Computes the cross product:<br>
	 * <br>
	 * c = a x b
	 * </p>
	 *
	 * @param a Homogeneous coordinates, z = 1 assumed. Not modified.
	 * @param b Homogeneous coordinates, z = 1 assumed. Not modified.
	 * @param c Modified.
	 */
	public static void cross( GeoTuple2D_F64 a, GeoTuple2D_F64 b, GeoTuple3D_F64 c ) {
		c.x = a.y * 1   -       b.y;
		c.y =       b.x - a.x;
		c.z = a.x * b.y - a.y * b.x;
	}

	/**
	 * <p>
	 * Adds two points together.<br>
	 * <br>
	 * c = a + b
	 * </p>
	 * <p/>
	 * <p>
	 * Point 'c' can be the same instance as 'a' or 'b'.
	 * </p>
	 *
	 * @param a A point. Not modified.
	 * @param b A point. Not modified.
	 * @param c Where the results are stored. Modified.
	 */
	public static void add( GeoTuple3D_F64 a, GeoTuple3D_F64 b, GeoTuple3D_F64 c ) {
		c.x = a.x + b.x;
		c.y = a.y + b.y;
		c.z = a.z + b.z;
	}

	/**
	 * <p>
	 * Adds two points together while scaling them.<br>
	 * <br>
	 * pt<sub>2</sub> = a<sub>0</sub> pt<sub>0</sub> + a<sub>1</sub> pt<sub>1</sub>
	 * </p>
	 * <p/>
	 * <p>
	 * Point 'c' can be the same instance as 'a' or 'b'.
	 * </p>
	 *
	 * @param a0 Scaling factor for pt0.
	 * @param pt0 A point. Not modified.
	 * @param a1 Scaling factor for pt1.
	 * @param pt1 A point. Not modified.
	 * @param pt2 Where the results are stored. Modified.
	 */
	public static void add( double a0, GeoTuple3D_F64 pt0, double a1, GeoTuple3D_F64 pt1, GeoTuple3D_F64 pt2 ) {
		pt2.x = a0 * pt0.x + a1 * pt1.x;
		pt2.y = a0 * pt0.y + a1 * pt1.y;
		pt2.z = a0 * pt0.z + a1 * pt1.z;
	}

	/**
	 * <p>ret = p0 + M*p1</p>
	 *
	 * Safe to pass in the same instance of a point more than once.
	 */
	public static <T extends GeoTuple3D_F64> T addMult( T p0, DMatrixRMaj M, T p1, @Nullable T result ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols );

		if( result == null ) {
			result = (T) p0.createNewInstance();
		}

		double x = p1.x;
		double y = p1.y;
		double z = p1.z;

		result.x = p0.x + (double) ( M.data[0] * x + M.data[1] * y + M.data[2] * z );
		result.y = p0.y + (double) ( M.data[3] * x + M.data[4] * y + M.data[5] * z );
		result.z = p0.z + (double) ( M.data[6] * x + M.data[7] * y + M.data[8] * z );

		return result;
	}

	/**
	 * <p>ret = p0 + M<sup>T</sup>*p1</p>
	 *
	 * Safe to pass in the same instance of a point more than once.
	 */
	public static <T extends GeoTuple3D_F64> T addMultTrans( T p0, DMatrixRMaj M, T p1, @Nullable T result ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols );

		if( result == null ) {
			result = (T) p0.createNewInstance();
		}

		double x = p1.x;
		double y = p1.y;
		double z = p1.z;

		result.x = p0.x + (double) ( M.data[0] * x + M.data[3] * y + M.data[6] * z );
		result.y = p0.y + (double) ( M.data[1] * x + M.data[4] * y + M.data[7] * z );
		result.z = p0.z + (double) ( M.data[2] * x + M.data[5] * y + M.data[8] * z );

		return result;
	}

	/**
	 * <p>
	 * Substracts two points from each other.<br>
	 * <br>
	 * c = a - b
	 * </p>
	 * <p/>
	 * <p>
	 * Point 'c' can be the same instance as 'a' or 'b'.
	 * </p>
	 *
	 * @param a A point. Not modified.
	 * @param b A point. Not modified.
	 * @param c Where the results are stored. Modified.
	 */
	public static void sub( GeoTuple3D_F64 a, GeoTuple3D_F64 b, GeoTuple3D_F64 c ) {
		c.x = a.x - b.x;
		c.y = a.y - b.y;
		c.z = a.z - b.z;
	}

	/**
	 * Rotates a 2D point by the specified angle.
	 *
	 * @param solution where the solution is written to. Can be the same point as 'pt'.
	 */
	public static void rotate( double theta, GeoTuple2D_F64 pt, GeoTuple2D_F64 solution ) {
		double c = Math.cos( theta );
		double s = Math.sin( theta );

		double x = pt.x;
		double y = pt.y;

		solution.x = c * x - s * y;
		solution.y = s * x + c * y;
	}

	/**
	 * Rotates a 2D point by the specified angle.
	 *
	 * @param c Cosine of theta
	 * @param s Sine of theta
	 * @param pt
	 * @param solution where the solution is written to. Can be the same point as 'pt'.
	 */
	public static void rotate( double c , double s, GeoTuple2D_F64 pt, GeoTuple2D_F64 solution ) {

		double x = pt.x;
		double y = pt.y;

		solution.x = c * x - s * y;
		solution.y = s * x + c * y;
	}

	/**
	 * mod = M*pt
	 * <p>
	 * pt and mod can be the same reference.
	 * </p>
	 *
	 * @param M
	 * @param pt
	 * @param result Storage for output. Can be the same instance as param 'pt'. Modified.
	 */
	public static <T extends GeoTuple3D_F64> T mult( DMatrixRMaj M, T pt, @Nullable T result ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols );

		if( result == null ) {
			result = (T) pt.createNewInstance();
		}

		double x = pt.x;
		double y = pt.y;
		double z = pt.z;

		result.x = (double) ( M.data[0] * x + M.data[1] * y + M.data[2] * z );
		result.y = (double) ( M.data[3] * x + M.data[4] * y + M.data[5] * z );
		result.z = (double) ( M.data[6] * x + M.data[7] * y + M.data[8] * z );

		return (T) result;
	}

	/**
	 * mod = M*pt
	 * <p>
	 * pt and mod can be the same reference. M is a 4x4 matrix. Homogenous coordinates with implicit w = 1
	 * </p>
	 *
	 * @param result Storage for output. Can be the same instance as param 'pt'. Modified.
	 */
	public static <T extends GeoTuple3D_F64> T mult4( DMatrixRMaj M, T pt, @Nullable T result ) {
		if( M.numRows != 4 || M.numCols != 4 )
			throw new IllegalArgumentException( "Input matrix must be 4 by 4, not " + M.numRows + " " + M.numCols );

		if( result == null ) {
			result = (T) pt.createNewInstance();
		}

		double x = pt.x;
		double y = pt.y;
		double z = pt.z;

		result.x = (double) ( M.data[0] * x + M.data[1] * y + M.data[2 ] * z + M.data[3]);
		result.y = (double) ( M.data[4] * x + M.data[5] * y + M.data[6 ] * z + M.data[7]);
		result.z = (double) ( M.data[8] * x + M.data[9] * y + M.data[10] * z + M.data[11]);
		double w = (double) ( M.data[12] * x + M.data[13] * y + M.data[14] * z + M.data[15]);

		result.x /= w;
		result.y /= w;
		result.z /= w;

		return (T) result;
	}


	/**
	 * <p>
	 * mod = M*pt<br>
	 * where mod is a 2D point that has an implicit z=1.
	 * </p>
	 *
	 * <p>
	 * Multiplies the 3x3 matrix against the 3D point, and normalizes the 2D point output
	 * by dividing the x and y values by the found z.
	 * </p>
	 */
	public static <T extends GeoTuple2D_F64> T mult( DMatrixRMaj M, GeoTuple3D_F64 pt, T mod ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols );

		double x = pt.x;
		double y = pt.y;
		double z = pt.z;

		mod.x = (double) ( M.unsafe_get(0, 0) * x + M.unsafe_get(0, 1) * y + M.unsafe_get(0, 2) * z );
		mod.y = (double) ( M.unsafe_get(1, 0) * x + M.unsafe_get(1, 1) * y + M.unsafe_get(1, 2) * z );
		z = (double) ( M.unsafe_get(2, 0) * x + M.unsafe_get(2, 1) * y + M.unsafe_get(2, 2) * z );

		mod.x /= z;
		mod.y /= z;

		return mod;
	}

	/**
	 * <p>
	 * mod = M*pt<br>
	 * where pt has z=1 implicitly.
	 * </p>
	 *
	 * <p>
	 * Multiplies the 3x3 matrix against the 2D point, which has an implicit z=1 value, and the output is
	 * a 3d point.
	 * </p>
	 */
	public static void mult( DMatrixRMaj M, GeoTuple2D_F64 pt, GeoTuple3D_F64 mod ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols );

		double x = pt.x;
		double y = pt.y;

		mod.x = (double) ( M.unsafe_get(0, 0) * x + M.unsafe_get(0, 1) * y + M.unsafe_get(0, 2) );
		mod.y = (double) ( M.unsafe_get(1, 0) * x + M.unsafe_get(1, 1) * y + M.unsafe_get(1, 2) );
		mod.z = (double) ( M.unsafe_get(2, 0) * x + M.unsafe_get(2, 1) * y + M.unsafe_get(2, 2) );
	}

	/**
	 * <p>
	 * Computes mod =  M*pt, where both pt and mod are in homogeneous coordinates with z assumed to be
	 * equal to 1, and M is a 3x3 matrix.
	 * </p>
	 * <p>
	 * 'pt' and 'mod' can be the same point.
	 * </p>
	 * @param M  3x3 matrix
	 * @param pt Homogeneous point with z=1
	 * @param mod Storage for the computation. If null a new point is declared. Can be same instance as pt.
	 * @return Result of computation.
	 */
	public static <T extends GeoTuple2D_F64> T mult( DMatrixRMaj M, T pt, @Nullable T mod ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols );

		if( mod == null ) {
			throw new IllegalArgumentException( "Must provide an instance in mod" );
		}

		double x = pt.x;
		double y = pt.y;

		double modz = (double) ( M.unsafe_get( 2, 0 ) * x + M.unsafe_get(2, 1) * y + M.unsafe_get(2, 2) );

		mod.x = (double) ( ( M.unsafe_get(0, 0) * x + M.unsafe_get(0, 1) * y + M.unsafe_get(0, 2) ) / modz );
		mod.y = (double) ( ( M.unsafe_get(1, 0) * x + M.unsafe_get(1, 1) * y + M.unsafe_get(1, 2) ) / modz );

		return mod;
	}

	/**
	 * x = P*X
	 *
	 * @param P Projective 3x4 matrix
	 * @param X 3D point in homogenous coordinates
	 * @param mod 2D point in homogenous coordinates
	 */
	public static void mult(DMatrixRMaj P, GeoTuple4D_F64 X, GeoTuple3D_F64 mod ) {
		if( P.numCols != 4 )
			throw new IllegalArgumentException( "Input matrix must have 4 columns not "+P.numCols );

		mod.x = P.data[0] * X.x + P.data[1] * X.y + P.data[2] * X.z + P.data[3] * X.w;
		mod.y = P.data[4] * X.x + P.data[5] * X.y + P.data[6] * X.z + P.data[7] * X.w;
		mod.z = P.data[8] * X.x + P.data[9] * X.y + P.data[10] * X.z + P.data[11] * X.w;

		if( P.numRows == 4 ) {
			double w = P.data[12] * X.x + P.data[13] * X.y + P.data[14] * X.z + P.data[15] * X.w;
			mod.x /= w;
			mod.y /= w;
			mod.z /= w;
		} else if( P.numRows != 3 ) {
			throw new IllegalArgumentException("rows must be 3 or 4 and not "+P.numRows);
		}
	}

	/**
	 * x = P*X
	 *
	 * @param P 4x4 matrix
	 * @param X 3D point in homogenous coordinates
	 * @param mod 3D point in homogenous coordinates
	 */
	public static void mult(DMatrixRMaj P, GeoTuple4D_F64 X, GeoTuple4D_F64 mod ) {
		if( P.numRows != 4 || P.numCols != 4 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 4 not " + P.numRows + " " + P.numCols );

		mod.x = P.data[0 ]*X.x + P.data[1 ]*X.y + P.data[2 ]*X.z + P.data[3 ]*X.w;
		mod.y = P.data[4 ]*X.x + P.data[5 ]*X.y + P.data[6 ]*X.z + P.data[7 ]*X.w;
		mod.z = P.data[8 ]*X.x + P.data[9 ]*X.y + P.data[10]*X.z + P.data[11]*X.w;
		mod.w = P.data[12]*X.x + P.data[13]*X.y + P.data[14]*X.z + P.data[15]*X.w;
	}

	/**
	 * x = (P*X)/z
	 *
	 * @param P Projective 3x4 matrix
	 * @param X 3D point in homogenous coordinates
	 * @param mod 2D point in coordinates
	 */
	public static void mult(DMatrixRMaj P, GeoTuple4D_F64 X, GeoTuple2D_F64 mod ) {
		if( P.numRows != 3 || P.numCols != 4 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 4 not " + P.numRows + " " + P.numCols );

		double x = P.data[0]*X.x + P.data[1]*X.y + P.data[2]*X.z + P.data[3]*X.w;
		double y = P.data[4]*X.x + P.data[5]*X.y + P.data[6]*X.z + P.data[7]*X.w;
		double z = P.data[8]*X.x + P.data[9]*X.y + P.data[10]*X.z + P.data[11]*X.w;

		mod.x = x/z;
		mod.y = y/z;
	}

	/**
	 * <p>
	 * Computes the following:<br>
	 * result = cross(A)*M<br>
	 * where M and result are 3x3 matrices, cross(A) is the cross product matrix of A.
	 * </p>
	 *
	 * @param A 2D homogenous coordinate (implicit z = 1) that is internally converted into cross product matrix.
	 * @param M 3x3 matrix.
	 * @param result Storage for results. Can be null.
	 * @return Results.
	 */
	public static DMatrixRMaj multCrossA( GeoTuple2D_F64 A , DMatrixRMaj M, @Nullable DMatrixRMaj result ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols );

		if( result == null ) {
			result = new DMatrixRMaj(3,3);
		}

		double x = A.x;
		double y = A.y;

		double a11 = M.data[0]; double a12 = M.data[1]; double a13 = M.data[2];
		double a21 = M.data[3]; double a22 = M.data[4]; double a23 = M.data[5];
		double a31 = M.data[6]; double a32 = M.data[7]; double a33 = M.data[8];

		result.data[0] = -a21 + a31*y;
		result.data[1] = -a22 + a32*y;
		result.data[2] = -a23 + a33*y;
		result.data[3] = a11 - a31*x;
		result.data[4] = a12 - a32*x;
		result.data[5] = a13 - a33*x;
		result.data[6] = -a11*y + a21*x;
		result.data[7] = -a12*y + a22*x;
		result.data[8] = -a13*y + a23*x;

		return result;
	}

	/**
	 * <p>
	 * Computes the following:<br>
	 * result = cross(A)<sup>T</sup>*M<br>
	 * where M and result are 3x3 matrices, cross(A) is the cross product matrix of A.
	 * </p>
	 *
	 * @param A 2D homogenous coordinate (implicit z = 1) that is internally converted into cross product matrix.
	 * @param M 3x3 matrix.
	 * @param result Storage for results. Can be null.
	 * @return Results.
	 */
	public static DMatrixRMaj multCrossATransA( GeoTuple2D_F64 A , DMatrixRMaj M, @Nullable DMatrixRMaj result ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols );

		if( result == null ) {
			result = new DMatrixRMaj(3,3);
		}

		double x = A.x;
		double y = A.y;

		double a11 = M.data[0]; double a12 = M.data[1]; double a13 = M.data[2];
		double a21 = M.data[3]; double a22 = M.data[4]; double a23 = M.data[5];
		double a31 = M.data[6]; double a32 = M.data[7]; double a33 = M.data[8];

		result.data[0] =  a21   - a31*y;
		result.data[1] =  a22   - a32*y;
		result.data[2] =  a23   - a33*y;
		result.data[3] = -a11   + a31*x;
		result.data[4] = -a12   + a32*x;
		result.data[5] = -a13   + a33*x;
		result.data[6] =  a11*y - a21*x;
		result.data[7] =  a12*y - a22*x;
		result.data[8] =  a13*y - a23*x;

		return result;
	}

	/**
	 * <p>
	 * Computes the following:<br>
	 * result = cross(A)*M<br>
	 * where M and result are 3x3 matrices, cross(A) is the cross product matrix of A.
	 * </p>
	 *
	 * @param A 3D coordinate that is internally converted into cross product matrix.
	 * @param M 3x3 matrix.
	 * @param result Storage for results. Can be null.
	 * @return Results.
	 */
	public static DMatrixRMaj multCrossA( GeoTuple3D_F64 A , DMatrixRMaj M, @Nullable DMatrixRMaj result ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols );

		if( result == null ) {
			result = new DMatrixRMaj(3,3);
		}

		double x = A.x;
		double y = A.y;
		double z = A.z;

		double a11 = M.data[0]; double a12 = M.data[1]; double a13 = M.data[2];
		double a21 = M.data[3]; double a22 = M.data[4]; double a23 = M.data[5];
		double a31 = M.data[6]; double a32 = M.data[7]; double a33 = M.data[8];

		result.data[0] = -a21*z + a31*y;
		result.data[1] = -a22*z + a32*y;
		result.data[2] = -a23*z + a33*y;
		result.data[3] =  a11*z - a31*x;
		result.data[4] =  a12*z - a32*x;
		result.data[5] =  a13*z - a33*x;
		result.data[6] = -a11*y + a21*x;
		result.data[7] = -a12*y + a22*x;
		result.data[8] = -a13*y + a23*x;

		return result;
	}

	/**
	 * <p>
	 * Computes the following:<br>
	 * result = cross(A)<sup>T</sup>*M<br>
	 * where M and result are 3x3 matrices, cross(A) is the cross product matrix of A.
	 * </p>
	 *
	 * @param A 3D coordinate that is internally converted into cross product matrix.
	 * @param M 3x3 matrix.
	 * @param result Storage for results. Can be null.
	 * @return Results.
	 */
	public static DMatrixRMaj multCrossATransA( GeoTuple3D_F64 A , DMatrixRMaj M, @Nullable DMatrixRMaj result ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols );

		if( result == null ) {
			result = new DMatrixRMaj(3,3);
		}

		double x = A.x;
		double y = A.y;
		double z = A.z;

		double a11 = M.data[0]; double a12 = M.data[1]; double a13 = M.data[2];
		double a21 = M.data[3]; double a22 = M.data[4]; double a23 = M.data[5];
		double a31 = M.data[6]; double a32 = M.data[7]; double a33 = M.data[8];

		result.data[0] =  a21*z - a31*y;
		result.data[1] =  a22*z - a32*y;
		result.data[2] =  a23*z - a33*y;
		result.data[3] = -a11*z + a31*x;
		result.data[4] = -a12*z + a32*x;
		result.data[5] = -a13*z + a33*x;
		result.data[6] =  a11*y - a21*x;
		result.data[7] =  a12*y - a22*x;
		result.data[8] =  a13*y - a23*x;

		return result;
	}

	/**
	 * mod = M<sup>T</sup>*pt. Both pt and mod can be the same instance.
	 */
	public static <T extends GeoTuple3D_F64> T multTran( DMatrixRMaj M, T pt, @Nullable T mod ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Rotation matrices are 3 by 3." );

		if( mod == null ) {
			mod = (T) pt.createNewInstance();
		}

		double x = pt.x;
		double y = pt.y;
		double z = pt.z;

		mod.x = (double) ( M.unsafe_get( 0, 0 ) * x + M.unsafe_get( 1, 0 ) * y + M.unsafe_get( 2, 0 ) * z );
		mod.y = (double) ( M.unsafe_get( 0, 1 ) * x + M.unsafe_get( 1, 1 ) * y + M.unsafe_get( 2, 1 ) * z );
		mod.z = (double) ( M.unsafe_get( 0, 2 ) * x + M.unsafe_get( 1, 2 ) * y + M.unsafe_get( 2, 2 ) * z );

		return (T) mod;
	}

	/**
	 * mod = M<sup>T</sup>*pt<br>
	 * where pt.z = 1 implicitly.
	 *
	 * @param M 3 by 3 matrix.
	 * @param pt 2D point in homogeneous coordinates. Implicit z = 1
	 * @param mod 2D point in homogeneous coordinates. Implicit z = 1
	 * @return 2D point in homogeneous coordinates. Implicit z = 1
	 */
	public static <T extends GeoTuple3D_F64> T multTran( DMatrixRMaj M, GeoTuple2D_F64 pt, T mod ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Rotation matrices are 3 by 3." );

		double x = pt.x;
		double y = pt.y;

		mod.x = (double) ( M.unsafe_get( 0, 0 ) * x + M.unsafe_get( 1, 0 ) * y + M.unsafe_get( 2, 0 ) );
		mod.y = (double) ( M.unsafe_get( 0, 1 ) * x + M.unsafe_get( 1, 1 ) * y + M.unsafe_get( 2, 1 ) );
		mod.z = (double) ( M.unsafe_get( 0, 2 ) * x + M.unsafe_get( 1, 2 ) * y + M.unsafe_get( 2, 2 ) );

		return mod;
	}

	/**
	 * mod = M<sup>T</sup>*pt<br>
	 * where pt.z = 1 implicitly.
	 *
	 * @param M 3 by 3 matrix.
	 * @param pt 2D point in homogeneous coordinates. Implicit z = 1
	 * @param mod 2D point in homogeneous coordinates. Implicit z = 1
	 * @return 2D point in homogeneous coordinates. Implicit z = 1
	 */
	public static <T extends GeoTuple2D_F64> T multTran( DMatrixRMaj M, GeoTuple2D_F64 pt, T mod ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "Rotation matrices are 3 by 3." );

		double x = pt.x;
		double y = pt.y;

		double modZ = (double) ( M.unsafe_get( 0, 2 ) * x + M.unsafe_get( 1, 2 ) * y + M.unsafe_get( 2, 2 ) );
		mod.x = (double) ( M.unsafe_get( 0, 0 ) * x + M.unsafe_get( 1, 0 ) * y + M.unsafe_get( 2, 0 ) )/modZ;
		mod.y = (double) ( M.unsafe_get( 0, 1 ) * x + M.unsafe_get( 1, 1 ) * y + M.unsafe_get( 2, 1 ) )/modZ;

		return mod;
	}

	/**
	 * <p>
	 * ret = a<sup>T</sup>*M*b
	 * </p>
	 *
	 * @param a 3D point.
	 * @param M 3 by 3 matrix.
	 * @param b 3D point.
	 * @return scalar number
	 */
	public static double innerProd( GeoTuple3D_F64 a, DMatrixRMaj M, GeoTuple3D_F64 b ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "M must be 3 by 3." );

		// a^T*M
		double am0 = a.x*M.data[0] + a.y*M.data[3] + a.z*M.data[6];
		double am1 = a.x*M.data[1] + a.y*M.data[4] + a.z*M.data[7];
		double am2 = a.x*M.data[2] + a.y*M.data[5] + a.z*M.data[8];

		// right side
		return am0*b.x + am1*b.y + am2*b.z;
	}

	/**
	 * <p>
	 * Computes the inner matrix product:<br>
	 * ret = x<sup>T</sup>A<sup>T</sup>y
	 * </p>
	 *
	 * @param a 3D point.
	 * @param M 3 by 3 matrix.
	 * @param b 3D point.
	 * @return scalar number
	 */
	public static double innerProdTranM( GeoTuple3D_F64 a, DMatrixRMaj M, GeoTuple3D_F64 b ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "M must be 3 by 3." );

		// a^T*M^T
		double am0 = a.x*M.data[0] + a.y*M.data[1] + a.z*M.data[2];
		double am1 = a.x*M.data[3] + a.y*M.data[4] + a.z*M.data[5];
		double am2 = a.x*M.data[6] + a.y*M.data[7] + a.z*M.data[8];

		// right side
		return am0*b.x + am1*b.y + am2*b.z;
	}

	/**
	 * Computes the outer product of two vectors:<br>
	 * O = a*b<sup>T</sup>
	 *
	 * @param a 3D vector
	 * @param b 3D vector
	 * @param ret 3 x 3 matrix or null.
	 * @return outer product of two 3d vectors
	 */
	public static DMatrixRMaj outerProd(GeoTuple3D_F64 a, GeoTuple3D_F64 b,
										@Nullable DMatrixRMaj ret) {
		if( ret == null )
			ret = new DMatrixRMaj(3,3);

		ret.data[0] = a.x*b.x;
		ret.data[1] = a.x*b.y;
		ret.data[2] = a.x*b.z;
		ret.data[3] = a.y*b.x;
		ret.data[4] = a.y*b.y;
		ret.data[5] = a.y*b.z;
		ret.data[6] = a.z*b.x;
		ret.data[7] = a.z*b.y;
		ret.data[8] = a.z*b.z;

		return ret;
	}

	/**
	 * Adds the outer product of two vectors onto a matrix:<br>
	 * ret = A + scalar*a*b<sup>T</sup>
	 *
	 * @param A 3x3 matrix
	 * @param b 3D vector
	 * @param c 3D vector
	 * @param ret 3 x 3 matrix or null.
	 * @return outer product of two 3d vectors
	 */
	public static DMatrixRMaj addOuterProd(DMatrixRMaj A , double scalar , GeoTuple3D_F64 b, GeoTuple3D_F64 c,
										   @Nullable DMatrixRMaj ret) {
		if( ret == null )
			ret = new DMatrixRMaj(3,3);

		ret.data[0] = A.data[0] + scalar*b.x*c.x;
		ret.data[1] = A.data[1] + scalar*b.x*c.y;
		ret.data[2] = A.data[2] + scalar*b.x*c.z;
		ret.data[3] = A.data[3] + scalar*b.y*c.x;
		ret.data[4] = A.data[4] + scalar*b.y*c.y;
		ret.data[5] = A.data[5] + scalar*b.y*c.z;
		ret.data[6] = A.data[6] + scalar*b.z*c.x;
		ret.data[7] = A.data[7] + scalar*b.z*c.y;
		ret.data[8] = A.data[8] + scalar*b.z*c.z;

		return ret;
	}

	/**
	 * <p>
	 * Computes the inner matrix product: ret = a'*M*b<br>
	 * where ret is a scalar number. 'a' and 'b' are automatically converted into homogeneous
	 * coordinates.
	 * </p>
	 *
	 * @param a 2D point.
	 * @param M 3 by 3 matrix.
	 * @param b 2D point.
	 * @return scalar number,
	 */
	public static double innerProd( GeoTuple2D_F64 a, DMatrixRMaj M, GeoTuple2D_F64 b ) {
		if( M.numRows != 3 || M.numCols != 3 )
			throw new IllegalArgumentException( "M must be 3 by 3." );

		// a^T*M
		double am0 = a.x*M.data[0] + a.y*M.data[3] + M.data[6];
		double am1 = a.x*M.data[1] + a.y*M.data[4] + M.data[7];
		double am2 = a.x*M.data[2] + a.y*M.data[5] + M.data[8];

		// right side
		return am0*b.x + am1*b.y + am2;
	}

	/**
	 * <p>
	 * Dot product: ret = a<sup>T</sup>b
	 * </p>
	 *
	 * @param a A tuple.
	 * @param b A tuple.
	 * @return scalar
	 */
	public static double dot( GeoTuple3D_F64 a, GeoTuple3D_F64 b ) {
		return a.x*b.x + a.y*b.y + a.z*b.z;
	}

	/**
	 * <p>
	 * Multiplies each element in the tuple by 'v'.<br>
	 * p<sub>i</sub>=p<sub>i</sub>*v
	 * </p>
	 *
	 * @param p tuple.
	 * @param v scaling factor.
	 */
	public static void scale( GeoTuple3D_F64 p, double v ) {
		p.x *= v;
		p.y *= v;
		p.z *= v;
	}

	/**
	 * Divides each element by 'v'
	 *
	 * @param p tuple
	 * @param v divisor
	 */
	public static void divide( GeoTuple3D_F64 p , double v ) {
		p.x /= v;
		p.y /= v;
		p.z /= v;
	}

	/**
	 * <p>
	 * Changes the sign of the vector:<br>
	 * <br>
	 * T = -T
	 * </p>
	 *
	 * @param t Vector whose sign is being changed. Modified.
	 */
	public static void changeSign( GeoTuple3D_F64 t ) {
		t.x = -t.x;
		t.y = -t.y;
		t.z = -t.z;
	}

	/**
	 * Converts a GeoTuple3D_F64 into DMatrixRMaj
	 *
	 * @param in Input vector
	 * @param out Output matrix. If null a new matrix will be declared
	 * @return Converted matrix
	 */
	public static DMatrixRMaj toMatrix(GeoTuple3D_F64 in, @Nullable DMatrixRMaj out) {
		if( out == null )
			out = new DMatrixRMaj(3,1);
		else if( out.getNumElements() != 3 )
			throw new IllegalArgumentException("Vector with 3 elements expected");

		out.data[0] = in.x;
		out.data[1] = in.y;
		out.data[2] = in.z;

		return out;
	}

	/**
	 * Converts a DMatrixRMaj into  GeoTuple3D_F64
	 *
	 * @param in Input matrix
	 * @param out Output vector.
	 */
	public static void toTuple3D(DMatrixRMaj in, GeoTuple3D_F64 out) {

		out.x = (double)in.get(0);
		out.y = (double)in.get(1);
		out.z = (double)in.get(2);
	}
}
