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

import georegression.geometry.RotationMatrixGenerator;
import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.EulerType;
import georegression.struct.se.Se3_F64;
import org.ejml.ops.MatrixFeatures;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestInterpolateLinearSe3_F64 {

	@Test
	public void identicalInputs() {
		Se3_F64 a = create(1,2,3,0.3,   -0.2,1.2);
		InterpolateLinearSe3_F64 alg = new InterpolateLinearSe3_F64();

		alg.setTransforms(a,a);
		Se3_F64 b = new Se3_F64();

		for( int i = 0; i < 10; i++ ) {
			alg.interpolate(i/(double)9, b);
			GeometryUnitTest.assertEquals(a, b, GrlConstants.DOUBLE_TEST_TOL, GrlConstants.DOUBLE_TEST_TOL*1000);
		}
	}

	@Test
	public void justTranslation() {
		Se3_F64 a = create(1,2,3,    0.3,-0.2,1.2);
		Se3_F64 b = create(4,4,4,    0.3,-0.2,1.2);
		InterpolateLinearSe3_F64 alg = new InterpolateLinearSe3_F64();

		alg.setTransforms(a,b);

		Se3_F64 c = new Se3_F64();

		for( int i = 0; i < 10; i++ ) {
			double t = i / (double) 9;
			alg.interpolate(t, c);
			double expectedX = 1 + 3*t;
			double expectedY = 2 + 2*t;
			double expectedZ = 3 + 1*t;

			assertEquals(expectedX,c.T.x, GrlConstants.DOUBLE_TEST_TOL);
			assertEquals(expectedY,c.T.y, GrlConstants.DOUBLE_TEST_TOL);
			assertEquals(expectedZ,c.T.z, GrlConstants.DOUBLE_TEST_TOL);
			assertTrue(MatrixFeatures.isIdentical(c.getR(),a.getR(),GrlConstants.DOUBLE_TEST_TOL));
		}
	}

	@Test
	public void justRotation() {
		Se3_F64 a = create(1,2,3,    0.1,0,0);
		Se3_F64 b = create(1,2,3,    0.9,0,0);
		InterpolateLinearSe3_F64 alg = new InterpolateLinearSe3_F64();

		alg.setTransforms(a,b);

		Se3_F64 c = new Se3_F64();


		for( int i = 0; i < 10; i++ ) {
			double t = i / (double) 9;
			alg.interpolate(t, c);

			double euler[] = RotationMatrixGenerator.matrixToEulerXYZ(c.getR(),(double[])null);

			assertEquals(1,c.T.x, GrlConstants.DOUBLE_TEST_TOL);
			assertEquals(2,c.T.y, GrlConstants.DOUBLE_TEST_TOL);
			assertEquals(3,c.T.z, GrlConstants.DOUBLE_TEST_TOL);

			assertEquals(0.1 + t * 0.8, euler[0], GrlConstants.DOUBLE_TEST_TOL);
			assertEquals(0,euler[1],GrlConstants.DOUBLE_TEST_TOL);
			assertEquals(0,euler[2],GrlConstants.DOUBLE_TEST_TOL);
		}
	}

	@Test
	public void both() {
		Se3_F64 a = create(1,2,3,    0.1,0,0);
		Se3_F64 b = create(4,4,4,    0.9,0,0);
		InterpolateLinearSe3_F64 alg = new InterpolateLinearSe3_F64();

		alg.setTransforms(a,b);

		Se3_F64 c = new Se3_F64();

		for( int i = 0; i < 10; i++ ) {
			double t = i / (double) 9;
			alg.interpolate(t, c);

			double euler[] = RotationMatrixGenerator.matrixToEulerXYZ(c.getR(),(double[])null);

			assertEquals(1+3*t,c.T.x, GrlConstants.DOUBLE_TEST_TOL);
			assertEquals(2+2*t,c.T.y, GrlConstants.DOUBLE_TEST_TOL);
			assertEquals(3+1*t,c.T.z, GrlConstants.DOUBLE_TEST_TOL);

			assertEquals(0.1+t*0.8,euler[0],GrlConstants.DOUBLE_TEST_TOL);
			assertEquals(0,euler[1],GrlConstants.DOUBLE_TEST_TOL);
			assertEquals(0,euler[2],GrlConstants.DOUBLE_TEST_TOL);
		}
	}

	public static Se3_F64 create( double x , double y, double z,
								  double rotX, double rotY , double rotZ ) {
		Se3_F64 ret = new Se3_F64();
		ret.setTranslation(x,y,z);
		RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,rotX,rotY,rotZ,ret.R);
		return ret;
	}
}
