/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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
import georegression.struct.GeoTuple_F64;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class GenericGeoTupleTests_F64 <T extends GeoTuple_F64> {

	private T seed;

	public GenericGeoTupleTests_F64( T seed ) {
		this.seed = seed;
	}

	public void checkAll( int dimension ) {
		checkCreateNewInstance();
		checkDimension( dimension );
		checkSetAndGetIndex();
		checkNorm();
		checkNormSq();
		checkCopy();
		checkEquals();
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
			assertEquals( 0, a.getIdx( i ), GrlConstants.TEST_F64);
			a.setIdx( i, 2 );
			assertEquals( 2, a.getIdx( i ), GrlConstants.TEST_F64);
		}
	}

	public void checkNorm() {
		T a = (T) seed.createNewInstance();
		double total = 0;
		for( int i = 0; i < a.getDimension(); i++ ) {
			a.setIdx( i, i + 1 );
			total += ( i + 1 ) * ( i + 1 );
		}

		double expected = Math.sqrt( total );
		assertEquals( expected, a.norm(), GrlConstants.TEST_F64);
	}

	public void checkNormSq() {
		T a = (T) seed.createNewInstance();
		double total = 0;
		for( int i = 0; i < a.getDimension(); i++ ) {
			a.setIdx( i, i + 1 );
			total += ( i + 1 ) * ( i + 1 );
		}

		assertEquals( total, a.normSq(), GrlConstants.TEST_F64);
	}

	public void checkCopy() {
		T a = (T) seed.createNewInstance();
		for( int i = 0; i < a.getDimension(); i++ ) {
			a.setIdx( i, i + 1 );
		}

		T b = (T) a.copy();

		assertTrue( a != b );
		for( int i = 0; i < a.getDimension(); i++ ) {
			assertTrue( a.getIdx( i ) == b.getIdx( i ) );
		}
	}

	public void checkEquals() {
		T a = (T) seed.createNewInstance();
		T b = (T) seed.createNewInstance();

		assertTrue( a != b );
		assertTrue( a.equals(b) );

		for( int i = 0; i < a.getDimension(); i++ ) {
			a.setIdx(i, 2);
			assertFalse(a.equals(b));
			b.setIdx(i, 2);
			assertTrue(a.equals(b));
		}
	}

}
