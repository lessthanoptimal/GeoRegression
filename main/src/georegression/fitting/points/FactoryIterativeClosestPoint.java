/*
 * Copyright (C)  2020, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.points;

import georegression.fitting.se.MotionSe2PointSVD_F32;
import georegression.fitting.se.MotionSe2PointSVD_F64;
import georegression.fitting.se.MotionSe3PointSVD_F32;
import georegression.fitting.se.MotionSe3PointSVD_F64;
import georegression.helper.KdTreePoint2D_F32;
import georegression.helper.KdTreePoint2D_F64;
import georegression.helper.KdTreePoint3D_F32;
import georegression.helper.KdTreePoint3D_F64;
import georegression.misc.StoppingCondition;
import georegression.struct.GeoTuple2D_F32;
import georegression.struct.GeoTuple2D_F64;
import georegression.struct.GeoTuple3D_F32;
import georegression.struct.GeoTuple3D_F64;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se2_F32;
import georegression.struct.se.Se2_F64;
import georegression.struct.se.Se3_F32;
import georegression.struct.se.Se3_F64;
import org.ddogleg.nn.FactoryNearestNeighbor;
import org.ddogleg.nn.NearestNeighbor;

/**
 * @author Peter Abeles
 */
public class FactoryIterativeClosestPoint {
	/**
	 * 2D {@link georegression.struct.se.Se2_F32 rigid-body} {@link IterativeClosestPoint}
	 * based {@link MatchCloudToCloud}.
	 *
	 * @param maxDistance Maximum Euclidean distance two points will be matched up.
	 * @param stop Iteration stopping criteria
	 * @return ICP based cloud matching
	 */
	public static MatchCloudToCloud<Se2_F32, Point2D_F32> cloudIcp2D_F32(double maxDistance, StoppingCondition stop)  {
		NearestNeighbor<Point2D_F32> nn = FactoryNearestNeighbor.kdtree(new KdTreePoint2D_F32());
		return new SE2_F32(nn,maxDistance*maxDistance,stop);
	}

	/**
	 * 2D {@link georegression.struct.se.Se2_F64 rigid-body} {@link IterativeClosestPoint}
	 * based {@link MatchCloudToCloud}.
	 *
	 * @param maxDistance Maximum Euclidean distance two points will be matched up.
	 * @param stop Iteration stopping criteria
	 * @return ICP based cloud matching
	 */
	public static MatchCloudToCloud<Se2_F64, Point2D_F64> cloudIcp2D_F64(double maxDistance, StoppingCondition stop)  {
		NearestNeighbor<Point2D_F64> nn = FactoryNearestNeighbor.kdtree(new KdTreePoint2D_F64());
		return new SE2_F64(nn,maxDistance*maxDistance,stop);
	}

	/**
	 * 3D {@link georegression.struct.se.Se3_F32 rigid-body} {@link IterativeClosestPoint}
	 * based {@link MatchCloudToCloud}.
	 *
	 * @param maxDistance Maximum Euclidean distance two points will be matched up.
	 * @param stop Iteration stopping criteria
	 * @return ICP based cloud matching
	 */
	public static MatchCloudToCloud<Se3_F32, Point3D_F32> cloudIcp3D_F32	(double maxDistance, StoppingCondition stop)  {
		NearestNeighbor<Point3D_F32> nn = FactoryNearestNeighbor.kdtree(new KdTreePoint3D_F32());
		return new SE3_F32(nn,maxDistance*maxDistance,stop);
	}

	/**
	 * 3D {@link georegression.struct.se.Se3_F64 rigid-body} {@link IterativeClosestPoint}
	 * based {@link MatchCloudToCloud}.
	 *
	 * @param maxDistance Maximum Euclidean distance two points will be matched up.
	 * @param stop Iteration stopping criteria
	 * @return ICP based cloud matching
	 */
	public static MatchCloudToCloud<Se3_F64, Point3D_F64> cloudIcp3D_F64(double maxDistance, StoppingCondition stop)  {
		NearestNeighbor<Point3D_F64> nn = FactoryNearestNeighbor.kdtree(new KdTreePoint3D_F64());
		return new SE3_F64(nn,maxDistance*maxDistance,stop);
	}

	/** Specialized implementation for rigid body 2D points */
	public static class SE2_F32 extends MatchCloudToCloudIcp<Se2_F32, Point2D_F32> {
		public SE2_F32(NearestNeighbor<Point2D_F32> nn, double maxDistanceSq, StoppingCondition stop) {
			super(new MotionSe2PointSVD_F32(), nn, GeoTuple2D_F32::distance2,maxDistanceSq, stop);
		}
	}

	/** Specialized implementation for rigid body 2D points */
	public static class SE2_F64 extends MatchCloudToCloudIcp<Se2_F64, Point2D_F64> {
		public SE2_F64(NearestNeighbor<Point2D_F64> nn, double maxDistanceSq, StoppingCondition stop) {
			super(new MotionSe2PointSVD_F64(), nn, GeoTuple2D_F64::distance2, maxDistanceSq, stop);
		}
	}

	/** Specialized implementation for rigid body 3D points */
	public static class SE3_F32 extends MatchCloudToCloudIcp<Se3_F32, Point3D_F32> {
		public SE3_F32(NearestNeighbor<Point3D_F32> nn, double maxDistanceSq, StoppingCondition stop) {
			super(new MotionSe3PointSVD_F32(), nn, GeoTuple3D_F32::distance2, maxDistanceSq, stop);
		}
	}

	/** Specialized implementation for rigid body 3D points */
	public static class SE3_F64 extends MatchCloudToCloudIcp<Se3_F64, Point3D_F64> {
		public SE3_F64(NearestNeighbor<Point3D_F64> nn, double maxDistanceSq, StoppingCondition stop) {
			super(new MotionSe3PointSVD_F64(), nn, GeoTuple3D_F64::distance2, maxDistanceSq, stop);
		}
	}
}
