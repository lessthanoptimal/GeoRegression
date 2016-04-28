/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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
import georegression.struct.so.Quaternion_F32;
import georegression.struct.so.Rodrigues_F32;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestAverageQuaternion_F32 {

	Random rand = new Random(234);

	/**
	 * Find the average of one quaternion.  Which should be the same as the input quaternion.
	 */
	@Test
	public void one() {
		Quaternion_F32 q = ConvertRotation3D_F32.eulerToQuaternion(EulerType.XYZ,0.1f,-0.5f,1.5f,null);

		List<Quaternion_F32> list = new ArrayList<Quaternion_F32>();
		list.add(q);

		AverageQuaternion_F32 alg = new AverageQuaternion_F32();
		Quaternion_F32 found = new Quaternion_F32();

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void two_same() {
		Quaternion_F32 q = ConvertRotation3D_F32.eulerToQuaternion(EulerType.XYZ,0.1f,-0.5f,1.5f,null);

		List<Quaternion_F32> list = new ArrayList<Quaternion_F32>();
		list.add(q);
		list.add(q);

		AverageQuaternion_F32 alg = new AverageQuaternion_F32();
		Quaternion_F32 found = new Quaternion_F32();

		assertTrue( alg.process(list,found) );

		checkEquals(q,found, GrlConstants.FLOAT_TEST_TOL);
	}

	/**
	 * Generate a bunch of quaternions, but noise them up on one axis and see if the result is close to the expected.
	 */
	@Test
	public void noiseOnOneAxis() {

		float rotX = 0.1f;
		float rotY = -0.5f;
		float rotZ = 1.5f;

		List<Quaternion_F32> list = new ArrayList<Quaternion_F32>();
		for (int i = 0; i < 40; i++) {
			float noise = (float)rand.nextGaussian()*0.03f;
			list.add( ConvertRotation3D_F32.eulerToQuaternion(EulerType.XYZ,rotX,rotY+noise,rotZ,null));
		}
		Quaternion_F32 expected = ConvertRotation3D_F32.eulerToQuaternion(EulerType.XYZ,rotX,rotY,rotZ,null);

		AverageQuaternion_F32 alg = new AverageQuaternion_F32();
		Quaternion_F32 found = new Quaternion_F32();

		assertTrue( alg.process(list,found) );

		checkEquals(expected, found, (float)Math.pow(GrlConstants.FLOAT_TEST_TOL,0.3f));
	}

	/**
	 * Sees if two quaternions are equal up to a sign ambiguity
	 */
	public static void checkEquals( Quaternion_F32 expected , Quaternion_F32 found , float errorTol ) {

		DenseMatrix64F E = ConvertRotation3D_F32.quaternionToMatrix(expected,null);
		DenseMatrix64F F = ConvertRotation3D_F32.quaternionToMatrix(found,null);

		DenseMatrix64F diff = new DenseMatrix64F(3,3);
		CommonOps.multTransA(E,F,diff);

		Rodrigues_F32 error = ConvertRotation3D_F32.matrixToRodrigues(diff,null);

		assertTrue( (float)Math.abs(error.theta) <= errorTol );
	}
}
