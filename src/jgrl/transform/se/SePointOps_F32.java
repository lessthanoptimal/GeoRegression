/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
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

package jgrl.transform.se;

import jgrl.geometry.GeometryMath_F32;
import jgrl.struct.point.Point2D_F32;
import jgrl.struct.point.Point3D_F32;
import jgrl.struct.point.Vector3D_F32;
import jgrl.struct.se.Se2_F32;
import jgrl.struct.se.Se3_F32;
import org.ejml.data.DenseMatrix64F;

import java.util.List;

/**
 * @author Peter Abeles
 */
public class SePointOps_F32 {

	/**
	 * Applies a 2D special euclidean transform to the point and stores the results in another
	 * variable.
	 *
	 * @param se	 The transform.
	 * @param orig   Original point being transformed. Not modified.
	 * @param result Where the results are stored.  Can be the same as orig. If null a new
	 *               instance is created. Modified.
	 * @return Transformed point.
	 */
	public static Point2D_F32 transform( Se2_F32 se, Point2D_F32 orig, Point2D_F32 result ) {

		if( result == null ) {
			result = new Point2D_F32();
		}

		final float c = se.getCosineYaw();
		final float s = se.getSineYaw();

		// copy the values so that no errors happen if orig and result are the same instance
		float x = orig.x;
		float y = orig.y;

		result.x = se.getX() + x * c - y * s;
		result.y = se.getY() + x * s + y * c;

		return result;
	}

	public static Point2D_F32 transform( Se2_F32 se, float x, float y, Point2D_F32 result ) {

		if( result == null ) {
			result = new Point2D_F32();
		}

		final float c = se.getCosineYaw();
		final float s = se.getSineYaw();

		result.x = se.getX() + x * c - y * s;
		result.y = se.getY() + x * s + y * c;

		return result;
	}

	public static Point2D_F32 transformReverse( Se2_F32 se, Point2D_F32 orig, Point2D_F32 result ) {

		if( result == null ) {
			result = new Point2D_F32();
		}

		final float c = se.getCosineYaw();
		final float s = se.getSineYaw();

		// copy the values so that no errors happen if orig and result are the same instance
		float x = orig.x - se.getX();
		float y = orig.y - se.getY();


		result.x = x * c + y * s;
		result.y = -x * s + y * c;

		return result;
	}

	/**
	 * Applies a 2D special euclidean transform to an array of points.
	 *
	 * @param se	 The transform.
	 * @param points Array of points which are to be transformed.  Modified.
	 * @param length The number of elements in the array that are to be processed.
	 */
	public static void transform( Se2_F32 se, Point2D_F32 points[], int length ) {

		float tranX = se.getX();
		float tranY = se.getY();

		final float c = se.getCosineYaw();
		final float s = se.getSineYaw();

		for( int i = 0; i < length; i++ ) {
			Point2D_F32 pt = points[i];

			float x = pt.x;
			float y = pt.y;

			pt.x = tranX + x * c - y * s;
			pt.y = tranY + x * s + y * c;
		}
	}

	/**
	 * Applies a 2D special euclidean transform to a list of points.
	 *
	 * @param se	 The transform.
	 * @param points List of points which are to be transformed.  Modified.
	 */
	public static void transform( Se2_F32 se, List<Point2D_F32> points ) {

		float tranX = se.getX();
		float tranY = se.getY();

		final float c = se.getCosineYaw();
		final float s = se.getSineYaw();

		for( Point2D_F32 pt : points ) {
			float x = pt.x;
			float y = pt.y;

			pt.x = tranX + x * c - y * s;
			pt.y = tranY + x * s + y * c;
		}
	}

	/**
	 * <p>.
	 * Applies the transform specified by SpecialEuclidean to a point.<br>
	 * <br>
	 * p' = R*p + T
	 * </p>
	 * <p/>
	 * <p>
	 * Both origPt and tranPt can be the same instance.
	 * </p>
	 *
	 * @param se	 SpecialEuclidean transform. Not modified.
	 * @param origPt Original coordinate of the point. Not modified.
	 * @param tranPt Storage for transformed coordinate of the point. Point declared if null.  Modified.
	 * @return Transformed point.
	 */
	public static Point3D_F32 transform( Se3_F32 se, Point3D_F32 origPt, Point3D_F32 tranPt ) {
		if( tranPt == null )
			tranPt = new Point3D_F32();

		DenseMatrix64F R = se.getR();
		Vector3D_F32 T = se.getT();

		GeometryMath_F32.mult( R, origPt, tranPt );
		GeometryMath_F32.add( tranPt, T, tranPt );

		return tranPt;
	}

	/**
	 * <p>.
	 * Applies the transform in the reverse direction<br>
	 * <br>
	 * p = R<sup>T</sup>*(p'-T)
	 * </p>
	 * <p/>
	 * <p>
	 * Both origPt and tranPt can be the same instance.
	 * </p>
	 *
	 * @param se	 SpecialEuclidean transform.
	 * @param origPt Original coordinate of the point.
	 * @param tranPt Transformed coordinate of the point.
	 */
	public static Point3D_F32 transformReverse( Se3_F32 se, Point3D_F32 origPt, Point3D_F32 tranPt ) {
		if( tranPt == null )
			tranPt = new Point3D_F32();

		DenseMatrix64F R = se.getR();
		Vector3D_F32 T = se.getT();

		GeometryMath_F32.sub( origPt, T, tranPt );
		GeometryMath_F32.multTran( R, tranPt, tranPt );

		return tranPt;
	}
}
