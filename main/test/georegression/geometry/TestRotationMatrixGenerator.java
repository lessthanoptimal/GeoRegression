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

package georegression.geometry;

import georegression.misc.test.GeometryUnitTest;
import georegression.struct.point.Point3D_F64;
import georegression.struct.so.Quaternion_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.UtilEjml;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;
import org.ejml.ops.RandomMatrices;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
public class TestRotationMatrixGenerator {

	Random rand = new Random( 234234 );

	@Test
	public void rodriguesF64_to_Matrix() {
		DenseMatrix64F rotZ = RotationMatrixGenerator.rotZ( 0.5, null );

		Rodrigues_F64 r = new Rodrigues_F64( 0.5, 0, 0, 1 );

		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( r, null );

		assertTrue( MatrixFeatures.isIdentical( rotZ, rod, 1e-8 ) );
	}

	@Test
	public void rodriguesF32_to_Matrix() {
		fail("implement");
	}

	@Test
	public void rodriguesToQuaternion() {
		fail( "Implement" );
	}

	@Test
	public void quaternionToRodrigues() {
		fail( "implement" );
	}

	@Test
	public void matrixToQuaternion() {
		fail( "implement" );
	}

	@Test
	public void rotationAxis() {
		fail( "Implement" );
	}

	@Test
	public void rotationAngle() {
		fail( "Implement" );
	}

	@Test
	public void matrixToRodrigues_F64() {
		// create the rotation axis
//		for( int i = 1; i < 20; i++ ) {
//			double angle = i * Math.PI / 20;
//			checkMatrixToRodrigues( new Rodrigues( angle, 0.1, 2, 6 ) );
//			checkMatrixToRodrigues( new Rodrigues( angle, 1, 0, 0 ) );
//			checkMatrixToRodrigues( new Rodrigues( angle, 1, 1, 1 ) );
//			checkMatrixToRodrigues( new Rodrigues( angle, -1, -1, -1 ) );
//		}
//
//		// see how well it handles underflow
//		checkMatrixToRodrigues( new Rodrigues( 1e-7, -1, -1, -1 ) );
//
//		// test known pathological cases
//		checkMatrixToRodrigues( new Rodrigues( 0, 1, 1, 1 ), new Rodrigues( 0, 1, 0, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( 1e-4, 1, 1, 1 ), new Rodrigues_F64( 1e-4, 1, 0, 0 ) );
//		checkMatrixToRodrigues( new Rodrigues( Math.PI/2, 1, 1, 1 ), new Rodrigues( Math.PI/2, 1, 1, 1 ) );
//		checkMatrixToRodrigues( new Rodrigues( Math.PI, 1, 1, 1 ), new Rodrigues( Math.PI, 1, 1, 1 ) );
//		checkMatrixToRodrigues( new Rodrigues( -Math.PI, 1, 1, 1 ), new Rodrigues( Math.PI, 1, 1, 1 ) );
	}

	@Test
	public void matrixToRodrigues_F32() {
		fail("implement");
	}

	private void checkMatrixToRodrigues( Rodrigues_F64 rodInput ) {
		// create the matrix using rodrigues
		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( rodInput, null );

		// see if the vectors are the same
		Rodrigues_F64 found = RotationMatrixGenerator.matrixToRodrigues( rod, (Rodrigues_F64)null );

		// if the lines are parallel the dot product will be 1 or -1
		double dot = found.unitAxisRotation.dot( rodInput.unitAxisRotation);
		assertEquals( 1, Math.abs( dot ), 1e-8 );

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertEquals( rodInput.theta * dot, found.theta, 1e-8 );
	}

	private void checkMatrixToRodrigues( Rodrigues_F64 input,
										 Rodrigues_F64 expected ) {

		// create the matrix using rodrigues
		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( input, null );

		// see if the vectors are the same
		Rodrigues_F64 found = RotationMatrixGenerator.matrixToRodrigues( rod, (Rodrigues_F64)null );

		// if the lines are parallel the dot product will be 1 or -1
		assertEquals( 1, Math.abs( found.unitAxisRotation.dot( expected.unitAxisRotation) ), 1e-8 );

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertEquals( expected.theta, found.theta, 1e-7 );
	}

