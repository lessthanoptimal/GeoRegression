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
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Vector3D_F32;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
public class TestUtilVector3D_F32 {

	Random rand = new Random( 23423 );

	@Test
	public void isIdentical() {
		Vector3D_F32 a = UtilVector3D_F32.createRandom( -1, 1, rand );
		Vector3D_F32 b = UtilVector3D_F32.createRandom( -1, 1, rand );


		// test positive
		assertTrue( UtilVector3D_F32.isIdentical( a, a.copy(), GrlConstants.FLOAT_TEST_TOL ) );

		// test negative
		assertFalse( UtilVector3D_F32.isIdentical( a, b, GrlConstants.FLOAT_TEST_TOL ) );
	}

	@Test
	public void normalize() {
		Vector3D_F32 a = new Vector3D_F32( 3, 3, 4 );

		UtilVector3D_F32.normalize( a );

		assertEquals( 1, a.norm(), GrlConstants.FLOAT_TEST_TOL );
	}
}
