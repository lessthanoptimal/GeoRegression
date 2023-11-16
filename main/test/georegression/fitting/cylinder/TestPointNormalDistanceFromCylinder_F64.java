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

import georegression.struct.point.PointNormal3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestPointNormalDistanceFromCylinder_F64 {
	@Test void simple() {
		var alg = new PointNormalDistanceFromCylinder_F64();
		alg.setModel(new Cylinder3D_F64().setTo(0, 0, 1, 0, 0, 1, 3.0));

		// see if it's invariant to points along the axis
		assertEquals(3.0, alg.distance(new PointNormal3D_F64().setTo(0, 0, 0,0, 0, -1)), UtilEjml.TEST_F64);
		assertEquals(3.0, alg.distance(new PointNormal3D_F64().setTo(0, 0, 10,0, 0, -1)), UtilEjml.TEST_F64);
		assertEquals(3.0, alg.distance(new PointNormal3D_F64().setTo(0, 0, -10,0, 0, -1)), UtilEjml.TEST_F64);
		assertEquals(1, alg.distance(new PointNormal3D_F64().setTo(2, 0, 0,0, 0, -1)), UtilEjml.TEST_F64);
		assertEquals(1, alg.distance(new PointNormal3D_F64().setTo(-2, 0, 10,0, 0, -1)), UtilEjml.TEST_F64);
		assertEquals(1, alg.distance(new PointNormal3D_F64().setTo(0, 2, -10,0, 0, -1)), UtilEjml.TEST_F64);
		assertEquals(1, alg.distance(new PointNormal3D_F64().setTo(0, 4, -10,0, 0, -1)), UtilEjml.TEST_F64);

		// Point in another direction
		alg.setModel(new Cylinder3D_F64().setTo(0, 0, 1, 0, 1, 0, 3.0));
		assertEquals(1, alg.distance(new PointNormal3D_F64().setTo(2, 0, 1,0, 0, -1)), UtilEjml.TEST_F64);
		assertEquals(1, alg.distance(new PointNormal3D_F64().setTo(-2, 0, 1,0, 0, -1)), UtilEjml.TEST_F64);
		assertEquals(1, alg.distance(new PointNormal3D_F64().setTo(2, 10, 1,0, 0, -1)), UtilEjml.TEST_F64);
	}
}