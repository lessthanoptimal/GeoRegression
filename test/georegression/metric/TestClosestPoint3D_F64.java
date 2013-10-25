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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestClosestPoint3D_F64 {
	/**
	 * Compute truth from 3 random points then see if the 3rd point is found again.
	 */
	@Test
	public void closestPoint_line() {
		Point3D_F64 a = new Point3D_F64( 1, 1, 1 );
		Point3D_F64 b = new Point3D_F64( 1.5, -2.5, 9 );
		Point3D_F64 c = new Point3D_F64( 10.1, 6, -3 );

		Vector3D_F64 va = new Vector3D_F64( a, b );
		Vector3D_F64 vc = new Vector3D_F64( c, b );

		LineParametric3D_F64 lineA = new LineParametric3D_F64( a, va );
		LineParametric3D_F64 lineB = new LineParametric3D_F64( c, vc );

		Point3D_F64 foundB = ClosestPoint3D_F64.closestPoint(lineA, lineB, null);

		assertTrue( b.isIdentical( foundB, GrlConstants.DOUBLE_TEST_TOL ) );
	}

	@Test
	public void closestPoints_lines() {
		Point3D_F64 a = new Point3D_F64( 1, 1, 1 );
		Point3D_F64 b = new Point3D_F64( 1.5, -2.5, 9 );
		Point3D_F64 c = new Point3D_F64( 10.1, 6, -3 );

		Vector3D_F64 va = new Vector3D_F64( a, b );
		Vector3D_F64 vc = new Vector3D_F64( c, b );
		// normalize the vector so that the value 't' is euclidean distance
		va.normalize();
		vc.normalize();

		LineParametric3D_F64 lineA = new LineParametric3D_F64( a, va );
		LineParametric3D_F64 lineB = new LineParametric3D_F64( c, vc );

		double param[] = new double[2];

		assertTrue(ClosestPoint3D_F64.closestPoints(lineA, lineB, param));

		assertEquals( a.distance(b) , param[0] , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( c.distance(b) , param[1] , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void closestPoint_point() {
		Point3D_F64 a = new Point3D_F64( 1, 1, 1 );
		Point3D_F64 b = new Point3D_F64( 1.5, -2.5, 9 );
		Point3D_F64 c = new Point3D_F64( 10.1, 6, -3 );

		Vector3D_F64 va = new Vector3D_F64( a, b );

		LineParametric3D_F64 lineA = new LineParametric3D_F64( a, va );

		Point3D_F64 foundB = ClosestPoint3D_F64.closestPoint(lineA, c, null);

		Vector3D_F64 p = new Vector3D_F64( foundB, c );

		// see if they are perpendicular and therefor c foundB is the closest point
		double d = p.dot( va );

		assertEquals( 0, d, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void closestPoint_planeNorm_point() {
		Point3D_F64 found;

		PlaneNormal3D_F64 n = new PlaneNormal3D_F64(3,4,-5,3,4,-5);

		found = ClosestPoint3D_F64.closestPoint(n,new Point3D_F64(0,0,0),null);
		assertEquals(3,found.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.DOUBLE_TEST_TOL);

		// move it closer, but the point shouldn't change
		Vector3D_F64 v = n.n;
		v.normalize();
		found = ClosestPoint3D_F64.closestPoint(n,new Point3D_F64(v.x,v.y,v.z),null);
		assertEquals(3,found.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.DOUBLE_TEST_TOL);

		// other side of normal
		found = ClosestPoint3D_F64.closestPoint(n,new Point3D_F64(3+v.x,4+v.y,v.z-5),null);
		assertEquals(3,found.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void closestPoint_planeGen_point() {
		Point3D_F64 found;

		PlaneNormal3D_F64 n = new PlaneNormal3D_F64(3,4,-5,3,4,-5);
		PlaneGeneral3D_F64 g = UtilPlane3D_F64.convert(n, null);

		found = ClosestPoint3D_F64.closestPoint(g,new Point3D_F64(0,0,0),null);
		assertEquals(3,found.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.DOUBLE_TEST_TOL);

		// move it closer, but the point shouldn't change
		Vector3D_F64 v = n.n;
		v.normalize();
		found = ClosestPoint3D_F64.closestPoint(g,new Point3D_F64(v.x,v.y,v.z),null);
		assertEquals(3,found.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.DOUBLE_TEST_TOL);

		// other side of normal
		found = ClosestPoint3D_F64.closestPoint(g,new Point3D_F64(3+v.x,4+v.y,v.z-5),null);
		assertEquals(3,found.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.DOUBLE_TEST_TOL);
	}
}
