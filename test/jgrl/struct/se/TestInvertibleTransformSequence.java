/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.se;

import jgrl.misc.autocode.JgrlConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestInvertibleTransformSequence {


	@Test
	public void computeTransform() {
		InvertibleTransformSequence path = new InvertibleTransformSequence();

		path.addTransform( false, new Se2_F64( 1, 2, 0 ) );
		path.addTransform( true, new Se2_F64( 4, 6, 0 ) );

		Se2_F64 found = new Se2_F64();
		path.computeTransform( found );

		assertEquals( 3, found.getX(), JgrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 4, found.getY(), JgrlConstants.DOUBLE_TEST_TOL );
	}
}
