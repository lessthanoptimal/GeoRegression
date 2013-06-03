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

package georegression.struct;

import georegression.misc.GrlConstants;
import georegression.struct.point.*;
import georegression.struct.se.Se3_F32;
import georegression.struct.se.Se3_F64;
import org.ejml.ops.MatrixFeatures;
import org.ejml.ops.RandomMatrices;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestConvertStructureDataType {

	Random rand = new Random(234);

	@Test
	public void convert_Se_64_32() {
		Se3_F64 src = new Se3_F64();
		RandomMatrices.setRandom(src.getR(),rand);
		src.getT().set(rand.nextDouble(),rand.nextDouble(),rand.nextDouble());

		Se3_F32 dst = ConvertStructureDataType.convert(src,null);

		assertTrue(MatrixFeatures.isIdentical(src.getR(),dst.getR(), GrlConstants.FLOAT_TEST_TOL));
		assertEquals(src.T.x,dst.T.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.T.y,dst.T.y,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.T.z,dst.T.z,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_Point3D_64_32() {
		Point3D_F64 src = new Point3D_F64(rand.nextDouble(),rand.nextDouble(),rand.nextDouble());

		Point3D_F32 dst = ConvertStructureDataType.convert(src,null);

		assertEquals(src.x,dst.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.y,dst.y,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.z,dst.z,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_Point2D_64_32() {
		Point2D_F64 src = new Point2D_F64(rand.nextDouble(),rand.nextDouble());

		Point2D_F32 dst = ConvertStructureDataType.convert(src,null);

		assertEquals(src.x,dst.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.y,dst.y,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_Vector3D_64_32() {
		Vector3D_F64 src = new Vector3D_F64(rand.nextDouble(),rand.nextDouble(),rand.nextDouble());

		Vector3D_F32 dst = ConvertStructureDataType.convert(src,null);

		assertEquals(src.x,dst.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.y,dst.y,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.z,dst.z,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_Vector2D_64_32() {
		Vector2D_F64 src = new Vector2D_F64(rand.nextDouble(),rand.nextDouble());

		Vector2D_F32 dst = ConvertStructureDataType.convert(src,null);

		assertEquals(src.x,dst.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.y,dst.y,GrlConstants.FLOAT_TEST_TOL);
	}

}
