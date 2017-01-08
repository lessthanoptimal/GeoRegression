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

package georegression.fitting.se;

import georegression.misc.GrlConstants;
import georegression.struct.se.Se3_F64;
import org.ejml.ops.MatrixFeatures_R64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestModelManagerSe3_F64 {

	@Test
	public void createModelInstance() {
		ModelManagerSe3_F64 alg = new ModelManagerSe3_F64();

		assertTrue(alg.createModelInstance() != null);
	}

	@Test
	public void copyModel() {
		ModelManagerSe3_F64 alg = new ModelManagerSe3_F64();

		Se3_F64 model = new Se3_F64();
		Se3_F64 found = new Se3_F64();

		model.T.set(1,2,3);
		model.getR().set(3,3,true,1,2,3,4,5,6,7,8,9);

		alg.copyModel(model,found);

		assertTrue(MatrixFeatures_R64.isEquals(model.getR(),found.getR()));
		assertEquals(model.T.x,found.T.x,GrlConstants.TEST_F64);
		assertEquals(model.T.y,found.T.y,GrlConstants.TEST_F64);
		assertEquals(model.T.z,found.T.z,GrlConstants.TEST_F64);
	}

}
