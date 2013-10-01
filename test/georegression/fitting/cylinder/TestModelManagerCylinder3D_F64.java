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

package georegression.fitting.cylinder;

import georegression.misc.GrlConstants;
import georegression.struct.shapes.Cylinder3D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestModelManagerCylinder3D_F64 {

	@Test
	public void createModelInstance() {
		ModelManagerCylinder3D_F64 alg = new ModelManagerCylinder3D_F64();

		assertTrue( alg.createModelInstance() != null);
	}

	@Test
	public void copyModel() {
		ModelManagerCylinder3D_F64 alg = new ModelManagerCylinder3D_F64();

		Cylinder3D_F64 model = new Cylinder3D_F64(1,2,3,4,5,6,7);
		Cylinder3D_F64 found = new Cylinder3D_F64();

		alg.copyModel(model,found);

		assertEquals(model.line.p.x, found.line.p.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.line.p.y,found.line.p.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.line.p.z,found.line.p.z, GrlConstants.DOUBLE_TEST_TOL);

		assertEquals(model.line.slope.x,found.line.slope.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.line.slope.y,found.line.slope.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(model.line.slope.z,found.line.slope.z, GrlConstants.DOUBLE_TEST_TOL);

		assertEquals(model.radius,found.radius, GrlConstants.DOUBLE_TEST_TOL);
	}

}
