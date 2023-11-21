/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

package georegression.fitting;

import georegression.fitting.cylinder.GenerateCylinderFromPointNormals_F64;
import georegression.fitting.cylinder.ModelManagerCylinder3D_F64;
import georegression.fitting.cylinder.PointNormalDistanceFromCylinder_F64;
import georegression.fitting.plane.*;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import org.ddogleg.fitting.modelset.DistanceFromModel;
import org.ddogleg.fitting.modelset.ModelGenerator;
import org.ddogleg.fitting.modelset.ModelManager;
import org.ddogleg.fitting.modelset.lmeds.LeastMedianOfSquares;
import org.ddogleg.fitting.modelset.lmeds.LeastMedianOfSquares_MT;
import org.ddogleg.struct.Factory;

import java.util.List;

/**
 * High level API for fitting points and related to shapes. Different robust fitting algorithms are applied
 */
public class ShapeFittingRobustOps {

	/** If it's fewer than this number of points it will use single threaded versions */
	public int thresholdConcurrent = 100;

	/** If true it will always use a single threaded implementation */
	public boolean forceSingleThread = false;

	/** Random number generator used by robust fitter. Same seed is used each time it's called */
	public long randomSeed = 0xDEADBEEFL;

	/** Either the maximum number of iterations or the number of iterations, depending on implementation */
	public int maxIterations = 0;

	/** Inlier threshold used by RANSAC */
	public double ransacThreshold = 0.0;

	/** LMedS will return else if the inlier error is more than this value */
	public double lsmedMaxAllowedError = 1e30; // Double.MAX_VALUE; <-- DDogleg bug prevents that. Update in future

	/** Inlier fraction for LMedS */
	public double lsmedInlierFraction = 0.5;

	/** How good the fit was according to the robust algorithm */
	public double outputFitQuality = 0.0;

	/**
	 * Configures RANSAC parameters
	 */
	public void configRansac( int iterations, double threshold ) {
		if (iterations <= 0)
			throw new IllegalArgumentException("Iterations must be more than zero");
		if (threshold <= 0)
			throw new IllegalArgumentException("threshold must be a positive number");
		this.maxIterations = iterations;
		this.ransacThreshold = threshold;
	}

	/**
	 * Estimates a plane from points
	 */
	public PlaneGeneral3D_F64 ransacPlaneFromPoints( List<Point3D_F64> points ) {
		return fitWithRansac(points, new ModelManagerPlaneGeneral3D_F64(),
				GeneratorPlaneGeneral3D_F64::new, PointDistanceFromPlaneGeneral_F64::new);
	}

	/**
	 * Estimates a plane from points with surface normals
	 */
	public PlaneGeneral3D_F64 ransacPlaneFromPointNormals( List<PlaneNormal3D_F64> points ) {
		return fitWithRansac(points, new ModelManagerPlaneGeneral3D_F64(),
				GeneratorPlaneFromPlane_F64::new, PointNormalDistanceFromPlaneGeneral_F64::new);
	}

	/**
	 * Estimates a plane from points
	 */
	public PlaneGeneral3D_F64 lmedsPlaneFromPoints( List<Point3D_F64> points ) {
		return fitWithLMedS(points, new ModelManagerPlaneGeneral3D_F64(),
				GeneratorPlaneGeneral3D_F64::new, PointDistanceFromPlaneGeneral_F64::new);
	}

	/**
	 * Estimates a plane from points with surface normals
	 */
	public PlaneGeneral3D_F64 lmedsPlaneFromPointNormals( List<PlaneNormal3D_F64> points ) {
		return fitWithLMedS(points, new ModelManagerPlaneGeneral3D_F64(),
				GeneratorPlaneFromPlane_F64::new, PointNormalDistanceFromPlaneGeneral_F64::new);
	}

	/**
	 * Estimates a plane from points with surface normals
	 */
	public Cylinder3D_F64 ransacCylinderFromPointNormals( List<PlaneNormal3D_F64> points ) {
		return fitWithRansac(points, new ModelManagerCylinder3D_F64(),
				GenerateCylinderFromPointNormals_F64::new, PointNormalDistanceFromCylinder_F64::new);
	}

	/**
	 * Estimates a plane from points with surface normals
	 */
	public Cylinder3D_F64 lmedsCylinderFromPointNormals( List<PlaneNormal3D_F64> points ) {
		return fitWithLMedS(points, new ModelManagerCylinder3D_F64(),
				GenerateCylinderFromPointNormals_F64::new, PointNormalDistanceFromCylinder_F64::new);
	}

	protected <Model, Point>
	Model fitWithLMedS( List<Point> points,
						ModelManager<Model> modelManager,
						Factory<ModelGenerator<Model, Point>> factoryGenerator,
						Factory<DistanceFromModel<Model, Point>> factoryDistance ) {
		if (points.isEmpty())
			throw new IllegalArgumentException("No points");

		LeastMedianOfSquares<Model, Point> robust;
		if (useConcurrent(points)) {
			robust = new LeastMedianOfSquares_MT<>(randomSeed, maxIterations,
					lsmedMaxAllowedError, lsmedInlierFraction,
					modelManager,
					(Class<Point>)points.get(0).getClass());
		} else {
			robust = new LeastMedianOfSquares<>(randomSeed, maxIterations,
					lsmedMaxAllowedError, lsmedInlierFraction,
					modelManager,
					(Class<Point>)points.get(0).getClass());
		}
		robust.setModel(factoryGenerator, factoryDistance);

		if (!robust.process(points)) {
			throw new RuntimeException("LMedS failed");
		}

		outputFitQuality = robust.getFitQuality();

		return robust.getModelParameters();
	}

	protected <Model, Point>
	Model fitWithRansac( List<Point> points,
						 ModelManager<Model> modelManager,
						 Factory<ModelGenerator<Model, Point>> factoryGenerator,
						 Factory<DistanceFromModel<Model, Point>> factoryDistance ) {
		if (maxIterations == 0 || ransacThreshold <= 0.0)
			throw new IllegalArgumentException("Must configure RANSAC first");

		if (points.isEmpty())
			throw new IllegalArgumentException("No points");

		LeastMedianOfSquares<Model, Point> robust;
		if (useConcurrent(points)) {
			robust = new LeastMedianOfSquares_MT<>(randomSeed, maxIterations,
					lsmedMaxAllowedError, lsmedInlierFraction,
					modelManager,
					(Class<Point>)points.get(0).getClass());
		} else {
			robust = new LeastMedianOfSquares<>(randomSeed, maxIterations,
					lsmedMaxAllowedError, lsmedInlierFraction,
					modelManager,
					(Class<Point>)points.get(0).getClass());
		}
		robust.setModel(factoryGenerator, factoryDistance);

		if (!robust.process(points)) {
			throw new RuntimeException("LMedS failed");
		}

		outputFitQuality = robust.getFitQuality();

		return robust.getModelParameters();
	}

	/**
	 * Checks to see if it should use a concurrent or single thread algorithm
	 */
	protected boolean useConcurrent( List<?> inputs ) {
		return !forceSingleThread && inputs.size() >= thresholdConcurrent;
	}
}
