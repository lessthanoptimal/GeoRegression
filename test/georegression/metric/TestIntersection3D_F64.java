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

import georegression.geometry.UtilPlane3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Cube3D_F64;
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
	public void contained_cube_point() {
		Cube3D_F64 cube = new Cube3D_F64(2,3,4,1,1.5,2.5);

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
