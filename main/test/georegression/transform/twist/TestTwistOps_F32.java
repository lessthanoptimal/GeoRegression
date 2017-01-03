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

package georegression.transform.twist;

import georegression.geometry.ConvertRotation3D_F32;
import georegression.geometry.GeometryMath_F32;
import georegression.misc.GrlConstants;
import georegression.struct.se.Se3_F32;
import georegression.struct.so.Rodrigues_F32;
import org.ejml.data.DenseMatrix32F;
import org.ejml.ops.MatrixFeatures_D32;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestTwistOps_F32 {

	@Test
	public void homogenous_se3() {
		Se3_F32 original = new Se3_F32();
		original.T.set(1,2,3);

		DenseMatrix32F H = TwistOps_F32.homogenous(original,null);

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				assertEquals(original.R.get(row,col), H.get(row,col), GrlConstants.TEST_F32);
			}
		}

		for (int i = 0; i < 3; i++) {
			assertEquals(original.T.getIndex(i), H.get(i,3), GrlConstants.TEST_F32);
		}
		for (int i = 0; i < 3; i++) {
			assertEquals(0, H.get(3,i), GrlConstants.TEST_F32);
		}
		assertEquals(1, H.get(3,3), GrlConstants.TEST_F32);
	}

	@Test
	public void homogenous_twist() {
		TwistCoordinate_F32 twist = new TwistCoordinate_F32();
		twist.w.set(-1,1,2);
		twist.w.normalize();
		twist.v.set(2,0.1f,-0.7f);

		DenseMatrix32F H = TwistOps_F32.homogenous(twist,null);

		DenseMatrix32F crossW = GeometryMath_F32.crossMatrix(twist.w,null);
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				assertEquals(crossW.get(row,col), H.get(row,col), GrlConstants.TEST_F32);
			}
		}

		for (int i = 0; i < 3; i++) {
			assertEquals(twist.v.getIndex(i), H.get(i,3), GrlConstants.TEST_F32);
		}
		for (int i = 0; i < 4; i++) {
			assertEquals(0, H.get(3,i), GrlConstants.TEST_F32);
		}
	}

	@Test
	public void exponential_twist_pure_r() {
		Rodrigues_F32 rod = new Rodrigues_F32(0.2f,0.1f,0.3f,-0.24f);
		rod.unitAxisRotation.normalize();

		Se3_F32 expected = new Se3_F32();
		ConvertRotation3D_F32.rodriguesToMatrix(rod,expected.R);

		TwistCoordinate_F32 twist = new TwistCoordinate_F32();
		twist.w.set(rod.unitAxisRotation);

		Se3_F32 found = TwistOps_F32.exponential(twist,rod.theta,null);

		assertTrue(MatrixFeatures_D32.isIdentical(expected.R,found.R, GrlConstants.TEST_F32));
		assertTrue(found.T.norm()<= GrlConstants.TEST_F32);
	}

	@Test
	public void exponential_twist_pure_t() {

		Se3_F32 expected = new Se3_F32();
		expected.T.set(1,0.1f,0);

		TwistCoordinate_F32 twist = new TwistCoordinate_F32();
		twist.v.set(expected.T);
		GeometryMath_F32.divide(twist.v,0.45f);

		Se3_F32 found = TwistOps_F32.exponential(twist,0.45f,null);

		assertTrue(MatrixFeatures_D32.isIdentical(expected.R,found.R, GrlConstants.TEST_F32));
		assertTrue(found.T.isIdentical(expected.T, GrlConstants.TEST_F32));
	}

	/**
	 * If the w has a norm of 1 then there should be a unique twist I believe
	 */
	@Test
	public void exponential_to_twist_verse_norm1() {

		TwistCoordinate_F32 twist = new TwistCoordinate_F32();
		twist.w.set(-1,1,2);
		twist.w.normalize();
		twist.v.set(2,0.1f,-0.7f);

		float theta = 0.7f;
		Se3_F32 se3 = TwistOps_F32.exponential(twist,theta,null);

		TwistCoordinate_F32 foundTwist = TwistOps_F32.twist(se3, null);

		// the generated twist needs to be adjusted so that it has the same theta
		foundTwist.w.divide(theta);
		foundTwist.v.divide(theta);

		assertTrue(foundTwist.v.isIdentical(twist.v, GrlConstants.TEST_F32) );
		assertTrue(foundTwist.w.isIdentical(twist.w, GrlConstants.TEST_F32) );
	}

	/**
	 * The norm of w is not one, but the two twists should describe the same Se3
	 */
	@Test
	public void exponential_to_twist_verse_NotNorm1() {

		TwistCoordinate_F32 twist1 = new TwistCoordinate_F32();
		twist1.w.set(-1,1,2);
		twist1.w.scale(GrlConstants.TEST_SQ_F32);
		twist1.v.set(2,0.1f,-0.7f);

		float theta1 = 0.7f;
		Se3_F32 motion1 = TwistOps_F32.exponential(twist1,theta1,null);

		TwistCoordinate_F32 twist2 = TwistOps_F32.twist(motion1, null);

		Se3_F32 motion2 = TwistOps_F32.exponential(twist2,1.0f,null);

		DenseMatrix32F diffR = new SimpleMatrix(motion1.R).transpose().mult(new SimpleMatrix(motion2.R)).getMatrix();

		assertTrue(MatrixFeatures_D32.isIdentity(diffR, GrlConstants.TEST_F32) );
		assertTrue(motion1.T.isIdentical(motion1.T, GrlConstants.TEST_F32) );

	}
}
