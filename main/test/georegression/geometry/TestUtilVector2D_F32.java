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

package georegression.geometry;

import georegression.misc.GrlConstants;
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

		assertEquals(Math.PI/2.0f,UtilVector2D_F32.acute(a,new Vector2D_F32(0, 1)), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Math.PI, UtilVector2D_F32.acute(a,new Vector2D_F32(-1, 0)), GrlConstants.FLOAT_TEST_TOL);
	}
}