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

package georegression.metric;

import georegression.misc.GrlConstants;
import org.junit.Test;

import static georegression.metric.UtilAngle.*;
import static georegression.misc.GrlConstants.*;
import static java.lang.Math.PI;
import static org.junit.Assert.*;

public class TestUtilAngle {

	@Test
	public void atanSafe() {
		assertEquals(Math.PI/2.0,UtilAngle.atanSafe(0.0,0.0),1e-8);
		assertEquals(Math.PI/2.0,UtilAngle.atanSafe(1.0,0.0),1e-8);
		assertEquals(-Math.PI/2.0,UtilAngle.atanSafe(-1.0,0.0),1e-8);
		assertEquals(Math.atan(1),UtilAngle.atanSafe(1.0,1.0),1e-8);
	}

	@Test
	public void toHalfCircle() {
		assertEquals(0,UtilAngle.toHalfCircle(0),1e-8);
		assertEquals(0.1,UtilAngle.toHalfCircle(0.1),1e-8);
		assertEquals(1.9-Math.PI,UtilAngle.toHalfCircle(1.9),1e-8);
		assertEquals(0,UtilAngle.toHalfCircle(Math.PI),1e-8);
		assertEquals(0.1,UtilAngle.toHalfCircle(0.1-Math.PI),1e-8);
		assertEquals(0.9,UtilAngle.toHalfCircle(0.9-Math.PI),1e-8);
	}

	@Test
	public void isStandardDomain() {
		assertTrue( UtilAngle.isStandardDomain( 0.1 ) );
		assertTrue( UtilAngle.isStandardDomain( -0.1 ) );
		assertFalse( UtilAngle.isStandardDomain( 4 ) );
		assertFalse( UtilAngle.isStandardDomain( -4 ) );
	}

	/**
	 * Test the bound function by providing several test cases.
	 */
	@Test
	public void testBound() {
		assertEquals( PI * 0.5, bound( PI * 2.5 ), 1e-5 );
		assertEquals( -PI * 0.5, bound( -PI * 2.5 ), 1e-5 );
		assertEquals( PI * 0.5, bound( -PI * 1.5 ), 1e-5 );
		assertEquals( -PI * 0.5, bound( PI * 1.5 ), 1e-5 );
		assertEquals( PI * 0.3, bound( PI * 8.3 ), 1e-5 );
		assertEquals( PI * 0.3, bound( PI * 0.3 ), 1e-5 );
		assertEquals( -PI * 0.9, bound( -PI * 0.9 ), 1e-5 );
	}

	@Test
	public void testBoundHalf_F64() {
		assertEquals( -0.1, boundHalf( PI - 0.1 ), 1e-5 );
		assertEquals(  0.1, boundHalf( -PI + 0.1 ), 1e-5 );
		assertEquals( PId2-0.1, boundHalf( PId2-0.1 ), 1e-5 );
		assertEquals( -PId2+0.1, boundHalf( -PId2+0.1 ), 1e-5 );

		assertEquals( -0.1, boundHalf( 3*PI - 0.1 ), 1e-5 );
		assertEquals(  0.1, boundHalf( -3*PI + 0.1 ), 1e-5 );
	}

	@Test
	public void testBoundHalf_F32() {
		assertEquals( -0.1f, boundHalf( F_PI - 0.1f ), 1e-5f );
		assertEquals(  0.1f, boundHalf( -F_PI + 0.1f ), 1e-5f );
		assertEquals( F_PId2-0.1f, boundHalf( F_PId2-0.1f ), 1e-5f );
		assertEquals( -F_PId2+0.1f, boundHalf( -F_PId2+0.1f ), 1e-5f );

		assertEquals( -0.1f, boundHalf( 3*F_PI - 0.1f ), 1e-5f );
		assertEquals(  0.1f, boundHalf( -3*F_PI + 0.1f ), 1e-5f );
	}

	@Test
	public void testDistanceCCW_F64() {
		assertEquals( PI * 1.5, distanceCCW(-0.75 * PI, 0.75 * PI), GrlConstants.TEST_F64);
		assertEquals( PI * 0.5, distanceCCW( 0.75 * PI, -0.75 * PI), GrlConstants.TEST_F64);
		assertEquals( 0, distanceCCW(1,1), GrlConstants.TEST_F64);
	}

	@Test
	public void testDistanceCCW_F32() {
		assertEquals( F_PI * 1.5f, distanceCCW(-0.75f * F_PI, 0.75f * F_PI), GrlConstants.TEST_F32);
		assertEquals( F_PI * 0.5f, distanceCCW( 0.75f * F_PI, -0.75f * F_PI), GrlConstants.TEST_F32);
		assertEquals( 0f, distanceCCW(1f,1f), GrlConstants.TEST_F32);
	}

