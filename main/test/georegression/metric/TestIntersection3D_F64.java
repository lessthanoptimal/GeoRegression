/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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
import georegression.struct.line.LineSegment3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Box3D_F64;
import georegression.struct.shapes.BoxLength3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import georegression.struct.shapes.Triangle3D_F64;
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

		assertEquals(2,found.x, GrlConstants.TEST_F64);
		assertEquals(0,found.y, GrlConstants.TEST_F64);
		assertEquals(0,found.z, GrlConstants.TEST_F64);
	}

	@Test
	public void intersect_planegen_linepara() {
		// simple case with a known solution
		PlaneNormal3D_F64 plane = new PlaneNormal3D_F64(2,1,0,2,0,0);
		LineParametric3D_F64 line = new LineParametric3D_F64(0,0,0,3,0,0);
		PlaneGeneral3D_F64 general = UtilPlane3D_F64.convert(plane,(PlaneGeneral3D_F64)null);

		Point3D_F64 found = new Point3D_F64();
		assertTrue(Intersection3D_F64.intersect(general, line, found));

		assertEquals(2,found.x, GrlConstants.TEST_F64);
		assertEquals(0,found.y, GrlConstants.TEST_F64);
		assertEquals(0,found.z, GrlConstants.TEST_F64);
	}

	@Test
	public void intersect_plane_plane() {
		PlaneGeneral3D_F64 a = new PlaneGeneral3D_F64(3,-4,0.5,6);
		PlaneGeneral3D_F64 b = new PlaneGeneral3D_F64(1.5,0.95,-4,-2);

		LineParametric3D_F64 line = new LineParametric3D_F64();

		Intersection3D_F64.intersect(a,b,line);

		// see if the origin of the line lies on both planes
		assertEquals(0, UtilPlane3D_F64.evaluate(a,line.p), GrlConstants.TEST_F64);
		assertEquals(0, UtilPlane3D_F64.evaluate(b,line.p), GrlConstants.TEST_F64);

		// now try another point on the line
		double x = line.p.x + line.slope.x;
		double y = line.p.y + line.slope.y;
		double z = line.p.z + line.slope.z;
		Point3D_F64 p = new Point3D_F64(x,y,z);

		assertEquals(0, UtilPlane3D_F64.evaluate(a,p), GrlConstants.TEST_F64);
		assertEquals(0, UtilPlane3D_F64.evaluate(b,p), GrlConstants.TEST_F64);

	}

	@Test
	public void intersection_triangle_ls() {
		LineSegment3D_F64 ls = new LineSegment3D_F64();
		Point3D_F64 p = new Point3D_F64();

		// degenerate triangle
		Triangle3D_F64 triangle = new Triangle3D_F64(1,1,1,2,2,2,3,3,3);
		assertEquals(-1,Intersection3D_F64.intersect(triangle,ls,p));

		// no intersection
		triangle.set(1,0,0,  3,0,0,  3,2,0);
		ls.set(0,0,0,  0,0,10); // completely miss
		assertEquals(0, Intersection3D_F64.intersect(triangle, ls, p));
		ls.set(0,0,0,  0,0,10); // hits the plain but not the triangle
		assertEquals(0, Intersection3D_F64.intersect(triangle, ls, p));
		ls.set(2,0.5,-1,  2,0.5,-0.5); // would hit, but is too short
		assertEquals(0, Intersection3D_F64.intersect(triangle, ls, p));
		ls.set(2,0.5,-0.5,  2,0.5,-1); // would hit, but is too short
		assertEquals(0,Intersection3D_F64.intersect(triangle,ls,p));

		// unique intersection
		ls.set(2,0.5,1,  2,0.5,-1);
		assertEquals(1,Intersection3D_F64.intersect(triangle,ls,p));
		assertEquals(0,p.distance(new Point3D_F64(2,0.5,0)),GrlConstants.TEST_F64);

		// infinite intersections
		ls.set(0, 0, 0, 4, 0, 0);
		assertEquals(2,Intersection3D_F64.intersect(triangle, ls, p));
	}

	@Test
	public void intersection_triangle_line() {
		LineParametric3D_F64 line = new LineParametric3D_F64();
		Point3D_F64 p = new Point3D_F64();


		// degenerate triangle
		Triangle3D_F64 triangle = new Triangle3D_F64(1,1,1,2,2,2,3,3,3);
		assertEquals(-1,Intersection3D_F64.intersect(triangle,line,p));

		// no intersection
		triangle.set(1,0,0,  3,0,0,  3,2,0);
		line.set(0,0,0,  0,0,10); // completely miss
		assertEquals(0, Intersection3D_F64.intersect(triangle, line, p));
		line.set(0,0,0,  0,0,10); // hits the plain but not the triangle
		assertEquals(0, Intersection3D_F64.intersect(triangle, line, p));

		// unique intersection - positive
		line.set(2,0.5,1,  0,0,-2);
		assertEquals(1,Intersection3D_F64.intersect(triangle,line,p));
		assertEquals(0,p.distance(new Point3D_F64(2,0.5,0)),GrlConstants.TEST_F64);
		// unique intersection - negative
		line.set(2,0.5,-1,  0,0,2);
		assertEquals(1,Intersection3D_F64.intersect(triangle,line,p));
		assertEquals(0,p.distance(new Point3D_F64(2,0.5,0)),GrlConstants.TEST_F64);

		// infinite intersections
		line.set(0, 0, 0, 4, 0, 0);
		assertEquals(2,Intersection3D_F64.intersect(triangle, line, p));
	}

	@Test
	public void intersect_poly2Dconvex_line() {


		fail("Implement");
	}

	@Test
	public void contained_boxLength_point() {
		BoxLength3D_F64 box = new BoxLength3D_F64(2,3,4,1,1.5,2.5);

		// point clearly inside the code
		assertTrue(Intersection3D_F64.contained(box,new Point3D_F64(2.1,3.1,4.1)));
		// point way outside
		assertFalse(Intersection3D_F64.contained(box,new Point3D_F64(-2,9,8)));

		// test edge cases
		assertTrue(Intersection3D_F64.contained(box,new Point3D_F64(2,3,4)));
		assertFalse(Intersection3D_F64.contained(box, new Point3D_F64(2+1, 3.1, 4.1)));
		assertFalse(Intersection3D_F64.contained(box, new Point3D_F64(2.1, 3+1.5, 4.1)));
		assertFalse(Intersection3D_F64.contained(box, new Point3D_F64(2.1, 3.1, 4+2.5)));
	}

	@Test
	public void contained_box_point() {
		Box3D_F64 box = new Box3D_F64(2,3,4,3,4.5,6.5);

		// point clearly inside the code
		assertTrue(Intersection3D_F64.contained(box,new Point3D_F64(2.1,3.1,4.1)));
		// point way outside
		assertFalse(Intersection3D_F64.contained(box,new Point3D_F64(-2,9,8)));

		// test edge cases
		assertTrue(Intersection3D_F64.contained(box,new Point3D_F64(2,3,4)));
		assertFalse(Intersection3D_F64.contained(box, new Point3D_F64(2+1, 3.1, 4.1)));
		assertFalse(Intersection3D_F64.contained(box, new Point3D_F64(2.1, 3+1.5, 4.1)));
		assertFalse(Intersection3D_F64.contained(box, new Point3D_F64(2.1, 3.1, 4+2.5)));
	}

	@Test
	public void contained2_box_point() {
		Box3D_F64 box = new Box3D_F64(2,3,4,3,4.5,6.5);

		// point clearly inside the code
		assertTrue(Intersection3D_F64.contained2(box, new Point3D_F64(2.1, 3.1, 4.1)));
		// point way outside
		assertFalse(Intersection3D_F64.contained2(box, new Point3D_F64(-2, 9, 8)));

		// test edge cases
		assertTrue(Intersection3D_F64.contained2(box, new Point3D_F64(2, 3, 4)));
		assertTrue(Intersection3D_F64.contained2(box, new Point3D_F64(2 + 1, 3.1, 4.1)));
		assertTrue(Intersection3D_F64.contained2(box, new Point3D_F64(2.1, 3 + 1.5, 4.1)));
		assertTrue(Intersection3D_F64.contained2(box, new Point3D_F64(2.1, 3.1, 4 + 2.5)));
	}

	@Test
	public void contained_box_box() {
		Box3D_F64 box = new Box3D_F64(2,3,4,3,4.5,6.5);

		// identical
		assertTrue(Intersection3D_F64.contained(box,new Box3D_F64(2,3,4,3,4.5,6.5)));
		// smaller
		assertTrue(Intersection3D_F64.contained(box,new Box3D_F64(2.1,3.1,4.1,2.9,4.4,6.4)));

		// partial x-axis
		assertFalse(Intersection3D_F64.contained(box,new Box3D_F64(1.9,3,4,3,4.5,6.5)));
		assertFalse(Intersection3D_F64.contained(box,new Box3D_F64(2,3,4,3.1,4.5,6.5)));
		// partial y-axis
		assertFalse(Intersection3D_F64.contained(box,new Box3D_F64(2,2.9,4,3,4.5,6.5)));
		assertFalse(Intersection3D_F64.contained(box,new Box3D_F64(2,3,4,3,4.6,6.5)));
		// partial z-axis
		assertFalse(Intersection3D_F64.contained(box,new Box3D_F64(2,3,3.9,3,4.5,6.5)));
		assertFalse(Intersection3D_F64.contained(box,new Box3D_F64(2,3,4,3,4.5,6.6)));
	}

	@Test
	public void intersect_box_box() {
		Box3D_F64 box = new Box3D_F64(2,3,4,3,4.5,6.5);

		// identical
		assertTrue(Intersection3D_F64.contained(box,new Box3D_F64(2,3,4,3,4.5,6.5)));
		// outside
		assertFalse(Intersection3D_F64.contained(box,new Box3D_F64(10,10,10,12,12,12)));
		assertFalse(Intersection3D_F64.contained(box,new Box3D_F64(10,3,4, 12,4.5,6.5)));
		assertFalse(Intersection3D_F64.contained(box,new Box3D_F64(2,10,4, 3,12,6.5)));
		assertFalse(Intersection3D_F64.contained(box,new Box3D_F64(2,3,10, 3,4.5,12)));

		// assume the 1D tests are sufficient.  the above tests do check to see if each axis is handled
		// individually
	}

	@Test
	public void intersect_1d() {
		// identical
		assertTrue(Intersection3D_F64.intersect(0,0,1,1));
		// bigger
		assertTrue(Intersection3D_F64.intersect(0,-1,1,2));
		assertTrue(Intersection3D_F64.intersect(-1,0,2,1));
		// shifted
		assertTrue(Intersection3D_F64.intersect(0,0.1,1,1.1));
		assertTrue(Intersection3D_F64.intersect(0,-0.1,1,0.9));
		assertTrue(Intersection3D_F64.intersect(0.1,0,1.1,1));
		assertTrue(Intersection3D_F64.intersect(-0.1,0,0.9,1));
		// graze
		assertFalse(Intersection3D_F64.intersect(0,1,1,2));
		assertFalse(Intersection3D_F64.intersect(1,0,2,1));
		// outside
		assertFalse(Intersection3D_F64.intersect(0,2,1,3));
	}

	@Test
	public void intersect_line_sphere() {
		Point3D_F64 a = new Point3D_F64();
		Point3D_F64 b = new Point3D_F64();

		// test a negative case first
		assertFalse(Intersection3D_F64.intersect(
				new LineParametric3D_F64(0,0,-10,1,0,0),new Sphere3D_F64(0,0,0,2),a,b));

		// Now a positive case
		assertTrue(Intersection3D_F64.intersect(
				new LineParametric3D_F64(0,0,2,1,0,0),new Sphere3D_F64(0,0,2,2),a,b));
		assertTrue( a.distance(new Point3D_F64( 2,0,2)) <= GrlConstants.TEST_F64);
		assertTrue( b.distance(new Point3D_F64(-2,0,2)) <= GrlConstants.TEST_F64);

	}
}
