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

package georegression.transform.affine;

import georegression.struct.affine.Affine2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;


/**
 * @author Peter Abeles
 */
public class AffinePointOps_F64 {

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
	public static Vector2D_F64 transform( Affine2D_F64 se, Vector2D_F64 orig, Vector2D_F64 result ) {

		if( result == null ) {
			result = new Vector2D_F64();
		}

		// copy the values so that no errors happen if orig and result are the same instance
		double x = orig.x;
		double y = orig.y;

		result.x = se.a11 * x + se.a12 * y;
		result.y = se.a21 * x + se.a22 * y;

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
	public static Point2D_F64 transform( Affine2D_F64 se, Point2D_F64 orig, Point2D_F64 result ) {

		if( result == null ) {
			result = new Point2D_F64();
		}

		// copy the values so that no errors happen if orig and result are the same instance
		double x = orig.x;
		double y = orig.y;

		result.x = se.tx + se.a11 * x + se.a12 * y;
		result.y = se.ty + se.a21 * x + se.a22 * y;

		return result;
	}

	/**
	 * Applies a 2D affine transform to the point and stores the results in another
	 * variable.
	 *
	 * @param se	 The transform.
	 * @param x	  Original x-coordinate
	 * @param y	  Original y-coordinate
	 * @param result Where the results are stored.  Can be the same as orig. If null a new
	 *               instance is created. Modified.
	 * @return Transformed point.
	 */
	public static Point2D_F64 transform( Affine2D_F64 se, double x, double y, Point2D_F64 result ) {

		if( result == null ) {
			result = new Point2D_F64();
		}

		result.x = se.tx + se.a11 * x + se.a12 * y;
		result.y = se.ty + se.a21 * x + se.a22 * y;

		return result;
	}
}
