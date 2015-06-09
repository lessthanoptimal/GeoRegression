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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestVector2D_F32 {

	@Test
	public void minus() {
		Point2D_F32 a = new Point2D_F32(4,9);
		Point2D_F32 b = new Point2D_F32(1.2f,3.5f);

		Vector2D_F32 out = new Vector2D_F32();
		out.minus(a,b);

		assertEquals(a.x-b.x,out.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(a.y-b.y,out.y,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void dot() {
		Vector2D_F32 a = new Vector2D_F32( 1, 2 );
		Vector2D_F32 b = new Vector2D_F32( 3, 4 );

		float found = a.dot( b );
		assertEquals( 11, found, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void acute() {

		Vector2D_F32 a = new Vector2D_F32(1,0);

		assertEquals(Math.PI/2.0f,a.acute(new Vector2D_F32(0, 1)),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Math.PI,a.acute(new Vector2D_F32(-1, 0)),GrlConstants.FLOAT_TEST_TOL);
	}
}
