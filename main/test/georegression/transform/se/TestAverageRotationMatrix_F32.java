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

package georegression.transform.se;

import georegression.geometry.ConvertRotation3D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.so.Rodrigues_F32;
import org.ejml.data.DenseMatrix32F;
import org.ejml.data.FixedMatrix3x3_32F;
import org.ejml.ops.CommonOps_D32;
import org.ejml.ops.ConvertMatrixStruct_F32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static georegression.geometry.ConvertRotation3D_F32.eulerToMatrix;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestAverageRotationMatrix_F32 {

	Random rand = new Random(234);

	/**
	 * Find the average of one quaternion.  Which should be the same as the input quaternion.
	 */
	@Test
	public void one_M() {
		DenseMatrix32F q = eulerToMatrix(EulerType.XYZ,0.1f,-0.5f,1.5f,null);

		List<DenseMatrix32F> list = new ArrayList<>();
		list.add(q);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		DenseMatrix32F found = new DenseMatrix32F(3,3);

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F32);
	}

	@Test
	public void one_F() {
		FixedMatrix3x3_32F q = new FixedMatrix3x3_32F();
		ConvertMatrixStruct_F32.convert(eulerToMatrix(EulerType.XYZ,0.1f,-0.5f,1.5f,null),q);

		List<FixedMatrix3x3_32F> list = new ArrayList<>();
		list.add(q);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		FixedMatrix3x3_32F found = new FixedMatrix3x3_32F();

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F32);
	}

	@Test
	public void two_same_M() {
		DenseMatrix32F q = eulerToMatrix(EulerType.XYZ,0.1f,-0.5f,1.5f,null);

		List<DenseMatrix32F> list = new ArrayList<>();
		list.add(q);
		list.add(q);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		DenseMatrix32F found = new DenseMatrix32F(3,3);

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F32);
	}

	@Test
	public void two_same_F() {
		FixedMatrix3x3_32F q = new FixedMatrix3x3_32F();
		ConvertMatrixStruct_F32.convert(eulerToMatrix(EulerType.XYZ,0.1f,-0.5f,1.5f,null),q);

		List<FixedMatrix3x3_32F> list = new ArrayList<>();
		list.add(q);
		list.add(q);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		FixedMatrix3x3_32F found = new FixedMatrix3x3_32F();

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F32);
	}

	/**
	 * Generate a bunch of quaternions, but noise them up on one axis and see if the result is close to the expected.
	 */
	@Test
	public void noiseOnOneAxis_M() {
		float rotX = 0.1f;
		float rotY = -0.5f;
		float rotZ = 1.5f;

		List<DenseMatrix32F> list = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			float noise = (float)rand.nextGaussian() * 0.03f;
			list.add(eulerToMatrix(EulerType.XYZ, rotX, rotY + noise, rotZ, null));
		}
		DenseMatrix32F expected = eulerToMatrix(EulerType.XYZ, 0.1f, -0.5f, 1.5f, null);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		DenseMatrix32F found = new DenseMatrix32F(3, 3);

		assertTrue(alg.process(list, found));

		checkEquals(expected, found, (float)Math.pow(GrlConstants.TEST_F32,0.3f));
	}

	@Test
	public void noiseOnOneAxis_F() {
		float rotX = 0.1f;
		float rotY = -0.5f;
		float rotZ = 1.5f;

		List<FixedMatrix3x3_32F> list = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			float noise = (float)rand.nextGaussian() * 0.03f;
			FixedMatrix3x3_32F q = new FixedMatrix3x3_32F();
			ConvertMatrixStruct_F32.convert(eulerToMatrix(EulerType.XYZ, rotX, rotY + noise, rotZ,null),q);
			list.add(q);
		}
		FixedMatrix3x3_32F expected = new FixedMatrix3x3_32F();
		ConvertMatrixStruct_F32.convert(eulerToMatrix(EulerType.XYZ,0.1f,-0.5f,1.5f,null),expected);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		FixedMatrix3x3_32F found = new FixedMatrix3x3_32F();

		assertTrue(alg.process(list, found));

		checkEquals(expected, found, (float)Math.pow(GrlConstants.TEST_F32,0.3f));
	}

	public static void checkEquals( DenseMatrix32F expected , DenseMatrix32F found , float errorTol ) {
		DenseMatrix32F diff = new DenseMatrix32F(3,3);
		CommonOps_D32.multTransA(expected,found,diff);

		Rodrigues_F32 error = ConvertRotation3D_F32.matrixToRodrigues(diff,null);

		assertTrue( (float)Math.abs(error.theta) <= errorTol );
	}

	public static void checkEquals( FixedMatrix3x3_32F expected , FixedMatrix3x3_32F found , float errorTol ) {
		DenseMatrix32F E = new DenseMatrix32F(3,3);
		DenseMatrix32F F = new DenseMatrix32F(3,3);

		ConvertMatrixStruct_F32.convert(expected,E);
		ConvertMatrixStruct_F32.convert(found,F);

		DenseMatrix32F diff = new DenseMatrix32F(3,3);
		CommonOps_D32.multTransA(E,F,diff);

		Rodrigues_F32 error = ConvertRotation3D_F32.matrixToRodrigues(diff,null);

		assertTrue( (float)Math.abs(error.theta) <= errorTol );
	}

}