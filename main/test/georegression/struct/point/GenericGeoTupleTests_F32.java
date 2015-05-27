/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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
import georegression.struct.GeoTuple_F32;

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
			assertEquals( 0, a.getIndex( i ), GrlConstants.FLOAT_TEST_TOL );
			a.setIndex( i, 2 );
			assertEquals( 2, a.getIndex( i ), GrlConstants.FLOAT_TEST_TOL );
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
		assertEquals( expected, a.norm(), GrlConstants.FLOAT_TEST_TOL );
	}

	public void checkNormSq() {
		T a = (T) seed.createNewInstance();
		float total = 0;
		for( int i = 0; i < a.getDimension(); i++ ) {
			a.setIndex( i, i + 1 );
			total += ( i + 1 ) * ( i + 1 );
		}

		assertEquals( total, a.normSq(), GrlConstants.FLOAT_TEST_TOL );
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
