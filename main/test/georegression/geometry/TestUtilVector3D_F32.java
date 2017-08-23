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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Vector3D_F32;
import org.ejml.UtilEjml;
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
		assertTrue( UtilVector3D_F32.isIdentical( a, a.copy(), GrlConstants.TEST_F32) );

		// test negative
		assertFalse( UtilVector3D_F32.isIdentical( a, b, GrlConstants.TEST_F32) );
	}

	@Test
	public void normalize() {
		Vector3D_F32 a = new Vector3D_F32( 3, 3, 4 );

		UtilVector3D_F32.normalize( a );

		assertEquals( 1, a.norm(), GrlConstants.TEST_F32);
	}

	@Test
	public void acute() {
		assertEquals(Math.PI/2.0f,
				UtilVector3D_F32.acute(new Vector3D_F32(1,0,0),new Vector3D_F32(0,1,0)),GrlConstants.TEST_F32);
		assertEquals(Math.PI/2.0f,
				UtilVector3D_F32.acute(new Vector3D_F32(1,0,0),new Vector3D_F32(0,0,1)),GrlConstants.TEST_F32);
		assertEquals(Math.PI,
				UtilVector3D_F32.acute(new Vector3D_F32(1,0,0),new Vector3D_F32(-1,0,0)),GrlConstants.TEST_F32);
	}

	@Test
	public void perpendicularCanonical() {
//		perpendicularCanonical(new Vector3D_F32(1,0,0));
//		perpendicularCanonical(new Vector3D_F32(0,-2,0));
		perpendicularCanonical(new Vector3D_F32(0,0,3));
		perpendicularCanonical(new Vector3D_F32(0,0,0));
		perpendicularCanonical(new Vector3D_F32(0.1f,-0.6f,0));
		perpendicularCanonical(new Vector3D_F32(0.1f,-0.6f,2));

		// test very small
		perpendicularCanonical(new Vector3D_F32(10.2e-14f,2.1e-14f,-9.3e-14f));

		for (int i = 0; i < 100; i++) {
			Vector3D_F32 v = new Vector3D_F32();
			v.x = (float)rand.nextGaussian();
			v.y = (float)rand.nextGaussian();
			v.z = (float)rand.nextGaussian();

			perpendicularCanonical(v);
		}
	}

	public void perpendicularCanonical(Vector3D_F32 A ) {
		Vector3D_F32 found = UtilVector3D_F32.perpendicularCanonical(A,null);

		assertFalse(UtilEjml.isUncountable(found.x));
		assertFalse(UtilEjml.isUncountable(found.y));
		assertFalse(UtilEjml.isUncountable(found.z));

		float scale = (float)Math.abs(A.x);
		scale = (float)Math.max(scale,Math.abs(A.y));
		scale = (float)Math.max(scale,Math.abs(A.z));

		if( scale == 0 ) {
			assertTrue(0 == found.x);
			assertTrue(0 == found.y);
			assertTrue(0 == found.z);
		} else {
			A.scale(1.0f / scale);

			scale = (float)Math.abs(found.x);
			scale = (float)Math.max(scale, (float)Math.abs(found.y));
			scale = (float)Math.max(scale, (float)Math.abs(found.z));
			found.scale(1.0f / scale);

			assertTrue(Math.abs(A.dot(found)) <= GrlConstants.F_EPS);
		}
	}
}
