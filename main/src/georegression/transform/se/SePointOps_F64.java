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

package georegression.transform.se;

import georegression.geometry.GeometryMath_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Point4D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.se.Se2_F64;
import georegression.struct.se.Se3_F64;
import org.ejml.data.DMatrixRMaj;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Peter Abeles
 */
public class SePointOps_F64 {

	/**
	 * Applies a 2D special euclidean transform to the point and stores the results in another
	 * variable.
	 *
	 * @param se	 The transform.
	 * @param orig   Original point being transformed. Not modified.
	 * @param result Where the results are stored. Can be the same as orig. If null a new
	 *               instance is created. Modified.
	 * @return Transformed point.
	 */
	public static Point2D_F64 transform( Se2_F64 se, Point2D_F64 orig, @Nullable Point2D_F64 result ) {

		if( result == null ) {
			result = new Point2D_F64();
		}

		final double c = se.getCosineYaw();
		final double s = se.getSineYaw();

		// copy the values so that no errors happen if orig and result are the same instance
		double x = orig.x;
		double y = orig.y;

		result.x = se.getX() + x * c - y * s;
		result.y = se.getY() + x * s + y * c;

		return result;
	}

	public static Point2D_F64 transform( Se2_F64 se, double x, double y, @Nullable Point2D_F64 result ) {

		if( result == null ) {
			result = new Point2D_F64();
		}

		final double c = se.getCosineYaw();
		final double s = se.getSineYaw();

		result.x = se.getX() + x * c - y * s;
		result.y = se.getY() + x * s + y * c;

		return result;
	}

	public static Point2D_F64 transformReverse( Se2_F64 se, Point2D_F64 orig, @Nullable Point2D_F64 result ) {

		if( result == null ) {
			result = new Point2D_F64();
		}

		final double c = se.getCosineYaw();
		final double s = se.getSineYaw();

		// copy the values so that no errors happen if orig and result are the same instance
		double x = orig.x - se.getX();
		double y = orig.y - se.getY();


		result.x = x * c + y * s;
		result.y = -x * s + y * c;

		return result;
	}

	/**
	 * Applies a 2D special euclidean transform to an array of points.
	 *
	 * @param se	 The transform.
	 * @param points Array of points which are to be transformed. Modified.
	 * @param length The number of elements in the array that are to be processed.
	 */
	public static void transform(Se2_F64 se, Point2D_F64[] points, int length ) {

		double tranX = se.getX();
		double tranY = se.getY();

		final double c = se.getCosineYaw();
		final double s = se.getSineYaw();

		for( int i = 0; i < length; i++ ) {
			Point2D_F64 pt = points[i];

			double x = pt.x;
			double y = pt.y;

			pt.x = tranX + x * c - y * s;
			pt.y = tranY + x * s + y * c;
		}
	}

	/**
	 * Applies a 2D special euclidean transform to a list of points.
	 *
	 * @param se	 The transform.
	 * @param points List of points which are to be transformed. Modified.
	 */
	public static void transform( Se2_F64 se, List<Point2D_F64> points ) {

		double tranX = se.getX();
		double tranY = se.getY();

		final double c = se.getCosineYaw();
		final double s = se.getSineYaw();

		for( Point2D_F64 pt : points ) {
			double x = pt.x;
			double y = pt.y;

			pt.x = tranX + x * c - y * s;
			pt.y = tranY + x * s + y * c;
		}
	}

	/**
	 * Applies a 3D special euclidean transform to a list of points.
	 *
	 * @param se	 The transform.
	 * @param points List of points which are to be transformed. Modified.
	 */
	public static void transform( Se3_F64 se, Point3D_F64[] points , int start , int length ) {
		for (int i = 0; i < length; i++) {
			Point3D_F64 p = points[i+start];
			transform(se,p,p);
		}
	}

	/**
	 * Applies a 3D special euclidean transform to a list of points.
	 *
	 * @param se	 The transform.
	 * @param points List of points which are to be transformed. Modified.
	 */
	public static void transform( Se3_F64 se, List<Point3D_F64> points ) {
		for( Point3D_F64 p : points ) {
			transform(se,p,p);
		}
	}

	/**
	 * <p>.
	 * Applies the transform specified by SpecialEuclidean to a point.<br>
	 * <br>
	 * p' = R*p + T
	 * </p>
	 * <p>
	 * Both origPt and tranPt can be the same instance.
	 * </p>
	 *
	 * @param se SpecialEuclidean transform. Not modified.
	 * @param src Original coordinate of the point. Not modified.
	 * @param dst Storage for transformed coordinate of the point. Point declared if null. Modified.
	 * @return Transformed point.
	 */
	public static Point3D_F64 transform( Se3_F64 se, Point3D_F64 src, @Nullable Point3D_F64 dst ) {
		return transform(se,src.x, src.y, src.z, dst);
	}

	public static Point3D_F64 transform( Se3_F64 se, double x , double y, double z, @Nullable Point3D_F64 dst ) {
		if( dst == null )
			dst = new Point3D_F64();

		DMatrixRMaj R = se.getR();
		Vector3D_F64 T = se.getT();

		dst.x = R.data[0]*x + R.data[1]*y + R.data[2]*z + T.x;
		dst.y = R.data[3]*x + R.data[4]*y + R.data[5]*z + T.y;
		dst.z = R.data[6]*x + R.data[7]*y + R.data[8]*z + T.z;

		return dst;
	}

