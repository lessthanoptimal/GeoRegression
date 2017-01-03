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

import georegression.geometry.ConvertRotation3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DenseMatrix64F;
import org.ejml.data.FixedMatrix3x3_64F;
import org.ejml.ops.CommonOps_D64;
import org.ejml.ops.ConvertMatrixStruct_F64;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static georegression.geometry.ConvertRotation3D_F64.eulerToMatrix;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestAverageRotationMatrix_F64 {

	Random rand = new Random(234);

	/**
	 * Find the average of one quaternion.  Which should be the same as the input quaternion.
	 */
	@Test
	public void one_M() {
		DenseMatrix64F q = eulerToMatrix(EulerType.XYZ,0.1,-0.5,1.5,null);

		List<DenseMatrix64F> list = new ArrayList<>();
		list.add(q);

		AverageRotationMatrix_F64 alg = new AverageRotationMatrix_F64();
		DenseMatrix64F found = new DenseMatrix64F(3,3);

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F64);
	}

	@Test
	public void one_F() {
		FixedMatrix3x3_64F q = new FixedMatrix3x3_64F();
		ConvertMatrixStruct_F64.convert(eulerToMatrix(EulerType.XYZ,0.1,-0.5,1.5,null),q);

		List<FixedMatrix3x3_64F> list = new ArrayList<>();
		list.add(q);

		AverageRotationMatrix_F64 alg = new AverageRotationMatrix_F64();
		FixedMatrix3x3_64F found = new FixedMatrix3x3_64F();

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F64);
	}

	@Test
	public void two_same_M() {
		DenseMatrix64F q = eulerToMatrix(EulerType.XYZ,0.1,-0.5,1.5,null);

		List<DenseMatrix64F> list = new ArrayList<>();
		list.add(q);
		list.add(q);

		AverageRotationMatrix_F64 alg = new AverageRotationMatrix_F64();
		DenseMatrix64F found = new DenseMatrix64F(3,3);

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F64);
	}

	@Test
	public void two_same_F() {
		FixedMatrix3x3_64F q = new FixedMatrix3x3_64F();
		ConvertMatrixStruct_F64.convert(eulerToMatrix(EulerType.XYZ,0.1,-0.5,1.5,null),q);

		List<FixedMatrix3x3_64F> list = new ArrayList<>();
		list.add(q);
		list.add(q);

		AverageRotationMatrix_F64 alg = new AverageRotationMatrix_F64();
		FixedMatrix3x3_64F found = new FixedMatrix3x3_64F();

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.TEST_F64);
	}

	/**
	 * Generate a bunch of quaternions, but noise them up on one axis and see if the result is close to the expected.
	 */
	@Test
	public void noiseOnOneAxis_M() {
		double rotX = 0.1;
		double rotY = -0.5;
		double rotZ = 1.5;

		List<DenseMatrix64F> list = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			double noise = rand.nextGaussian() * 0.03;
			list.add(eulerToMatrix(EulerType.XYZ, rotX, rotY + noise, rotZ, null));
		}
		DenseMatrix64F expected = eulerToMatrix(EulerType.XYZ, 0.1, -0.5, 1.5, null);

		AverageRotationMatrix_F64 alg = new AverageRotationMatrix_F64();
		DenseMatrix64F found = new DenseMatrix64F(3, 3);

		assertTrue(alg.process(list, found));

		checkEquals(expected, found, Math.pow(GrlConstants.TEST_F64,0.3));
	}

	@Test
	public void noiseOnOneAxis_F() {
		double rotX = 0.1;
		double rotY = -0.5;
		double rotZ = 1.5;

		List<FixedMatrix3x3_64F> list = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			double noise = rand.nextGaussian() * 0.03;
			FixedMatrix3x3_64F q = new FixedMatrix3x3_64F();
			ConvertMatrixStruct_F64.convert(eulerToMatrix(EulerType.XYZ, rotX, rotY + noise, rotZ,null),q);
			list.add(q);
		}
		FixedMatrix3x3_64F expected = new FixedMatrix3x3_64F();
		ConvertMatrixStruct_F64.convert(eulerToMatrix(EulerType.XYZ,0.1,-0.5,1.5,null),expected);

		AverageRotationMatrix_F64 alg = new AverageRotationMatrix_F64();
		FixedMatrix3x3_64F found = new FixedMatrix3x3_64F();

		assertTrue(alg.process(list, found));

		checkEquals(expected, found, Math.pow(GrlConstants.TEST_F64,0.3));
	}

	public static void checkEquals( DenseMatrix64F expected , DenseMatrix64F found , double errorTol ) {
		DenseMatrix64F diff = new DenseMatrix64F(3,3);
		CommonOps_D64.multTransA(expected,found,diff);

		Rodrigues_F64 error = ConvertRotation3D_F64.matrixToRodrigues(diff,null);

		assertTrue( Math.abs(error.theta) <= errorTol );
	}

	public static void checkEquals( FixedMatrix3x3_64F expected , FixedMatrix3x3_64F found , double errorTol ) {
		DenseMatrix64F E = new DenseMatrix64F(3,3);
		DenseMatrix64F F = new DenseMatrix64F(3,3);

		ConvertMatrixStruct_F64.convert(expected,E);
		ConvertMatrixStruct_F64.convert(found,F);

		DenseMatrix64F diff = new DenseMatrix64F(3,3);
		CommonOps_D64.multTransA(E,F,diff);

		Rodrigues_F64 error = ConvertRotation3D_F64.matrixToRodrigues(diff,null);

		assertTrue( Math.abs(error.theta) <= errorTol );
	}

}
