/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.fitting.se;

import jgrl.fitting.MotionTransformPoint;
import jgrl.struct.point.Point2D_F64;
import jgrl.struct.point.Point3D_F64;
import jgrl.struct.se.Se2_F64;
import jgrl.struct.se.Se3_F64;

import java.util.List;


/**
 * Functions related to fitting {@link jgrl.struct.se.SpecialEuclidean} transform to a set
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
		return new MotionSe3PointCrossCovariance_F64();
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
