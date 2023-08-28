/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.ConvertRotation3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DMatrix3x3;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.ops.DConvertMatrixStruct;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static georegression.geometry.ConvertRotation3D_F64.eulerToMatrix;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAverageRotationMatrix_F64 {

	Random rand = new Random(234);

	/**
	 * Find the average of one quaternion. Which should be the same as the input quaternion.
	 */
	@Test void one_M() {
		DMatrixRMaj q = eulerToMatrix(EulerType.XYZ, 0.1, -0.5, 1.5, null);

		var list = new ArrayList<DMatrixRMaj>();
		list.add(q);

		var alg = new AverageRotationMatrix_F64();
		var found = new DMatrixRMaj(3, 3);

		assertTrue(alg.process(list, found));

		checkEquals(q, found, GrlConstants.TEST_F64);
	}

	@Test void one_F() {
		var q = new DMatrix3x3();
		DConvertMatrixStruct.convert(eulerToMatrix(EulerType.XYZ, 0.1, -0.5, 1.5, null), q);

		var list = new ArrayList<DMatrix3x3>();
		list.add(q);

		var alg = new AverageRotationMatrix_F64();
		var found = new DMatrix3x3();

		assertTrue(alg.process(list, found));

		checkEquals(q, found, GrlConstants.TEST_F64);
	}

	@Test void two_same_M() {
		DMatrixRMaj q = eulerToMatrix(EulerType.XYZ, 0.1, -0.5, 1.5, null);

		var list = new ArrayList<DMatrixRMaj>();
		list.add(q);
		list.add(q);

		var alg = new AverageRotationMatrix_F64();
		var found = new DMatrixRMaj(3, 3);

		assertTrue(alg.process(list, found));

		checkEquals(q, found, GrlConstants.TEST_F64);
	}

	@Test void two_same_F() {
		var q = new DMatrix3x3();
		DConvertMatrixStruct.convert(eulerToMatrix(EulerType.XYZ, 0.1, -0.5, 1.5, null), q);

		var list = new ArrayList<DMatrix3x3>();
		list.add(q);
		list.add(q);

		var alg = new AverageRotationMatrix_F64();
		var found = new DMatrix3x3();

		assertTrue(alg.process(list, found));

		checkEquals(q, found, GrlConstants.TEST_F64);
	}

	@Test void two_same_Op() {
		DMatrixRMaj q = eulerToMatrix(EulerType.XYZ, 0.1, -0.5, 1.5, null);

		var list = new ArrayList<DMatrixRMaj>();
		list.add(q);
		list.add(q);

		var alg = new AverageRotationMatrix_F64();
		var found = new DMatrixRMaj(3, 3);

		assertTrue(alg.processOp(list, found, ( src, dst ) -> dst.setTo(src)));

		checkEquals(q, found, GrlConstants.TEST_F64);
	}

	/**
	 * Generate a bunch of quaternions, but noise them up on one axis and see if the result is close to the expected.
	 */
	@Test void noiseOnOneAxis_M() {
		double rotX = 0.1;
		double rotY = -0.5;
		double rotZ = 1.5;

		var list = new ArrayList<DMatrixRMaj>();
		for (int i = 0; i < 40; i++) {
			double noise = rand.nextGaussian()*0.03;
			list.add(eulerToMatrix(EulerType.XYZ, rotX, rotY + noise, rotZ, null));
		}
		DMatrixRMaj expected = eulerToMatrix(EulerType.XYZ, 0.1, -0.5, 1.5, null);

		var alg = new AverageRotationMatrix_F64();
		var found = new DMatrixRMaj(3, 3);

		assertTrue(alg.process(list, found));

		checkEquals(expected, found, Math.pow(GrlConstants.TEST_F64, 0.3));
	}

	@Test void noiseOnOneAxis_F() {
		double rotX = 0.1;
		double rotY = -0.5;
		double rotZ = 1.5;

		var list = new ArrayList<DMatrix3x3>();
		for (int i = 0; i < 40; i++) {
			double noise = rand.nextGaussian()*0.03;
			var q = new DMatrix3x3();
			DConvertMatrixStruct.convert(eulerToMatrix(EulerType.XYZ, rotX, rotY + noise, rotZ, null), q);
			list.add(q);
		}
		var expected = new DMatrix3x3();
		DConvertMatrixStruct.convert(eulerToMatrix(EulerType.XYZ, 0.1, -0.5, 1.5, null), expected);

		var alg = new AverageRotationMatrix_F64();
		var found = new DMatrix3x3();

		assertTrue(alg.process(list, found));

		checkEquals(expected, found, Math.pow(GrlConstants.TEST_F64, 0.3));
	}

	public static void checkEquals( DMatrixRMaj expected, DMatrixRMaj found, double errorTol ) {
		var diff = new DMatrixRMaj(3, 3);
		CommonOps_DDRM.multTransA(expected, found, diff);

		Rodrigues_F64 error = ConvertRotation3D_F64.matrixToRodrigues(diff, null);

		assertTrue(Math.abs(error.theta) <= errorTol);
	}

	public static void checkEquals( DMatrix3x3 expected, DMatrix3x3 found, double errorTol ) {
		var E = new DMatrixRMaj(3, 3);
		var F = new DMatrixRMaj(3, 3);

		DConvertMatrixStruct.convert(expected, E);
		DConvertMatrixStruct.convert(found, F);

		var diff = new DMatrixRMaj(3, 3);
		CommonOps_DDRM.multTransA(E, F, diff);

		Rodrigues_F64 error = ConvertRotation3D_F64.matrixToRodrigues(diff, null);

		assertTrue(Math.abs(error.theta) <= errorTol);
	}
}
