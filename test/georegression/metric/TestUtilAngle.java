/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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

package georegression.metric;

import org.junit.Test;

import static georegression.metric.UtilAngle.*;
import static java.lang.Math.PI;
import static org.junit.Assert.*;

public class TestUtilAngle {

	@Test
	public void atanSafe() {
		assertEquals(Math.PI/2.0,UtilAngle.atanSafe(0.0,0.0),1e-8);
		assertEquals(Math.PI/2.0,UtilAngle.atanSafe(1.0,0.0),1e-8);
		assertEquals(Math.PI/2.0,UtilAngle.atanSafe(-1.0,0.0),1e-8);
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
	public void testDistanceCCW() {
		assertEquals( PI * 1.5, distanceCCW(-0.75 * PI, 0.75 * PI), 1e-5 );
		assertEquals( PI * 0.5, distanceCCW( 0.75 * PI, -0.75 * PI), 1e-5 );
	}

	@Test
	public void testDistanceCW() {
		assertEquals( PI * 0.5, distanceCW(-0.75 * PI, 0.75 * PI), 1e-5 );
		assertEquals( PI * 1.5, distanceCW(0.75 * PI, -0.75 * PI), 1e-5 );
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
}
