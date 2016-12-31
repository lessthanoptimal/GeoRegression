/*
 * Copyright (C) 2011-2016, Peter Abeles. All Rights Reserved.
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

		Se3_F32 dst = ConvertFloatType.convert(src,null);

		assertTrue(MatrixFeatures.isIdentical(src.getR(),dst.getR(), GrlConstants.FLOAT_TEST_TOL));
		assertEquals(src.T.x,dst.T.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.T.y,dst.T.y,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.T.z,dst.T.z,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_Point3D_64_32() {
		Point3D_F64 src = new Point3D_F64(rand.nextDouble(),rand.nextDouble(),rand.nextDouble());

		Point3D_F32 dst = ConvertFloatType.convert(src,null);

		assertEquals(src.x,dst.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.y,dst.y,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.z,dst.z,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_Point2D_64_32() {
		Point2D_F64 src = new Point2D_F64(rand.nextDouble(),rand.nextDouble());

		Point2D_F32 dst = ConvertFloatType.convert(src,null);

		assertEquals(src.x,dst.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.y,dst.y,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_Vector3D_64_32() {
		Vector3D_F64 src = new Vector3D_F64(rand.nextDouble(),rand.nextDouble(),rand.nextDouble());

		Vector3D_F32 dst = ConvertFloatType.convert(src,null);

		assertEquals(src.x,dst.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.y,dst.y,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.z,dst.z,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void convert_Vector2D_64_32() {
		Vector2D_F64 src = new Vector2D_F64(rand.nextDouble(),rand.nextDouble());

		Vector2D_F32 dst = ConvertFloatType.convert(src,null);

		assertEquals(src.x,dst.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(src.y,dst.y,GrlConstants.FLOAT_TEST_TOL);
	}

}
