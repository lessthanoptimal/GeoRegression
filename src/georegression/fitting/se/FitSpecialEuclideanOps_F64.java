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

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se2_F64;
import georegression.struct.se.Se3_F64;

import java.util.List;


/**
 * Functions related to fitting {@link georegression.struct.se.SpecialEuclidean} transform to a set
 * of corresponding points.
 *
 * @author Peter Abeles
 */
public class FitSpecialEuclideanOps_F64 {

	/**
	 * Creates a {@link MotionTransformPoint} for finding a {@link Se2_F64} from two
	 * sets of 2D points.
	 *
	 * @return {@link MotionTransformPoint}.
	 */
	public static MotionTransformPoint<Se2_F64, Point2D_F64> fitPoints2D() {
		return new MotionSe2PointSVD_F64();
	}

	/**
	 * Creates a {@link MotionTransformPoint} for finding a {@link Se3_F64} from two
	 * sets of 3D points.
	 *
	 * @return {@link MotionTransformPoint}.
	 */
	public static MotionTransformPoint<Se3_F64, Point3D_F64> fitPoints3D() {
		return new MotionSe3PointSVD_F64();
	}

	/**
	 * Given two sets of corresponding points compute the {@link Se2_F64} transform
	 * which minimizes the difference between the two sets of points.
	 *
	 * @param from List of the points original location.
	 * @param to Observed location of the points after a transform has been applied.
	 * @return Found transform.
	 */
	public static Se2_F64 fitPoints2D( List<Point2D_F64> from, List<Point2D_F64> to ) {
		MotionTransformPoint<Se2_F64, Point2D_F64> alg = fitPoints2D();

		alg.process( from, to );

		return alg.getMotion();
	}

	/**
	 * Given two sets of corresponding points compute the {@link Se3_F64} transform
	 * which minimizes the difference between the two sets of points.
	 *
	 * @param from List of the points original location.
	 * @param to Observed location of the points after a transform has been applied.
	 * @return Found transform.
	 */
	public static Se3_F64 fitPoints3D( List<Point3D_F64> from, List<Point3D_F64> to ) {
		MotionTransformPoint<Se3_F64, Point3D_F64> alg = fitPoints3D();

		alg.process( from, to );

		return alg.getMotion();
	}
}
