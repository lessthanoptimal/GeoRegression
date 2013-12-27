/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.misc.test;

import georegression.geometry.UtilPoint2D_F64;
import georegression.struct.*;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import georegression.struct.se.Se2_F32;
import georegression.struct.se.Se2_F64;


/**
 * @author Peter Abeles
 */
public class GeometryUnitTest {

	public static void assertEquals( Se2_F64 expected, Se2_F64 found, double tolTran, double tolyaw ) {
		assertEquals( expected.getTranslation(), found.getTranslation(), tolTran );
		assertEquals( expected.getYaw(), found.getYaw(), tolyaw, "yaw" );
	}

	public static void assertEquals( Se2_F32 expected, Se2_F32 found, double tolTran, double tolyaw ) {
		assertEquals( expected.getTranslation(), found.getTranslation(), tolTran );
		assertEquals( expected.getYaw(), found.getYaw(), tolyaw, "yaw" );
	}

	/**
	 * Sees if every parameter in expected is not each to each other.  Note that this test
	 * passes if only one is not equals.
	 *
	 * @param expected The expected transform.
	 * @param found	The found transform.
	 * @param tolTran  tolerance used for translational parameters.
	 * @param tolYaw   tolerance used for rotational parameters.
	 */
	public static void assertNotEquals( Se2_F64 expected, Se2_F64 found, double tolTran, double tolYaw ) {
		if( !UtilPoint2D_F64.isEquals( expected.getTranslation(), found.getTranslation(), tolTran ) )
			return;

		assertNotEquals( expected.getYaw(), found.getYaw(), tolYaw, "yaw" );
	}

	public static void assertEquals( GeoTuple3D_F64 expected, GeoTuple3D_F64 found, double tol ) {
		assertEquals( expected.getX(), found.getX(), tol, "x-axis is not equals." );
		assertEquals( expected.getY(), found.getY(), tol, "y-axis is not equals." );
		assertEquals( expected.getZ(), found.getZ(), tol, "z-axis is not equals." );
	}

	public static void assertEquals( GeoTuple3D_F32 expected, GeoTuple3D_F32 found, float tol ) {
		assertEquals( expected.getX(), found.getX(), tol, "x-axis is not equals." );
		assertEquals( expected.getY(), found.getY(), tol, "y-axis is not equals." );
		assertEquals( expected.getZ(), found.getZ(), tol, "z-axis is not equals." );
	}

	public static void assertEquals( GeoTuple2D_F64 expected, GeoTuple2D_F64 found, double tol ) {
		assertEquals( expected.getX(), found.getX(), tol, "x-axis is not equals." );
		assertEquals( expected.getY(), found.getY(), tol, "y-axis is not equals." );
	}

	public static void assertEquals( GeoTuple2D_F32 expected, GeoTuple2D_F32 found, double tol ) {
		assertEquals( expected.getX(), found.getX(), tol, "x-axis is not equals." );
		assertEquals( expected.getY(), found.getY(), tol, "y-axis is not equals." );
	}

	public static void assertEquals( Point2D_I32 expected, Point2D_I32 found ) {
		assertEquals(expected.getX(), found.getX(), "x-axis is not equal.");
		assertEquals(expected.getY(), found.getY(), "y-axis is not equal.");
	}

	public static void assertEquals( int x , int y, Point2D_I32 found ) {
		assertEquals(x, found.getX(), "x-axis is not equal.");
		assertEquals(y, found.getY(), "y-axis is not equal.");
	}

	public static void assertEquals( float x , float y, Point2D_F32 found , float tol ) {
		assertEquals(x, found.getX(), tol, "x-axis is not equal.");
		assertEquals(y, found.getY(), tol, "y-axis is not equal.");
	}

	public static void assertEquals( double x , double y, Point2D_F64 found , double tol ) {
		assertEquals(x, found.getX(), tol, "x-axis is not equal.");
		assertEquals(y, found.getY(), tol, "y-axis is not equal.");
	}

	public static void assertNotEquals( GeoTuple2D_F64 expected, GeoTuple2D_F64 found, double tol ) {
		assertNotEquals( expected.getX(), found.getX(), tol, "x-axis is equal." );
		assertNotEquals( expected.getY(), found.getY(), tol, "y-axis is equal." );
	}

	public static void assertEquals( GeoTuple3D_F64 a, double x, double y, double z, double tol ) {
		assertEquals( a.getX(), x, tol, "x-axis is not equals." );
		assertEquals( a.getY(), y, tol, "y-axis is not equals." );
		assertEquals( a.getZ(), z, tol, "z-axis is not equals." );
	}

	public static void assertEquals( GeoTuple3D_F32 a, float x, float y, float z, float tol ) {
		assertEquals( a.getX(), x, tol, "x-axis is not equals." );
		assertEquals( a.getY(), y, tol, "y-axis is not equals." );
		assertEquals( a.getZ(), z, tol, "z-axis is not equals." );
	}

	public static void assertEquals( GeoTuple3D_F32 expected, GeoTuple3D_F32 found, double tol ) {
		assertEquals( expected.getX(), found.getX(), tol, "x-axis is equal." );
		assertEquals( expected.getY(), found.getY(), tol, "y-axis is equal." );
		assertEquals( expected.getZ(), found.getZ(), tol, "z-axis is equal." );
	}

	public static void assertNotEquals( GeoTuple3D_F32 expected, GeoTuple3D_F32 found, double tol ) {
		if( Math.abs(expected.getX() - found.getX()) <= tol &&
				Math.abs(expected.getY() - found.getY()) <= tol &&
				Math.abs(expected.getZ() - found.getZ()) <= tol ) {
			throw new RuntimeException("Points are equal to each other!");
		}
	}

	public static void assertNotEquals( GeoTuple3D_F64 expected, GeoTuple3D_F64 found, double tol ) {
		if( Math.abs(expected.getX() - found.getX()) <= tol &&
				Math.abs(expected.getY() - found.getY()) <= tol &&
				Math.abs(expected.getZ() - found.getZ()) <= tol ) {
			throw new RuntimeException("Points are equal to each other!");
		}
	}

	public static void assertEquals( GeoTuple_F64 a, GeoTuple_F64 b, double tol ) {
		assertTrue( a.getClass() == b.getClass(), "a and b are not the same type." );

		int N = a.getDimension();
		for( int i = 0; i < N; i++ ) {
			assertEquals( a.getIndex( i ), b.getIndex( i ), tol, "Index " + i + " is not the same." );
		}
	}

	public static void assertEquals( int valueA, int valueB, String message ) {
		if( valueA != valueB)
			throw new RuntimeException( message + " " + valueA + "  " + valueB );
	}

	public static void assertEquals( double valueA, double valueB, double tol, String message ) {
		if( Math.abs( valueA - valueB ) > tol )
			throw new RuntimeException( message + " " + valueA + "  " + valueB );
	}

	public static void assertNotEquals( double valueA, double valueB, double tol, String message ) {
		if( Math.abs( valueA - valueB ) <= tol )
			throw new RuntimeException( message + " " + valueA + "  " + valueB );
	}

	public static void assertTrue( boolean value, String message ) {
		if( !value )
			throw new RuntimeException( message );
	}
}
