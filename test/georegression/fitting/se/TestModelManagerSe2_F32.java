/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.fitting.se;

import georegression.misc.GrlConstants;
import georegression.struct.se.Se2_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestModelManagerSe2_F32 {

	@Test
	public void createModelInstance() {
		ModelManagerSe2_F32 alg = new ModelManagerSe2_F32();

		assertTrue(alg.createModelInstance() != null);
	}

	@Test
	public void copyModel() {
		ModelManagerSe2_F32 alg = new ModelManagerSe2_F32();

		Se2_F32 model = new Se2_F32(1,2,0.5f);
		Se2_F32 found = new Se2_F32();

		alg.copyModel(model,found);

		assertEquals(model.T.x,found.T.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.T.y,found.T.y,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.c,found.c,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(model.s,found.s,GrlConstants.FLOAT_TEST_TOL);

	}

}
