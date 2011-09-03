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

package jgrl.struct.point;

import jgrl.misc.autocode.JgrlConstants;
import jgrl.struct.GeoTuple_F32;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class GenericGeoTupleTests_F32<T extends GeoTuple_F32> {

	private T seed;

	public GenericGeoTupleTests_F32( T seed ) {
		this.seed = seed;
	}

	public void checkAll( int dimension ) {
		checkCreateNewInstance();
		checkDimension( dimension );
		checkSetAndGetIndex();
		checkNorm();
		checkNormSq();
		checkCopy();
	}

	public void checkCreateNewInstance() {
		T a = (T) seed.createNewInstance();

		assertTrue( a != null );
		assertTrue( a.getClass() == seed.getClass() );
	}

	public void checkDimension( int expected ) {
		T a = (T) seed.createNewInstance();

		assertEquals( a.getDimension(), expected );
	}

	public void checkSetAndGetIndex() {

		T a = (T) seed.createNewInstance();

		for( int i = 0; i < a.getDimension(); i++ ) {
			assertEquals( 0, a.getIndex( i ), JgrlConstants.FLOAT_TEST_TOL );
			a.setIndex( i, 2 );
			assertEquals( 2, a.getIndex( i ), JgrlConstants.FLOAT_TEST_TOL );
		}
	}

	public void checkNorm() {
		T a = (T) seed.createNewInstance();
		float total = 0;
		for( int i = 0; i < a.getDimension(); i++ ) {
			a.setIndex( i, i + 1 );
			total += ( i + 1 ) * ( i + 1 );
		}

		float expected = (float)Math.sqrt( total );
		assertEquals( expected, a.norm(), JgrlConstants.FLOAT_TEST_TOL );
	}

	public void checkNormSq() {
		T a = (T) seed.createNewInstance();
		float total = 0;
		for( int i = 0; i < a.getDimension(); i++ ) {
			a.setIndex( i, i + 1 );
			total += ( i + 1 ) * ( i + 1 );
		}

		assertEquals( total, a.normSq(), JgrlConstants.FLOAT_TEST_TOL );
	}

	public void checkCopy() {
		T a = (T) seed.createNewInstance();
		for( int i = 0; i < a.getDimension(); i++ ) {
			a.setIndex( i, i + 1 );
		}

		T b = (T) a.copy();

		assertTrue( a != b );
		for( int i = 0; i < a.getDimension(); i++ ) {
			assertTrue( a.getIndex( i ) == b.getIndex( i ) );
		}
	}


}
