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

package georegression.fitting.plane;

import georegression.misc.GrlConstants;
import georegression.struct.plane.PlaneGeneral3D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestModelManagerPlaneGeneral3D_F32 {

	@Test
	public void createModelInstance() {
		ModelManagerPlaneGeneral3D_F32 alg = new ModelManagerPlaneGeneral3D_F32();

		assertTrue( alg.createModelInstance() != null);
	}

	@Test
	public void copyModel() {
		ModelManagerPlaneGeneral3D_F32 alg = new ModelManagerPlaneGeneral3D_F32();

		PlaneGeneral3D_F32 model = new PlaneGeneral3D_F32(1,2,3,4);
		PlaneGeneral3D_F32 found = new PlaneGeneral3D_F32();

		alg.copyModel(model,found);

		assertEquals(model.A,found.A, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.B,found.B, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.C,found.C, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.D,found.D, GrlConstants.FLOAT_TEST_TOL);
	}

}
