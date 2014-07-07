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

import georegression.geometry.UtilPlane3D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.plane.PlaneGeneral3D_F32;
import georegression.struct.plane.PlaneNormal3D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.shapes.Cube3D_F32;
import georegression.struct.shapes.CubeLength3D_F32;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
public class TestIntersection3D_F32 {

	@Test
	public void intersect_planenorm_linepara() {
		// simple case with a known solution
		PlaneNormal3D_F32 plane = new PlaneNormal3D_F32(2,1,0,2,0,0);
		LineParametric3D_F32 line = new LineParametric3D_F32(0,0,0,3,0,0);

		Point3D_F32 found = new Point3D_F32();
		assertTrue(Intersection3D_F32.intersect(plane, line, found));

		assertEquals(2,found.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0,found.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0,found.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void intersect_planegen_linepara() {
		// simple case with a known solution
		PlaneNormal3D_F32 plane = new PlaneNormal3D_F32(2,1,0,2,0,0);
		LineParametric3D_F32 line = new LineParametric3D_F32(0,0,0,3,0,0);
		PlaneGeneral3D_F32 general = UtilPlane3D_F32.convert(plane,(PlaneGeneral3D_F32)null);

		Point3D_F32 found = new Point3D_F32();
		assertTrue(Intersection3D_F32.intersect(general, line, found));

		assertEquals(2,found.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0,found.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0,found.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void intersect_plane_plane() {
		PlaneGeneral3D_F32 a = new PlaneGeneral3D_F32(3,-4,0.5f,6);
		PlaneGeneral3D_F32 b = new PlaneGeneral3D_F32(1.5f,0.95f,-4,-2);

		LineParametric3D_F32 line = new LineParametric3D_F32();

		Intersection3D_F32.intersect(a,b,line);

		// see if the origin of the line lies on both planes
		assertEquals(0, UtilPlane3D_F32.evaluate(a,line.p), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0, UtilPlane3D_F32.evaluate(b,line.p), GrlConstants.FLOAT_TEST_TOL);

		// now try another point on the line
		float x = line.p.x + line.slope.x;
		float y = line.p.y + line.slope.y;
		float z = line.p.z + line.slope.z;
		Point3D_F32 p = new Point3D_F32(x,y,z);

		assertEquals(0, UtilPlane3D_F32.evaluate(a,p), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0, UtilPlane3D_F32.evaluate(b,p), GrlConstants.FLOAT_TEST_TOL);

	}

	@Test
	public void contained_cubeLength_point() {
		CubeLength3D_F32 cube = new CubeLength3D_F32(2,3,4,1,1.5f,2.5f);

		// point clearly inside the code
		assertTrue(Intersection3D_F32.contained(cube,new Point3D_F32(2.1f,3.1f,4.1f)));
		// point way outside
		assertFalse(Intersection3D_F32.contained(cube,new Point3D_F32(-2,9,8)));

		// test edge cases
		assertTrue(Intersection3D_F32.contained(cube,new Point3D_F32(2,3,4)));
		assertFalse(Intersection3D_F32.contained(cube, new Point3D_F32(2+1, 3.1f, 4.1f)));
		assertFalse(Intersection3D_F32.contained(cube, new Point3D_F32(2.1f, 3+1.5f, 4.1f)));
		assertFalse(Intersection3D_F32.contained(cube, new Point3D_F32(2.1f, 3.1f, 4+2.5f)));
	}

	@Test
	public void contained_cube_point() {
		Cube3D_F32 cube = new Cube3D_F32(2,3,4,3,4.5f,6.5f);

		// point clearly inside the code
		assertTrue(Intersection3D_F32.contained(cube,new Point3D_F32(2.1f,3.1f,4.1f)));
		// point way outside
		assertFalse(Intersection3D_F32.contained(cube,new Point3D_F32(-2,9,8)));

		// test edge cases
		assertTrue(Intersection3D_F32.contained(cube,new Point3D_F32(2,3,4)));
		assertFalse(Intersection3D_F32.contained(cube, new Point3D_F32(2+1, 3.1f, 4.1f)));
		assertFalse(Intersection3D_F32.contained(cube, new Point3D_F32(2.1f, 3+1.5f, 4.1f)));
		assertFalse(Intersection3D_F32.contained(cube, new Point3D_F32(2.1f, 3.1f, 4+2.5f)));
	}

	@Test
	public void contained_cube_cube() {
		Cube3D_F32 cube = new Cube3D_F32(2,3,4,3,4.5f,6.5f);

		// identical
		assertTrue(Intersection3D_F32.contained(cube,new Cube3D_F32(2,3,4,3,4.5f,6.5f)));
		// smaller
		assertTrue(Intersection3D_F32.contained(cube,new Cube3D_F32(2.1f,3.1f,4.1f,2.9f,4.4f,6.4f)));

		// partial x-axis
		assertFalse(Intersection3D_F32.contained(cube,new Cube3D_F32(1.9f,3,4,3,4.5f,6.5f)));
		assertFalse(Intersection3D_F32.contained(cube,new Cube3D_F32(2,3,4,3.1f,4.5f,6.5f)));
		// partial y-axis
		assertFalse(Intersection3D_F32.contained(cube,new Cube3D_F32(2,2.9f,4,3,4.5f,6.5f)));
		assertFalse(Intersection3D_F32.contained(cube,new Cube3D_F32(2,3,4,3,4.6f,6.5f)));
		// partial z-axis
		assertFalse(Intersection3D_F32.contained(cube,new Cube3D_F32(2,3,3.9f,3,4.5f,6.5f)));
		assertFalse(Intersection3D_F32.contained(cube,new Cube3D_F32(2,3,4,3,4.5f,6.6f)));
	}

	@Test
	public void intersect_cube_cube() {
		Cube3D_F32 cube = new Cube3D_F32(2,3,4,3,4.5f,6.5f);

		// identical
		assertTrue(Intersection3D_F32.contained(cube,new Cube3D_F32(2,3,4,3,4.5f,6.5f)));
		// outside
		assertFalse(Intersection3D_F32.contained(cube,new Cube3D_F32(10,10,10,12,12,12)));
		assertFalse(Intersection3D_F32.contained(cube,new Cube3D_F32(10,3,4, 12,4.5f,6.5f)));
		assertFalse(Intersection3D_F32.contained(cube,new Cube3D_F32(2,10,4, 3,12,6.5f)));
		assertFalse(Intersection3D_F32.contained(cube,new Cube3D_F32(2,3,10, 3,4.5f,12)));

		// assume the 1D tests are sufficient.  the above tests do check to see if each axis is handled
		// individually
	}

	@Test
	public void intersect_1d() {
		// identical
		assertTrue(Intersection3D_F32.intersect(0,0,1,1));
		// bigger
		assertTrue(Intersection3D_F32.intersect(0,-1,1,2));
		assertTrue(Intersection3D_F32.intersect(-1,0,2,1));
		// shifted
		assertTrue(Intersection3D_F32.intersect(0,0.1f,1,1.1f));
		assertTrue(Intersection3D_F32.intersect(0,-0.1f,1,0.9f));
		assertTrue(Intersection3D_F32.intersect(0.1f,0,1.1f,1));
		assertTrue(Intersection3D_F32.intersect(-0.1f,0,0.9f,1));
		// graze
		assertFalse(Intersection3D_F32.intersect(0,1,1,2));
		assertFalse(Intersection3D_F32.intersect(1,0,2,1));
		// outside
		assertFalse(Intersection3D_F32.intersect(0,2,1,3));
	}
}
