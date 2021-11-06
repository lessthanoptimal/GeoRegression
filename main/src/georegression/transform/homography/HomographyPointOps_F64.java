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

package georegression.transform.homography;

import georegression.struct.homography.Homography2D_F64;
import georegression.struct.point.Point2D_F64;
import org.jetbrains.annotations.Nullable;


/**
 * Applies homography transform to 2D points.
 *
 * @author Peter Abeles
 */
public class HomographyPointOps_F64 {

	/**
	 * Applies a 2D homography transform to the point and stores the results in another
	 * variable. b = H*a, where 'a' is the input/orig point, 'b' is the output/result point, and 'H'
	 * is a homography from a to b.
	 *
	 * @param H Homography transform
	 * @param orig   Original point being transformed. Not modified.
	 * @param result Where the results are stored. Can be the same as orig. If null a new
	 *               instance is created. Modified.
	 * @return Transformed point.
	 */
	public static Point2D_F64 transform( Homography2D_F64 H, Point2D_F64 orig, @Nullable Point2D_F64 result ) {

		if( result == null ) {
			result = new Point2D_F64();
		}

		// copy the values so that no errors happen if orig and result are the same instance
		double x = orig.x;
		double y = orig.y;

		double z = H.a31 * x + H.a32 * y + H.a33;

		result.x = (H.a11 * x + H.a12 * y + H.a13)/z;
		result.y = (H.a21 * x + H.a22 * y + H.a23)/z;

		return result;
	}


	/**
	 * Applies a 2D homography transform to the point and stores the results in another
	 * variable. b = H*a, where 'a' is the input/orig point, 'b' is the output/result point, and 'H'
	 * is a homography from a to b.
	 *
	 * @param H Homography transform
	 * @param x  Original x-coordinate
	 * @param y  Original y-coordinate
	 * @param result Where the results are stored. Can be the same as orig. If null a new
	 *               instance is created. Modified.
	 * @return Transformed point.
	 */
	public static Point2D_F64 transform( Homography2D_F64 H, double x , double y ,
										 @Nullable Point2D_F64 result )
	{
		if( result == null ) {
			result = new Point2D_F64();
		}

		double z = H.a31 * x + H.a32 * y + H.a33;

		result.x = (H.a11 * x + H.a12 * y + H.a13)/z;
		result.y = (H.a21 * x + H.a22 * y + H.a23)/z;

		return result;
	}
}
