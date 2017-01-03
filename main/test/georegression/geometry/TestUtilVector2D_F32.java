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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Vector2D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilVector2D_F32 {
	@Test
	public void acute() {

		Vector2D_F32 a = new Vector2D_F32(1,0);

		assertEquals(Math.PI/2.0f,UtilVector2D_F32.acute(a,new Vector2D_F32(0, 1)), GrlConstants.TEST_F32);
		assertEquals(Math.PI, UtilVector2D_F32.acute(a,new Vector2D_F32(-1, 0)), GrlConstants.TEST_F32);
	}

	@Test
	public void acute_pts() {

		assertEquals(Math.PI/2.0f, UtilVector2D_F32.acute(1,0, 0, 1), GrlConstants.TEST_F32);
		assertEquals(Math.PI    , UtilVector2D_F32.acute(1,0,-1, 0), GrlConstants.TEST_F32);
	}

	@Test
	public void minus() {

		Point2D_F32 a = new Point2D_F32(4,9);
		Point2D_F32 b = new Point2D_F32(1.2f,3.5f);

		Vector2D_F32 out = UtilVector2D_F32.minus(a,b,null);

		assertEquals(a.x-b.x,out.x,GrlConstants.TEST_F32);
		assertEquals(a.y-b.y,out.y,GrlConstants.TEST_F32);
	}
}