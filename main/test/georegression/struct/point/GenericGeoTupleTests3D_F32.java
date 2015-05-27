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
import georegression.struct.GeoTuple3D_F32;

import java.util.Random;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class GenericGeoTupleTests3D_F32<T extends GeoTuple3D_F32> extends GenericGeoTupleTests_F32<T> {

	Random rand = new Random(234234);
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

	protected Vector3D_F32 randomVector() {
		float x = (rand.nextFloat()-0.5f)*2.0f;
		float y = (rand.nextFloat()-0.5f)*2.0f;
		float z = (rand.nextFloat()-0.5f)*2.0f;

		return new Vector3D_F32(x,y,z);
	}

	protected Point3D_F32 randomPoint() {
		float x = (rand.nextFloat()-0.5f)*2.0f;
		float y = (rand.nextFloat()-0.5f)*2.0f;
		float z = (rand.nextFloat()-0.5f)*2.0f;

		return new Point3D_F32(x,y,z);
	}
}
