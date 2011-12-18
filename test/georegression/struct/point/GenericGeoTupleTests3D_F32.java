/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
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

package georegression.struct.point;

import georegression.misc.GrlConstants;
import georegression.struct.GeoTuple3D_F32;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class GenericGeoTupleTests3D_F32<T extends GeoTuple3D_F32> extends GenericGeoTupleTests_F32<T> {

	private T seed;

	public GenericGeoTupleTests3D_F32( T seed ) {
		super( seed );
		this.seed = seed;
	}

	public void checkAll() {
		super.checkAll( 3 );
		checkGetAndSetAxis();
		checkSetAxisAll();
		isIdentical_3_float();
		isIdentical_tuple();
		isNaN();
	}

	public void checkGetAndSetAxis() {
		T a = (T) seed.createNewInstance();

		assertEquals( 0, a.getX(), GrlConstants.FLOAT_TEST_TOL );
		a.setX( 1.5f );
		assertEquals( 1.5f, a.getX(), GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 0, a.getY(), GrlConstants.FLOAT_TEST_TOL );
		a.setY( 1.5f );
		assertEquals( 1.5f, a.getY(), GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 0, a.getZ(), GrlConstants.FLOAT_TEST_TOL );
		a.setZ( 1.5f );
		assertEquals( 1.5f, a.getZ(), GrlConstants.FLOAT_TEST_TOL );
	}

	public void checkSetAxisAll() {
		T a = (T) seed.createNewInstance();

		a.set( 1.5f, 2.5f, 3.5f );

		assertEquals( 1.5f, a.getX(), GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 2.5f, a.getY(), GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 3.5f, a.getZ(), GrlConstants.FLOAT_TEST_TOL );
	}

	public void isIdentical_3_float() {
		T a = (T) seed.createNewInstance();

		a.set( 1, 2, 3 );

		assertTrue( a.isIdentical( 1, 2, 3, GrlConstants.FLOAT_TEST_TOL ) );
	}

	public void isIdentical_tuple() {
		T a = (T) seed.createNewInstance();

		a.set( 1, 2, 3 );

		T b = (T) a.copy();

		assertTrue( a.isIdentical( b, GrlConstants.FLOAT_TEST_TOL ) );
	}

	public void isNaN() {
		T a = (T) seed.createNewInstance();

		a.set( 1, 2, 3 );

		assertFalse( a.isNaN() );

		a.set( 1, 2, Float.NaN );
		assertTrue( a.isNaN() );
		a.set( 1, Float.NaN, 3 );
		assertTrue( a.isNaN() );
		a.set( Float.NaN, 1, 3 );
		assertTrue( a.isNaN() );
	}
}