	@Test
	public void testDistanceCW_F64() {
		assertEquals( PI * 0.5, distanceCW(-0.75 * PI, 0.75 * PI), GrlConstants.TEST_F64);
		assertEquals( PI * 1.5, distanceCW(0.75 * PI, -0.75 * PI), GrlConstants.TEST_F64);
		assertEquals( 0, distanceCW(1, 1), GrlConstants.TEST_F64);
	}

	@Test
	public void testDistanceCW_F32() {
		assertEquals( F_PI * 0.5f, distanceCW(-0.75f * F_PI, 0.75f * F_PI), GrlConstants.TEST_F32);
		assertEquals( F_PI * 1.5f, distanceCW(0.75f * F_PI, -0.75f * F_PI), GrlConstants.TEST_F32);
		assertEquals( 0, distanceCW(1f, 1f), GrlConstants.TEST_F32);
	}

	@Test
	public void testMinus() {
		assertEquals( -0.1, minus( 0.1, 0.2 ), 1e-5 );
		assertEquals( 0.1, minus( 0.2, 0.1 ), 1e-5 );
		assertEquals( 0.1, minus( -0.1, -0.2 ), 1e-5 );
		assertEquals( -0.1, minus( -0.2, -0.1 ), 1e-5 );
		assertEquals( PI, minus( PI, 0 ), 1e-5 );
		assertEquals( -PI, minus( -PI, 0 ), 1e-5 );
		assertEquals( 0, minus( -PI, PI ), 1e-5 );
		assertEquals( 0, minus( PI, -PI ), 1e-5 );
		assertEquals( PI, minus( 0.1 * PI, -0.9 * PI ), 1e-5 );
		assertEquals( -PI, minus( -0.9 * PI, 0.1 * PI ), 1e-5 );
		assertEquals( 0.2 * PI, minus( 0.9 * PI, -0.9 * PI ), 1e-5 );
		assertEquals( -0.2 * PI, minus( -0.9 * PI, 0.9 * PI ), 1e-5 );
	}

	@Test
	public void testDist_F64() {
		assertEquals(0,dist(0,0),1e-8);
		assertEquals(0,dist(1,1),1e-8);
		assertEquals(2,dist(-1,1),1e-8);
		assertEquals(0,dist(-Math.PI,Math.PI),1e-8);
		assertEquals(0.2,dist(-Math.PI+0.1,Math.PI-0.1),1e-8);
	}

	@Test
	public void testDist_F32() {
		assertEquals(0f,dist(0f,0f),1e-4f);
		assertEquals(0f,dist(1f,1f),1e-4f);
		assertEquals(2f,dist(-1f,1f),1e-4f);
		assertEquals(0f,dist((float)-Math.PI,Math.PI),1e-5);
		assertEquals(0.2f,dist((float)(-Math.PI+0.1),(float)(Math.PI-0.1)),1e-4);
	}

	@Test
	public void distHalf_F64() {
		assertEquals(0,UtilAngle.distHalf(0,0),1e-8);
		assertEquals(0,UtilAngle.distHalf(Math.PI/2,Math.PI/2),1e-8);
		assertEquals(0,UtilAngle.distHalf(-Math.PI/2,-Math.PI/2),1e-8);
		assertEquals(0,UtilAngle.distHalf(Math.PI/2,-Math.PI/2),1e-8);
		assertEquals(0,UtilAngle.distHalf(-Math.PI/2,Math.PI/2),1e-8);

		assertEquals(0.2,UtilAngle.distHalf(0.1,-0.1),1e-8);
		assertEquals(Math.PI/2.0,UtilAngle.distHalf(0,Math.PI/2.0),1e-8);
		assertEquals(Math.PI/2.0-0.1,UtilAngle.distHalf(-0.1,Math.PI/2.0),1e-8);
	}

	@Test
	public void domain2PI() {
		double angles[] = new double[]{-0.1,-Math.PI,-0.4,0.1,0.4,2.5,Math.PI};

		for( double angle : angles ) {
			assertEquals(Math.cos(angle), Math.cos(UtilAngle.domain2PI(angle)), 1e-8);
			assertEquals(Math.sin(angle), Math.sin(UtilAngle.domain2PI(angle)), 1e-8);
		}
	}

	@Test
	public void wrapZeroToOne_F64() {
		assertEquals(0.00, UtilAngle.wrapZeroToOne(2.0), GrlConstants.TEST_F64);
		assertEquals(0.00, UtilAngle.wrapZeroToOne(1.0), GrlConstants.TEST_F64);
		assertEquals(0.00, UtilAngle.wrapZeroToOne(0.0), GrlConstants.TEST_F64);
		assertEquals(0.50, UtilAngle.wrapZeroToOne(1.5), GrlConstants.TEST_F64);
		assertEquals(0.00, UtilAngle.wrapZeroToOne(-1.0), GrlConstants.TEST_F64);
		assertEquals(0.00, UtilAngle.wrapZeroToOne(-6.0), GrlConstants.TEST_F64);
		assertEquals(0.25, UtilAngle.wrapZeroToOne(0.25), GrlConstants.TEST_F64);
		assertEquals(0.50, UtilAngle.wrapZeroToOne(0.50), GrlConstants.TEST_F64);
		assertEquals(0.75, UtilAngle.wrapZeroToOne(-0.25), GrlConstants.TEST_F64);
		assertEquals(0.75, UtilAngle.wrapZeroToOne(-5.25), GrlConstants.TEST_F64);
		assertEquals(0.50, UtilAngle.wrapZeroToOne(-0.50), GrlConstants.TEST_F64);
	}

