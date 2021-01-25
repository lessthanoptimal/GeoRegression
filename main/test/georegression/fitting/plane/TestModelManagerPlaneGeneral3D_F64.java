/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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
import georegression.struct.plane.PlaneGeneral3D_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Peter Abeles
 */
public class TestModelManagerPlaneGeneral3D_F64 {

	@Test
	void createModelInstance() {
		ModelManagerPlaneGeneral3D_F64 alg = new ModelManagerPlaneGeneral3D_F64();

		assertNotNull(alg.createModelInstance());
	}

	@Test
	void copyModel() {
		ModelManagerPlaneGeneral3D_F64 alg = new ModelManagerPlaneGeneral3D_F64();

		PlaneGeneral3D_F64 model = new PlaneGeneral3D_F64(1,2,3,4);
		PlaneGeneral3D_F64 found = new PlaneGeneral3D_F64();

		alg.copyModel(model,found);

		assertEquals(model.A,found.A, GrlConstants.TEST_F64);
		assertEquals(model.B,found.B, GrlConstants.TEST_F64);
		assertEquals(model.C,found.C, GrlConstants.TEST_F64);
		assertEquals(model.D,found.D, GrlConstants.TEST_F64);
	}

}
