/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.sphere;

import georegression.metric.Distance3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestSphereToPointSignedDistance_F64 {

	@Test
	void compareToDistance() {

		Sphere3D_F64 sphere = new Sphere3D_F64(1,2,3,4);

		SphereToPointSignedDistance_F64 alg = new SphereToPointSignedDistance_F64();

		/**/double param[] = new /**/double[4];
		param[0] = sphere.center.x;
		param[1] = sphere.center.y;
		param[2] = sphere.center.z;
		param[3] = sphere.radius;

		List<Point3D_F64> points = new ArrayList<Point3D_F64>();

		// inside, should be negative
		points.add(new Point3D_F64(1,2,3.5));
		// outside, should be positive
		points.add(new Point3D_F64(1,2,12));

		/**/double output[] = new /**/double[ points.size() ];

		alg.setPoints(points);
		alg.process(param,output);

		for( int i = 0; i < points.size(); i++ ) {
			double expected = Distance3D_F64.distance(sphere,points.get(i));
			assertEquals(expected,(double) output[i], GrlConstants.TEST_F64);
		}
	}

	@Test
	void getN_and_getM() {
		SphereToPointSignedDistance_F64 alg = new SphereToPointSignedDistance_F64();

		List<Point3D_F64> points = new ArrayList<Point3D_F64>();
		points.add(new Point3D_F64(1,2,3.5));
		points.add(new Point3D_F64(1,2,3.5));
		points.add(new Point3D_F64(1,2,3.5));

		alg.setPoints(points);

		assertEquals(4,alg.getNumOfInputsN());
		assertEquals(points.size(),alg.getNumOfOutputsM());
	}

}