	@Test
	public void wrapZeroToOne_F32() {
		assertEquals(0.00f, UtilAngle.wrapZeroToOne(2.0f), GrlConstants.TEST_F32);
		assertEquals(0.00f, UtilAngle.wrapZeroToOne(1.0f), GrlConstants.TEST_F32);
		assertEquals(0.00f, UtilAngle.wrapZeroToOne(0.0f), GrlConstants.TEST_F32);
		assertEquals(0.50f, UtilAngle.wrapZeroToOne(1.5f), GrlConstants.TEST_F32);
		assertEquals(0.00f, UtilAngle.wrapZeroToOne(-1.0f), GrlConstants.TEST_F32);
		assertEquals(0.00f, UtilAngle.wrapZeroToOne(-6.0f), GrlConstants.TEST_F32);
		assertEquals(0.25f, UtilAngle.wrapZeroToOne(0.25f), GrlConstants.TEST_F32);
		assertEquals(0.50f, UtilAngle.wrapZeroToOne(0.50f), GrlConstants.TEST_F32);
		assertEquals(0.75f, UtilAngle.wrapZeroToOne(-0.25f), GrlConstants.TEST_F32);
		assertEquals(0.75f, UtilAngle.wrapZeroToOne(-5.25f), GrlConstants.TEST_F32);
		assertEquals(0.50f, UtilAngle.wrapZeroToOne(-0.50f), GrlConstants.TEST_F32);
	}

	@Test
	public void reflectZeroToOne_F64() {
		assertEquals(0.00, UtilAngle.reflectZeroToOne(2.0), GrlConstants.TEST_F64);
		assertEquals(1.00, UtilAngle.reflectZeroToOne(1.0), GrlConstants.TEST_F64);
		assertEquals(0.00, UtilAngle.reflectZeroToOne(0.0), GrlConstants.TEST_F64);
		assertEquals(0.50, UtilAngle.reflectZeroToOne(1.5), GrlConstants.TEST_F64);
		assertEquals(0.25, UtilAngle.reflectZeroToOne(1.75), GrlConstants.TEST_F64);
		assertEquals(0.25, UtilAngle.reflectZeroToOne(-0.25), GrlConstants.TEST_F64);
		assertEquals(0.25, UtilAngle.reflectZeroToOne(0.25), GrlConstants.TEST_F64);
		assertEquals(0.50, UtilAngle.reflectZeroToOne(0.50), GrlConstants.TEST_F64);
		assertEquals(0.50, UtilAngle.reflectZeroToOne(-0.50), GrlConstants.TEST_F64);
		assertEquals(1.00, UtilAngle.reflectZeroToOne(-1.0), GrlConstants.TEST_F64);
		assertEquals(0.00, UtilAngle.reflectZeroToOne(-6.0), GrlConstants.TEST_F64);
		assertEquals(0.75, UtilAngle.reflectZeroToOne(-5.25), GrlConstants.TEST_F64);
	}

	@Test
	public void reflectZeroToOne_F32() {
		assertEquals(0.00f, UtilAngle.reflectZeroToOne(2.0f), GrlConstants.TEST_F32);
		assertEquals(1.00f, UtilAngle.reflectZeroToOne(1.0f), GrlConstants.TEST_F32);
		assertEquals(0.00f, UtilAngle.reflectZeroToOne(0.0f), GrlConstants.TEST_F32);
		assertEquals(0.50f, UtilAngle.reflectZeroToOne(1.5f), GrlConstants.TEST_F32);
		assertEquals(0.25f, UtilAngle.reflectZeroToOne(1.75f), GrlConstants.TEST_F32);
		assertEquals(0.25f, UtilAngle.reflectZeroToOne(-0.25f), GrlConstants.TEST_F32);
		assertEquals(0.25f, UtilAngle.reflectZeroToOne(0.25f), GrlConstants.TEST_F32);
		assertEquals(0.50f, UtilAngle.reflectZeroToOne(0.50f), GrlConstants.TEST_F32);
		assertEquals(0.50f, UtilAngle.reflectZeroToOne(-0.50f), GrlConstants.TEST_F32);
		assertEquals(1.00f, UtilAngle.reflectZeroToOne(-1.0f), GrlConstants.TEST_F32);
		assertEquals(0.00f, UtilAngle.reflectZeroToOne(-6.0f), GrlConstants.TEST_F32);
		assertEquals(0.75f, UtilAngle.reflectZeroToOne(-5.25f), GrlConstants.TEST_F32);
	}
}
