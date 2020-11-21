/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.GeometryMath_F64;
import georegression.misc.GrlConstants;
import georegression.struct.se.Se3_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.MatrixFeatures_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestTwistOps_F64 {

	@Test
	void homogenous_se3() {
		Se3_F64 original = new Se3_F64();
		original.T.setTo(1,2,3);

		DMatrixRMaj H = TwistOps_F64.homogenous(original,null);

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				assertEquals(original.R.get(row,col), H.get(row,col), GrlConstants.TEST_F64);
			}
		}

		for (int i = 0; i < 3; i++) {
			assertEquals(original.T.getIdx(i), H.get(i,3), GrlConstants.TEST_F64);
		}
		for (int i = 0; i < 3; i++) {
			assertEquals(0, H.get(3,i), GrlConstants.TEST_F64);
		}
		assertEquals(1, H.get(3,3), GrlConstants.TEST_F64);
	}

	@Test
	void homogenous_twist() {
		TwistCoordinate_F64 twist = new TwistCoordinate_F64();
		twist.w.setTo(-1,1,2);
		twist.w.normalize();
		twist.v.setTo(2,0.1,-0.7);

		DMatrixRMaj H = TwistOps_F64.homogenous(twist,null);

		DMatrixRMaj crossW = GeometryMath_F64.crossMatrix(twist.w,null);
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				assertEquals(crossW.get(row,col), H.get(row,col), GrlConstants.TEST_F64);
			}
		}

		for (int i = 0; i < 3; i++) {
			assertEquals(twist.v.getIdx(i), H.get(i,3), GrlConstants.TEST_F64);
		}
		for (int i = 0; i < 4; i++) {
			assertEquals(0, H.get(3,i), GrlConstants.TEST_F64);
		}
	}

	@Test
	void exponential_twist_pure_r() {
		Rodrigues_F64 rod = new Rodrigues_F64(0.2,0.1,0.3,-0.24);
		rod.unitAxisRotation.normalize();

		Se3_F64 expected = new Se3_F64();
		ConvertRotation3D_F64.rodriguesToMatrix(rod,expected.R);

		TwistCoordinate_F64 twist = new TwistCoordinate_F64();
		twist.w.setTo(rod.unitAxisRotation);

		Se3_F64 found = TwistOps_F64.exponential(twist,rod.theta,null);

		assertTrue(MatrixFeatures_DDRM.isIdentical(expected.R,found.R, GrlConstants.TEST_F64));
		assertTrue(found.T.norm()<= GrlConstants.TEST_F64);
	}

	@Test
	void exponential_twist_pure_t() {

		Se3_F64 expected = new Se3_F64();
		expected.T.setTo(1,0.1,0);

		TwistCoordinate_F64 twist = new TwistCoordinate_F64();
		twist.v.setTo(expected.T);
		GeometryMath_F64.divide(twist.v,0.45);

		Se3_F64 found = TwistOps_F64.exponential(twist,0.45,null);

		assertTrue(MatrixFeatures_DDRM.isIdentical(expected.R,found.R, GrlConstants.TEST_F64));
		assertTrue(found.T.isIdentical(expected.T, GrlConstants.TEST_F64));
	}

	/**
	 * If the w has a norm of 1 then there should be a unique twist I believe
	 */
	@Test
	void exponential_to_twist_verse_norm1() {

		TwistCoordinate_F64 twist = new TwistCoordinate_F64();
		twist.w.setTo(-1,1,2);
		twist.w.normalize();
		twist.v.setTo(2,0.1,-0.7);

		double theta = 0.7;
		Se3_F64 se3 = TwistOps_F64.exponential(twist,theta,null);

		TwistCoordinate_F64 foundTwist = TwistOps_F64.twist(se3, null);

		// the generated twist needs to be adjusted so that it has the same theta
		foundTwist.w.divide(theta);
		foundTwist.v.divide(theta);

		assertTrue(foundTwist.v.isIdentical(twist.v, GrlConstants.TEST_F64) );
		assertTrue(foundTwist.w.isIdentical(twist.w, GrlConstants.TEST_F64) );
	}

	/**
	 * The norm of w is not one, but the two twists should describe the same Se3
	 */
	@Test
	void exponential_to_twist_verse_NotNorm1() {

		TwistCoordinate_F64 twist1 = new TwistCoordinate_F64();
		twist1.w.setTo(-1,1,2);
		twist1.w.scale(GrlConstants.TEST_SQ_F64);
		twist1.v.setTo(2,0.1,-0.7);

		double theta1 = 0.7;
		Se3_F64 motion1 = TwistOps_F64.exponential(twist1,theta1,null);

		TwistCoordinate_F64 twist2 = TwistOps_F64.twist(motion1, null);

		Se3_F64 motion2 = TwistOps_F64.exponential(twist2,1.0,null);

		DMatrixRMaj diffR = new SimpleMatrix(motion1.R).transpose().mult(new SimpleMatrix(motion2.R)).getMatrix();

		assertTrue(MatrixFeatures_DDRM.isIdentity(diffR, GrlConstants.TEST_F64) );
		assertTrue(motion1.T.isIdentical(motion1.T, GrlConstants.TEST_F64) );

	}
}
