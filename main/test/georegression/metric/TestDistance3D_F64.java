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
import georegression.struct.line.LineSegment3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import georegression.struct.shapes.Triangle3D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestDistance3D_F64 {

	@Test
	public void distance_line_line() {
		// test closestPoint
		LineParametric3D_F64 l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		LineParametric3D_F64 l1 = new LineParametric3D_F64(0,0,0,0,1,0);

		assertEquals(0,Distance3D_F64.distance( l0,l1 ), GrlConstants.DOUBLE_TEST_TOL );

		// test the lines separated over the z-axis
		l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		l1 = new LineParametric3D_F64(0,0,2,0,1,0);

		assertEquals(2,Distance3D_F64.distance( l0,l1 ), GrlConstants.DOUBLE_TEST_TOL );

		// test parallel but no closest point
		l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		l1 = new LineParametric3D_F64(0,0,2,1,0,0);

		assertEquals(2,Distance3D_F64.distance( l0,l1 ), GrlConstants.DOUBLE_TEST_TOL );

		// test identical lines
		l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		l1 = new LineParametric3D_F64(0,0,0,1,0,0);

		assertEquals(0,Distance3D_F64.distance( l0,l1 ), GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void distance_line_point() {
		// a point above the line
		LineParametric3D_F64 l = new LineParametric3D_F64(1,2,3,0,1,0);
		Point3D_F64 p = new Point3D_F64( 3 , 2 , 3);

		assertEquals(2,Distance3D_F64.distance( l,p ), GrlConstants.DOUBLE_TEST_TOL );

		// a point on the line
		l.getSlope().set( 1 , 0 , 0 );
		assertEquals(0,Distance3D_F64.distance( l,p ), GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void distance_lineseg_point() {
		// a point above the line
		LineSegment3D_F64 l = new LineSegment3D_F64(1,2,3,1,3,3);
		Point3D_F64 p = new Point3D_F64( 3 , 2 , 3);

		assertEquals(2,Distance3D_F64.distance( l,p ), GrlConstants.DOUBLE_TEST_TOL );

		// a point on the line
		p.set(1,2.5,3);
		assertEquals(0,Distance3D_F64.distance( l,p ), GrlConstants.DOUBLE_TEST_TOL );

		// point past l.a
		p.set(3,-1,3);
		assertEquals( p.distance(l.a) , Distance3D_F64.distance( l,p ), GrlConstants.DOUBLE_TEST_TOL );
		// point past l.b
		p.set(3,5,3);
		assertEquals( p.distance(l.b) , Distance3D_F64.distance( l,p ), GrlConstants.DOUBLE_TEST_TOL );

		// this was a bug
		l = new LineSegment3D_F64(0,0,0,0,2,0);
		assertEquals( 0 , Distance3D_F64.distance( l,new Point3D_F64(0,1.5,0) ), GrlConstants.DOUBLE_TEST_TOL );
	}

	/**
	 * The distance is zero here, but due to round off error it is a negative number, which can cause
	 * sqrt to blow up
	 */
	@Test
	public void distance_line_point_NegativeZero() {
		LineParametric3D_F64 line =
				new LineParametric3D_F64( 1.2182178902359924 , -0.39089105488200365 , 2.945445527441002 ,
						0.8728715609439697 , 0.4364357804719848 , -0.21821789023599247 );
		Point3D_F64 p = new Point3D_F64( 1.0 ,-0.5 ,3.0 );

		assertEquals(0,Distance3D_F64.distance(line,p),1e-8);
	}


	@Test
	public void distance_plane_point() {
		PlaneNormal3D_F64 n = new PlaneNormal3D_F64(3,4,-5,3,4,-5);
		PlaneGeneral3D_F64 g = UtilPlane3D_F64.convert(n, null);

		// distance from origin
		double expected = Math.sqrt(3*3 + 4*4 + 5*5);
		double found = Distance3D_F64.distance(g,new Point3D_F64(0,0,0));
		assertEquals(-expected,found, GrlConstants.DOUBLE_TEST_TOL);

		// on the plane
		found = Distance3D_F64.distance(g,new Point3D_F64(3,4,-5));
		assertEquals(0,found, GrlConstants.DOUBLE_TEST_TOL);

		// move it away from the plane
		Vector3D_F64 v = n.n;
		v.normalize();
		found = Distance3D_F64.distance(g,new Point3D_F64(v.x,v.y,v.z));
		assertEquals(-(expected-1),found, GrlConstants.DOUBLE_TEST_TOL);

		// make it to the other side and see if the sign changes
		found = Distance3D_F64.distance(g,new Point3D_F64(v.x+3,v.y+4,v.z-5));
		assertEquals(1,found, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void distance_sphere_point() {

		Sphere3D_F64 sphere = new Sphere3D_F64(2,3,4,4.5);
		Point3D_F64 outside = new Point3D_F64(-3,4,-6.7);
		Point3D_F64 inside = new Point3D_F64(-1,2,2);

		double ro = sphere.center.distance(outside);
		double ri = sphere.center.distance(inside);

		assertTrue(ro>4.5);
		assertTrue(ri<4.5);

		assertEquals(ro-4.5,Distance3D_F64.distance(sphere,outside), GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(ri-4.5,Distance3D_F64.distance(sphere,inside), GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void distance_cylinder_point() {

		Cylinder3D_F64 cylinder = new Cylinder3D_F64(1,2,3,0,0,2,3.5);
		Point3D_F64 outside = new Point3D_F64(1,10,0);
		Point3D_F64 inside = new Point3D_F64(1,4,3);

		double ro = Distance3D_F64.distance(cylinder.line,outside);
		double ri = Distance3D_F64.distance(cylinder.line,inside);

		assertTrue(ro>3.5);
		assertTrue(ri<3.5);

		assertEquals(ro-3.5,Distance3D_F64.distance(cylinder,outside), GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(ri-3.5,Distance3D_F64.distance(cylinder,inside), GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void distance_triangle_point() {
		Triangle3D_F64 triangle = new Triangle3D_F64(0,0,0,  0,2,0,  1,1,0);

		double found = Distance3D_F64.distance(triangle,new Point3D_F64(0.2,0.5,2));

		assertEquals(2,found,GrlConstants.DOUBLE_TEST_TOL);

		found = Distance3D_F64.distance(triangle,new Point3D_F64(0.2,0.5,-2));

		assertEquals(-2,found,GrlConstants.DOUBLE_TEST_TOL);
	}
}