	/**
	 * <p>.
	 * Applies rigid body motion to a point. The homogeneous coordinate is converted into a cartesian coordinate.
	 * This will not handle points at infinity. A NaN will be produced.<br>
	 * <br>
	 * p' = [R t]*p
	 * </p>
	 * <p>
	 * Both origPt and tranPt can be the same instance.
	 * </p>
	 *
	 * @param se SpecialEuclidean transform. Not modified.
	 * @param src Original coordinate of the point. Not modified.
	 * @param dst Storage for transformed coordinate of the point. Point declared if null. Modified.
	 * @return Transformed point.
	 */
	public static Point3D_F64 transform(Se3_F64 se, Point4D_F64 src, @Nullable Point3D_F64 dst ) {
		return transform(se, src.x, src.y, src.z, src.w, dst);
	}

	public static Point3D_F64 transform(Se3_F64 se,
										final double x, final double y, final double z, final double w,
										@Nullable Point3D_F64 dst ) {
		if( dst == null )
			dst = new Point3D_F64();

		DMatrixRMaj R = se.R;
		Vector3D_F64 T = se.T;

		// [R T]*X
		dst.x = R.data[0]*x + R.data[1]*y + R.data[2]*z + T.x*w;
		dst.y = R.data[3]*x + R.data[4]*y + R.data[5]*z + T.y*w;
		dst.z = R.data[6]*x + R.data[7]*y + R.data[8]*z + T.z*w;

		dst.x /= w;
		dst.y /= w;
		dst.z /= w;

		return dst;
	}

	/**
	 * Applies the transform to src, but omits the last implicit last row in dst where dst.w = src.w
	 */
	public static Point3D_F64 transformV(Se3_F64 se, Point4D_F64 src, @Nullable Point3D_F64 dst ) {
		return transformV(se,src.x, src.y, src.z, src.w, dst);
	}

	public static Point3D_F64 transformV(Se3_F64 se,
										 final double x, final double y, final double z, final double w,
										 @Nullable Point3D_F64 dst ) {
		if( dst == null )
			dst = new Point3D_F64();

		DMatrixRMaj R = se.getR();
		Vector3D_F64 T = se.getT();

		dst.x = R.data[0]*x + R.data[1]*y + R.data[2]*z + T.x*w;
		dst.y = R.data[3]*x + R.data[4]*y + R.data[5]*z + T.y*w;
		dst.z = R.data[6]*x + R.data[7]*y + R.data[8]*z + T.z*w;

		return dst;
	}

	/**
	 * <p>.
	 * Applies the transform specified by SpecialEuclidean to a homogenous point.<br>
	 * <br>
	 * p' = [R t]*p
	 * </p>
	 * <p>
	 * Both origPt and tranPt can be the same instance.
	 * </p>
	 *
	 * @param se SpecialEuclidean transform. Not modified.
	 * @param src Original coordinate of the point. Not modified.
	 * @param dst Storage for transformed coordinate of the point. Point declared if null. Modified.
	 * @return Transformed point.
	 */
	public static Point4D_F64 transform(Se3_F64 se, Point4D_F64 src, @Nullable Point4D_F64 dst ) {
		return transform(se, src.x, src.y, src.z ,src.w, dst);
	}

	public static Point4D_F64 transform(Se3_F64 se,
										final double x, final double y, final double z, final double w,
										@Nullable Point4D_F64 dst ) {
		if( dst == null )
			dst = new Point4D_F64();

		DMatrixRMaj R = se.getR();
		Vector3D_F64 T = se.getT();

		double P11 = R.data[0], P12 = R.data[1], P13 = R.data[2], P14 = T.x;
		double P21 = R.data[3], P22 = R.data[4], P23 = R.data[5], P24 = T.y;
		double P31 = R.data[6], P32 = R.data[7], P33 = R.data[8], P34 = T.z;
		// the bottom row is implicitly [0 0 0 1]

		dst.x = P11*x + P12*y + P13*z + P14*w;
		dst.y = P21*x + P22*y + P23*z + P24*w;
		dst.z = P31*x + P32*y + P33*z + P34*w;
		dst.w = w;

		return dst;
	}

	/**
	 * <p>.
	 * Applies the transform in the reverse direction<br>
	 * <br>
	 * p = R<sup>T</sup>*(p'-T)
	 * </p>
	 * <p>
	 * Both origPt and tranPt can be the same instance.
	 * </p>
	 *
	 * @param se	 SpecialEuclidean transform.
	 * @param origPt Original coordinate of the point.
	 * @param tranPt Transformed coordinate of the point.
	 */
	public static Point3D_F64 transformReverse( Se3_F64 se, Point3D_F64 origPt, @Nullable Point3D_F64 tranPt ) {
		if( tranPt == null )
			tranPt = new Point3D_F64();

		DMatrixRMaj R = se.getR();
		Vector3D_F64 T = se.getT();

		GeometryMath_F64.sub( origPt, T, tranPt );
		GeometryMath_F64.multTran( R, tranPt, tranPt );

		return tranPt;
	}
}
