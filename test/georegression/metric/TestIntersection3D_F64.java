/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.metric;

import georegression.geometry.UtilPlane3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Cube3D_F64;
import georegression.struct.shapes.CubeLength3D_F64;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
public class TestIntersection3D_F64 {

	@Test
	public void intersect_planenorm_linepara() {
		// simple case with a known solution
		PlaneNormal3D_F64 plane = new PlaneNormal3D_F64(2,1,0,2,0,0);
		LineParametric3D_F64 line = new LineParametric3D_F64(0,0,0,3,0,0);

		Point3D_F64 found = new Point3D_F64();
		assertTrue(Intersection3D_F64.intersect(plane, line, found));

		assertEquals(2,found.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0,found.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0,found.z, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void intersect_planegen_linepara() {
		// simple case with a known solution
		PlaneNormal3D_F64 plane = new PlaneNormal3D_F64(2,1,0,2,0,0);
		LineParametric3D_F64 line = new LineParametric3D_F64(0,0,0,3,0,0);
		PlaneGeneral3D_F64 general = UtilPlane3D_F64.convert(plane,(PlaneGeneral3D_F64)null);

		Point3D_F64 found = new Point3D_F64();
		assertTrue(Intersection3D_F64.intersect(general, line, found));

		assertEquals(2,found.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0,found.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0,found.z, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void intersect_plane_plane() {
		PlaneGeneral3D_F64 a = new PlaneGeneral3D_F64(3,-4,0.5,6);
		PlaneGeneral3D_F64 b = new PlaneGeneral3D_F64(1.5,0.95,-4,-2);

		LineParametric3D_F64 line = new LineParametric3D_F64();

		Intersection3D_F64.intersect(a,b,line);

		// see if the origin of the line lies on both planes
		assertEquals(0, UtilPlane3D_F64.evaluate(a,line.p), GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0, UtilPlane3D_F64.evaluate(b,line.p), GrlConstants.DOUBLE_TEST_TOL);

		// now try another point on the line
		double x = line.p.x + line.slope.x;
		double y = line.p.y + line.slope.y;
		double z = line.p.z + line.slope.z;
		Point3D_F64 p = new Point3D_F64(x,y,z);

		assertEquals(0, UtilPlane3D_F64.evaluate(a,p), GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0, UtilPlane3D_F64.evaluate(b,p), GrlConstants.DOUBLE_TEST_TOL);

	}

	@Test
	public void contained_cubeLength_point() {
		CubeLength3D_F64 cube = new CubeLength3D_F64(2,3,4,1,1.5,2.5);

		// point clearly inside the code
		assertTrue(Intersection3D_F64.contained(cube,new Point3D_F64(2.1,3.1,4.1)));
		// point way outside
		assertFalse(Intersection3D_F64.contained(cube,new Point3D_F64(-2,9,8)));

		// test edge cases
		assertTrue(Intersection3D_F64.contained(cube,new Point3D_F64(2,3,4)));
		assertFalse(Intersection3D_F64.contained(cube, new Point3D_F64(2+1, 3.1, 4.1)));
		assertFalse(Intersection3D_F64.contained(cube, new Point3D_F64(2.1, 3+1.5, 4.1)));
		assertFalse(Intersection3D_F64.contained(cube, new Point3D_F64(2.1, 3.1, 4+2.5)));
	}

	@Test
	public void contained_cube_point() {
		Cube3D_F64 cube = new Cube3D_F64(2,3,4,3,4.5,6.5);

		// point clearly inside the code
		assertTrue(Intersection3D_F64.contained(cube,new Point3D_F64(2.1,3.1,4.1)));
		// point way outside
		assertFalse(Intersection3D_F64.contained(cube,new Point3D_F64(-2,9,8)));

		// test edge cases
		assertTrue(Intersection3D_F64.contained(cube,new Point3D_F64(2,3,4)));
		assertFalse(Intersection3D_F64.contained(cube, new Point3D_F64(2+1, 3.1, 4.1)));
		assertFalse(Intersection3D_F64.contained(cube, new Point3D_F64(2.1, 3+1.5, 4.1)));
		assertFalse(Intersection3D_F64.contained(cube, new Point3D_F64(2.1, 3.1, 4+2.5)));
	}
}
