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

package georegression.fitting.homography;

import georegression.misc.GrlConstants;
import georegression.struct.homo.Homography2D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestModelManagerHomography2D_F64 {

	@Test
	public void createModelInstance() {
		ModelManagerHomography2D_F64 alg = new ModelManagerHomography2D_F64();

		assertTrue(alg.createModelInstance() != null);
	}

	@Test
	public void copyModel() {
		ModelManagerHomography2D_F64 alg = new ModelManagerHomography2D_F64();

		Homography2D_F64 model = new Homography2D_F64(1,2,3,4,5,6,7,8,9);
		Homography2D_F64 found = new Homography2D_F64();

		alg.copyModel(model,found);

		assertEquals(model.a11,found.a11, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.a12,found.a12, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.a13,found.a13, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.a21,found.a21, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.a22,found.a22, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.a23,found.a23, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.a31,found.a31, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.a32,found.a32, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.a33,found.a33, GrlConstants.DOUBLE_TEST_TOL);

	}

}
