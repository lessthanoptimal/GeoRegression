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
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
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
