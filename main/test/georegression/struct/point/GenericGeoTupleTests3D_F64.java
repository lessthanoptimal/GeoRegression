/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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
import georegression.struct.GeoTuple3D_F64;

import java.util.Random;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class GenericGeoTupleTests3D_F64 <T extends GeoTuple3D_F64> extends GenericGeoTupleTests_F64<T> {

	Random rand = new Random(234234);
	private T seed;

	public GenericGeoTupleTests3D_F64( T seed ) {
		super( seed );
		this.seed = seed;
	}

	public void checkAll() {
		super.checkAll( 3 );
		checkGetAndSetAxis();
		checkSetAxisAll();
		isIdentical_3_double();
		isIdentical_tuple();
		isNaN();
	}

	public void checkGetAndSetAxis() {
		T a = (T) seed.createNewInstance();

		assertEquals( 0, a.getX(), GrlConstants.TEST_F64);
		a.setX( 1.5 );
		assertEquals( 1.5, a.getX(), GrlConstants.TEST_F64);
		assertEquals( 0, a.getY(), GrlConstants.TEST_F64);
		a.setY( 1.5 );
		assertEquals( 1.5, a.getY(), GrlConstants.TEST_F64);
		assertEquals( 0, a.getZ(), GrlConstants.TEST_F64);
		a.setZ( 1.5 );
		assertEquals( 1.5, a.getZ(), GrlConstants.TEST_F64);
	}

	public void checkSetAxisAll() {
		T a = (T) seed.createNewInstance();

		a.set( 1.5, 2.5, 3.5 );

		assertEquals( 1.5, a.getX(), GrlConstants.TEST_F64);
		assertEquals( 2.5, a.getY(), GrlConstants.TEST_F64);
		assertEquals( 3.5, a.getZ(), GrlConstants.TEST_F64);
	}

	public void isIdentical_3_double() {
		T a = (T) seed.createNewInstance();

		a.set( 1, 2, 3 );

		assertTrue( a.isIdentical( 1, 2, 3, GrlConstants.TEST_F64) );
	}

	public void isIdentical_tuple() {
		T a = (T) seed.createNewInstance();

		a.set( 1, 2, 3 );

		T b = (T) a.copy();

		assertTrue( a.isIdentical( b, GrlConstants.TEST_F64) );
	}

	public void isNaN() {
		T a = (T) seed.createNewInstance();

		a.set( 1, 2, 3 );

		assertFalse( a.isNaN() );

		a.set( 1, 2, Double.NaN );
		assertTrue( a.isNaN() );
		a.set( 1, Double.NaN, 3 );
		assertTrue( a.isNaN() );
		a.set( Double.NaN, 1, 3 );
		assertTrue( a.isNaN() );
	}

	protected Vector3D_F64 randomVector() {
		double x = (rand.nextDouble()-0.5)*2.0;
		double y = (rand.nextDouble()-0.5)*2.0;
		double z = (rand.nextDouble()-0.5)*2.0;

		return new Vector3D_F64(x,y,z);
	}

	protected Point3D_F64 randomPoint() {
		double x = (rand.nextDouble()-0.5)*2.0;
		double y = (rand.nextDouble()-0.5)*2.0;
		double z = (rand.nextDouble()-0.5)*2.0;

		return new Point3D_F64(x,y,z);
	}
}
