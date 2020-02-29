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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestVector2D_F64 {

	@Test
	void minus() {
		Point2D_F64 a = new Point2D_F64(4,9);
		Point2D_F64 b = new Point2D_F64(1.2,3.5);

		Vector2D_F64 out = new Vector2D_F64();
		out.minus(a,b);

		assertEquals(a.x-b.x,out.x,GrlConstants.TEST_F64);
		assertEquals(a.y-b.y,out.y,GrlConstants.TEST_F64);
	}

	@Test
	void dot() {
		Vector2D_F64 a = new Vector2D_F64( 1, 2 );
		Vector2D_F64 b = new Vector2D_F64( 3, 4 );

		double found = a.dot( b );
		assertEquals( 11, found, GrlConstants.TEST_F64);
	}

	@Test
	void acute() {

		Vector2D_F64 a = new Vector2D_F64(1,0);

		assertEquals(Math.PI/2.0,a.acute(new Vector2D_F64(0, 1)),GrlConstants.TEST_F64);
		assertEquals(Math.PI,a.acute(new Vector2D_F64(-1, 0)),GrlConstants.TEST_F64);
	}
}
