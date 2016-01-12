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

package georegression.struct.se;

import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.GeometryMath_F64;
import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.affine.Affine2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.transform.affine.AffinePointOps_F64;
import georegression.transform.se.SePointOps_F64;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestSpecialEuclideanOps_F64 {

	@Test
	public void toAffine_2D() {
		Se2_F64 se = new Se2_F64(1.5,-3.4,0.5);
		Affine2D_F64 affine = SpecialEuclideanOps_F64.toAffine(se,null);

		Point2D_F64 original = new Point2D_F64(-1.5,2.3);
		Point2D_F64 found = new Point2D_F64();
		Point2D_F64 expected = new Point2D_F64();

		SePointOps_F64.transform(se,original,expected);
		AffinePointOps_F64.transform(affine, original, found);

		assertEquals(expected.x,found.x,GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(expected.y,found.y,GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void toHomogeneous_3D() {
		Se3_F64 se = SpecialEuclideanOps_F64.setEulerXYZ( 0.1, 2, -0.3, 2, -3, 4.4, null );

		DenseMatrix64F H = SpecialEuclideanOps_F64.toHomogeneous( se, null );

		assertEquals( 4, H.numCols );
		assertEquals( 4, H.numRows );

		DenseMatrix64F R = se.getR();

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
		Point2D_F64 pt = new Point2D_F64( 3.4, -9.21 );
		Se2_F64 se = new Se2_F64( -3, 6.9, -1.3 );

		DenseMatrix64F H = SpecialEuclideanOps_F64.toHomogeneous( se, null );

		Point2D_F64 expected = SePointOps_F64.transform( se, pt, null );

		// convert the point into homogeneous matrix notation
		DenseMatrix64F pt_m = new DenseMatrix64F( 3, 1 );
		pt_m.set( 0, 0, pt.x );
		pt_m.set( 1, 0, pt.y );
		pt_m.set( 2, 0, 1 );

		DenseMatrix64F found = new DenseMatrix64F( 3, 1 );
		CommonOps.mult( H, pt_m, found );

		assertEquals( expected.x, found.get( 0, 0 ), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( expected.y, found.get( 1, 0 ), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 1, found.get( 2, 0 ), GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void toSe3_F64() {
		Se3_F64 se = SpecialEuclideanOps_F64.setEulerXYZ( 0.1, 2, -0.3, 2, -3, 4.4, null );

		DenseMatrix64F H = SpecialEuclideanOps_F64.toHomogeneous( se, null );

		Se3_F64 found = SpecialEuclideanOps_F64.toSe3_F64( H, null );

		assertEquals( se.getX(), found.getX(), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( se.getY(), found.getY(), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( se.getZ(), found.getZ(), GrlConstants.DOUBLE_TEST_TOL );

		assertTrue( MatrixFeatures.isIdentical( se.getR(), found.getR(), GrlConstants.DOUBLE_TEST_TOL ) );
	}

	@Test
	public void toSe2() {
		Se2_F64 se = new Se2_F64( -3, 6.9, -1.3 );

		DenseMatrix64F H = SpecialEuclideanOps_F64.toHomogeneous( se, null );

		Se2_F64 found = SpecialEuclideanOps_F64.toSe2( H, null );

		assertEquals( se.getX(), found.getX(), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( se.getY(), found.getY(), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( se.getCosineYaw(), found.getCosineYaw(), GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( se.getSineYaw(), found.getSineYaw(), GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void setEulerXYZ() {
		Point3D_F64 orig = new Point3D_F64( 1, 2, 3 );

		Se3_F64 se = SpecialEuclideanOps_F64.setEulerXYZ( 0.1, 2, -0.3, 2, -3, 4.4, null );

		Point3D_F64 expected = SePointOps_F64.transform( se, orig, null );

		DenseMatrix64F R = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, 0.1, 2, -0.3, se.getR() );

		Point3D_F64 found = GeometryMath_F64.mult( R, orig, (Point3D_F64) null );
		found.x += 2;
		found.y += -3;
		found.z += 4.4;

		assertTrue( found.isIdentical( expected, GrlConstants.DOUBLE_TEST_TOL ) );
	}
}
