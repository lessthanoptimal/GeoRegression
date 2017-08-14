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
import org.ejml.data.FMatrix3x3;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.CommonOps_FDRM;
import org.ejml.ops.ConvertFMatrixStruct;
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
		FMatrixRMaj q = eulerToMatrix(EulerType.XYZ,0.1f,-0.5f,1.5f,null);

		List<FMatrixRMaj> list = new ArrayList<>();
		list.add(q);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		FMatrixRMaj found = new FMatrixRMaj(3,3);

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F32);
	}

	@Test
	public void one_F() {
		FMatrix3x3 q = new FMatrix3x3();
		ConvertFMatrixStruct.convert(eulerToMatrix(EulerType.XYZ,0.1f,-0.5f,1.5f,null),q);

		List<FMatrix3x3> list = new ArrayList<>();
		list.add(q);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		FMatrix3x3 found = new FMatrix3x3();

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F32);
	}

	@Test
	public void two_same_M() {
		FMatrixRMaj q = eulerToMatrix(EulerType.XYZ,0.1f,-0.5f,1.5f,null);

		List<FMatrixRMaj> list = new ArrayList<>();
		list.add(q);
		list.add(q);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		FMatrixRMaj found = new FMatrixRMaj(3,3);

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F32);
	}

	@Test
	public void two_same_F() {
		FMatrix3x3 q = new FMatrix3x3();
		ConvertFMatrixStruct.convert(eulerToMatrix(EulerType.XYZ,0.1f,-0.5f,1.5f,null),q);

		List<FMatrix3x3> list = new ArrayList<>();
		list.add(q);
		list.add(q);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		FMatrix3x3 found = new FMatrix3x3();

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

		List<FMatrixRMaj> list = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			float noise = (float)rand.nextGaussian() * 0.03f;
			list.add(eulerToMatrix(EulerType.XYZ, rotX, rotY + noise, rotZ, null));
		}
		FMatrixRMaj expected = eulerToMatrix(EulerType.XYZ, 0.1f, -0.5f, 1.5f, null);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		FMatrixRMaj found = new FMatrixRMaj(3, 3);

		assertTrue(alg.process(list, found));

		checkEquals(expected, found, (float)Math.pow(GrlConstants.TEST_F32,0.3f));
	}

	@Test
	public void noiseOnOneAxis_F() {
		float rotX = 0.1f;
		float rotY = -0.5f;
		float rotZ = 1.5f;

		List<FMatrix3x3> list = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			float noise = (float)rand.nextGaussian() * 0.03f;
			FMatrix3x3 q = new FMatrix3x3();
			ConvertFMatrixStruct.convert(eulerToMatrix(EulerType.XYZ, rotX, rotY + noise, rotZ,null),q);
			list.add(q);
		}
		FMatrix3x3 expected = new FMatrix3x3();
		ConvertFMatrixStruct.convert(eulerToMatrix(EulerType.XYZ,0.1f,-0.5f,1.5f,null),expected);

		AverageRotationMatrix_F32 alg = new AverageRotationMatrix_F32();
		FMatrix3x3 found = new FMatrix3x3();

		assertTrue(alg.process(list, found));

		checkEquals(expected, found, (float)Math.pow(GrlConstants.TEST_F32,0.3f));
	}

	public static void checkEquals( FMatrixRMaj expected , FMatrixRMaj found , float errorTol ) {
		FMatrixRMaj diff = new FMatrixRMaj(3,3);
		CommonOps_FDRM.multTransA(expected,found,diff);

		Rodrigues_F32 error = ConvertRotation3D_F32.matrixToRodrigues(diff,null);

		assertTrue( (float)Math.abs(error.theta) <= errorTol );
	}

	public static void checkEquals( FMatrix3x3 expected , FMatrix3x3 found , float errorTol ) {
		FMatrixRMaj E = new FMatrixRMaj(3,3);
		FMatrixRMaj F = new FMatrixRMaj(3,3);

		ConvertFMatrixStruct.convert(expected,E);
		ConvertFMatrixStruct.convert(found,F);

		FMatrixRMaj diff = new FMatrixRMaj(3,3);
		CommonOps_FDRM.multTransA(E,F,diff);

		Rodrigues_F32 error = ConvertRotation3D_F32.matrixToRodrigues(diff,null);

		assertTrue( (float)Math.abs(error.theta) <= errorTol );
	}

}
