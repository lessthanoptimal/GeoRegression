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

package georegression.struct.se;

import georegression.geometry.ConvertRotation3D_F32;
import georegression.geometry.GeometryMath_F32;
import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.affine.Affine2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.transform.affine.AffinePointOps_F32;
import georegression.transform.se.SePointOps_F32;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.CommonOps_FDRM;
import org.ejml.dense.row.MatrixFeatures_FDRM;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestSpecialEuclideanOps_F32 {

	@Test
	public void toAffine_2D() {
		Se2_F32 se = new Se2_F32(1.5f,-3.4f,0.5f);
		Affine2D_F32 affine = SpecialEuclideanOps_F32.toAffine(se,null);

		Point2D_F32 original = new Point2D_F32(-1.5f,2.3f);
		Point2D_F32 found = new Point2D_F32();
		Point2D_F32 expected = new Point2D_F32();

		SePointOps_F32.transform(se,original,expected);
		AffinePointOps_F32.transform(affine, original, found);

		assertEquals(expected.x,found.x,GrlConstants.TEST_F32);
		assertEquals(expected.y,found.y,GrlConstants.TEST_F32);
	}

	@Test
	public void toHomogeneous_3D() {
		Se3_F32 se = SpecialEuclideanOps_F32.setEulerXYZ( 0.1f, 2, -0.3f, 2, -3, 4.4f, null );

		FMatrixRMaj H = SpecialEuclideanOps_F32.toHomogeneous( se, null );

		assertEquals( 4, H.numCols );
		assertEquals( 4, H.numRows );

		FMatrixRMaj R = se.getR();

		for( int i = 0; i < 3; i++ ) {
			for( int j = 0; j < 3; j++ ) {
				assertTrue( R.get( i, j ) == H.get( i, j ) );
			}
			assertTrue( 0 == H.get( 3, i ) );
		}

		assertTrue( se.getX() == H.get( 0, 3 ) );
		assertTrue( se.getY() == H.get( 1, 3 ) );
		assertTrue( se.getZ() == H.get( 2, 3 ) );
	}

	@Test
	public void toHomogeneous_2D() {
		Point2D_F32 pt = new Point2D_F32( 3.4f, -9.21f );
		Se2_F32 se = new Se2_F32( -3, 6.9f, -1.3f );

		FMatrixRMaj H = SpecialEuclideanOps_F32.toHomogeneous( se, null );

		Point2D_F32 expected = SePointOps_F32.transform( se, pt, null );

		// convert the point into homogeneous matrix notation
		FMatrixRMaj pt_m = new FMatrixRMaj( 3, 1 );
		pt_m.set( 0, 0, pt.x );
		pt_m.set( 1, 0, pt.y );
		pt_m.set( 2, 0, 1 );

		FMatrixRMaj found = new FMatrixRMaj( 3, 1 );
		CommonOps_FDRM.mult( H, pt_m, found );

		assertEquals( expected.x, found.get( 0, 0 ), GrlConstants.TEST_F32);
		assertEquals( expected.y, found.get( 1, 0 ), GrlConstants.TEST_F32);
		assertEquals( 1, found.get( 2, 0 ), GrlConstants.TEST_F32);
	}

	@Test
	public void toSe3_F32() {
		Se3_F32 se = SpecialEuclideanOps_F32.setEulerXYZ( 0.1f, 2, -0.3f, 2, -3, 4.4f, null );

		FMatrixRMaj H = SpecialEuclideanOps_F32.toHomogeneous( se, null );

		Se3_F32 found = SpecialEuclideanOps_F32.toSe3( H, null );

		assertEquals( se.getX(), found.getX(), GrlConstants.TEST_F32);
		assertEquals( se.getY(), found.getY(), GrlConstants.TEST_F32);
		assertEquals( se.getZ(), found.getZ(), GrlConstants.TEST_F32);

		assertTrue( MatrixFeatures_FDRM.isIdentical( se.getR(), found.getR(), GrlConstants.TEST_F32) );
	}

	@Test
	public void toSe2() {
		Se2_F32 se = new Se2_F32( -3, 6.9f, -1.3f );

		FMatrixRMaj H = SpecialEuclideanOps_F32.toHomogeneous( se, null );

		Se2_F32 found = SpecialEuclideanOps_F32.toSe2( H, null );

		assertEquals( se.getX(), found.getX(), GrlConstants.TEST_F32);
		assertEquals( se.getY(), found.getY(), GrlConstants.TEST_F32);
		assertEquals( se.getCosineYaw(), found.getCosineYaw(), GrlConstants.TEST_F32);
		assertEquals( se.getSineYaw(), found.getSineYaw(), GrlConstants.TEST_F32);
	}

	@Test
	public void setEulerXYZ() {
		Point3D_F32 orig = new Point3D_F32( 1, 2, 3 );

		Se3_F32 se = SpecialEuclideanOps_F32.setEulerXYZ( 0.1f, 2, -0.3f, 2, -3, 4.4f, null );

		Point3D_F32 expected = SePointOps_F32.transform( se, orig, null );

		FMatrixRMaj R = ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ, 0.1f, 2, -0.3f, se.getR() );

		Point3D_F32 found = GeometryMath_F32.mult( R, orig, (Point3D_F32) null );
		found.x += 2;
		found.y += -3;
		found.z += 4.4f;

		assertTrue( found.isIdentical( expected, GrlConstants.TEST_F32) );
	}
}
