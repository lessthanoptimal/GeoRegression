/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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

package georegression.transform.homo;

import georegression.struct.homo.Homography2D_F32;
import georegression.struct.homo.Homography2D_F64;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point2D_F64;


/**
 * @author Peter Abeles
 */
public class HomographyPointOps {

	/**
	 * Applies a 2D affine transform to the point and stores the results in another
	 * variable.
	 *
	 * @param se	 The transform.
	 * @param orig   Original point being transformed. Not modified.
	 * @param result Where the results are stored.  Can be the same as orig. If null a new
	 *               instance is created. Modified.
	 * @return Transformed point.
	 */
	public static Point2D_F64 transform( Homography2D_F64 se, Point2D_F64 orig, Point2D_F64 result ) {

		if( result == null ) {
			result = new Point2D_F64();
		}

		// copy the values so that no errors happen if orig and result are the same instance
		double x = orig.x;
		double y = orig.y;

		double z = se.a31 * x + se.a32 * y + se.a33;

		result.x = (se.a11 * x + se.a12 * y + se.a13)/z;
		result.y = (se.a21 * x + se.a22 * y + se.a23)/z;

		return result;
	}

	/**
	 * Applies a 2D affine transform to the point and stores the results in another
	 * variable.
	 *
	 * @param se	 The transform.
	 * @param orig   Original point being transformed. Not modified.
	 * @param result Where the results are stored.  Can be the same as orig. If null a new
	 *               instance is created. Modified.
	 * @return Transformed point.
	 */
	public static Point2D_F32 transform( Homography2D_F32 se, Point2D_F32 orig, Point2D_F32 result ) {

		if( result == null ) {
			result = new Point2D_F32();
		}

		// copy the values so that no errors happen if orig and result are the same instance
		float x = orig.x;
		float y = orig.y;

		float z = se.a31 * x + se.a32 * y + se.a33;

		result.x = (se.a11 * x + se.a12 * y + se.a13)/z;
		result.y = (se.a21 * x + se.a22 * y + se.a23)/z;

		return result;
	}
}
