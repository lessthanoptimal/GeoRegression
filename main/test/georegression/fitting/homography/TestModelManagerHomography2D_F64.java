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

package georegression.fitting.homography;

import georegression.misc.GrlConstants;
import georegression.struct.homography.Homography2D_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestModelManagerHomography2D_F64 {

	@Test
	void createModelInstance() {
		ModelManagerHomography2D_F64 alg = new ModelManagerHomography2D_F64();

		assertTrue(alg.createModelInstance() != null);
	}

	@Test
	void copyModel() {
		ModelManagerHomography2D_F64 alg = new ModelManagerHomography2D_F64();

		Homography2D_F64 model = new Homography2D_F64(1,2,3,4,5,6,7,8,9);
		Homography2D_F64 found = new Homography2D_F64();

		alg.copyModel(model,found);

		assertEquals(model.a11,found.a11, GrlConstants.TEST_F64);
		assertEquals(model.a12,found.a12, GrlConstants.TEST_F64);
		assertEquals(model.a13,found.a13, GrlConstants.TEST_F64);
		assertEquals(model.a21,found.a21, GrlConstants.TEST_F64);
		assertEquals(model.a22,found.a22, GrlConstants.TEST_F64);
		assertEquals(model.a23,found.a23, GrlConstants.TEST_F64);
		assertEquals(model.a31,found.a31, GrlConstants.TEST_F64);
		assertEquals(model.a32,found.a32, GrlConstants.TEST_F64);
		assertEquals(model.a33,found.a33, GrlConstants.TEST_F64);

	}

}
