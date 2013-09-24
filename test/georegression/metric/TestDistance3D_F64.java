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
import georegression.struct.point.Vector3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestDistance3D_F64 {

	@Test
	public void distance_line_line() {
		// test intersection
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
}
