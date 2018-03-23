/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestFitPlane3D_F32 {

	Random rand = new Random(234);

	Vector3D_F32 axisX,axisY,axisZ;
	Point3D_F32 center;
	List<Point3D_F32> cloud;

	@Test
	public void svd() {
		createCloud();

		// compute the plane's equation
		Point3D_F32 foundCenter = new Point3D_F32();
		Vector3D_F32 foundNorm = new Vector3D_F32();

		FitPlane3D_F32 alg = new FitPlane3D_F32();

		alg.svd(cloud,foundCenter,foundNorm);

		// see if the found center is on the plane
		assertEquals(0,
				(foundCenter.x-center.x)*axisZ.x +
				(foundCenter.y-center.y)*axisZ.y +
				(foundCenter.z-center.z)*axisZ.z,
				GrlConstants.TEST_F32);

		// see if the found normal is valid
		foundNorm.normalize();
		float dot = foundNorm.dot(axisZ);
		assertEquals(0, (float)Math.abs(dot) - 1, GrlConstants.TEST_F32);
	}

	@Test
	public void svdPoint() {
		createCloud();

		// compute the plane's equation
		Vector3D_F32 foundNorm = new Vector3D_F32();

		FitPlane3D_F32 alg = new FitPlane3D_F32();

		alg.solvePoint(cloud,cloud.get(10),foundNorm);

		// see if the found normal is valid
		foundNorm.normalize();
		float dot = foundNorm.dot(axisZ);
		assertEquals(0, (float)Math.abs(dot) - 1, GrlConstants.TEST_F32);
	}

	private void createCloud() {
		// define a plane and its coordinate system
		axisX = new Vector3D_F32(1,2,3);
		axisY = new Vector3D_F32(3,-2,1);
		axisZ = axisX.cross(axisY);

		axisX.normalize();
		axisZ.normalize();
		axisY = axisX.cross(axisZ);

		center = new Point3D_F32(2,-1,0.5f);

		// randomly generate points on the plane
		cloud = new ArrayList<Point3D_F32>();
		for( int i = 0; i < 100; i++ ) {
			float x = (float)rand.nextGaussian()*5;
			float y = (float)rand.nextGaussian()*5;

			Point3D_F32 p = new Point3D_F32();
			p.x = center.x + x*axisX.x + y*axisY.x;
			p.y = center.y + x*axisX.y + y*axisY.y;
			p.z = center.z + x*axisX.z + y*axisY.z;

			cloud.add(p);
		}
	}

}
