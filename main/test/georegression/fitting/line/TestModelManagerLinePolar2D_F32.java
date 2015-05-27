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

package georegression.fitting.line;

import georegression.misc.GrlConstants;
import georegression.struct.line.LinePolar2D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestModelManagerLinePolar2D_F32 {

	@Test
	public void createModelInstance() {
		ModelManagerLinePolar2D_F32 alg = new ModelManagerLinePolar2D_F32();

		assertTrue( alg.createModelInstance() != null);
	}

	@Test
	public void copyModel() {
		ModelManagerLinePolar2D_F32 alg = new ModelManagerLinePolar2D_F32();

		LinePolar2D_F32 model = new LinePolar2D_F32(1,2);
		LinePolar2D_F32 found = new LinePolar2D_F32();

		alg.copyModel(model,found);

		assertEquals(model.angle,found.angle, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.distance,found.distance, GrlConstants.FLOAT_TEST_TOL);
	}

}
