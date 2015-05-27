/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

package georegression.struct.point;

import georegression.geometry.GeometryMath_F32;
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
		assertEquals( 26, found, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void minus_a_b() {
		for( int i = 0; i < 20; i++ ) {
			Point3D_F32 a = randomPoint();
			Point3D_F32 b = randomPoint();
			Vector3D_F32 expected = new Vector3D_F32();
			Vector3D_F32 found = new Vector3D_F32();

			GeometryMath_F32.sub(a, b, expected);
			found.minus(a,b);
			GeometryUnitTest.assertEquals(expected,found,GrlConstants.FLOAT_TEST_TOL);
		}
	}

	@Test
	public void cross_a_b() {

		for( int i = 0; i < 20; i++ ) {
			Vector3D_F32 a = randomVector();
			Vector3D_F32 b = randomVector();
			Vector3D_F32 expected = new Vector3D_F32();
			Vector3D_F32 found = new Vector3D_F32();

			GeometryMath_F32.cross(a, b, expected);
			found.cross(a,b);
			GeometryUnitTest.assertEquals(expected,found,GrlConstants.FLOAT_TEST_TOL);
		}
	}

	@Test
	public void cross_b() {
		for( int i = 0; i < 20; i++ ) {
			Vector3D_F32 a = randomVector();
			Vector3D_F32 b = randomVector();
			Vector3D_F32 expected = new Vector3D_F32();

			GeometryMath_F32.cross(a, b, expected);
			GeometryUnitTest.assertEquals(expected,a.cross(b),GrlConstants.FLOAT_TEST_TOL);
		}
	}

	@Test
	public void normalize() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		float n = a.norm();
		a.normalize();

		assertEquals( 1.0f, a.norm(), GrlConstants.FLOAT_TEST_TOL );
		GeometryUnitTest.assertEquals( a, 1.0f / n, 2.0f / n, 3.0f / n, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void acute() {

		Vector3D_F32 a = new Vector3D_F32(1,0,0);

		assertEquals(Math.PI/2.0f,a.acute(new Vector3D_F32(0, 1, 0)),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Math.PI/2.0f,a.acute(new Vector3D_F32(0, 0, 1)),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Math.PI,a.acute(new Vector3D_F32(-1, 0, 0)),GrlConstants.FLOAT_TEST_TOL);
	}
}
