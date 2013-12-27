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

package georegression.fitting.homography;

import georegression.misc.GrlConstants;
import georegression.struct.homo.Homography2D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestModelManagerHomography2D_F32 {

	@Test
	public void createModelInstance() {
		ModelManagerHomography2D_F32 alg = new ModelManagerHomography2D_F32();

		assertTrue(alg.createModelInstance() != null);
	}

	@Test
	public void copyModel() {
		ModelManagerHomography2D_F32 alg = new ModelManagerHomography2D_F32();

		Homography2D_F32 model = new Homography2D_F32(1,2,3,4,5,6,7,8,9);
		Homography2D_F32 found = new Homography2D_F32();

		alg.copyModel(model,found);

		assertEquals(model.a11,found.a11, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.a12,found.a12, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.a13,found.a13, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.a21,found.a21, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.a22,found.a22, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.a23,found.a23, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.a31,found.a31, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.a32,found.a32, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.a33,found.a33, GrlConstants.FLOAT_TEST_TOL);

	}

}
