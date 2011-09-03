/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.se;

import georegression.geometry.GeometryMath_F32;
import georegression.geometry.RotationMatrixGenerator;
import georegression.misc.autocode.JgrlConstants;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.transform.se.SePointOps_F32;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
public class TestSpecialEuclideanOps_F32 {

	@Test
	public void toAffine_2D() {
		fail("implement");
	}

	@Test
	public void toHomogeneous_3D() {
		Se3_F32 se = SpecialEuclideanOps_F32.setEulerXYZ( 0.1f, 2, -0.3f, 2, -3, 4.4f, null );

		DenseMatrix64F H = SpecialEuclideanOps_F32.toHomogeneous( se, null );

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
		Point2D_F32 pt = new Point2D_F32( 3.4f, -9.21f );
		Se2_F32 se = new Se2_F32( -3, 6.9f, -1.3f );

		DenseMatrix64F H = SpecialEuclideanOps_F32.toHomogeneous( se, null );

		Point2D_F32 expected = SePointOps_F32.transform( se, pt, null );

		// convert the point into homogeneous matrix notation
		DenseMatrix64F pt_m = new DenseMatrix64F( 3, 1 );
		pt_m.set( 0, 0, pt.x );
		pt_m.set( 1, 0, pt.y );
		pt_m.set( 2, 0, 1 );

		DenseMatrix64F found = new DenseMatrix64F( 3, 1 );
		CommonOps.mult( H, pt_m, found );

		assertEquals( expected.x, found.get( 0, 0 ), JgrlConstants.FLOAT_TEST_TOL );
		assertEquals( expected.y, found.get( 1, 0 ), JgrlConstants.FLOAT_TEST_TOL );
		assertEquals( 1, found.get( 2, 0 ), JgrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void toSe3_F32() {
		Se3_F32 se = SpecialEuclideanOps_F32.setEulerXYZ( 0.1f, 2, -0.3f, 2, -3, 4.4f, null );

		DenseMatrix64F H = SpecialEuclideanOps_F32.toHomogeneous( se, null );

		Se3_F32 found = SpecialEuclideanOps_F32.toSe3_F32( H, null );

		assertEquals( se.getX(), found.getX(), JgrlConstants.FLOAT_TEST_TOL );
		assertEquals( se.getY(), found.getY(), JgrlConstants.FLOAT_TEST_TOL );
		assertEquals( se.getZ(), found.getZ(), JgrlConstants.FLOAT_TEST_TOL );

		assertTrue( MatrixFeatures.isIdentical( se.getR(), found.getR(), JgrlConstants.FLOAT_TEST_TOL ) );
	}

	@Test
	public void toSe2() {
		Se2_F32 se = new Se2_F32( -3, 6.9f, -1.3f );

		DenseMatrix64F H = SpecialEuclideanOps_F32.toHomogeneous( se, null );

		Se2_F32 found = SpecialEuclideanOps_F32.toSe2( H, null );

		assertEquals( se.getX(), found.getX(), JgrlConstants.FLOAT_TEST_TOL );
		assertEquals( se.getY(), found.getY(), JgrlConstants.FLOAT_TEST_TOL );
		assertEquals( se.getCosineYaw(), found.getCosineYaw(), JgrlConstants.FLOAT_TEST_TOL );
		assertEquals( se.getSineYaw(), found.getSineYaw(), JgrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void setEulerXYZ() {
		Point3D_F32 orig = new Point3D_F32( 1, 2, 3 );

		Se3_F32 se = SpecialEuclideanOps_F32.setEulerXYZ( 0.1f, 2, -0.3f, 2, -3, 4.4f, null );

		Point3D_F32 expected = SePointOps_F32.transform( se, orig, null );

		DenseMatrix64F R = RotationMatrixGenerator.eulerXYZ( 0.1f, 2, -0.3f, se.getR() );

		Point3D_F32 found = GeometryMath_F32.mult( R, orig, (Point3D_F32) null );
		found.x += 2;
		found.y += -3;
		found.z += 4.4f;

		assertTrue( found.isIdentical( expected, JgrlConstants.FLOAT_TEST_TOL ) );
	}
}
