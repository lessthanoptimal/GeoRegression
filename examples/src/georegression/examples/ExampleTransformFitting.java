/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.examples;

import georegression.fitting.MotionTransformPoint;
import georegression.fitting.se.MotionSe3PointSVD_F64;
import georegression.geometry.ConvertRotation3D_F64;
import georegression.struct.EulerType;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.transform.se.SePointOps_F64;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Demonstrate how to estimate rigid body motion of a point cloud
 *
 * @author Peter Abeles
 */
public class ExampleTransformFitting {
	public static void main(String[] args) {
		Random rand = new Random(234);

		// Create a transform which will be applied to the point cloud
		Se3_F64 actual = new Se3_F64();
		ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0.5,-2,0.15,actual.R);
		actual.T.setTo(2,3,-2);

		// Create a random point cloud and transform it
		List<Point3D_F64> fromPts = new ArrayList<Point3D_F64>();
		List<Point3D_F64> toPts = new ArrayList<Point3D_F64>();

		for( int i = 0; i < 50; i++ ) {
			double x = rand.nextGaussian();
			double y = rand.nextGaussian();
			double z = rand.nextGaussian();

			Point3D_F64 p = new Point3D_F64(x,y,z);
			Point3D_F64 tranP = new Point3D_F64();

			SePointOps_F64.transform(actual,p,tranP);

			fromPts.add(p);
			toPts.add(tranP);
		}

		// Estimate the transform from the point pairs
		MotionTransformPoint<Se3_F64, Point3D_F64> estimator = new MotionSe3PointSVD_F64();

		if(!estimator.process(fromPts,toPts))
			throw new RuntimeException("Estimation of Se3 failed");

		Se3_F64 found = estimator.getTransformSrcToDst();

		// Print out the results and see how it compares to ground truth
		actual.print();
		System.out.println();
		found.print();
	}
}
