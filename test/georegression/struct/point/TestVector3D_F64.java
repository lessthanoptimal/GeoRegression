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

package georegression.struct.point;

import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestVector3D_F64 extends GenericGeoTupleTests3D_F64 {
	public TestVector3D_F64() {
		super( new Vector3D_F64() );
	}

	@Test
	public void generic() {
		checkAll();
	}

	@Test
	public void dot() {
		Vector3D_F64 a = new Vector3D_F64( 1, 2, 3 );
		Vector3D_F64 b = new Vector3D_F64( 3, 4, 5 );

		double found = a.dot( b );
		assertEquals( 26, found, 13 - 8 );
	}

	@Test
	public void normalize() {
		Vector3D_F64 a = new Vector3D_F64( 1, 2, 3 );
		double n = a.norm();
		a.normalize();

		assertEquals( 1.0, a.norm(), GrlConstants.DOUBLE_TEST_TOL );
		GeometryUnitTest.assertEquals( a, 1.0 / n, 2.0 / n, 3.0 / n, GrlConstants.DOUBLE_TEST_TOL );
	}
}
