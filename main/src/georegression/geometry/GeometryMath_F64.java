/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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
import georegression.struct.point.Vector3D_F64;
import georegression.struct.so.Quaternion_F64;
import org.ejml.data.DGrowArray;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.MatrixFeatures_DDRM;
import org.ejml.dense.row.SingularOps_DDRM;
import org.jetbrains.annotations.Nullable;

/**
 * Math operations that can be applied to geometric primitives.
 *
 * @author Peter Abeles
 */
// TODO rename to PerspectiveMath?
// todo separate off functions that are in homogeneous coordinates into their own class?
//      alternatively indicate by the function name?
// todo make sure all functions have unit tests
@SuppressWarnings({"unchecked", "RedundantCast", "rawtypes"})
public class GeometryMath_F64 {

	/**
	 * Creates a 3x3 skew symmetric cross product matrix from the provided tuple.
	 *
	 * @param x0 Element 0.
	 * @param x1 Element 1.
	 * @param x2 Element 2.
	 * @param ret If not null the results are stored here, otherwise a new matrix is created.
	 * @return Skew symmetric cross product matrix.
	 */
	public static DMatrixRMaj crossMatrix( double x0, double x1, double x2, @Nullable DMatrixRMaj ret ) {
		if (ret == null) {
			ret = new DMatrixRMaj(3, 3);
		} else {
			ret.reshape(3, 3);
			ret.zero();
		}

		ret.set(0, 1, -x2);
		ret.set(0, 2, x1);
		ret.set(1, 0, x2);
		ret.set(1, 2, -x0);
		ret.set(2, 0, -x1);
		ret.set(2, 1, x0);

		return ret;
	}

	/**
	 * Creates a 3x3 skew symmetric cross product matrix from the provided tuple.
	 *
	 * @param v Tuple. Not modified.
	 * @param ret If not null the results are stored here, otherwise a new matrix is created.
	 * @return Skew symmetric cross product matrix.
	 */
	public static DMatrixRMaj crossMatrix( GeoTuple3D_F64 v, @Nullable DMatrixRMaj ret ) {
		if (ret == null) {
			ret = new DMatrixRMaj(3, 3);
		} else {
			ret.reshape(3, 3);
			ret.zero();
		}

		double x = v.getX();
		double y = v.getY();
		double z = v.getZ();

		ret.set(0, 1, -z);
		ret.set(0, 2, y);
		ret.set(1, 0, z);
		ret.set(1, 2, -x);
		ret.set(2, 0, -y);
		ret.set(2, 1, x);

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
	 * @param c Modified. Must not be the same instance as a or b.
	 */
	public static void cross( GeoTuple3D_F64 a, GeoTuple3D_F64 b, GeoTuple3D_F64 c ) {
		c.x = a.y*b.z - a.z*b.y;
		c.y = a.z*b.x - a.x*b.z;
		c.z = a.x*b.y - a.y*b.x;
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
	public static void cross( double a_x, double a_y, double a_z,
	                          double b_x, double b_y, double b_z,
	                          GeoTuple3D_F64 c ) {
		c.x = a_y*b_z - a_z*b_y;
		c.y = a_z*b_x - a_x*b_z;
		c.z = a_x*b_y - a_y*b_x;
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
		c.x = a.y*b.z - b.y;
		c.y = b.x - a.x*b.z;
		c.z = a.x*b.y - a.y*b.x;
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
		c.x = a.y*1 - b.y;
		c.y = b.x - a.x;
		c.z = a.x*b.y - a.y*b.x;
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
		pt2.x = a0*pt0.x + a1*pt1.x;
		pt2.y = a0*pt0.y + a1*pt1.y;
		pt2.z = a0*pt0.z + a1*pt1.z;
	}

	/**
	 * <p>ret = p0 + M*p1</p>
	 *
	 * Safe to pass in the same instance of a point more than once.
	 */
	public static <T extends GeoTuple3D_F64> T addMult( T p0, DMatrixRMaj M, T p1, @Nullable T result ) {
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols);

		if (result == null) {
			result = (T)p0.createNewInstance();
		}

		double x = p1.x;
		double y = p1.y;
		double z = p1.z;

		result.x = p0.x + (double)(M.data[0]*x + M.data[1]*y + M.data[2]*z);
		result.y = p0.y + (double)(M.data[3]*x + M.data[4]*y + M.data[5]*z);
		result.z = p0.z + (double)(M.data[6]*x + M.data[7]*y + M.data[8]*z);

		return result;
	}

	/**
	 * <p>ret = p0 + M<sup>T</sup>*p1</p>
	 *
	 * Safe to pass in the same instance of a point more than once.
	 */
	public static <T extends GeoTuple3D_F64> T addMultTrans( T p0, DMatrixRMaj M, T p1, @Nullable T result ) {
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols);

		if (result == null) {
			result = (T)p0.createNewInstance();
		}

		double x = p1.x;
		double y = p1.y;
		double z = p1.z;

		result.x = p0.x + (double)(M.data[0]*x + M.data[3]*y + M.data[6]*z);
		result.y = p0.y + (double)(M.data[1]*x + M.data[4]*y + M.data[7]*z);
		result.z = p0.z + (double)(M.data[2]*x + M.data[5]*y + M.data[8]*z);

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
		double c = Math.cos(theta);
		double s = Math.sin(theta);

		double x = pt.x;
		double y = pt.y;

		solution.x = c*x - s*y;
		solution.y = s*x + c*y;
	}

