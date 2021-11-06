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

package georegression.fitting.points;

import georegression.fitting.MotionTransformPoint;
import georegression.misc.StoppingCondition;
import georegression.struct.GeoTuple;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se2_F64;
import georegression.struct.se.Se3_F64;
import georegression.transform.se.SePointOps_F64;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A straight forward implementation of the Iterative Closest Point (ICP) algorithm for 2D or 3D objects. ICP
 * works by finding a locally optimal rigid body transform that minimizes the error between a set of points
 * and a model. The model can be described in several different formats and is implement as an interface
 * {@link ClosestPointToModel}.
 * </p>
 * <p/>
 * <p>
 * While this implementation is primarily designed for simplicity, generic, and correctness, its performance will
 * be primarily determined by the efficiency of the ClosestPointToModel provided to it. This is especially
 * true for complex models with large number of points.
 * </p>
 *
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked","NullAway.Init"})
public class IterativeClosestPoint<SE extends InvertibleTransform, P extends GeoTuple> {

	// stopping condition
	private final StoppingCondition stop;

	// the mean squared error
	private double foundError;

	private ClosestPointToModel<P> model;
	private final MotionTransformPoint<SE, P> motion;
	private final Distance<P> distance;

	// transform from the original point location to their current one
	private SE foundModelToPoints;

	// number of points which were matched
	private int totalMatched;

	public IterativeClosestPoint(StoppingCondition stop,
								 MotionTransformPoint<SE, P> motion,
								 Distance<P> distance) {
		this.stop = stop.copy();
		this.motion = motion;
		this.distance = distance;
		foundModelToPoints = (SE)motion.getTransformSrcToDst().createInstance();
	}

	/**
	 * Mean square error between the model and the set of points after the optimal transformation has been found
	 */
	public double getFoundError() {
		return foundError;
	}

	/**
	 * Found rigid body transformation from model to points.
	 */
	public SE getPointsToModel() {
		return foundModelToPoints;
	}

	/**
	 * The model that the input points is being fitted against.
	 */
	public void setModel(ClosestPointToModel model) {
		this.model = model;
	}

	/**
	 * Computes the best fit transform
	 *
	 * @param points Points which are to matched to a model. Their state is modified to the optimal fit location.
	 */
	public boolean process(List<P> points) {
		foundModelToPoints.reset();
		if (points.isEmpty()) {
			return false;
		}

		int dof = points.get(0).getDimension();

		List<P> modelPts = new ArrayList<P>();
		List<P> dstPts = new ArrayList<P>();

		boolean first = true;
		stop.reset();
		totalMatched = 0;
		while (true) {
			// find correspondences
			modelPts.clear();
			dstPts.clear();
			for (P p : points) {
				P match = model.findClosestPoint(p);
				if (match != null) {
					modelPts.add(p);
					dstPts.add(match);
				}
			}

			totalMatched = points.size();

			// from the optimal transform
			if( !motion.process(modelPts, dstPts) ) {
				return false;
			}

			if (dof == 2) {
				transform2D((List<Point2D_F64>) points);
			} else if (dof == 3) {
				transform3D((List<Point3D_F64>) points);
			} else {
				throw new RuntimeException("Unknown dimension");
			}

			// sum up all the transforms up to this point
			if (first) {
				first = false;
				foundModelToPoints.setTo(motion.getTransformSrcToDst());
			} else {
				// the returned transform is the result of the sequence of transforms.
				foundModelToPoints = (SE) motion.getTransformSrcToDst().concat(foundModelToPoints, null);
			}

			// compute mean squared error
			foundError = computeMeanSquaredError(modelPts, dstPts);

			if (stop.isFinished(foundError))
				break;
		}

		return true;
	}

	private double computeMeanSquaredError(List<P> fromPts, List<P> toPts) {
		double error = 0;
		for (int i = 0; i < fromPts.size(); i++) {
			P a = fromPts.get(i);
			P b = toPts.get(i);

			error += distance.distance(a,b);
		}
		error /= fromPts.size();
		return error;
	}

	private void transform3D(List<Point3D_F64> points) {
		Se3_F64 m = (Se3_F64) motion.getTransformSrcToDst();

		for (Point3D_F64 p : points) {
			SePointOps_F64.transform(m, p, p);
		}
	}

	private void transform2D(List<Point2D_F64> points) {
		Se2_F64 m = (Se2_F64) motion.getTransformSrcToDst();

		for (Point2D_F64 p : points) {
			SePointOps_F64.transform(m, p, p);
		}
	}

	public int getTotalMatched() {
		return totalMatched;
	}

	public interface Distance<P> {
		double distance( P a, P b);
	}
}