	/**
	 * A found test case where it failed
	 */
	@Test
	public void matrixToRodrigues_case0() {
		DenseMatrix64F R = UtilEjml.parseMatrix(
						"1.00000000000000000000e+00 -5.42066399999221260000e-14 -3.16267800000013500000e-13 \n" +
						"5.42066400000000000000e-14 1.00000000000000040000e+00 2.46136444559397200000e-13 \n" +
						"3.16267800000000000000e-13 -2.46191955710628460000e-13 1.00000000000000040000e+00", 3);

		Rodrigues_F64 found = RotationMatrixGenerator.matrixToRodrigues( R, (Rodrigues_F64)null );

		assertEquals(0,found.getTheta(),1e-8);
	}

	@Test
	public void matrixToRodrigues_case1() {
		DenseMatrix64F R = UtilEjml.parseMatrix(
						"0.99999999999999000000e+00 -5.42066399999221260000e-14 -3.16267800000013500000e-13 \n" +
						"5.42066400000000000000e-14 0.99999999999999000000e+00 2.46136444559397200000e-13 \n" +
						"3.16267800000000000000e-13 -2.46191955710628460000e-13 0.99999999999999000000e+00", 3);

		Rodrigues_F64 found = RotationMatrixGenerator.matrixToRodrigues( R, (Rodrigues_F64)null );

		assertEquals(0,found.getTheta(),1e-8);
	}


	@Test
	public void rotX() {
		Point3D_F64 pt_y = new Point3D_F64( 0, 1.5, 0 );
		Point3D_F64 pt_z = new Point3D_F64( 0, 0, 1.5 );

		DenseMatrix64F R = RotationMatrixGenerator.rotX( Math.PI / 2.0, null );

		GeometryMath_F64.mult( R, pt_y, pt_y );
		GeometryMath_F64.mult( R, pt_z, pt_z );

		assertTrue( pt_y.isIdentical( 0, 0, 1.5, 1e-8 ) );
		assertTrue( pt_z.isIdentical( 0, -1.5, 0, 1e-8 ) );
	}

	@Test
	public void rotY() {
		Point3D_F64 pt_x = new Point3D_F64( 1.5, 0, 0 );
		Point3D_F64 pt_z = new Point3D_F64( 0, 0, 1.5 );

		DenseMatrix64F R = RotationMatrixGenerator.rotY( Math.PI / 2.0, null );

		GeometryMath_F64.mult( R, pt_x, pt_x );
		GeometryMath_F64.mult( R, pt_z, pt_z );

		assertTrue( pt_x.isIdentical( 0, 0, -1.5, 1e-8 ) );
		assertTrue( pt_z.isIdentical( 1.5, 0, 0, 1e-8 ) );
	}

	@Test
	public void rotZ() {
		Point3D_F64 pt_x = new Point3D_F64( 1.5, 0, 0 );
		Point3D_F64 pt_y = new Point3D_F64( 0, 1.5, 0 );

		DenseMatrix64F R = RotationMatrixGenerator.rotZ( Math.PI / 2.0, null );

		GeometryMath_F64.mult( R, pt_x, pt_x );
		GeometryMath_F64.mult( R, pt_y, pt_y );

		assertTrue( pt_x.isIdentical( 0, 1.5, 0, 1e-8 ) );
		assertTrue( pt_y.isIdentical( -1.5, 0, 0, 1e-8 ) );
	}

	@Test
	public void eulerArbitrary() {
		DenseMatrix64F R_e = RotationMatrixGenerator.eulerXYZ( 1.2, -.5, 2.4, null );
		DenseMatrix64F R_a = RotationMatrixGenerator.eulerArbitrary( 0, 1, 2, 1.2, -.5, 2.4 );

		assertTrue( MatrixFeatures.isIdentical( R_e, R_a, 1e-8 ) );
	}