	/**
	 * Rotates a 2D point by the specified angle.
	 *
	 * @param c Cosine of theta
	 * @param s Sine of theta
	 * @param solution where the solution is written to. Can be the same point as 'pt'.
	 */
	public static void rotate( double c, double s, GeoTuple2D_F64 pt, GeoTuple2D_F64 solution ) {

		double x = pt.x;
		double y = pt.y;

		solution.x = c*x - s*y;
		solution.y = s*x + c*y;
	}

	/**
	 * mod = M*pt
	 * <p>
	 * pt and mod can be the same reference.
	 * </p>
	 *
	 * @param result Storage for output. Can be the same instance as param 'pt'. Modified.
	 */
	public static <T extends GeoTuple3D_F64> T mult( DMatrixRMaj M, T pt, @Nullable T result ) {
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols);

		if (result == null) {
			result = (T)pt.createNewInstance();
		}

		double x = pt.x;
		double y = pt.y;
		double z = pt.z;

		result.x = (double)(M.data[0]*x + M.data[1]*y + M.data[2]*z);
		result.y = (double)(M.data[3]*x + M.data[4]*y + M.data[5]*z);
		result.z = (double)(M.data[6]*x + M.data[7]*y + M.data[8]*z);

		return (T)result;
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
		if (M.numRows != 4 || M.numCols != 4)
			throw new IllegalArgumentException("Input matrix must be 4 by 4, not " + M.numRows + " " + M.numCols);

		if (result == null) {
			result = (T)pt.createNewInstance();
		}

		double x = pt.x;
		double y = pt.y;
		double z = pt.z;

		result.x = (double)(M.data[0]*x + M.data[1]*y + M.data[2]*z + M.data[3]);
		result.y = (double)(M.data[4]*x + M.data[5]*y + M.data[6]*z + M.data[7]);
		result.z = (double)(M.data[8]*x + M.data[9]*y + M.data[10]*z + M.data[11]);
		double w = (double)(M.data[12]*x + M.data[13]*y + M.data[14]*z + M.data[15]);

		result.x /= w;
		result.y /= w;
		result.z /= w;

