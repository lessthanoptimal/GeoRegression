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

package georegression.metric;

import georegression.geometry.UtilPlane3D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.plane.PlaneGeneral3D_F32;
import georegression.struct.plane.PlaneNormal3D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.shapes.Cube3D_F32;
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
	public void contained_cube_point() {
		Cube3D_F32 cube = new Cube3D_F32(2,3,4,1,1.5f,2.5f);

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
}
