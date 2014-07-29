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

	@Test
	public void acute() {
		assertEquals(Math.PI/2.0f,
				UtilVector3D_F32.acute(new Vector3D_F32(1,0,0),new Vector3D_F32(0,1,0)),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Math.PI/2.0f,
				UtilVector3D_F32.acute(new Vector3D_F32(1,0,0),new Vector3D_F32(0,0,1)),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Math.PI,
				UtilVector3D_F32.acute(new Vector3D_F32(1,0,0),new Vector3D_F32(-1,0,0)),GrlConstants.FLOAT_TEST_TOL);
	}
}
