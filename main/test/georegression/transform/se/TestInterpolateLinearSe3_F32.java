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
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.EulerType;
import georegression.struct.se.Se3_F32;
import org.ejml.ops.MatrixFeatures_D32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestInterpolateLinearSe3_F32 {

	@Test
	public void identicalInputs() {
		Se3_F32 a = create(1,2,3,0.3f,   -0.2f,1.2f);
		InterpolateLinearSe3_F32 alg = new InterpolateLinearSe3_F32();

		alg.setTransforms(a,a);
		Se3_F32 b = new Se3_F32();

		for( int i = 0; i < 10; i++ ) {
			alg.interpolate(i/(float)9, b);
			GeometryUnitTest.assertEquals(a, b, GrlConstants.TEST_F32, GrlConstants.TEST_F32 *1000);
		}
	}

	@Test
	public void justTranslation() {
		Se3_F32 a = create(1,2,3,    0.3f,-0.2f,1.2f);
		Se3_F32 b = create(4,4,4,    0.3f,-0.2f,1.2f);
		InterpolateLinearSe3_F32 alg = new InterpolateLinearSe3_F32();

		alg.setTransforms(a,b);

		Se3_F32 c = new Se3_F32();

		for( int i = 0; i < 10; i++ ) {
			float t = i / (float) 9;
			alg.interpolate(t, c);
			float expectedX = 1 + 3*t;
			float expectedY = 2 + 2*t;
			float expectedZ = 3 + 1*t;

			assertEquals(expectedX,c.T.x, GrlConstants.TEST_F32);
			assertEquals(expectedY,c.T.y, GrlConstants.TEST_F32);
			assertEquals(expectedZ,c.T.z, GrlConstants.TEST_F32);
			assertTrue(MatrixFeatures_D32.isIdentical(c.getR(),a.getR(),GrlConstants.TEST_F32));
		}
	}

	@Test
	public void justRotation() {
		Se3_F32 a = create(1,2,3,    0.1f,0,0);
		Se3_F32 b = create(1,2,3,    0.9f,0,0);
		InterpolateLinearSe3_F32 alg = new InterpolateLinearSe3_F32();

		alg.setTransforms(a,b);

		Se3_F32 c = new Se3_F32();


		for( int i = 0; i < 10; i++ ) {
			float t = i / (float) 9;
			alg.interpolate(t, c);

			float euler[] = ConvertRotation3D_F32.matrixToEuler(c.getR(),EulerType.XYZ,(float[])null);

			assertEquals(1,c.T.x, GrlConstants.TEST_F32);
			assertEquals(2,c.T.y, GrlConstants.TEST_F32);
			assertEquals(3,c.T.z, GrlConstants.TEST_F32);

			assertEquals(0.1f + t * 0.8f, euler[0], GrlConstants.TEST_F32);
			assertEquals(0,euler[1],GrlConstants.TEST_F32);
			assertEquals(0,euler[2],GrlConstants.TEST_F32);
		}
	}

	@Test
	public void both() {
		Se3_F32 a = create(1,2,3,    0.1f,0,0);
		Se3_F32 b = create(4,4,4,    0.9f,0,0);
		InterpolateLinearSe3_F32 alg = new InterpolateLinearSe3_F32();

		alg.setTransforms(a,b);

		Se3_F32 c = new Se3_F32();

		for( int i = 0; i < 10; i++ ) {
			float t = i / (float) 9;
			alg.interpolate(t, c);

			float euler[] = ConvertRotation3D_F32.matrixToEuler(c.getR(),EulerType.XYZ,(float[])null);

			assertEquals(1+3*t,c.T.x, GrlConstants.TEST_F32);
			assertEquals(2+2*t,c.T.y, GrlConstants.TEST_F32);
			assertEquals(3+1*t,c.T.z, GrlConstants.TEST_F32);

			assertEquals(0.1f+t*0.8f,euler[0],GrlConstants.TEST_F32);
			assertEquals(0,euler[1],GrlConstants.TEST_F32);
			assertEquals(0,euler[2],GrlConstants.TEST_F32);
		}
	}

	public static Se3_F32 create( float x , float y, float z,
								  float rotX, float rotY , float rotZ ) {
		Se3_F32 ret = new Se3_F32();
		ret.setTranslation(x,y,z);
		ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,rotX,rotY,rotZ,ret.R);
		return ret;
	}
}
