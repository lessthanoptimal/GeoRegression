/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.metric;

import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestClosestPoint3D_F32 {
	/**
	 * Compute truth from 3 random points then see if the 3rd point is found again.
	 */
	@Test
	public void closestPoint_line() {
		Point3D_F32 a = new Point3D_F32( 1, 1, 1 );
		Point3D_F32 b = new Point3D_F32( 1.5f, -2.5f, 9 );
		Point3D_F32 c = new Point3D_F32( 10.1f, 6, -3 );

		Vector3D_F32 va = new Vector3D_F32( a, b );
		Vector3D_F32 vc = new Vector3D_F32( c, b );

		LineParametric3D_F32 lineA = new LineParametric3D_F32( a, va );
		LineParametric3D_F32 lineB = new LineParametric3D_F32( c, vc );

		Point3D_F32 foundB = ClosestPoint3D_F32.closestPoint(lineA, lineB, null);

		assertTrue( b.isIdentical( foundB, GrlConstants.FLOAT_TEST_TOL ) );
	}

	@Test
	public void closestPoints_lines() {
		Point3D_F32 a = new Point3D_F32( 1, 1, 1 );
		Point3D_F32 b = new Point3D_F32( 1.5f, -2.5f, 9 );
		Point3D_F32 c = new Point3D_F32( 10.1f, 6, -3 );

		Vector3D_F32 va = new Vector3D_F32( a, b );
		Vector3D_F32 vc = new Vector3D_F32( c, b );
		// normalize the vector so that the value 't' is euclidean distance
		va.normalize();
		vc.normalize();

		LineParametric3D_F32 lineA = new LineParametric3D_F32( a, va );
		LineParametric3D_F32 lineB = new LineParametric3D_F32( c, vc );

		float param[] = new float[2];

		assertTrue(ClosestPoint3D_F32.closestPoints(lineA, lineB, param));

		assertEquals( a.distance(b) , param[0] , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( c.distance(b) , param[1] , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void closestPoint_point() {
		Point3D_F32 a = new Point3D_F32( 1, 1, 1 );
		Point3D_F32 b = new Point3D_F32( 1.5f, -2.5f, 9 );
		Point3D_F32 c = new Point3D_F32( 10.1f, 6, -3 );

		Vector3D_F32 va = new Vector3D_F32( a, b );

		LineParametric3D_F32 lineA = new LineParametric3D_F32( a, va );

		Point3D_F32 foundB = ClosestPoint3D_F32.closestPoint(lineA, c, null);

		Vector3D_F32 p = new Vector3D_F32( foundB, c );

		// see if they are perpendicular and therefor c foundB is the closest point
		float d = p.dot( va );

		assertEquals( 0, d, GrlConstants.FLOAT_TEST_TOL );
	}
}
