/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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
import georegression.struct.so.Quaternion_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestAverageQuaternion_F64 {

	Random rand = new Random(234);

	/**
	 * Find the average of one quaternion. Which should be the same as the input quaternion.
	 */
	@Test
	void one() {
		Quaternion_F64 q = ConvertRotation3D_F64.eulerToQuaternion(EulerType.XYZ,0.1,-0.5,1.5,null);

		List<Quaternion_F64> list = new ArrayList<>();
		list.add(q);

		AverageQuaternion_F64 alg = new AverageQuaternion_F64();
		Quaternion_F64 found = new Quaternion_F64();

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F64);
	}

	@Test
	void two_same() {
		Quaternion_F64 q = ConvertRotation3D_F64.eulerToQuaternion(EulerType.XYZ,0.1,-0.5,1.5,null);

		List<Quaternion_F64> list = new ArrayList<>();
		list.add(q);
		list.add(q);

		AverageQuaternion_F64 alg = new AverageQuaternion_F64();
		Quaternion_F64 found = new Quaternion_F64();

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F64);
	}

	/**
	 * Generate a bunch of quaternions, but noise them up on one axis and see if the result is close to the expected.
	 */
	@Test
	void noiseOnOneAxis() {

		double rotX = 0.1;
		double rotY = -0.5;
		double rotZ = 1.5;

		List<Quaternion_F64> list = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			double noise = rand.nextGaussian()*0.03;
			list.add( ConvertRotation3D_F64.eulerToQuaternion(EulerType.XYZ,rotX,rotY+noise,rotZ,null));
		}
		Quaternion_F64 expected = ConvertRotation3D_F64.eulerToQuaternion(EulerType.XYZ,rotX,rotY,rotZ,null);

		AverageQuaternion_F64 alg = new AverageQuaternion_F64();
		Quaternion_F64 found = new Quaternion_F64();

		assertTrue( alg.process(list,found) );

		checkEquals(expected, found, Math.pow(GrlConstants.TEST_F64,0.3));
	}

	/**
	 * Sees if two quaternions are equal up to a sign ambiguity
	 */
	public static void checkEquals( Quaternion_F64 expected , Quaternion_F64 found , double errorTol ) {

		DMatrixRMaj E = ConvertRotation3D_F64.quaternionToMatrix(expected,null);
		DMatrixRMaj F = ConvertRotation3D_F64.quaternionToMatrix(found,null);

		DMatrixRMaj diff = new DMatrixRMaj(3,3);
		CommonOps_DDRM.multTransA(E,F,diff);

		Rodrigues_F64 error = ConvertRotation3D_F64.matrixToRodrigues(diff,null);

		assertTrue( Math.abs(error.theta) <= 10*errorTol );
	}
}