		return (T)result;
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
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols);

		double x = pt.x;
		double y = pt.y;
		double z = pt.z;

		mod.x = (double)(M.unsafe_get(0, 0)*x + M.unsafe_get(0, 1)*y + M.unsafe_get(0, 2)*z);
		mod.y = (double)(M.unsafe_get(1, 0)*x + M.unsafe_get(1, 1)*y + M.unsafe_get(1, 2)*z);
		z = (double)(M.unsafe_get(2, 0)*x + M.unsafe_get(2, 1)*y + M.unsafe_get(2, 2)*z);

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
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols);

		double x = pt.x;
		double y = pt.y;

		mod.x = (double)(M.unsafe_get(0, 0)*x + M.unsafe_get(0, 1)*y + M.unsafe_get(0, 2));
		mod.y = (double)(M.unsafe_get(1, 0)*x + M.unsafe_get(1, 1)*y + M.unsafe_get(1, 2));
		mod.z = (double)(M.unsafe_get(2, 0)*x + M.unsafe_get(2, 1)*y + M.unsafe_get(2, 2));
	}

	/**
	 * <p>
	 * Computes mod =  M*pt, where both pt and mod are in homogeneous coordinates with z assumed to be
	 * equal to 1, and M is a 3x3 matrix.
	 * </p>
	 * <p>
	 * 'pt' and 'mod' can be the same point.
	 * </p>
	 *
	 * @param M 3x3 matrix
	 * @param pt Homogeneous point with z=1
	 * @param mod Storage for the computation. If null a new point is declared. Can be same instance as pt.
	 * @return Result of computation.
	 */
	public static <T extends GeoTuple2D_F64> T mult( DMatrixRMaj M, T pt, @Nullable T mod ) {
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols);

		if (mod == null) {
			throw new IllegalArgumentException("Must provide an instance in mod");
		}

		double x = pt.x;
		double y = pt.y;

		double modz = (double)(M.unsafe_get(2, 0)*x + M.unsafe_get(2, 1)*y + M.unsafe_get(2, 2));

		mod.x = (double)((M.unsafe_get(0, 0)*x + M.unsafe_get(0, 1)*y + M.unsafe_get(0, 2))/modz);
		mod.y = (double)((M.unsafe_get(1, 0)*x + M.unsafe_get(1, 1)*y + M.unsafe_get(1, 2))/modz);

		return mod;
	}

	/**
	 * x = P*X
	 *
	 * @param P Projective 3x4 matrix or 3x3 matrix with implicit [0,0,0,1]' last column
	 * @param X 3D point in homogenous coordinates
	 * @param mod 2D point in homogenous coordinates. Can be same instance as X.
	 */
	public static void mult( DMatrixRMaj P, GeoTuple4D_F64 X, GeoTuple3D_F64 mod ) {
		final double x = X.x, y = X.y, z = X.z, w = X.w;

		if (P.numCols == 4) {
			mod.x = P.data[0]*x + P.data[1]*y + P.data[2]*z + P.data[3]*w;
			mod.y = P.data[4]*x + P.data[5]*y + P.data[6]*z + P.data[7]*w;
			mod.z = P.data[8]*x + P.data[9]*y + P.data[10]*z + P.data[11]*w;
		} else if (P.numCols == 3) {
			mod.x = P.data[0]*x + P.data[1]*y + P.data[2]*z;
			mod.y = P.data[3]*x + P.data[4]*y + P.data[5]*z;
			mod.z = P.data[6]*x + P.data[7]*y + P.data[8]*z;
		} else {
			throw new IllegalArgumentException("Input matrix must have 3 or 4 columns not " + P.numCols);
		}

		if (P.numRows == 4) {
			double ww = P.data[12]*x + P.data[13]*y + P.data[14]*z + P.data[15]*w;
			mod.x /= ww;
			mod.y /= ww;
			mod.z /= ww;
		} else if (P.numRows != 3) {
			throw new IllegalArgumentException("rows must be 3 or 4 and not " + P.numRows);
		}
	}

	/**
	 * x = P*X
	 *
	 * @param P 4x4 matrix or 3x3 with implicit [0,0,0,1] right column and bottom row
	 * @param X 3D point in homogenous coordinates
	 * @param mod 3D point in homogenous coordinates. Can be same instance as X.
	 */
	public static void mult( DMatrixRMaj P, GeoTuple4D_F64 X, GeoTuple4D_F64 mod ) {
		if (!MatrixFeatures_DDRM.isSquare(P))
			throw new RuntimeException("Expected P to be a square matrix");

		final double x = X.x, y = X.y, z = X.z, w = X.w;

		if (P.numRows == 4) {
			mod.x = P.data[0]*x + P.data[1]*y + P.data[2]*z + P.data[3]*w;
			mod.y = P.data[4]*x + P.data[5]*y + P.data[6]*z + P.data[7]*w;
			mod.z = P.data[8]*x + P.data[9]*y + P.data[10]*z + P.data[11]*w;
			mod.w = P.data[12]*x + P.data[13]*y + P.data[14]*z + P.data[15]*w;
		} else if (P.numCols == 3) {
			mod.x = P.data[0]*x + P.data[1]*y + P.data[2]*z;
			mod.y = P.data[3]*x + P.data[4]*y + P.data[5]*z;
			mod.z = P.data[6]*x + P.data[7]*y + P.data[8]*z;
			mod.w = w;
		} else {
			throw new IllegalArgumentException("Expected P to be a 3x3 or 4x4 matrix not " + P.numRows + "x" + P.numCols);
		}
	}

	/**
	 * x = P<sup>T</sup>*X
	 *
	 * @param P 4x4 matrix or 3x3 with implicit [0,0,0,1] right column and bottom row
	 * @param X 3D point in homogenous coordinates
	 * @param mod 3D point in homogenous coordinates. Can be same instance as X.
	 */
	public static void multTran( DMatrixRMaj P, GeoTuple4D_F64 X, GeoTuple4D_F64 mod ) {
		if (!MatrixFeatures_DDRM.isSquare(P))
			throw new RuntimeException("Expected P to be a square matrix");

		final double x = X.x, y = X.y, z = X.z, w = X.w;

		if (P.numRows == 4) {
			mod.x = P.data[0]*x + P.data[4]*y + P.data[8]*z + P.data[12]*w;
			mod.y = P.data[1]*x + P.data[5]*y + P.data[9]*z + P.data[13]*w;
			mod.z = P.data[2]*x + P.data[6]*y + P.data[10]*z + P.data[14]*w;
			mod.w = P.data[3]*x + P.data[7]*y + P.data[11]*z + P.data[15]*w;
		} else if (P.numCols == 3) {
			mod.x = P.data[0]*x + P.data[3]*y + P.data[6]*z;
			mod.y = P.data[1]*x + P.data[4]*y + P.data[7]*z;
			mod.z = P.data[2]*x + P.data[5]*y + P.data[8]*z;
			mod.w = w;
		} else {
			throw new IllegalArgumentException("Expected P to be a 3x3 or 4x4 matrix not " + P.numRows + "x" + P.numCols);
		}
	}

	/**
	 * x = (P*X)/z
	 *
	 * @param P Projective 3x4 matrix
	 * @param X 3D point in homogenous coordinates
	 * @param mod 2D point in coordinates
	 */
	public static void mult( DMatrixRMaj P, GeoTuple4D_F64 X, GeoTuple2D_F64 mod ) {
		if (P.numRows != 3 || P.numCols != 4)
			throw new IllegalArgumentException("Input matrix must be 3 by 4 not " + P.numRows + " " + P.numCols);

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
	public static DMatrixRMaj multCrossA( GeoTuple2D_F64 A, DMatrixRMaj M, @Nullable DMatrixRMaj result ) {
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols);

		if (result == null) {
			result = new DMatrixRMaj(3, 3);
		}

		double x = A.x;
		double y = A.y;

		double a11 = M.data[0], a12 = M.data[1], a13 = M.data[2];
		double a21 = M.data[3], a22 = M.data[4], a23 = M.data[5];
		double a31 = M.data[6], a32 = M.data[7], a33 = M.data[8];

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
	public static DMatrixRMaj multCrossATransA( GeoTuple2D_F64 A, DMatrixRMaj M, @Nullable DMatrixRMaj result ) {
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols);

		if (result == null) {
			result = new DMatrixRMaj(3, 3);
		}

		double x = A.x;
		double y = A.y;

		double a11 = M.data[0], a12 = M.data[1], a13 = M.data[2];
		double a21 = M.data[3], a22 = M.data[4], a23 = M.data[5];
		double a31 = M.data[6], a32 = M.data[7], a33 = M.data[8];

		result.data[0] = a21 - a31*y;
		result.data[1] = a22 - a32*y;
		result.data[2] = a23 - a33*y;
		result.data[3] = -a11 + a31*x;
		result.data[4] = -a12 + a32*x;
		result.data[5] = -a13 + a33*x;
		result.data[6] = a11*y - a21*x;
		result.data[7] = a12*y - a22*x;
		result.data[8] = a13*y - a23*x;

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
	public static DMatrixRMaj multCrossA( GeoTuple3D_F64 A, DMatrixRMaj M, @Nullable DMatrixRMaj result ) {
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols);

		if (result == null) {
			result = new DMatrixRMaj(3, 3);
		}

		double x = A.x;
		double y = A.y;
		double z = A.z;

		double a11 = M.data[0], a12 = M.data[1], a13 = M.data[2];
		double a21 = M.data[3], a22 = M.data[4], a23 = M.data[5];
		double a31 = M.data[6], a32 = M.data[7], a33 = M.data[8];

		result.data[0] = -a21*z + a31*y;
		result.data[1] = -a22*z + a32*y;
		result.data[2] = -a23*z + a33*y;
		result.data[3] = a11*z - a31*x;
		result.data[4] = a12*z - a32*x;
		result.data[5] = a13*z - a33*x;
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
	public static DMatrixRMaj multCrossATransA( GeoTuple3D_F64 A, DMatrixRMaj M, @Nullable DMatrixRMaj result ) {
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Input matrix must be 3 by 3, not " + M.numRows + " " + M.numCols);

		if (result == null) {
			result = new DMatrixRMaj(3, 3);
		}

		double x = A.x;
		double y = A.y;
		double z = A.z;

		double a11 = M.data[0], a12 = M.data[1], a13 = M.data[2];
		double a21 = M.data[3], a22 = M.data[4], a23 = M.data[5];
		double a31 = M.data[6], a32 = M.data[7], a33 = M.data[8];

		result.data[0] = a21*z - a31*y;
		result.data[1] = a22*z - a32*y;
		result.data[2] = a23*z - a33*y;
		result.data[3] = -a11*z + a31*x;
		result.data[4] = -a12*z + a32*x;
		result.data[5] = -a13*z + a33*x;
		result.data[6] = a11*y - a21*x;
		result.data[7] = a12*y - a22*x;
		result.data[8] = a13*y - a23*x;

		return result;
	}

	/**
	 * mod = M<sup>T</sup>*pt. Both pt and mod can be the same instance.
	 */
	public static <T extends GeoTuple3D_F64> T multTran( DMatrixRMaj M, T pt, @Nullable T mod ) {
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Rotation matrices are 3 by 3.");

		if (mod == null) {
			mod = (T)pt.createNewInstance();
		}

		double x = pt.x;
		double y = pt.y;
		double z = pt.z;

		mod.x = (double)(M.unsafe_get(0, 0)*x + M.unsafe_get(1, 0)*y + M.unsafe_get(2, 0)*z);
		mod.y = (double)(M.unsafe_get(0, 1)*x + M.unsafe_get(1, 1)*y + M.unsafe_get(2, 1)*z);
		mod.z = (double)(M.unsafe_get(0, 2)*x + M.unsafe_get(1, 2)*y + M.unsafe_get(2, 2)*z);

		return (T)mod;
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
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Rotation matrices are 3 by 3.");

		double x = pt.x;
		double y = pt.y;

		mod.x = (double)(M.unsafe_get(0, 0)*x + M.unsafe_get(1, 0)*y + M.unsafe_get(2, 0));
		mod.y = (double)(M.unsafe_get(0, 1)*x + M.unsafe_get(1, 1)*y + M.unsafe_get(2, 1));
		mod.z = (double)(M.unsafe_get(0, 2)*x + M.unsafe_get(1, 2)*y + M.unsafe_get(2, 2));

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
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("Rotation matrices are 3 by 3.");

		double x = pt.x;
		double y = pt.y;

		double modZ = (double)(M.unsafe_get(0, 2)*x + M.unsafe_get(1, 2)*y + M.unsafe_get(2, 2));
		mod.x = (double)(M.unsafe_get(0, 0)*x + M.unsafe_get(1, 0)*y + M.unsafe_get(2, 0))/modZ;
		mod.y = (double)(M.unsafe_get(0, 1)*x + M.unsafe_get(1, 1)*y + M.unsafe_get(2, 1))/modZ;

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
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("M must be 3 by 3.");

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
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("M must be 3 by 3.");

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
	public static DMatrixRMaj outerProd( GeoTuple3D_F64 a, GeoTuple3D_F64 b,
	                                     @Nullable DMatrixRMaj ret ) {
		if (ret == null)
			ret = new DMatrixRMaj(3, 3);

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
	public static DMatrixRMaj addOuterProd( DMatrixRMaj A, double scalar, GeoTuple3D_F64 b, GeoTuple3D_F64 c,
	                                        @Nullable DMatrixRMaj ret ) {
		if (ret == null)
			ret = new DMatrixRMaj(3, 3);

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
		if (M.numRows != 3 || M.numCols != 3)
			throw new IllegalArgumentException("M must be 3 by 3.");

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
	public static void divide( GeoTuple3D_F64 p, double v ) {
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
	public static DMatrixRMaj toMatrix( GeoTuple3D_F64 in, @Nullable DMatrixRMaj out ) {
		if (out == null)
			out = new DMatrixRMaj(3, 1);
		else if (out.getNumElements() != 3)
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
	public static void toTuple3D( DMatrixRMaj in, GeoTuple3D_F64 out ) {
		out.x = (double)in.get(0);
		out.y = (double)in.get(1);
		out.z = (double)in.get(2);
	}

	/// Computes quaternion which will rotate vector 'a' into the same direction as 'b'.
	///
	/// @return Resulting rotation quaternion
	public static Quaternion_F64 quatFromTwoVectors(
			double ax, double ay, double az, double bx, double by, double bz,
			@Nullable Quaternion_F64 result ) {
		if (result == null)
			result = new Quaternion_F64();

		// Normalize the two input vectors
		double anorm = Math.sqrt(ax*ax + ay*ay + az*az);
		double bnorm = Math.sqrt(bx*bx + by*by + bz*bz);

		ax /= anorm;
		ay /= anorm;
		az /= anorm;

		bx /= bnorm;
		by /= bnorm;
		bz /= bnorm;

		// dot(a,b) = cos(theta)
		double dot = ax*bx + ay*by + az*bz;

		// Antiparallel case (180 deg rotation). Pick one axis.
		var axis = new Vector3D_F64();
		if (dot < -1.0 + 1e-10) {
			double perpX = 0, perpY = 0;
			if (Math.abs(ax) < 0.9) {
				perpX = 1;
			} else {
				perpY = 1;
			}
			cross(ax, ay, az, perpX, perpY, 0.0, axis);
			axis.normalize();
			return new Quaternion_F64(0, axis.x, axis.y, axis.z); // 180° rotation
		}

		// Cross product gives the rotation axis (scaled by sin(theta))
		cross(ax, ay, az, bx, by, bz, axis);

		// w = 1 + cos(theta)  (using the half-angle identity shortcut)
		result.setTo(1.0 + dot, axis.x, axis.y, axis.z);
		result.normalize();
		return result;
	}

	public static Quaternion_F64 quatFromTwoVectors( Vector3D_F64 a, Vector3D_F64 b, @Nullable Quaternion_F64 result ) {
		return quatFromTwoVectors(a.x, a.y, a.z, b.x, b.y, b.z, result);
	}

	/// Finds a rotation represented by a quaternion for rotation a into b using SVD. This implementation will
	/// be more computationally expensive, but select a solution (from the infinite number of valid solutions)
	/// which are less sensitive to noise.
	///
	/// Note: This formulation is taken from Eigen, but won't produce identical solutions because the SVD
	/// implementations are different.
	public static Quaternion_F64 quatFromTwoVectorsSvd( Vector3D_F64 a, Vector3D_F64 b, @Nullable Quaternion_F64 result ) {
		// Normalize inputs
		Vector3D_F64 v0 = new Vector3D_F64(a).normalized();
		Vector3D_F64 v1 = new Vector3D_F64(b).normalized();

		double c = v0.dot(v1);

		// If dot == -1, vectors are nearly opposite
		// => accurately compute the rotation axis by computing the
		//    intersection of the two planes. This is done by solving:
		//       x^T v0 = 0
		//       x^T v1 = 0
		//    under the constraint:
		//       ||x|| = 1
		//    which yields a singular value problem
		if (c < -1.0 + 1e-10) {  // dummy_precision for double is ~1e-10
			c = Math.max(c, -1.0);

			// Build 2x3 matrix [v0; v1]
			var m = new DMatrixRMaj(new double[][]{
					{v0.x, v0.y, v0.z},
					{v1.x, v1.y, v1.z}
			});

			// SVD to find axis (last column of V). Note that V transpose is computed
			var Vt = new DMatrixRMaj(1, 1);
			SingularOps_DDRM.svd(m, null, new DGrowArray(), Vt);

			// Last row of V.transposed is the rotation axis
			var axis = new Vector3D_F64(
					Vt.get(1, 0),
					Vt.get(1, 1),
					Vt.get(1, 2));

			double w2 = (1.0 + c)*0.5;
			double w = Math.sqrt(w2);
			double s = Math.sqrt(1.0 - w2);

			// Quaternion_F64 is stored as w, x, y, z
			return new Quaternion_F64(w, axis.x*s, axis.y*s, axis.z*s);
		}

		// Normal case. axis = v0 x v1
		Vector3D_F64 axis = v0.crossWith(v1);

		double s = Math.sqrt((1.0 + c)*2.0);
		double invs = 1.0/s;

		// Quaternion_F64 is stored as w, x, y, z
		return new Quaternion_F64(s*0.5, axis.x*invs, axis.y*invs, axis.z*invs);
	}

	/// Computes 3x3 rotation which will rotate vector 'a' into the same direction as 'b'.
	///
	/// @return Resulting rotation matrix
	public static DMatrixRMaj rotationFromTwoVectors(
			double ax, double ay, double az, double bx, double by, double bz,
			@Nullable DMatrixRMaj result ) {
		if (result == null)
			result = new DMatrixRMaj(3, 3);
		result.reshape(3, 3);

		// Convert inputs into a unit vector
		double anorm = Math.sqrt(ax*ax + ay*ay + az*az);
		double bnorm = Math.sqrt(bx*bx + by*by + bz*bz);

		ax /= anorm;
		ay /= anorm;
		az /= anorm;

		bx /= bnorm;
		by /= bnorm;
		bz /= bnorm;

		// c = dot(a,b) = cos(theta)
		double c = ax*bx + ay*by + az*bz;

		// Antiparallel case (180 deg rotation). Pick a vector that is perpendicular to a
		if (c < -1.0 + 1e-10) {

			// Pick a vector which is perpendicular. We need to make sure we use an axis which isn't zero
			double absX = Math.abs(ax), absY = Math.abs(ay), absZ = Math.abs(az);
			if (absX <= absY && absX <= absZ) {
				bx = 0;   by = -az; bz = ay;
			} else if (absY <= absZ) {
				bx = -az; by = 0;   bz = ax;
			} else {
				bx = -ay; by = ax;  bz = 0;
			}

			bnorm = Math.sqrt(bx*bx + by*by + bz*bz);
			bx /= bnorm;
			by /= bnorm;
			bz /= bnorm;

			// cross(a, b)
			double vx = ay*bz - az*by;
			double vy = az*bx - ax*bz;
			double vz = ax*by - ay*bx;

			// 180 deg rotation matrix around axis a: R = 2*v*v - I
			result.set(0, 0, 2*vx*vx - 1);
			result.set(0, 1, 2*vx*vy);
			result.set(0, 2, 2*vx*vz);
			result.set(1, 0, 2*vy*vx);
			result.set(1, 1, 2*vy*vy - 1);
			result.set(1, 2, 2*vy*vz);
			result.set(2, 0, 2*vz*vx);
			result.set(2, 1, 2*vz*vy);
			result.set(2, 2, 2*vz*vz - 1);
			return result;
		}

		// cross(a, b)
		double vx = ay*bz - az*by;
		double vy = az*bx - ax*bz;
		double vz = ax*by - ay*bx;

		double k = 1.0/(1.0 + c);

		// Rodrigues rotation formula in matrix form
		// R = I + [v]x + [v]x^2 + (1/(1+c))
		// [v]x is a skew-symmetric matrix

		result.set(0, 0, 1 + k*(vx*vx - 1 + c));
		result.set(0, 1, k*vx*vy - vz);
		result.set(0, 2, k*vx*vz + vy);
		result.set(1, 0, k*vx*vy + vz);
		result.set(1, 1, 1 + k*(vy*vy - 1 + c));
		result.set(1, 2, k*vy*vz - vx);
		result.set(2, 0, k*vx*vz - vy);
		result.set(2, 1, k*vy*vz + vx);
		result.set(2, 2, 1 + k*(vz*vz - 1 + c));

		return result;
	}

	public static DMatrixRMaj rotationFromTwoVectors( Vector3D_F64 a, Vector3D_F64 b, @Nullable DMatrixRMaj result ) {
		return rotationFromTwoVectors(a.x, a.y, a.z, b.x, b.y, b.z, result);
	}

	/// Picks an arbitrary unit vector `b` perpendicular to `a`:
	/// a · b = 0, |b| = 1.
	///
	/// The smallest-magnitude component of `a` is zeroed, the other two are
	/// swapped, and one is negated — producing a vector exactly perpendicular to
	/// `a` before normalization, with magnitude ≥ sqrt(2/3) for unit
	/// `a`. Ties resolve to the lowest-index component, so the output is
	/// deterministic.
	///
	/// @param a Not modified. Must be non-zero; need not be unit length.
	/// @param b (Optional) Storage for output. Modified; unit length on return.
	/// @return Perpendicular vector. Same instance as `b` if not null.
	public static <T extends GeoTuple3D_F64<T>> T pickPerpendicular( T a, @Nullable T b ) {
		if (b == null)
			b = a.createNewInstance();

		// @formatter:off
		// Pick the component with smallest magnitude, gives the largest |b|
		double absX = Math.abs(a.x), absY = Math.abs(a.y), absZ = Math.abs(a.z);
		if (absX <= absY && absX <= absZ) {
			// smallest is x: b = (0, -a.z, a.y), perpendicular by construction
			b.x = 0;   b.y = -a.z; b.z = a.y;
		} else if (absY <= absZ) {
			// smallest is y: b = (-a.z, 0, a.x)
			b.x = -a.z; b.y = 0;   b.z = a.x;
		} else {
			// smallest is z: b = (-a.y, a.x, 0)
			b.x = -a.y; b.y = a.x;  b.z = 0;
		}
		// @formatter:on

		b.divideIP(b.norm());

		return b;
	}

	/// Assigns the specified column in 3 by N matrix `a` to the values in `vector`
	public static void setColumn( int column, GeoTuple3D_F64<?> vector, DMatrixRMaj a ) {
		if (a.numRows != 3)
			throw new IllegalArgumentException("Matrix A must be 3 by n");
		if (column < 0 || a.numCols <= column)
			throw new IndexOutOfBoundsException("Requested column is out of bounds");

		a.unsafe_set(0, column, vector.x);
		a.unsafe_set(1, column, vector.y);
		a.unsafe_set(2, column, vector.z);
	}

	/// Assigns the specified row in M by 3 matrix `a` to the values in `vector`
	public static void setRow( int row, GeoTuple3D_F64<?> vector, DMatrixRMaj a ) {
		if (a.numCols != 3)
			throw new IllegalArgumentException("Matrix A must be m by 3");
		if (row < 0 || a.numRows <= row)
			throw new IndexOutOfBoundsException("Requested row is out of bounds");

		a.unsafe_set(row, 0, vector.x);
		a.unsafe_set(row, 1, vector.y);
		a.unsafe_set(row, 2, vector.z);
	}
}
