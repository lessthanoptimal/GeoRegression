/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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
import georegression.struct.shapes.Box3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import georegression.struct.shapes.Triangle3D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestDistance3D_F64 {

	@Test
	void distance_line_line() {
		// test closestPoint
		LineParametric3D_F64 l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		LineParametric3D_F64 l1 = new LineParametric3D_F64(0,0,0,0,1,0);

		assertEquals(0,Distance3D_F64.distance( l0,l1 ), GrlConstants.TEST_F64);

		// test the lines separated over the z-axis
		l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		l1 = new LineParametric3D_F64(0,0,2,0,1,0);

		assertEquals(2,Distance3D_F64.distance( l0,l1 ), GrlConstants.TEST_F64);

		// test parallel but no closest point
		l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		l1 = new LineParametric3D_F64(0,0,2,1,0,0);

		assertEquals(2,Distance3D_F64.distance( l0,l1 ), GrlConstants.TEST_F64);

		// test identical lines
		l0 = new LineParametric3D_F64(0,0,0,1,0,0);
		l1 = new LineParametric3D_F64(0,0,0,1,0,0);

		assertEquals(0,Distance3D_F64.distance( l0,l1 ), GrlConstants.TEST_F64);
	}

	@Test
	void distance_line_point() {
		// a point above the line
		LineParametric3D_F64 l = new LineParametric3D_F64(1,2,3,0,1,0);
		Point3D_F64 p = new Point3D_F64( 3 , 2 , 3);

		assertEquals(2,Distance3D_F64.distance( l,p ), GrlConstants.TEST_F64);

		// a point on the line
		l.getSlope().setTo( 1 , 0 , 0 );
		assertEquals(0,Distance3D_F64.distance( l,p ), GrlConstants.TEST_F64);
	}

	@Test
	void distance_lineseg_point() {
		// a point above the line
		LineSegment3D_F64 l = new LineSegment3D_F64(1,2,3,1,3,3);
		Point3D_F64 p = new Point3D_F64( 3 , 2 , 3);

		assertEquals(2,Distance3D_F64.distance( l,p ), GrlConstants.TEST_F64);

		// a point on the line
		p.setTo(1,2.5,3);
		assertEquals(0,Distance3D_F64.distance( l,p ), GrlConstants.TEST_F64);

		// point past l.a
		p.setTo(3,-1,3);
		assertEquals( p.distance(l.a) , Distance3D_F64.distance( l,p ), GrlConstants.TEST_F64);
		// point past l.b
		p.setTo(3,5,3);
		assertEquals( p.distance(l.b) , Distance3D_F64.distance( l,p ), GrlConstants.TEST_F64);

		// this was a bug
		l = new LineSegment3D_F64(0,0,0,0,2,0);
		assertEquals( 0 , Distance3D_F64.distance( l,new Point3D_F64(0,1.5,0) ), GrlConstants.TEST_F64);
	}

	/**
	 * The distance is zero here, but due to round off error it is a negative number, which can cause
	 * sqrt to blow up
	 */
	@Test
	void distance_line_point_NegativeZero() {
		LineParametric3D_F64 line =
				new LineParametric3D_F64( 1.2182178902359924 , -0.39089105488200365 , 2.945445527441002 ,
						0.8728715609439697 , 0.4364357804719848 , -0.21821789023599247 );
		Point3D_F64 p = new Point3D_F64( 1.0 ,-0.5 ,3.0 );

		assertEquals(0,Distance3D_F64.distance(line,p), GrlConstants.TEST_F64);
	}


	@Test
	void distance_plane_point() {
		PlaneNormal3D_F64 n = new PlaneNormal3D_F64(3,4,-5,3,4,-5);
		PlaneGeneral3D_F64 g = UtilPlane3D_F64.convert(n, null);

		// distance from origin
		double expected = Math.sqrt(3*3 + 4*4 + 5*5);
		double found = Distance3D_F64.distanceSigned(g,new Point3D_F64(0,0,0));
		assertEquals(-expected,found, GrlConstants.TEST_F64);

		// on the plane
		found = Distance3D_F64.distanceSigned(g,new Point3D_F64(3,4,-5));
		assertEquals(0,found, GrlConstants.TEST_F64);

		// move it away from the plane
		Vector3D_F64 v = n.n;
		v.normalize();
		found = Distance3D_F64.distanceSigned(g,new Point3D_F64(v.x,v.y,v.z));
		assertEquals(-(expected-1),found, GrlConstants.TEST_F64);

		// make it to the other side and see if the sign changes
		found = Distance3D_F64.distanceSigned(g,new Point3D_F64(v.x+3,v.y+4,v.z-5));
		assertEquals(1,found, GrlConstants.TEST_F64);
	}

	@Test
	void distance_sphere_point() {

		Sphere3D_F64 sphere = new Sphere3D_F64(2,3,4,4.5);
		Point3D_F64 outside = new Point3D_F64(-3,4,-6.7);
		Point3D_F64 inside = new Point3D_F64(-1,2,2);

		double ro = sphere.center.distance(outside);
		double ri = sphere.center.distance(inside);

		assertTrue(ro>4.5);
		assertTrue(ri<4.5);

		assertEquals(ro-4.5,Distance3D_F64.distance(sphere,outside), GrlConstants.TEST_F64);
		assertEquals(ri-4.5,Distance3D_F64.distance(sphere,inside), GrlConstants.TEST_F64);
	}

	@Test
	void distance_cylinder_point() {

		Cylinder3D_F64 cylinder = new Cylinder3D_F64(1,2,3,0,0,2,3.5);
		Point3D_F64 outside = new Point3D_F64(1,10,0);
		Point3D_F64 inside = new Point3D_F64(1,4,3);

		double ro = Distance3D_F64.distance(cylinder.line,outside);
		double ri = Distance3D_F64.distance(cylinder.line,inside);

		assertTrue(ro>3.5);
		assertTrue(ri<3.5);

		assertEquals(ro-3.5,Distance3D_F64.distance(cylinder,outside), GrlConstants.TEST_F64);
		assertEquals(ri-3.5,Distance3D_F64.distance(cylinder,inside), GrlConstants.TEST_F64);
	}

	@Test
	void distance_triangle_point() {
		Triangle3D_F64 triangle = new Triangle3D_F64(0,0,0,  0,2,0,  1,1,0);

		double found = Distance3D_F64.distance(triangle,new Point3D_F64(0.2,0.5,2));

		assertEquals(2,found,GrlConstants.TEST_F64);

		found = Distance3D_F64.distance(triangle,new Point3D_F64(0.2,0.5,-2));

		assertEquals(-2,found,GrlConstants.TEST_F64);
	}

	@Test void scoreIoU_box() {
		// area = 24
		var A = new Box3D_F64(-1,-1,-1,2,1,3);
		// area = 8
		var B = new Box3D_F64(-2,-2,-2,0,0,0);

		double expected = 1.0/(24+8-1);
		assertEquals(expected, Distance3D_F64.scoreIoU(A,B), UtilEjml.TEST_F64);

		// Test no intersection
		var C = new Box3D_F64(10,-2,-9,12,-1,-8);
		assertEquals(0.0, Distance3D_F64.scoreIoU(A,C), UtilEjml.TEST_F64);
	}
}
