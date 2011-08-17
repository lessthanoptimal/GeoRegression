/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package jgrl.metric;

import org.junit.Test;

import static java.lang.Math.PI;
import static jgrl.metric.UtilAngle.bound;
import static jgrl.metric.UtilAngle.minus;
import static org.junit.Assert.*;

public class TestUtilAngle {

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
	public void testDist() {
		fail("implement");
	}
}
