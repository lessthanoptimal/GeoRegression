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

package georegression.fitting.plane;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
@SuppressWarnings("NullAway.Init")
public class TestFitPlane3D_F64 {

	Random rand = new Random(234);

	Vector3D_F64 axisX,axisY,axisZ;
	Point3D_F64 center;
	List<Point3D_F64> cloud;

	@Test
	void svd() {
		createCloud();

		// compute the plane's equation
		Point3D_F64 foundCenter = new Point3D_F64();
		Vector3D_F64 foundNorm = new Vector3D_F64();

		FitPlane3D_F64 alg = new FitPlane3D_F64();

		alg.svd(cloud,foundCenter,foundNorm);

		// see if the found center is on the plane
		assertEquals(0,
				(foundCenter.x-center.x)*axisZ.x +
				(foundCenter.y-center.y)*axisZ.y +
				(foundCenter.z-center.z)*axisZ.z,
				GrlConstants.TEST_F64);

		// see if the found normal is valid
		foundNorm.normalize();
		double dot = foundNorm.dot(axisZ);
		assertEquals(0, Math.abs(dot) - 1, GrlConstants.TEST_F64);
	}

	@Test
	void svdPoint() {
		createCloud();

		// compute the plane's equation
		Vector3D_F64 foundNorm = new Vector3D_F64();

		FitPlane3D_F64 alg = new FitPlane3D_F64();

		alg.solvePoint(cloud,cloud.get(10),foundNorm);

		// see if the found normal is valid
		foundNorm.normalize();
		double dot = foundNorm.dot(axisZ);
		assertEquals(0, Math.abs(dot) - 1, GrlConstants.TEST_F64);
	}

	private void createCloud() {
		// define a plane and its coordinate system
		axisX = new Vector3D_F64(1,2,3);
		axisY = new Vector3D_F64(3,-2,1);
		axisZ = axisX.crossWith(axisY);

		axisX.normalize();
		axisZ.normalize();
		axisY = axisX.crossWith(axisZ);

		center = new Point3D_F64(2,-1,0.5);

		// randomly generate points on the plane
		cloud = new ArrayList<Point3D_F64>();
		for( int i = 0; i < 100; i++ ) {
			double x = rand.nextGaussian()*5;
			double y = rand.nextGaussian()*5;

			Point3D_F64 p = new Point3D_F64();
			p.x = center.x + x*axisX.x + y*axisY.x;
			p.y = center.y + x*axisX.y + y*axisY.y;
			p.z = center.z + x*axisX.z + y*axisY.z;

			cloud.add(p);
		}
	}

}
