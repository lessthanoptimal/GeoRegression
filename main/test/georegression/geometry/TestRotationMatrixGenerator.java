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

import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.EulerType;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Point3D_F64;
import georegression.struct.so.Quaternion_F32;
import georegression.struct.so.Quaternion_F64;
import georegression.struct.so.Rodrigues_F32;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.UtilEjml;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;
import org.ejml.ops.RandomMatrices;
import org.junit.Test;

import java.util.Random;

import static georegression.misc.GrlConstants.F_PI;
import static georegression.misc.GrlConstants.F_PId2;
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

		assertTrue( MatrixFeatures.isIdentical( rotZ, rod, GrlConstants.DOUBLE_TEST_TOL ) );
	}

	@Test
	public void rodriguesF32_to_Matrix() {
		DenseMatrix64F rotZ = RotationMatrixGenerator.rotZ( 0.5, null );

		Rodrigues_F32 r = new Rodrigues_F32( 0.5f, 0, 0, 1 );

		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( r, null );

		assertTrue( MatrixFeatures.isIdentical( rotZ, rod, GrlConstants.FLOAT_TEST_TOL ) );
	}

	@Test
	public void rodriguesToQuaternion_F32() {
		Rodrigues_F32 rod = new Rodrigues_F32(-1.5f,1,3,-4);

		Quaternion_F32 quat = RotationMatrixGenerator.rodriguesToQuaternion(rod,null);

		DenseMatrix64F A = RotationMatrixGenerator.quaternionToMatrix(quat, null);
		DenseMatrix64F B = RotationMatrixGenerator.rodriguesToMatrix(rod, null);

		DenseMatrix64F C = new DenseMatrix64F(3,3);
		CommonOps.multTransA(A,B,C);

		assertTrue(MatrixFeatures.isIdentity(C, GrlConstants.FLOAT_TEST_TOL));
	}

	@Test
	public void rodriguesToQuaternion_F64() {
		Rodrigues_F64 rod = new Rodrigues_F64(-1.5,1,3,-4);

		Quaternion_F64 quat = RotationMatrixGenerator.rodriguesToQuaternion(rod, null);

		DenseMatrix64F A = RotationMatrixGenerator.quaternionToMatrix(quat, null);
		DenseMatrix64F B = RotationMatrixGenerator.rodriguesToMatrix(rod, null);

		DenseMatrix64F C = new DenseMatrix64F(3,3);
		CommonOps.multTransA(A,B,C);

		assertTrue(MatrixFeatures.isIdentity(C, GrlConstants.DOUBLE_TEST_TOL));
	}

	@Test
	public void quaternionToRodrigues_F32() {
		Quaternion_F32 quat = new Quaternion_F32(0.6f,2f,3f,-1f);
		quat.normalize();

		Rodrigues_F32 rod = RotationMatrixGenerator.quaternionToRodrigues(quat,null);

		DenseMatrix64F A = RotationMatrixGenerator.quaternionToMatrix(quat, null);
		DenseMatrix64F B = RotationMatrixGenerator.rodriguesToMatrix(rod, null);

		DenseMatrix64F C = new DenseMatrix64F(3,3);
		CommonOps.multTransA(A,B,C);

		assertTrue(MatrixFeatures.isIdentity(C, GrlConstants.FLOAT_TEST_TOL));
	}

	@Test
	public void quaternionToRodrigues_F64() {
		Quaternion_F64 quat = new Quaternion_F64(0.6,2,3,-1);
		quat.normalize();

		Rodrigues_F64 rod = RotationMatrixGenerator.quaternionToRodrigues(quat,null);

		quat.normalize();
		DenseMatrix64F A = RotationMatrixGenerator.quaternionToMatrix(quat, null);
		DenseMatrix64F B = RotationMatrixGenerator.rodriguesToMatrix(rod, null);

		DenseMatrix64F C = new DenseMatrix64F(3,3);
		CommonOps.multTransA(A,B,C);

		assertTrue(MatrixFeatures.isIdentity(C, GrlConstants.DOUBLE_TEST_TOL));
	}

	@Test
	public void quaternionToEuler() {
		for(EulerType type : EulerType.values() ) {
			System.out.println("type = "+type);

			double PId2 = Math.PI/2.0;

			quaternionToEuler(type,0,0,0);

			// single axis
			quaternionToEuler(type,PId2,0,0);
			quaternionToEuler(type,0,PId2,0);
			quaternionToEuler(type,0,0,PId2);
			quaternionToEuler(type,-PId2,0,0);
			quaternionToEuler(type,0,-PId2,0);
			quaternionToEuler(type,0,0,-PId2);

			// two axis maybe pathological
			quaternionToEuler(type,PId2,PId2,0);
			quaternionToEuler(type,PId2,0,PId2);

			quaternionToEuler(type,PId2,PId2,0);
			quaternionToEuler(type,0,PId2,PId2);

			quaternionToEuler(type,PId2,0,PId2);
			quaternionToEuler(type,0,PId2,PId2);

			for (int i = 0; i < 30; i++) {
				double rotA = 2.0*rand.nextDouble()*Math.PI-Math.PI;
				double rotB = rand.nextDouble()*Math.PI-Math.PI/2.0;
				double rotC = 2.0*rand.nextDouble()*Math.PI-Math.PI;

				quaternionToEuler(type,rotA,rotB,rotC);
			}
		}
	}

	private void quaternionToEuler(EulerType type , double rotA , double rotB , double rotC )
	{
		DenseMatrix64F expected = RotationMatrixGenerator.eulerToMatrix(type,rotA,rotB,rotC,null);

		Quaternion_F64 q = RotationMatrixGenerator.matrixToQuaternion(expected,null);
		double euler[] = RotationMatrixGenerator.quaternionToEuler(q,type,null);

		DenseMatrix64F found = RotationMatrixGenerator.eulerToMatrix(type,euler[0],euler[1],euler[2],null);

		DenseMatrix64F difference = new DenseMatrix64F(3,3);
		CommonOps.multTransB(expected,found,difference);
		assertTrue(MatrixFeatures.isIdentity(difference,Math.sqrt(GrlConstants.DOUBLE_TEST_TOL)));
	}

	@Test
	public void matrixToQuaternion() {

		double pid2 = Math.PI/2.0;

		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,0,0,0,null));

		// single axis rotations
		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,pid2,0,0,null));
		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,0,pid2,0,null));
		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,0,0,pid2,null));
		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,-pid2,0,0,null));
		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,0,-pid2,0,null));
		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,0,0,-pid2,null));

		// two axis rotations which could be pathological
		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,pid2,pid2,0,null));
		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,pid2,0,pid2,null));

		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,pid2,pid2,0,null));
		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,0,pid2,pid2,null));

		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,pid2,0,pid2,null));
		matrixToQuaternion(RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,0,pid2,pid2,null));

		for (int i = 0; i < 100; i++) {
			double rotX = 2.0*rand.nextDouble()*Math.PI-Math.PI;
			double rotY = 2.0*rand.nextDouble()*Math.PI-Math.PI;
			double rotZ = 2.0*rand.nextDouble()*Math.PI-Math.PI;

			DenseMatrix64F R = RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,rotX,rotY,rotZ,null);
			matrixToQuaternion(R);
		}
	}

	public void matrixToQuaternion( DenseMatrix64F R ) {
		Quaternion_F64 q = RotationMatrixGenerator.matrixToQuaternion(R,null);
		q.normalize();
		DenseMatrix64F found = RotationMatrixGenerator.quaternionToMatrix(q,null);

		DenseMatrix64F result = new DenseMatrix64F(3,3);
		CommonOps.multTransB(R,found,result);

		assertTrue(MatrixFeatures.isIdentity(result,Math.sqrt(GrlConstants.DOUBLE_TEST_TOL)));
	}

	@Test
	public void matrixToRodrigues_F64() {
		// create the rotation axis
		for( int i = 1; i < 20; i++ ) {
			double angle = i * Math.PI / 20;
			checkMatrixToRodrigues( new Rodrigues_F64( angle, 0.1, 2, 6 ) );
			checkMatrixToRodrigues( new Rodrigues_F64( angle, 1, 0, 0 ) );
			checkMatrixToRodrigues( new Rodrigues_F64( angle, 1, 1, 1 ) );
			checkMatrixToRodrigues( new Rodrigues_F64( angle, -1, -1, -1 ) );
		}

		// see how well it handles underflow
		checkMatrixToRodrigues( new Rodrigues_F64( 1e-7, -1, -1, -1 ) );

		// test known pathological cases
		checkMatrixToRodrigues( new Rodrigues_F64( 0, 1, 1, 1 ), new Rodrigues_F64( 0, 1, 0, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( Math.PI/2, 1, 1, 1 ), new Rodrigues_F64( Math.PI/2, 1, 1, 1 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( Math.PI, 1, 1, 1 ), new Rodrigues_F64( Math.PI, 1, 1, 1 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( -Math.PI, 1, 1, 1 ), new Rodrigues_F64( Math.PI, 1, 1, 1 ) );

		// edge case where diagonals sum up to 1
		checkMatrixToRodrigues( new Rodrigues_F64( Math.PI, 1, 0, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( Math.PI, 0, 1, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( Math.PI, 0, 0, 1 ));

		checkMatrixToRodrigues(Math.PI,Math.PI/2,0);
		checkMatrixToRodrigues(Math.PI,-Math.PI/2,0);
		checkMatrixToRodrigues(Math.PI,0,Math.PI/2);
		checkMatrixToRodrigues(Math.PI,0,-Math.PI/2);
		checkMatrixToRodrigues(0,Math.PI,Math.PI/2);
		checkMatrixToRodrigues(0,Math.PI,-Math.PI/2);;
	}

	@Test
	public void matrixToRodrigues_F32() {
		// create the rotation axis
		for( int i = 1; i < 20; i++ ) {
			float angle = i * F_PI / 20;
			checkMatrixToRodrigues( new Rodrigues_F32( angle, 0.1f, 2, 6 ) );
			checkMatrixToRodrigues( new Rodrigues_F32( angle, 1, 0, 0 ) );
			checkMatrixToRodrigues( new Rodrigues_F32( angle, 1, 1, 1 ) );
			checkMatrixToRodrigues( new Rodrigues_F32( angle, -1, -1, -1 ) );
		}

		// see how well it handles underflow
		checkMatrixToRodrigues( new Rodrigues_F64( 1e-7, -1, -1, -1 ) );

		// test known pathological cases
		checkMatrixToRodrigues( new Rodrigues_F32( 0, 1, 1, 1 ), new Rodrigues_F32( 0, 1, 0, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F32( F_PId2, 1, 1, 1 ), new Rodrigues_F32( F_PId2, 1, 1, 1 ) );
		checkMatrixToRodrigues( new Rodrigues_F32( F_PI, 1, 1, 1 ), new Rodrigues_F32( F_PI, 1, 1, 1 ) );
		checkMatrixToRodrigues( new Rodrigues_F32( -F_PI, 1, 1, 1 ), new Rodrigues_F32( F_PI, 1, 1, 1 ) );

		// edge case where diagonals sum up to 1
		checkMatrixToRodrigues( new Rodrigues_F32( F_PI, 1, 0, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F32( F_PI, 0, 1, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F32( F_PI, 0, 0, 1 ));

	}

	private void checkMatrixToRodrigues( Rodrigues_F64 rodInput ) {
		// create the matrix using rodrigues
		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( rodInput, null );

		// see if the vectors are the same
		Rodrigues_F64 found = RotationMatrixGenerator.matrixToRodrigues( rod, (Rodrigues_F64)null );

		// if the lines are parallel the dot product will be 1 or -1
		double dot = found.unitAxisRotation.dot( rodInput.unitAxisRotation);
		assertEquals( 1, Math.abs( dot ), GrlConstants.DOUBLE_TEST_TOL );

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertTrue(UtilAngle.dist(rodInput.theta * dot, found.theta) <= GrlConstants.DOUBLE_TEST_TOL);
	}

	private void checkMatrixToRodrigues( Rodrigues_F32 rodInput ) {
		// create the matrix using rodrigues
		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( rodInput, null );

		// see if the vectors are the same
		Rodrigues_F32 found = RotationMatrixGenerator.matrixToRodrigues( rod, (Rodrigues_F32)null );

		// if the lines are parallel the dot product will be 1 or -1
		float dot = found.unitAxisRotation.dot( rodInput.unitAxisRotation);
		assertEquals( 1, (float)Math.abs( dot ), GrlConstants.FLOAT_TEST_TOL );

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertTrue(UtilAngle.dist(rodInput.theta * dot, found.theta) <= GrlConstants.FLOAT_TEST_TOL);
	}

	private void checkMatrixToRodrigues( double eulerX , double eulerY , double eulerZ ) {

		DenseMatrix64F M = RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,eulerX,eulerY,eulerZ,null);
		Rodrigues_F64 rod = RotationMatrixGenerator.matrixToRodrigues(M, (Rodrigues_F64)null);
		DenseMatrix64F found = RotationMatrixGenerator.rodriguesToMatrix(rod,null);
		assertTrue(MatrixFeatures.isIdentical(M,found,1e-6));
	}

	private void checkMatrixToRodrigues( Rodrigues_F64 input,
										 Rodrigues_F64 expected ) {

		// create the matrix using rodrigues
		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( input, null );

		// see if the vectors are the same
		Rodrigues_F64 found = RotationMatrixGenerator.matrixToRodrigues( rod, (Rodrigues_F64)null );

		// if the lines are parallel the dot product will be 1 or -1
		assertEquals( 1, Math.abs( found.unitAxisRotation.dot( expected.unitAxisRotation) ), GrlConstants.DOUBLE_TEST_TOL );

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertEquals( expected.theta, found.theta, 10.0*GrlConstants.DOUBLE_TEST_TOL );
	}

	private void checkMatrixToRodrigues( Rodrigues_F32 input,
										 Rodrigues_F32 expected ) {

		// create the matrix using rodrigues
		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( input, null );

		// see if the vectors are the same
		Rodrigues_F32 found = RotationMatrixGenerator.matrixToRodrigues( rod, (Rodrigues_F32)null );

		// if the lines are parallel the dot product will be 1 or -1
		assertEquals( 1, Math.abs( found.unitAxisRotation.dot( expected.unitAxisRotation) ), GrlConstants.FLOAT_TEST_TOL );

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertEquals( expected.theta, found.theta, 10.0f*GrlConstants.FLOAT_TEST_TOL );
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

		assertEquals(0,found.getTheta(),5e-7);
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
	public void matrixToEuler() {
		for(EulerType type : EulerType.values() ) {
			System.out.println("type = "+type);

			double PId2 = Math.PI/2.0;

			quaternionToEuler(type,0,0,0);

			// single axis
			quaternionToEuler(type,PId2,0,0);
			quaternionToEuler(type,0,PId2,0);
			quaternionToEuler(type,0,0,PId2);
			quaternionToEuler(type,-PId2,0,0);
			quaternionToEuler(type,0,-PId2,0);
			quaternionToEuler(type,0,0,-PId2);

			// two axis maybe pathological
			quaternionToEuler(type,PId2,PId2,0);
			quaternionToEuler(type,PId2,0,PId2);

			quaternionToEuler(type,PId2,PId2,0);
			quaternionToEuler(type,0,PId2,PId2);

			quaternionToEuler(type,PId2,0,PId2);
			quaternionToEuler(type,0,PId2,PId2);

			for (int i = 0; i < 30; i++) {
				double rotA = 2.0*rand.nextDouble()*Math.PI-Math.PI;
				double rotB = rand.nextDouble()*Math.PI-Math.PI/2.0;
				double rotC = 2.0*rand.nextDouble()*Math.PI-Math.PI;

				matrixToEuler(type,rotA,rotB,rotC);
			}
		}
	}

	private void matrixToEuler(EulerType type , double rotA , double rotB , double rotC )
	{
		DenseMatrix64F expected = RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,rotA,rotB,rotC,null);

		double euler[] = RotationMatrixGenerator.matrixToEuler(expected,type,null);

		DenseMatrix64F found = RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ,euler[0],euler[1],euler[2],null);

		DenseMatrix64F difference = new DenseMatrix64F(3,3);
		CommonOps.multTransB(expected,found,difference);
		assertTrue(MatrixFeatures.isIdentity(difference,Math.sqrt(GrlConstants.DOUBLE_TEST_TOL)));
	}


	@Test
	public void matrixToEulerXYZ_F64() {
		// test case one
		DenseMatrix64F A = RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ, 0.1, -0.5, -0.96, null );
		double[] euler = RotationMatrixGenerator.matrixToEulerXYZ( A , (double[])null );
		DenseMatrix64F B = RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ, euler[0], euler[1], euler[2], null );

		assertTrue( MatrixFeatures.isIdentical( A, B, 1e-8 ) );

		// now try a pathological case
		A = RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ, 0.1, -0.5, 0, null );
		euler = RotationMatrixGenerator.matrixToEulerXYZ( A , (double[])null );
		B = RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ, euler[0], euler[1], euler[2], null );

		assertTrue( MatrixFeatures.isIdentical( A, B, 1e-8 ) );

		// try all zeros
		A = RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ, 0, 0, 0, null );
		euler = RotationMatrixGenerator.matrixToEulerXYZ( A , (double[])null );
		B = RotationMatrixGenerator.eulerToMatrix(EulerType.XYZ, euler[0], euler[1], euler[2], null );

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
	public void eulerToQuaternion() {
		fail("implement");
	}

	@Test
	public void eulerToMatrix() {
		fail("implement");
	}

	@Test
	public void eulerZyxToQuaternion() {
		double pid2 = Math.PI/2.0;
		eulerZyxToQuaternion(0,0,0);
		eulerZyxToQuaternion(pid2,0,0);
		eulerZyxToQuaternion(0,pid2,0);
		eulerZyxToQuaternion(0,0,pid2);
		eulerZyxToQuaternion(-pid2,0,0);
		eulerZyxToQuaternion(0,-pid2,0);
		eulerZyxToQuaternion(0,0,-pid2);

		for (int i = 0; i < 50; i++) {
			double a = Math.PI;
			double rotZ = 2*rand.nextDouble()*a - a;
			double rotY = 2*rand.nextDouble()*a - a;
			double rotX = 2*rand.nextDouble()*a - a;

			eulerZyxToQuaternion(rotZ,rotY,rotX);
		}
	}

	public void eulerZyxToQuaternion( double rotZ , double rotY , double rotX ) {
		DenseMatrix64F rotZyx = RotationMatrixGenerator.eulerToMatrix(EulerType.ZYX,rotZ,rotX,rotY,null);

		Quaternion_F64 quat = new Quaternion_F64();
		RotationMatrixGenerator.eulerZyxToQuaternion(rotZ,rotY,rotX,quat);

		DenseMatrix64F rotQuat = RotationMatrixGenerator.quaternionToMatrix(quat,null);

		DenseMatrix64F result = new DenseMatrix64F(3,3);
		CommonOps.multTransB(rotZyx,rotQuat,result);
		result.print();
		assertTrue(MatrixFeatures.isIdentity(result, Math.sqrt(GrlConstants.DOUBLE_TEST_TOL)));
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
		GeometryUnitTest.assertEquals( p, 0, 1, 0, GrlConstants.DOUBLE_TEST_TOL );


		// rotate around y-axis 90 degrees
		q = RotationMatrixGenerator.rodriguesToQuaternion( new Rodrigues_F64( Math.PI / 2.0, 0, 1, 0 ), null );
		q.normalize();

		R = RotationMatrixGenerator.quaternionToMatrix( q, R );

		p.set( 1, 0, 0 );
		GeometryMath_F64.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 0, -1, GrlConstants.DOUBLE_TEST_TOL );

		for (int i = 0; i < 30; i++) {
			Rodrigues_F64 rod = new Rodrigues_F64();
			rod.theta = Math.PI*(2*rand.nextDouble()-1);
			rod.setParamVector(rand.nextDouble()-0.5,rand.nextDouble()-0.5,rand.nextDouble()-0.5);
			rod.unitAxisRotation.normalize();

			q = RotationMatrixGenerator.rodriguesToQuaternion( rod, null );
			q.normalize();
			DenseMatrix64F expected = RotationMatrixGenerator.rodriguesToMatrix( rod, null );
			DenseMatrix64F found = RotationMatrixGenerator.quaternionToMatrix( q, null );

			DenseMatrix64F difference = new DenseMatrix64F(3,3);
			CommonOps.multTransB(expected,found,difference);
			assertTrue(MatrixFeatures.isIdentity(difference,GrlConstants.DOUBLE_TEST_TOL));
		}
	}

	@Test
	public void quaternionToMatrix_F32() {
		// rotate around z-axis 90 degrees
		Quaternion_F32 q = RotationMatrixGenerator.rodriguesToQuaternion( new Rodrigues_F32( GrlConstants.F_PId2, 0, 0, 1 ), null );

		DenseMatrix64F R = RotationMatrixGenerator.quaternionToMatrix( q, null );

		Point3D_F32 p = new Point3D_F32( 1, 0, 0 );
		GeometryMath_F32.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 1, 0, GrlConstants.FLOAT_TEST_TOL );


		// rotate around y-axis 90 degrees
		q = RotationMatrixGenerator.rodriguesToQuaternion( new Rodrigues_F32( GrlConstants.F_PId2, 0, 1, 0 ), null );
		q.normalize();

		R = RotationMatrixGenerator.quaternionToMatrix( q, R );

		p.set( 1, 0, 0 );
		GeometryMath_F32.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 0, -1, GrlConstants.FLOAT_TEST_TOL );
	}
}
