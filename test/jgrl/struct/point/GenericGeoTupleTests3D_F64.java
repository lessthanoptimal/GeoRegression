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
import jgrl.struct.GeoTuple3D_F64;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class GenericGeoTupleTests3D_F64<T extends GeoTuple3D_F64> extends GenericGeoTupleTests_F64<T> {

	private T seed;

	public GenericGeoTupleTests3D_F64(T seed) {
		super(seed);
		this.seed = seed;
	}

	public void checkAll() {
		super.checkAll(3);
		checkGetAndSetAxis();
		checkSetAxisAll();
		isIdentical_3_double();
		isIdentical_tuple();
		isNaN();
	}

	public void checkGetAndSetAxis() {
		T a = (T) seed.createNewInstance();

		assertEquals(0, a.getX(), JgrlConstants.DOUBLE_TEST_TOL);
		a.setX(1.5);
		assertEquals(1.5, a.getX(), JgrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0, a.getY(), JgrlConstants.DOUBLE_TEST_TOL);
		a.setY(1.5);
		assertEquals(1.5, a.getY(), JgrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0, a.getZ(), JgrlConstants.DOUBLE_TEST_TOL);
		a.setZ(1.5);
		assertEquals(1.5, a.getZ(), JgrlConstants.DOUBLE_TEST_TOL);
	}

	public void checkSetAxisAll() {
		T a = (T) seed.createNewInstance();

		a.set(1.5, 2.5, 3.5);

		assertEquals(1.5, a.getX(), JgrlConstants.DOUBLE_TEST_TOL);
		assertEquals(2.5, a.getY(), JgrlConstants.DOUBLE_TEST_TOL);
		assertEquals(3.5, a.getZ(), JgrlConstants.DOUBLE_TEST_TOL);
	}

	public void isIdentical_3_double() {
		T a = (T) seed.createNewInstance();

		a.set(1, 2, 3);

		assertTrue(a.isIdentical(1, 2, 3, JgrlConstants.DOUBLE_TEST_TOL));
	}

	public void isIdentical_tuple() {
		T a = (T) seed.createNewInstance();

		a.set(1, 2, 3);

		T b = (T) a.copy();

		assertTrue(a.isIdentical(b, JgrlConstants.DOUBLE_TEST_TOL));
	}

	public void isNaN() {
		T a = (T) seed.createNewInstance();

		a.set(1, 2, 3);

		assertFalse(a.isNaN());

		a.set(1, 2, Double.NaN);
		assertTrue(a.isNaN());
		a.set(1, Double.NaN, 3);
		assertTrue(a.isNaN());
		a.set(Double.NaN, 1, 3);
		assertTrue(a.isNaN());
	}
}
