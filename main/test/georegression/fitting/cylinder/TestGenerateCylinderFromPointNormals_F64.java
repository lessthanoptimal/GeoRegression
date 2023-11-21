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

package georegression.fitting.cylinder;

import georegression.metric.Distance3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestGenerateCylinderFromPointNormals_F64 {
	Random rand = new Random(2345);

	/**
	 * Test cases with perfect noise free
	 */
	@Test void perfect() {
		var alg = new GenerateCylinderFromPointNormals_F64();
		var found = new Cylinder3D_F64();

		for (int i = 0; i < 10; i++) {
			var cylinder = new Cylinder3D_F64();
			cylinder.radius = 0.5 + rand.nextDouble()*2;
			cylinder.line.p.x = rand.nextGaussian();
			cylinder.line.p.y = rand.nextGaussian();
			cylinder.line.p.z = rand.nextGaussian();
			cylinder.line.slope.x = rand.nextGaussian();
			cylinder.line.slope.y = rand.nextGaussian();
			cylinder.line.slope.z = rand.nextGaussian();

			// Create two points that should be good for estimating the cylinder. They can't be close to each other.
			double theta = 0;// rand.nextDouble()*Math.PI*2.0;
			PlaneNormal3D_F64 p0 = paramToPointOnCylinder(cylinder, rand.nextGaussian(), theta);
			PlaneNormal3D_F64 p1 = paramToPointOnCylinder(cylinder, rand.nextGaussian(), theta + Math.PI/2.0);

			// Sanity check
			assertEquals(0.0, Distance3D_F64.distanceSigned(cylinder, p0.p), UtilEjml.TEST_F64);
			assertEquals(0.0, Distance3D_F64.distanceSigned(cylinder, p1.p), UtilEjml.TEST_F64);

			assertTrue(alg.generate(List.of(p0, p1), found));

			// Verify the found cylinder is correct by seeing if the two points lie on it
			assertEquals(0.0, Distance3D_F64.distanceSigned(found, p0.p), UtilEjml.TEST_F64);
			assertEquals(0.0, Distance3D_F64.distanceSigned(found, p1.p), UtilEjml.TEST_F64);
		}
	}

	PlaneNormal3D_F64 paramToPointOnCylinder( Cylinder3D_F64 cylinder, double axis, double angle ) {
		var point = new PlaneNormal3D_F64();

		// Put it on a point that's on the axis
		point.p.x = cylinder.line.p.x + cylinder.line.slope.x*axis;
		point.p.y = cylinder.line.p.y + cylinder.line.slope.y*axis;
		point.p.z = cylinder.line.p.z + cylinder.line.slope.z*axis;

		// Define the axis of the new coordinate system
		var axisX = new Vector3D_F64(0, 0, 1).crossWith(cylinder.line.slope);
		axisX.normalize();
		var axisY = axisX.crossWith(cylinder.line.slope);
		axisY.normalize();

		// rotate point around the cylinder
		double xx = Math.cos(angle);
		double yy = Math.sin(angle);

		// find the normal vector
		point.n.x = axisX.x*xx + axisY.x*yy;
		point.n.y = axisX.y*xx + axisY.y*yy;
		point.n.z = axisX.z*xx + axisY.z*yy;

		// Move it out to the cylinder's surface
		point.p.x += point.n.x*cylinder.radius;
		point.p.y += point.n.y*cylinder.radius;
		point.p.z += point.n.z*cylinder.radius;

		return point;
	}
}