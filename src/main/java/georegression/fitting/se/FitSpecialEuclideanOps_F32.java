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

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.se.Se2_F32;
import georegression.struct.se.Se3_F32;

import java.util.List;


/**
 * Functions related to fitting {@link georegression.struct.se.SpecialEuclidean} transform to a set
 * of corresponding points.
 *
 * @author Peter Abeles
 */
public class FitSpecialEuclideanOps_F32 {

	/**
	 * Creates a {@link MotionTransformPoint} for finding a {@link Se2_F32} from two
	 * sets of 2D points.
	 *
	 * @return {@link MotionTransformPoint}.
	 */
	public static MotionTransformPoint<Se2_F32, Point2D_F32> fitPoints2D() {
		return new MotionSe2PointSVD_F32();
	}

	/**
	 * Creates a {@link MotionTransformPoint} for finding a {@link Se3_F32} from two
	 * sets of 3D points.
	 *
	 * @return {@link MotionTransformPoint}.
	 */
	public static MotionTransformPoint<Se3_F32, Point3D_F32> fitPoints3D() {
		return new MotionSe3PointSVD_F32();
	}

	/**
	 * Given two sets of corresponding points compute the {@link Se2_F32} transform
	 * which minimizes the difference between the two sets of points.
	 *
	 * @param from List of the points original location.
	 * @param to Observed location of the points after a transform has been applied.
	 * @return Found transform.
	 */
	public static Se2_F32 fitPoints2D( List<Point2D_F32> from, List<Point2D_F32> to ) {
		MotionTransformPoint<Se2_F32, Point2D_F32> alg = fitPoints2D();

		alg.process( from, to );

		return alg.getMotion();
	}

	/**
	 * Given two sets of corresponding points compute the {@link Se3_F32} transform
	 * which minimizes the difference between the two sets of points.
	 *
	 * @param from List of the points original location.
	 * @param to Observed location of the points after a transform has been applied.
	 * @return Found transform.
	 */
	public static Se3_F32 fitPoints3D( List<Point3D_F32> from, List<Point3D_F32> to ) {
		MotionTransformPoint<Se3_F32, Point3D_F32> alg = fitPoints3D();

		alg.process( from, to );

		return alg.getMotion();
	}
}