	@Test
	public void eulerXYZ() {
		DenseMatrix64F R_x = RotationMatrixGenerator.rotX( 1.2, null );
		DenseMatrix64F R_y = RotationMatrixGenerator.rotY( -.5, null );
		DenseMatrix64F R_z = RotationMatrixGenerator.rotZ( 2.4, null );

		DenseMatrix64F A = new DenseMatrix64F( 3, 3 );
		DenseMatrix64F R = new DenseMatrix64F( 3, 3 );

		CommonOps.mult( R_y, R_x, A );
		CommonOps.mult( R_z, A, R );

		DenseMatrix64F xyz = RotationMatrixGenerator.eulerXYZ( 1.2, -.5, 2.4, null );

		Point3D_F64 pt_found = new Point3D_F64( -1.56, 2.03, 0.5 );
		Point3D_F64 pt_expected = pt_found.copy();


		GeometryMath_F64.mult( R, pt_expected, pt_expected );
		GeometryMath_F64.mult( xyz, pt_found, pt_found );

		assertTrue( pt_expected.isIdentical( pt_found, 1e-8 ) );

	}

	@Test
	public void matrixToEulerXYZ_F64() {
		// test case one
		DenseMatrix64F A = RotationMatrixGenerator.eulerXYZ( 0.1, -0.5, -0.96, null );
		double[] euler = RotationMatrixGenerator.matrixToEulerXYZ( A , (double[])null );
		DenseMatrix64F B = RotationMatrixGenerator.eulerXYZ( euler[0], euler[1], euler[2], null );

		assertTrue( MatrixFeatures.isIdentical( A, B, 1e-8 ) );

		// now try a pathological case
		A = RotationMatrixGenerator.eulerXYZ( 0.1, -0.5, 0, null );
		euler = RotationMatrixGenerator.matrixToEulerXYZ( A , (double[])null );
		B = RotationMatrixGenerator.eulerXYZ( euler[0], euler[1], euler[2], null );

		assertTrue( MatrixFeatures.isIdentical( A, B, 1e-8 ) );

		// try all zeros
		A = RotationMatrixGenerator.eulerXYZ( 0, 0, 0, null );
		euler = RotationMatrixGenerator.matrixToEulerXYZ( A , (double[])null );
		B = RotationMatrixGenerator.eulerXYZ( euler[0], euler[1], euler[2], null );

		assertTrue( MatrixFeatures.isIdentical( A, B, 1e-8 ) );
	}

	/**
	 * Creates a random matrix and sees if the approximation is a valid rotation matrix
	 */
	@Test
	public void approximateRotationMatrix_random() {
		DenseMatrix64F Q = RandomMatrices.createRandom( 3, 3, rand );

		DenseMatrix64F R = RotationMatrixGenerator.approximateRotationMatrix( Q, null );

		assertTrue( MatrixFeatures.isOrthogonal( R, 1e-8 ) );
	}

	/**
	 * Create a rotation matrix and see if the exact same matrix is returned
	 */
	@Test
	public void approximateRotationMatrix_nochange() {
		DenseMatrix64F Q = RandomMatrices.createOrthogonal( 3, 3, rand );

		DenseMatrix64F R = RotationMatrixGenerator.approximateRotationMatrix( Q, null );

		assertTrue( MatrixFeatures.isIdentical( Q, R, 1e-8 ) );
	}

	@Test
	public void eulerToQuaternions() {
		fail( "Implement" );
	}

	/**
	 * Tests quaternions using the following property:
	 * <p/>
	 * q = cos(a/2) + u*sin(a/2)
	 * <p/>
	 * where 'a' is the angle of rotation, u is the unit axis of rotation.
	 */
	@Test
	public void quaternionToMatrix_F64() {
		// rotate around z-axis 90 degrees
		Quaternion_F64 q = RotationMatrixGenerator.rodriguesToQuaternion( new Rodrigues_F64( Math.PI / 2.0, 0, 0, 1 ), null );

		DenseMatrix64F R = RotationMatrixGenerator.quaternionToMatrix( q, null );

		Point3D_F64 p = new Point3D_F64( 1, 0, 0 );
		GeometryMath_F64.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 1, 0, 1e-8 );


		// rotate around y-axis 90 degrees
		q = RotationMatrixGenerator.rodriguesToQuaternion( new Rodrigues_F64( Math.PI / 2.0, 0, 1, 0 ), null );
		q.normalize();

		R = RotationMatrixGenerator.quaternionToMatrix( q, R );

		p.set( 1, 0, 0 );
		GeometryMath_F64.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 0, -1, 1e-8 );
	}

	@Test
	public void quaternionToMatrix_F32() {
		fail("implement");
	}
}
