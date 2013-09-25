/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.fitting.sphere;

import georegression.metric.Distance3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestSphereToPointSignedDistance_F64 {

	@Test
	public void compareToDistance() {

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
			assertEquals(expected,(double) output[i], GrlConstants.DOUBLE_TEST_TOL);
		}
	}

	@Test
	public void getN_and_getM() {
		SphereToPointSignedDistance_F64 alg = new SphereToPointSignedDistance_F64();

		List<Point3D_F64> points = new ArrayList<Point3D_F64>();
		points.add(new Point3D_F64(1,2,3.5));
		points.add(new Point3D_F64(1,2,3.5));
		points.add(new Point3D_F64(1,2,3.5));

		alg.setPoints(points);

		assertEquals(4,alg.getN());
		assertEquals(points.size(),alg.getM());
	}

}
