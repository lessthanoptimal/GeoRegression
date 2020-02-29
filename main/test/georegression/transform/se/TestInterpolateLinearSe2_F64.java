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

package georegression.transform.se;

import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.struct.se.Se2_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestInterpolateLinearSe2_F64 {
	@Test
	void interpolate_translation() {
		Se2_F64 a = new Se2_F64(1,2,0);
		Se2_F64 b = new Se2_F64(4,6,0);

		Se2_F64 found = new Se2_F64();
		InterpolateLinearSe2_F64.interpolate(a,b,0,found);
		assertEquals(1, found.T.x, GrlConstants.TEST_F64);
		assertEquals(2,found.T.y,GrlConstants.TEST_F64);
		assertEquals(0,found.getYaw(),GrlConstants.TEST_F64);

		InterpolateLinearSe2_F64.interpolate(a,b,1,found);
		assertEquals(4,found.T.x,GrlConstants.TEST_F64);
		assertEquals(6,found.T.y,GrlConstants.TEST_F64);
		assertEquals(0,found.getYaw(),GrlConstants.TEST_F64);

		InterpolateLinearSe2_F64.interpolate(a,b,0.25,found);
		assertEquals(1+0.25*3,found.T.x,GrlConstants.TEST_F64);
		assertEquals(2+0.25*4,found.T.y,GrlConstants.TEST_F64);
		assertEquals(0,found.getYaw(),GrlConstants.TEST_F64);
	}

	@Test
	void interpolate_angleEasy() {
		Se2_F64 a = new Se2_F64(0,0,1);
		Se2_F64 b = new Se2_F64(0,0,1.5);

		Se2_F64 found = new Se2_F64();
		InterpolateLinearSe2_F64.interpolate(a,b,0, found);
		assertEquals(1,found.getYaw(),GrlConstants.TEST_F64);

		InterpolateLinearSe2_F64.interpolate(a,b,1, found);
		assertEquals(1.5,found.getYaw(),GrlConstants.TEST_F64);

		InterpolateLinearSe2_F64.interpolate(a,b,0.25, found);
		assertEquals(1+0.25*0.5,found.getYaw(),GrlConstants.TEST_F64);
	}

	@Test
	void interpolate_angleHard0() {
		Se2_F64 a = new Se2_F64(0,0,0.2);
		Se2_F64 b = new Se2_F64(0,0,-0.1);

		Se2_F64 found = new Se2_F64();
		InterpolateLinearSe2_F64.interpolate(a,b,0,found);
		assertEquals(0.2,found.getYaw(),GrlConstants.TEST_F64);

		InterpolateLinearSe2_F64.interpolate(a,b,1.0,found);
		assertEquals(-0.1,found.getYaw(),GrlConstants.TEST_F64);

		InterpolateLinearSe2_F64.interpolate(a,b,0.25,found);
		assertEquals(0.2-0.25*0.3,found.getYaw(),GrlConstants.TEST_F64);
	}

	@Test
	void interpolate_angleHard1() {
		Se2_F64 a = new Se2_F64(0,0, Math.PI-0.1);
		Se2_F64 b = new Se2_F64(0,0,- Math.PI+0.2);

		Se2_F64 found = new Se2_F64();
		InterpolateLinearSe2_F64.interpolate(a,b,0.25,found);
		assertEquals(UtilAngle.bound( Math.PI - 0.1 + 0.25 * 0.3),found.getYaw(),GrlConstants.TEST_F64);
		InterpolateLinearSe2_F64.interpolate(a,b,0.5,found);
		assertEquals(UtilAngle.bound( Math.PI - 0.1 + 0.5 * 0.3), found.getYaw(), GrlConstants.TEST_F64);
	}
}