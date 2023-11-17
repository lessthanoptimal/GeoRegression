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

package georegression.fitting.plane;

import georegression.geometry.UtilPlane3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class TestPointNormalFromPlaneGeneral_F64 {
	Random rand = new Random(234);
	PlaneNormal3D_F64 normal = new PlaneNormal3D_F64();
	PlaneGeneral3D_F64 general = new PlaneGeneral3D_F64();

	@Test void distance() {
		normal.p.setTo(20, 10, 2);
		normal.n.setTo(0, 0, 1);
		UtilPlane3D_F64.convert(normal, general);

		var alg = new PointNormalDistanceFromPlaneGeneral_F64();
		alg.setModel(general);
		assertEquals(0.0, alg.distance(createPoint(20.0, 10.0, 2.0)), UtilEjml.TEST_F64);
		assertEquals(0.0, alg.distance(createPoint(0.0, 0.0, 2.0)), UtilEjml.TEST_F64);
		assertEquals(0.0, alg.distance(createPoint(-0.5, 4.5, 2.0)), UtilEjml.TEST_F64);

		assertEquals(1.0, alg.distance(createPoint(0.0, 0.0, 3.0)), UtilEjml.TEST_F64);
		assertEquals(1.0, alg.distance(createPoint(0.0, 0.0, 1.0)), UtilEjml.TEST_F64);
	}

	private PlaneNormal3D_F64 createPoint( double x, double y, double z ) {
		var pn = new PlaneNormal3D_F64();
		pn.p.setTo(x, y, z);
		pn.n.setTo(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
		return pn;
	}

	@Test void distances() {
		normal.p.setTo(20, 10, 2);
		normal.n.setTo(0, 0, 1);
		UtilPlane3D_F64.convert(normal, general);

		var alg = new PointNormalDistanceFromPlaneGeneral_F64();
		alg.setModel(general);

		var points = new ArrayList<PlaneNormal3D_F64>();
		for (int i = 0; i < 20; i++) {
			points.add(createPoint(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian()));
		}

		/**/double[] errors = new /**/double[points.size()];
		alg.distances(points, errors);

		for (int i = 0; i < points.size(); i++) {
			/**/double expected = alg.distance(points.get(i));
			assertEquals(expected, errors[i]);
		}
	}

	@Test void types() {
		var alg = new PointNormalDistanceFromPlaneGeneral_F64();

		assertSame(PlaneNormal3D_F64.class, alg.getPointType());
		assertSame(PlaneGeneral3D_F64.class, alg.getModelType());
	}
}