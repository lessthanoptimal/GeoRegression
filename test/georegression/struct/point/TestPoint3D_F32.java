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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestPoint3D_F32 extends GenericGeoTupleTests3D_F32<Point3D_F32> {

	public TestPoint3D_F32() {
		super( new Point3D_F32() );
	}

	@Test
	public void generic() {
		checkAll();
	}

	@Test
	public void plus_pt() {
		Point3D_F32 a = new Point3D_F32( 1, 2, 3 );
		Point3D_F32 b = new Point3D_F32( 1, 2, 3 );

		Point3D_F32 c = a.plus( b );

		assertEquals( 2, c.getX(), GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 4, c.getY(), GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 6, c.getZ(), GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void plus_v() {
		Point3D_F32 a = new Point3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 1, 2, 3 );

		Point3D_F32 c = a.plus( b );

		assertEquals( 2, c.getX(), GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 4, c.getY(), GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 6, c.getZ(), GrlConstants.FLOAT_TEST_TOL );
	}
}
