/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
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

package georegression.struct.point;

import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestVector3D_F32 extends GenericGeoTupleTests3D_F32 {
	public TestVector3D_F32() {
		super( new Vector3D_F32() );
	}

	@Test
	public void generic() {
		checkAll();
	}

	@Test
	public void dot() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 3, 4, 5 );

		float found = a.dot( b );
		assertEquals( 26, found, 13 - 8 );
	}

	@Test
	public void normalize() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		float n = a.norm();
		a.normalize();

		assertEquals( 1.0f, a.norm(), GrlConstants.FLOAT_TEST_TOL );
		GeometryUnitTest.assertEquals( a, 1.0f / n, 2.0f / n, 3.0f / n, GrlConstants.FLOAT_TEST_TOL );
	}
}
