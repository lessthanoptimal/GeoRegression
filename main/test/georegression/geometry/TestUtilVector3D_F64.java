/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Peter Abeles
 */
public class TestUtilVector3D_F64 {

	Random rand = new Random( 23423 );

	@Test
	void isIdentical() {
		Vector3D_F64 a = UtilVector3D_F64.createRandom( -1, 1, rand );
		Vector3D_F64 b = UtilVector3D_F64.createRandom( -1, 1, rand );


		// test positive
		assertTrue( UtilVector3D_F64.isIdentical( a, a.copy(), GrlConstants.TEST_F64) );

		// test negative
		assertFalse( UtilVector3D_F64.isIdentical( a, b, GrlConstants.TEST_F64) );
	}

	@Test
	void normalize() {
		Vector3D_F64 a = new Vector3D_F64( 3, 3, 4 );

		UtilVector3D_F64.normalize( a );

		assertEquals( 1, a.norm(), GrlConstants.TEST_F64);
	}

	@Test
	void acute() {
		assertEquals(Math.PI/2.0,
				UtilVector3D_F64.acute(new Vector3D_F64(1,0,0),new Vector3D_F64(0,1,0)),GrlConstants.TEST_F64);
		assertEquals(Math.PI/2.0,
				UtilVector3D_F64.acute(new Vector3D_F64(1,0,0),new Vector3D_F64(0,0,1)),GrlConstants.TEST_F64);
		assertEquals(Math.PI,
				UtilVector3D_F64.acute(new Vector3D_F64(1,0,0),new Vector3D_F64(-1,0,0)),GrlConstants.TEST_F64);
	}

	@Test
	void perpendicularCanonical() {
//		perpendicularCanonical(new Vector3D_F64(1,0,0));
//		perpendicularCanonical(new Vector3D_F64(0,-2,0));
		perpendicularCanonical(new Vector3D_F64(0,0,3));
		perpendicularCanonical(new Vector3D_F64(0,0,0));
		perpendicularCanonical(new Vector3D_F64(0.1,-0.6,0));
		perpendicularCanonical(new Vector3D_F64(0.1,-0.6,2));

		// test very small
		perpendicularCanonical(new Vector3D_F64(10.2e-14,2.1e-14,-9.3e-14));

		for (int i = 0; i < 100; i++) {
			Vector3D_F64 v = new Vector3D_F64();
			v.x = rand.nextGaussian();
			v.y = rand.nextGaussian();
			v.z = rand.nextGaussian();

			perpendicularCanonical(v);
		}
	}

	public void perpendicularCanonical(Vector3D_F64 A ) {
		Vector3D_F64 found = UtilVector3D_F64.perpendicularCanonical(A,null);

		assertFalse(UtilEjml.isUncountable(found.x));
		assertFalse(UtilEjml.isUncountable(found.y));
		assertFalse(UtilEjml.isUncountable(found.z));

		double scale = Math.abs(A.x);
		scale = Math.max(scale,Math.abs(A.y));
		scale = Math.max(scale,Math.abs(A.z));

		if( scale == 0 ) {
			assertEquals(found.x, 0);
			assertEquals(found.y, 0);
			assertEquals(found.z, 0);
		} else {
			A.scale(1.0 / scale);

			scale = Math.abs(found.x);
			scale = Math.max(scale, Math.abs(found.y));
			scale = Math.max(scale, Math.abs(found.z));
			found.scale(1.0 / scale);

			assertTrue(Math.abs(A.dot(found)) <= GrlConstants.EPS);
		}
	}

	@Test
	void axisMaxMag() {
		assertEquals(0,UtilVector3D_F64.axisMaxMag(new Point3D_F64()));
		assertEquals(1,UtilVector3D_F64.axisMaxMag(new Point3D_F64(10,-11,0)));
		assertEquals(1,UtilVector3D_F64.axisMaxMag(new Point3D_F64(-10,11,0)));
		assertEquals(2,UtilVector3D_F64.axisMaxMag(new Point3D_F64(0,-0.5,1)));
	}
}
