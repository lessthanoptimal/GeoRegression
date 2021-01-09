/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.GeometryMath_F64;
import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestVector3D_F64 extends GenericGeoTupleTests3D_F64 {

	public TestVector3D_F64() {
		super( new Vector3D_F64() );
	}

	@Test
	void generic() {
		checkAll();
	}

	@Test
	void dot() {
		Vector3D_F64 a = new Vector3D_F64( 1, 2, 3 );
		Vector3D_F64 b = new Vector3D_F64( 3, 4, 5 );

		double found = a.dot( b );
		assertEquals( 26, found, GrlConstants.TEST_F64);
	}

	@Test
	void minus_a_b() {
		for( int i = 0; i < 20; i++ ) {
			Point3D_F64 a = randomPoint();
			Point3D_F64 b = randomPoint();
			Vector3D_F64 expected = new Vector3D_F64();
			Vector3D_F64 found = new Vector3D_F64();

			GeometryMath_F64.sub(a, b, expected);
			found.minus(a,b);
			GeometryUnitTest.assertEquals(expected,found,GrlConstants.TEST_F64);
		}
	}

	@Test
	void cross_a_b() {

		for( int i = 0; i < 20; i++ ) {
			Vector3D_F64 a = randomVector();
			Vector3D_F64 b = randomVector();
			Vector3D_F64 expected = new Vector3D_F64();
			Vector3D_F64 found = new Vector3D_F64();

			GeometryMath_F64.cross(a, b, expected);
			found.crossSetTo(a,b);
			GeometryUnitTest.assertEquals(expected,found,GrlConstants.TEST_F64);
		}
	}

	@Test
	void cross_b() {
		for( int i = 0; i < 20; i++ ) {
			Vector3D_F64 a = randomVector();
			Vector3D_F64 b = randomVector();
			Vector3D_F64 expected = new Vector3D_F64();

			GeometryMath_F64.cross(a, b, expected);
			GeometryUnitTest.assertEquals(expected,a.crossWith(b),GrlConstants.TEST_F64);
		}
	}

	@Test
	void normalize() {
		Vector3D_F64 a = new Vector3D_F64( 1, 2, 3 );
		double n = a.norm();
		a.normalize();

		assertEquals( 1.0, a.norm(), GrlConstants.TEST_F64);
		GeometryUnitTest.assertEquals( a, 1.0 / n, 2.0 / n, 3.0 / n, GrlConstants.TEST_F64);
	}

	@Test
	void normalize_overflow() {
		Vector3D_F64 a = new Vector3D_F64( Double.MAX_VALUE, Double.MAX_VALUE/2, Double.MAX_VALUE/3 );
		a.normalize();

		Vector3D_F64 b = new Vector3D_F64(1, 1.0/2, 1.0/3 );
		b.normalize();

		GeometryUnitTest.assertEquals( a, b.x,b.y,b.z, GrlConstants.TEST_F64);
	}

	@Test
	void acute() {

		Vector3D_F64 a = new Vector3D_F64(1,0,0);

		assertEquals(Math.PI/2.0,a.acute(new Vector3D_F64(0, 1, 0)),GrlConstants.TEST_F64);
		assertEquals(Math.PI/2.0,a.acute(new Vector3D_F64(0, 0, 1)),GrlConstants.TEST_F64);
		assertEquals(Math.PI,a.acute(new Vector3D_F64(-1, 0, 0)),GrlConstants.TEST_F64);
	}
}
