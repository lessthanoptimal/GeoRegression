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

package georegression.struct.se;

import georegression.misc.GrlConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestInvertibleTransformSequence {


	/**
	 * Test a sequence where it goes forward and backwards
	 */
	@Test
	public void computeTransform() {
		InvertibleTransformSequence path = new InvertibleTransformSequence();

		path.addTransform( false, new Se2_F64( 1, 2, 0 ) );
		path.addTransform( true, new Se2_F64( 4, 6, 0 ) );

		Se2_F64 found = new Se2_F64();
		path.computeTransform( found );

		assertEquals( 3, found.getX(), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 4, found.getY(), GrlConstants.DOUBLE_TEST_TOL );
	}

	/**
	 * Test a sequence where it goes forward twice.  This exposed a bug
	 * where a unique instances were not being passed in to concat().
	 */
	@Test
	public void computeTransform2() {
		InvertibleTransformSequence path = new InvertibleTransformSequence();

		path.addTransform( true, new Se2_F64( 1, 2, Math.PI/2.0 ) );
		path.addTransform( true, new Se2_F64( 4, 6, Math.PI/2.0 ) );

		Se2_F64 found = new Se2_F64();
		path.computeTransform( found );

		assertEquals( 2, found.getX(), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 7, found.getY(), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( Math.PI, found.getYaw(), GrlConstants.DOUBLE_TEST_TOL );
	}
}
