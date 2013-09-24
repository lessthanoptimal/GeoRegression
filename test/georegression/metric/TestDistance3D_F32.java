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
import georegression.struct.point.Vector3D_F32;
import georegression.struct.shapes.Cylinder3D_F32;
import georegression.struct.shapes.Sphere3D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestDistance3D_F32 {

	@Test
	public void distance_line_line() {
		// test intersection
		LineParametric3D_F32 l0 = new LineParametric3D_F32(0,0,0,1,0,0);
		LineParametric3D_F32 l1 = new LineParametric3D_F32(0,0,0,0,1,0);

		assertEquals(0,Distance3D_F32.distance( l0,l1 ), GrlConstants.FLOAT_TEST_TOL );

		// test the lines separated over the z-axis
		l0 = new LineParametric3D_F32(0,0,0,1,0,0);
		l1 = new LineParametric3D_F32(0,0,2,0,1,0);

		assertEquals(2,Distance3D_F32.distance( l0,l1 ), GrlConstants.FLOAT_TEST_TOL );

		// test parallel but no closest point
		l0 = new LineParametric3D_F32(0,0,0,1,0,0);
		l1 = new LineParametric3D_F32(0,0,2,1,0,0);

		assertEquals(2,Distance3D_F32.distance( l0,l1 ), GrlConstants.FLOAT_TEST_TOL );

		// test identical lines
		l0 = new LineParametric3D_F32(0,0,0,1,0,0);
		l1 = new LineParametric3D_F32(0,0,0,1,0,0);

		assertEquals(0,Distance3D_F32.distance( l0,l1 ), GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void distance_line_point() {
		// a point above the line
		LineParametric3D_F32 l = new LineParametric3D_F32(1,2,3,0,1,0);
		Point3D_F32 p = new Point3D_F32( 3 , 2 , 3);

		assertEquals(2,Distance3D_F32.distance( l,p ), GrlConstants.FLOAT_TEST_TOL );

		// a point on the line
		l.getSlope().set( 1 , 0 , 0 );
		assertEquals(0,Distance3D_F32.distance( l,p ), GrlConstants.FLOAT_TEST_TOL );
	}

	/**
	 * The distance is zero here, but due to round off error it is a negative number, which can cause
	 * sqrt to blow up
	 */
	@Test
	public void distance_line_point_NegativeZero() {
		LineParametric3D_F32 line =
				new LineParametric3D_F32( 1.2182178902359924f , -0.39089105488200365f , 2.945445527441002f ,
						0.8728715609439697f , 0.4364357804719848f , -0.21821789023599247f );
		Point3D_F32 p = new Point3D_F32( 1.0f ,-0.5f ,3.0f );

		assertEquals(0,Distance3D_F32.distance(line,p),1e-8);
	}


	@Test
	public void distance_plane_point() {
		PlaneNormal3D_F32 n = new PlaneNormal3D_F32(3,4,-5,3,4,-5);
		PlaneGeneral3D_F32 g = UtilPlane3D_F32.convert(n, null);

		// distance from origin
		float expected = (float)Math.sqrt(3*3 + 4*4 + 5*5);
		float found = Distance3D_F32.distance(g,new Point3D_F32(0,0,0));
		assertEquals(-expected,found, GrlConstants.FLOAT_TEST_TOL);

		// on the plane
		found = Distance3D_F32.distance(g,new Point3D_F32(3,4,-5));
		assertEquals(0,found, GrlConstants.FLOAT_TEST_TOL);

		// move it away from the plane
		Vector3D_F32 v = n.n;
		v.normalize();
		found = Distance3D_F32.distance(g,new Point3D_F32(v.x,v.y,v.z));
		assertEquals(-(expected-1),found, GrlConstants.FLOAT_TEST_TOL);

		// make it to the other side and see if the sign changes
		found = Distance3D_F32.distance(g,new Point3D_F32(v.x+3,v.y+4,v.z-5));
		assertEquals(1,found, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void distance_sphere_point() {

		Sphere3D_F32 sphere = new Sphere3D_F32(2,3,4,4.5f);
		Point3D_F32 outside = new Point3D_F32(-3,4,-6.7f);
		Point3D_F32 inside = new Point3D_F32(-1,2,2);

		float ro = sphere.center.distance(outside);
		float ri = sphere.center.distance(inside);

		assertTrue(ro>4.5f);
		assertTrue(ri<4.5f);

		assertEquals(ro-4.5f,Distance3D_F32.distance(sphere,outside), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(ri-4.5f,Distance3D_F32.distance(sphere,inside), GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void distance_cylinder_point() {

		Cylinder3D_F32 cylinder = new Cylinder3D_F32(1,2,3,0,0,2,3.5f);
		Point3D_F32 outside = new Point3D_F32(1,10,0);
		Point3D_F32 inside = new Point3D_F32(1,4,3);

		float ro = Distance3D_F32.distance(cylinder.line,outside);
		float ri = Distance3D_F32.distance(cylinder.line,inside);

		assertTrue(ro>3.5f);
		assertTrue(ri<3.5f);

		assertEquals(ro-3.5f,Distance3D_F32.distance(cylinder,outside), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(ri-3.5f,Distance3D_F32.distance(cylinder,inside), GrlConstants.FLOAT_TEST_TOL);
	}
}
