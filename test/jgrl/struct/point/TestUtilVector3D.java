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

package jgrl.struct.point;

import jgrl.autocode.JgrlConstants;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
public class TestUtilVector3D {

	Random rand = new Random(23423);

	@Test
	public void isIdentical() {
		Vector3D_F64 a = UtilVector3D.createRandom64(-1, 1, rand);
		Vector3D_F64 b = UtilVector3D.createRandom64(-1, 1, rand);


		// test positive
		assertTrue(UtilVector3D.isIdentical(a, a.copy(), JgrlConstants.DOUBLE_TEST_TOL));

		// test negative
		assertFalse(UtilVector3D.isIdentical(a, b, JgrlConstants.DOUBLE_TEST_TOL));
	}

	@Test
	public void normalize() {
		Vector3D_F64 a = new Vector3D_F64(3, 3, 4);

		UtilVector3D.normalize(a);

		assertEquals(1, a.norm(), JgrlConstants.DOUBLE_TEST_TOL);
	}
}
